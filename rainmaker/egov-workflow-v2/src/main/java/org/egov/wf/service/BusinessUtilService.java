package org.egov.wf.service;

import org.egov.tracer.model.CustomException;
import org.egov.wf.repository.BusinessServiceRepository;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class BusinessUtilService {

    private BusinessServiceRepository businessServiceRepository;

    @Autowired
    public BusinessUtilService(BusinessServiceRepository businessServiceRepository) {
        this.businessServiceRepository = businessServiceRepository;
    }




    /**
     *  Fetches and validates the current currentState and action for the processInstance
     * @param processInstance The ProcessInstance whose current currentState and action have to be fetched
     * @return BusinessService containing the current currentState and action
     */
    public BusinessService getCurrentStateAndAction(ProcessInstance processInstance,Boolean isTransition,List<String> allowedRoles){
        List<BusinessService> businessServices = businessServiceRepository.getCurrentStateAndAction(processInstance,isTransition);
        if(CollectionUtils.isEmpty(businessServices))
            throw new CustomException("INVALID PROCESSINSTANCE","No businessService found for the currentState of processInstance");
        if(businessServices.size()!=1)
            throw new CustomException("INVALID PROCESSINSTANCE","Multiple businessService found for the current currentState of processInstance");
        if(CollectionUtils.isEmpty(businessServices.get(0).getStates()))
            throw new CustomException("INVALID PROCESSINSTANCE","No current State found for the processInstance");
        if(businessServices.get(0).getStates().size()!=1)
            throw new CustomException("INVALID PROCESSINSTANCE","Multiple current States found for the processInstance");
        if(isTransition){
            if(businessServices.get(0).getStates().get(0).getActions().size()!=1)
                throw new CustomException("INVALID PROCESSINSTANCE","Multiple Actions found for the processInstance");
        }
        setAllRoles(businessServices,allowedRoles);
        return businessServices.get(0);
    }



    /**
     *  Fetches and validates the next currentState of the action object
     * @param action The Action object whose next currentState have to be fetched
     * @return BusinessService containing the next currentState
     */
    public BusinessService getResultantState(Action action,List<String> allowedRoles){
        List<BusinessService> businessServices = businessServiceRepository.getResultantState(action);
        if(CollectionUtils.isEmpty(businessServices))
            throw new CustomException("INVALID PROCESSINSTANCE","No businessService found for the next state of processInstance");
        if(businessServices.size()!=1)
            throw new CustomException("INVALID PROCESSINSTANCE","Multiple businessService found for the next state of processInstance");
        if(CollectionUtils.isEmpty(businessServices.get(0).getStates()))
            throw new CustomException("INVALID PROCESSINSTANCE","No next State found for the processInstance");
        if(businessServices.get(0).getStates().size()!=1)
            throw new CustomException("INVALID PROCESSINSTANCE","Multiple next States found for the processInstance");
        setAllRoles(businessServices,allowedRoles);
        return businessServices.get(0);
    }




    public List<String> getAllowedRoles(ProcessInstanceRequest request){
        String businessService = request.getProcessInstances().get(0).getBusinessService();
        String tenantId = request.getProcessInstances().get(0).getTenantId();
        BusinessServiceSearchCriteria criteria = new BusinessServiceSearchCriteria();
        criteria.setBusinessService(businessService);
        criteria.setTenantId(tenantId);
        List<BusinessService> businessServices = businessServiceRepository.getBusinessServices(criteria);
        Set<String> allowedRoles = new HashSet<>();
        businessServices.get(0).getStates().forEach(state -> {
            if(!CollectionUtils.isEmpty(state.getActions()))
                state.getActions().forEach(action -> {
                    if(!action.getRoles().contains("*"))
                        allowedRoles.addAll(action.getRoles());
                });
        });
        return new LinkedList<>(allowedRoles);
    }


    private void setAllRoles(List<BusinessService> businessServices,List<String> allowedRoles){
        businessServices.forEach(businessService -> {
            businessService.getStates().forEach(state -> {
                state.getActions().forEach(action -> {
                    if(action.getRoles().contains("*"))
                        action.setRoles(allowedRoles);
                });
            });
        });

    }




}
