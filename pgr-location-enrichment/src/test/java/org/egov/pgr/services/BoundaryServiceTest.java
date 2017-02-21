package org.egov.pgr.services;

import org.egov.pgr.config.PropertiesManager;
import org.egov.pgr.contract.BoundaryResponse;
import org.egov.pgr.contract.BoundaryServiceResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BoundaryServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PropertiesManager propertiesManager;
    @InjectMocks
    BoundaryService boundaryService;

    private String templateBoundaryUrl = "http://boundary/{lat}/{lng}";

    @Before
    public void setUp() throws Exception {
        when(propertiesManager.getFetchBoundaryByLatLngUrl()).thenReturn(templateBoundaryUrl);
    }

    @Test
    public void testThatBoundaryServiceCallIsMade() throws Exception {
        Double lat = 12.343, lng = 23.23;
        BoundaryServiceResponse himalaya = getSuccessBoundaryResponse(12L, "himalaya");
        when(restTemplate.getForObject(templateBoundaryUrl, BoundaryServiceResponse.class, "12.343", "23.23")).thenReturn(himalaya);
        BoundaryResponse boundaryResponse = boundaryService.fetchBoundaryByLatLng(lat, lng);

        assertEquals("himalaya", boundaryResponse.getName());
        assertEquals(Long.valueOf(12), boundaryResponse.getId());
    }

    private BoundaryServiceResponse getSuccessBoundaryResponse(Long locationId, String locationName) {
        BoundaryResponse boundaryResponse = new BoundaryResponse(locationId, locationName);
        List<BoundaryResponse> boundaries = new ArrayList<>();
        boundaries.add(boundaryResponse);
        BoundaryServiceResponse boundaryServiceResponse = new BoundaryServiceResponse(boundaries);
        return boundaryServiceResponse;
    }
}