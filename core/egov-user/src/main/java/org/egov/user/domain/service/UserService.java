package org.egov.user.domain.service;

import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.OtpRepository;
import org.egov.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private OtpRepository otpRepository;

    public UserService(UserRepository userRepository,
                       OtpRepository otpRepository) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
    }

    public User getUserByUsername(final String userName) {
        return userRepository.findByUsername(userName);
    }

    public User getUserByEmailId(final String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    public User save(org.egov.user.domain.model.User user, boolean ensureOtpValidation) {
        user.validate();
        validateOtp(user, ensureOtpValidation);
        return persistNewUser(user);
    }

    public List<User> searchUsers(UserSearch userSearch) {
        return userRepository.findAll(userSearch);
    }

    private User persistNewUser(org.egov.user.domain.model.User user) {
        return userRepository.save(user);
    }

    private void validateOtp(org.egov.user.domain.model.User user, boolean ensureOtpValidation) {
        if (ensureOtpValidation && !otpRepository.isOtpValidationComplete(user))
            throw new OtpValidationPendingException(user);
    }


}