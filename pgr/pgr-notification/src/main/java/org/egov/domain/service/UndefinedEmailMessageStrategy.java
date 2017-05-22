package org.egov.domain.service;

import org.egov.domain.model.EmailMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UndefinedEmailMessageStrategy implements EmailMessageStrategy {

    @Override
    public boolean matches(SevaRequest sevaRequest, ServiceType serviceType) {
        return true;
    }

    @Override
    public EmailMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType) {
        throw new NotImplementedException();
    }
}
