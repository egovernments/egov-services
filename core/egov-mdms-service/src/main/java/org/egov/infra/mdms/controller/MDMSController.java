package org.egov.infra.mdms.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.infra.mdms.service.MDMSService;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@RestController
@Slf4j
public class MDMSController {

	@Autowired
	private MDMSService mdmsService;
	
    @PostMapping("_search")
    @ResponseBody
    private ResponseEntity<?> search(@RequestBody @Valid MdmsCriteriaReq mdmsCriteriaReq){
    	log.info("MDMSController mdmsCriteriaReq:"+mdmsCriteriaReq);
    	/*if(bindingResult.hasErrors()) {
    		throw new CustomBindingResultExceprion(bindingResult);
    	}*/
    	Map<String, List<Map<String, JSONArray>>> response = mdmsService.getMaster(mdmsCriteriaReq);
    	MdmsResponse mdmsResponse = new MdmsResponse();
    	mdmsResponse.setMdmsRes(response);
		return new ResponseEntity<>(mdmsResponse ,HttpStatus.OK);

    }
}
