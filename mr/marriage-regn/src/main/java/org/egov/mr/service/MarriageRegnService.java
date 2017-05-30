package org.egov.mr.service;

import java.util.List;

import org.egov.mr.broker.MarriageRegnProducer;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.MarryingPerson;
import org.egov.mr.model.enums.RegnStatus;
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
	private PropertiesManager propertiesManager;

	public List<MarriageRegn> getMarriageRegns(MarriageRegnCriteria marriageRegnCriteria, RequestInfo requestInfo) {
		List<MarriageRegn> marriageRegnList = marriageRegnRepository.findForCriteria(marriageRegnCriteria);
		return marriageRegnList;
	}

	public MarriageRegn createAsync(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		marriageRegn.setApplicationNumber(marriageRegnRepository.generateApplicationNumber());

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

	@Transactional
	public void create(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		

		marriageRegnRepository.save(marriageRegn);
		MarryingPerson brideGroom = marriageRegn.getBridegroom();
		MarryingPerson bride = marriageRegn.getBride();
		marryingPersonRepository.save(brideGroom, marriageRegn.getTenantId());
		marryingPersonRepository.save(bride, marriageRegn.getTenantId());
	}

	public MarriageRegn updateAsync(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
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
	
	@Transactional
	public void update(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		marriageRegnRepository.update(marriageRegn);
		MarryingPerson brideGroom = marriageRegn.getBridegroom();
		MarryingPerson bride = marriageRegn.getBride();
		marryingPersonRepository.update(brideGroom, marriageRegn.getTenantId());
		marryingPersonRepository.update(bride, marriageRegn.getTenantId());
		//List<String> applicationNosList = marriageCertRepository.getApplicationNos(marriageRegn.getTenantId());
		//if(marriageRegn.getStatus().equals(RegnStatusEnum.REGISTERED))
			//marriageCertRepository.insert(marriageRegn);

		/*
		marriageRegn.getCertificates().forEach(certificate -> {
			if(applicationNosList.contains(certificate.getApplicationNumber()))
			     //marriageCertRepository.update(certificate);
			marriageCertRepository.insert(certificate);
		});
		*/
	}
}