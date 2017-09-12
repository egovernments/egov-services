package org.egov.mr.service;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageDocument;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.Page;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.repository.MarriageCertRepository;
import org.egov.mr.util.SequenceIdGenService;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.egov.mr.web.contract.ReissueCertRequest;
import org.egov.mr.web.contract.ReissueCertResponse;
import org.egov.mr.web.contract.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarriageCertService {

	@Autowired
	private MarriageCertRepository marriageCertRepository;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private SequenceIdGenService sequenceGenUtil;

	@Autowired
	private MarriageRegnService marriageRegnService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Value("${egov.mr.services.reissueCertificate_sequence}")
	private String certSeq;

	@Value("${egov.mr.services.reissueApp_sequence}")
	private String reisuueIdSeq;

	public ReissueCertResponse getMarriageCerts(MarriageCertCriteria marriageCertCriteria, RequestInfo requestInfo) {

		return getSuccessResponseForSearch(marriageCertRepository.findForCriteria(marriageCertCriteria), requestInfo);
	}

	private ReissueCertResponse getSuccessResponseForSearch(List<ReissueCertAppl> marriageCertList,
			RequestInfo requestInfo) {
		ReissueCertResponse reissueCertResponse = new ReissueCertResponse();
		Page page = new Page();
		reissueCertResponse.setReissueApplications(marriageCertList);
		log.info("marriageCertList=" + marriageCertList);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setMsgId(requestInfo.getMsgId());
		responseInfo.setStatus(HttpStatus.OK.toString());
		responseInfo.setTs(requestInfo.getTs());
		responseInfo.setVer(requestInfo.getVer());
		reissueCertResponse.setResponseInfo(responseInfo);
		int totalresults = marriageCertList.size();
		page.setTotalResults(totalresults);
		reissueCertResponse.setPage(page);
		return reissueCertResponse;
	}

	public ReissueCertResponse createAsync(ReissueCertRequest reissueCertRequest) {

		List<MarriageDocument> marriageDocuments = reissueCertRequest.getReissueApplication().getDocuments();
		ReissueCertAppl reIssueApp = reissueCertRequest.getReissueApplication();
		List<ReissueCertAppl> reissueCertAppl = new ArrayList<ReissueCertAppl>();
		int docsSize = marriageDocuments.size();
		List<String> docIds = sequenceGenUtil.getIds(docsSize, "egmr_documents_id");
		reIssueApp.setId(sequenceGenUtil.getIds(1, reisuueIdSeq).get(0));

		int index = 0;
		for (MarriageDocument docs : marriageDocuments) {
			docs.setId(docIds.get(index++));
			docs.setReissueCertificateId(reIssueApp.getId());
		}

		try {
			kafkaTemplate.send(propertiesManager.getCreateReissueMarriageRegnTopicName(),
					propertiesManager.getCreateReissueMarriageRegnKey(), reissueCertRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reissueCertAppl.add(reIssueApp);
		return getMarriageCertResponse(reissueCertAppl, reissueCertRequest.getRequestInfo());
	}

	public ReissueCertResponse updateAsync(ReissueCertRequest reissueCertRequest) {

		ReissueCertAppl reIssueApp = reissueCertRequest.getReissueApplication();
		List<ReissueCertAppl> reissueCertAppl = new ArrayList<ReissueCertAppl>();

		try {
			kafkaTemplate.send(propertiesManager.getUpdateReissueMarriageRegnTopicName(),
					propertiesManager.getUpdateReissueMarriageRegnKey(), reissueCertRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (reIssueApp.getReissueApplStatus().equals(ApplicationStatus.APPROVED))
			certificateCreateAsync(reIssueApp);

		reissueCertAppl.add(reIssueApp);

		return getMarriageCertResponse(reissueCertAppl, reissueCertRequest.getRequestInfo());
	}

	public void certificateCreateAsync(ReissueCertAppl reIssueApp) {

		MarriageRegnCriteria marriageRegnCriteria = MarriageRegnCriteria.builder().regnNo(reIssueApp.getRegnNo())
				.tenantId(reIssueApp.getTenantId()).build();

		MarriageRegn marraigeRegn = marriageRegnService.getMarriageRegns(marriageRegnCriteria, new RequestInfo())
				.get(0);

		reIssueApp.setCertificate(marraigeRegn.getCertificates().get(0));
		reIssueApp.getCertificate().setCertificateType(CertificateType.REISSUE);
		reIssueApp.getCertificate().setCertificateDate(new Date().getTime());
		reIssueApp.getCertificate().setCertificateNo(sequenceGenUtil.getIds(1, certSeq).get(0));

		kafkaTemplate.send(propertiesManager.getCreateReissueCertificateTopicName(), reIssueApp);

	}

	public void createCert(ReissueCertAppl reIssueApp) {
		marriageCertRepository.createCert(reIssueApp);
	}

	public void create(ReissueCertRequest reissueCertRequest) {
		marriageCertRepository.createReissue(reissueCertRequest);
		marriageCertRepository.createDoc(reissueCertRequest);
	}

	public void update(ReissueCertRequest reissueCertRequest) {
		marriageCertRepository.updateReissue(reissueCertRequest);
		marriageCertRepository.updateDoc(reissueCertRequest);
	}

	private ReissueCertResponse getMarriageCertResponse(List<ReissueCertAppl> reissuCert, RequestInfo requestInfo) {
		ReissueCertResponse marriageCertResponse = new ReissueCertResponse();
		marriageCertResponse.setReissueApplications(reissuCert);
		marriageCertResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
		return marriageCertResponse;
	}

	private void getMarriageCertificate(ReissueCertAppl reIssueApp, MarriageRegn marraigeRegn) {

		MarriageCertificate marriageCertificate = new MarriageCertificate();

		marriageCertificate.setBridegroomPhoto(marraigeRegn.getBridegroom().getPhoto());
		marriageCertificate.setBridePhoto(marraigeRegn.getBride().getPhoto());
		marriageCertificate.setHusbandAddress(marraigeRegn.getBridegroom().getResidenceAddress());
		marriageCertificate.setHusbandName(marraigeRegn.getBridegroom().getName());
		marriageCertificate.setMarriageDate(marraigeRegn.getMarriageDate());
		marriageCertificate.setMarriageVenueAddress(marraigeRegn.getPlaceOfMarriage());
		marriageCertificate.setRegnDate(marraigeRegn.getMarriageDate());
		marriageCertificate.setRegnNumber(marraigeRegn.getRegnNumber());
		marriageCertificate.setRegnSerialNo(marraigeRegn.getSerialNo());
		marriageCertificate.setRegnVolumeNo(marraigeRegn.getVolumeNo());
		marriageCertificate.setTenantId(marraigeRegn.getTenantId());
		marriageCertificate.setWifeAddress(marraigeRegn.getBride().getLocality());
		marriageCertificate.setWifeName(marraigeRegn.getBride().getName());
		marriageCertificate.setTemplateVersion(marraigeRegn.getCertificates().get(0).getTemplateVersion());
		marriageCertificate.setCertificateType(CertificateType.valueOf("REISSUE"));
		marriageCertificate.setCertificatePlace(marraigeRegn.getCertificates().get(0).getCertificatePlace());
		marriageCertificate.setCertificateNo(sequenceGenUtil.getIds(1, "seq_egmr_marriage_certificate").get(0));
		marriageCertificate.setCertificateDate(new Date().getTime());

		reIssueApp.setCertificate(marraigeRegn.getCertificates().get(0));
	}

}
