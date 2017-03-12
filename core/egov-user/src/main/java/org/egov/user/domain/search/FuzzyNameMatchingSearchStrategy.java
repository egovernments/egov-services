package org.egov.user.domain.search;

import com.querydsl.core.types.Predicate;
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
@Order(3)
public class FuzzyNameMatchingSearchStrategy implements SearchStrategy {

    private UserRepository userRepository;

    public FuzzyNameMatchingSearchStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> search(UserSearch userSearch) {
        QUser user = QUser.user;
        Predicate predicate = user.name.lower().contains(userSearch.getName());
        Iterable<User> results = userRepository.findAll(predicate);
        return StreamSupport
                .stream(results.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public boolean matches(UserSearch userSearch) {
        return userSearch.isFuzzyLogic() && StringUtils.isNotBlank(userSearch.getName());
    }
}
