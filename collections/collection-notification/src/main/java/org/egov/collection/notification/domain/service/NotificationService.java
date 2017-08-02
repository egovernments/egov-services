package org.egov.collection.notification.domain.service;

import org.egov.collection.notification.web.contract.Receipt;
import org.egov.collection.notification.web.contract.ReceiptRequest;
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

    public void notify(ReceiptRequest receiptRequest) {
        this.smsService.send(receiptRequest);
        this.emailService.send(receiptRequest);
    }



}
