package org.egov.pgr.employee.enrichment.consumer;

import org.egov.pgr.employee.enrichment.model.RequestInfo;
import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.model.RequestContext;
import org.egov.pgr.employee.enrichment.repository.ComplaintRepository;
import org.egov.pgr.employee.enrichment.service.WorkflowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.MDC;

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
    public void test_should_persist_enriched_seva_request() {
        final HashMap<String, Object> sevaRequestMap = getSevaRequestMap();
        final SevaRequest enrichedSevaRequest = new SevaRequest(null);
        when(workflowService.enrichWorkflow(any(SevaRequest.class))).thenReturn(enrichedSevaRequest);

        complaintAssignmentListener.process(sevaRequestMap);

        verify(complaintRepository).save(enrichedSevaRequest);
    }

    @Test
    public void test_should_set_request_context_with_correlation_id() {
        final HashMap<String, Object> sevaRequestMap = getSevaRequestMap();
        final SevaRequest enrichedSevaRequest = new SevaRequest(null);
        when(workflowService.enrichWorkflow(any(SevaRequest.class))).thenReturn(enrichedSevaRequest);

        complaintAssignmentListener.process(sevaRequestMap);

        assertEquals("correlationId", RequestContext.getId());
    }

    private HashMap<String, Object> getSevaRequestMap() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final RequestInfo requestInfo = RequestInfo.builder()
                .msgId("correlationId")
                .build();
        sevaRequestMap.put("RequestInfo", requestInfo);
        return sevaRequestMap;
    }


}