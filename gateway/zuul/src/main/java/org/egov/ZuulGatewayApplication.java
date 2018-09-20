package org.egov;

import org.egov.filters.pre.AuthFilter;
import org.egov.filters.pre.AuthPreCheckFilter;
import org.egov.filters.pre.RbacFilter;
import org.egov.filters.pre.RbacPreCheckFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashSet;

@EnableZuulProxy
@SpringBootApplication
public class ZuulGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class, args);
    }

    @Value("${egov.user-info-header}")
    private String userInfoHeader;

    @Value("#{'${egov.open-endpoints-whitelist}'.split(',')}")
    private String[] openEndpointsWhitelist;

    @Value("#{'${egov.mixed-mode-endpoints-whitelist}'.split(',')}")
    private String[] mixedModeEndpointsWhitelist;

    @Value("${egov.auth-service-host}")
    private String authServiceHost;

    @Value("${egov.auth-service-uri}")
    private String authServiceUri;

    @Bean
    public AuthPreCheckFilter authCheckFilter() {
        return new AuthPreCheckFilter(new HashSet<>(Arrays.asList(openEndpointsWhitelist)),
            new HashSet<>(Arrays.asList(mixedModeEndpointsWhitelist)));
    }

    @Bean
    public AuthFilter authFilter() {
        RestTemplate restTemplate = new RestTemplate();
        final ProxyRequestHelper proxyRequestHelper = new ProxyRequestHelper();
        return new AuthFilter(proxyRequestHelper, restTemplate, authServiceHost, authServiceUri);
    }

    @Bean
    public RbacFilter rbacFilter() {
        return new RbacFilter();
    }
    
    @Bean
    public RestTemplate restTemplate() {
    	return new RestTemplate();
    }

    @Bean
    public RbacPreCheckFilter rbacCheckFilter() {
        return new RbacPreCheckFilter(new HashSet<>(Arrays.asList(openEndpointsWhitelist)),
            new HashSet<>(Arrays.asList(mixedModeEndpointsWhitelist))
        );
    }
}