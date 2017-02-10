package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.egov.pgr.employee.enrichment.consumer.contract.ServiceRequest.*;
import static org.egov.pgr.employee.enrichment.consumer.contract.SevaRequest.SERVICE_REQUEST;

@Getter
@Builder
public class WorkflowRequest {

    private static final String VALUES_COMLAINT_TYPE_CODE = "complaint_type_code";
    private static final String BOUNDARY_ID = "boundary_id";
    public static final String WORKFLOW_TYPE = "Complaint";

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("object_id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("description")
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",timezone = "IST")
    @JsonProperty("created_Date")
    private Date createdDate;

    @JsonProperty("last_updated")
    private Date lastupdatedSince;

    @JsonProperty("status")
    private String status;

    @JsonProperty("action")
    private String action;

    @JsonProperty("business_key")
    private String businessKey;

    @JsonProperty("assignee")
    private Long assignee;

    @JsonProperty("group")
    private String group;

    @JsonProperty("sender_name")
    private String senderName;

    @JsonProperty("state_id")
    private Long stateId;

    @JsonProperty("state_details")
    private String stateDetails;

    @JsonProperty("values")
    private Map<String, String> values;

    public static WorkflowRequest fromComplaintHash(Map complaintRequestHash) {
        Map serviceRequest = (Map) complaintRequestHash.get(SERVICE_REQUEST);
        RequestInfo requestInfo = new ObjectMapper().convertValue(complaintRequestHash.get(REQUEST_INFO), RequestInfo.class);
        Map values = (Map) serviceRequest.get(VALUES);
        String boundaryId = (String) values.get(VALUES_LOCATION_ID);
        String complaintType = (String) serviceRequest.get(SERVICE_CODE);
        String status = (String) serviceRequest.get(STATUS);
        Long currentAssignee = values.get(VALUES_ASSIGNEE_ID) != null ? Long.valueOf(String.valueOf(values.get(VALUES_ASSIGNEE_ID))) : null;
        Map<String, String > valuesToSet = new HashMap<>();
        valuesToSet.put(VALUES_COMLAINT_TYPE_CODE, complaintType);
        valuesToSet.put(BOUNDARY_ID, boundaryId);
        return WorkflowRequest.builder().assignee(currentAssignee).action(requestInfo.getAction())
                .requestInfo(requestInfo).values(valuesToSet).status(status).type(WORKFLOW_TYPE).build();
    }
}
