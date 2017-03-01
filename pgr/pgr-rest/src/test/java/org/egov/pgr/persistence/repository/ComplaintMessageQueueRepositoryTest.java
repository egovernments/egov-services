package org.egov.pgr.persistence.repository;

import org.egov.pgr.domain.model.RequestContext;
import org.egov.pgr.persistence.queue.contract.RequestInfo;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintMessageQueueRepositoryTest {

    private static final String TENANT_ID = "tenantId";
    private static final String correlationId = "correlation-id";
    private static final String QUEUE_NAME = "suffix";

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private ComplaintMessageQueueRepository complaintMessageQueueRepository;

    @Before
    public void before() {
        complaintMessageQueueRepository = new ComplaintMessageQueueRepository(kafkaTemplate, QUEUE_NAME);
    }

    @Test
    public void test_should_set_correlation_id_on_outgoing_message() {
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), null);
        RequestContext.setId(correlationId);

        complaintMessageQueueRepository.save(sevaRequest);

        assertEquals(correlationId, sevaRequest.getRequestInfo().getMsgId());
    }
}