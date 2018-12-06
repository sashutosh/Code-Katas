package com.novell.zenworks;

/**
 * Created by user on 3/6/2018.
 */
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.IndexedRecord;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import io.confluent.kafka.serializers.KafkaAvroDecoder;
import kafka.message.MessageAndMetadata;
import kafka.utils.VerifiableProperties;
import org.apache.kafka.common.errors.SerializationException;
import java.util.*;


public class KafkaAvroConsumer {

    ConsumerConnector consumer;
    Map<String, Integer> topicCountMap = new HashMap<>();
    KafkaAvroDecoder keyDecoder;
    KafkaAvroDecoder valueDecoder;
    String topic = "topic1";

    public void init(String zkUrl, String schemaRegistryUrl){
        Properties props = new Properties();
        props.put("zookeeper.connect", zkUrl);
        props.put("group.id", "group1");
        props.put("schema.registry.url", schemaRegistryUrl);

        topicCountMap.put(topic, new Integer(1));

        VerifiableProperties vProps = new VerifiableProperties(props);
        keyDecoder = new KafkaAvroDecoder(vProps);
        valueDecoder = new KafkaAvroDecoder(vProps);
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

    }

    public void consume(){



        Map<String, List<KafkaStream<Object, Object>>> consumerMap = consumer.createMessageStreams(
                topicCountMap, keyDecoder, valueDecoder);
        KafkaStream stream = consumerMap.get(topic).get(0);
        ConsumerIterator it = stream.iterator();
        while (it.hasNext()) {
            MessageAndMetadata messageAndMetadata = it.next();
            try {
                String key = (String) messageAndMetadata.key();
                IndexedRecord value = (IndexedRecord) messageAndMetadata.message();
                System.out.println("Key "+key + " Value- "+ value );

            } catch(SerializationException e) {
                // may need to do something with it
            }
        }
    }
}
