package org.egov.domain.service.emailstrategy;

import org.egov.domain.model.EmailMessageContext;
import org.egov.domain.model.NotificationContext;

public interface EmailMessageStrategy {
    boolean matches(NotificationContext context);
    EmailMessageContext getMessageContext(NotificationContext context);
}
