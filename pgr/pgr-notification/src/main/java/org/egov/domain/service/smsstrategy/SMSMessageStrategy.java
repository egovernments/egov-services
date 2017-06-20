package org.egov.domain.service.smsstrategy;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.SMSMessageContext;

public interface SMSMessageStrategy {
    boolean matches(NotificationContext context);
    SMSMessageContext getMessageContext(NotificationContext context);
}

