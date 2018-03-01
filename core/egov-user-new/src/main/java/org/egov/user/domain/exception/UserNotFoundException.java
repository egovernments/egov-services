package org.egov.user.domain.exception;

import lombok.Getter;
import org.egov.user.domain.model.User;

@Getter
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -761312648494992125L;
    private User user;

    public UserNotFoundException(final User user) {
        this.user = user;
    }

}

