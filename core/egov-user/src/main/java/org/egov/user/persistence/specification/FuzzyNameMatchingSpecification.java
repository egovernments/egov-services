package org.egov.user.persistence.specification;

import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class FuzzyNameMatchingSpecification implements Specification<User> {

    private UserSearchCriteria userSearchCriteria;

    public FuzzyNameMatchingSpecification(UserSearchCriteria userSearchCriteria) {
        this.userSearchCriteria = userSearchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final String QUERY = String.format("%%%s%%", userSearchCriteria.getName().toLowerCase());
        Path<String> name = root.get(User_.name);
        Path<Boolean> active = root.get(User_.active);
        Path<String> tenantId = root.get(User_.tenantId);

        return criteriaBuilder.and(
                criteriaBuilder.like(criteriaBuilder.lower(name), QUERY),
                criteriaBuilder.equal(active, userSearchCriteria.isActive()),
				criteriaBuilder.equal(tenantId, userSearchCriteria.getTenantId())
        );
    }
}
