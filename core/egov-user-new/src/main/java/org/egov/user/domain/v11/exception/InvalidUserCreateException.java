package org.egov.user.domain.v11.exception;

import org.egov.user.domain.v11.model.User;

import lombok.Getter;


@Getter
public class InvalidUserCreateException extends RuntimeException {

    private static final long serialVersionUID = -761312648494992125L;
    private User user;

    public InvalidUserCreateException(User user) {
        this.user = user;
    }

}

