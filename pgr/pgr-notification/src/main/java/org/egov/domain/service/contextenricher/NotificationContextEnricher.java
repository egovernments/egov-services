package org.egov.domain.service.contextenricher;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.SevaRequest;

public interface NotificationContextEnricher {
    void enrich(SevaRequest sevaRequest, NotificationContext context);
}




