package config;


import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class AppProperties {

    private Long sessionTimeout;

    private String outputKafkaTopic;

    private String outputTelemetrySessionsIndex;

    private String esURL;
    private String esHost;
    private String esPort;
    private String esNodesWANOnly;

    private String inputTelemetryIndex;



    public AppProperties() {

        sessionTimeout = TimeUnit.MINUTES.toMillis(30);
        outputKafkaTopic = "batch-telemetry";
        outputTelemetrySessionsIndex = "batch-telemetry*/general/";
        esURL = "http://localhost:9200/";
        esHost = "localhost";
        esPort = "9200";
        esNodesWANOnly = "false";
        inputTelemetryIndex = "egovtelemetryreindex/general/";
    }

}
