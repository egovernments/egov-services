package org.egov.pgr.notification.domain.service.contextenricher;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SevaRequest;

public interface NotificationContextEnricher {
    void enrich(SevaRequest sevaRequest, NotificationContext context);
}




