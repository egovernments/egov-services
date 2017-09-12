package org.egov.egf.bill.index;

import java.text.SimpleDateFormat;
import java.util.List;

import org.egov.egf.bill.index.web.interceptor.CorrelationIdInterceptor;
import org.egov.tracer.config.TracerConfiguration;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Import({ TracerConfiguration.class })
@SpringBootApplication
public class EgfBillIndexerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgfBillIndexerApplication.class, args);
	}
	
	public MappingJackson2HttpMessageConverter jacksonConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		converter.setObjectMapper(mapper);
		return converter;
	}

	@Primary
	@Bean
	public RestTemplate getRestTemplate(LogAwareRestTemplate restTemplate) {

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		converter.setObjectMapper(mapper);

		MappingJackson2HttpMessageConverter mk = null;

		for (HttpMessageConverter<?> ob : restTemplate.getMessageConverters()) {

			if (ob.getClass().getSimpleName().equals("MappingJackson2HttpMessageConverter")) {
				mk = (MappingJackson2HttpMessageConverter) ob;
				break;
			}
		}
		restTemplate.getMessageConverters().remove(mk);
		restTemplate.getMessageConverters().add(converter);
		return restTemplate;

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

			@Override
			public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
				MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
				converter.setObjectMapper(mapper);
				converters.add(converter);
				super.configureMessageConverters(converters);
			}
		};
	}
}
