package org.egov.lcms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PropertiesManager {
	@Autowired
	Environment environment;
	
	public String createLegalCase;
	
	public String updateLegalCase;

	public String getCreateLegalCase() {
		return environment.getProperty("egov.lcms.creare.legal.case");
	}

	public String getUpdateLegalCase() {
		return environment.getProperty("egov.lcms.update.legal.case");
	}
}
