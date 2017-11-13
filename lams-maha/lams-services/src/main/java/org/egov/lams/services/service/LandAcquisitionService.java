package org.egov.lams.services.service;

import java.util.Calendar;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.LandAcquisition;
import org.egov.lams.common.web.contract.LandAcquisitionSearchCriteria;
import org.egov.lams.common.web.request.LandAcquisitionRequest;
import org.egov.lams.common.web.response.LandAcquisitionResponse;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.persistence.repository.LandAcquisitionRepository;
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
	private LandAcquisitionRepository landAcquisitionRepository;
	
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
			
			
			landAcquisition.getValuationDetails().stream().forEach(valuation-> valuation.setId(sequenceGenService.getIds(1, "seq_eg_lams_valuationdetail").get(0)));
			
			landAcquisition.getProposalDetails().setId(sequenceGenService.getIds(1, "seq_eg_lams_proposaldetails").get(0));
//			landAcquisition.getProposalDetails().setProposalNumber("P"+Calendar.getInstance().get(Calendar.YEAR)+String.format("%05d",landAcquisition.getProposalDetails().getId()));
			
			landAcquisition.getDocuments().stream().forEach(suportDocument -> {
			suportDocument.setId(sequenceGenService.getIds(1,"seq_eg_lams_supportdocuments").get(0));
			});
			
			
			
		});
		landAcquisitionRequest.getLandAcquisition().stream().forEach(landAcquisition -> {
			landAcquisition.setLandAcquisitionNumber(sequenceGen());
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
	
	
	public LandAcquisitionResponse search(LandAcquisitionSearchCriteria landAcquisitionSearchCriteria, RequestInfo requestInfo) {

		LandAcquisitionResponse landAcquisitionList = landAcquisitionRepository.search(landAcquisitionSearchCriteria);

		return landAcquisitionList;
	}
	
	
	private LandAcquisitionResponse getLandAcquisitionResponse(List<LandAcquisition> landAcquisition,
			RequestInfo requestInfo) {
		LandAcquisitionResponse landAcquisitionResponse = new LandAcquisitionResponse();
		landAcquisitionResponse.setLandAcquisition(landAcquisition);
		landAcquisitionResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
		return landAcquisitionResponse;
	}
	
	private String sequenceGen()
	{  
	    Calendar cal = Calendar.getInstance();
		String code= "LACQ-";
		String id = sequenceGenService.getIds(1, "seq_eg_lams_landacquisition").get(0);
		int year= cal.get(Calendar.YEAR);
		String idgen =String.format("%05d", id);

		String seqId =code + year + "-" +idgen;
		return seqId;
	}
	
}
