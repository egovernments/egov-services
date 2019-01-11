package org.egov.infra.indexer.controller;

import javax.validation.Valid;

import org.egov.IndexerApplicationRunnerImpl;
import org.egov.infra.indexer.producer.IndexerProducer;
import org.egov.infra.indexer.service.IndexerService;
import org.egov.infra.indexer.service.LegacyIndexService;
import org.egov.infra.indexer.service.ReindexService;
import org.egov.infra.indexer.util.ResponseInfoFactory;
import org.egov.infra.indexer.validator.Validator;
import org.egov.infra.indexer.web.contract.LegacyIndexRequest;
import org.egov.infra.indexer.web.contract.LegacyIndexResponse;
import org.egov.infra.indexer.web.contract.ReindexRequest;
import org.egov.infra.indexer.web.contract.ReindexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private IndexerApplicationRunnerImpl runner;
	
	@Autowired
	private IndexerService service;
	
	@Autowired
	private ReindexService reindexService;
	
	@Autowired
	private LegacyIndexService legacyIndexService;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Autowired
	private Validator validator;
	
	//This is testing API 
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
    
    @PostMapping("/_reindex")
    @ResponseBody
    private ResponseEntity<?> reIndexData(@Valid @RequestBody ReindexRequest reindexRequest){
    	validator.validaterReindexRequest(reindexRequest);
    	ReindexResponse response = reindexService.createReindexJob(reindexRequest);
		return new ResponseEntity<>(response ,HttpStatus.OK);

    }
    
    @PostMapping("/_legacyindex")
    @ResponseBody
    private ResponseEntity<?> legacyIndexData(@Valid @RequestBody LegacyIndexRequest legacyIndexRequest){
    	validator.validaterLegacyindexRequest(legacyIndexRequest);
    	LegacyIndexResponse response = legacyIndexService.createLegacyindexJob(legacyIndexRequest);
		return new ResponseEntity<>(response ,HttpStatus.OK);

    }
}
