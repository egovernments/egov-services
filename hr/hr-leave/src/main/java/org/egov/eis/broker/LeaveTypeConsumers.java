package org.egov.eis.broker;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.eis.model.LeaveType;
import org.egov.eis.repository.ElasticSearchRepository;
import org.egov.eis.service.LeaveTypeService;
import org.egov.eis.web.contract.LeaveTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LeaveTypeConsumers {

	public static final Logger LOGGER = LoggerFactory.getLogger(LeaveTypeConsumers.class);

	private static final String OBJECT_TYPE_LEAVETYPE = "leavetype";

	@Autowired
	private LeaveTypeService leaveTypeService;

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "egov-hr-leavetype")
	public void listen(final ConsumerRecord<String, String> record) {
		LOGGER.info("key:" + record.key() + ":" + "value:" + record.value() + "thread:" + Thread.currentThread());
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (record.topic().equals("egov-hr-leavetype")) {
			final ObjectMapper objectMapper = new ObjectMapper();
			try {
				LOGGER.info("SaveLeaveTypeConsumer egov-hr-leavetype leaveTypeService:" + leaveTypeService);
				final LeaveTypeRequest leaveTypeRequest = leaveTypeService
						.create(objectMapper.readValue(record.value(), LeaveTypeRequest.class));
				for (final LeaveType leaveType : leaveTypeRequest.getLeaveType())
					//TODO : leavetype index id should be changed
					elasticSearchRepository.index(OBJECT_TYPE_LEAVETYPE,
							leaveType.getTenantId() + "" + leaveType.getName(), leaveType);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
}