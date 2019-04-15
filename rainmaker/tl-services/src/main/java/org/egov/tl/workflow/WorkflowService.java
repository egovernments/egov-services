package org.egov.tl.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.repository.ServiceRequestRepository;
import org.egov.tl.web.models.RequestInfoWrapper;
import org.egov.tl.web.models.workflow.BusinessService;
import org.egov.tl.web.models.workflow.State;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {


    private TLConfiguration config;

    private ServiceRequestRepository serviceRequestRepository;

    private ObjectMapper mapper;

    @Autowired
    public WorkflowService(TLConfiguration config, ServiceRequestRepository serviceRequestRepository, ObjectMapper mapper) {
        this.config = config;
        this.serviceRequestRepository = serviceRequestRepository;
        this.mapper = mapper;
    }

    /**
     * Get the workflow config for the given tenant
     * @param tenantId    The tenantId for which businessService is requested
     * @param requestInfo The RequestInfo object of the request
     * @return BusinessService for the the given tenantId
     */
    public BusinessService getBusinessService(String tenantId, RequestInfo requestInfo) {
        StringBuilder url = getSearchURLWithParams(tenantId);
        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        Object result = serviceRequestRepository.fetchResult(url, requestInfoWrapper);
        BusinessService response = null;
        try {
            response = mapper.convertValue(result, BusinessService.class);
        } catch (IllegalArgumentException e) {
            throw new CustomException("PARSING ERROR", "Failed to parse response of calculate");
        }
        return response;
    }


    /**
     * Creates url for search based on given tenantId
     *
     * @param tenantId The tenantId for which url is generated
     * @return The search url
     */
    private StringBuilder getSearchURLWithParams(String tenantId) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append("?tenantId=");
        url.append(tenantId);
        url.append("&businessService");
        url.append(config.getBusinessServiceValue());
        return url;
    }


    /**
     * Fetches the state object whith given code from businessService
     * @param stateCode The stateCode for which State object has to be fetched
     * @param businessService The BusinessService from which to fetch state object
     * @return State object to be fetched
     */
    public State getState(String stateCode, BusinessService businessService){
       for(State state : businessService.getStates()){
           if(state.getApplicationStatus().equalsIgnoreCase(stateCode))
               return state;
       }
       return null;
    }


}
