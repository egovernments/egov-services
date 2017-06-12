package org.egov.domain.service;

import org.egov.domain.model.*;
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

