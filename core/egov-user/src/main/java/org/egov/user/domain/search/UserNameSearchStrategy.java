package org.egov.user.domain.search;

import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.search.SearchStrategy;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.UserRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Order(2)
public class UserNameSearchStrategy implements SearchStrategy {

    private UserRepository userRepository;

    public UserNameSearchStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> search(UserSearch userSearch) {
        return Collections.singletonList(
                userRepository.findByUsername(userSearch.getUserName())
        );
    }

    public boolean matches(UserSearch userSearch) {
        return isUserNamePresent(userSearch);
    }

    private boolean isUserNamePresent(UserSearch userSearch) {
        return StringUtils.isNotBlank(userSearch.getUserName());
    }
}
