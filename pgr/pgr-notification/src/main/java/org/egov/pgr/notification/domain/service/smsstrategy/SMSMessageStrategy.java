package org.egov.pgr.notification.domain.service.smsstrategy;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SMSMessageContext;

public interface SMSMessageStrategy {
    boolean matches(NotificationContext context);
    SMSMessageContext getMessageContext(NotificationContext context);
}

