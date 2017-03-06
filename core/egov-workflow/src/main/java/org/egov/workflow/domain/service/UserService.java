package org.egov.workflow.domain.service;

import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.web.contract.GetUserByIdResponse;
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
