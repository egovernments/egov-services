package org.egov.user.domain.search;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.User;

import java.util.List;

public interface SearchStrategy {

    List<User> search(UserSearch userSearch);
    boolean matches(UserSearch userSearch);
}
