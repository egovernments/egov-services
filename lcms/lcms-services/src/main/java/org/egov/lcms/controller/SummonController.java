package org.egov.lcms.controller;

import javax.validation.Valid;

import org.egov.lcms.models.SummonResponse;
import org.egov.lcms.service.SummonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/")
public class SummonController {
	
	@Autowired
	SummonService summonService;
	
	
	@RequestMapping(path="summmon/_create",method=RequestMethod.POST)
	public SummonResponse createSummon(@RequestBody @Valid SummonResponse summonResponse){
		return null;
		
	}

}
