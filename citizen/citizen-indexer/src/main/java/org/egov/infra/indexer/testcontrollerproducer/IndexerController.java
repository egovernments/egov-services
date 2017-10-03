package org.egov.infra.indexer.testcontrollerproducer;

import org.egov.infra.indexer.IndexerInfraApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/push-index-json")
public class IndexerController {

	public static final Logger logger = LoggerFactory.getLogger(IndexerController.class);
	
	@Autowired
	private IndexerProducer indexerProducer;
	
    @PostMapping("/_index")
    @ResponseBody
    private ResponseEntity<?> produceIndexJson(@RequestBody Object indexJson){
    	try{
    		indexerProducer.producer("egov.wcms.newconnection-create", indexJson);
    	}catch(Exception e){
    		return new ResponseEntity<>(indexJson ,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<>(indexJson ,HttpStatus.OK);

    }

    @GetMapping("/_reload")
    @ResponseBody
    private ResponseEntity<?> reload(){
    	try{
    		IndexerInfraApplication.loadYaml();
    	}catch(Exception e){
    		return new ResponseEntity<>("Reload FAILED" ,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<>("Reload Successful" ,HttpStatus.OK);

    }
}
