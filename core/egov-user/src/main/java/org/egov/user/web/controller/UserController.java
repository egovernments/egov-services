package org.egov.user.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserDetail;
import org.egov.user.domain.service.TokenService;
import org.egov.user.domain.service.UserService;
import org.egov.user.web.contract.*;
import org.egov.user.web.contract.auth.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserController {

	private UserService userService;
	private TokenService tokenService;

	@Value("${mobile.number.validation.workaround.enabled}")
	private String mobileValidationWorkaroundEnabled;
	private static final String X_PASS_THROUGH_GATEWAY_KEY = "x-pass-through-gateway";
	
	@Value("${otp.validation.register.mandatory}")
	private boolean IsValidationMandatory;

	@Value("${citizen.registration.withlogin.enabled}")
	private boolean isRegWithLoginEnabled;


	@Autowired
	public UserController(UserService userService, TokenService tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

	/**
	 * end-point to create the citizen with otp.Here otp is mandatory to create
	 * citizen.
	 * 
	 * @param createUserRequest
	 * @return
	 */
	@PostMapping("/citizen/_create")
	public Object createCitizen(@RequestBody CreateUserRequest createUserRequest) {
		log.info("Received Citizen Registration Request  " + createUserRequest);
		User user = createUserRequest.toDomain(true);
		user.setOtpValidationMandatory(IsValidationMandatory);
		if(isRegWithLoginEnabled) {
			Object object = userService.registerWithLogin(user);
			return new ResponseEntity<>(object, HttpStatus.OK);
		}
		User createdUser = userService.createCitizen(user);
		return createResponse(createdUser);
	}

	/**
	 * end-point to create the user without otp validation.
	 * 
	 * @param createUserRequest
	 * @param headers
	 * @return
	 */
	@PostMapping("/users/_createnovalidate")
	public UserDetailResponse createUserWithoutValidation(@RequestBody CreateUserRequest createUserRequest,
			@RequestHeader HttpHeaders headers) {
		User user = createUserRequest.toDomain(true);
		user.setMobileValidationMandatory(isMobileValidationRequired(headers));
		user.setOtpValidationMandatory(false);
		final User newUser = userService.createUser(user);
		return createResponse(newUser);
	}

	/**
	 * end-point to search the users by providing userSearchRequest. In Request
	 * if there is no active filed value, it will fetch only active users
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/_search")
	public UserSearchResponse get(@RequestBody UserSearchRequest request, HttpServletRequest servletRequest){

		log.debug("Received User search Request  " +  request);
		if (request.getActive() == null) {
			request.setActive(true);
		}
		return searchUsers(request);
	}

	/**
	 * end-point to search the users by providing userSearchRequest. In Request
	 * if there is no active filed value, it will fetch all(active & inactive)
	 * users.
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/v1/_search")
	public UserSearchResponse getV1(@RequestBody UserSearchRequest request) {
		return searchUsers(request);
	}

	/**
	 * end-point to fetch the user details by access-token
	 * 
	 * @param accessToken
	 * @return
	 */
	@PostMapping("/_details")
	public CustomUserDetails getUser(@RequestParam(value = "access_token") String accessToken) {
		final UserDetail userDetail = tokenService.getUser(accessToken);
		return new CustomUserDetails(userDetail);
	}

	/**
	 * end-point to update the user details without otp validations.
	 *
	 * @param createUserRequest
	 * @param headers
	 * @return
	 */
	@PostMapping("/users/_updatenovalidate")
	public UserDetailResponse updateUserWithoutValidation(@RequestBody final CreateUserRequest createUserRequest, @RequestHeader HttpHeaders headers) {
		User user = createUserRequest.toDomain(false);
		user.setMobileValidationMandatory(isMobileValidationRequired(headers));
		final User updatedUser = userService.updateWithoutOtpValidation( user);
		return createResponse(updatedUser);
	}

	/**
	 * end-point to update user profile.
	 * 
	 * @param createUserRequest
	 * @return
	 */
	@PostMapping("/profile/_update")
	public UserDetailResponse patch(@RequestBody final CreateUserRequest createUserRequest) {
		log.info("Received Profile Update Request  " + createUserRequest);
		User user = createUserRequest.toDomain(false);
		final User updatedUser = userService.partialUpdate(user);
		return createResponse(updatedUser);
	}

	private UserDetailResponse createResponse(User newUser) {
		UserRequest userRequest = new UserRequest(newUser);
		ResponseInfo responseInfo = ResponseInfo.builder().status(String.valueOf(HttpStatus.OK.value())).build();
		return new UserDetailResponse(responseInfo, Collections.singletonList(userRequest));
	}

	private UserSearchResponse searchUsers(@RequestBody UserSearchRequest request) {
		List<User> userModels = userService.searchUsers(request.toDomain());

		List<UserSearchResponseContent> userContracts = userModels.stream().map(UserSearchResponseContent::new)
				.collect(Collectors.toList());
		ResponseInfo responseInfo = ResponseInfo.builder().status(String.valueOf(HttpStatus.OK.value())).build();
		return new UserSearchResponse(responseInfo, userContracts);
	}

	private boolean isMobileValidationRequired(HttpHeaders headers) {
		String x_pass_through_gatewayStr = headers.getFirst(X_PASS_THROUGH_GATEWAY_KEY);
		Boolean x_pass_through_gateway = false;
		if (x_pass_through_gatewayStr != null && x_pass_through_gatewayStr.equalsIgnoreCase("true")) {
			x_pass_through_gateway = true;
		}
		if (mobileValidationWorkaroundEnabled != null && Boolean.valueOf(mobileValidationWorkaroundEnabled)
				&& !x_pass_through_gateway) {
			return false;
		}
		return true;
	}
}
