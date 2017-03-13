package org.egov.eis.consumers;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.eis.service.AttendanceService;
import org.egov.eis.web.contract.AttendanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AttendanceConsumers {

    public static final Logger LOGGER = LoggerFactory.getLogger(AttendanceConsumers.class);

    @Autowired
    private AttendanceService attendanceService;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "egov-hr-attendance")
    public void listen(final ConsumerRecord<String, String> record) {
        LOGGER.info("key:" + record.key() + ":" + "value:" + record.value() + "thread:" + Thread.currentThread());
        if (record.topic().equals("egov-hr-attendance")) {
            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                LOGGER.info("SaveAttendanceConsumer egov-hr-attendance attendanceService:" + attendanceService);
                attendanceService.create(objectMapper.readValue(record.value(), AttendanceRequest.class));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}