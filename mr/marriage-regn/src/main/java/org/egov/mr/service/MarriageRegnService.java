package org.egov.mr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Fee;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.repository.FeeRepository;
import org.egov.mr.repository.MarriageCertRepository;
import org.egov.mr.repository.MarriageRegnRepository;
import org.egov.mr.repository.MarryingPersonRepository;
import org.egov.mr.web.contract.FeeRequest;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MarriageRegnService {

	@Autowired
	private MarriageRegnRepository marriageRegnRepository;

	@Autowired
	private MarryingPersonRepository marryingPersonRepository;

	@Autowired
	private MarriageCertRepository marriageCertRepository;

	@Autowired
	private RegnNumberService regnNumberService;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public List<MarriageRegn> getMarriageRegns(MarriageRegnCriteria marriageRegnCriteria,
			org.egov.common.contract.request.RequestInfo requestInfo) {
		List<MarriageRegn> marriageRegnList = marriageRegnRepository.findForCriteria(marriageRegnCriteria);
		return marriageRegnList;
	}

	public MarriageRegn createAsync(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();

		String applicationNumber = marriageRegnRepository.generateApplicationNumber();
		marriageRegn.setApplicationNumber(applicationNumber);
		populateAuditDetailsForMarriageRegnCreate(marriageRegnRequest);

		log.info("marriageRegnRequest::" + marriageRegnRequest);
		kafkaTemplate.send(propertiesManager.getCreateMarriageFeeGenerated(), marriageRegnRequest);
		kafkaTemplate.send(propertiesManager.getCreateMarriageRegnTopicName(), marriageRegnRequest);
		return marriageRegn;
	}

	private void populateAuditDetailsForMarriageRegnCreate(MarriageRegnRequest marriageRegnRequest) {
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();

		marriageRegn.setIsActive(false);
		/*
		 * AuditDetails auditDetails = new AuditDetails();
		 * auditDetails.setCreatedBy(requestInfo.);
		 * auditDetails.setCreatedTime(new Date().getTime());
		 * auditDetails.setLastModifiedBy(requestInfo.);
		 * auditDetails.setLastModifiedTime(new Date().getTime());
		 * marriageRegn.setAuditDetails(auditDetails);
		 */
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

		log.info("marriageRegnRequest::" + marriageRegnRequest);
		kafkaTemplate.send(propertiesManager.getUpdateMarriageRegnTopicName(), marriageRegnRequest);
		return marriageRegn;
	}

	private void populateDefaultDetailsForMarriageRegnUpdate(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		if (marriageRegn.getStatus().toString().equals("REGISTERED")) {
			marriageRegn.setRegnNumber(regnNumberService.generateRegnNumber());
			marriageRegn.setRegnDate(new Date().getTime());
			marriageRegn.setIsActive(true);
		}
	}

	private void populateAuditDetailsForMarriageRegnUpdate(MarriageRegnRequest marriageRegnRequest) {
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();

		AuditDetails auditDetails = new AuditDetails();
		/* auditDetails.setLastModifiedBy(requestInfo.getRequesterId()); */
		auditDetails.setLastModifiedTime(new Date().getTime());
		marriageRegn.setAuditDetails(auditDetails);
	}

	@Transactional
	public void update(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		marriageRegnRepository.update(marriageRegn);
		marryingPersonRepository.update(marriageRegn.getBridegroom(), marriageRegn.getTenantId());
		marryingPersonRepository.update(marriageRegn.getBride(), marriageRegn.getTenantId());
		// List<String> applicationNosList =
		// marriageCertRepository.getApplicationNos(marriageRegn.getTenantId());
		if (marriageRegn.getStatus().toString().equals("REGISTERED"))
			marriageCertRepository.insert(marriageRegn);

	}
}