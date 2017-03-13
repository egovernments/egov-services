package org.egov.user.domain.search;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class FuzzyNameMatchingSpecification implements Specification<User> {

    private UserSearch userSearch;

    public FuzzyNameMatchingSpecification(UserSearch userSearch) {
        this.userSearch = userSearch;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final String QUERY = String.format("%%%s%%", userSearch.getName().toLowerCase());
        Path<String> name = root.get(User_.name);
        return criteriaBuilder.like(criteriaBuilder.lower(name), QUERY);
    }
}
