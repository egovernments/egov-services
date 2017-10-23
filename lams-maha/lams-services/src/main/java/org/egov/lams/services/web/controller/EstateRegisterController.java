package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/estates")
@Slf4j
public class EstateRegisterController {

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final EstateRegisterRequest estateRegisterRequest,
			final BindingResult bindingResult) {
		
		return null;//new ResponseEntity<>(null, HttpStatus.CREATED);
	}
}
