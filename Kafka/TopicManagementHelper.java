package com.novell.zenworks;

import kafka.admin.AdminUtils;
import kafka.admin.BrokerMetadata;
import kafka.cluster.Broker;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import kafka.zk.KafkaZkClient;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.*;
import org.apache.kafka.common.internals.KafkaFutureImpl;
import org.apache.kafka.common.utils.Time;
import scala.Tuple2;
import scala.collection.Map;
import scala.collection.Seq;
import scala.collection.mutable.HashMap;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

/**
 * Created by user on 6/20/2018.
 */
public  class TopicManagementHelper {

    public static void main(String[] args){

        System.out.println("Getting topic config");
        String topic = "topic-4";
        ZkUtils zkUtils = ZkUtils.apply("10.71.70.114:2181", 30000,30000,false);
        List<TopicPartitionInfo> result;
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "10.71.64.144:9092");
        props.put(AdminClientConfig.CLIENT_ID_CONFIG, "reassign-partitions-tool");

        AdminClient adminClient= AdminClient.create(props);

        Collection<Broker> brokers = scala.collection.JavaConversions.asJavaCollection(zkUtils.getAllBrokersInCluster());
        //This partition info contains only replicas
        List<PartitionInfo> partitionInfoList = getPartitionInfo(zkUtils,topic);

        //This Topic partition info contains leader, isr and replicas for a given topic partition. Use this to get leader and isr for topic/partition
        try {
            result = getTopicPartitionInfo(topic,adminClient);
            System.out.println(result.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int currentReplicationFactor = getReplicationFactor(partitionInfoList);
        System.out.println("Current replication factor  "+ currentReplicationFactor);

        Properties currentProperties = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topic);

        KafkaZkClient kafkaZkClient = KafkaZkClient.apply("10.71.70.114:2181",false,5000,5000, Integer.MAX_VALUE, Time.SYSTEM,"kafka.server","SessionExpireListener");

        if (kafkaZkClient.reassignPartitionsInProgress()) {
            System.out.println("There is an existing assignment running.");
            return;
        }

        reassignPartitions(kafkaZkClient,brokers,topic,1,3);

        scala.collection.mutable.ArrayBuffer<String> topicList = new scala.collection.mutable.ArrayBuffer<>();
        topicList.$plus$eq(topic);
        scala.collection.Map<Object, scala.collection.Seq<Object>> currentAssignment = zkUtils.getPartitionAssignmentForTopics(topicList).apply(topic);
        String currentAssignmentJson = formatAsReassignmentJson(topic, currentAssignment);
        System.out.println("Current partition replica assignment after update" + currentAssignmentJson);

        System.out.println("Current topic config "+ currentProperties);
        kafkaZkClient.close();
    }

    private static List<TopicPartitionInfo> getTopicPartitionInfo(String topic, AdminClient client) throws ExecutionException, InterruptedException {
        List<TopicPartitionInfo> topicPartitionInfoList =null;
        Collection<String> topics = new ArrayList<>();
        ((ArrayList<String>) topics).add(topic);
        DescribeTopicsResult result = client.describeTopics(topics);
        Iterator<java.util.Map.Entry<String, KafkaFuture<TopicDescription>>> resultList = result.values().entrySet().iterator();
        while (resultList.hasNext()){
        java.util.Map.Entry<String, KafkaFuture<TopicDescription>> currentEntry = resultList.next();
        KafkaFuture<TopicDescription> kfTopicdescription = currentEntry.getValue();
        TopicDescription topicDescription = kfTopicdescription.get();
        topicPartitionInfoList = topicDescription.partitions();
        break;
    }

        //topicPartitionInfo = ((KafkaFutureImpl)((HashMap.Node)((HashMap)result.values()).entrySet().toArray()[0]).getValue()).value;
        return topicPartitionInfoList;
    }

    private static List<PartitionInfo> getPartitionInfo(ZkUtils zkUtils, String topic) {
        scala.collection.mutable.ArrayBuffer<String> topicList = new scala.collection.mutable.ArrayBuffer<>();
        topicList.$plus$eq(topic);
        scala.collection.Map<Object, scala.collection.Seq<Object>> partitionAssignments =
                zkUtils.getPartitionAssignmentForTopics(topicList).apply(topic);
        List<PartitionInfo> partitionInfoList = new ArrayList<>();
        scala.collection.Iterator<scala.Tuple2<Object, scala.collection.Seq<Object>>> it = partitionAssignments.iterator();
        while (it.hasNext()) {
            scala.Tuple2<Object, scala.collection.Seq<Object>> scalaTuple = it.next();
            Integer partition = (Integer) scalaTuple._1();
            scala.Option<Object> leaderOption = zkUtils.getLeaderForPartition(topic, partition);
            Node leader = leaderOption.isEmpty() ?  null : new Node((Integer) leaderOption.get(), "", -1);
            Node[] replicas = new Node[scalaTuple._2().size()];
            for (int i = 0; i < replicas.length; i++) {
                Integer brokerId = (Integer) scalaTuple._2().apply(i);
                replicas[i] = new Node(brokerId, "", -1);
            }
            partitionInfoList.add(new PartitionInfo(topic, partition, leader, replicas, null));
        }



        return partitionInfoList;
    }

    static int getReplicationFactor(List<PartitionInfo> partitionInfoList) {
        if (partitionInfoList.isEmpty())
            throw new RuntimeException("Partition list is empty");

        int replicationFactor = partitionInfoList.get(0).replicas().length;
        for (PartitionInfo partitionInfo : partitionInfoList) {
            if (replicationFactor != partitionInfo.replicas().length) {
                String topic = partitionInfoList.get(0).topic();
                System.out.println("Partitions of the topic " + topic + " have different replication factor");
                return -1;
            }
        }
        return replicationFactor;
    }

    @SuppressWarnings("unchecked")
    private static <K, V> scala.collection.immutable.Map<K, V> toScalaImmutableMap(java.util.Map<K, V> javaMap) {
        final java.util.List<scala.Tuple2<K, V>> list = new java.util.ArrayList<>(javaMap.size());
        for (final java.util.Map.Entry<K, V> entry : javaMap.entrySet()) {
            list.add(scala.Tuple2.apply(entry.getKey(), entry.getValue()));
        }
        final scala.collection.Seq<Tuple2<K, V>> seq = scala.collection.JavaConverters.asScalaBufferConverter(list).asScala().toSeq();
        return (scala.collection.immutable.Map<K, V>) scala.collection.immutable.Map$.MODULE$.apply(seq);
    }

    private static void reassignPartitions(KafkaZkClient kafkaZkClient,Collection<Broker> brokers, String topic, int partitionCount, int replicationFactor) {

        scala.collection.mutable.ArrayBuffer<BrokerMetadata> brokersMetadata = new scala.collection.mutable.ArrayBuffer<>(brokers.size());
        for (Broker broker : brokers) {
            brokersMetadata.$plus$eq(new BrokerMetadata(broker.id(), broker.rack()));
        }

        //Get the new assign scheme for the topic/partition
        scala.collection.Map<Object, Seq<Object>> newAssignment =
                AdminUtils.assignReplicasToBrokers(brokersMetadata, partitionCount, replicationFactor, 0, 0);
        String newAssignmentJson = formatAsReassignmentJson(topic, newAssignment);

        System.out.println("New partition replica assignment " + newAssignmentJson);



        //val replicaAssignment = mutable.Map.empty[TopicPartitionReplica, String]

        java.util.Map<TopicPartitionReplica,String> replicaAssignment = new java.util.HashMap<>();
        java.util.Map<TopicPartition,Seq<Object>> validPartitions = new java.util.HashMap<>();
        scala.collection.immutable.Map<TopicPartition,Seq<Object>> partitionAssignmentImmutable=null;


        Iterator partitions = scala.collection.JavaConversions.asJavaCollection(newAssignment.keys()).iterator();

        while(partitions.hasNext()) {
            int partition = (Integer) partitions.next();
            TopicPartition topicPartition = new TopicPartition(topic,partition);

            Seq<Object> replicaCollection = newAssignment.get(partition).get();
            validPartitions.put(topicPartition,replicaCollection);
            //Tuple2<TopicPartition,Seq<Object>> tuple=new Tuple2<TopicPartition,Seq<Object>>(topicPartition,newAssignment.get(partition).get());


            //This step is needed only if we are changing log dirs commenting out for now
            /*for (Broker broker : brokers) {
                TopicPartitionReplica partitionReplica = new TopicPartitionReplica(topic, partition,broker.id() );
                //String replicas = replicaCollection.toString();
                replicaAssignment.put(partitionReplica,"/tmp/kafka-logs");
                //System.out.print(replicas);
            }*/
        }
       partitionAssignmentImmutable = toScalaImmutableMap(validPartitions);

        //This step is needed only if we are changing the log dir
        /*AlterReplicaLogDirsResult logDirsResult = adminClient.alterReplicaLogDirs(replicaAssignment);

        java.util.Map<TopicPartitionReplica, KafkaFuture<Void>> response=  logDirsResult.values();
        for (java.util.Map.Entry<TopicPartitionReplica, KafkaFuture<Void>> entry : response.entrySet()) {
            try {
                entry.getValue().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
*/

        kafkaZkClient.createPartitionReassignment(partitionAssignmentImmutable);
    }

    private static String formatAsReassignmentJson(String topic, scala.collection.Map<Object, Seq<Object>> partitionsToBeReassigned) {
        StringBuilder bldr = new StringBuilder();
        bldr.append("{\"version\":1,\"partitions\":[\n");
        for (int partition = 0; partition < partitionsToBeReassigned.size(); partition++) {
            bldr.append("  {\"topic\":\"").append(topic).append("\",\"partition\":").append(partition).append(",\"replicas\":[");
            scala.collection.Seq<Object> replicas = partitionsToBeReassigned.apply(partition);
            for (int replicaIndex = 0; replicaIndex < replicas.size(); replicaIndex++) {
                Object replica = replicas.apply(replicaIndex);
                bldr.append(replica).append(",");
            }
            bldr.setLength(bldr.length() - 1);
            bldr.append("]},\n");
        }
        bldr.setLength(bldr.length() - 2);
        bldr.append("]}");
        return bldr.toString();
    }





//    private final boolean _topicCreationEnabled;
//    private final String _topic;
//    private final String _zkConnect;
//    private final int _replicationFactor;
//    private final double _minPartitionsToBrokersRatio;
//    private final int _minPartitionNum;
//    private final TopicFactory _topicFactory;
//    private final Properties _topicProperties;
//
//    TopicManagementHelper(Map<String, Object> props) throws Exception {
//        TopicManagementServiceConfig config = new TopicManagementServiceConfig(props);
//        _topicCreationEnabled = config.getBoolean(TopicManagementServiceConfig.TOPIC_CREATION_ENABLED_CONFIG);
//        _topic = config.getString(TopicManagementServiceConfig.TOPIC_CONFIG);
//        _zkConnect = config.getString(TopicManagementServiceConfig.ZOOKEEPER_CONNECT_CONFIG);
//        _replicationFactor = config.getInt(TopicManagementServiceConfig.TOPIC_REPLICATION_FACTOR_CONFIG);
//        _minPartitionsToBrokersRatio = config.getDouble(TopicManagementServiceConfig.PARTITIONS_TO_BROKERS_RATIO_CONFIG);
//        _minPartitionNum = config.getInt(TopicManagementServiceConfig.MIN_PARTITION_NUM_CONFIG);
//        String topicFactoryClassName = config.getString(TopicManagementServiceConfig.TOPIC_FACTORY_CLASS_CONFIG);
//
//        _topicProperties = new Properties();
//        if (props.containsKey(TopicManagementServiceConfig.TOPIC_PROPS_CONFIG)) {
//            for (Map.Entry<String, Object> entry: ((Map<String, Object>) props.get(TopicManagementServiceConfig.TOPIC_PROPS_CONFIG)).entrySet())
//                _topicProperties.put(entry.getKey(), entry.getValue().toString());
//        }
//
//        Map topicFactoryConfig = props.containsKey(TopicManagementServiceConfig.TOPIC_FACTORY_PROPS_CONFIG) ?
//                (Map) props.get(TopicManagementServiceConfig.TOPIC_FACTORY_PROPS_CONFIG) : new HashMap();
//        _topicFactory = (TopicFactory) Class.forName(topicFactoryClassName).getConstructor(Map.class).newInstance(topicFactoryConfig);
//    }
//
//    void maybeCreateTopic() throws Exception {
//        if (_topicCreationEnabled) {
//            _topicFactory.createTopicIfNotExist(_zkConnect, _topic, _replicationFactor, _minPartitionsToBrokersRatio, _topicProperties);
//        }
//    }
//
//    int minPartitionNum() {
//        int brokerCount = Utils.getBrokerCount(_zkConnect);
//        return Math.max((int) Math.ceil(_minPartitionsToBrokersRatio * brokerCount), _minPartitionNum);
//    }
//
//    void maybeAddPartitions(int minPartitionNum) {
//        ZkUtils zkUtils = ZkUtils.apply(_zkConnect, ZK_SESSION_TIMEOUT_MS, ZK_CONNECTION_TIMEOUT_MS, JaasUtils.isZkSecurityEnabled());
//        try {
//            int partitionNum = getPartitionInfo(zkUtils, _topic).size();
//            if (partitionNum < minPartitionNum) {
//                LOG.info("MultiClusterTopicManagementService will increase partition of the topic {} "
//                        + "in cluster {} from {} to {}.", _topic, _zkConnect, partitionNum, minPartitionNum);
//                AdminUtils.addPartitions(zkUtils, _topic, minPartitionNum, null, false, RackAwareMode.Enforced$.MODULE$);
//            }
//        } finally {
//            zkUtils.close();
//        }
//    }
//
//    void maybeReassignPartitionAndElectLeader() throws Exception {
//        ZkUtils zkUtils = ZkUtils.apply(_zkConnect, ZK_SESSION_TIMEOUT_MS, ZK_CONNECTION_TIMEOUT_MS, JaasUtils.isZkSecurityEnabled());
//
//        try {
//            List<PartitionInfo> partitionInfoList = getPartitionInfo(zkUtils, _topic);
//            Collection<Broker> brokers = scala.collection.JavaConversions.asJavaCollection(zkUtils.getAllBrokersInCluster());
//
//            if (partitionInfoList.size() == 0)
//                throw new IllegalStateException("Topic " + _topic + " does not exist in cluster " + _zkConnect);
//
//            int currentReplicationFactor = getReplicationFactor(partitionInfoList);
//            int expectedReplicationFactor = Math.max(currentReplicationFactor, _replicationFactor);
//
//            if (_replicationFactor < currentReplicationFactor)
//                LOG.debug("Configured replication factor {} is smaller than the current replication factor {} of the topic {} in cluster {}",
//                        _replicationFactor, currentReplicationFactor, _topic, _zkConnect);
//
//            if (expectedReplicationFactor > currentReplicationFactor && zkUtils.getPartitionsBeingReassigned().isEmpty()) {
//                LOG.info("MultiClusterTopicManagementService will increase the replication factor of the topic {} in cluster {}"
//                        + "from {} to {}", _topic, _zkConnect, currentReplicationFactor, expectedReplicationFactor);
//                reassignPartitions(zkUtils, brokers, _topic, partitionInfoList.size(), expectedReplicationFactor);
//            }
//
//            // Update the properties of the monitor topic if any config is different from the user-specified config
//            Properties currentProperties = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), _topic);
//            Properties expectedProperties = new Properties();
//            for (Object key: currentProperties.keySet())
//                expectedProperties.put(key, currentProperties.get(key));
//            for (Object key: _topicProperties.keySet())
//                expectedProperties.put(key, _topicProperties.get(key));
//
//            if (!currentProperties.equals(expectedProperties)) {
//                LOG.info("MultiClusterTopicManagementService will overwrite properties of the topic {} "
//                        + "in cluster {} from {} to {}.", _topic, _zkConnect, currentProperties, expectedProperties);
//                AdminUtils.changeTopicConfig(zkUtils, _topic, expectedProperties);
//            }
//
//            if (partitionInfoList.size() >= brokers.size() &&
//                    someBrokerNotPreferredLeader(partitionInfoList, brokers) &&
//                    zkUtils.getPartitionsBeingReassigned().isEmpty()) {
//                LOG.info("MultiClusterTopicManagementService will reassign partitions of the topic {} in cluster {}", _topic, _zkConnect);
//                reassignPartitions(zkUtils, brokers, _topic, partitionInfoList.size(), expectedReplicationFactor);
//            }
//
//            if (partitionInfoList.size() >= brokers.size() &&
//                    someBrokerNotElectedLeader(partitionInfoList, brokers)) {
//                LOG.info("MultiClusterTopicManagementService will trigger preferred leader election for the topic {} in cluster {}", _topic, _zkConnect);
//                triggerPreferredLeaderElection(zkUtils, partitionInfoList);
//            }
//        } finally {
//            zkUtils.close();
//        }
//    }
//
//    private static void triggerPreferredLeaderElection(ZkUtils zkUtils, List<PartitionInfo> partitionInfoList) {
//        scala.collection.mutable.HashSet<TopicAndPartition> scalaPartitionInfoSet = new scala.collection.mutable.HashSet<>();
//        for (PartitionInfo javaPartitionInfo : partitionInfoList) {
//            scalaPartitionInfoSet.add(new TopicAndPartition(javaPartitionInfo.topic(), javaPartitionInfo.partition()));
//        }
//        PreferredReplicaLeaderElectionCommand.writePreferredReplicaElectionData(zkUtils, scalaPartitionInfoSet);
//    }
//
//    private static void reassignPartitions(ZkUtils zkUtils, Collection<Broker> brokers, String topic, int partitionCount, int replicationFactor) {
//        scala.collection.mutable.ArrayBuffer<BrokerMetadata> brokersMetadata = new scala.collection.mutable.ArrayBuffer<>(brokers.size());
//        for (Broker broker : brokers) {
//            brokersMetadata.$plus$eq(new BrokerMetadata(broker.id(), broker.rack()));
//        }
//        scala.collection.Map<Object, Seq<Object>> newAssignment =
//                AdminUtils.assignReplicasToBrokers(brokersMetadata, partitionCount, replicationFactor, 0, 0);
//
//        scala.collection.mutable.ArrayBuffer<String> topicList = new scala.collection.mutable.ArrayBuffer<>();
//        topicList.$plus$eq(topic);
//        scala.collection.Map<Object, scala.collection.Seq<Object>> currentAssignment = zkUtils.getPartitionAssignmentForTopics(topicList).apply(topic);
//        String currentAssignmentJson = formatAsReassignmentJson(topic, currentAssignment);
//        String newAssignmentJson = formatAsReassignmentJson(topic, newAssignment);
//
//        LOG.info("Reassign partitions for topic " + topic);
//        LOG.info("Current partition replica assignment " + currentAssignmentJson);
//        LOG.info("New partition replica assignment " + newAssignmentJson);
//        zkUtils.createPersistentPath(ZkUtils.ReassignPartitionsPath(), newAssignmentJson, zkUtils.DefaultAcls());
//    }
//
//    private static List<PartitionInfo> getPartitionInfo(ZkUtils zkUtils, String topic) {
//        scala.collection.mutable.ArrayBuffer<String> topicList = new scala.collection.mutable.ArrayBuffer<>();
//        topicList.$plus$eq(topic);
//        scala.collection.Map<Object, scala.collection.Seq<Object>> partitionAssignments =
//                zkUtils.getPartitionAssignmentForTopics(topicList).apply(topic);
//        List<PartitionInfo> partitionInfoList = new ArrayList<>();
//        scala.collection.Iterator<scala.Tuple2<Object, scala.collection.Seq<Object>>> it = partitionAssignments.iterator();
//        while (it.hasNext()) {
//            scala.Tuple2<Object, scala.collection.Seq<Object>> scalaTuple = it.next();
//            Integer partition = (Integer) scalaTuple._1();
//            scala.Option<Object> leaderOption = zkUtils.getLeaderForPartition(topic, partition);
//            Node leader = leaderOption.isEmpty() ?  null : new Node((Integer) leaderOption.get(), "", -1);
//            Node[] replicas = new Node[scalaTuple._2().size()];
//            for (int i = 0; i < replicas.length; i++) {
//                Integer brokerId = (Integer) scalaTuple._2().apply(i);
//                replicas[i] = new Node(brokerId, "", -1);
//            }
//            partitionInfoList.add(new PartitionInfo(topic, partition, leader, replicas, null));
//        }
//
//        return partitionInfoList;
//    }
//
//    static int getReplicationFactor(List<PartitionInfo> partitionInfoList) {
//        if (partitionInfoList.isEmpty())
//            throw new RuntimeException("Partition list is empty");
//
//        int replicationFactor = partitionInfoList.get(0).replicas().length;
//        for (PartitionInfo partitionInfo : partitionInfoList) {
//            if (replicationFactor != partitionInfo.replicas().length) {
//                String topic = partitionInfoList.get(0).topic();
//                LOG.warn("Partitions of the topic " + topic + " have different replication factor");
//                return -1;
//            }
//        }
//        return replicationFactor;
//    }
//
//    static boolean someBrokerNotPreferredLeader(List<PartitionInfo> partitionInfoList, Collection<Broker> brokers) {
//        Set<Integer> brokersNotPreferredLeader = new HashSet<>(brokers.size());
//        for (Broker broker: brokers)
//            brokersNotPreferredLeader.add(broker.id());
//        for (PartitionInfo partitionInfo : partitionInfoList)
//            brokersNotPreferredLeader.remove(partitionInfo.replicas()[0].id());
//
//        return !brokersNotPreferredLeader.isEmpty();
//    }
//
//    static boolean someBrokerNotElectedLeader(List<PartitionInfo> partitionInfoList, Collection<Broker> brokers) {
//        Set<Integer> brokersNotElectedLeader = new HashSet<>(brokers.size());
//        for (Broker broker: brokers)
//            brokersNotElectedLeader.add(broker.id());
//        for (PartitionInfo partitionInfo : partitionInfoList) {
//            if (partitionInfo.leader() != null)
//                brokersNotElectedLeader.remove(partitionInfo.leader().id());
//        }
//        return !brokersNotElectedLeader.isEmpty();
//    }
//
//    /**
//     * @param topic topic
//     * @param partitionsToBeReassigned a map from partition (int) to replica list (int seq)
//     *
//     * @return a json string with the same format as output of kafka.utils.ZkUtils.formatAsReassignmentJson
//     *
//     * Example:
//     * <pre>
//     *   {"version":1,"partitions":[
//     *     {"topic":"kmf-topic","partition":1,"replicas":[0,1]},
//     *     {"topic":"kmf-topic","partition":2,"replicas":[1,2]},
//     *     {"topic":"kmf-topic","partition":0,"replicas":[2,0]}]}
//     * </pre>
//     */
//    private static String formatAsReassignmentJson(String topic, scala.collection.Map<Object, Seq<Object>> partitionsToBeReassigned) {
//        StringBuilder bldr = new StringBuilder();
//        bldr.append("{\"version\":1,\"partitions\":[\n");
//        for (int partition = 0; partition < partitionsToBeReassigned.size(); partition++) {
//            bldr.append("  {\"topic\":\"").append(topic).append("\",\"partition\":").append(partition).append(",\"replicas\":[");
//            scala.collection.Seq<Object> replicas = partitionsToBeReassigned.apply(partition);
//            for (int replicaIndex = 0; replicaIndex < replicas.size(); replicaIndex++) {
//                Object replica = replicas.apply(replicaIndex);
//                bldr.append(replica).append(",");
//            }
//            bldr.setLength(bldr.length() - 1);
//            bldr.append("]},\n");
//        }
//        bldr.setLength(bldr.length() - 2);
//        bldr.append("]}");
//        return bldr.toString();
//    }
//
//}
//}
}