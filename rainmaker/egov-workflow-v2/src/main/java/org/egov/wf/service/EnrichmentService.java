package org.egov.wf.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.web.models.AuditDetails;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service
public class EnrichmentService {


    private WorkflowUtil util;

    private UserService userService;

    private TransitionService transitionService;

    @Autowired
    public EnrichmentService(WorkflowUtil util, UserService userService,TransitionService transitionService) {
        this.util = util;
        this.userService = userService;
        this.transitionService = transitionService;
    }




    /**
     * Enriches incoming request
     * @param requestInfo The RequestInfo of the request
     * @param processStateAndActions List of ProcessStateAndAction containing ProcessInstance to be created
     */
    public void enrichProcessRequest(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions){
        AuditDetails auditDetails = util.getAuditDetails(requestInfo.getUserInfo().getUuid(),true);
        processStateAndActions.forEach(processStateAndAction -> {
            String tenantId = processStateAndAction.getProcessInstance().getTenantId();
            processStateAndAction.getProcessInstance().setId(UUID.randomUUID().toString());
            processStateAndAction.getProcessInstance().setAuditDetails(auditDetails);
            processStateAndAction.getProcessInstance().setAssigner(requestInfo.getUserInfo());
            if(!CollectionUtils.isEmpty(processStateAndAction.getProcessInstance().getDocuments())){
                processStateAndAction.getProcessInstance().getDocuments().forEach(document -> {
                    document.setAuditDetails(auditDetails);
                    document.setTenantId(tenantId);
                    document.setId(UUID.randomUUID().toString());
                });
            }
            setNextActions(requestInfo,processStateAndActions,true);
        });
        enrichUsers(processStateAndActions);
    }





    /**
     * Enriches the processInstance with next possible action depending on current currentState
     * @param requestInfo The RequestInfo of the request
     * @param processStateAndActions
     */
    private void setNextActions(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions,Boolean isTransition){
        List<Role> roles = requestInfo.getUserInfo().getRoles();

        processStateAndActions.forEach(processStateAndAction -> {
            State state;
            if(isTransition)
             state = processStateAndAction.getResultantState();
            else state = processStateAndAction.getCurrentState();
            List<String> nextAction = new LinkedList<>();
            state.getActions().forEach(action -> {
                if(util.isRoleAvailable(roles,action.getRoles()))
                    nextAction.add(action.getAction());
            });
            processStateAndAction.getProcessInstance().setNextActions(nextAction);
        });
    }

    /**
     * Enriches the assignee and assigner user object from user search response
     * @param processStateAndActions The List of ProcessStateAndAction containing processInstance to be enriched
     */
    public void enrichUsers(List<ProcessStateAndAction> processStateAndActions){
        List<String> uuids = new LinkedList<>();
        processStateAndActions.forEach(processStateAndAction -> {
            if(processStateAndAction.getProcessInstance().getAssignee()!=null)
                uuids.add(processStateAndAction.getProcessInstance().getAssignee().getUuid());
            uuids.add(processStateAndAction.getProcessInstance().getAssigner().getUuid());
        });

        Map<String,User> idToUserMap = userService.searchUser(uuids);
        Map<String,String> errorMap = new HashMap<>();
        processStateAndActions.forEach(processStateAndAction -> {
            User assignee=null,assigner;
            if(processStateAndAction.getProcessInstance().getAssignee()!=null)
                 assignee = idToUserMap.get(processStateAndAction.getProcessInstance().getAssignee().getUuid());
            assigner = idToUserMap.get(processStateAndAction.getProcessInstance().getAssigner().getUuid());
            if(processStateAndAction.getProcessInstance().getAssignee()!=null && assignee==null)
                errorMap.put("INVALID UUID","User not found for uuid: "+processStateAndAction.getProcessInstance().getAssignee().getUuid());
            if(assigner==null)
                errorMap.put("INVALID UUID","User not found for uuid: "+processStateAndAction.getProcessInstance().getAssigner().getUuid());
            processStateAndAction.getProcessInstance().setAssignee(assignee);
            processStateAndAction.getProcessInstance().setAssigner(assigner);
        });
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    /**
     * Enriches processInstance from the search response
     * @param processInstances The list of processInstances from search
     */
    public void enrichUsersFromSearch(List<ProcessInstance> processInstances){
        List<String> uuids = new LinkedList<>();
        processInstances.forEach(processInstance -> {
            if(processInstance.getAssignee()!=null)
                uuids.add(processInstance.getAssignee().getUuid());
            uuids.add(processInstance.getAssigner().getUuid());
        });
        Map<String,User> idToUserMap = userService.searchUser(uuids);
        Map<String,String> errorMap = new HashMap<>();
        processInstances.forEach(processInstance -> {
            User assignee=null,assigner;
            if(processInstance.getAssignee()!=null)
                assignee = idToUserMap.get(processInstance.getAssignee().getUuid());
            assigner = idToUserMap.get(processInstance.getAssigner().getUuid());
            if(processInstance.getAssignee()!=null && assignee==null)
                errorMap.put("INVALID UUID","User not found for uuid: "+processInstance.getAssignee().getUuid());
            if(assigner==null)
                errorMap.put("INVALID UUID","User not found for uuid: "+processInstance.getAssigner().getUuid());
            processInstance.setAssignee(assignee);
            processInstance.setAssigner(assigner);
        });
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    public void enrichNextActionForSearch(RequestInfo requestInfo,List<ProcessInstance> processInstances){
        List<ProcessStateAndAction> processStateAndActions =
                transitionService.getProcessStateAndActions(new ProcessInstanceRequest(requestInfo,processInstances),false);
        setNextActions(requestInfo,processStateAndActions,false);
    }


    /**
     * Enriches the incoming list of businessServices
     * @param request The BusinessService request to be enriched
     */
    public void enrichBusinessService(BusinessServiceRequest request){
        RequestInfo requestInfo = request.getRequestInfo();
        List<BusinessService> businessServices = request.getBusinessServices();
        AuditDetails auditDetails = util.getAuditDetails(requestInfo.getUserInfo().getUuid(),true);
        businessServices.forEach(businessService -> {
            businessService.setUuid(UUID.randomUUID().toString());
            businessService.setAuditDetails(auditDetails);
            businessService.getStates().forEach(state -> {
                state.setAuditDetails(auditDetails);
                state.setUuid(UUID.randomUUID().toString());
                if(!CollectionUtils.isEmpty(state.getActions()))
                    state.getActions().forEach(action -> {
                        action.setAuditDetails(auditDetails);
                        action.setUuid(UUID.randomUUID().toString());
                        action.setCurrentState(state.getUuid());
                    });
            });
            enrichNextState(businessService);
        });
    }


    /**
     * Enriches the nextState varibale in BusinessService
     * @param businessService The businessService whose action objects are to be enriched
     */
    private void enrichNextState(BusinessService businessService){
        Map<String,String> statusToUuidMap = new HashMap<>();
        businessService.getStates().forEach(state -> {
            statusToUuidMap.put(state.getState(),state.getUuid());
        });
        businessService.getStates().forEach(state -> {
            if(!CollectionUtils.isEmpty(state.getActions())){
                state.getActions().forEach(action -> {
                    action.setNextState(statusToUuidMap.get(action.getNextState()));
                });
            }
        });
    }


}
