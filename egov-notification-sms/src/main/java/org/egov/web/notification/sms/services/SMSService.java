package org.egov.web.notification.sms.services;

import org.egov.web.notification.sms.contract.SMSRequest;

public interface SMSService {
    void sendSMS(SMSRequest smsRequest, Priority priority);
}

