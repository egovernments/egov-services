package org.egov.collection.indexer.consumer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.collection.indexer.contract.ReceiptRequest;
import org.egov.collection.indexer.repository.ElasticSearchRepository;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;
import org.egov.collection.indexer.service.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IndexerListenerTest {

    @Mock
    private ElasticSearchRepository elasticSearchRepository;

    @Mock
    private DocumentService documentService;

    private IndexerListener indexerListener;

    @Before
    public void before() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        indexerListener = new IndexerListener(elasticSearchRepository, objectMapper, documentService);
    }

    @Test
    public void test_should_index_document() {
        final List<ReceiptRequestDocument> expectedDocumentToIndex = new ArrayList<ReceiptRequestDocument>();
        final HashMap<String, Object> sevaRequestMap = getReceiptRequestMap();
        when(documentService.enrich(any(ReceiptRequest.class))).thenReturn(expectedDocumentToIndex);

        indexerListener.listen(sevaRequestMap);

        verify(elasticSearchRepository).index(expectedDocumentToIndex);
    }

    private HashMap<String, Object> getReceiptRequestMap() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final ArrayList<HashMap<String, String>> attributeEntries = new ArrayList<>();
        final HashMap<String, String> statusEntry = new HashMap<>();
        statusEntry.put("key", "status");
        statusEntry.put("name", "REGISTERED");
        attributeEntries.add(statusEntry);
        serviceRequest.put("attribValues", attributeEntries);
        sevaRequestMap.put("serviceRequest", serviceRequest);
        final HashMap<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("action", "POST");
        sevaRequestMap.put("RequestInfo", requestInfo);

        return sevaRequestMap;
    }
}
