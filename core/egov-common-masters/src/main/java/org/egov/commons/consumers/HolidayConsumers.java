package org.egov.commons.consumers;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.commons.repository.ElasticSearchRepository;
import org.egov.commons.service.HolidayService;
import org.egov.commons.web.contract.HolidayRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HolidayConsumers {

	public static final Logger LOGGER = LoggerFactory.getLogger(HolidayConsumers.class);

	private static final String OBJECT_TYPE_HOLIDAY = "holiday";

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "egov-common-holiday")
	public void listen(final ConsumerRecord<String, String> record) {
		LOGGER.info("key:" + record.key() + ":" + "value:" + record.value() + "thread:" + Thread.currentThread());
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (record.topic().equals("egov-common-holiday")) {
			final ObjectMapper objectMapper = new ObjectMapper();
			try {
				LOGGER.info("SaveHolidayConsumer egov-common-holiday holidayService:" + holidayService);
				final HolidayRequest holidayRequest = holidayService
						.create(objectMapper.readValue(record.value(), HolidayRequest.class));
                //TODO : holiday index id should be changed
				elasticSearchRepository.index(OBJECT_TYPE_HOLIDAY, holidayRequest.getHoliday().getApplicableOn().toString(),
						holidayRequest.getHoliday());

			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
}