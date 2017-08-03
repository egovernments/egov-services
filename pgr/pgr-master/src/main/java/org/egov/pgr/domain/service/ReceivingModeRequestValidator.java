package org.egov.pgr.domain.service;


import org.egov.pgr.domain.model.ReceivingMode;

public interface ReceivingModeRequestValidator {
    boolean canValidate(ReceivingMode receivingMode);
    void validate(ReceivingMode receivingMode);

}
