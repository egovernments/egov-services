package org.egov.pgr.notification.domain.service.emailstrategy;

import org.egov.pgr.notification.domain.model.EmailMessageContext;
import org.egov.pgr.notification.domain.model.NotificationContext;

public interface EmailMessageStrategy {
    boolean matches(NotificationContext context);
    EmailMessageContext getMessageContext(NotificationContext context);
}
