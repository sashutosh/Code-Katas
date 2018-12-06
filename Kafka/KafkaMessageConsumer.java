package com.novell.zenworks;/*
 * © 2017 Micro Focus Software Inc. All rights reserved
 *
 *   THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
 *   AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS  TO  THIS  WORK IS
 *   RESTRICTED TO (I) NOVELL, INC.  EMPLOYEES WHO HAVE A NEED TO  KNOW HOW
 *   TO  PERFORM  TASKS WITHIN  THE SCOPE  OF  THEIR   ASSIGNMENTS AND (II)
 *   ENTITIES OTHER  THAN  NOVELL, INC.  WHO  HAVE ENTERED INTO APPROPRIATE
 *   LICENSE   AGREEMENTS.  NO  PART  OF  THIS WORK MAY BE USED, PRACTICED,
 *   PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
 *   CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED,  RECAST, TRANSFORMED
 *   OR ADAPTED  WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC.  ANY USE
 *   OR EXPLOITATION  OF  THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
 *   PERPETRATOR  TO CRIMINAL AND CIVIL LIABILITY.
 */

/*
 * © 2017 Micro Focus Software Inc. All rights reserved
 *
 *   THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
 *   AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS  TO  THIS  WORK IS
 *   RESTRICTED TO (I) NOVELL, INC.  EMPLOYEES WHO HAVE A NEED TO  KNOW HOW
 *   TO  PERFORM  TASKS WITHIN  THE SCOPE  OF  THEIR   ASSIGNMENTS AND (II)
 *   ENTITIES OTHER  THAN  NOVELL, INC.  WHO  HAVE ENTERED INTO APPROPRIATE
 *   LICENSE   AGREEMENTS.  NO  PART  OF  THIS WORK MAY BE USED, PRACTICED,
 *   PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
 *   CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED,  RECAST, TRANSFORMED
 *   OR ADAPTED  WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC.  ANY USE
 *   OR EXPLOITATION  OF  THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
 *   PERPETRATOR  TO CRIMINAL AND CIVIL LIABILITY.
 */

/*
 * © 2017 Micro Focus Software Inc. All rights reserved
 *
 *   THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
 *   AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS  TO  THIS  WORK IS
 *   RESTRICTED TO (I) NOVELL, INC.  EMPLOYEES WHO HAVE A NEED TO  KNOW HOW
 *   TO  PERFORM  TASKS WITHIN  THE SCOPE  OF  THEIR   ASSIGNMENTS AND (II)
 *   ENTITIES OTHER  THAN  NOVELL, INC.  WHO  HAVE ENTERED INTO APPROPRIATE
 *   LICENSE   AGREEMENTS.  NO  PART  OF  THIS WORK MAY BE USED, PRACTICED,
 *   PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
 *   CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED,  RECAST, TRANSFORMED
 *   OR ADAPTED  WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC.  ANY USE
 *   OR EXPLOITATION  OF  THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
 *   PERPETRATOR  TO CRIMINAL AND CIVIL LIABILITY.
 */

/*
 * © 2017 Micro Focus Software Inc. All rights reserved
 *
 *   THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
 *   AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS  TO  THIS  WORK IS
 *   RESTRICTED TO (I) NOVELL, INC.  EMPLOYEES WHO HAVE A NEED TO  KNOW HOW
 *   TO  PERFORM  TASKS WITHIN  THE SCOPE  OF  THEIR   ASSIGNMENTS AND (II)
 *   ENTITIES OTHER  THAN  NOVELL, INC.  WHO  HAVE ENTERED INTO APPROPRIATE
 *   LICENSE   AGREEMENTS.  NO  PART  OF  THIS WORK MAY BE USED, PRACTICED,
 *   PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
 *   CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED,  RECAST, TRANSFORMED
 *   OR ADAPTED  WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC.  ANY USE
 *   OR EXPLOITATION  OF  THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
 *   PERPETRATOR  TO CRIMINAL AND CIVIL LIABILITY.
 */

/*
 * © 2017 Micro Focus Software Inc. All rights reserved
 *
 *   THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
 *   AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS  TO  THIS  WORK IS
 *   RESTRICTED TO (I) NOVELL, INC.  EMPLOYEES WHO HAVE A NEED TO  KNOW HOW
 *   TO  PERFORM  TASKS WITHIN  THE SCOPE  OF  THEIR   ASSIGNMENTS AND (II)
 *   ENTITIES OTHER  THAN  NOVELL, INC.  WHO  HAVE ENTERED INTO APPROPRIATE
 *   LICENSE   AGREEMENTS.  NO  PART  OF  THIS WORK MAY BE USED, PRACTICED,
 *   PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
 *   CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED,  RECAST, TRANSFORMED
 *   OR ADAPTED  WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC.  ANY USE
 *   OR EXPLOITATION  OF  THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
 *   PERPETRATOR  TO CRIMINAL AND CIVIL LIABILITY.
 */




import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by user on 7/10/2017.
 */
public class KafkaMessageConsumer {

    private KafkaConsumer consumer;
    private List<IObserver> observers;
    private ConsumerLoop consumerReader;
    private List<String> subscribedTopics;
    private String tenantName;
    private String eventType;
    private String topicName;

    /***
     * Creates an instance of Kafka consumer with given configuration
     * @param brokerList - List of brokers
     * @param groupId - The group to which this consumer belongs
     * @param keyDeserializer - Deserialization class for the message key
     * @param valueDeserializer - Deserialization class for message value
     */
    public void init(String brokerList, String groupId, String keyDeserializer, String valueDeserializer, String tenantName,String eventType ){

        observers = new ArrayList<>();
        //String brokers = String.join( ",",brokerList);
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerList);
        props.put("group.id", groupId);
        props.put("auto.offset.reset","earliest");
        //Setting auto comit to false to ensure that on processing failure we retry the read
        props.put("auto.commit.offset", "false");
        props.put("key.deserializer", keyDeserializer);
        props.put("value.deserializer", valueDeserializer);
        props.put("schema.registry.url", "http://10.71.70.114:8081");
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        this.tenantName=tenantName;
        this.eventType=eventType;
        consumer= new KafkaConsumer(props);
        consumerReader= new ConsumerLoop(this);
    }

    /***
     *
     * @return the kafka consumer instance
     */

    public KafkaConsumer getConsumer(){
        return consumer;
    }


    public String getTopicName() {
        return topicName;
    }

    /***
     *
     * @param topic - The topic which this consumer wants to subscribe to
     */

    public  void subscribeToTopic(String topic){

        this.topicName = topic;
        consumer.subscribe(Collections.singletonList(topic));
    }

    public  void subscribeToTopic(String[] topics){

        this.subscribedTopics =Arrays.asList(topics);
        consumer.subscribe(Arrays.asList(topics));
    }

    public List<String> getSubscribedTopics(){
        return  this.subscribedTopics;
    }


    /***
     *
     * @param observer - The clients which wants to be notified when a message is received
     */

    public void addObservers(IObserver observer){
        observers.add(observer);
    }

    /***
     *
     * @return Objects which are subscribed to this consumer's message stream
     */

    public List<IObserver> getObservers(){
        return observers;
    }

    /***
     *
     * @return The tenant to which this data belongs
     */
    public String getTenantName() {
        return tenantName;
    }

    /***
     *
     * @return event type which will be sent to consumer.
     */

    public String getEventType() {
        return eventType;
    }
    /***
     * Starts a thread that poll kafka topic for reading data;
     */
    public void startConsumer(){
        //ExecutorService executor = Executors.newFixedThreadPool(1);
        //executor.submit(consumerReader);
        Thread thread = new Thread(consumerReader);
        thread.start();
    }

    /***
     * Causes the reader thread to exit
     */

    public void shutdown(){
        consumerReader.shutdown();
    }
}
