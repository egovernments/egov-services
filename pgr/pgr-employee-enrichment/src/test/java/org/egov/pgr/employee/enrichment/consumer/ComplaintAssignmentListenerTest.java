package org.egov.pgr.employee.enrichment.consumer;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintRepository;
import org.egov.pgr.employee.enrichment.service.WorkflowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintAssignmentListenerTest {

    @Mock
    private WorkflowService workflowService;

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintAssignmentListener complaintAssignmentListener;

    @Test
    public void testShouldPersistEnrichedSevaRequest() {
        final HashMap<String, Object> sevaRequestMap = getSevaRequestMap();
        final SevaRequest enrichedSevaRequest = new SevaRequest(null);
        when(workflowService.enrichWorkflow(any(SevaRequest.class))).thenReturn(enrichedSevaRequest);

        complaintAssignmentListener.process(sevaRequestMap);

        verify(complaintRepository).save(enrichedSevaRequest);
    }

    @Test
    public void testThatWorkflowEnrichmentIsCalledWithSevaRequest() throws Exception {
        HashMap<String, Object> sevaRequest = getSevaRequestMap();
        ArgumentCaptor<SevaRequest> sevaRequestCaptor = ArgumentCaptor.forClass(SevaRequest.class);
        complaintAssignmentListener.process(sevaRequest);

        verify(workflowService).enrichWorkflow(sevaRequestCaptor.capture());
        SevaRequest value = sevaRequestCaptor.getValue();
        assertEquals(sevaRequest, value.getRequestMap());
    }

    private HashMap<String, Object> getSevaRequestMap() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("msg_id", "correlationId");
        sevaRequestMap.put("RequestInfo", requestInfo);
        return sevaRequestMap;
    }


}