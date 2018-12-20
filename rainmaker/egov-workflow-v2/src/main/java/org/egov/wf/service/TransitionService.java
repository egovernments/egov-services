package org.egov.wf.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.wf.repository.BusinessServiceRepository;
import org.egov.wf.repository.WorKflowRepository;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;

@Slf4j
@Service
public class TransitionService {


    private ObjectMapper mapper;

    private WorKflowRepository repository;


    private BusinessServiceRepository businessServiceRepository;

    private WorkflowUtil workflowUtil;



    @Autowired
    public TransitionService(ObjectMapper mapper, WorKflowRepository repository,
                             BusinessServiceRepository businessServiceRepository,
                             WorkflowUtil workflowUtil) {
        this.mapper = mapper;
        this.repository = repository;
        this.businessServiceRepository = businessServiceRepository;
        this.workflowUtil = workflowUtil;
    }




    /**
     * Creates list of ProcessStateAndAction from the list of the processInstances
     * @param request The incoming ProcessInstanceRequest
     * @return List of ProcessStateAndAction containing the State object for status before the action and after the action and
     * the Action object for the given action
     */
    public List<ProcessStateAndAction> getProcessStateAndActions(ProcessInstanceRequest request,Boolean isTransition){
        List<ProcessStateAndAction> processStateAndActions = new LinkedList<>();
        getStatus(request.getProcessInstances());
        BusinessService businessService = getBusinessService(request);
        List<String> allowedRoles = workflowUtil.rolesAllowedInService(businessService);
        for(ProcessInstance processInstance: request.getProcessInstances()){
            ProcessStateAndAction processStateAndAction = new ProcessStateAndAction();
            processStateAndAction.setProcessInstance(processInstance);

            for(State state : businessService.getStates()){
                if(!StringUtils.isEmpty(state.getState()) && state.getUuid().equalsIgnoreCase(processInstance.getStatus())){
                    processStateAndAction.setCurrentState(state);
                    break;
                }
                if(StringUtils.isEmpty(state.getState()) && StringUtils.isEmpty(processInstance.getStatus())){
                    processStateAndAction.setCurrentState(state);
                    break;
                }
            }

            if(!CollectionUtils.isEmpty(processStateAndAction.getCurrentState().getActions())){
                for (Action action : processStateAndAction.getCurrentState().getActions()){
                    if(action.getAction().equalsIgnoreCase(processInstance.getAction())){
                        if(action.getRoles().contains("*"))
                            action.setRoles(allowedRoles);
                        processStateAndAction.setAction(action);
                        break;
                    }
                }
            }

            if(isTransition){
                if(processStateAndAction.getAction()==null)
                    throw new CustomException("INVALID ACTION","Action "+processStateAndAction.getProcessInstance().getAction()
                            + " not found in config for the businessId: "
                            +processStateAndAction.getProcessInstance().getBusinessId());

                for(State state : businessService.getStates()){
                    if(state.getUuid().equalsIgnoreCase(processStateAndAction.getAction().getNextState())){
                        processStateAndAction.setResultantState(state);
                        break;
                    }
                }
            }

            processStateAndActions.add(processStateAndAction);

        }
        return processStateAndActions;
    }



    /**
     * Searches the db and sets the status of the processInstance based on businessId
     * @param processInstances The list of ProcessInstance to be created
     */
    private void getStatus(List<ProcessInstance> processInstances){
        ProcessInstanceSearchCriteria criteria = new ProcessInstanceSearchCriteria();
        processInstances.forEach(processInstance -> {
            criteria.setTenantId(processInstance.getTenantId());
            criteria.setBusinessId(processInstance.getBusinessId());
            List<ProcessInstance> processInstancesFromDB = repository.getProcessInstances(criteria);
            if(!CollectionUtils.isEmpty(processInstancesFromDB)){
                processInstance.setStatus(processInstancesFromDB.get(0).getStatus());
            }
        });
    }



    private BusinessService getBusinessService(ProcessInstanceRequest request){
        BusinessServiceSearchCriteria criteria = new BusinessServiceSearchCriteria();
        String tenantId = request.getProcessInstances().get(0).getTenantId();
        String businessService = request.getProcessInstances().get(0).getBusinessService();
        criteria.setTenantId(tenantId);
        criteria.setBusinessService(businessService);
        List<BusinessService> businessServices = businessServiceRepository.getBusinessServices(criteria);
        if(CollectionUtils.isEmpty(businessServices))
            throw new CustomException("BUSINESSSERVICE ERROR","No bussinessService object found for businessSerice: "+
                    businessService + " and tenantId: "+tenantId);
        if(businessServices.size()!=1)
            throw new CustomException("BUSINESSSERVICE ERROR","Multiple bussinessService object found for businessSerice: "+
                    businessService + " and tenantId: "+tenantId);
        return businessServices.get(0);
    }
















}
