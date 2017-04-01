package org.egov.pgr.write.contracts.grievance;

import java.util.HashMap;

public class SevaRequestMapFactory {
    public static HashMap<String, Object> create() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("service_request_id", "crn");
        serviceRequestMap.put("expected_datetime", 1491205848337L);
        serviceRequestMap.put("description", "complaint description");
        serviceRequestMap.put("address", "landmark");
        serviceRequestMap.put("first_name", "firstName");
        serviceRequestMap.put("phone", "1234567890");
        serviceRequestMap.put("email", "email@email.com");
        serviceRequestMap.put("service_code", "complaintTypeCode");
        serviceRequestMap.put("requested_datetime", "01-04-2017 13:20:47");
        serviceRequestMap.put("updated_datetime", "02-04-2017 13:20:47");
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("designationId", "29");
        valuesMap.put("complainantAddress", "complainant address");
        valuesMap.put("userId", "3");
        valuesMap.put("receivingMode", "receiving mode");
        valuesMap.put("receivingCenter", "5");
        valuesMap.put("status", "REGISTERED");
        valuesMap.put("locationId", "7");
        valuesMap.put("child_location_id", "8");
        valuesMap.put("assignment_id", "6");
        valuesMap.put("stateId", "9");
        serviceRequestMap.put("values", valuesMap);
        sevaRequestMap.put("ServiceRequest", serviceRequestMap);
        final HashMap<String, Object> requestInfoMap = new HashMap<>();
        requestInfoMap.put("requester_id", "2");
        sevaRequestMap.put("RequestInfo", requestInfoMap);
        return sevaRequestMap;
    }
}
