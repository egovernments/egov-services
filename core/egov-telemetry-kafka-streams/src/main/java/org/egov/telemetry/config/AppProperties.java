package org.egov.telemetry.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;


@Slf4j
public class AppProperties {

    @Getter
    private String kafkaBootstrapServerConfig;
    @Getter
    private String telemetryRawInput;
    @Getter
    private String telemetryValidatedMessages;
    @Getter
    private String telemetryDedupedMessages;
    @Getter
    private String telemetryFinalMessages;
    @Getter
    private Integer deDupeStorageTime; //In Minutes


    public AppProperties() {
        kafkaBootstrapServerConfig = System.getenv("BOOTSTRAP_SERVER_CONFIG");
        telemetryRawInput = System.getenv("TELEMETRY_RAW_INPUT");
        telemetryValidatedMessages = System.getenv("TELEMETRY_VALIDATED_MESSAGES");
        telemetryDedupedMessages = System.getenv("TELEMETRY_DEDUPED_MESSAGES");
        telemetryFinalMessages = System.getenv("TELEMETRY_FINAL_MESSAGES");

        if(System.getenv("DEDUPE_DTORAGE_TIME") != null)
            deDupeStorageTime = Integer.parseInt(System.getenv("DEDUPE_STORAGE_TIME"));

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            log.error("application.properties not found");
        }

        if(kafkaBootstrapServerConfig == null)
          kafkaBootstrapServerConfig = properties.getProperty("kafka.config.bootstrap_server_config");

        if(telemetryRawInput == null)
            telemetryRawInput = properties.getProperty("kafka.telemetry.config.topics.telemetry_raw_input");

        if(telemetryValidatedMessages == null)
            telemetryValidatedMessages = properties.getProperty("kafka.telemetry.config.topics.telemetry_validated_messages");

        if(telemetryDedupedMessages == null)
            telemetryDedupedMessages = properties.getProperty("kafka.telemetry.config.topics.telemetry_deduped_messages");

        if(telemetryFinalMessages == null)
            telemetryFinalMessages = properties.getProperty("kafka.telemetry.config.topics.telemetry_final_messages");

        if(deDupeStorageTime == null)
            if(properties.getProperty("kafka.telemetry.config.dedupe_storage_time") != null)
                deDupeStorageTime = Integer.parseInt(properties.getProperty("kafka.telemetry.config.dedupe_storage_time"));

    }

}
