package org.egov.pgrrest.write.consumer.contracts.request;

import java.util.ArrayList;
import java.util.HashMap;

public class SevaRequestMapFactory {
    public static HashMap<String, Object> create() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("serviceRequestId", "crn");
        serviceRequestMap.put("expectedDatetime", "01-04-2017 13:20:47");
        serviceRequestMap.put("description", "complaint description");
        serviceRequestMap.put("address", "landmark");
        serviceRequestMap.put("firstName", "firstName");
        serviceRequestMap.put("phone", "1234567890");
        serviceRequestMap.put("email", "email@email.com");
        serviceRequestMap.put("serviceCode", "complaintTypeCode");
        serviceRequestMap.put("requestedDatetime", "01-04-2017 13:20:47");
        serviceRequestMap.put("updatedDatetime", "02-04-2017 13:20:47");
        serviceRequestMap.put("tenantId", "ap.public");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        attributeValues.add(toMap("systemReceivingMode", "receiving mode"));
        attributeValues.add(toMap("systemReceivingCenter", "5"));
        attributeValues.add(toMap("systemStateId", "9"));
        attributeValues.add(toMap("systemDepartmentId", "29"));
        attributeValues.add(toMap("systemRequesterAddress", "complainant address"));
        attributeValues.add(toMap("systemStatus", "REGISTERED"));
        attributeValues.add(toMap("systemLocationId", "7"));
        attributeValues.add(toMap("systemChildLocationId", "8"));
        attributeValues.add(toMap("systemPositionId", "6"));
        attributeValues.add(toMap("systemCitizenUserId", "4"));
        serviceRequestMap.put("attribValues", attributeValues);
        sevaRequestMap.put("serviceRequest", serviceRequestMap);
        final HashMap<String, Object> requestInfoMap = new HashMap<>();
        final HashMap<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("type", "CITIZEN");
        userInfoMap.put("id", "3");
        requestInfoMap.put("userInfo", userInfoMap);
        sevaRequestMap.put("RequestInfo", requestInfoMap);
        return sevaRequestMap;
    }

    private static HashMap<String, String> toMap(String key, String code) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("name", code);
        return map;
    }
}
