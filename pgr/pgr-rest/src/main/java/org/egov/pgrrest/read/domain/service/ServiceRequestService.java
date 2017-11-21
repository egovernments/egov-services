package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.contract.web.SevaRequest;
import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.common.persistence.repository.UserRepository;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.egov.pgrrest.read.persistence.repository.ServiceRequestRepository;
import org.egov.pgrrest.read.web.contract.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestService {

    private static final String SYSTEM_USER = "SYSTEM";
    private static final String ANONYMOUS_USER_NAME = "anonymous";
    private ServiceRequestRepository serviceRequestRepository;
    private UserRepository userRepository;
    private SevaNumberGeneratorService sevaNumberGeneratorService;
    private ServiceRequestTypeService serviceRequestTypeService;
    private List<ServiceRequestValidator> validators;
    private ServiceRequestCustomFieldService customFieldService;
    private DraftService draftService;
    private boolean postgresEnabled;

    @Autowired
    public ServiceRequestService(ServiceRequestRepository serviceRequestRepository,
                                 SevaNumberGeneratorService sevaNumberGeneratorService,
                                 UserRepository userRepository,
                                 ServiceRequestTypeService serviceRequestTypeService,
                                 List<ServiceRequestValidator> validators,
                                 ServiceRequestCustomFieldService customFieldService,
                                 DraftService draftService, @Value("${postgres.enabled}") boolean postgresEnabled)

    {
        this.serviceRequestRepository = serviceRequestRepository;
        this.sevaNumberGeneratorService = sevaNumberGeneratorService;
        this.userRepository = userRepository;
        this.serviceRequestTypeService = serviceRequestTypeService;
        this.validators = validators;
        this.customFieldService = customFieldService;
        this.draftService = draftService;
        this.postgresEnabled = postgresEnabled;
    }

    public List<ServiceRequest> findAll(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
        List<ServiceRequest> serviceRequestList;
        serviceRequestList = postgresEnabled ? serviceRequestRepository.findFromDb(serviceRequestSearchCriteria) :
            getRecordFromES(serviceRequestSearchCriteria);
        maskCitizenDetailsForAnonymousRequest(serviceRequestSearchCriteria, serviceRequestList);
        //Pagination
        if(serviceRequestSearchCriteria.getFromIndex() != null && serviceRequestSearchCriteria.getPageSize() != null){
            return getPageData(serviceRequestList, serviceRequestSearchCriteria);
        }
        return serviceRequestList;
    }

    public Long getCount(ServiceRequestSearchCriteria searchCriteria) {
        if(postgresEnabled)
            return (long) serviceRequestRepository.findFromDb(searchCriteria).size();
        else
            return serviceRequestRepository.getCount(searchCriteria);
    }

    public void save(ServiceRequest serviceRequest, SevaRequest contractSevaRequest) {
        validate(serviceRequest);
        enrichWithServiceType(serviceRequest);
        enrichWithCRN(serviceRequest);
        contractSevaRequest.update(serviceRequest);
        setUserIdForAnonymousUser(contractSevaRequest);
        enrichWithComputedFields(serviceRequest, contractSevaRequest, serviceRequest.getServiceStatus());
        serviceRequestRepository.save(contractSevaRequest);
        deleteDraft(serviceRequest);
    }

    public void update(ServiceRequest serviceRequest, SevaRequest contractSevaRequest) {
        validate(serviceRequest);
        setUserIdForAnonymousUser(contractSevaRequest);
        enrichWithComputedFields(serviceRequest, contractSevaRequest, serviceRequest.getServiceStatus());
        serviceRequestRepository.update(contractSevaRequest);
        deleteDraft(serviceRequest);
    }

    public List<String> getCrnByAttributes(String receivingMode, Long locationId, String keyword) {
        ServiceRequestSearchCriteria serviceRequestSearchCriteria = ServiceRequestSearchCriteria.builder()
            .receivingMode(receivingMode)
            .locationId(locationId)
            .keyword(keyword)
            .build();

        return serviceRequestRepository.getCrnBySubmissionAttributes(serviceRequestSearchCriteria);

    }

    private List<ServiceRequest> getPageData(List<ServiceRequest> serviceRequestList, ServiceRequestSearchCriteria serviceRequestSearchCriteria){
        Integer fromIndex = serviceRequestSearchCriteria.getFromIndex();
        Integer toIndex = serviceRequestSearchCriteria.getFromIndex() + serviceRequestSearchCriteria.getPageSize();
        if(toIndex > serviceRequestList.size())
            toIndex = serviceRequestList.size();

        return serviceRequestList.subList(fromIndex,toIndex);
    }

    private void enrichWithComputedFields(ServiceRequest serviceRequest, SevaRequest contractSevaRequest,
                                          ServiceStatus action) {
        customFieldService.enrich(serviceRequest, contractSevaRequest, action);
    }

    private void maskCitizenDetailsForAnonymousRequest(ServiceRequestSearchCriteria searchCriteria,
                                                       List<ServiceRequest> serviceRequestList) {
        if (searchCriteria.isAnonymous())
            serviceRequestList.forEach(ServiceRequest::maskUserDetails);
    }

    private void validate(ServiceRequest serviceRequest) {
        validators.stream()
            .filter(v -> v.canValidate(serviceRequest))
            .forEach(validator -> validator.validate(serviceRequest));
    }

    private void enrichWithCRN(ServiceRequest serviceRequest) {
        final String crn = sevaNumberGeneratorService.generate(serviceRequest.getTenantId());
        serviceRequest.setCrn(crn);
    }

    private void enrichWithServiceType(ServiceRequest serviceRequest) {
        serviceRequestTypeService.enrich(serviceRequest);
    }

    private void setUserIdForAnonymousUser(SevaRequest sevaRequest) {
        if (sevaRequest.getUserId() != null) {
            return;
        }

        final User anonymousUser = getAnonymousUser(sevaRequest.getServiceRequest().getTenantId());
        sevaRequest.getRequestInfo()
            .setUserInfo(org.egov.common.contract.request.User.builder()
                .id(anonymousUser.getId())
                .type(SYSTEM_USER)
                .build());
    }

    private User getAnonymousUser(String tenantId) {
        return userRepository.getUserByUserName(ANONYMOUS_USER_NAME, tenantId);
    }

    private void deleteDraft(ServiceRequest serviceRequest) {
        final Long draftId = serviceRequest.getDraftId();
        if (draftId == null) {
            return;
        }
        draftService.delete(draftId);
    }

    private List<ServiceRequest> getRecordFromES(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
        List<ServiceRequest> serviceRequestList;
        serviceRequestList = serviceRequestRepository.find(serviceRequestSearchCriteria);
        if (serviceRequestList.isEmpty()) {
            serviceRequestList = serviceRequestRepository.findFromDb(serviceRequestSearchCriteria);
        }
        return serviceRequestList;
    }

}
