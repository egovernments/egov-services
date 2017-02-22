package org.egov.web.indexer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class IndexerPropertiesManager {

	@Autowired
	private Environment environment;

	public String getProperty(String propKey) {
		return this.environment.getProperty(propKey, "");
	}

}
