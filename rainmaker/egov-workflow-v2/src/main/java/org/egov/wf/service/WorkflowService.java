package org.egov.wf.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.wf.repository.WorKflowRepository;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.validator.WorkflowValidator;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class WorkflowService {

    private MDMSService mdmsService;

    private TransitionService transitionService;

    private EnrichmentService enrichmentService;

    private WorkflowValidator workflowValidator;

    private StatusUpdateService statusUpdateService;

    private WorKflowRepository workflowRepository;

    private WorkflowUtil util;


    @Autowired
    public WorkflowService(MDMSService mdmsService, TransitionService transitionService,
                           EnrichmentService enrichmentService, WorkflowValidator workflowValidator,
                           StatusUpdateService statusUpdateService, WorKflowRepository workflowRepository,
                           WorkflowUtil util) {
        this.mdmsService = mdmsService;
        this.transitionService = transitionService;
        this.enrichmentService = enrichmentService;
        this.workflowValidator = workflowValidator;
        this.statusUpdateService = statusUpdateService;
        this.workflowRepository = workflowRepository;
        this.util = util;
    }


    /**
     * Creates or updates the processInstance
     * @param request The incoming request for workflow transition
     * @return The list of processInstance objects after taking action
     */
    public List<ProcessInstance> transition(ProcessInstanceRequest request){
        RequestInfo requestInfo = request.getRequestInfo();
        Object mdmsData = mdmsService.mdmsCall(request);
        String businessServiceName = request.getProcessInstances().get(0).getBusinessService();
        BusinessService businessService = util.getBusinessService(mdmsData,businessServiceName);
        List<ProcessStateAndAction> processStateAndActions = transitionService.getProcessStateAndActions(request,businessService);
        workflowValidator.validateRequst(requestInfo,processStateAndActions,mdmsData);
        enrichmentService.enrichProcessRequest(requestInfo,processStateAndActions);
        statusUpdateService.updateStatus(requestInfo,processStateAndActions);
        return request.getProcessInstances();
    }


    /**
     * Fetches ProcessInstances from db based on processSearchCriteria
     * @param requestInfo The RequestInfo of the search request
     * @param criteria The object containing Search params
     * @return List of processInstances based on search criteria
     */
    public List<ProcessInstance> search(RequestInfo requestInfo,ProcessInstanceSearchCriteria criteria){
        Set<ProcessInstance> processInstances = new HashSet<>();
        Object mdmsData = mdmsService.mdmsCall(requestInfo,criteria.getTenantId());
        if(criteria.isNull())
            processInstances = getUserBasedProcessInstances(requestInfo,criteria,mdmsData);

        return new LinkedList<>(processInstances);
    }


    /**
     * Searches the processInstances based on user and its roles
     * @param requestInfo The RequestInfo of the search request
     * @param criteria The object containing Search params
     * @param mdmsData The data fetched from MDMS search
     * @return List of processInstances based on search criteria
     */
    private Set<ProcessInstance> getUserBasedProcessInstances(RequestInfo requestInfo,ProcessInstanceSearchCriteria criteria,Object mdmsData){
        List<BusinessService> businessServices = util.getAllBusinessServices(mdmsData);
        List<String> actionableStatuses = util.getActionableStatusesForRole(requestInfo,businessServices);
        criteria.setAssignee(requestInfo.getUserInfo().getUuid());
        criteria.setStatus(actionableStatuses);
        List<ProcessInstance> processInstancesForAssignee = workflowRepository.getProcessInstancesForAssignee(criteria);
        List<ProcessInstance> processInstancesForStatus = workflowRepository.getProcessInstancesForStatus(criteria);
        Set<ProcessInstance> processInstanceSet = new LinkedHashSet<>(processInstancesForStatus);
        processInstanceSet.addAll(processInstancesForAssignee);
        return processInstanceSet;
    }











}
