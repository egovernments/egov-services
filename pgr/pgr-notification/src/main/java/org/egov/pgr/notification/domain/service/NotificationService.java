package org.egov.pgr.notification.domain.service;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SevaRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private EmailService emailService;
    private SMSService smsService;
    private NotificationContextFactory notificationContextFactory;

    public NotificationService(EmailService emailService,
                               SMSService smsService,
                               NotificationContextFactory notificationContextFactory) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.notificationContextFactory = notificationContextFactory;
    }

    public void notify(SevaRequest sevaRequest) {
        final NotificationContext context = getNotificationContext(sevaRequest);
        this.smsService.send(context);
        this.emailService.send(context);
    }

    private NotificationContext getNotificationContext(SevaRequest sevaRequest) {
        return notificationContextFactory.create(sevaRequest);
    }

}
