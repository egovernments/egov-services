package org.egov.hr.consumers;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.hr.dao.AttendanceDao;
import org.egov.hr.model.AttendanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SaveAttendanceConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(SaveAttendanceConsumer.class);

    @Autowired
    private AttendanceDao attendanceDao;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "attendance-save-db")
    public void listen(final ConsumerRecord<String, String> record) {
        LOGGER.info("key:" + record.key() + ":" + "value:" + record.value());
        if (record.topic().equals("attendance-save-db")) {
            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                LOGGER.info("SaveAttendanceConsumer attendance-save-db AttendanceDao:" + attendanceDao);
                attendanceDao.saveAttendance(objectMapper.readValue(record.value(), AttendanceRequest.class));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}