package org.egov.wf.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.web.models.Action;
import org.egov.wf.web.models.ProcessStateAndAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.*;
import static org.egov.wf.util.WorkflowConstants.*;


@Component
public class WorkflowValidator {



    private WorkflowUtil util;


    @Autowired
    public WorkflowValidator(WorkflowUtil util) {
        this.util = util;
    }


    /**
     * Validates the request
     * @param requestInfo RequestInfo of the request
     * @param processStateAndActions The processStateAndActions containing processInstances to be validated
     * @param mdmsData The mdms data from MDMS search
     */
    public void validateRequst(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions,Object mdmsData){
        validateAction(requestInfo,processStateAndActions,mdmsData);
        validateDocuments(processStateAndActions);

    }


    /**
     * Validates if documents are required to perform state change
     * @param processStateAndActions ProcessStateAndAction to be validated
     */
    private void validateDocuments(List<ProcessStateAndAction> processStateAndActions){
        Map<String,String> errorMap = new HashMap<>();
        for (ProcessStateAndAction processStateAndAction : processStateAndActions){
            if(processStateAndAction.getPostActionState().getDocUploadRequired()){
                if(CollectionUtils.isEmpty(processStateAndAction.getProcessInstance().getDocuments()))
                    errorMap.put("INVALID DOCUMENT","Documents cannot be null for status: "+processStateAndAction.getPostActionState().getState());
            }
        }
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    /**
     * Validates if the action can be performed
     * @param requestInfo The RequestInfo of the incoming request
     * @param processStateAndActions The processStateAndActions containing processInstances to be validated
     * @param mdmsData The mdms data from MDMS search
     */
    private void validateAction(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions,Object mdmsData){
        Map<String,String> errorMap = new HashMap<>();
        List<Role> roles = requestInfo.getUserInfo().getRoles();
        for(ProcessStateAndAction processStateAndAction : processStateAndActions){
            Action action = processStateAndAction.getAction();
            if(action==null && !processStateAndAction.getCurrentState().getIsTerminateState())
                errorMap.put("INVALID ACTION","Action not found for businessId: "+
                        processStateAndAction.getCurrentState().getBusinessServiceId());

            Boolean isRoleAvailable = util.isRoleAvailable(roles,action.getRoles());
            Boolean isStateChanging = (action.getStateId() == action.getNextStateId()) ? false : true;

            if(action!=null && isStateChanging && !isRoleAvailable)
                errorMap.put("INVALID ROLE","User is not authorized to perform action");
            if(action!=null && !isStateChanging && !util.isRoleAvailable(roles,util.rolesAllowedInService(mdmsData,MDMS_ALLOWED_ROLES)))
                errorMap.put("INVALID ROLE","User is not authorized to perform action");

        }
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }



}
