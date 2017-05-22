package org.egov.domain.service;

import org.egov.domain.model.EmailMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;

public interface EmailMessageStrategy {
    boolean matches(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant);

    EmailMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant);
}
