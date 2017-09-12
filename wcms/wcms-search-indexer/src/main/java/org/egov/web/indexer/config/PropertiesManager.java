package org.egov.web.indexer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.Getter;

@Configuration
@Getter
public class PropertiesManager {
	
	 @Autowired
	 private Environment environment;
	 
	 @Value("${kafka.topics.newconnection.create.name}")
	 private String newConnectionTopicName;

	 @Value("${kafka.topics.newconnection.update.name}")
	 private String updateConnectionTopicName;
	 
	 @Value("${egov.services.esindexer.host}")
	 private String indexServiceHostUrl; 
	 
	 @Value("${es.index.name}")
	 private String indexName;
	 
	 @Value("${es.document.type}")
	 private String documentType;
	 
}
