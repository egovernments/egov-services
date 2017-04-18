package org.egov.lams.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Notice;
import org.egov.lams.repository.NoticeRepository;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.NoticeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	@Autowired
	private AgreementService agreementService;
	
	public NoticeResponse generateNotice(NoticeRequest noticeRequest) {
		
		List<Agreement> agreements = getAgreementByAuckNumOrAgreementNum(noticeRequest.getNotice().getTenantId(),
											noticeRequest.getNotice().getAgreementNumber(),
											noticeRequest.getNotice().getAcknowledgementNumber());
		
		noticeRequest.getNotice().toNotice(agreements.get(0));
		
		Notice notice = noticeRepository.createNotice(noticeRequest);
		List<Notice> notices = new ArrayList<Notice>();
		notices.add(notice);
		
		return getNoticeResponse(notices);
	}
	
	public List<Agreement> getAgreementByAuckNumOrAgreementNum(String tenantId, String agreementNum, String auckNum) {
		
		AgreementCriteria agreementCriteria = new AgreementCriteria();
		agreementCriteria.setTenantId(tenantId);
		agreementCriteria.setAcknowledgementNumber(auckNum);
		agreementCriteria.setAgreementNumber(agreementNum);
		List<Agreement> agreements = agreementService.searchAgreement(agreementCriteria);
		if(agreements.isEmpty()){
			throw new RuntimeException("No agreement found for given criteria");
		}
		return agreements;
	}
	
	/*public NoticeResponse getNotice(NoticeCriteria noticeCriteria) {
		noticeRepository.createNotice(noticeRequest);
	}*/
	
	private NoticeResponse getNoticeResponse(List<Notice> notices){
		NoticeResponse  noticeResponse = new NoticeResponse();
		noticeResponse.setNotices(notices);
	  return noticeResponse;
	}
}
