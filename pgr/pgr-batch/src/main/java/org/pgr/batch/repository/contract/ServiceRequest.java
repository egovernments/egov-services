package org.pgr.batch.repository.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgr.common.contract.AttributeValues;
import org.pgr.batch.service.model.Position;

import java.util.*;

import static org.egov.pgr.common.contract.AttributeValues.createOrUpdateAttributeEntry;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequest {

    public static final String VALUES_ASSIGNEE_ID = "assignmentId";
    public static final String VALUES_STATE_ID = "stateId";
    public static final String STATE_DETAILS = "stateDetails";
    private static final String WORKFLOW_TYPE = "Complaint";
    public static final String STATUS = "status";
    public static final String VALUES_APPROVAL_COMMENT_KEY = "approvalComments";
    private static final String PREVIOUS_ASSIGNEE = "previousAssignee";
    private static final String ESCALATION_STATUS = "IN PROGRESS";
    private static final String  VALUES_APPROVAL_COMMENT_VALUE= "Complaint is escalated";
    private static final String VALUES_DESIGNATION_ID = "designationId";
    private static final String VALUES_DEPARTMENT_ID = "departmentId";
    private static final String ESCALATION_HOURS = "escalationHours";

    private String tenantId;

    @JsonProperty("serviceRequestId")
    @Setter
    private String crn;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("serviceName")
    private String complaintTypeName;

    @JsonProperty("serviceCode")
    private String complaintTypeCode;

    private String description;

    private String agencyResponsible;

    private String serviceNotice;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("requestedDatetime")
    @Setter
    private Date createdDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("updatedDatetime")
    @Setter
    private Date lastModifiedDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("expectedDatetime")
    @Setter
    private Date escalationDate;

    private String address;

    @JsonProperty("addressId")
    private String crossHierarchyId;

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

    //  Short term feature flag - to support values and attribValues usage
//  This flag should be set by the consumer for the service to consider attribValues instead of existing values field.
    @JsonProperty("isAttribValuesPopulated")
    private boolean attribValuesPopulated;

    private List<AttributeEntry> attribValues = new ArrayList<>();

    private void setAssignee(String assignee) {
        createOrUpdateAttributeEntry(attribValues,VALUES_ASSIGNEE_ID,assignee);
    }

    private void setStateId(String stateId) {
        createOrUpdateAttributeEntry(attribValues,VALUES_STATE_ID,stateId);
    }

    public void setPreviousAssignee(String previousAssignee){
        createOrUpdateAttributeEntry(attribValues,PREVIOUS_ASSIGNEE,previousAssignee);
    }

    @JsonIgnore
    public String getAssigneeId(){
        return getDynamicSingleValue(VALUES_ASSIGNEE_ID);
    }

    public void setAssigneeId(String assigneeId){
        createOrUpdateAttributeEntry(attribValues,VALUES_ASSIGNEE_ID,assigneeId);
    }

    public void setDesignation(String designationId) {
        createOrUpdateAttributeEntry(attribValues,VALUES_DESIGNATION_ID, designationId);
    }

    public void setDepartment(String departmentId) {
        createOrUpdateAttributeEntry(attribValues,VALUES_DEPARTMENT_ID, departmentId);
    }

    public void setEscalationHours(String escalationHours) {
        createOrUpdateAttributeEntry(attribValues,ESCALATION_HOURS, escalationHours);
    }

    @JsonIgnore
    public String getEscalationHours(){
        return getDynamicSingleValue(ESCALATION_HOURS);
    }

    public WorkflowRequest getWorkFlowRequestForEscalation(RequestInfo requestInfo){
        String crn = this.getCrn();
        Map<String, Attribute> valuesToSet = getWorkFlowRequestValues();
        valuesToSet.put(PREVIOUS_ASSIGNEE, Attribute.asStringAttr(PREVIOUS_ASSIGNEE, getDynamicSingleValue(VALUES_ASSIGNEE_ID)));

        WorkflowRequest.WorkflowRequestBuilder workflowRequestBuilder = WorkflowRequest.builder()
                .assignee(null)
                .action(WorkflowRequest.Action.forComplaintStatus(getDynamicSingleValue(STATUS)))
                .requestInfo(requestInfo)
                .values(valuesToSet)
                .status(ESCALATION_STATUS)
                .type(WORKFLOW_TYPE)
                .businessKey(WORKFLOW_TYPE)
                .tenantId(getTenantId())
                .crn(crn);

        return workflowRequestBuilder.build();
    }

    private Map<String, Attribute> getWorkFlowRequestValues() {
        Map<String, Attribute> valuesToSet = new HashMap<>();
        valuesToSet.put(STATE_DETAILS, Attribute.asStringAttr(STATE_DETAILS, StringUtils.EMPTY));
        valuesToSet.put(VALUES_STATE_ID, Attribute.asStringAttr(VALUES_STATE_ID, getCurrentStateId()));
        valuesToSet.put(VALUES_APPROVAL_COMMENT_KEY, Attribute.asStringAttr(VALUES_APPROVAL_COMMENT_KEY, VALUES_APPROVAL_COMMENT_VALUE));
        return valuesToSet;
    }

    private String getCurrentStateId() {
        return Objects.isNull(getDynamicSingleValue(VALUES_STATE_ID)) ? null : getDynamicSingleValue(VALUES_STATE_ID);
    }

    public String getDesignation() {
        return getDynamicSingleValue(VALUES_DESIGNATION_ID);
    }

    public void update(WorkflowResponse workflowResponse){
        setAssignee(workflowResponse.getAssignee());
        setStateId(workflowResponse.getValueForKey(VALUES_STATE_ID));
    }

    public void update(Position position){
        setDesignation(position.getDesignationId());
        setDepartment(position.getDepartmentId());
    }

    private String getDynamicSingleValue(String key) {
        if (attribValuesPopulated) {
            return AttributeValues.getAttributeSingleValue(attribValues, key);
        } else {
            return values.get(key);
        }
    }
}