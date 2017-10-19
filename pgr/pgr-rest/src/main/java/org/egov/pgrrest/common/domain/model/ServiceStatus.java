package org.egov.pgrrest.common.domain.model;

import lombok.Getter;
import org.egov.pgrrest.read.domain.exception.ServiceStatusNotPresentException;
import org.egov.pgrrest.read.domain.exception.UnknownServiceStatusException;

import java.util.Optional;
import java.util.stream.Stream;

public enum ServiceStatus {
    COMPLAINT_REGISTERED("REGISTERED"),
    COMPLAINT_FORWARDED("FORWARDED"),
    COMPLAINT_PROCESSING("PROCESSING"),
    COMPLAINT_COMPLETED("COMPLETED"),
    COMPLAINT_REJECTED("REJECTED"),
    COMPLAINT_WITHDRAWN("WITHDRAWN"),
    COMPLAINT_REOPENED("REOPENED"),
    COMPLAINT_ONHOLD("ONHOLD"),

    CITIZEN_SERVICE_NEW("DSNEW"),
    CITIZEN_SERVICE_PROGRESS("DSPROGRESS"),
    CITIZEN_SERVICE_APPROVED("DSAPPROVED"),
    CITIZEN_SERVICE_REJECTED("DSREJECTED"),
    CITIZEN_SERVICE_RESUBMIT("DS-RESUBMIT");

    @Getter
    private String code;

    ServiceStatus(String code) {
        this.code = code;
    }

    public static ServiceStatus parse(String code) {
        validateMandatoryCode(code);
        return findKnownCode(code);
    }

    private static ServiceStatus findKnownCode(String code) {
        final Optional<ServiceStatus> serviceStatus = Stream.of(ServiceStatus.values())
            .filter(status -> status.getCode().equals(code))
            .findFirst();
        return serviceStatus.orElseThrow(() -> new UnknownServiceStatusException(code));
    }

    private static void validateMandatoryCode(String code) {
        if (code == null) {
            throw new ServiceStatusNotPresentException();
        }
    }
}
