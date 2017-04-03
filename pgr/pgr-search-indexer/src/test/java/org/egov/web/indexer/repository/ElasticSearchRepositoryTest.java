package org.egov.web.indexer.repository;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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
    private final String INDEX_ID = "indexId";

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        elasticSearchRepository = new ElasticSearchRepository(
                restTemplate, INDEX_SERVICE_URL, INDEX_SERVICE_USERNAME, INDEX_SERVICE_PASSWORD);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_index_object_instance() throws Exception {
        String expectedUri = String.format("%s%s/%s/%s", INDEX_SERVICE_URL, INDEX_NAME, INDEX_NAME, INDEX_ID);
        server.expect(once(), requestTo(expectedUri))
                .andExpect(header("Authorization", "Basic dXNlcm5hbWU6cGFzc3dvcmQ="))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new Resources().getFileContents("esIndexRequestBody.json")))
                .andRespond(withSuccess());
        Map<String, Object> indexContent = new HashMap<String, Object>() {{
           put("crn", "CRN");
        }};

        elasticSearchRepository.index(INDEX_NAME, INDEX_ID, indexContent);

        server.verify();
    }
}