package org.egov.lams.services.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.LandAcquisition;
import org.egov.lams.common.web.request.LandAcquisitionRequest;
import org.egov.lams.common.web.response.LandAcquisitionResponse;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.util.SequenceGenUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service 
public class LandAcquisitionService {
	
	@Autowired
	private ResponseFactory responseInfoFactory;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private SequenceGenUtil sequenceGenService;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	public LandAcquisitionResponse  create(LandAcquisitionRequest landAcquisitionRequest) {
		
		landAcquisitionRequest.getLandAcquisition().stream().forEach(landAcquisition -> {
			landAcquisition.setId(sequenceGenService.getIds(1, "seq_eg_lams_landacquisition").get(0));
			
			landAcquisition.getPossessionOfLand().stream().forEach(possessionOfLand -> {
			possessionOfLand.setId(sequenceGenService.getIds(1, "seq_eg_lams_landpossession").get(0));
			});
			
			landAcquisition.getLandTransfer().stream().forEach(landTransfer -> {
			landTransfer.setId(sequenceGenService.getIds(1, "seq_eg_lams_landtransfer").get(0));
			});
			
			landAcquisition.getValuationDetails().setId(sequenceGenService.getIds(1, "seq_eg_lams_valuationdetail").get(0));
			
			landAcquisition.getProposalDetails().setId(sequenceGenService.getIds(1, "seq_eg_lams_proposaldetails").get(0));
			
			landAcquisition.getSupportDocuments().stream().forEach(suportDocument -> {
			suportDocument.setId(sequenceGenService.getIds(1,"seq_eg_lams_supportdocuments").get(0));
			});
			
		});
		
		kafkaTemplate.send(propertiesManager.getCreateLandAcquisitionKafkaTopic(), landAcquisitionRequest);
		return getLandAcquisitionResponse(landAcquisitionRequest.getLandAcquisition(),
				landAcquisitionRequest.getRequestInfo());	
		}
	
	public LandAcquisitionResponse  update(LandAcquisitionRequest landAcquisitionRequest) {
		kafkaTemplate.send(propertiesManager.getUpdateLandAcquisitionKafkaTopic(), landAcquisitionRequest);
		return getLandAcquisitionResponse(landAcquisitionRequest.getLandAcquisition(),
				landAcquisitionRequest.getRequestInfo());	
		}
	
	private LandAcquisitionResponse getLandAcquisitionResponse(List<LandAcquisition> landAcquisition,
			RequestInfo requestInfo) {
		LandAcquisitionResponse landAcquisitionResponse = new LandAcquisitionResponse();
		landAcquisitionResponse.setLandAcquisitions(landAcquisition);
		landAcquisitionResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
		return landAcquisitionResponse;
	}
}
