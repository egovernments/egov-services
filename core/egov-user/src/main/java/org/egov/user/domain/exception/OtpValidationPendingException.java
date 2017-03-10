package org.egov.user.domain.exception;

import lombok.Getter;
import org.egov.user.domain.model.User;

@Getter
public class OtpValidationPendingException extends RuntimeException {

    private User user;

    public OtpValidationPendingException(User user) {
        this.user = user;
    }
    
}
