package org.egov.workflow.service;

import org.egov.workflow.model.UserResponse;

public interface UserService {

    UserResponse getUserById(Long userId);
}
