package org.egov.user.domain.search;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.search.SearchStrategy;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.UserRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class UserIdSearchStrategy implements SearchStrategy {

    private UserRepository userRepository;

    public UserIdSearchStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> search(UserSearch userSearch) {
        return userRepository.findAll(userSearch.getId());
    }

    public boolean matches(UserSearch userSearch) {
        return isUserIdPresent(userSearch);
    }

    private boolean isUserIdPresent(UserSearch userSearch) {
        return userSearch.getId() != null && userSearch.getId().size() > 0;
    }
}
