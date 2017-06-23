package org.egov.user.persistence.specification;

import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.UserKey;
import org.egov.user.persistence.entity.UserKey_;
import org.egov.user.persistence.entity.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

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
		Path<UserKey> userKey = root.get(User_.userKey);
		Path<String> tenantId = userKey.get(UserKey_.tenantId);

		List<Predicate> predicates = new ArrayList<>();

		predicates.add(criteriaBuilder.equal(tenantId, userSearchCriteria.getTenantId()));
		predicates.add(criteriaBuilder.like(criteriaBuilder.lower(name), QUERY));

		if (userSearchCriteria.getActive() != null) {
			predicates.add(criteriaBuilder.equal(active, userSearchCriteria.getActive().booleanValue()));
		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
