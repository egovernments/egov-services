package org.egov.pgr.specification;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.Complaint_;
import org.egov.pgr.model.SevaSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SevaSpecification implements Specification<Complaint> {
    private SevaSearchCriteria criteria;

    public SevaSpecification(SevaSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Complaint> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Path<String> crn = root.get(Complaint_.crn);

        final List<Predicate> predicates = new ArrayList<Predicate>();
        if (criteria.getService_request_id() != null) {
            predicates.add(criteriaBuilder.equal(crn, criteria.getService_request_id()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
