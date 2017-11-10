package org.egov.lcms.notification;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.egov.lcms.notification.config.PropertiesManager;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@Import({TracerConfiguration.class})
public class LcmsNotificationApplication {
	
	@Autowired
	PropertiesManager propertiesManager;
	
	public static void main(String[] args) {
		SpringApplication.run(LcmsNotificationApplication.class, args);
	}
	
	@Bean
	public PropertiesManager getPropertiesManager() {
		return new PropertiesManager();
	}
	
	@Bean
	public MustacheEngine getMustacheEngine() {
		ClassPathTemplateLocator classPathTemplateLocator = new ClassPathTemplateLocator(
				Integer.valueOf(propertiesManager.getTemplatePriority()),
				propertiesManager.getTemplateFolder(),
				propertiesManager.getTemplateType());
		return MustacheEngineBuilder.newBuilder().addTemplateLocator(classPathTemplateLocator).build();
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter jacksonConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a"));
		mapper.setTimeZone(TimeZone.getTimeZone(propertiesManager.getAppTimeZone()));
		converter.setObjectMapper(mapper);
		return converter;
	}
}
