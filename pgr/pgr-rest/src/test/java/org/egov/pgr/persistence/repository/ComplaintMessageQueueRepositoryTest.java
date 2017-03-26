package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.queue.contract.RequestInfo;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.RequestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void test_should_set_correlation_id_to_seva_request() {
        RequestContext.setId("correlationId");
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), null);

        complaintMessageQueueRepository.save(sevaRequest);

        assertEquals("correlationId", sevaRequest.getRequestInfo().getCorrelationId());
    }
}