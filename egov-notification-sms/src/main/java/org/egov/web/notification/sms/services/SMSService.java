package org.egov.web.notification.sms.services;

public interface SMSService {
    boolean sendSMS(String mobileNumber, String message, MessagePriority priority);
}

