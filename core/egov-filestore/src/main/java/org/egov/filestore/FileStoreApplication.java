package org.egov.filestore;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.TimeZone;

@SpringBootApplication
@Import({TracerConfiguration.class})
public class FileStoreApplication {
	private static final String IST = "Asia/Calcutta";

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(IST));

		SpringApplication.run(FileStoreApplication.class, args);
	}
}
