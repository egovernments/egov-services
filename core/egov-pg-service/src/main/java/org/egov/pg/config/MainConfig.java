package org.egov.pg.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class MainConfig {


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate(getClientHttpRequestFactory());
//    }

    @Autowired
    @Bean
    public JdbcTemplate template(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

//    private ClientHttpRequestFactory getClientHttpRequestFactory() {
//        int timeout = 5000;
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
//                = new HttpComponentsClientHttpRequestFactory();
//        clientHttpRequestFactory.setConnectTimeout(timeout);
//        return clientHttpRequestFactory;
//    }

    @Bean
    public io.opentracing.Tracer jaegerTracer() {
//        Sampler sampler = new ConstSampler(true);
//        Sender sender = new UdpSender();
//        Reporter reporter = new RemoteReporter.Builder().withMaxQueueSize(1).withSender(sender).build();


        return io.jaegertracing.Configuration.fromEnv()
                .getTracer();


    }
}