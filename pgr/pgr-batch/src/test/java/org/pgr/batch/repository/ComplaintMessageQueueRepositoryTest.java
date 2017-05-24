package org.pgr.batch.repository;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pgr.batch.service.model.SevaRequest;

import java.util.HashMap;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintMessageQueueRepositoryTest {
    private static final String TOPIC_NAME = "topicName";

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private ComplaintMessageQueueRepository complaintMessageQueueRepository;

    @Before
    public void before() {
        complaintMessageQueueRepository = new ComplaintMessageQueueRepository(TOPIC_NAME, kafkaTemplate);
    }

    @Test
    public void test_should_send_message_on_save() {
        final HashMap<String, Object> expectedMap = new HashMap<>();
        final SevaRequest sevaRequest = new SevaRequest();

        complaintMessageQueueRepository.save(sevaRequest);

        verify(kafkaTemplate).send(TOPIC_NAME, sevaRequest);
    }

}