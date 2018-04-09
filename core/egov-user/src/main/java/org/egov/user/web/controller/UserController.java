package org.egov.user.web.controller;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserDetail;
import org.egov.user.domain.service.TokenService;
import org.egov.user.domain.service.UserService;
import org.egov.user.web.contract.*;
import org.egov.user.web.contract.auth.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

	private UserService userService;
	private TokenService tokenService;

	public UserController(UserService userService, TokenService tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

	@PostMapping("/citizen/_create")
	public UserDetailResponse createCitizen(@RequestBody CreateUserRequest createUserRequest) {
		User user = createUserRequest.toDomain(true);
		user.setOtpValidationMandatory(true);
		final User newUser = userService.createCitizen(user);
		return createResponse(newUser);
	}

	@PostMapping("/users/_createnovalidate")
	public UserDetailResponse createUserWithoutValidation(@RequestBody CreateUserRequest createUserRequest) {
		User user = createUserRequest.toDomain(true);
		user.setOtpValidationMandatory(false);
		final User newUser = userService.createUser(user);
		return createResponse(newUser);
	}

	@PostMapping("/_search")
	public UserSearchResponse get(@RequestBody UserSearchRequest request) {
		if(request.getActive() == null) {
			request.setActive(true);
		}
		return searchUsers(request);
	}


	@PostMapping("/v1/_search")
	public UserSearchResponse getV1(@RequestBody UserSearchRequest request) {
		return searchUsers(request);
	}

	@PostMapping("/_details")
	public CustomUserDetails getUser(@RequestParam(value = "access_token") String accessToken) {
		final UserDetail userDetail = tokenService.getUser(accessToken);
		return new CustomUserDetails(userDetail);
	}

	@PostMapping("/users/{id}/_updatenovalidate")
	public UserDetailResponse updateUserWithoutValidation(@PathVariable final Long id,
														  @RequestBody final CreateUserRequest createUserRequest) {
		User user = createUserRequest.toDomain(false);
		user.setId(id);
		final User updatedUser = userService.updateWithoutOtpValidation(id, user);
		return createResponse(updatedUser);
	}

	@PostMapping("/profile/_update")
	public UserDetailResponse patch(@RequestBody final CreateUserRequest createUserRequest) {
		User user = createUserRequest.toDomain(false);
		final User updatedUser = userService.partialUpdate(user);
		return createResponse(updatedUser);
	}

	private UserDetailResponse createResponse(User newUser) {
		UserRequest userRequest = new UserRequest(newUser);
		ResponseInfo responseInfo = ResponseInfo.builder()
				.status(String.valueOf(HttpStatus.OK.value()))
				.build();
		return new UserDetailResponse(responseInfo, Collections.singletonList(userRequest));
	}


	private UserSearchResponse searchUsers(@RequestBody UserSearchRequest request) {
		List<User> userModels = userService.searchUsers(request.toDomain());

		List<UserSearchResponseContent> userContracts = userModels.stream()
				.map(UserSearchResponseContent::new)
				.collect(Collectors.toList());
		ResponseInfo responseInfo = ResponseInfo.builder().status(String.valueOf(HttpStatus.OK.value())).build();
		return new UserSearchResponse(responseInfo, userContracts);
	}

}