package org.egov.domain.service.contextenricher;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.SevaRequest;
import org.springframework.stereotype.Service;

@Service
public class SevaRequestContextEnricher implements NotificationContextEnricher {

    @Override
    public void enrich(SevaRequest sevaRequest, NotificationContext context) {
        context.setSevaRequest(sevaRequest);
    }
}
