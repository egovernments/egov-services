package org.egov.lams.services.service;

import java.util.Calendar;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.LandPossessionResponse;
import org.egov.lams.common.web.contract.LandPossessionSearchCriteria;
import org.egov.lams.common.web.contract.LandTransfer;
import org.egov.lams.common.web.contract.LandTransferRequest;
import org.egov.lams.common.web.contract.LandTransferResponse;
import org.egov.lams.common.web.contract.LandTransferSearchCriteria;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.persistence.repository.LandPossessionRepository;
import org.egov.lams.services.service.persistence.repository.LandTransferRepository;
import org.egov.lams.services.util.SequenceGenUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
@Service
public class LandTransferService {
	@Autowired
	private ResponseFactory responseInfoFactory;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private SequenceGenUtil sequenceGenService;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private LandTransferRepository landTransferRepository;
	
public LandTransferResponse  create(LandTransferRequest landTransferRequest) {
		
	landTransferRequest.getLandTransfer().stream().forEach(landPossession -> {
			landPossession.setId(sequenceGenService.getIds(1, "seq_eg_lams_landtransfer").get(0));
		});
		landTransferRequest.getLandTransfer().stream().forEach(landPossession -> {
			landPossession.setTransferNumber(sequenceGen());
		});
		
		kafkaTemplate.send(propertiesManager.getCreateLandTransferKafkaTopic(), landTransferRequest);
		return getLandTransferResponse(landTransferRequest.getLandTransfer(),
				landTransferRequest.getRequestInfo());	
		}

		public LandTransferResponse  update(LandTransferRequest landTransferRequest) {
	kafkaTemplate.send(propertiesManager.getUpdateLandTransferKafkaTopic(), landTransferRequest);
	return getLandTransferResponse(landTransferRequest.getLandTransfer(),
			landTransferRequest.getRequestInfo());	
	}
		
		public LandTransferResponse search(LandTransferSearchCriteria landTransferSearchCriteria, RequestInfo requestInfo) {
			LandTransferResponse landTransferResponseList = landTransferRepository.search(landTransferSearchCriteria);
			return landTransferResponseList;
		}

		private LandTransferResponse getLandTransferResponse(List<LandTransfer> landTransfer,
				RequestInfo requestInfo) {
			LandTransferResponse landTransferResponse = new LandTransferResponse();
			landTransferResponse.setLandTransfer(landTransfer);
			landTransferResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
			return landTransferResponse;
		}


		private String sequenceGen()
		{  
		    Calendar cal = Calendar.getInstance();
			String id = sequenceGenService.getIds(1, "seq_eg_lams_landtransfer").get(0);
			int year= cal.get(Calendar.YEAR);
			String idgen =String.format("%05d", id);
			String seqId =year +idgen;
			return seqId;
		}
}
