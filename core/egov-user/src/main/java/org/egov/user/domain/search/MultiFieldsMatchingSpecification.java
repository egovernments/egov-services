package org.egov.user.domain.search;

import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class MultiFieldsMatchingSpecification implements Specification<User> {

    private UserSearch userSearch;

    public MultiFieldsMatchingSpecification(UserSearch userSearch) {
        this.userSearch = userSearch;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Path<Long> longPath = root.get(User_.id);
        Path<String> userName = root.get(User_.username);
        Path<String> name = root.get(User_.name);
        Path<String> mobileNumber = root.get(User_.mobileNumber);
        Path<String> aadhaarNumber = root.get(User_.aadhaarNumber);
        Path<String> pan = root.get(User_.pan);
        Path<String> emailId = root.get(User_.emailId);
        Path<Boolean> active = root.get(User_.active);

        List<Predicate> predicates = new ArrayList<>();

        if (isIdPresent(userSearch)) {
            predicates.add(longPath.in(userSearch.getId()));
        }

        if (isUserNamePresent(userSearch)) {
            predicates.add(criteriaBuilder.equal(userName, userSearch.getUserName()));
        }

        if (isNamePresent(userSearch)) {
            predicates.add(criteriaBuilder.equal(name, userSearch.getName()));
        }

        if (isMobileNumberPresent(userSearch)) {
            predicates.add(criteriaBuilder.equal(mobileNumber, userSearch.getMobileNumber()));
        }

        if (isEmailIdPresent(userSearch)) {
            predicates.add(criteriaBuilder.equal(emailId, userSearch.getEmailId()));
        }

        if (isAadhaarNumberPresent(userSearch)) {
            predicates.add(criteriaBuilder.equal(aadhaarNumber, userSearch.getAadhaarNumber()));
        }

        if (isPanPresent(userSearch)) {
            predicates.add(criteriaBuilder.equal(pan, userSearch.getPan()));
        }

        predicates.add(criteriaBuilder.equal(active, userSearch.isActive()));


        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private boolean isIdPresent(UserSearch userSearch) {
        return userSearch.getId() != null && userSearch.getId().size() > 0;
    }

    private boolean isNamePresent(UserSearch userSearch) {
        return StringUtils.isNotBlank(userSearch.getName());
    }

    private boolean isEmailIdPresent(UserSearch userSearch) {
        return StringUtils.isNotBlank(userSearch.getEmailId());
    }

    private boolean isAadhaarNumberPresent(UserSearch userSearch) {
        return StringUtils.isNotBlank(userSearch.getAadhaarNumber());
    }

    private boolean isMobileNumberPresent(UserSearch userSearch) {
        return StringUtils.isNotBlank(userSearch.getMobileNumber());
    }

    private boolean isPanPresent(UserSearch userSearch) {
        return StringUtils.isNotBlank(userSearch.getPan());
    }

    private boolean isUserNamePresent(UserSearch userSearch) {
        return StringUtils.isNotBlank(userSearch.getUserName());
    }
}
