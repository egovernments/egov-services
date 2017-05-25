package org.egov.domain.service;

import org.egov.domain.model.SMSMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;

public interface SMSMessageStrategy {
    boolean matches(SevaRequest sevaRequest, ServiceType serviceType);

    SMSMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant);
}

