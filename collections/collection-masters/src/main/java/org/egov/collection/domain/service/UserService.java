package org.egov.collection.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import org.egov.collection.domain.model.AuthenticatedUser;
import org.egov.collection.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public AuthenticatedUser getUser(String token) {
		if (isEmpty(token)) {
			return AuthenticatedUser.createAnonymousUser();
		}
		return userRepository.findUser(token);
	}
}
