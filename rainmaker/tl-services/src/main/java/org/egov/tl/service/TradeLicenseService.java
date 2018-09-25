package org.egov.tl.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.producer.Producer;
import org.egov.tl.repository.TLRepository;
import org.egov.tl.validator.TLValidator;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.egov.tl.web.models.TradeLicenseSearchCriteria;
import org.egov.tl.web.models.user.UserDetailResponse;
import org.egov.tl.workflow.ActionValidator;
import org.egov.tl.workflow.TLWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TradeLicenseService {

    private EnrichmentService enrichmentService;

    private UserService userService;

    private TLRepository repository;

    private ActionValidator actionValidator;

    private TLValidator tlValidator;

    private TLWorkflowService workflowService;


    @Autowired
    public TradeLicenseService(EnrichmentService enrichmentService, UserService userService,
                               TLRepository repository, ActionValidator actionValidator,
                               TLValidator tlValidator, TLWorkflowService workflowService) {
        this.enrichmentService = enrichmentService;
        this.userService = userService;
        this.repository = repository;
        this.actionValidator = actionValidator;
        this.tlValidator = tlValidator;
        this.workflowService = workflowService;
    }





    public List<TradeLicense> create(TradeLicenseRequest tradeLicenseRequest){
        actionValidator.validateCreateRequest(tradeLicenseRequest);
        enrichmentService.enrichTLCreateRequest(tradeLicenseRequest);
        userService.createUser(tradeLicenseRequest);
        repository.save(tradeLicenseRequest);
        return tradeLicenseRequest.getLicenses();
    }

    public List<TradeLicense> search(TradeLicenseSearchCriteria criteria, RequestInfo requestInfo){
        List<TradeLicense> licenses;
         if(criteria.getMobileNumber()!=null){
             UserDetailResponse userDetailResponse = userService.getUser(criteria,requestInfo);
             // If user not found with given user fields return empty list
             if(userDetailResponse.getUser().size()==0){
                 return Collections.emptyList();
             }
             enrichmentService.enrichTLCriteriaWithOwnerids(criteria,userDetailResponse);
             licenses = repository.getLicenses(criteria);
             // If property not found with given propertyId or oldPropertyId or address fields return empty list
             if(licenses.size()==0){
                 return Collections.emptyList();
             }
             criteria = enrichmentService.getTLSearchCriteriaFromTLIds(licenses);
             licenses = getLicensesWithOwnerInfo(criteria,requestInfo);
         }
         else {
             licenses = getLicensesWithOwnerInfo(criteria,requestInfo);
         }
       return licenses;
    }


    List<TradeLicense> getLicensesWithOwnerInfo(TradeLicenseSearchCriteria criteria,RequestInfo requestInfo){
        List<TradeLicense> licenses = repository.getLicenses(criteria);
        enrichmentService.enrichTLSearchCriteriaWithOwnerids(criteria,licenses);
        UserDetailResponse userDetailResponse = userService.getUser(criteria,requestInfo);
        enrichmentService.enrichOwner(userDetailResponse,licenses);
        return licenses;
    }


    public List<TradeLicense> update(TradeLicenseRequest tradeLicenseRequest){
        tlValidator.validateUpdate(tradeLicenseRequest);
        actionValidator.validateUpdateRequest(tradeLicenseRequest);
        enrichmentService.enrichTLUpdateRequest(tradeLicenseRequest);
        workflowService.updateStatus(tradeLicenseRequest);
        userService.createUser(tradeLicenseRequest);
        repository.update(tradeLicenseRequest);
        return tradeLicenseRequest.getLicenses();
    }


}
