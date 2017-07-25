package org.egov.propertyIndexer;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
/**
 * 
 * @author Prasad
 *
 * This is the starting class of the Property indexer
 */
@SpringBootApplication
@Import({TracerConfiguration.class})
public class PtIndexerApplication {
	public static void main(String[] args) {
		SpringApplication.run(PtIndexerApplication.class, args);

	}
}
