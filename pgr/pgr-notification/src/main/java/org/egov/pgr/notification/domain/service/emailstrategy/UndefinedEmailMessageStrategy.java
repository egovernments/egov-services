package org.egov.pgr.notification.domain.service.emailstrategy;

import org.egov.pgr.notification.domain.model.EmailMessageContext;
import org.egov.pgr.notification.domain.model.NotificationContext;
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
