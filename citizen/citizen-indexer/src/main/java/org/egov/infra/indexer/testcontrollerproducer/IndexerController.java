package org.egov.infra.indexer.testcontrollerproducer;

import org.egov.IndexerApplicationRunnerImpl;
import org.egov.infra.indexer.consumer.KafkaConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index-operations")
public class IndexerController {

	public static final Logger logger = LoggerFactory.getLogger(IndexerController.class);
	
	@Autowired
	private IndexerProducer indexerProducer;
	
	@Autowired
	private KafkaConsumerConfig kafkaConsumerConfig;
	
	@Autowired
	private IndexerApplicationRunnerImpl runner;
	
    @PostMapping("/_index")
    @ResponseBody
    private ResponseEntity<?> produceIndexJson(@RequestParam(name = "topic") String topic, @RequestBody Object indexJson){
    	try{
    		indexerProducer.producer(topic, indexJson);
    	}catch(Exception e){
    		return new ResponseEntity<>(indexJson ,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<>(indexJson ,HttpStatus.OK);

    }

    @GetMapping("/_reload")
    @ResponseBody
    private ResponseEntity<?> reload(){
    	Object response = null;
    	try{
    		runner.readFiles();
    		kafkaConsumerConfig.startContainer();
    	}catch(Exception e){
    		response = "Reload FAILED";
    		return new ResponseEntity<>(response ,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	response = "Reload Successful";
		return new ResponseEntity<>(response ,HttpStatus.OK);

    }
}
