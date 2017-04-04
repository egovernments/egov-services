package org.egov.web.notification.mail.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.web.notification.mail.model.Email;
import org.egov.web.notification.mail.model.EmailRequest;
import org.egov.web.notification.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class EmailNotificationListener {

	@Autowired
	private EmailService emailService;

	@KafkaListener(id = "${kafka.topics.notification.mail.id}", topics = "${kafka.topics.notification.mail.name}", group = "${kafka.topics.notification.mail.group}")
	public void listen(ConsumerRecord<String, EmailRequest> record) {
		System.err.println("***** received message [key " + record.key() + "] + value [" + record.value()
				+ "] from topic egov-notification-mail");
		EmailRequest request = record.value();
		Email email = new Email(request.getEmail(), request.getSubject(), request.getBody());
		emailService.sendEmail(email);
	}

}
