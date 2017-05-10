package org.egov.pgrrest.write.contracts.grievance;

import java.util.ArrayList;
import java.util.HashMap;

public class SevaRequestMapFactory {
    public static HashMap<String, Object> create() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("serviceRequestId", "crn");
        serviceRequestMap.put("expectedDatetime", 1491205848337L);
        serviceRequestMap.put("description", "complaint description");
        serviceRequestMap.put("address", "landmark");
        serviceRequestMap.put("firstName", "firstName");
        serviceRequestMap.put("phone", "1234567890");
        serviceRequestMap.put("email", "email@email.com");
        serviceRequestMap.put("serviceCode", "complaintTypeCode");
        serviceRequestMap.put("requestedDatetime", "01-04-2017 13:20:47");
        serviceRequestMap.put("updatedDatetime", "02-04-2017 13:20:47");
        serviceRequestMap.put("tenantId", "ap.public");
        serviceRequestMap.put("isAttribValuesPopulated", false);
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        serviceRequestMap.put("attribValues", attributeValues);
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("departmentId", "29");
        valuesMap.put("complainantAddress", "complainant address");
        valuesMap.put("receivingMode", "receiving mode");
        valuesMap.put("receivingCenter", "5");
        valuesMap.put("status", "REGISTERED");
        valuesMap.put("locationId", "7");
        valuesMap.put("childLocationId", "8");
        valuesMap.put("assignmentId", "6");
        valuesMap.put("stateId", "9");
        serviceRequestMap.put("values", valuesMap);
        sevaRequestMap.put("serviceRequest", serviceRequestMap);
        final HashMap<String, Object> requestInfoMap = new HashMap<>();
        final HashMap<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("type", "CITIZEN");
        userInfoMap.put("id", "3");
        requestInfoMap.put("requesterId", "2");
        requestInfoMap.put("userInfo", userInfoMap);
        sevaRequestMap.put("RequestInfo", requestInfoMap);
        return sevaRequestMap;
    }
}
