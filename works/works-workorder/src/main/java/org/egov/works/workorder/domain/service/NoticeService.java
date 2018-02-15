package org.egov.works.workorder.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.config.PropertiesManager;
import org.egov.works.workorder.domain.repository.NoticeRepository;
import org.egov.works.workorder.domain.repository.builder.IdGenerationRepository;
import org.egov.works.workorder.domain.validator.NoticeValidator;
import org.egov.works.workorder.utils.WorkOrderUtils;
import org.egov.works.workorder.web.contract.DocumentDetail;
import org.egov.works.workorder.web.contract.Notice;
import org.egov.works.workorder.web.contract.NoticeDetail;
import org.egov.works.workorder.web.contract.NoticeRequest;
import org.egov.works.workorder.web.contract.NoticeResponse;
import org.egov.works.workorder.web.contract.NoticeSearchContract;
import org.egov.works.workorder.web.contract.NoticeStatus;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

	@Autowired
	private WorkOrderUtils workOrderUtils;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private NoticeValidator noticeValidator;

	@Autowired
	private IdGenerationRepository idGenerationRepository;

	@Autowired
	private NoticeRepository noticeRepository;

	public NoticeResponse create(final NoticeRequest noticeRequest) {
		noticeValidator.validateNotice(noticeRequest, Boolean.FALSE);
		String departmentCode;
		for (Notice notice : noticeRequest.getNotices()) {
			notice.setId(commonUtils.getUUID());
			notice.setAuditDetails(workOrderUtils.setAuditDetails(noticeRequest.getRequestInfo(), false));
			for (NoticeDetail noticeDetail : notice.getNoticeDetails()) {
				noticeDetail.setId(commonUtils.getUUID());
				noticeDetail.setNotice(notice.getId());
				noticeDetail.setAuditDetails(workOrderUtils.setAuditDetails(noticeRequest.getRequestInfo(), false));
			}

			if (!notice.getLetterOfAcceptance().getSpillOverFlag()) {
				departmentCode = noticeValidator.getLetterOfAcceptanceResponse(noticeRequest.getRequestInfo(), notice)
						.getLetterOfAcceptances().get(0).getLetterOfAcceptanceEstimates().get(0).getDetailedEstimate()
						.getDepartment().getCode();
				String noticeNumber = idGenerationRepository.generateNoticeNumber(notice.getTenantId(),
						noticeRequest.getRequestInfo());

				notice.setNoticeNumber(workOrderUtils.getCityCode(notice.getTenantId(), noticeRequest.getRequestInfo())
						+ "/" + propertiesManager.getNoticeNumberPrefix() + "/" + departmentCode + noticeNumber);
			}
			if (notice.getDocumentDetails() != null)
				for (final DocumentDetail documentDetail : notice.getDocumentDetails()) {
					documentDetail.setId(commonUtils.getUUID());
					documentDetail.setObjectId(notice.getNoticeNumber());
					documentDetail.setObjectType(CommonConstants.NOTICE);
					documentDetail
							.setAuditDetails(workOrderUtils.setAuditDetails(noticeRequest.getRequestInfo(), false));
				}

		}
		kafkaTemplate.send(propertiesManager.getWorksNoticeCreateTopic(), noticeRequest);
		NoticeResponse noticeResponse = new NoticeResponse();
		noticeResponse.setNotices(noticeRequest.getNotices());
		noticeResponse.setResponseInfo(workOrderUtils.getResponseInfo(noticeRequest.getRequestInfo()));
		return noticeResponse;
	}

	public NoticeResponse update(final NoticeRequest noticeRequest) {
		noticeValidator.validateNotice(noticeRequest, Boolean.FALSE);
		for (Notice notice : noticeRequest.getNotices()) {
			if (notice.getId() == null)
				notice.setId(commonUtils.getUUID());
			notice.setAuditDetails(workOrderUtils.setAuditDetails(noticeRequest.getRequestInfo(), true));
			for (NoticeDetail noticeDetail : notice.getNoticeDetails()) {
				if (noticeDetail.getId().isEmpty())
					noticeDetail.setId(commonUtils.getUUID());
				noticeDetail.setNotice(notice.getId());
				noticeDetail.setAuditDetails(workOrderUtils.setAuditDetails(noticeRequest.getRequestInfo(), true));
			}
			if (notice.getDocumentDetails() != null)
				for (final DocumentDetail documentDetail : notice.getDocumentDetails()) {
					documentDetail.setId(commonUtils.getUUID());
					documentDetail.setObjectId(notice.getNoticeNumber());
					documentDetail.setObjectType(CommonConstants.NOTICE);
					documentDetail
							.setAuditDetails(workOrderUtils.setAuditDetails(noticeRequest.getRequestInfo(), false));
				}
//			updateStatus(notice, false);
		}
		kafkaTemplate.send(propertiesManager.getWorksNoticeCreateTopic(), noticeRequest);
		NoticeResponse noticeResponse = new NoticeResponse();
		noticeResponse.setNotices(noticeRequest.getNotices());
		noticeResponse.setResponseInfo(workOrderUtils.getResponseInfo(noticeRequest.getRequestInfo()));
		return noticeResponse;
	}

	public NoticeResponse search(final NoticeSearchContract noticeSearchContract, final RequestInfo requestInfo) {
		NoticeResponse noticeResponse = new NoticeResponse();
		noticeResponse.setNotices(noticeRepository.search(noticeSearchContract, requestInfo));
		return noticeResponse;
	}
	
	private void updateStatus(Notice notice, boolean isNew) {
		String action = notice.getWorkFlowDetails().getAction();
		if (isNew) {
			notice.setStatus(NoticeStatus.CREATED);
		} else {
			if (action.equalsIgnoreCase(Constants.SUBMIT)
					&& notice.getStatus().equals(NoticeStatus.CREATED))
				notice.setStatus(NoticeStatus.APPROVED);
			else if (action.equalsIgnoreCase(Constants.REJECT)
					&& !(notice.getStatus().equals(NoticeStatus.APPROVED)
							|| notice.getStatus().equals(NoticeStatus.CANCELLED)))
				notice.setStatus(NoticeStatus.REJECTED);
			else if (action.equalsIgnoreCase(Constants.CANCEL)
					&& notice.getStatus().equals(NoticeStatus.REJECTED))
				notice.setStatus(NoticeStatus.CANCELLED);
			else if (action.equalsIgnoreCase(Constants.FORWARD)
					&& notice.getStatus().equals(NoticeStatus.REJECTED))
				notice.setStatus(NoticeStatus.RESUBMITTED);
		}
	}
}
