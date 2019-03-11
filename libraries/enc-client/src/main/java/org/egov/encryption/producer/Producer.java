package org.egov.encryption.producer;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.egov.encryption.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class Producer {

    @Autowired
    private AppProperties appProperties;

    private KafkaProducer kafkaProducer;

    public Producer() {
        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appProperties.getKafkaBootstrapServerConfig());

        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.connect.json.JsonSerializer");

        kafkaProducer = new KafkaProducer(configProperties);

    }

    public void push(String topic, String key, Long timestamp, JsonNode data) {
        ProducerRecord<String, JsonNode> record = new ProducerRecord<>(topic, null, timestamp, key, data);
        kafkaProducer.send(record);
    }

}
