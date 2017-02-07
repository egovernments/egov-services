package org.egov.pgr.domain.service;

import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.apache.commons.lang.StringUtils.isEmpty;

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
