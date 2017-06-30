package org.egov.pgrrest.read.domain.service;

import org.egov.pgr.common.model.OtpRequest;
import org.egov.pgr.common.model.OtpValidationRequest;
import org.egov.pgr.common.repository.ComplaintConfigurationRepository;
import org.egov.pgr.common.repository.OtpRepository;
import org.egov.pgr.common.repository.OtpSMSRepository;
import org.egov.pgrrest.read.domain.exception.OtpValidationNotCompleteException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private OtpRepository otpRepository;
    private OtpSMSRepository otpSMSSender;
    private ComplaintConfigurationRepository complaintConfigurationRepository;

    public OtpService(OtpRepository otpRepository,
                      OtpSMSRepository otpSMSSender,
                      ComplaintConfigurationRepository complaintConfigurationRepository) {
        this.otpRepository = otpRepository;
        this.otpSMSSender = otpSMSSender;
        this.complaintConfigurationRepository = complaintConfigurationRepository;
    }

    public void sendOtp(OtpRequest otpRequest) {
        otpRequest.validate();
        final String otpNumber = otpRepository.fetchOtp(otpRequest);
        otpSMSSender.send(otpRequest, otpNumber);
    }

    public void validateOtp(ServiceRequest serviceRequest) {
        final boolean otpValidationMandatory = isOtpValidationMandatory(serviceRequest);
        if (!otpValidationMandatory) {
            return;
        }
        final boolean isOtpValidationComplete = isOtpValidationComplete(serviceRequest);
        if (!isOtpValidationComplete) {
            throw new OtpValidationNotCompleteException();
        }
    }

    private boolean isOtpValidationComplete(ServiceRequest serviceRequest) {
        final OtpValidationRequest otpValidationRequest = serviceRequest.getOtpValidationRequest();
        otpValidationRequest.validate();
        return otpRepository.isOtpValidationComplete(otpValidationRequest);
    }

    private boolean isOtpValidationMandatory(ServiceRequest serviceRequest) {
        return complaintConfigurationRepository.isOtpEnabledForAnonymousComplaint(serviceRequest.getTenantId());
    }

}

