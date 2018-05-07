package org.egov.user.web.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.user.contract.UserReq;
import org.egov.user.contract.UserRes;
import org.egov.user.contract.UserSearchCriteria;
import org.egov.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("v2")
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	/*
	 *  To create new user in the system
	 * 
	 *  
	 * */
	@PostMapping("_create")
	public ResponseEntity<Optional<?>> create(@RequestBody @Valid UserReq userReq) {
		return new ResponseEntity<>(userService.create(userReq),HttpStatus.CREATED);
	}
	
	@PostMapping("_search")
	public ResponseEntity<UserRes> search(@RequestBody @Valid RequestInfo requestInfo, @ModelAttribute UserSearchCriteria userSearchCriteria) {
		
		return new ResponseEntity<>(userService.search(userSearchCriteria, requestInfo),HttpStatus.OK);
	}
	
	@PostMapping("_update")
	public String update(@RequestBody UserReq userReq) {
		userService.update(userReq);
		return null;
	}
}
