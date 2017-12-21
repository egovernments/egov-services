package org.egov.works.workorder.domain.validator;

import java.util.Arrays;
import java.util.HashMap;

import org.egov.tracer.model.CustomException;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.service.LetterOfAcceptanceService;
import org.egov.works.workorder.domain.service.WorkOrderService;
import org.egov.works.workorder.web.contract.LOAStatus;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceResponse;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceSearchContract;
import org.egov.works.workorder.web.contract.Notice;
import org.egov.works.workorder.web.contract.NoticeRequest;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.egov.works.workorder.web.contract.WorkOrderResponse;
import org.egov.works.workorder.web.contract.WorkOrderSearchContract;
import org.egov.works.workorder.web.contract.WorkOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeValidator {

	@Autowired
	private WorkOrderService workOrderService;

	@Autowired
	private LetterOfAcceptanceService letterOfAcceptanceService;

	public void validateNotice(final NoticeRequest noticeRequest, Boolean isUpdate) {
		HashMap<String, String> messages = new HashMap<>();
		for (Notice notice : noticeRequest.getNotices()) {
			WorkOrderResponse workOrderResponse = getWorkOrders(noticeRequest.getRequestInfo(),
					notice.getLetterOfAcceptance().getId(), notice.getTenantId());
			if (workOrderResponse.getWorkOrders() != null && workOrderResponse.getWorkOrders().isEmpty())
				messages.put(Constants.KEY_NOTICE_WO_NOT_APPROVED, Constants.MESSAGE_NOTICE_WO_NOT_APPROVED);
		}
		if (!messages.isEmpty())
			throw new CustomException(messages);
	}

	public WorkOrderResponse getWorkOrders(RequestInfo requestInfo, String letterOfAcceptanceId, String tenantId) {
		WorkOrderSearchContract workOrderSearchContract = new WorkOrderSearchContract();
		workOrderSearchContract.setLetterOfAcceptances(Arrays.asList(letterOfAcceptanceId));
		workOrderSearchContract.setTenantId(tenantId);
		workOrderSearchContract.setStatuses(Arrays.asList(WorkOrderStatus.APPROVED.toString()));
		return workOrderService.search(workOrderSearchContract, requestInfo);
	}

	public LetterOfAcceptanceResponse getLetterOfAcceptanceResponse(RequestInfo requestInfo, Notice notice) {
		LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria = new LetterOfAcceptanceSearchContract();
		if (notice.getLetterOfAcceptance() != null && notice.getLetterOfAcceptance().getLoaNumber() != null)
			letterOfAcceptanceSearchCriteria
					.setLoaNumbers(Arrays.asList(notice.getLetterOfAcceptance().getLoaNumber()));
		letterOfAcceptanceSearchCriteria.setTenantId(notice.getTenantId());
		letterOfAcceptanceSearchCriteria.setIds(Arrays.asList(notice.getLetterOfAcceptance().getId()));
		letterOfAcceptanceSearchCriteria.setStatuses(Arrays.asList(LOAStatus.APPROVED.toString()));
		return letterOfAcceptanceService.search(letterOfAcceptanceSearchCriteria, requestInfo);
	}
}
