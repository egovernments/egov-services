package org.egov.user.web.controller;

import org.egov.user.domain.model.User;
import org.egov.user.domain.service.UserService;
import org.egov.user.web.contract.*;
import org.egov.user.web.contract.Error;
import org.egov.user.web.contract.auth.SecureUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private UserService userService;
    private TokenStore tokenStore;

    public UserController(UserService userService,
                          TokenStore tokenStore) {
        this.userService = userService;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/users/_create")
    public UserDetailResponse createUserWithValidation(
            @RequestBody CreateUserRequest createUserRequest) {
        return createUser(createUserRequest, true);
    }

    @PostMapping("/users/_createnovalidate")
    public UserDetailResponse createUserWithoutValidation(
            @RequestBody CreateUserRequest createUserRequest) {
        return createUser(createUserRequest, false);
    }

    @PostMapping("/_search")
    public ResponseEntity<UserSearchResponse> get(@RequestBody UserSearchRequest request) {
        List<org.egov.user.domain.model.User> userModels = userService.searchUsers(request.toDomain());

        List<UserSearchResponseContent> userContracts = userModels.stream()
                .map(UserSearchResponseContent::new)
                .collect(Collectors.toList());
        ResponseInfo responseInfo = ResponseInfo.builder().status(String.valueOf(HttpStatus.OK.value())).build();
        UserSearchResponse userSearchResponse = new UserSearchResponse(responseInfo, userContracts);

        return new ResponseEntity<>(userSearchResponse, HttpStatus.OK);
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

    private ErrorResponse populateErrors() {
        ResponseInfo responseInfo = ResponseInfo.builder().status(HttpStatus.BAD_REQUEST.toString()).apiId("").build();
        Error error = Error.builder().code(1).description("Error while fetching user details").build();
        return new ErrorResponse(responseInfo, error);
    }

    private UserDetailResponse createUser(@RequestBody CreateUserRequest createUserRequest,
                                          boolean validateUser) {
        User user = createUserRequest.toDomain();
        final User newUser = userService.save(user, validateUser);
        return createResponse(newUser);
    }

    private UserDetailResponse createResponse(User newUser) {
        UserRequest userRequest = new UserRequest(newUser);
        ResponseInfo responseInfo = ResponseInfo.builder()
                .status(String.valueOf(HttpStatus.OK.value()))
                .build();
        return new UserDetailResponse(responseInfo, Collections.singletonList(userRequest));
    }
    
    @PostMapping("/users/{id}/_update")
    public UserDetailResponse updateUserWithValidation(@PathVariable final Long id,
            @RequestBody final CreateUserRequest createUserRequest) {
        return updateUser(id, createUserRequest, true);
    }
    
    private UserDetailResponse updateUser(final Long id, final CreateUserRequest createUserRequest,
            final boolean validateUser) {
        User user = createUserRequest.toUpdateDomain();
        final User updatedUser = userService.update(id, user, validateUser);
        return createResponse(updatedUser);
    }
}
