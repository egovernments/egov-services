package org.egov.pgr;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.resolver.AuthenticatedUserResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@SpringBootApplication
public class PgrRestSpringBootApplication {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(AuthenticatedUserResolver authenticatedUserResolver) {
        return new WebMvcConfigurerAdapter() {

            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
            }

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                argumentResolvers.add(authenticatedUserResolver);
            }

        };
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        converter.setObjectMapper(mapper);
        return converter;
    }

    public static void main(String[] args) {
        SpringApplication.run(PgrRestSpringBootApplication.class, args);
    }
}

