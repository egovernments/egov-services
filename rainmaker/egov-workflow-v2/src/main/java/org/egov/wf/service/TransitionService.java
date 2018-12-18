package org.egov.wf.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.wf.repository.WorKflowRepository;
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

    private BusinessUtilService businessUtilService;


    @Autowired
    public TransitionService(ObjectMapper mapper, WorKflowRepository repository, BusinessUtilService businessUtilService) {
        this.mapper = mapper;
        this.repository = repository;
        this.businessUtilService = businessUtilService;
    }


    /**
     * Creates list of ProcessStateAndAction from the list of the processInstances
     * @param request The incoming ProcessInstanceRequest
     * @param isTransition Flag if the search or transition api is calling the method
     * @return List of ProcessStateAndAction containing the State object for status before the action and after the action and
     * the Action object for the given action
     */
    public List<ProcessStateAndAction> getProcessStateAndActions(ProcessInstanceRequest request,Boolean isTransition){
        List<ProcessStateAndAction> processStateAndActions = new LinkedList<>();
        getStatus(request.getProcessInstances());
        for(ProcessInstance processInstance: request.getProcessInstances()) {

            BusinessService businessService = businessUtilService.getCurrentStateAndAction(processInstance,isTransition);
            ProcessStateAndAction processStateAndAction = new ProcessStateAndAction();
            processStateAndAction.setProcessInstance(processInstance);
            processStateAndAction.setCurrentState(businessService.getStates().get(0));
            if(!CollectionUtils.isEmpty(businessService.getStates().get(0).getActions()))
                processStateAndAction.setAction(businessService.getStates().get(0).getActions().get(0));

            BusinessService businessServiceForNextState = businessUtilService.getResultantState(processStateAndAction.getAction());
            processStateAndAction.setResultantState(businessServiceForNextState.getStates().get(0));
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





















}
