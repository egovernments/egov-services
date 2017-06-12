package org.egov.domain.service;

import org.egov.domain.model.*;

public interface EmailMessageStrategy {
    boolean matches(NotificationContext context);
    EmailMessageContext getMessageContext(NotificationContext context);
}
