package org.egov.egf;

import org.egov.egf.web.interceptor.CorrelationIdAwareRestTemplate;
import org.egov.egf.web.interceptor.CorrelationIdInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
public class FinancialsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialsApplication.class, args);
	}

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter() {
    	//DateFormat std=DateFormat.getInstance().f
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
       // mapper.setDateFormat(std);
        converter.setObjectMapper(mapper);
        return converter;
    }
    @Bean
    public RestTemplate getRestTemplate() {
        return new CorrelationIdAwareRestTemplate();
    }


    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {

            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new CorrelationIdInterceptor());
            }
        };
    }
}
