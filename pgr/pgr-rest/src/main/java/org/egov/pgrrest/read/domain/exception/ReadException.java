package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

import java.io.IOException;

@Getter
public class ReadException extends RuntimeException {

    public ReadException(IOException ioe) {
        super(ioe);
    }
}
