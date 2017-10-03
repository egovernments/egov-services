package org.egov.property.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
@Setter
public class DemandUpdateException extends RuntimeException {

    public DemandUpdateException(HttpStatusCodeException ex) {
        super(ex);
    }
}
