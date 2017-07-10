package org.egov.pgrrest.read.domain.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

@Getter
public class MalformedDraftException extends RuntimeException {

    public MalformedDraftException(JsonProcessingException ioe) {
        super(ioe);
    }
}
