package org.egov.user.web.controller;

import org.egov.user.domain.service.UserService;
import org.egov.user.persistence.entity.User;
import org.egov.user.web.contract.*;
import org.egov.user.web.contract.Error;
import org.egov.user.web.contract.auth.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private UserService userService;
    private TokenStore tokenStore;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, TokenStore tokenStore, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenStore = tokenStore;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/_create")
    public ResponseEntity<UserDetailResponse> createUserWithValidation(@RequestBody CreateUserRequest createUserRequest) {
        Boolean validateUser = Boolean.TRUE;
        return createUser(createUserRequest, validateUser);
    }

    @PostMapping("/_createnovalidate")
    public ResponseEntity<UserDetailResponse> createUserWithoutValidation(@RequestBody CreateUserRequest createUserRequest) {
        Boolean validateUser = Boolean.FALSE;
        return createUser(createUserRequest, validateUser);
    }

    @PostMapping("/_search")
    public ResponseEntity<?> get(@RequestBody UserSearchRequest request) {
        if (request.getId() != null && request.getId().size() > 0) {
            List<User> userEntities = userService.getUsersById(request.getId());
            List<org.egov.user.web.contract.UserRequest> userContracts = userEntities.stream()
                    .map(org.egov.user.web.contract.UserRequest::new).collect(Collectors.toList());
            ResponseInfo responseInfo = ResponseInfo.builder().status(String.valueOf(HttpStatus.OK.value())).build();
            UserResponse getUserByIdResponse = new UserResponse(responseInfo, userContracts);
            return new ResponseEntity<>(getUserByIdResponse, HttpStatus.OK);
        } else if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            User user = userService.getUserByUsername(request.getUserName());
            List<User> userEntities = new ArrayList<>();
            if (user != null)
                userEntities.add(user);
            List<org.egov.user.web.contract.UserRequest> userContracts = userEntities.stream()
                    .map(org.egov.user.web.contract.UserRequest::new).collect(Collectors.toList());
            ResponseInfo responseInfo = ResponseInfo.builder().status(String.valueOf(HttpStatus.OK.value())).build();
            UserResponse getUserByIdResponse = new UserResponse(responseInfo, userContracts);
            return new ResponseEntity<>(getUserByIdResponse, HttpStatus.OK);
        } else {
            Error error = Error.builder().code(400).message("User ids or username cannot be empty").build();
            ResponseInfo responseInfo = ResponseInfo.builder().status(HttpStatus.BAD_REQUEST.toString()).build();
            ErrorResponse errorResponse = ErrorResponse.builder().error(error).responseInfo(responseInfo).build();

            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/_details")
    public ResponseEntity<?> getUser(@RequestParam(value = "access_token") String accessToken) {
        OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);
        if (authentication != null)
            return new ResponseEntity<>(
                    ((SecureUser) authentication.getPrincipal()).getUser(), HttpStatus.OK);
        else {
            ErrorResponse errRes = populateErrors();
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<UserDetailResponse> createUser(@RequestBody CreateUserRequest createUserRequest, Boolean validateUser) {
        org.egov.user.domain.model.User user = createUserRequest.toDomainForCreate(passwordEncoder);
        UserRequest userRequest = new UserRequest(userService.save(createUserRequest.getRequestInfo(), user, validateUser));
        ResponseInfo responseInfo = ResponseInfo.builder().status(String.valueOf(HttpStatus.OK.value())).build();
        UserDetailResponse createdUserResponse = new UserDetailResponse(responseInfo, Arrays.asList(userRequest));
        return new ResponseEntity<>(createdUserResponse, HttpStatus.OK);
    }

    private ErrorResponse populateErrors() {
        ResponseInfo responseInfo = ResponseInfo.builder().status(HttpStatus.BAD_REQUEST.toString()).apiId("").build();

        Error error = Error.builder().code(1).description("Error while fetching user details").build();

        return ErrorResponse.builder().responseInfo(responseInfo).error(error).build();
    }
}
