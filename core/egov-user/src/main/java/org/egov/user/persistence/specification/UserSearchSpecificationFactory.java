package org.egov.user.persistence.specification;

import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSearchSpecificationFactory {

    public Specification<User> getSpecification(UserSearchCriteria userSearch) {
        if (userSearch.isFuzzyLogic()) {
            return new FuzzyNameMatchingSpecification(userSearch);
        }
        return new MultiFieldsMatchingSpecification(userSearch);
    }
}
