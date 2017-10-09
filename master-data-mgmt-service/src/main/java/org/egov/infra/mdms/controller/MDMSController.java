package org.egov.infra.mdms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/infra-mdms")
public class MDMSController {

	public static final Logger logger = LoggerFactory.getLogger(MDMSController.class);

	
    @PostMapping("/_search")
    @ResponseBody
    private ResponseEntity<?> produceIndexJson(@RequestParam(name = "topic") String topic, @RequestBody Object indexJson){
    	try{
    		
    	}catch(Exception e){
    		return new ResponseEntity<>(indexJson ,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<>(indexJson ,HttpStatus.OK);

    }
}
