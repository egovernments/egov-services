package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.repository.AssigneeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.egov.pgr.employee.enrichment.consumer.contract.ServiceRequest.*;
import static org.egov.pgr.employee.enrichment.consumer.contract.SevaRequest.SERVICE_REQUEST;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceTest {

    @Mock
    private AssigneeRepository assigneeRepository;
    private AssignmentService assignmentService;
    private String location;
    private String complaintType;
    private Map sevaRequestHash;

    @Before
    public void setUp() throws Exception {
        location = "12";
        complaintType = "PCMF";
        assignmentService = new AssignmentService(assigneeRepository);
        when(assigneeRepository.assigneeByBoundaryAndComplaintType(12L, complaintType)).thenReturn(10L);
    }

    @Test
    public void testThatHashIsEnrichedWithLocation() throws Exception {
        Map sevaRequestMap = buildSevaRequestMap();
        Map map = assignmentService.enrichComplaintWithAssignee(sevaRequestMap);

        assertEquals("10", extractValue(map, VALUES_ASSIGNMENT_ID));
    }

    private String extractValue(Map map, String keyName) {
        Map serviceRequest = (Map) map.get(SERVICE_REQUEST);
        Map values = (Map) serviceRequest.get(VALUES);
        return (String) values.get(keyName);
    }

    private Map buildSevaRequestMap() {
        Map sevaRequest = new HashMap();
        Map serviceRequest = new HashMap();
        Map values = new HashMap();
        values.put(VALUES_LOCATION_ID, location);
        serviceRequest.put(SERVICE_CODE, complaintType);
        serviceRequest.put(VALUES, values);
        sevaRequest.put(SERVICE_REQUEST, serviceRequest);
        return sevaRequest;
    }
}