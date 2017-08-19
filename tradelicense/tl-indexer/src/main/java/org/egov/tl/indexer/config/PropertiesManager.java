package org.egov.tl.indexer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.Getter;
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

	@Value("${egov.services.tl-masters_v1.hostname}")
	private String tradeLicenseMasterServiceHostName;

	@Value("${egov.services.tl-masters_v1.basepath}")
	private String tradeLicenseMasterServiceBasePath;

	@Value("${egov.services.tl-masters_v1.category.searchpath}")
	private String categoryServiceSearchPath;

	@Value("${egov.services.tl-masters_v1.uom.searchpath}")
	private String uomServiceSearchPath;

	@Value("${egov.services.tl-masters_v1.status.searchpath}")
	private String statusServiceSearchPath;

	@Value("${egov.services.egov-location.hostname}")
	private String locationServiceHostName;

	@Value("${egov.services.egov-location.basepath}")
	private String locationServiceBasePath;

	@Value("${egov.services.egov-location.searchpath}")
	private String locationServiceSearchPath;

}