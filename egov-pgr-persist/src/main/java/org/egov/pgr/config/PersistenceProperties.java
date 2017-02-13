package org.egov.pgr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PersistenceProperties {

    @Autowired
    private Environment environment;

    @Value("${egov.services.eis.hostname}")
    private String positionServiceHostname;

    @Value("${egov.services.eis.fetch_assignee.context}")
    private String fetchAssigneeContext;

    @Value("${kafka.topics.core.notifications.sms.name}")
    private String smsTopic;

    @Value("${kafka.topics.core.notifications.email.name}")
    private String emailTopic;

    @Value("${kafka.topics.pgr.indexing.name}")
    private String indexingTopic;

    private String getProperty(String propKey) {
        return this.environment.getProperty(propKey, "");
    }

    public String getPositionServiceEndpoint() {
        return positionServiceHostname + fetchAssigneeContext;
    }

    public String getSmsTopic() {
        return smsTopic;
    }

    public String getIndexingTopic() {
        return indexingTopic;
    }

    public String getEmailTopic() {
        return emailTopic;
    }
}
