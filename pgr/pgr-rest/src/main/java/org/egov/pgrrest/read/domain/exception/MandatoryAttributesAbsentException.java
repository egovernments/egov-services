package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;

public class MandatoryAttributesAbsentException extends RuntimeException {
    @Getter
    private ArrayList<String> missingMandatoryAttributeCodes;

    public MandatoryAttributesAbsentException(Set<String> missingMandatoryAttributeCodes) {
        this.missingMandatoryAttributeCodes = new ArrayList<>(missingMandatoryAttributeCodes);
    }
}