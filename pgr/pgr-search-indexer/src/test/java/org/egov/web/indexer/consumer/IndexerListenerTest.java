package org.egov.web.indexer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.web.indexer.adaptor.ComplaintAdapter;
import org.egov.web.indexer.contract.RequestInfo;
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.contract.ComplaintIndex;
import org.egov.web.indexer.models.RequestContext;
import org.egov.web.indexer.repository.ElasticSearchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IndexerListenerTest {

    public static final String CRN = "crn";
    public static final String TOPIC_NAME = "topicName";
    @Mock
    private ElasticSearchRepository elasticSearchRepository;

    @Mock
    private ComplaintAdapter complaintAdapter;

    @InjectMocks
    private IndexerListener indexerListener;

    @Test
    public void test_should_set_correlation_id() {
        final RequestInfo requestInfo = RequestInfo.builder()
                .msgId("correlationId")
                .build();
        final ServiceRequest serviceRequest = ServiceRequest.builder().build();
        final SevaRequest sevaRequest = new SevaRequest(requestInfo, serviceRequest);
        final ConsumerRecord<String, SevaRequest> consumerRecord =
                new ConsumerRecord<>(TOPIC_NAME, 0, 0, "key", sevaRequest);
        final ComplaintIndex complaintIndex = new ComplaintIndex();
        when(complaintAdapter.adapt(serviceRequest)).thenReturn(complaintIndex);

        indexerListener.listen(consumerRecord);

        assertEquals("correlationId", RequestContext.getId());
    }

    @Test
    public void test_should_index_complaint_instande() {
        final RequestInfo requestInfo = RequestInfo.builder()
                .msgId("correlationId")
                .build();
        final ServiceRequest serviceRequest = ServiceRequest.builder()
                .crn(CRN)
                .build();
        final SevaRequest sevaRequest = new SevaRequest(requestInfo, serviceRequest);
        final ConsumerRecord<String, SevaRequest> consumerRecord =
                new ConsumerRecord<>(TOPIC_NAME, 0, 0, "key", sevaRequest);
        final ComplaintIndex complaintIndex = new ComplaintIndex();
        complaintIndex.setCrn(CRN);
        when(complaintAdapter.adapt(serviceRequest)).thenReturn(complaintIndex);

        indexerListener.listen(consumerRecord);

        verify(elasticSearchRepository).index("complaint", CRN, complaintIndex);
    }

}