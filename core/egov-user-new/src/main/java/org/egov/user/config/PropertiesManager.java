package org.egov.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class PropertiesManager {

    @Autowired
    private Environment environment;

    @Value("${kafka.topics.notification.user.otp}")
    private String sendOtp;
    
    @Value("${kafka.topics.notification.sms.name}")
    private String smsNotificationTopic;

    @Value("${kafka.topics.notification.sms.group}")
    private String smsNotificationTopicKey;
    

    @Value("${kafka.topics.notification.email.name}")
    private String emailNotificationTopic;

    @Value("${kafka.topics.notification.email.key}")
    private String emailNotificationTopicKey;

}

