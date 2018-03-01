package org.egov.user.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.domain.service.UserServiceVersionv11;
import org.egov.user.domain.v11.model.User;
import org.egov.user.domain.v11.model.UserSearchCriteria;
import org.egov.user.validator.RequestValidator;
import org.egov.user.web.contract.factory.ResponseInfoFactory;
import org.egov.user.web.errorhandlers.ErrorResponse;
import org.egov.user.web.v11.contract.UserRequest;
import org.egov.user.web.v11.contract.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v110")
public class UserVersionv11Controller {

	@Autowired
	private UserServiceVersionv11 userService;

	@Autowired
	RequestValidator requestValidator;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping(value = "/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final UserRequest userRequest, final BindingResult errors) {
		if (errors.hasErrors()) {
			final List<ErrorResponse> errResList = new ArrayList<>();
			errResList.add(requestValidator.populateErrors(errors));

			return new ResponseEntity<>(errResList, HttpStatus.BAD_REQUEST);
		}
		final List<ErrorResponse> errorResponses = requestValidator.validateRequest(userRequest, Boolean.TRUE);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
		userService.createUser(userRequest);
		return createSuccessResponse(userRequest.getRequestInfo(), userRequest.getUsers());
	}

	@PostMapping("/_update")
	public ResponseEntity<?> updateUser(@RequestBody @Valid final UserRequest userRequest, final BindingResult errors) {
		if (errors.hasErrors()) {
			final List<ErrorResponse> errResList = new ArrayList<>();
			errResList.add(requestValidator.populateErrors(errors));

			return new ResponseEntity<>(errResList, HttpStatus.BAD_REQUEST);
		}
		final List<ErrorResponse> errorResponses = requestValidator.validateRequest(userRequest, Boolean.FALSE);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
		userService.updateUser(userRequest);
		return createSuccessResponse(userRequest.getRequestInfo(), userRequest.getUsers());
	}

	@PostMapping("/_search")
	public ResponseEntity<?> searchUsers(@RequestBody RequestInfo requestInfo,
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "id", required = false) List<Long> id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber,
			@RequestParam(value = "aadharNumber", required = false) String aadharNumber,
			@RequestParam(value = "emailId", required = false) String emailId,
			@RequestParam(value = "pan", required = false) String pan,
			@RequestParam(value = "active", required = false) boolean active,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "roleCodes", required = false) List<String> roleCodes,
			@RequestParam(value = "lastChangedSince", required = false) Long lastChangedSince,
			@RequestParam(value = "includeDetails", required = false) boolean includeDetails,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) String sort) {

		UserSearchCriteria searchCriteria = UserSearchCriteria.builder().tenantId(tenantId).id(id).name(name)
				.userName(userName).aadhaarNumber(aadharNumber).mobileNumber(mobileNumber).emailId(emailId).pan(pan)
				.active(active).type(type).roleCodes(roleCodes).lastChangedSince(lastChangedSince)
				.includeDetails(includeDetails).build();

		List<User> users = userService.searchUsers(requestInfo, searchCriteria);
		return createSuccessResponse(requestInfo, users);
	}

	@PostMapping("/_sendotp")
	public ResponseEntity<?> sendOtp(@RequestBody RequestInfo requestInfo,
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "identity", required = true) String mobileNumber) {
		userService.createAndSendOtp(requestInfo, tenantId, userName, mobileNumber);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> createSuccessResponse(final RequestInfo requestInfo, List<User> users) {
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		UserResponse userResponse = new UserResponse(responseInfo, users);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

}
