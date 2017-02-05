package org.egov.web.notification.sms.services;

import org.egov.web.notification.sms.models.Priority;
import org.egov.web.notification.sms.models.Sms;

public interface SMSService {
    void sendSMS(Sms sms, Priority priority);
}

