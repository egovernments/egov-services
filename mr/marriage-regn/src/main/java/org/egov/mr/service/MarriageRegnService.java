package org.egov.mr.service;

import java.util.Date;
import java.util.List;

import org.egov.mr.broker.MarriageRegnProducer;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.MarryingPerson;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.repository.MarriageCertRepository;
import org.egov.mr.repository.MarriageRegnRepository;
import org.egov.mr.repository.MarryingPersonRepository;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.mr.web.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MarriageRegnService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MarriageRegnService.class);
	
	@Autowired
	private MarriageRegnRepository marriageRegnRepository;
	
	@Autowired
	private MarryingPersonRepository marryingPersonRepository;

	@Autowired
	private MarriageCertRepository marriageCertRepository;
	
	@Autowired
	private MarriageRegnProducer marriageRegnProducer;
	
	@Autowired
	private RegnNumberService regnNumberService;
	
	@Autowired
	private PropertiesManager propertiesManager;

	public List<MarriageRegn> getMarriageRegns(MarriageRegnCriteria marriageRegnCriteria, RequestInfo requestInfo) {
		List<MarriageRegn> marriageRegnList = marriageRegnRepository.findForCriteria(marriageRegnCriteria);
		return marriageRegnList;
	}

	public MarriageRegn createAsync(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		marriageRegn.setApplicationNumber(marriageRegnRepository.generateApplicationNumber());
		populateAuditDetailsForMarriageRegnCreate(marriageRegnRequest);
	
		String marriageRegnRequestJson = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			marriageRegnRequestJson = mapper.writeValueAsString(marriageRegnRequest);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("marriageRegnRequestJson::" + marriageRegnRequestJson);
		marriageRegnProducer.sendMessage(propertiesManager.getCreateMarriageRegnTopicName(), propertiesManager.getMarriageRegnKey(),
				marriageRegnRequestJson);
		return marriageRegn;	
	}

	private void populateAuditDetailsForMarriageRegnCreate(MarriageRegnRequest marriageRegnRequest) {
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();

		marriageRegn.setIsActive(false);
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(requestInfo.getRequesterId());
		auditDetails.setCreatedTime(new Date().getTime());
		auditDetails.setLastModifiedBy(requestInfo.getRequesterId());
		auditDetails.setLastModifiedTime(new Date().getTime());	
		marriageRegn.setAuditDetails(auditDetails);
	}

	@Transactional
	public void create(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		marriageRegnRepository.save(marriageRegn);
		marryingPersonRepository.save(marriageRegn.getBridegroom(), marriageRegn.getTenantId());
		marryingPersonRepository.save(marriageRegn.getBride(), marriageRegn.getTenantId());
	}

	public MarriageRegn updateAsync(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		populateAuditDetailsForMarriageRegnUpdate(marriageRegnRequest);
		populateDefaultDetailsForMarriageRegnUpdate(marriageRegnRequest);
		String marriageRegnRequestJson = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			marriageRegnRequestJson = mapper.writeValueAsString(marriageRegnRequest);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		LOGGER.info("marriageRegnRequestJson::" + marriageRegnRequestJson);
		marriageRegnProducer.sendMessage(propertiesManager.getUpdateMarriageRegnTopicName(), propertiesManager.getMarriageRegnKey(),
				marriageRegnRequestJson);
		return marriageRegn;	
	}
	
	private void populateDefaultDetailsForMarriageRegnUpdate(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		if(marriageRegn.getStatus().toString().equals("REGISTERED"))
		{
			marriageRegn.setRegnNumber(regnNumberService.generateRegnNumber());
			marriageRegn.setRegnDate(new Date().getTime());
			marriageRegn.setIsActive(true);
		}
	}

	private void populateAuditDetailsForMarriageRegnUpdate(MarriageRegnRequest marriageRegnRequest) {
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setLastModifiedBy(requestInfo.getRequesterId());
		auditDetails.setLastModifiedTime(new Date().getTime());		
		marriageRegn.setAuditDetails(auditDetails);
	}

	@Transactional
	public void update(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		marriageRegnRepository.update(marriageRegn);
		marryingPersonRepository.update(marriageRegn.getBridegroom(), marriageRegn.getTenantId());
		marryingPersonRepository.update(marriageRegn.getBride(), marriageRegn.getTenantId());
		//List<String> applicationNosList = marriageCertRepository.getApplicationNos(marriageRegn.getTenantId());
		if(marriageRegn.getStatus().toString().equals("REGISTERED"))
			marriageCertRepository.insert(marriageRegn);
		
	}
}