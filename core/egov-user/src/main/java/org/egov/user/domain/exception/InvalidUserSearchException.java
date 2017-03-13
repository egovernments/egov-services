package org.egov.user.domain.exception;

import lombok.Getter;
import org.egov.user.domain.model.UserSearch;

@Getter
public class InvalidUserSearchException extends RuntimeException {

    private UserSearch userSearch;

    public InvalidUserSearchException(UserSearch userSearch) {
        this.userSearch = userSearch;
    }
}
