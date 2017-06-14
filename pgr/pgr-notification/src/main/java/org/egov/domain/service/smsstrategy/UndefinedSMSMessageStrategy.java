package org.egov.domain.service.smsstrategy;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.SMSMessageContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UndefinedSMSMessageStrategy implements SMSMessageStrategy {

    @Override
    public boolean matches(NotificationContext context) {
        return true;
    }

    @Override
    public SMSMessageContext getMessageContext(NotificationContext context) {
        throw new NotImplementedException();
    }
}

