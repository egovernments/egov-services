package org.egov.web.indexer.repository;

import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

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
    private ESDateTimeFormatter esDateTimeFormatter;

    @Before
    public void before() {
        esDateTimeFormatter = new ESDateTimeFormatter("UTC");
        RestTemplate restTemplate = new RestTemplate();
        elasticSearchRepository = new ElasticSearchRepository(
            restTemplate, INDEX_SERVICE_URL, INDEX_SERVICE_USERNAME, INDEX_SERVICE_PASSWORD, INDEX_NAME, DOCUMENT_TYPE);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_index_object_instance() throws Exception {
        String expectedUri = "http://host/indexName/documentType/id";
        server.expect(once(), requestTo(expectedUri))
            .andExpect(header("Authorization", "Basic dXNlcm5hbWU6cGFzc3dvcmQ="))
            .andExpect(method(HttpMethod.PUT))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().string(new Resources().getFileContents("esIndexRequestBody.json")))
            .andRespond(withSuccess());
        ServiceRequestDocument indexContent = new ServiceRequestDocument();
        indexContent.setCrn("CRN");
        indexContent.setId("id");
        indexContent.setCreatedDate(esDateTimeFormatter.toESDateTimeString("23-12-2016 09:45:00"));
        indexContent.setLastModifiedDate(esDateTimeFormatter.toESDateTimeString("26-12-2016 23:15:40"));
        indexContent.setEscalationDate(esDateTimeFormatter.toESDateTimeString("30-12-2016 09:25:00"));
        indexContent.setDepartmentId("1");
        indexContent.setDesignationId("1");
        indexContent.setStateId("1");

        elasticSearchRepository.index(indexContent);

        server.verify();
    }

}