package org.egov.pgr.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.config.PropertiesManager;
import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.model.SevaRequest;
import org.egov.pgr.producer.GrievanceAssignmentProducer;
import org.egov.pgr.services.BoundaryService;
import org.egov.pgr.services.CrossHierarchyService;
import org.egov.pgr.contract.BoundaryResponse;
import org.egov.pgr.contract.CrossHierarchyResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.egov.pgr.model.ServiceRequest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GrievanceLocationEnrichmentListenerTest {

    public static final String TEST_CONSUMER_TOPIC = "";
    public static final int PARTITION = 12;
    public static final long OFFSET = 24L;
    public static final String KEY = "";
    public static final String PUBLISH_TOPIC = "holycow";

    @Mock
    private BoundaryService boundaryService;
    @Mock
    private CrossHierarchyService crossHierarchyService;
    @Mock
    private GrievanceAssignmentProducer kafkaProducer;
    @Mock
    private PropertiesManager propertiesManager;

    private GrievanceLocationEnrichmentListener listener;

    @Before
    public void setUp() throws Exception {
        when(propertiesManager.getLocationEnrichedTopicName()).thenReturn(PUBLISH_TOPIC);
        listener = new GrievanceLocationEnrichmentListener(boundaryService, crossHierarchyService, kafkaProducer, propertiesManager);
    }

    @Test
    public void testThatLocationIsPopulatedWhenCoordinatesProvidedAndLocationIdMissing() throws Exception {
        Double lat = 12.3443, lng = 23.23323;
        String locationName = "himalaya";
        Long locationId = 12L;
        boundaryCallSuccessMock(lat, lng, locationId, locationName);
        listener.listen(getConsumeRecord(getServiceRequestWithLatLng(lat, lng)));

        ArgumentCaptor<SevaRequest> sevaRequestCaptor = ArgumentCaptor.forClass(SevaRequest.class);
        verify(kafkaProducer).sendMessage(eq(PUBLISH_TOPIC), sevaRequestCaptor.capture());
        SevaRequest sevaRequest = sevaRequestCaptor.getValue();

        assertNotNull(sevaRequest.getServiceRequest().getValues());
        assertEquals(String.valueOf(locationId), sevaRequest.getServiceRequest().getValues().get(LOCATION_ID));
        assertEquals(locationName, sevaRequest.getServiceRequest().getValues().get(LOCATION_NAME));
    }

    @Test
    public void testThatLocationIsPopulatedWhenCrossHierarchyIdIsProvidedAndLocationIdMissing() throws Exception {
        String crossHierarchyId = "12";
        String parentLocName = "himalaya";
        long parentLocId = 100L;
        long childLocId = 50L;

        crossHierarchyCallSuccess(crossHierarchyId, parentLocId, parentLocName, childLocId);
        listener.listen(getConsumeRecord(getServiceRequestWithCrossHierarchyId(crossHierarchyId)));

        ArgumentCaptor<SevaRequest> sevaRequestCaptor = ArgumentCaptor.forClass(SevaRequest.class);
        verify(kafkaProducer).sendMessage(eq(PUBLISH_TOPIC), sevaRequestCaptor.capture());
        SevaRequest sevaRequest = sevaRequestCaptor.getValue();

        assertNotNull(sevaRequest.getServiceRequest().getValues());
        assertEquals(String.valueOf(parentLocId), sevaRequest.getServiceRequest().getValues().get(LOCATION_ID));
        assertEquals(parentLocName, sevaRequest.getServiceRequest().getValues().get(LOCATION_NAME));
        assertEquals(String.valueOf(childLocId), sevaRequest.getServiceRequest().getValues().get(CHILD_LOCATION_ID));
    }

    @Test
    public void testThatWhenLocationIsProvidedBoundaryServiceIsNotCalled() throws Exception {
        String locationId = "13";
        Double lat = 12.343, lng = 24.223;
        listener.listen(getConsumeRecord(getServiceRequestWithLocationId(locationId, lat, lng)));

        verify(boundaryService, never()).fetchBoundaryByLatLng(any(RequestInfo.class), any(Double.class), any(Double.class));
    }

    @Test
    public void testThatWhenLocationIsProvidedCrossHierarchyServiceIsNotCalled() throws Exception {
        String crossHierarchyId = "13", locationId = "12";
        listener.listen(getConsumeRecord(getServiceRequestWithLocationId(locationId, crossHierarchyId)));

        verify(crossHierarchyService, never()).fetchCrossHierarchyById(any(RequestInfo.class), any(String.class));
    }

    @Test
    public void testForEmptyLocationIdBounadaryServiceShouldBeCalled() throws Exception {
        String locationId = "";
        Double lat = 22.000;
        Double lng = 22.584;
        String locationName = "himalaya";
        Long locId = 12L;
        boundaryCallSuccessMock(lat, lng, locId, locationName);

        listener.listen(getConsumeRecord(getServiceRequestWithLocationId(locationId, lat, lng)));
        verify(boundaryService, atLeastOnce()).fetchBoundaryByLatLng(any(RequestInfo.class), eq(lat), eq(lng));
    }

    @Test
    public void testForEmptyLocationIdCrossHierarchyServiceShouldBeCalled() throws Exception {
        String locationId = "";
        String crossHierarchyId = "23";
        String parentLocName = "himalaya";
        Long parentLocId = 12L;
        Long childLocId = 12L;
        crossHierarchyCallSuccess(crossHierarchyId, parentLocId, parentLocName, childLocId);

        listener.listen(getConsumeRecord(getServiceRequestWithLocationId(locationId, crossHierarchyId)));
        verify(crossHierarchyService, atLeastOnce()).fetchCrossHierarchyById(any(RequestInfo.class), eq(crossHierarchyId));
    }

    private void crossHierarchyCallSuccess(String crossHierarchyId, Long parentLocId, String parentLocName, Long childLocId) {
        BoundaryResponse parentResponse = new BoundaryResponse(parentLocId, parentLocName);
        BoundaryResponse childResponse = new BoundaryResponse(childLocId, null);
        CrossHierarchyResponse crossHierarchyResponse = new CrossHierarchyResponse(parentResponse, childResponse);
        when(crossHierarchyService.fetchCrossHierarchyById(any(RequestInfo.class), eq(crossHierarchyId))).thenReturn(crossHierarchyResponse);
    }

    private ServiceRequest getServiceRequestWithCrossHierarchyId(String crossHierarchyId) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setCrossHierarchyId(crossHierarchyId);
        return serviceRequest;
    }

    private void boundaryCallSuccessMock(Double lat, Double lng, Long locationId, String locationName) {
        BoundaryResponse boundaryResponse = new BoundaryResponse(locationId, locationName);
        when(boundaryService.fetchBoundaryByLatLng(any(RequestInfo.class), eq(lat), eq(lng))).thenReturn(boundaryResponse);
    }

    private ConsumerRecord<String, SevaRequest> getConsumeRecord(ServiceRequest serviceRequest) {
        SevaRequest sevaRequest = new SevaRequest();
        sevaRequest.setServiceRequest(serviceRequest);
        ConsumerRecord<String, SevaRequest> consumerRecord = new ConsumerRecord<>(TEST_CONSUMER_TOPIC, PARTITION, OFFSET, KEY, sevaRequest);
        return consumerRecord;
    }

    private ServiceRequest getServiceRequestWithLatLng(Double lat, Double lng) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setLat(lat);
        serviceRequest.setLng(lng);
        return serviceRequest;
    }

    private ServiceRequest getServiceRequestWithLocationId(String locationId, Double lat, Double lng) {
        Map<String, String> values = new HashMap<>();
        values.put(LOCATION_ID, locationId);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setValues(values);
        serviceRequest.setLat(lat);
        serviceRequest.setLng(lng);
        return serviceRequest;
    }

    private ServiceRequest getServiceRequestWithLocationId(String locationId, String crossHierarchyId) {
        Map<String, String> values = new HashMap<>();
        values.put(LOCATION_ID, locationId);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setValues(values);
        serviceRequest.setCrossHierarchyId(crossHierarchyId);
        return serviceRequest;
    }
}