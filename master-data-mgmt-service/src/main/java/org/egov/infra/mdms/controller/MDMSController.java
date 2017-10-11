package org.egov.infra.mdms.controller;

import org.egov.infra.mdms.model.MdmsCriteriaReq;
import org.egov.infra.mdms.service.MDMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("mdms")
@Slf4j
public class MDMSController {

	@Autowired
	private MDMSService mdmsService;
	
    @PostMapping("_search")
    @ResponseBody
    private ResponseEntity<?> search(@RequestBody MdmsCriteriaReq mdmsCriteriaReq){
    	log.info("MDMSController mdmsCriteriaReq:"+mdmsCriteriaReq);
    	String s = mdmsService.getMaster(mdmsCriteriaReq);
		return new ResponseEntity<>(s ,HttpStatus.OK);

    }
}
