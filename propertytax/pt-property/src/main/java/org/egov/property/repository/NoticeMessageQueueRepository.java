package org.egov.property.repository;

import org.egov.models.NoticeRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NoticeMessageQueueRepository {

	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	private String createtopicName;

	private String createTopicKey;

	public NoticeMessageQueueRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
			@Value("${egov.propertytax.property.notice.create}") final String createtopicName,
			@Value("${egov.propertytax.property.notice.create.key}") final String createTopicKey) {
		this.kafkaTemplate = kafkaTemplate;
		this.createtopicName = createtopicName;
		this.createTopicKey = createTopicKey;
	}

	public void save(NoticeRequest noticeRequest) {

		kafkaTemplate.send(createtopicName, createTopicKey, noticeRequest);
	}
}
