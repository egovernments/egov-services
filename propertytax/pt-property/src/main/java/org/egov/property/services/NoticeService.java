package org.egov.property.services;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Date;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.NoticeRequest;
import org.egov.models.NoticeSearchCriteria;
import org.egov.property.repository.NoticeMessageQueueRepository;
import org.egov.property.repository.NoticeRepository;
import org.egov.property.utility.NoticeValidator;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

	private NoticeRepository noticeRepository;

	private NoticeMessageQueueRepository noticeMessageQueueRepository;

	private NoticeValidator noticeValidator;

	public NoticeService(NoticeRepository noticeRepository, NoticeMessageQueueRepository noticeMessageQueueRepository,
			NoticeValidator noticeValidator) {
		this.noticeRepository = noticeRepository;
		this.noticeMessageQueueRepository = noticeMessageQueueRepository;
		this.noticeValidator = noticeValidator;
	}

	public void pushToQueue(NoticeRequest noticeRequest) {
		updateAuditDetailsForCreate(noticeRequest);
		noticeValidator.validateNotice(noticeRequest.getNotice());
		noticeMessageQueueRepository.save(noticeRequest);
	}

	public void create(NoticeRequest noticeRequest) {
		noticeRepository.save(noticeRequest.getNotice());
	}

	public List search(NoticeSearchCriteria searchCriteria) {
		return noticeRepository.search(searchCriteria);
	}

	private void updateAuditDetailsForCreate(NoticeRequest noticeRequest) {

		if (isEmpty(noticeRequest.getNotice().getAuditDetails())) {
			AuditDetails auditDetails = AuditDetails.builder()
					.createdBy(noticeRequest.getRequestInfo().getUserInfo().getId().toString())
					.createdTime(new Date().getTime()).build();

			noticeRequest.getNotice().setAuditDetails(auditDetails);
		} else {
			AuditDetails auditDetails = noticeRequest.getNotice().getAuditDetails();
			auditDetails.setCreatedBy(noticeRequest.getRequestInfo().getUserInfo().getId().toString());
			auditDetails.setCreatedTime(new Date().getTime());
		}
	}
}
