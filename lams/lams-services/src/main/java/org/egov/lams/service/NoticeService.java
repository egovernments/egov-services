package org.egov.lams.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Notice;
import org.egov.lams.model.NoticeCriteria;
import org.egov.lams.repository.NoticeRepository;
import org.egov.lams.repository.WorkFlowRepository;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.NoticeResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

	private NoticeRepository noticeRepository;

	private AgreementService agreementService;

	private WorkFlowRepository workFlowRepository;

	

	public NoticeService(NoticeRepository noticeRepository, AgreementService agreementService,
			WorkFlowRepository workFlowRepository) {
		this.noticeRepository = noticeRepository;
		this.agreementService = agreementService;
		this.workFlowRepository = workFlowRepository;

	}

	public NoticeResponse generateNotice(NoticeRequest noticeRequest) {

		Notice notice = noticeRequest.getNotice();
		List<Agreement> agreements = getAgreementByAuckNumOrAgreementNum(getAgreementSearchCriteria(notice),
				noticeRequest.getRequestInfo());

		Agreement agreement = agreements.get(0);
		notice.toNotice(agreement);
		notice.setCommissionerName(workFlowRepository.getCommissionerName(agreement.getStateId(),
				agreement.getTenantId(), noticeRequest.getRequestInfo()));

		notice = noticeRepository.createNotice(noticeRequest);
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);

		return getNoticeResponse(notices);
	}

	public List<Agreement> getAgreementByAuckNumOrAgreementNum(AgreementCriteria agreementCriteria,
			RequestInfo requestInfo) {

		List<Agreement> agreements = agreementService.getAgreementsByAgreementNumber(agreementCriteria, requestInfo);
		if (agreements.isEmpty()) {
			throw new RuntimeException("No agreement found for given criteria");
		}
		return agreements;
	}

	public List<Notice> getNotices(NoticeCriteria noticeCriteria) {
		return noticeRepository.getNotices(noticeCriteria);
	}

	private NoticeResponse getNoticeResponse(List<Notice> notices) {
		NoticeResponse noticeResponse = new NoticeResponse();
		noticeResponse.setNotices(notices);
		return noticeResponse;
	}

	private AgreementCriteria getAgreementSearchCriteria(Notice notice) {
		return AgreementCriteria.builder().tenantId(notice.getTenantId())
				.acknowledgementNumber(notice.getAcknowledgementNumber()).agreementNumber(notice.getAgreementNumber())
				.status(notice.getStatus()).build();
	}
}
