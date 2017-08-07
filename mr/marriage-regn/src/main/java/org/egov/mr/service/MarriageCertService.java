package org.egov.mr.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.MarriageDocument;
import org.egov.mr.model.Page;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.repository.MarriageCertRepository;
import org.egov.mr.util.SequenceIdGenService;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.egov.mr.web.contract.ReissueCertRequest;
import org.egov.mr.web.contract.ReissueCertResponse;
import org.egov.mr.web.contract.RequestInfo;
import org.egov.mr.web.contract.ResponseInfo;
import org.egov.mr.web.contract.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public ReissueCertResponse getMarriageCerts(MarriageCertCriteria marriageCertCriteria, RequestInfo requestInfo) {

		return getSuccessResponseForSearch(marriageCertRepository.findForCriteria(marriageCertCriteria), requestInfo);
	}

	private ReissueCertResponse getSuccessResponseForSearch(List<ReissueCertAppl> marriageCertList,
			RequestInfo requestInfo) {
		ReissueCertResponse reissueCertResponse = new ReissueCertResponse();
		Page page = new Page();
		reissueCertResponse.setReissueApplications(marriageCertList);
		log.info("marriageCertList=" + marriageCertList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		reissueCertResponse.setResponseInfo(responseInfo);
		int totalresults = marriageCertList.size();
		page.setTotalResults(totalresults);
		reissueCertResponse.setPage(page);
		return reissueCertResponse;
	}
	
	public ReissueCertResponse createAsync(ReissueCertRequest reissueCertRequest){
		
		List<MarriageDocument> marriageDocuments = reissueCertRequest.getReissueApplication().getDocuments();
		ReissueCertAppl reIssueApp = reissueCertRequest.getReissueApplication();
		List<ReissueCertAppl> reissueCertAppl=new ArrayList<ReissueCertAppl>();
		int docsSize = marriageDocuments.size();
		List<String> docIds = sequenceGenUtil.getIds(docsSize, "egmr_documents_id");
		reIssueApp.setId(sequenceGenUtil.getIds(1, "egmr_reissuecertificate_id").get(0));

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
		return getMarriageCertResponse(reissueCertAppl,reissueCertRequest.getRequestInfo());
	}
	
	public ReissueCertResponse updateAsync(ReissueCertRequest reissueCertRequest){
		
		ReissueCertAppl reIssueApp = reissueCertRequest.getReissueApplication();
		List<ReissueCertAppl> reissueCertAppl=new ArrayList<ReissueCertAppl>();
		
		try {
			kafkaTemplate.send(propertiesManager.getUpdateReissueMarriageRegnTopicName(),
					propertiesManager.getUpdateReissueMarriageRegnKey(), reissueCertRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reissueCertAppl.add(reIssueApp);
		return getMarriageCertResponse(reissueCertAppl,reissueCertRequest.getRequestInfo());
	}
	
	public void create(ReissueCertRequest reissueCertRequest){
		marriageCertRepository.createReissue(reissueCertRequest);
		marriageCertRepository.createDoc(reissueCertRequest);
	}
	
	public void update(ReissueCertRequest reissueCertRequest){
		marriageCertRepository.updateReissue(reissueCertRequest);
		marriageCertRepository.updateDoc(reissueCertRequest);
	}
	
	private ReissueCertResponse getMarriageCertResponse(List<ReissueCertAppl> reissuCert, RequestInfo requestInfo) {
		ReissueCertResponse marriageCertResponse = new ReissueCertResponse();
		marriageCertResponse.setReissueApplications(reissuCert);
		marriageCertResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
		return marriageCertResponse;
	}
}
