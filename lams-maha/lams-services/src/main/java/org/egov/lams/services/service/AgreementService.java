package org.egov.lams.services.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.Agreement;
import org.egov.lams.common.web.contract.AgreementDocs;
import org.egov.lams.common.web.contract.AuditDetails;
import org.egov.lams.common.web.request.AgreementRequest;
import org.egov.lams.common.web.response.AgreementResponse;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.util.SequenceGenUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgreementService {

	@Autowired
	private ResponseFactory responseInfoFactory;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private SequenceGenUtil sequenceGenService;

	@Autowired
	private PropertiesManager propertiesManager;

	public AgreementResponse createAgreement(AgreementRequest agreementRequest) {
		int index = 0;
		int year = Calendar.getInstance().get(Calendar.YEAR);
		List<String> ids = sequenceGenService.getIds(agreementRequest.getAgreements().size(),
				propertiesManager.getCreateAgreementSequence());

		for (Agreement agreement : agreementRequest.getAgreements()) {
			List<String> docIds = sequenceGenService.getIds(agreement.getDocuments().size(),
					propertiesManager.getCreateAgreementDocSequence());
			agreement.setId(ids.get(index).toString());
			agreement.setAgreementNumber(year + String.format("%05d", ids.get(index++)));
			int docIndex = 0;
			for (AgreementDocs document : agreement.getDocuments()) {
				document.setId(docIds.get(docIndex++).toString());
			}
			setAuditDetails(agreement, agreementRequest.getRequestInfo());
		}

		kafkaTemplate.send(propertiesManager.getStartAgreementWorkflowTopic(), agreementRequest);

		return getEstateRegisterResponse(agreementRequest.getAgreements(), agreementRequest.getRequestInfo());
	}

	public AgreementResponse updateAgreement(AgreementRequest agreementRequest) {

		kafkaTemplate.send(propertiesManager.getUpdateAgreementWorkflowTopic(), agreementRequest);

		return getEstateRegisterResponse(agreementRequest.getAgreements(), agreementRequest.getRequestInfo());
	}

	private void setAuditDetails(Agreement agreement, RequestInfo requestInfo) {
		String requesterId = requestInfo.getUserInfo().getId().toString();
		AuditDetails auditDetails = AuditDetails.builder().createdBy(requesterId).createdTime(new Date().getTime())
				.lastModifiedBy(requesterId).lastModifiedTime(new Date().getTime()).build();
		agreement.setAuditDetails(auditDetails);
	}

	private AgreementResponse getEstateRegisterResponse(List<Agreement> agreement, RequestInfo requestInfo) {
		AgreementResponse agreementResponse = new AgreementResponse();
		agreementResponse.setAgreements(agreement);
		agreementResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
		return agreementResponse;
	}

}
