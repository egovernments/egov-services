package org.egov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;

@SpringBootApplication
public class PgrNotificationApplication {

	private static final String TEMPLATE_TYPE = "txt";
	private static final String TEMPLATE_FOLDER = "templates";
	private static final int PRIORITY = 1;

	public static void main(String[] args) {
		SpringApplication.run(PgrNotificationApplication.class, args);
	}

	@Bean
    public MustacheEngine getMustacheEngine() {
		ClassPathTemplateLocator classPathTemplateLocator =
				new ClassPathTemplateLocator(PRIORITY, TEMPLATE_FOLDER, TEMPLATE_TYPE);
		return MustacheEngineBuilder.newBuilder()
				.addTemplateLocator(classPathTemplateLocator)
				.build();
	}
}
