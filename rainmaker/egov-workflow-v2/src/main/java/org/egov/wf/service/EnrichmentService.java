package org.egov.wf.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.web.models.AuditDetails;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@Service
public class EnrichmentService {


    private WorkflowUtil util;

    @Autowired
    public EnrichmentService(WorkflowUtil util) {
        this.util = util;
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
            processStateAndAction.getProcessInstance().setAssigner(requestInfo.getUserInfo().getUuid());
            if(!CollectionUtils.isEmpty(processStateAndAction.getProcessInstance().getDocuments())){
                processStateAndAction.getProcessInstance().getDocuments().forEach(document -> {
                    document.setAuditDetails(auditDetails);
                    document.setTenantId(tenantId);
                    document.setId(UUID.randomUUID().toString());
                });
            }
            setNextActions(requestInfo,processStateAndActions);
        });
    }





    /**
     * Enriches the processInstance with next possible action depenending on current state
     * @param requestInfo The RequestInfo of the request
     * @param processStateAndActions
     */
    private void setNextActions(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions){
        List<Role> roles = requestInfo.getUserInfo().getRoles();

        processStateAndActions.forEach(processStateAndAction -> {
            State state = processStateAndAction.getPostActionState();
            List<String> nextAction = new LinkedList<>();
            state.getActions().forEach(action -> {
                if(util.isRoleAvailable(roles,action.getRoles()))
                    nextAction.add(action.getAction());
            });
            processStateAndAction.getProcessInstance().setNextActions(nextAction);
        });

    }












}
