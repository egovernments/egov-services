package org.egov.web.notification.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
@EnableBinding(Sink.class)
public class EgovNotificationSmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgovNotificationSmsApplication.class, args);
	}
}
