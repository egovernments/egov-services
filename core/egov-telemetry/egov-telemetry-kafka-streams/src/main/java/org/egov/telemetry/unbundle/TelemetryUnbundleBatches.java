package org.egov.telemetry.unbundle;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TelemetryUnbundleBatches {

    public void unbundleBatches(Properties streamsConfiguration, String inputTopic, String outputTopic) {
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "message-unbundle");
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> inputStream = builder.stream(inputTopic);

        inputStream.flatMapValues(new ValueMapper<String, Iterable<?>>() {
            @Override
            public Iterable<?> apply(String value) {
                JSONObject jsonObject = new JSONObject(value);
                JSONArray jsonArray = jsonObject.getJSONArray("events");
                List<String> events = new ArrayList<>();
                for(Object object : jsonArray) {
                    JSONObject jsonObject1 = (JSONObject) object;
                    events.add(jsonObject1.toString());
                }
                return events;
            }
        }).to(outputTopic);


        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);
        streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

}
