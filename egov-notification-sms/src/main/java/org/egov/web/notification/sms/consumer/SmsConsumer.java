package org.egov.web.notification.sms.consumer;

import org.egov.web.notification.sms.consumer.contract.SMSRequest;
import org.egov.web.notification.sms.services.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
public class SmsConsumer {

    private SMSService smsService;

    @Autowired
    public SmsConsumer(SMSService smsService) {
        this.smsService = smsService;
    }

    @StreamListener(Sink.INPUT)
    public void receive(SMSRequest smsRequest) {
        smsService.sendSMS(smsRequest.toDomain());
    }
}
