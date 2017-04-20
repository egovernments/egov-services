package org.egov.user.domain.service;

import org.egov.user.domain.exception.*;
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

    public List<org.egov.user.domain.model.User> searchUsers(UserSearch userSearch) {
        return userRepository.findAll(userSearch);
    }

    public User updateWithoutOtpValidation(final Long id, final User user) {
        validateUser(id, user);
        return updateExistingUser(id, user);
    }

    public User partialUpdate(final User user) {
		validateUserId(user);
		validateProfileUpdateIsDoneByTheSameLoggedInUser(user);
		user.nullifySensitiveFields();
		return updateExistingUser(user.getId(), user);
	}

	private void validateProfileUpdateIsDoneByTheSameLoggedInUser(User user) {
		if(user.isLoggedInUserDifferentFromUpdatedUser()) {
			throw new UserProfileUpdateDeniedException();
		}
	}

	private void validateUserId(User user) {
		if (user.isIdAbsent()) {
			throw new UserIdMandatoryException();
		}
	}

	private void validateDuplicateUserName(Long id, User user) {
		if( userRepository.isUserPresent(user.getUsername(), id, user.getTenantId())) {
			throw new DuplicateUserNameException(user);
		}
	}

	private void validateDuplicateUserName(User user) {
		if( userRepository.isUserPresent(user.getUsername(), user.getTenantId())) {
			throw new DuplicateUserNameException(user);
		}
	}

	private User persistNewUser(User user) {
		return userRepository.save(user);
	}

	private void validateOtp(User user, boolean ensureOtpValidation) {
		if (ensureOtpValidation && !otpRepository.isOtpValidationComplete(user))
			throw new OtpValidationPendingException(user);
	}

	private void validateUser(final Long id, final User user) {
        validateDuplicateUserName(id, user);
        if (userRepository.getUserById(id) == null) {
            throw new UserNotFoundException(user);
        }
    }

    private User updateExistingUser(final Long id, final User user) {
        return userRepository.update(id, user);
    }
}