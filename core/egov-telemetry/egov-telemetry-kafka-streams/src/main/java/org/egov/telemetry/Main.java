package org.egov.telemetry;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.egov.telemetry.config.AppProperties;
import org.egov.telemetry.deduplicator.TelemetryDeduplicator;
import org.egov.telemetry.enrich.TelemetryEnrichMessages;
import org.egov.telemetry.formatchecker.TelemetryFormatChecker;
import org.egov.telemetry.sink.TelemetryFinalStream;
import org.egov.telemetry.unbundle.TelemetryUnbundleBatches;

import java.util.Properties;

public class Main {

    public static void main(String args[]) throws Exception {
        checkIfFilesExists();

        AppProperties appProperties = new AppProperties();

        Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, appProperties.getKafkaBootstrapServerConfig());
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        TelemetryFormatChecker telemetryFormatChecker = new TelemetryFormatChecker();
        telemetryFormatChecker.validateInputMessages(streamsConfiguration, appProperties.getTelemetryRawInput(), appProperties.getTelemetryValidatedMessages());

        TelemetryDeduplicator telemetryDeduplicator = new TelemetryDeduplicator();
        telemetryDeduplicator.shouldRemoveDuplicatesFromTheInput(streamsConfiguration, appProperties.getTelemetryValidatedMessages(), appProperties.getTelemetryDedupedMessages(), appProperties.getDeDupStorageTime());

        TelemetryUnbundleBatches telemetryUnbundleBatches = new TelemetryUnbundleBatches();
        telemetryUnbundleBatches.unbundleBatches(streamsConfiguration, appProperties.getTelemetryDedupedMessages(),
                appProperties.getTelemetryUnbundledMessages());

        TelemetryEnrichMessages telemetryEnrichMessages = new TelemetryEnrichMessages();
        telemetryEnrichMessages.enrichMessages(streamsConfiguration, appProperties.getTelemetryUnbundledMessages(),
                appProperties.getTelemetryEnrichedMessages());

        TelemetryFinalStream telemetryFinalStream = new TelemetryFinalStream();
        telemetryFinalStream.pushFinalMessages(streamsConfiguration, appProperties.getTelemetryEnrichedMessages(),
                appProperties.getTelemetryFinalMessages());
    }

    private static void checkIfFilesExists() throws Exception {
        if(Main.class.getClassLoader().getResource("application.properties") == null)
            throw new Exception("Properties File Not Found");
        if(Main.class.getClassLoader().getResource("telemetryMessageSchema.json") == null)
            throw new Exception("Schema File Not Found");
    }

}
