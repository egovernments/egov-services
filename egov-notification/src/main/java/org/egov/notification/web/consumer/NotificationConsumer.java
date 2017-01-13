package org.egov.notification.web.consumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.egov.notification.web.messaging.MessagePriority;
import org.egov.notification.web.messaging.email.EmailService;
import org.egov.notification.web.messaging.sms.SMSService;
import org.egov.notification.web.model.EmailRequest;
import org.egov.notification.web.model.SMSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Component
public class NotificationConsumer {

	@Autowired
	@Qualifier("smsService")
	private SMSService smsService;

	@Autowired
	private EmailService emailService;

	public void consume() {

		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("group.id", "notifications");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> notifications = new KafkaConsumer<>(props);
		notifications.subscribe(Arrays.asList("egov-notification-sms", "egov-notification-email"));
		while (true) {
			ConsumerRecords<String, String> records = notifications.poll(1000);
			System.err.println("******** polling at time " + new Date().toString());
			for (ConsumerRecord<String, String> record : records) {
				if (record.topic().equals("egov-notification-sms")) {
					System.err.println("***** received message [key" + record.key() + "] + value [" + record.value()
							+ "] from topic egov-notification-sms");
					ObjectMapper mapper = new ObjectMapper();
					SMSRequest request;
					try {
						request = mapper.readValue(record.value(), SMSRequest.class);
						smsService.sendSMS(request.getMobile_no(), request.getMessage(), MessagePriority.HIGH);
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				if (record.topic().equals("egov-notification-email")) {
					System.err.println("***** received message [key " + record.key() + "] + value [" + record.value()
							+ "] from topic egov-notification-email");
					Gson gson = new Gson();
					EmailRequest request = gson.fromJson(record.value(), EmailRequest.class);
					emailService.sendMail(request.getEmail(), request.getSubject(), request.getBody());
				}

			}
		}
	}
}