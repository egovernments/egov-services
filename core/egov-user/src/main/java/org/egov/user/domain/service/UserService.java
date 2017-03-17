package org.egov.user.domain.service;

import org.egov.user.domain.exception.DuplicateUserNameException;
import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserSearch;
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

    public User save(User user, boolean ensureOtpValidation) {
        user.validate();
        validateOtp(user, ensureOtpValidation);
        validateDuplicateUserName(user);
        return persistNewUser(user);
    }

    private void validateDuplicateUserName(User user) {
        if( userRepository.isUserPresent(user.getUsername())) {
            throw new DuplicateUserNameException(user);
        }
    }

    public List<org.egov.user.domain.model.User> searchUsers(UserSearch userSearch) {
        return userRepository.findAll(userSearch);
    }

    private User persistNewUser(User user) {
        return userRepository.save(user);
    }

    private void validateOtp(User user, boolean ensureOtpValidation) {
        if (ensureOtpValidation && !otpRepository.isOtpValidationComplete(user))
            throw new OtpValidationPendingException(user);
    }


}