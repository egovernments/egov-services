package org.egov.pgr.service;

import org.egov.pgr.contracts.user.GetUserByIdResponse;
import org.egov.pgr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GetUserByIdResponse getUserById(Long userId) {
        return userRepository.findUserById(userId);
    }
}
