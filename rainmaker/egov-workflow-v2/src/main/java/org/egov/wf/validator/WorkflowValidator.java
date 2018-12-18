package org.egov.wf.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.egov.wf.util.BusinessUtil;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.web.models.Action;
import org.egov.wf.web.models.BusinessService;
import org.egov.wf.web.models.ProcessStateAndAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.*;


@Component
public class WorkflowValidator {



    private WorkflowUtil util;

    private BusinessUtil businessUtil;


    @Autowired
    public WorkflowValidator(WorkflowUtil util, BusinessUtil businessUtil) {
        this.util = util;
        this.businessUtil = businessUtil;
    }



    /**
     * Validates the request
     * @param requestInfo RequestInfo of the request
     * @param processStateAndActions The processStateAndActions containing processInstances to be validated
     */
    public void validateRequest(RequestInfo requestInfo, List<ProcessStateAndAction> processStateAndActions){
        String tenantId = processStateAndActions.get(0).getProcessInstance().getTenantId();
        String businessServiceCode = processStateAndActions.get(0).getProcessInstance().getBusinessService();
        BusinessService businessService = businessUtil.getBusinessService(tenantId,businessServiceCode);
        validateAction(requestInfo,processStateAndActions,businessService);
        validateDocuments(processStateAndActions);
    }


    /**
     * Validates if documents are required to perform currentState change
     * @param processStateAndActions ProcessStateAndAction to be validated
     */
    private void validateDocuments(List<ProcessStateAndAction> processStateAndActions){
        Map<String,String> errorMap = new HashMap<>();
        for (ProcessStateAndAction processStateAndAction : processStateAndActions){
            if(processStateAndAction.getResultantState().getDocUploadRequired()){
                if(CollectionUtils.isEmpty(processStateAndAction.getProcessInstance().getDocuments()))
                    errorMap.put("INVALID DOCUMENT","Documents cannot be null for status: "+processStateAndAction.getResultantState().getState());
            }
        }
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    /**
     * Validates if the action can be performed
     * @param requestInfo The RequestInfo of the incoming request
     * @param processStateAndActions The processStateAndActions containing processInstances to be validated
     */
    private void validateAction(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions
             ,BusinessService businessService){
        Map<String,String> errorMap = new HashMap<>();
        List<Role> roles = requestInfo.getUserInfo().getRoles();
        for(ProcessStateAndAction processStateAndAction : processStateAndActions){
            Action action = processStateAndAction.getAction();
            if(action==null && !processStateAndAction.getCurrentState().getIsTerminateState())
                errorMap.put("INVALID ACTION","Action not found for businessId: "+
                        processStateAndAction.getCurrentState().getBusinessServiceId());

            Boolean isRoleAvailable = util.isRoleAvailable(roles,action.getRoles());
            Boolean isStateChanging = (action.getCurrentState() == action.getNextState()) ? false : true;

            if(action!=null && isStateChanging && !isRoleAvailable)
                errorMap.put("INVALID ROLE","User is not authorized to perform action");
            if(action!=null && !isStateChanging && !util.isRoleAvailable(roles,util.rolesAllowedInService(businessService)))
                errorMap.put("INVALID ROLE","User is not authorized to perform action");
        }
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }



}
