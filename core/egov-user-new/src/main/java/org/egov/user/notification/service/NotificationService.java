package org.egov.user.notification.service;

import org.egov.user.domain.v11.model.Otp;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private EmailService emailService;
    private SMSService smsService;

    public NotificationService(EmailService emailService,
                               SMSService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public void notify(Otp userRequest) {
        this.smsService.send(userRequest);
        this.emailService.send(userRequest);
    }



}