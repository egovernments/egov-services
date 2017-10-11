package org.egov.collection.indexer.repository;

import org.egov.collection.indexer.Resources.Resources;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ElasticSearchRepositoryTest {

    private MockRestServiceServer server;

    private ElasticSearchRepository elasticSearchRepository;

    private final String INDEX_SERVICE_URL = "http://host/";
    private final String INDEX_SERVICE_USERNAME = "username";
    private final String INDEX_SERVICE_PASSWORD = "password";
    private final String INDEX_NAME = "indexName";
    private final String DOCUMENT_TYPE = "documentType";

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        elasticSearchRepository = new ElasticSearchRepository(
                restTemplate, INDEX_SERVICE_URL, INDEX_SERVICE_USERNAME, INDEX_SERVICE_PASSWORD, INDEX_NAME, DOCUMENT_TYPE);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    //@Test
    public void test_should_index_object_instance() throws Exception {
        String expectedUri = "http://host/indexName/documentType/";
        server.expect(once(), requestTo(expectedUri))
                .andExpect(header("Authorization", "Basic dXNlcm5hbWU6cGFzc3dvcmQ="))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new Resources().getFileContents("esIndexRequestBody.json")))
                .andRespond(withSuccess());
        List<ReceiptRequestDocument> documents =  prepareCollectionDocuments();

        elasticSearchRepository.index(documents);

        server.verify();
    }

    private List<ReceiptRequestDocument> prepareCollectionDocuments() {
        List<ReceiptRequestDocument> documents = new ArrayList<ReceiptRequestDocument>();
        ReceiptRequestDocument collectionDocument1 = new ReceiptRequestDocument();
        collectionDocument1.setConsumerName("Narasappa");
        collectionDocument1.setConsumerNumber("0001");
        collectionDocument1.setTenantId("default");
        collectionDocument1.setTotalReceiptAmount(new BigDecimal(1000));
        collectionDocument1.setStatus("Created");
        collectionDocument1.setServiceType("PropertyTax");
        collectionDocument1.setCityName("Kurnool");
        collectionDocument1.setBillNumber("Bill001");
        collectionDocument1.setReceiptNumber("test123");
        collectionDocument1.setPaymentMode("Cash");


        documents.add(collectionDocument1);

        return documents;
    }
}