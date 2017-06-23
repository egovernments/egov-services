package org.egov.pgr.notification.domain.service;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SevaRequest;
import org.egov.pgr.notification.domain.service.contextenricher.NotificationContextEnricher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationContextFactory {

    private List<NotificationContextEnricher> notificationContextEnricherList;

    public NotificationContextFactory(List<NotificationContextEnricher> notificationContextEnricherList) {
        this.notificationContextEnricherList = notificationContextEnricherList;
    }

    public NotificationContext create(SevaRequest sevaRequest) {
        final NotificationContext context = new NotificationContext();
        notificationContextEnricherList.forEach(enricher -> enricher.enrich(sevaRequest, context));
        return context;
    }
}

