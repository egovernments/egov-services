package org.egov.wf.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.egov.tracer.model.CustomException;
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


    @Autowired
    public TransitionService(ObjectMapper mapper, WorKflowRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    /**
     * Creates list of ProcessStateAndAction from the list of the processInstances
     * @param request The incoming ProcessInstanceRequest
     * @param businessService The BusinessService object for the given businessService and tenantId
     * @return List of ProcessStateAndAction containing the State object for status before the action and after the action and
     * the Action object for the given action
     */
    public List<ProcessStateAndAction> getProcessStateAndActions(ProcessInstanceRequest request, BusinessService businessService){
        List<ProcessStateAndAction> processStateAndActions = new LinkedList<>();
        getStatus(request.getProcessInstances());
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

           /* if(processStateAndAction.getCurrentState()==null)
                throw new CustomException("INVALID STATUS","No state found in config for the businessId: "
                        +processStateAndAction.getProcessInstance().getBusinessId() + " and status: "+
                        processStateAndAction.getProcessInstance().getStatus());*/

            for (Action action : processStateAndAction.getCurrentState().getActions()){
                if(action.getAction().equalsIgnoreCase(processInstance.getAction())){
                    processStateAndAction.setAction(action);
                    break;
                }
            }

            if(processStateAndAction.getAction()==null)
                throw new CustomException("INVALID ACTION","Action "+processStateAndAction.getProcessInstance().getAction()
                        + " not found in config for the businessId: "
                        +processStateAndAction.getProcessInstance().getBusinessId());

            for(State state : businessService.getStates()){
                if(state.getUuid().equalsIgnoreCase(processStateAndAction.getAction().getNextStateId())){
                    processStateAndAction.setPostActionState(state);
                    break;
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





















}
