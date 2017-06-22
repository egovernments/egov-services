package org.egov.pgr.notification.domain.service.smsstrategy;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SMSMessageContext;
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

