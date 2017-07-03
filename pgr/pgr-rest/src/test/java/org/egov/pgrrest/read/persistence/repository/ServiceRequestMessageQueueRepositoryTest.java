package org.egov.pgrrest.read.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgrrest.common.contract.web.SevaRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceRequestMessageQueueRepositoryTest {

    private static final String TOPIC_NAME = "topic";

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private ServiceRequestMessageQueueRepository serviceRequestMessageQueueRepository;

    @Before
    public void before() {
        serviceRequestMessageQueueRepository = new ServiceRequestMessageQueueRepository(kafkaTemplate, TOPIC_NAME);
    }

    @Test
    public void test_should_send_message_to_kafka() {
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), null);

        serviceRequestMessageQueueRepository.save(sevaRequest);

        verify(kafkaTemplate).send(TOPIC_NAME, sevaRequest);
    }
}