package org.egov.hrms.utils;

import org.egov.hrms.model.SMSRequest;
import org.egov.hrms.model.User;
import org.egov.hrms.producer.HRMSProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j


public class SMSUtils {

    @Value("${kafka.topics.notification.sms}")
    private String smsNotifTopic;

    @Autowired
    private HRMSProducer hrmsProducer;


    public void processSMS(User user){
        List<SMSRequest> smsRequests = prepareSMSRequest(user);
        if (CollectionUtils.isEmpty(smsRequests)) {
            log.info("Messages from localization couldn't be fetched!");
        }
        else{
            for(SMSRequest smsRequest: smsRequests) {
                hrmsProducer.push(smsNotifTopic, smsRequest);

            }
        }

    }



    public List<SMSRequest> prepareSMSRequest(User user) {
        List<SMSRequest> smsRequestsTobeSent = new ArrayList<>();
        String phone = user.getMobileNumber();
        String message= "Test SMS!";

        smsRequestsTobeSent.add(SMSRequest.builder().mobileNumber(phone).message(message).build());
        return smsRequestsTobeSent;
    }
}
