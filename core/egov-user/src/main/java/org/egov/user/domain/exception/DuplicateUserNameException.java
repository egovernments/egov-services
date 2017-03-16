package org.egov.user.domain.exception;

import lombok.Getter;
import org.egov.user.domain.model.User;

public class DuplicateUserNameException extends RuntimeException {

    private static final long serialVersionUID = -6903761146294214595L;
    @Getter
    private User user;

    public DuplicateUserNameException(User user) {
        this.user = user;
    }

}
