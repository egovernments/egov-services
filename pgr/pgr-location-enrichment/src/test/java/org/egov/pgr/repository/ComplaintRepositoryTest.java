package org.egov.pgr.repository;

import org.egov.pgr.model.SevaRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintRepositoryTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    private ComplaintRepository complaintRepository;

    @Before
    public void before() {
        complaintRepository = new ComplaintRepository("topicName", kafkaTemplate);
    }

    @Test
    public void test_should_send_message() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        complaintRepository.save(sevaRequest);

        verify(kafkaTemplate).send("topicName", sevaRequestMap);
    }

}