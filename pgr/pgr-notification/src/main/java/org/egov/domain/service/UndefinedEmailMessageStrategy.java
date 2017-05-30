package org.egov.domain.service;

import org.egov.domain.model.EmailMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UndefinedEmailMessageStrategy implements EmailMessageStrategy {

    @Override
    public boolean matches(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        return true;
    }

    @Override
    public EmailMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        throw new NotImplementedException();
    }
}
