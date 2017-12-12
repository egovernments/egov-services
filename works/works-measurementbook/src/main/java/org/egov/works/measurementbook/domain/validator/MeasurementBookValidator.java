package org.egov.works.measurementbook.domain.validator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.works.measurementbook.config.Constants;
import org.egov.works.measurementbook.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.measurementbook.domain.repository.MeasurementBookRepository;
import org.egov.works.measurementbook.domain.repository.WorkOrderRepository;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptance;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceEstimate;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceResponse;
import org.egov.works.measurementbook.web.contract.MeasurementBook;
import org.egov.works.measurementbook.web.contract.MeasurementBookDetail;
import org.egov.works.measurementbook.web.contract.MeasurementBookRequest;
import org.egov.works.measurementbook.web.contract.MeasurementBookSearchContract;
import org.egov.works.measurementbook.web.contract.MeasurementBookStatus;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.egov.works.measurementbook.web.contract.WorkOrder;
import org.egov.works.measurementbook.web.contract.WorkOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasurementBookValidator {

	@Autowired
	private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

	@Autowired
	private WorkOrderRepository workOrderRepository;
	
	@Autowired
	private MeasurementBookRepository measurementBookRepository;

	public void validateMB(MeasurementBookRequest measurementBookRequest, Boolean isNew) {
		final RequestInfo requestInfo = measurementBookRequest.getRequestInfo();
		Map<String, String> messages = new HashMap<>();
		for (MeasurementBook measurementBook : measurementBookRequest.getMeasurementBooks()) {
			LetterOfAcceptanceResponse letterOfAcceptanceResponse = letterOfAcceptanceRepository.searchLOAById(
					Arrays.asList(measurementBook.getLetterOfAcceptanceEstimate().getLetterOfAcceptance()),
					measurementBook.getTenantId(), requestInfo);
			WorkOrderResponse workOrderResponse = workOrderRepository.searchWorkOrder(
					Arrays.asList(measurementBook.getLetterOfAcceptanceEstimate().getLetterOfAcceptance()),
					measurementBook.getTenantId(), requestInfo);
			if (letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty()
					&& workOrderResponse.getWorkOrders().isEmpty())
				messages.put(Constants.KEY_MB_LOA_WO_DOES_NOT_EXIST, Constants.MSG_MB_LOA_WO_DOES_NOT_EXIST);
			if (!letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty() && isNew)
				validateInWorkflow(measurementBook, letterOfAcceptanceResponse.getLetterOfAcceptances().get(0),
						messages, requestInfo);
			vallidateMBDate(measurementBook, letterOfAcceptanceResponse.getLetterOfAcceptances().get(0),
					workOrderResponse.getWorkOrders().get(0), messages, isNew);
			if (measurementBook.getFromPageNo() > measurementBook.getToPageNo())
				messages.put(Constants.KEY_MB_FROM_TO_PAGE_NUMBER, Constants.MSG_MB_FROM_TO_PAGE_NUMBER);
			if (measurementBook.getMbAmount().compareTo(BigDecimal.ZERO) <= 0)
				messages.put(Constants.KEY_MB_AMOUNT_LESS_THAN_ZERO, Constants.MSG_MB_AMOUNT_LESS_THAN_ZERO);
			validateMBDetails(measurementBook, messages, isNew);
			Boolean loaEstimateExists = false;
			for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptanceResponse.getLetterOfAcceptances()) {
				for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance.getLetterOfAcceptanceEstimates())
					if (letterOfAcceptanceEstimate.getId().equalsIgnoreCase(measurementBook.getLetterOfAcceptanceEstimate().getId())) {
						loaEstimateExists = true;
						break;
					}
			}
			if (!loaEstimateExists)
				messages.put(Constants.KEY_MB_LOA_ESTIMATE_NOT_EXISTS, Constants.MSG_MB_LOA_ESTIMATE_NOT_EXISTS);
			if (measurementBook.getIsLegacyMB() && !letterOfAcceptanceResponse.getLetterOfAcceptances().get(0).getSpillOverFlag())
				messages.put(Constants.KEY_MB_SPILLOVER_IF_LEGACY, Constants.MSG_MB_SPILLOVER_IF_LEGACY);
		}
	}

	private void validateMBDetails(MeasurementBook measurementBook, Map<String, String> messages, Boolean isNew) {
		for (MeasurementBookDetail detail : measurementBook.getMeasurementBookDetails()) {
			if (detail.getLoaActivity() == null)
				messages.put(Constants.KEY_MB_DETAILS_LOA_ACTIVITY, Constants.MSG_MB_DETAILS_LOA_ACTIVITY);
			if (detail.getQuantity() <= 0)
				messages.put(Constants.KEY_MB_DETAILS_QUANTITY_ZERO, Constants.MSG_MB_DETAILS_QUANTITY_ZERO);
			if (detail.getRate() <= 0)
				messages.put(Constants.KEY_MB_DETAILS_RATE_ZERO, Constants.MSG_MB_DETAILS_RATE_ZERO);
			if (detail.getAmount().compareTo(BigDecimal.ZERO) <= 0)
				messages.put(Constants.KEY_MB_DETAILS_AMOUNT_ZERO, Constants.MSG_MB_DETAILS_AMOUNT_ZERO);
			
			if (detail.getPartRate() != null && detail.getReducedRate() != null)
				messages.put(Constants.KEY_MB_DETAILS_PART_REDUCED_RATE,
						Constants.MSG_MB_DETAILS_PART_REDUCED_RATE);
		}
		if (isNew) {
			if (measurementBook.getMeasurementBookDetails().isEmpty() && measurementBook.getLumpSumMBDetails().isEmpty())
				messages.put(Constants.KEY_MB_DETAILS_MANDATORY, Constants.MSG_MB_DETAILS_MANDATORY);
		} else {
			if (measurementBook.getMeasurementBookDetails().isEmpty() && measurementBook.getLumpSumMBDetails().isEmpty())
				messages.put(Constants.KEY_MB_DETAILS_MANDATORY, Constants.MSG_MB_DETAILS_MANDATORY);
			for (MeasurementBookDetail detail : measurementBook.getLumpSumMBDetails()) {
				if (detail.getLoaActivity() == null)
					messages.put(Constants.KEY_MB_DETAILS_LOA_ACTIVITY, Constants.MSG_MB_DETAILS_LOA_ACTIVITY);
				if (detail.getQuantity() <= 0)
					messages.put(Constants.KEY_MB_DETAILS_QUANTITY_ZERO, Constants.MSG_MB_DETAILS_QUANTITY_ZERO);
				if (detail.getRate() <= 0)
					messages.put(Constants.KEY_MB_DETAILS_RATE_ZERO, Constants.MSG_MB_DETAILS_RATE_ZERO);
				if (detail.getAmount().compareTo(BigDecimal.ZERO) <= 0)
					messages.put(Constants.KEY_MB_DETAILS_AMOUNT_ZERO, Constants.MSG_MB_DETAILS_AMOUNT_ZERO);
				
				if (detail.getPartRate() != null && detail.getReducedRate() != null)
					messages.put(Constants.KEY_MB_DETAILS_PART_REDUCED_RATE,
							Constants.MSG_MB_DETAILS_PART_REDUCED_RATE);
			}
		}
	}

	private void vallidateMBDate(MeasurementBook measurementBook, LetterOfAcceptance letterOfAcceptance,
			WorkOrder workOrder, Map<String, String> messages, Boolean isNew) {
		if (!measurementBook.getIsLegacyMB() && measurementBook.getMbDate() != null && measurementBook.getMbDate() > new Date().getTime())
			messages.put(Constants.KEY_INVALID_MB_DATE, Constants.MSG_INVALID_MB_DATE);
		if (measurementBook.getMbDate() != null && (measurementBook.getMbDate() < letterOfAcceptance.getLoaDate()
				|| measurementBook.getMbDate() < workOrder.getWorkOrderDate()))
			messages.put(Constants.KEY_MB_DATE_LOA_WO, Constants.MSG_MB_DATE_LOA_WO);
		if (measurementBook.getMbIssuedDate() != null && measurementBook.getMbIssuedDate() > measurementBook.getMbDate())
			messages.put(Constants.KEY_MB_DATE_ISSUE_DATE, Constants.MSG_MB_DATE_ISSUE_DATE);
	}

	private void validateInWorkflow(MeasurementBook measurementBook, LetterOfAcceptance letterOfAcceptance,
			Map<String, String> messages, RequestInfo requestInfo) {
		MeasurementBookSearchContract measurementBookSearchContract = new MeasurementBookSearchContract();
		measurementBookSearchContract.setTenantId(measurementBook.getTenantId());
		measurementBookSearchContract.setLoaNumbers(Arrays.asList(letterOfAcceptance.getLoaNumber()));
		List<MeasurementBook> measurementBooks = measurementBookRepository
				.searchMeasurementBooks(measurementBookSearchContract, requestInfo);
		for (MeasurementBook book : measurementBooks) {
			if (book.getStatus() != null && !(book.getStatus().equals(MeasurementBookStatus.APPROVED)
					|| book.getStatus().equals(MeasurementBookStatus.CANCELLED)))
					messages.put(Constants.KEY_MB_IN_WORKFLOW, Constants.MSG_MB_IN_WORKFLOW);
			if (measurementBook.getMbDate() < book.getMbDate())
				messages.put(Constants.MSG_MB_DATE_PREVIOUS_DATE, Constants.MSG_MB_DATE_PREVIOUS_DATE);
		}
	}

	public void validateMasterData(MeasurementBook measurementBook, RequestInfo requestInfo,
			Map<String, String> messages, Boolean isNew) {

	}
}
