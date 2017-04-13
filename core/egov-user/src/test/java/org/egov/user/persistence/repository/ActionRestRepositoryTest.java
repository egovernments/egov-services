package org.egov.user.persistence.repository;

import org.apache.commons.io.IOUtils;
import org.egov.user.web.contract.ActionResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


public class ActionRestRepositoryTest {

    private static final String HOST = "http://host";
    private static final String ROLE_ACTION = "/access/v1/actions/_search";

    private ActionRestRepository actionRestRepository;
    private MockRestServiceServer server;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        actionRestRepository = new ActionRestRepository(restTemplate, HOST, ROLE_ACTION);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testShouldGetActionByRole() {
        server.expect(once(),
                requestTo("http://host/access/v1/actions/_search"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(getFileContents("getActionUrls.json"),
                        MediaType.APPLICATION_JSON_UTF8));
        final ActionResponse actionResponse = actionRestRepository.getActionByRoleCodes(getRoles(),"ap.public");
        server.verify();
        assertTrue(actionResponse != null);
    }

    public List<String> getRoles(){
        List<String> roleCodes=new ArrayList<String>();
        roleCodes.add("EMPLOYEE");
        return  roleCodes;
    }

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
