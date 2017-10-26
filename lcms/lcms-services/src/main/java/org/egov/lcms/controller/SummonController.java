package org.egov.lcms.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.SummonRequest;
import org.egov.lcms.models.SummonResponse;
import org.egov.lcms.service.SummonService;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;;

@RestController
@RequestMapping("/test/")
public class SummonController {
	
	@Autowired
	SummonService summonService;
	
	@Autowired
	ResponseFactory responseInfoFactory;
	
	@Autowired
	UniqueCodeGeneration UniqueCodeGeneration;
	
	
	@RequestMapping(path="summmon/_create",method=RequestMethod.POST)
	public ResponseEntity<?> createSummon(@RequestBody @Valid SummonRequest summonRequest,BindingResult bindingResult) throws Exception{
		RequestInfo requestInfo = summonRequest.getRequestInfo();
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseInfoFactory.getErrorResponse(bindingResult, requestInfo), HttpStatus.BAD_REQUEST);
		}
		SummonResponse summonResponse = summonService.createSummon(summonRequest);
		return new ResponseEntity<>(summonResponse, HttpStatus.CREATED);
	}

}
