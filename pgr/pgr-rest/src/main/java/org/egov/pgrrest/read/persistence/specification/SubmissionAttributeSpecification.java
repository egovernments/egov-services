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

import static org.springframework.util.StringUtils.isEmpty;

public class SubmissionAttributeSpecification implements Specification<SubmissionAttribute> {

    private ServiceRequestSearchCriteria criteria;

    public SubmissionAttributeSpecification(ServiceRequestSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<SubmissionAttribute> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        Path<SubmissionAttributeKey> key = root.get(SubmissionAttribute_.id);
        Path<String> tenantId = key.get(SubmissionAttributeKey_.tenantId);
        Path<String> codeName = key.get(SubmissionAttributeKey_.code);

        List<Predicate> predicates = new ArrayList<>();
        if (!isEmpty(criteria.getTenantId())) {
            predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
        }

       /* if (criteria.getLocationId() != null) {
            predicates.add(criteriaBuilder.equal(codeName, criteria.getLocationId().toString()));
        }*/

       /* if (criteria.getReceivingMode() != null) {
            predicates.add(criteriaBuilder.equal(codeName, criteria.getReceivingMode()));
        }*/

        if (criteria.getReceivingMode() != null || criteria.getLocationId() != null) {
            String locationId = !isEmpty(criteria.getLocationId()) ? criteria.getLocationId().toString() : null;

            String receivingMode = !isEmpty(criteria.getReceivingMode()) ? criteria.getReceivingMode() : null;

            predicates.add(codeName.in(receivingMode, locationId));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}



