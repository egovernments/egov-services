package org.egov.property.repository;

import org.egov.models.NoticeRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NoticeMessageQueueRepository {

	public static final String CREATE = "CREATE";
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	private String createtopicName;

	private String createTopicKey;

	private String updateTopicName;

	private String updateTopicKey;

	public NoticeMessageQueueRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
			@Value("${egov.propertytax.property.notice.create}") final String createtopicName,
			@Value("${egov.propertytax.property.notice.create.key}") final String createTopicKey,
			@Value("${egov.propertytax.property.notice.update}") final String updateTopicName,
			@Value("${egov.propertytax.property.notice.update.key}") final String updateTopicKey) {
		this.kafkaTemplate = kafkaTemplate;
		this.createtopicName = createtopicName;
		this.createTopicKey = createTopicKey;
		this.updateTopicName = updateTopicName;
		this.updateTopicKey = updateTopicKey;
	}

	public void save(NoticeRequest noticeRequest, String action) {

		if (action.equals(CREATE))
			kafkaTemplate.send(createtopicName, createTopicKey, noticeRequest);
		else
			kafkaTemplate.send(updateTopicName, updateTopicKey, noticeRequest);
	}
}
