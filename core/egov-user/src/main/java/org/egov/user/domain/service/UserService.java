package org.egov.user.domain.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.exception.AtleastOneRoleCodeException;
import org.egov.user.domain.exception.DuplicateUserNameException;
import org.egov.user.domain.exception.InvalidOtpException;
import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.domain.exception.PasswordMismatchException;
import org.egov.user.domain.exception.UserIdMandatoryException;
import org.egov.user.domain.exception.UserNameNotValidException;
import org.egov.user.domain.exception.UserNotFoundException;
import org.egov.user.domain.exception.UserProfileUpdateDeniedException;
import org.egov.user.domain.model.LoggedInUserUpdatePasswordRequest;
import org.egov.user.domain.model.NonLoggedInUserUpdatePasswordRequest;
import org.egov.user.domain.model.OtpValidationRequest;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.persistence.repository.OtpRepository;
import org.egov.user.persistence.repository.UserRepository;
import org.egov.user.web.contract.Otp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;

@Service
public class UserService {

	private UserRepository userRepository;
	private OtpRepository otpRepository;
	private PasswordEncoder passwordEncoder;
	private int defaultPasswordExpiryInDays;
	private boolean IsCitizenLoginOtpBased;

	public UserService(UserRepository userRepository, OtpRepository otpRepository, PasswordEncoder passwordEncoder,
			@Value("${default.password.expiry.in.days}") int defaultPasswordExpiryInDays,
			@Value("${citizen.login.password.otp.enabled}") boolean IsCitizenLoginOtpBased) {
		this.userRepository = userRepository;
		this.otpRepository = otpRepository;
		this.passwordEncoder = passwordEncoder;
		this.defaultPasswordExpiryInDays = defaultPasswordExpiryInDays;
		this.IsCitizenLoginOtpBased = IsCitizenLoginOtpBased;
	}

	public User getUserByUsername(final String userName, String tenantId) {
		return userRepository.findByUsername(userName, tenantId);
	}

	public User getUserByEmailId(final String emailId, String tenantId) {
		return userRepository.findByEmailId(emailId, tenantId);
	}

	public User createUser(User user) {
		user.setUuid(UUID.randomUUID().toString());
		user.validateNewUser();
		conditionallyValidateOtp(user);
		validateDuplicateUserName(user);
		user.setDefaultPasswordExpiry(defaultPasswordExpiryInDays);
		return persistNewUser(user);
	}

	public User createCitizen(User user) {
		user.setUuid(UUID.randomUUID().toString());
		if (IsCitizenLoginOtpBased)
			if (!StringUtils.isNumeric(user.getUsername()))
				throw new UserNameNotValidException();

		user.setRoleToCitizen();
		user.validateNewUser();
		validateDuplicateUserName(user);
		// validateOtp(user.getOtpValidationRequest());
		Otp otp = Otp.builder().otp(user.getOtpReference()).identity(user.getUsername()).tenantId(user.getTenantId())
				.build();
		try {
			validateOtp(otp);
		} catch (Exception e) {
			String errorMessage = JsonPath.read(e.getMessage(), "$.error.message");
			System.out.println("message " + errorMessage);
			throw new InvalidOtpException(errorMessage);
		}
		user.setDefaultPasswordExpiry(defaultPasswordExpiryInDays);
		user.setActive(true);
		return persistNewUser(user);
	}

	public List<org.egov.user.domain.model.User> searchUsers(UserSearchCriteria searchCriteria) {
		searchCriteria.validate();
		return userRepository.findAll(searchCriteria);
	}

	public User updateWithoutOtpValidation(final Long id, final User user) {
		user.validateUserModification();
		validateUser(id, user);
		validateUserRoles(user);
		return updateExistingUser(user);
	}

	private void validateUserRoles(User user) {
		if (user.getRoles() == null || user.getRoles() != null && user.getRoles().isEmpty()) {
			throw new AtleastOneRoleCodeException();
		}
	}

	public User partialUpdate(final User user) {
		validateUserId(user);
		validateProfileUpdateIsDoneByTheSameLoggedInUser(user);
		user.nullifySensitiveFields();
		return updateExistingUser(user);
	}

	public void updatePasswordForLoggedInUser(LoggedInUserUpdatePasswordRequest updatePasswordRequest) {
		updatePasswordRequest.validate();
		final User user = userRepository.getUserById(updatePasswordRequest.getUserId(),
				updatePasswordRequest.getTenantId());
		validateUserPresent(user);
		validateExistingPassword(user, updatePasswordRequest.getExistingPassword());
		user.updatePassword(updatePasswordRequest.getNewPassword());
		userRepository.update(user);
	}

	public void updatePasswordForNonLoggedInUser(NonLoggedInUserUpdatePasswordRequest request) {
		request.validate();
		// validateOtp(request.getOtpValidationRequest());
		Otp otp = Otp.builder().otp(request.getOtpReference()).identity(request.getUserName())
				.tenantId(request.getTenantId()).build();
		try {
			validateOtp(otp);
		} catch (Exception e) {
			String errorMessage = JsonPath.read(e.getMessage(), "$.error.message");
			System.out.println("message " + errorMessage);
			throw new InvalidOtpException(errorMessage);
		}
		final User user = fetchUserByUserName(request.getUserName(), request.getTenantId());
		validateUserPresent(user);
		user.updatePassword(request.getNewPassword());
		userRepository.update(user);
	}

	private User fetchUserByUserName(String userName, String tenantId) {
		return userRepository.findByUsername(userName, tenantId);
	}

	private void conditionallyValidateOtp(User user) {
		if (user.isOtpValidationMandatory()) {
			validateOtp(user.getOtpValidationRequest());
		}
	}

	private void validateExistingPassword(User user, String existingRawPassword) {
		if (!passwordEncoder.matches(existingRawPassword, user.getPassword())) {
			throw new PasswordMismatchException();
		}
	}

	private void validateUserPresent(User user) {
		if (user == null) {
			throw new UserNotFoundException(null);
		}
	}

	private void validateProfileUpdateIsDoneByTheSameLoggedInUser(User user) {
		if (user.isLoggedInUserDifferentFromUpdatedUser()) {
			throw new UserProfileUpdateDeniedException();
		}
	}

	private void validateUserId(User user) {
		if (user.isIdAbsent()) {
			throw new UserIdMandatoryException();
		}
	}

	private void validateDuplicateUserName(Long id, User user) {
		if (userRepository.isUserPresent(user.getUsername(), id, user.getTenantId())) {
			throw new DuplicateUserNameException(user);
		}
	}

	private void validateDuplicateUserName(User user) {
		if (userRepository.isUserPresent(user.getUsername(), user.getTenantId())) {
			throw new DuplicateUserNameException(user);
		}
	}

	private User persistNewUser(User user) {
		return userRepository.create(user);
	}

	private void validateOtp(OtpValidationRequest otpValidationRequest) {
		if (!otpRepository.isOtpValidationComplete(otpValidationRequest))
			throw new OtpValidationPendingException();
	}

	private void validateUser(final Long id, final User user) {
		validateDuplicateUserName(id, user);
		if (userRepository.getUserById(id, user.getTenantId()) == null) {
			throw new UserNotFoundException(user);
		}
	}

	private User updateExistingUser(final User user) {
		return userRepository.update(user);
	}

	public boolean validateOtp(Otp otp) throws Exception {
		// TODO Auto-generated method stub
		if (otpRepository.validateOtp(otp))
			return true;
		return false;

	}

}
