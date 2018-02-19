package org.egov.pgr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping(value = "/v1")
public class ServiceRequestController {


	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create() {
		return new ResponseEntity<>(HttpStatus.OK);	
	}

	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update() {
		return new ResponseEntity<>(HttpStatus.OK);	
	}

	@PostMapping("_search")
	@ResponseBody
	private ResponseEntity<?> search() {
		return new ResponseEntity<>(HttpStatus.OK);	
	}

	@PostMapping("count/_search")
	@ResponseBody
	private ResponseEntity<?> count() {
		return new ResponseEntity<>(HttpStatus.OK);	
	}

}
