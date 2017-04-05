package org.egov.pgr.read.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.common.contract.SevaRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintMessageQueueRepositoryTest {

    private static final String TOPIC_NAME = "topic";

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private ComplaintMessageQueueRepository complaintMessageQueueRepository;

    @Before
    public void before() {
        complaintMessageQueueRepository = new ComplaintMessageQueueRepository(kafkaTemplate, TOPIC_NAME);
    }

    @Test
    public void test_should_send_message_to_kafka() {
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), null);

        complaintMessageQueueRepository.save(sevaRequest);

        verify(kafkaTemplate).send(TOPIC_NAME, sevaRequest);
    }
}