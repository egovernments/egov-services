package org.egov.pgr.employee.enrichment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager {

    @Value("${egov.services.workflow.hostname}")
    private String workflowHost;

    @Value("${egov.services.workflow.create_workflow}")
    private String workflowCreatePath;

    @Value("${egov.services.workflow.close_workflow}")
    private String workflowClosePath;

    public String workflowCreateUrl() {
        return workflowHost + workflowCreatePath;
    }

    public String workflowCloseUrl() {
        return workflowHost + workflowClosePath;
    }


}
