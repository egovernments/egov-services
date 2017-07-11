package org.egov.pgrrest.read.domain.exception;

public class DraftNotFoundException extends RuntimeException {

    public DraftNotFoundException(NullPointerException ne) {
        super(ne);
    }
}

