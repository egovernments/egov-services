package org.egov.pgr.write.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PersistenceProperties {

    @Value("${egov.services.eis.hostname}")
    private String positionServiceHostname;

    @Value("${egov.services.eis.fetch_assignee.context}")
    private String fetchAssigneeContext;

    public String getPositionServiceEndpoint() {
        return positionServiceHostname + fetchAssigneeContext;
    }
}
