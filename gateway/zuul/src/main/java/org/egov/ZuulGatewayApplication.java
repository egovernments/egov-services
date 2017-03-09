package org.egov;

import org.egov.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import org.egov.filters.pre.AuthFilter;
import org.springframework.web.client.RestTemplate;

@EnableZuulProxy
@SpringBootApplication
public class ZuulGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class, args);
    }

    @Value("${user.service.url}")
    private String userServiceHost;

    @Value("${egov.services.user.get_user_details}")
    private String getUserDetailsUrl;

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public RestTemplate getRestTemplate(){return new RestTemplate();}

    @Bean
    public UserRepository userRepository(RestTemplate restTemplate){
        return new UserRepository(restTemplate, userServiceHost, getUserDetailsUrl);
    }
}