package org.egov.pgr.location.consumer;

import org.egov.pgr.location.model.SevaRequest;
import org.egov.pgr.location.repository.ComplaintRepository;
import org.egov.pgr.location.services.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

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
    public void test_should_perist_enriched_seva_request() {
        final HashMap<String, Object> sevaRequest = getSevaRequestMap();
        final SevaRequest enrichedSevaRequest = new SevaRequest(new HashMap<>());
        when(locationService.enrich(any())).thenReturn(enrichedSevaRequest);

        listener.process(sevaRequest);

        verify(complaintRepository).save(enrichedSevaRequest);
    }

    private HashMap<String, Object> getSevaRequestMap() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("msg_id", "correlationId");
        sevaRequestMap.put("RequestInfo", requestInfo);
        return sevaRequestMap;
    }
}