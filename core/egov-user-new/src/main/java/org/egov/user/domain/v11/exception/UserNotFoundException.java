package org.egov.user.domain.v11.exception;

import org.egov.user.domain.v11.model.User;

import lombok.Getter;


@Getter
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -761312648494992125L;
    private User user;

    public UserNotFoundException(final User user) {
        this.user = user;
    }

}
