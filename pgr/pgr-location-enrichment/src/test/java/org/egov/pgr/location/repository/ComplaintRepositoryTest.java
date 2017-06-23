package org.egov.pgr.location.repository;

import org.egov.pgr.location.model.SevaRequest;
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

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
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