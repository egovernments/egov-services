package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.SubmissionAttribute;
import org.egov.pgrrest.common.persistence.repository.SubmissionAttributeJpaRepository;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.egov.pgrrest.read.persistence.specification.SubmissionAttributeSpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionAttributeRepository {

    private SubmissionAttributeJpaRepository submissionAttributeJpaRepository;

    public SubmissionAttributeRepository(SubmissionAttributeJpaRepository submissionAttributeJpaRepository) {
        this.submissionAttributeJpaRepository = submissionAttributeJpaRepository;
    }

    public List<String> getCrnBySubmissionAttributes(ServiceRequestSearchCriteria searchCriteria) {
        final SubmissionAttributeSpecification submissionAttributeSpecification = new SubmissionAttributeSpecification(searchCriteria);
        List<SubmissionAttribute> submissionAttributes = this.submissionAttributeJpaRepository.findAll(submissionAttributeSpecification);
        return submissionAttributes.stream()
            .map(submissionAttribute -> submissionAttribute.getCrn())
            .distinct()
            .collect(Collectors.toList());
    }
}
