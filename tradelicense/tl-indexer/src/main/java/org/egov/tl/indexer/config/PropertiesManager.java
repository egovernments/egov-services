package org.egov.tl.indexer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author Shubham pratap singh
 *
 */
@Configuration
@Getter
@ToString
public class PropertiesManager {

	@Autowired
	Environment environment;

	@Value("${egov.services.tl-services.create.legacy.tradelicense.validated}")
	private String createLegacyTradeValidated;
	
	@Value("${es.host}")
	private String esHost;
	
	@Value("${es.port}")
	private String esPort;

	@Value("${es.multiThread.indicator}")
	private String isMultithreaded;
	
	@Value("${es.timeout}")
	private String timeOut;
	
	@Value("${tradelicense.es.index.name}")
	private String esIndex;
	
	@Value("${tradelicense.es.index.type}")
	private String esIndexType;
	
	@Value("${auto.offset.reset.config}")
	private String kafkaOffsetConfig;
	
	@Value("${spring.kafka.bootstrap.servers}")
	private String kafkaServerConfig;


	
	


}