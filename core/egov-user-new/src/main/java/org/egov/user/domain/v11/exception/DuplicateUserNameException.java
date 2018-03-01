package org.egov.user.domain.v11.exception;

import org.egov.user.domain.v11.model.User;

import lombok.Getter;


public class DuplicateUserNameException extends RuntimeException {

    private static final long serialVersionUID = -6903761146294214595L;
    @Getter
    private User user;

    public DuplicateUserNameException(User user) {
        this.user = user;
    }

}
