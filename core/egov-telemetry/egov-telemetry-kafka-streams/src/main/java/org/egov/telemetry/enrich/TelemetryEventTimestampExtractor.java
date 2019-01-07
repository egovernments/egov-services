package org.egov.telemetry.enrich;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.json.JSONObject;

@Slf4j
public class TelemetryEventTimestampExtractor implements TimestampExtractor {

    Configuration configuration;

    public TelemetryEventTimestampExtractor() {
        configuration = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL,
                Option.SUPPRESS_EXCEPTIONS);
    }

    private Long getTimestamp(JSONObject jsonObject) {
        return jsonObject.getLong("ets");
    }

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long previousTimestamp) {
        JSONObject jsonObject = new JSONObject( (String) record.value());
        return getTimestamp(jsonObject);
    }

}
