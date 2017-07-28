package org.egov.pgr.location.services;

import org.egov.pgr.location.contract.BoundaryResponse;
import org.egov.pgr.location.contract.CrossHierarchyResponse;
import org.egov.pgr.location.model.SevaRequest;
import org.egov.pgr.location.repository.BoundaryRepository;
import org.egov.pgr.location.repository.CrossHierarchyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {

    private static final String BOUNDARY_NAME = "boundaryName";
    private static final long LOCATION_ID = 5L;
    @Mock
    private BoundaryRepository boundaryRepository;

    @Mock
    private CrossHierarchyRepository crossHierarchyRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    public void test_should_return_seva_request_as_is_when_location_id_is_already_present() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<Object, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("lat", 1.2d);
        serviceRequestMap.put("lng", 4.5d);
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> attribEntry = new HashMap<>();
        attribEntry.put("key", "systemLocationId");
        attribEntry.put("name", "5");
        attributeValues.add(attribEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        sevaRequestMap.put("serviceRequest", serviceRequestMap);
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final SevaRequest actualSevaRequest = locationService.enrich(sevaRequest);

        assertEquals(sevaRequest, actualSevaRequest);
        verify(boundaryRepository, never()).findBoundary(any(), any(),any());
        verify(crossHierarchyRepository, never()).getCrossHierarchy(any(),any());
    }

    @Test
    public void test_should_set_location_based_on_coordinates_when_present() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<Object, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("lat", 1.2d);
        serviceRequestMap.put("lng", 4.5d);
        serviceRequestMap.put("tenantId","ap.public");
        serviceRequestMap.put("isAttribValuesPopulated", false);
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        serviceRequestMap.put("attribValues", attributeValues);
        sevaRequestMap.put("serviceRequest", serviceRequestMap);
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final BoundaryResponse boundaryResponse = new BoundaryResponse(LOCATION_ID, BOUNDARY_NAME);
        when(boundaryRepository.findBoundary("1.2", "4.5","ap.public")).thenReturn(boundaryResponse);
        final SevaRequest actualSevaRequest = locationService.enrich(sevaRequest);

        assertEquals("5", actualSevaRequest.getLocationId());
        assertEquals(BOUNDARY_NAME, actualSevaRequest.getLocationName());
    }

    @Test
    public void test_should_set_location_based_on_cross_hierarchy_id_when_present() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<Object, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("addressId", "crossHierarchyId");
        serviceRequestMap.put("tenantId", "ap.public");
        serviceRequestMap.put("isAttribValuesPopulated", false);
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        serviceRequestMap.put("attribValues", attributeValues);
        sevaRequestMap.put("serviceRequest", serviceRequestMap);
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        final BoundaryResponse parentBoundaryResponse = new BoundaryResponse(1L, "parent");
        final BoundaryResponse childBoundaryResponse = new BoundaryResponse(2L, "child");
        final CrossHierarchyResponse hierarchyResponse =
                new CrossHierarchyResponse(parentBoundaryResponse, childBoundaryResponse);
        when(crossHierarchyRepository.getCrossHierarchy("crossHierarchyId","ap.public")).thenReturn(hierarchyResponse);

        final SevaRequest actualSevaRequest = locationService.enrich(sevaRequest);

        assertEquals("1", actualSevaRequest.getLocationId());
        assertEquals("parent", actualSevaRequest.getLocationName());
        assertEquals("2", actualSevaRequest.getChildLocationId());
    }

}