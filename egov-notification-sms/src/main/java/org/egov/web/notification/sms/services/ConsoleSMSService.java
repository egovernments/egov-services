package org.egov.web.notification.sms.services;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "sms.enabled", havingValue = "false", matchIfMissing = true)
public class ConsoleSMSService implements SMSService {

    @Override
    public void sendSMS(String mobileNumber, String message, Priority priority) {
        System.out.println(String.format("Sending sms to %s with message '%s'", mobileNumber, message));
    }
}
