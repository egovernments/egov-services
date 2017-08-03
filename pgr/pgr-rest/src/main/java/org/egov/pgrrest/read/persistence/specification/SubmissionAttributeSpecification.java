package org.egov.pgrrest.read.persistence.specification;


import org.egov.pgrrest.common.persistence.entity.SubmissionAttribute;
import org.egov.pgrrest.common.persistence.entity.SubmissionAttributeKey;
import org.egov.pgrrest.common.persistence.entity.SubmissionAttributeKey_;
import org.egov.pgrrest.common.persistence.entity.SubmissionAttribute_;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionAttributeSpecification implements Specification<SubmissionAttribute> {

    private ServiceRequestSearchCriteria criteria;

    public SubmissionAttributeSpecification(ServiceRequestSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<SubmissionAttribute> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        Path<SubmissionAttributeKey> key = root.get(SubmissionAttribute_.id);
        Path<String> crn = key.get(SubmissionAttributeKey_.crn);
        Path<String> tenantId = key.get(SubmissionAttributeKey_.tenantId);
        Path<String> keyName = root.get(String.valueOf(SubmissionAttributeKey_.key));

        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getTenantId() != null && !criteria.getTenantId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
        }

        if (criteria.getServiceRequestId() != null) {
            predicates.add(criteriaBuilder.equal(crn, criteria.getServiceRequestId()));
        }

        if (criteria.getChildLocationId() != null) {
            predicates.add(criteriaBuilder.equal(crn, criteria.getServiceRequestId()));
        }


        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}



