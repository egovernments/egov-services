package org.egov.user.persistence.specification;

import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.persistence.entity.*;
import org.egov.user.persistence.enums.UserType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class MultiFieldsMatchingSpecification implements Specification<User> {

    private UserSearchCriteria searchCriteria;

    public MultiFieldsMatchingSpecification(UserSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		Path<UserKey> userKey = root.get(User_.userKey);
		Path<Long> longPath = userKey.get(UserKey_.id);
        Path<String> userName = root.get(User_.username);
        Path<String> name = root.get(User_.name);
        Path<String> mobileNumber = root.get(User_.mobileNumber);
        Path<String> aadhaarNumber = root.get(User_.aadhaarNumber);
        Path<String> pan = root.get(User_.pan);
        Path<String> emailId = root.get(User_.emailId);
        Path<Boolean> active = root.get(User_.active);
        Path<UserType> type = root.get(User_.type);
        Path<String> tenantId = userKey.get(UserKey_.tenantId);

		List<Predicate> predicates = new ArrayList<>();

        if (isIdPresent(searchCriteria)) {
            predicates.add(longPath.in(searchCriteria.getId()));
        }

		if (isTenantIdPresent(searchCriteria)) {
			predicates.add(criteriaBuilder.equal(tenantId, searchCriteria.getTenantId()));
		}

		if (isUserNamePresent(searchCriteria)) {
            predicates.add(criteriaBuilder.equal(userName, searchCriteria.getUserName()));
        }

        if (isNamePresent(searchCriteria)) {
            predicates.add(criteriaBuilder.equal(name, searchCriteria.getName()));
        }

        if (isMobileNumberPresent(searchCriteria)) {
            predicates.add(criteriaBuilder.equal(mobileNumber, searchCriteria.getMobileNumber()));
        }

        if (isEmailIdPresent(searchCriteria)) {
            predicates.add(criteriaBuilder.equal(emailId, searchCriteria.getEmailId()));
        }

        if (isAadhaarNumberPresent(searchCriteria)) {
            predicates.add(criteriaBuilder.equal(aadhaarNumber, searchCriteria.getAadhaarNumber()));
        }

        if (isPanPresent(searchCriteria)) {
            predicates.add(criteriaBuilder.equal(pan, searchCriteria.getPan()));
        }

        if(isUserTypePresent(searchCriteria)) {
            predicates.add(criteriaBuilder.equal(type, UserType.valueOf(searchCriteria.getType())));
        }

		if (isActiveFlagSet(searchCriteria)) {
			predicates.add(criteriaBuilder.equal(active, searchCriteria.getActive().booleanValue()));
		}

        if(isRolesPresent(searchCriteria)) {
			searchCriteria.getRoleCodes().forEach(roleCode -> {
				final SetJoin<User, Role> rolesJoin = root.join(User_.roles);
				predicates.add(criteriaBuilder.equal(rolesJoin.get(Role_.code), roleCode));
			});
		}

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

	private boolean isActiveFlagSet(UserSearchCriteria searchCriteria) {
		return searchCriteria.getActive() != null;
	}

	private boolean isRolesPresent(UserSearchCriteria searchCriteria) {
		return !CollectionUtils.isEmpty(searchCriteria.getRoleCodes());
	}

	private boolean isUserTypePresent(UserSearchCriteria userSearch) {
        return StringUtils.isNotBlank(userSearch.getType());
    }

    private boolean isIdPresent(UserSearchCriteria userSearch) {
        return userSearch.getId() != null && userSearch.getId().size() > 0;
    }

    private boolean isNamePresent(UserSearchCriteria userSearch) {
        return StringUtils.isNotBlank(userSearch.getName());
    }

    private boolean isEmailIdPresent(UserSearchCriteria userSearch) {
        return StringUtils.isNotBlank(userSearch.getEmailId());
    }

    private boolean isAadhaarNumberPresent(UserSearchCriteria userSearch) {
        return StringUtils.isNotBlank(userSearch.getAadhaarNumber());
    }

    private boolean isMobileNumberPresent(UserSearchCriteria userSearch) {
        return StringUtils.isNotBlank(userSearch.getMobileNumber());
    }

    private boolean isPanPresent(UserSearchCriteria userSearch) {
        return StringUtils.isNotBlank(userSearch.getPan());
    }

    private boolean isUserNamePresent(UserSearchCriteria userSearch) {
        return StringUtils.isNotBlank(userSearch.getUserName());
    }

    private boolean isTenantIdPresent(UserSearchCriteria searchCriteria) {
    	return isNotEmpty(searchCriteria.getTenantId());
	}
}
