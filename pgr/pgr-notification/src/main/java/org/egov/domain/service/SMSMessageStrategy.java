package org.egov.domain.service;

import org.egov.domain.model.*;

public interface SMSMessageStrategy {
    boolean matches(NotificationContext context);
    SMSMessageContext getMessageContext(NotificationContext context);
}

