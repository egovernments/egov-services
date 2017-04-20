package org.pgr.batch.repository.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.egov.common.contract.request.RequestInfo;

import java.util.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequest {

    public static final String VALUES_ASSIGNEE_ID = "assigneeId";
    public static final String VALUES_STATE_ID = "stateId";
    public static final String STATE_DETAILS = "stateDetails";
    private static final String WORKFLOW_TYPE = "Complaint";
    public static final String STATUS = "complaintStatus";
    public static final String VALUES_APPROVAL_COMMENT_KEY = "approvalComments";
    private static final String PREVIOUS_ASSIGNEE = "previousAssignee";
    private static final String ESCALATION_STATUS = "IN PROGRESS";
    private static final String  VALUES_APPROVAL_COMMENT_VALUE= "Complaint is escalated";

    private String tenantId;

    @JsonProperty("service_request_id")
    @Setter
    private String crn;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("service_name")
    private String complaintTypeName;

    @JsonProperty("service_code")
    private String complaintTypeCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("agency_responsible")
    private String agencyResponsible;

    @JsonProperty("service_notice")
    private String serviceNotice;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("requested_datetime")
    @Setter
    private Date createdDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("updated_datetime")
    @Setter
    private Date lastModifiedDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("expected_datetime")
    private Date escalationDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("address_id")
    private String crossHierarchyId;

    @JsonProperty("zipcode")
    private Integer zipcode;

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("lng")
    private Double longitude;

    @JsonProperty("media_urls")
    private List<String> mediaUrls;

    @Setter
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("values")
    private Map<String, String> values = new HashMap<>();

    private void setAssignee(String assignee) {
        getValues().put(VALUES_ASSIGNEE_ID, assignee);
    }

    private void setStateId(String stateId) {
        getValues().put(VALUES_STATE_ID, stateId);
    }

    public WorkflowRequest getWorkFlowRequestForEscalation(RequestInfo requestInfo){
        String complaintType = this.complaintTypeCode;
        String crn = this.getCrn();
        Map<String, Attribute> valuesToSet = getWorkFlowRequestValues(values, complaintType);
        valuesToSet.put(PREVIOUS_ASSIGNEE, Attribute.asStringAttr(PREVIOUS_ASSIGNEE, values.get(VALUES_ASSIGNEE_ID)));

        WorkflowRequest.WorkflowRequestBuilder workflowRequestBuilder = WorkflowRequest.builder()
                .assignee(null)
                .action(WorkflowRequest.Action.forComplaintStatus(values.get(STATUS)))
                .requestInfo(requestInfo)
                .values(valuesToSet)
                .status(ESCALATION_STATUS)
                .type(WORKFLOW_TYPE)
                .businessKey(WORKFLOW_TYPE)
                .tenantId(getTenantId())
                .crn(crn);

        return workflowRequestBuilder.build();
    }

    private Map<String, Attribute> getWorkFlowRequestValues(Map<String, String> values, String complaintType) {
        Map<String, Attribute> valuesToSet = new HashMap<>();
        valuesToSet.put(STATE_DETAILS, Attribute.asStringAttr(STATE_DETAILS, StringUtils.EMPTY));
        valuesToSet.put(VALUES_STATE_ID, Attribute.asStringAttr(VALUES_STATE_ID, getCurrentStateId(values)));
        valuesToSet.put(VALUES_APPROVAL_COMMENT_KEY, Attribute.asStringAttr(VALUES_APPROVAL_COMMENT_KEY, VALUES_APPROVAL_COMMENT_VALUE));
        return valuesToSet;
    }

    private String getCurrentStateId(Map<String, String> values) {
        return Objects.isNull(values.get(VALUES_STATE_ID)) ? null : values.get(VALUES_STATE_ID);
    }

    public void update(WorkflowResponse workflowResponse){
        setAssignee(workflowResponse.getAssignee());
        setStateId(workflowResponse.getValueForKey(VALUES_STATE_ID));
    }

}