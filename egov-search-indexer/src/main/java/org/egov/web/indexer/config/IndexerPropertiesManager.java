package org.egov.web.indexer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class IndexerPropertiesManager {

	@Autowired
    private Environment environment;
	
	@Value("${egov.services.esindexer.host}")
    private String elasticSearchHost;
	
    @Value("${egov.services.boundary.host}")
    private String boundaryServiceHostname;
    
    @Value("${egov.services.pgrrest.host}")
    private String complaintTypeServiceHostname;
    
    public String getProperty(String propKey) {
        return this.environment.getProperty(propKey, "");
    }

}
