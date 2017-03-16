package org.egov;

import org.egov.filters.route.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.support.RibbonRequestCustomizer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@EnableZuulProxy
@SpringBootApplication
public class ZuulGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class, args);
    }

    @Autowired
    private RibbonCommandFactory<?> ribbonCommandFactory;

    @Value("${egov.user-info-header}")
    private String userInfoHeader;

    @Value("${egov.open-endpoints-whitelist}")
    private String openEndpointsWhitelist;

    @Bean
    public AuthFilter authFilter() {
        ProxyRequestHelper helper = new ProxyRequestHelper();
        List<RibbonRequestCustomizer> requestCustomizers = new ArrayList<>();

        return new AuthFilter(new RibbonRoutingFilter(helper, ribbonCommandFactory, requestCustomizers),
                userInfoHeader, openEndpointsWhitelist);
    }
}