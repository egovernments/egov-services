package org.egov.user.web.controller;

import javax.validation.Valid;

import org.egov.user.contract.UserReq;
import org.egov.user.contract.UserRes;
import org.egov.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<UserRes> create(@RequestBody @Valid UserReq userReq) {
		userService.create(userReq);
		return null;
	}
	
	@PostMapping("_search")
	public String search(@RequestBody @Valid UserReq userReq) {
		userService.create(userReq);
		return null;
	}
	
	@PostMapping("_update")
	public String update(@RequestBody UserReq userReq) {
		userService.update(userReq);
		return null;
	}
}
