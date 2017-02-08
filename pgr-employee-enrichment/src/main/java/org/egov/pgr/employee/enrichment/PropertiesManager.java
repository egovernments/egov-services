package org.egov.pgr.employee.enrichment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager {

    @Autowired
    private Environment environment;

    @Value("${egov.services.workflow_service.hostname}")
    private String workflowServiceHostname;

    @Value("${egov.services.workflow_service.fetch_assignee}")
    private String fetchAssigneeContext;

    public String getAssigneeUrl() {
        return this.workflowServiceHostname + this.fetchAssigneeContext;
    }

}
