package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.entity.ServiceType;
import org.egov.pgrrest.common.entity.Submission;
import org.egov.pgrrest.common.entity.SubmissionAttribute;
import org.egov.pgrrest.common.repository.ServiceRequestTypeJpaRepository;
import org.egov.pgrrest.common.repository.SubmissionAttributeJpaRepository;
import org.egov.pgrrest.common.repository.SubmissionJpaRepository;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.egov.pgrrest.read.persistence.specification.SubmissionSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubmissionRepository {
    private SubmissionJpaRepository submissionJpaRepository;
    private SubmissionAttributeJpaRepository submissionAttributeJpaRepository;
    private ServiceRequestTypeJpaRepository serviceTypeJpaRepository;

    public SubmissionRepository(SubmissionJpaRepository submissionJpaRepository,
                                SubmissionAttributeJpaRepository submissionAttributeJpaRepository,
                                ServiceRequestTypeJpaRepository serviceTypeJpaRepository) {
        this.submissionJpaRepository = submissionJpaRepository;
        this.submissionAttributeJpaRepository = submissionAttributeJpaRepository;
        this.serviceTypeJpaRepository = serviceTypeJpaRepository;
    }

    public List<ServiceRequest> find(ServiceRequestSearchCriteria searchCriteria) {
        final List<Submission> submissions = getSubmissions(searchCriteria);
        enrichSubmissionsWithAttributeEntries(searchCriteria, submissions);
        enrichSubmissionsWithServiceTypes(searchCriteria, submissions);
        return submissions.stream()
            .map(Submission::toDomain)
            .collect(Collectors.toList());
    }

    private void enrichSubmissionsWithServiceTypes(ServiceRequestSearchCriteria searchCriteria,
                                                   List<Submission> submissions) {
        final List<String> serviceCodes = getServiceCodes(submissions);
        final Map<String, ServiceType> codeToServiceTypeMap = getServiceCodeToTypeMap(searchCriteria, serviceCodes);
        submissions.forEach(submission -> {
            final ServiceType matchingServiceType = codeToServiceTypeMap.get(submission.getServiceCode());
            submission.setServiceType(matchingServiceType);
        });
    }

    private Map<String, ServiceType> getServiceCodeToTypeMap(ServiceRequestSearchCriteria searchCriteria,
                                                             List<String> serviceCodes) {
        final List<ServiceType> serviceTypes = serviceTypeJpaRepository
            .findByCodeInAndTenantId(serviceCodes, searchCriteria.getTenantId());
        return serviceTypes.stream()
            .collect(Collectors.toMap(ServiceType::getCode, serviceType -> serviceType));
    }

    private List<String> getServiceCodes(List<Submission> submissions) {
        return submissions.stream()
            .map(Submission::getServiceCode)
            .distinct()
            .collect(Collectors.toList());
    }

    private List<Submission> getSubmissions(ServiceRequestSearchCriteria searchCriteria) {
        final SubmissionSpecification specification = new SubmissionSpecification(searchCriteria);
        final Sort sort = new Sort(Sort.Direction.DESC, "lastModifiedDate");
        return this.submissionJpaRepository.findAll(specification, sort);
    }

    private void enrichSubmissionsWithAttributeEntries(ServiceRequestSearchCriteria searchCriteria,
                                                       List<Submission> submissions) {
        final Map<String, List<SubmissionAttribute>> submissionAttributes =
            getSubmissionAttributes(searchCriteria, submissions);
        submissions.forEach(submission -> {
            final List<SubmissionAttribute> matchingAttributes = submissionAttributes
                .getOrDefault(submission.getCrn(), Collections.emptyList());
            submission.setAttributeValues(matchingAttributes);
        });
    }

    private Map<String, List<SubmissionAttribute>> getSubmissionAttributes(
        ServiceRequestSearchCriteria searchCriteria, List<Submission> submissions) {
        final List<String> crnList = submissions.stream()
            .map(Submission::getCrn)
            .collect(Collectors.toList());
        final List<SubmissionAttribute> submissionAttributes = submissionAttributeJpaRepository
            .findByCrnListAndTenantId(crnList, searchCriteria.getTenantId());
        return submissionAttributes.stream()
            .collect(Collectors.groupingBy(SubmissionAttribute::getCrn));
    }
}
