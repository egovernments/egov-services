package org.egov.pgr.consumer;

import org.egov.pgr.contract.RequestInfo;
import org.egov.pgr.model.RequestContext;
import org.egov.pgr.model.SevaRequest;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.services.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrievanceLocationEnrichmentListenerTest {

    @Mock
    private LocationService locationService;

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private GrievanceLocationEnrichmentListener listener;

    @Test
    public void test_should_set_request_context_with_correlation_id() {
        final HashMap<String, Object> sevaRequest = getSevaRequestMap();

        listener.process(sevaRequest);

        assertEquals("correlationId", RequestContext.getId());
    }

    @Test
    public void test_should_perist_enriched_seva_request() {
        final HashMap<String, Object> sevaRequest = getSevaRequestMap();
        final SevaRequest enrichedSevaRequest = new SevaRequest(new HashMap<>());
        when(locationService.enrich(any())).thenReturn(enrichedSevaRequest);

        listener.process(sevaRequest);

        verify(complaintRepository).save(enrichedSevaRequest);
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