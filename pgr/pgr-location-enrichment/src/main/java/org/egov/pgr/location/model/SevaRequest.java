package org.egov.pgr.location.model;

import org.egov.pgr.location.contract.BoundaryResponse;
import org.egov.pgr.location.contract.CrossHierarchyResponse;
import org.egov.pgr.location.contract.ServiceRequest;
import org.egov.pgr.location.json.ObjectMapperFactory;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class SevaRequest {

    private static final String LOCATION_ID = "systemLocationId";
    private static final String LOCATION_NAME = "systemLocationName";
    private static final String CHILD_LOCATION_ID = "systemChildLocationId";
    private final static String SERVICE_REQUEST = "serviceRequest";
    private static final String ATTRIBUTE_VALUES = "attribValues";
    private static final String ATTRIBUTE_VALUES_KEY_FIELD = "key";
    private static final String ATTRIBUTE_VALUES_NAME_FIELD = "name";

    private final ServiceRequest serviceRequestObject;

    private HashMap<String, Object> sevaRequestMap;

    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequestMap = sevaRequestMap;
        this.serviceRequestObject = ObjectMapperFactory.create()
            .convertValue(sevaRequestMap.get(SERVICE_REQUEST), ServiceRequest.class);
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> getServiceRequest() {
        return (HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST);
    }

    public void update(BoundaryResponse response) {
        final String location = String.valueOf(response.getId());
        createOrUpdateAttributeEntry(LOCATION_ID, location);
        final String locationName = response.getName();
        createOrUpdateAttributeEntry(LOCATION_NAME, locationName);
    }

    public void update(CrossHierarchyResponse hierarchyResponse) {
        final String locationId = hierarchyResponse.getLocationId();
        createOrUpdateAttributeEntry(LOCATION_ID, locationId);
        final String locationName = hierarchyResponse.getLocationName();
        createOrUpdateAttributeEntry(LOCATION_NAME, locationName);
        final String childLocationId = hierarchyResponse.getChildLocationId();
        createOrUpdateAttributeEntry(CHILD_LOCATION_ID, childLocationId);
    }

    public String getLocationId() {
        return getDynamicSingleValue(LOCATION_ID);
    }

    public String getTenantId() {
        return serviceRequestObject.getTenantId();
    }

    public String getLocationName() {
        return getDynamicSingleValue(LOCATION_NAME);
    }

    public String getChildLocationId() {
        return getDynamicSingleValue(CHILD_LOCATION_ID);
    }

    public String getLatitude() {
        return serviceRequestObject.getLatitude();
    }

    public String getLongitude() {
        return serviceRequestObject.getLongitude();
    }

    public boolean isCrossHierarchyIdPresent() {
        return serviceRequestObject.isCrossHierarchyIdPresent();
    }

    public String getCrossHierarchyId() {
        return serviceRequestObject.getCrossHierarchyId();
    }

    public boolean isLocationCoordinatesPresent() {
        return serviceRequestObject.isLocationCoordinatesPresent();
    }

    public boolean isLocationPresent() {
        return isNotEmpty(getDynamicSingleValue(LOCATION_ID));
    }

    public HashMap<String, Object> getRequestMap() {
        return sevaRequestMap;
    }

    @SuppressWarnings("unchecked")
    private List<HashMap<String, String>> getAttributeValues() {
        HashMap<String, Object> serviceRequest = getServiceRequest();
        final List<HashMap<String, String>> attributeValues =
            (List<HashMap<String, String>>) serviceRequest.get(ATTRIBUTE_VALUES);
        return attributeValues == null ? new ArrayList<>() : attributeValues;
    }

    private String getDynamicSingleValue(String key) {
        return getAttributeValues().stream()
            .filter(attribute -> key.equals(attribute.get(ATTRIBUTE_VALUES_KEY_FIELD)))
            .findFirst()
            .map(attribute -> attribute.get(ATTRIBUTE_VALUES_NAME_FIELD))
            .orElse(null);
    }

    private boolean isAttributeKeyPresent(String key) {
        return getAttributeValues().stream()
            .anyMatch(attribute -> key.equals(attribute.get(ATTRIBUTE_VALUES_KEY_FIELD)));
    }

    private void createOrUpdateAttributeEntry(String key, String name) {
        if (isAttributeKeyPresent(key)) {
            updateAttributeEntry(key, name);
        } else {
            createAttributeEntry(key, name);
        }
    }

    private void createAttributeEntry(String key, String name) {
        final HashMap<String, String> entry = new HashMap<>();
        entry.put(ATTRIBUTE_VALUES_KEY_FIELD, key);
        entry.put(ATTRIBUTE_VALUES_NAME_FIELD, name);
        getAttributeValues().add(entry);
    }

    private void updateAttributeEntry(String key, String name) {
        final HashMap<String, String> matchingEntry = getAttributeValues().stream()
            .filter(attribute -> key.equals(attribute.get(ATTRIBUTE_VALUES_KEY_FIELD)))
            .findFirst()
            .orElse(null);
        matchingEntry.put(ATTRIBUTE_VALUES_NAME_FIELD, name);
    }
}