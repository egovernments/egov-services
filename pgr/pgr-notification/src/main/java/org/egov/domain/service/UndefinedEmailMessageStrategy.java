package org.egov.domain.service;

import org.egov.domain.model.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UndefinedEmailMessageStrategy implements EmailMessageStrategy {

    @Override
    public boolean matches(NotificationContext context) {
        return true;
    }

    @Override
    public EmailMessageContext getMessageContext(NotificationContext context) {
        throw new NotImplementedException();
    }
}
