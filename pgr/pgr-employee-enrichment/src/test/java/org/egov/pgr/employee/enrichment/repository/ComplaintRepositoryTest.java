package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintRepositoryTest {

    private static final String TOPIC_NAME = "topicName";

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private ComplaintRepository complaintRepository;

    @Before
    public void before() {
        complaintRepository = new ComplaintRepository(TOPIC_NAME, kafkaTemplate);
    }

    @Test
    public void test_should_send_message_on_save() {
        final HashMap<String, Object> expectedMap = new HashMap<>();
        final SevaRequest sevaRequest = new SevaRequest(expectedMap);

        complaintRepository.save(sevaRequest);

        verify(kafkaTemplate).send(TOPIC_NAME, expectedMap);
    }

}