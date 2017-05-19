package org.pgr.batch.service;

import org.egov.common.contract.request.User;
import org.pgr.batch.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserByUserName(String userName, String tenantId){
        return userRepository.getUserByUserName(userName,tenantId);
    }
}
