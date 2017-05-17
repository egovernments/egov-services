package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.entity.Submission;
import org.egov.pgrrest.common.entity.SubmissionAttribute;
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

    public SubmissionRepository(SubmissionJpaRepository submissionJpaRepository,
                                SubmissionAttributeJpaRepository submissionAttributeJpaRepository) {
        this.submissionJpaRepository = submissionJpaRepository;
        this.submissionAttributeJpaRepository = submissionAttributeJpaRepository;
    }

    public List<ServiceRequest> find(ServiceRequestSearchCriteria searchCriteria) {
        final List<Submission> submissions = getSubmissions(searchCriteria);
        enrichSubmissionsWithAttributeEntries(searchCriteria, submissions);
        return submissions.stream()
            .map(Submission::toDomain)
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
                .getOrDefault(submission.getId(), Collections.emptyList());
            submission.setAttributeValues(matchingAttributes);
        });
    }

    private Map<String, List<SubmissionAttribute>> getSubmissionAttributes(
        ServiceRequestSearchCriteria searchCriteria, List<Submission> submissions) {
        final List<String> crnList = submissions.stream()
            .map(Submission::getId)
            .collect(Collectors.toList());
        final List<SubmissionAttribute> submissionAttributes = submissionAttributeJpaRepository
            .findByCrnListAndTenantId(crnList, searchCriteria.getTenantId());
        return submissionAttributes.stream()
            .collect(Collectors.groupingBy(SubmissionAttribute::getCrn));
    }
}
