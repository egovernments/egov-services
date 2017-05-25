package org.egov.web.indexer.consumer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.web.indexer.adaptor.ComplaintAdapter;
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.repository.ElasticSearchRepository;
import org.egov.web.indexer.repository.contract.ComplaintIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IndexerListenerTest {

    public static final String CRN = "crn";
    @Mock
    private ElasticSearchRepository elasticSearchRepository;

    @Mock
    private ComplaintAdapter complaintAdapter;

    private IndexerListener indexerListener;

    @Before
    public void before() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        indexerListener = new IndexerListener(elasticSearchRepository, complaintAdapter, objectMapper);
    }

    @Test
    public void test_should_index_new_complaint_instance() {
        final ComplaintIndex complaintIndex = new ComplaintIndex();
        complaintIndex.setCrn(CRN);
        when(complaintAdapter.indexOnCreate(any(ServiceRequest.class))).thenReturn(complaintIndex);
        final HashMap<String, Object> sevaRequestMap = getSevaRequestMap();

        indexerListener.listen(sevaRequestMap);

        verify(elasticSearchRepository).index("complaint", CRN, complaintIndex);
    }
    
    @Test
    public void test_should_index_update_complaint() {
        final ComplaintIndex complaintIndex = new ComplaintIndex();
        complaintIndex.setCrn(CRN);
        when(complaintAdapter.indexOnUpdate(any(ServiceRequest.class))).thenReturn(complaintIndex);
        final HashMap<String, Object> sevaRequestMap = getSevaRequestMapForUpdate();

        indexerListener.listen(sevaRequestMap);

        verify(elasticSearchRepository).index("complaint", CRN, complaintIndex);
    }

    private HashMap<String, Object> getSevaRequestMap() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final ArrayList<HashMap<String, String>> attributeEntries = new ArrayList<>();
        final HashMap<String, String> statusEntry = new HashMap<>();
        statusEntry.put("key", "status");
        statusEntry.put("name", "REGISTERED");
        attributeEntries.add(statusEntry);
        serviceRequest.put("attribValues", attributeEntries);
        sevaRequestMap.put("serviceRequest", serviceRequest);
        return sevaRequestMap;
    }
    
    private HashMap<String, Object> getSevaRequestMapForUpdate() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final ArrayList<HashMap<String, String>> attributeEntries = new ArrayList<>();
        final HashMap<String, String> statusEntry = new HashMap<>();
        statusEntry.put("key", "status");
        statusEntry.put("name", "COMPLETED");
        attributeEntries.add(statusEntry);
        serviceRequest.put("attribValues", attributeEntries);
        sevaRequestMap.put("serviceRequest", serviceRequest);
        return sevaRequestMap;
    }

}