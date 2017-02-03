package org.egov.web.notification.sms.services;

public interface SMSService {
    void sendSMS(String mobileNumber, String message, MessagePriority priority);
}

