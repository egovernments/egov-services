package org.egov;

import org.egov.filters.pre.AuthFilter;
import org.egov.filters.pre.AuthPreCheckFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableZuulProxy
@SpringBootApplication
public class ZuulGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class, args);
    }

    @Value("${egov.user-info-header}")
    private String userInfoHeader;

    @Value("${egov.open-endpoints-whitelist}")
    private String openEndpointsWhitelist;

    @Value("${egov.anonymous-endpoints-whitelist}")
    private String anonymousEndpointsWhitelist;

    @Value("${egov.auth-service-host}")
    private String authServiceHost;

    @Value("${egov.auth-service-uri}")
    private String authServiceUri;

    @Bean
    public AuthPreCheckFilter authCheckFilter() {
        return new AuthPreCheckFilter(openEndpointsWhitelist, anonymousEndpointsWhitelist);
    }

    @Bean
    public AuthFilter authFilter() {
        RestTemplate restTemplate = new RestTemplate();
        return new AuthFilter(restTemplate, authServiceHost, authServiceUri, userInfoHeader);
    }
}