package org.egov.eis.consumers;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.eis.model.Attendance;
import org.egov.eis.repository.ElasticSearchRepository;
import org.egov.eis.service.AttendanceService;
import org.egov.eis.web.contract.AttendanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AttendanceConsumers {

    public static final Logger LOGGER = LoggerFactory.getLogger(AttendanceConsumers.class);

    private static final String OBJECT_TYPE_ATENDANCE = "attendance";

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ElasticSearchRepository elasticSearchRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "${kafka.topics.attendance.name}")
    public void listen(final ConsumerRecord<String, String> record) {
        LOGGER.info("key:" + record.key() + ":" + "value:" + record.value() + "thread:" + Thread.currentThread());
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            LOGGER.info("SaveAttendanceConsumer egov-hr-attendance attendanceService:" + attendanceService);
            final AttendanceRequest attendanceRequest = attendanceService
                    .create(objectMapper.readValue(record.value(), AttendanceRequest.class));
            for (final Attendance attendance : attendanceRequest.getAttendances())
                elasticSearchRepository.index(OBJECT_TYPE_ATENDANCE,
                        attendance.getEmployee() + "-" + sdf.format(attendance.getAttendanceDate()) + "-"
                                + attendance.getTenantId(),
                        attendance);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}