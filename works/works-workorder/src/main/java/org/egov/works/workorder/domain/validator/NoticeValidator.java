package org.egov.works.workorder.domain.validator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.repository.WorksMastersRepository;
import org.egov.works.workorder.domain.service.LetterOfAcceptanceService;
import org.egov.works.workorder.domain.service.WorkOrderService;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeValidator {

	@Autowired
	private WorkOrderService workOrderService;

	@Autowired
	private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private WorksMastersRepository worksMastersRepository;

	public void validateNotice(final NoticeRequest noticeRequest, Boolean isUpdate) {
		HashMap<String, String> messages = new HashMap<>();
		for (Notice notice : noticeRequest.getNotices()) {
			WorkOrderResponse workOrderResponse = getWorkOrders(noticeRequest.getRequestInfo(),
					notice.getLetterOfAcceptance().getId(), notice.getTenantId());
			if (workOrderResponse.getWorkOrders() != null && workOrderResponse.getWorkOrders().isEmpty())
				messages.put(Constants.KEY_NOTICE_WO_NOT_APPROVED, Constants.MESSAGE_NOTICE_WO_NOT_APPROVED);

            //TODO : FIX remarks master topic
            //validateRemarks(notice, messages, noticeRequest.getRequestInfo());
		}
		if (!messages.isEmpty())
			throw new CustomException(messages);
	}

    private void validateRemarks(Notice notice, HashMap<String, String> messages, RequestInfo requestInfo) {
        for(NoticeDetail noticeDetail : notice.getNoticeDetails()) {
            if(StringUtils.isNotBlank(noticeDetail.getRemarks())) {
                List<Remarks> remarks = worksMastersRepository.SearchRemarks(notice.getTenantId(),noticeDetail.getRemarks(), requestInfo);
                if(remarks != null && remarks.isEmpty())
                    messages.put(Constants.KEY_NOTICE_REMARKS_INVALID,
                            Constants.MESSAGE_NOTICE_REMARKS_INVALID);

            }
        }
    }

	public WorkOrderResponse getWorkOrders(RequestInfo requestInfo, String letterOfAcceptanceId, String tenantId) {
		WorkOrderSearchContract workOrderSearchContract = new WorkOrderSearchContract();
		workOrderSearchContract.setLetterOfAcceptances(Arrays.asList(letterOfAcceptanceId));
		workOrderSearchContract.setTenantId(tenantId);
		workOrderSearchContract.setStatuses(Arrays.asList(CommonConstants.STATUS_APPROVED));
		return workOrderService.search(workOrderSearchContract, requestInfo);
	}

	public LetterOfAcceptanceResponse getLetterOfAcceptanceResponse(RequestInfo requestInfo, Notice notice) {
		LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria = new LetterOfAcceptanceSearchContract();
		if (notice.getLetterOfAcceptance() != null && notice.getLetterOfAcceptance().getLoaNumber() != null)
			letterOfAcceptanceSearchCriteria
					.setLoaNumbers(Arrays.asList(notice.getLetterOfAcceptance().getLoaNumber()));
		letterOfAcceptanceSearchCriteria.setTenantId(notice.getTenantId());
		letterOfAcceptanceSearchCriteria.setIds(Arrays.asList(notice.getLetterOfAcceptance().getId()));
		letterOfAcceptanceSearchCriteria.setStatuses(Arrays.asList(CommonConstants.STATUS_APPROVED));
		return letterOfAcceptanceService.search(letterOfAcceptanceSearchCriteria, requestInfo);
	}
}
