package com.novell.zenworks;

/**
 * Created by user on 3/6/2018.
 */
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;

import java.util.Properties;


public class KafkaAvroProducer {


    Properties props = new java.util.Properties();
    GenericRecord avroRecord;
    Schema schema;
    KafkaProducer producer;
    String key;

    public void init(String kafkaServer, String schemaRegistry) {


        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put("schema.registry.url", schemaRegistry);

        producer = new KafkaProducer(props);

        key = "key1";
        String userSchema = "{\"type\":\"record\"," +
                "\"name\":\"myrecord\"," +
                "\"fields\":[{\"name\":\"f1\",\"type\":\"string\"}]}";
        Schema.Parser parser = new Schema.Parser();
        schema = parser.parse(userSchema);
        avroRecord = new GenericData.Record(schema);
    }

    public void produce() {

        for(int i=0;i<100;i++) {

            avroRecord.put("f1", "value"+ i);

            ProducerRecord<Object, Object> record = new ProducerRecord<>("topic1", key, avroRecord);
            try {
                producer.send(record);
            } catch (SerializationException e) {
               System.out.println("Exception "+e.getMessage());
            }
        }
    }


}
