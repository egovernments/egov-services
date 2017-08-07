package org.egov.user.web.controller;

import javax.validation.Valid;

import org.egov.user.model.RequestInfoWrapper;
import org.egov.user.model.User;
import org.egov.user.model.UserReq;
import org.egov.user.model.UserRes;
import org.egov.user.model.UserSearchCriteria;
import org.egov.user.service.NewUserService;
import org.egov.user.web.contract.factory.ResponseFactory;
import org.egov.user.web.validator.NewUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v110")
@Slf4j
public class NewUserController {
	
	@Autowired
	private NewUserService newUserService;
	
	@Autowired
	private NewUserValidator userValidator;
	
	@Autowired
	private ResponseFactory responseFactory;
	
	@PostMapping("_search")
	public ResponseEntity<?> search(@ModelAttribute UserSearchCriteria userSearchCriteria,
			@RequestBody RequestInfoWrapper requestInfoWrapper){
		log.info("NewUserController userSearchCriteria:"+userSearchCriteria);
		UserRes userRes = newUserService.search(userSearchCriteria);
		return new ResponseEntity<>(userRes, HttpStatus.OK);
	}
	
	@PostMapping("_create")
	public ResponseEntity<?> create(@RequestBody @Valid UserReq userReq, BindingResult errors){
		log.info("create userReq : "+userReq);

		userValidator.validate(userReq, errors);
		if (errors.hasErrors()) {
			return new ResponseEntity<>(responseFactory.getErrorResponse(errors, userReq.getRequestInfo()), HttpStatus.BAD_REQUEST);
		}
		
		UserRes userRes = newUserService.createAsync(userReq);
		return new ResponseEntity<>(userRes, HttpStatus.CREATED);
	}
	
	@PostMapping("_update")
	public ResponseEntity<?> update(@RequestBody UserReq userReq, BindingResult errors){
		log.info("update userReq : "+userReq);
		UserRes userRes = newUserService.updateAsync(userReq);
		return new ResponseEntity<>(userRes, HttpStatus.CREATED);
	}
	
}
