package org.egov;

import org.egov.notification.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;

@SpringBootApplication
//@Import({TracerConfiguration.class})
public class PtNotificationApplication {
	
	@Autowired
	PropertiesManager propertiesManager;

	@Bean
	public MustacheEngine getMustacheEngine() {
		ClassPathTemplateLocator classPathTemplateLocator = new ClassPathTemplateLocator(
				Integer.valueOf(propertiesManager.getTemplatePriority()),
				propertiesManager.getTemplateFolder(),
				propertiesManager.getTemplateType());
		return MustacheEngineBuilder.newBuilder().addTemplateLocator(classPathTemplateLocator).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(PtNotificationApplication.class, args);
	}
	
	@Bean
	public PropertiesManager getPropertiesManager() {
		return new PropertiesManager();
	}
}
