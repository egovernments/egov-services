package org.egov.pgr.employee.enrichment.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.employee.enrichment.config.PropertiesManager;
import org.egov.pgr.employee.enrichment.repository.contract.AssigneeResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class AssigneeRepositoryImplTest {

    @Mock
    private PropertiesManager propertiesManager;
    private AssigneeRepository assigneeRepository;
    private String assigneeResponse;
    private MockRestServiceServer server;
    private String expectedUri = "http://workflow/assignee";

    @Before
    public void setUp() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        this.assigneeResponse = new ObjectMapper().writeValueAsString(new AssigneeResponse(222L, "Jake"));
        assigneeRepository = new AssigneeRepositoryImpl(restTemplate, propertiesManager);
        server = MockRestServiceServer.bindTo(restTemplate).build();
        when(propertiesManager.getAssigneeUrl()).thenReturn(expectedUri);
    }

    @Test
    public void testAssigneeIsFetchedByBoundaryIdAndComplaintType() throws Exception {
        long boundaryId = 12L;
        String complaintType = "PCMG";
        server.expect(once(), requestTo(expectedUri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(this.assigneeResponse, MediaType.APPLICATION_JSON));
        Long assigneeId = assigneeRepository.assigneeByBoundaryAndComplaintType(boundaryId, complaintType);

        assertEquals(Long.valueOf(222L), assigneeId);
    }

}