package org.egov.user.domain.search;

import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.QUser;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.UserRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Order(4)
public class MultiFieldsMatchingStrategy implements SearchStrategy {

    private UserRepository userRepository;

    public MultiFieldsMatchingStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> search(UserSearch userSearch) {
        QUser user = QUser.user;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (isNamePresent(userSearch)) booleanBuilder.and(user.name.eq(userSearch.getName()));
        if (isEmailIdPresent(userSearch)) booleanBuilder.and(user.emailId.eq(userSearch.getEmailId()));
        if (isAadhaarNumberPresent(userSearch))
            booleanBuilder.and(user.aadhaarNumber.eq(userSearch.getAadhaarNumber()));
        if (isMobileNumberPresent(userSearch)) booleanBuilder.and(user.mobileNumber.eq(userSearch.getMobileNumber()));
        if (isPanPresent(userSearch)) booleanBuilder.and(user.pan.eq(userSearch.getPan()));

        Iterable<User> result = userRepository.findAll(booleanBuilder.getValue());

        return StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public boolean matches(UserSearch userSearch) {
        return isNamePresent(userSearch) ||
                isEmailIdPresent(userSearch) ||
                isAadhaarNumberPresent(userSearch) ||
                isMobileNumberPresent(userSearch) ||
                isPanPresent(userSearch);
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
}
