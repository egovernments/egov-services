package org.egov.user.domain.service;

import org.egov.user.domain.model.User;
import org.egov.user.persistence.repository.OtpRepository;
import org.egov.user.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private OtpRepository otpRepository;

    @Autowired
    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public Boolean isOtpValidationComplete(RequestInfo requestInfo, User user) {
        return otpRepository.isOtpValidationComplete(requestInfo, user);
    }

}
