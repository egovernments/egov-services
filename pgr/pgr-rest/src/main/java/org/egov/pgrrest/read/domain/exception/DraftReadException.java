package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

import java.io.IOException;

@Getter
public class DraftReadException extends RuntimeException {

    public DraftReadException(IOException ioe) {
        super(ioe);
    }
}
