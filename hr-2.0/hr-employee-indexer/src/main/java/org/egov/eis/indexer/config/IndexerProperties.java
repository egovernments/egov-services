package org.egov.eis.indexer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class IndexerProperties {

    @Autowired
    private Environment environment;

    public String getProperty(String propKey) {
        return this.environment.getProperty(propKey, "");
    }

}
