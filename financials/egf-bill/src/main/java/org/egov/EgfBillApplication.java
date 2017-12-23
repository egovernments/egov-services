package org.egov;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.tracer.config.TracerConfiguration;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Import({ TracerConfiguration.class })
@SpringBootApplication
public class EgfBillApplication {

    public static void main(final String[] args) {
        SpringApplication.run(EgfBillApplication.class, args);
    }

    private static final String CLUSTER_NAME = "cluster.name";

    @Value("${app.timezone}")
    private String timeZone;

    @Value("${es.host}")
    private String elasticSearchHost;

    @Value("${es.transport.port}")
    private Integer elasticSearchTransportPort;

    @Value("${es.cluster.name}")
    private String elasticSearchClusterName;

    private TransportClient client;

    @PostConstruct
    public void init() throws UnknownHostException {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        final Settings settings = Settings.builder().put(CLUSTER_NAME, elasticSearchClusterName).build();
        final InetAddress esAddress = InetAddress.getByName(elasticSearchHost);
        final InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(esAddress,
                elasticSearchTransportPort);
        client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TransportClient getTransportClient() {
        return client;
    }
}
