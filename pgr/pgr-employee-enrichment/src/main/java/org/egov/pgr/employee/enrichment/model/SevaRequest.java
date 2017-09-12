package org.egov.pgr.employee.enrichment.model;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgr.common.date.DateFormatter;
import org.egov.pgr.employee.enrichment.json.ObjectMapperFactory;
import org.egov.pgr.employee.enrichment.repository.contract.Attribute;
import org.egov.pgr.employee.enrichment.repository.contract.ProcessInstance;
import org.egov.pgr.employee.enrichment.repository.contract.ProcessInstanceRequest;
import org.egov.pgr.employee.enrichment.repository.contract.ProcessInstanceResponse;
import org.egov.pgr.employee.enrichment.repository.contract.Task;
import org.egov.pgr.employee.enrichment.repository.contract.TaskRequest;
import org.egov.pgr.employee.enrichment.repository.contract.TaskResponse;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;

import java.util.*;

public class SevaRequest {

    public final static String SERVICE_REQUEST = "serviceRequest";
    public final static String WF_TYPE_SERVICE_REQUEST = "Service Request";
    public final static String REQUEST_INFO = "RequestInfo";
    public static final String VALUES_POSITION_ID = "systemPositionId";
    public static final String STATE_ID = "stateId";
    public static final String VALUES_STATE_ID = "systemStateId";
    private static final String VALUES_DESIGNATION_ID = "systemDesignationId";
    public static final String VALUES_DEPARTMENT_ID = "systemDepartmentId";
    public static final String VALUES_COMLAINT_TYPE_CODE = "complaintTypeCode";
    public static final String BOUNDARY_ID = "boundaryId";
    public static final String STATE_DETAILS = "stateDetails";
    private static final String WORKFLOW_TYPE = "Complaint";
    public static final String STATUS = "systemStatus";
    public static final String SERVICE_CODE = "serviceCode";
    public static final String SERVICE_NAME = "serviceName";
    public static final String SERVICE_CATEGORY_NAME = "serviceCategoryName";
    public static final String VALUES_LOCATION_ID = "systemLocationId";
    public static final String VALUES_APPROVAL_COMMENT = "systemApprovalComments";
    public static final String VALUES_ACTION = "systemAction";
    public static final String VALUES_KEYWORD = "keyword";
    public static final String VALUES_SENDER_NAME = "systemSenderName";
    public static final String VALUES_EXTRA_INFO = "systemExtraInfo";
    public static final String VALUES_DEPARTMENT_NAME = "systemDepartmentName";
    public static final String VALUES_NATUREOFTASK = "natureOfTask";
    public static final String USER_ROLE = "userRole";
    private static final String SERVICE_REQUEST_ID = "serviceRequestId";
    public static final String DEPARTMENT_ID = "systemDepartmentId";
    private static final String COMPLAINT_CRN = "crn";
    private static final String EXPECTED_DATETIME = "expectedDatetime";
    private static final String TENANT_ID = "tenantId";
    private static final String ESCALATION_HOURS = "systemEscalationHours";
    private static final String ATTRIBUTE_VALUES = "attribValues";
    private static final String ATTRIBUTE_VALUES_KEY_FIELD = "key";
    private static final String ATTRIBUTE_VALUES_NAME_FIELD = "name";
    private static final String POST = "POST";
    public static final String SYSTEM_KEYWORD = "keyword";

    private HashMap<String, Object> sevaRequestMap;

    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequestMap = sevaRequestMap;
    }

    private void setAssignee(String assignee) {
        createOrUpdateAttributeEntry(VALUES_POSITION_ID, assignee);
    }

    private void setStateId(String stateId) {
        createOrUpdateAttributeEntry(VALUES_STATE_ID, stateId);
    }

    public void setEscalationHours(String escalationHours) {
        createOrUpdateAttributeEntry(ESCALATION_HOURS, escalationHours);
    }

    private RequestInfo getRequestInfo() {
        return ObjectMapperFactory.create().convertValue(sevaRequestMap.get(REQUEST_INFO), RequestInfo.class);
    }

    public HashMap<String, Object> getRequestMap() {
        return sevaRequestMap;
    }

    public WorkflowRequest getWorkFlowRequest() {
        HashMap<String, Object> serviceRequest = getServiceRequest();
        RequestInfo requestInfo = getRequestInfo();
        String complaintType = (String) serviceRequest.get(SERVICE_CODE);
        String crn = (String) serviceRequest.get(SERVICE_REQUEST_ID);
        Map<String, Attribute> valuesToSet = getWorkFlowRequestValues(complaintType);
        valuesToSet.put(COMPLAINT_CRN, Attribute.asStringAttr(COMPLAINT_CRN, crn));
        final String status = getDynamicSingleValue(STATUS);
        WorkflowRequest.WorkflowRequestBuilder workflowRequestBuilder = WorkflowRequest.builder()
            .positionId(getCurrentPositionId())
            .action(WorkflowRequest.Action.forComplaintStatus(status, isCreate()))
            .requestInfo(requestInfo)
            .values(valuesToSet)
            .status(status)
            .type(WORKFLOW_TYPE)
            .businessKey(WORKFLOW_TYPE)
            .tenantId(getTenantId())
            .crn(crn);

        return workflowRequestBuilder.build();
    }

    private Map<String, Attribute> getWorkFlowRequestValues(String complaintType) {
        final String locationId = getDynamicSingleValue(VALUES_LOCATION_ID);
        final String approvalComment = getDynamicSingleValue(VALUES_APPROVAL_COMMENT);
        final String departmentId = getDynamicSingleValue(DEPARTMENT_ID);
        Map<String, Attribute> valuesToSet = new HashMap<>();
        valuesToSet.put(VALUES_COMLAINT_TYPE_CODE, Attribute.asStringAttr(VALUES_COMLAINT_TYPE_CODE, complaintType));
        valuesToSet.put(BOUNDARY_ID, Attribute.asStringAttr(BOUNDARY_ID, locationId));
        valuesToSet.put(STATE_DETAILS, Attribute.asStringAttr(STATE_DETAILS, StringUtils.EMPTY));
        valuesToSet.put(USER_ROLE, Attribute.asStringAttr(USER_ROLE, getUserType()));
        valuesToSet.put(VALUES_STATE_ID, Attribute.asStringAttr(VALUES_STATE_ID, getCurrentStateId()));
        valuesToSet.put(VALUES_APPROVAL_COMMENT, Attribute.asStringAttr(VALUES_APPROVAL_COMMENT, approvalComment));
        valuesToSet.put(DEPARTMENT_ID, Attribute.asStringAttr(DEPARTMENT_ID, departmentId));
        valuesToSet.put(SYSTEM_KEYWORD, Attribute.asStringAttr(SYSTEM_KEYWORD, getKeyword()));
        return valuesToSet;
    }

    public ProcessInstanceRequest getProcessInstanceRequest() {
        ProcessInstanceRequest request = new ProcessInstanceRequest();
        ProcessInstance processInstance = new ProcessInstance();

        processInstance.setBusinessKey(getServiceName());
        processInstance.setType(WF_TYPE_SERVICE_REQUEST);
        processInstance.setComments(getApprovalComments());
        processInstance.setTenantId(getTenantId());
        processInstance.setAssignee(new org.egov.pgr.employee.enrichment.repository.contract.Position());
        processInstance.getAssignee().setId(getCurrentPositionId());
        processInstance.setSenderName(getSenderName());
        processInstance.setDetails(getExtraInfo());
        processInstance.setStatus(getStatus());
        processInstance.getAttributes().put(SERVICE_CATEGORY_NAME, Attribute.asStringAttr(SERVICE_CATEGORY_NAME, getServiceCategoryName()));
        processInstance.getAttributes().put(VALUES_NATUREOFTASK, Attribute.asStringAttr(VALUES_NATUREOFTASK, getServiceName()));
        processInstance.getAttributes().put(SERVICE_REQUEST_ID, Attribute.asStringAttr(SERVICE_REQUEST_ID, getServiceRequestId()));
        processInstance.getAttributes().put(VALUES_COMLAINT_TYPE_CODE, Attribute.asStringAttr(VALUES_COMLAINT_TYPE_CODE, getComplaintTypeCode()));
        processInstance.getAttributes().put(BOUNDARY_ID, Attribute.asStringAttr(BOUNDARY_ID, getLocationId()));

        request.setRequestInfo(getRequestInfo());
        request.setProcessInstance(processInstance);

        return request;
    }

    public TaskRequest getTaskRequest() {
        TaskRequest request = new TaskRequest();
        Task task = new Task();

        task.getAttributes().put(STATE_ID, Attribute.asStringAttr(STATE_ID, getCurrentStateId()));
        task.setId(getCurrentStateId());
        task.setBusinessKey(getServiceName());
        task.setType(WF_TYPE_SERVICE_REQUEST);
        task.setComments(getApprovalComments());
        task.setAction(getAction());
        task.setStatus(getStatus());
        task.setTenantId(getTenantId());
        task.setAssignee(new org.egov.pgr.employee.enrichment.repository.contract.Position());
        task.getAssignee().setId(getCurrentPositionId());
        task.getAttributes().put(SERVICE_CATEGORY_NAME, Attribute.asStringAttr(SERVICE_CATEGORY_NAME, getServiceCategoryName()));

        request.setRequestInfo(getRequestInfo());
        request.setTask(task);

        return request;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> getServiceRequest() {
        return (HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST);
    }

    public void update(WorkflowResponse workflowResponse) {
        setAssignee(workflowResponse.getPositionId());
        setStateId(workflowResponse.getValueForKey(VALUES_STATE_ID));
    }

    public void update(ProcessInstanceResponse processInstanceResponse) {
        setAssignee(processInstanceResponse.getProcessInstance().getOwner().getId().toString());
        setStateId(processInstanceResponse.getProcessInstance().getValueForKey(STATE_ID));
    }

    public void update(TaskResponse taskResponse) {
        setAssignee(taskResponse.getTask().getOwner().getId().toString());
        setStateId(taskResponse.getTask().getValueForKey(STATE_ID));
    }

    public void update(Position position) {
        setDesignation(position.getDesignationId());
        setDepartment(position.getDepartmentId());
    }

    public void setEscalationDate(Date date) {
        getServiceRequest().put(EXPECTED_DATETIME, DateFormatter.toString(date));
    }

    public String getComplaintTypeCode() {
        return (String) this.getServiceRequest().get(SERVICE_CODE);
    }

    public String getLocationId() {
        return getDynamicSingleValue(VALUES_LOCATION_ID);
    }

    public String getKeyword() {
        return getDynamicSingleValue(VALUES_KEYWORD);
    }

    public String getServiceName() {
        return (String) getServiceRequest().get(SERVICE_NAME);
    }

    public String getServiceRequestId() {
        return (String) getServiceRequest().get(SERVICE_REQUEST_ID);
    }

    public String getApprovalComments() {
        return getDynamicSingleValue(VALUES_APPROVAL_COMMENT);
    }

    public String getAction() {
        return getDynamicSingleValue(VALUES_ACTION);
    }

    public String getStatus() {
        return getDynamicSingleValue(STATUS);
    }

    public String getSenderName() {
        return getDynamicSingleValue(VALUES_SENDER_NAME);
    }

    public String getExtraInfo() {
        return getDynamicSingleValue(VALUES_EXTRA_INFO);
    }

    public String getDepartmentName() {
        return getDynamicSingleValue(VALUES_DEPARTMENT_NAME);
    }

    public String getServiceCategoryName() {
        return (String) getServiceRequest().get(SERVICE_CATEGORY_NAME);
    }

    public String getTenantId() {
        return (String) getServiceRequest().get(TENANT_ID);
    }

    private String getUserType() {
        final User userInfo = getRequestInfo().getUserInfo();
        return userInfo != null ? userInfo.getType() : null;
    }

    private String getCurrentStateId() {
        return getDynamicSingleValue(VALUES_STATE_ID);
    }

    public Long getCurrentPositionId() {
        final String positionId = getDynamicSingleValue(VALUES_POSITION_ID);
        return positionId != null ? Long.valueOf(String.valueOf(positionId)) : null;
    }

    public boolean isCreate() {
        return POST.equalsIgnoreCase(this.getRequestInfo().getAction());
    }

    public boolean isWorkflowCreate() {
        return getAction() != null && getAction().equalsIgnoreCase("create");
    }

    public void setDesignation(String designationId) {
        createOrUpdateAttributeEntry(VALUES_DESIGNATION_ID, designationId);
    }

    public String getDesignation() {
        return getDynamicSingleValue(VALUES_DESIGNATION_ID);
    }

    public void setDepartment(String departmentId) {
        createOrUpdateAttributeEntry(VALUES_DEPARTMENT_ID, departmentId);
    }

    @SuppressWarnings("unchecked")
    private List<HashMap<String, String>> getAttributeValues() {
        HashMap<String, Object> serviceRequest = getServiceRequest();
        final List<HashMap<String, String>> attributeValues = serviceRequest != null ?
            (List<HashMap<String, String>>) serviceRequest.get(ATTRIBUTE_VALUES) : new ArrayList<>();
        return attributeValues == null ? new ArrayList<>() : attributeValues;
    }

    public String getDynamicSingleValue(String key) {
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