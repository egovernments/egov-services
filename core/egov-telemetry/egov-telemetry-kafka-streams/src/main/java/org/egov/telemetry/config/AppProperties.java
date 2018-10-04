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
    private Integer deDupStorageTime; //In Minutes


    public AppProperties() {
        kafkaBootstrapServerConfig = System.getenv("BOOTSTRAP_SERVER_CONFIG");
        telemetryRawInput = System.getenv("TELEMETRY_RAW_INPUT");
        telemetryValidatedMessages = System.getenv("TELEMETRY_VALIDATED_MESSAGES");
        telemetryDedupedMessages = System.getenv("TELEMETRY_DEDUPED_MESSAGES");
        telemetryFinalMessages = System.getenv("TELEMETRY_FINAL_MESSAGES");

        if(System.getenv("DEDUP_STORAGE_TIME") != null)
            deDupStorageTime = Integer.parseInt(System.getenv("DEDUPE_STORAGE_TIME"));

//        Properties properties = new Properties();
//        try {
//            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
//        } catch (IOException e) {
//            log.error("application.properties not found");
//        }
//
//        if(kafkaBootstrapServerConfig == null)
//          kafkaBootstrapServerConfig = properties.getProperty("BOOTSTRAP_SERVER_CONFIG");
//
//        if(telemetryRawInput == null)
//            telemetryRawInput = properties.getProperty("TELEMETRY_RAW_INPUT");
//
//        if(telemetryValidatedMessages == null)
//            telemetryValidatedMessages = properties.getProperty("TELEMETRY_VALIDATED_MESSAGES");
//
//        if(telemetryDedupedMessages == null)
//            telemetryDedupedMessages = properties.getProperty("TELEMETRY_DEDUPED_MESSAGES");
//
//        if(telemetryFinalMessages == null)
//            telemetryFinalMessages = properties.getProperty("TELEMETRY_FINAL_MESSAGES");
//
//        if(deDupStorageTime == null)
//            if(properties.getProperty("DEDUP_STORAGE_TIME") != null)
//                deDupStorageTime = Integer.parseInt(properties.getProperty("DEDUP_STORAGE_TIME"));

    }

}
