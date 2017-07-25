package org.egov;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;

@SpringBootApplication
@Import({TracerConfiguration.class})
public class PtNotificationApplication {

	@Autowired
	Environment environment;

	@Bean
	public MustacheEngine getMustacheEngine() {
		ClassPathTemplateLocator classPathTemplateLocator = new ClassPathTemplateLocator(
				Integer.valueOf(environment.getProperty("pt-notification.template.priority")),
				environment.getProperty("pt-notification.template.folder"),
				environment.getProperty("pt-notification.template.type"));
		return MustacheEngineBuilder.newBuilder().addTemplateLocator(classPathTemplateLocator).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(PtNotificationApplication.class, args);
	}
}
