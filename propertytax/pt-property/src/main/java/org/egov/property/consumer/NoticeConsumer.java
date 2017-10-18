package org.egov.property.consumer;

import java.util.Map;

import org.egov.models.NoticeRequest;
import org.egov.property.services.NoticeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@EnableKafka
public class NoticeConsumer {

	private String createNoticeTopic;

	private String updateNoticeTopic;

	private ObjectMapper objectMapper;

	private NoticeService noticeService;

	public NoticeConsumer(@Value("${egov.propertytax.property.notice.create}") String createNoticeTopic,
			@Value("${egov.propertytax.property.notice.update}") String updateNoticeTopic, ObjectMapper objectMapper,
			NoticeService noticeService) {
		this.createNoticeTopic = createNoticeTopic;
		this.updateNoticeTopic = updateNoticeTopic;
		this.objectMapper = objectMapper;
		this.noticeService = noticeService;
	}

	@KafkaListener(topics = { "${egov.propertytax.property.notice.create}",
			"${egov.propertytax.property.notice.update}" })
	public void listen(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {
		if (topic.equalsIgnoreCase(createNoticeTopic))
			noticeService.create(objectMapper.convertValue(consumerRecord, NoticeRequest.class));

		if (topic.equalsIgnoreCase(updateNoticeTopic))
			noticeService.update(objectMapper.convertValue(consumerRecord, NoticeRequest.class));
	}
}
