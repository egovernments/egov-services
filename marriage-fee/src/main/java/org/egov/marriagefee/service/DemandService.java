package org.egov.marriagefee.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.marriagefee.config.PropertiesManager;
import org.egov.marriagefee.model.Bill;
import org.egov.marriagefee.model.Demand;
import org.egov.marriagefee.model.MarriageRegn;
import org.egov.marriagefee.repository.DemandServiceRepository;
import org.egov.marriagefee.web.contract.BillResponse;
import org.egov.marriagefee.web.contract.DemandResponse;
import org.egov.marriagefee.web.contract.MarriageRegnRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DemandService {

	@Autowired
	private DemandServiceRepository demandServiceRepository;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTempplate;

	public void createMarriageRegnDemand(MarriageRegnRequest marriageRegnRequest) {
		log.info(" DemandService  createDemand" + marriageRegnRequest);
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		List<Demand> demands = demandServiceRepository.prepareDemand(marriageRegn);
		DemandResponse demandResponse = demandServiceRepository.createDemand(demands,
				requestInfo);
		log.info(" DemandService  createDemand" + demandResponse);
		marriageRegn.setDemands(demandResponse.getDemands());

		log.info(" DemandService  marriageRegnRequest.getRequestInfo()" + requestInfo);
		ResponseEntity<?> billResponse = demandServiceRepository
				.generatebillForMarriageRegnDemand(demandResponse.getDemands(), requestInfo);
		log.info(" DemandService  createDemand" + billResponse);
		log.info(" DemandService  createDemand" + marriageRegnRequest);
		log.info(" DemandService  createDemand" + propertiesManager.getCreateMarriageRegnTopicName());
		BillResponse billResponseBody=null;
		billResponseBody= (BillResponse) billResponse.getBody();
		
		log.info(" DemandService  createReciept billResponseBody" + billResponseBody);
		log.info(" DemandService  createReciept  requestInfo" + requestInfo);
		ResponseEntity<?> receiptResponse	=demandServiceRepository.generaterecieptForMarriagerRegnPayment(billResponseBody.getBill(), requestInfo);
		kafkaTempplate.send(propertiesManager.getCreateMarriageRegnTopicName(), marriageRegnRequest);
	}

}
