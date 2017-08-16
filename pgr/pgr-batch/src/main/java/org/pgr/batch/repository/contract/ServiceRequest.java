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

    public static final String VALUES_POSITION_ID = "systemPositionId";
    public static final String VALUES_ESCALATED_FLAG = "systemIsEscalated";
    public static final String VALUES_STATE_ID = "systemStateId";
    public static final String STATE_DETAILS = "systemStateDetails";
    private static final String WORKFLOW_TYPE = "Complaint";
    public static final String STATUS = "systemStatus";
    public static final String VALUES_APPROVAL_COMMENT_KEY = "systemApprovalComments";
    public static final String PREVIOUS_ASSIGNEE = "systemPreviousAssignee";
    private static final String ESCALATION_STATUS = "status";
    private static final String VALUES_APPROVAL_COMMENT_VALUE = "Complaint is escalated";
    private static final String VALUES_DESIGNATION_ID = "systemDesignationId";
    private static final String VALUES_DEPARTMENT_ID = "systemDepartmentId";
    private static final String ESCALATION_HOURS = "systemEscalationHours";
    private static final String TRUE = "true";
    public static final String COMPLAINT_TYPE_CODE = "complaintTypeCode";

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

    @JsonProperty("mediaUrls")
    private List<String> mediaUrls;

    @Setter
    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("deviceId")
    private String deviceId;

    @JsonProperty("accountId")
    private String accountId;

    private List<AttributeEntry> attribValues = new ArrayList<>();

    private void setAssignee(String assignee) {
        createOrUpdateAttributeEntry(attribValues, VALUES_POSITION_ID, assignee);
    }

    private void setStateId(String stateId) {
        createOrUpdateAttributeEntry(attribValues, VALUES_STATE_ID, stateId);
    }

    public void setPreviousAssignee(String previousAssignee) {
        createOrUpdateAttributeEntry(attribValues, PREVIOUS_ASSIGNEE, previousAssignee);
    }

    @JsonIgnore
    public String getPositionId() {
        return getDynamicSingleValue(VALUES_POSITION_ID);
    }

    public void setPositionId(String assigneeId) {
        createOrUpdateAttributeEntry(attribValues, VALUES_POSITION_ID, assigneeId);
    }

    public void setEscalatedFlag() {
        createOrUpdateAttributeEntry(attribValues, VALUES_ESCALATED_FLAG, TRUE);
    }

    public boolean isEscalated() {
        return TRUE.equals(getDynamicSingleValue(VALUES_ESCALATED_FLAG));
    }

    public void setDesignation(String designationId) {
        createOrUpdateAttributeEntry(attribValues, VALUES_DESIGNATION_ID, designationId);
    }

    public void setDepartment(String departmentId) {
        createOrUpdateAttributeEntry(attribValues, VALUES_DEPARTMENT_ID, departmentId);
    }

    public void setEscalationHours(String escalationHours) {
        createOrUpdateAttributeEntry(attribValues, ESCALATION_HOURS, escalationHours);
    }

    @JsonIgnore
    public String getEscalationHours() {
        return getDynamicSingleValue(ESCALATION_HOURS);
    }

    public WorkflowRequest getWorkFlowRequestForEscalation(RequestInfo requestInfo) {
        String crn = this.getCrn();
        Map<String, Attribute> valuesToSet = getWorkFlowRequestValues();
        valuesToSet.put(PREVIOUS_ASSIGNEE, Attribute.asStringAttr(PREVIOUS_ASSIGNEE, getDynamicSingleValue
                (VALUES_POSITION_ID)));
        valuesToSet.put(COMPLAINT_TYPE_CODE, Attribute.asStringAttr("complaintTypeCode", complaintTypeCode));
        WorkflowRequest.WorkflowRequestBuilder workflowRequestBuilder = WorkflowRequest.builder()
                .assignee(null)
                .action(WorkflowRequest.Action.forComplaintStatus(getDynamicSingleValue(STATUS)))
                .requestInfo(requestInfo)
                .values(valuesToSet)
                .status(getDynamicSingleValue(ESCALATION_STATUS))
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
        valuesToSet.put(VALUES_APPROVAL_COMMENT_KEY, Attribute.asStringAttr(VALUES_APPROVAL_COMMENT_KEY,
                VALUES_APPROVAL_COMMENT_VALUE));
        return valuesToSet;
    }

    private String getCurrentStateId() {
        return Objects.isNull(getDynamicSingleValue(VALUES_STATE_ID)) ? null : getDynamicSingleValue(VALUES_STATE_ID);
    }

    public String getDesignation() {
        return getDynamicSingleValue(VALUES_DESIGNATION_ID);
    }

    public void update(WorkflowResponse workflowResponse) {
        setAssignee(workflowResponse.getAssignee());
        setStateId(workflowResponse.getValueForKey(VALUES_STATE_ID));
    }

    public void update(Position position) {
        setDesignation(position.getDesignationId());
        setDepartment(position.getDepartmentId());
    }

    public boolean isAttributeEntryPresent(String key) {
        return getDynamicSingleValue(key) != null;
    }

    public boolean isNewAssigneeSameAsPreviousAssignee() {
        return Objects.equals(getPositionId(), getPreviousAssignee());
    }

    private String getPreviousAssignee() {
        return getDynamicSingleValue(PREVIOUS_ASSIGNEE);
    }

    private String getDynamicSingleValue(String key) {
        return AttributeValues.getAttributeSingleValue(attribValues, key);
    }
}