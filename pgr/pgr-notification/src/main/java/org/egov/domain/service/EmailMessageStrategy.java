package org.egov.domain.service;

import org.egov.domain.model.EmailMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;

public interface EmailMessageStrategy {
    boolean matches(SevaRequest sevaRequest, ServiceType serviceType);

    EmailMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType);
}
