package org.egov.web.indexer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class ElasticSearchIndexerServiceTest {

    private MockRestServiceServer server;

    private ElasticSearchIndexerService elasticSearchIndexerService;

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        elasticSearchIndexerService = new ElasticSearchIndexerService(restTemplate, "http://host/");
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_index_object_instance() throws Exception {
        String indexName = "test";
        String indexId = "test1";
        server.expect(once(), requestTo("http://host/test/test/test1"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andRespond(withSuccess());
        Map<String, String> stringObj = new HashMap<String, String>();
        stringObj.put("key", "value");

        elasticSearchIndexerService.index(indexName, indexId, stringObj);

        server.verify();
    }
}