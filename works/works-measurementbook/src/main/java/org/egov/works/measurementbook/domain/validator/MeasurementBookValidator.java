package org.egov.works.measurementbook.domain.validator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.works.measurementbook.config.Constants;
import org.egov.works.measurementbook.domain.repository.EstimateRepository;
import org.egov.works.measurementbook.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.measurementbook.domain.repository.MeasurementBookRepository;
import org.egov.works.measurementbook.domain.repository.OfflineStatusRepository;
import org.egov.works.measurementbook.domain.repository.WorkOrderRepository;
import org.egov.works.measurementbook.web.contract.DetailedEstimate;
import org.egov.works.measurementbook.web.contract.EstimateActivity;
import org.egov.works.measurementbook.web.contract.EstimateMeasurementSheet;
import org.egov.works.measurementbook.web.contract.LOAActivity;
import org.egov.works.measurementbook.web.contract.LOAMeasurementSheet;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptance;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceEstimate;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceResponse;
import org.egov.works.measurementbook.web.contract.MBMeasurementSheet;
import org.egov.works.measurementbook.web.contract.MeasurementBook;
import org.egov.works.measurementbook.web.contract.MeasurementBookDetail;
import org.egov.works.measurementbook.web.contract.MeasurementBookRequest;
import org.egov.works.measurementbook.web.contract.MeasurementBookSearchContract;
import org.egov.works.measurementbook.web.contract.MeasurementBookStatus;
import org.egov.works.measurementbook.web.contract.OfflineStatusResponse;
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

	@Autowired
	private OfflineStatusRepository offlineStatusRepository;

	@Autowired
	private EstimateRepository estimateRepository;

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
			if (letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty()) {
			    messages.put(Constants.KEY_MB_LOA_DOES_NOT_EXIST, Constants.MSG_MB_LOA_DOES_NOT_EXIST);
			}
			if(workOrderResponse.getWorkOrders().isEmpty()) {
			    messages.put(Constants.KEY_MB_WO_DOES_NOT_EXIST, Constants.MSG_MB_WO_DOES_NOT_EXIST);
			}
				
			
			if (!messages.isEmpty())
		                throw new CustomException(messages);
			
			if (!letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty() && isNew)
				validateInWorkflow(measurementBook, letterOfAcceptanceResponse.getLetterOfAcceptances().get(0),
						messages, requestInfo);
			vallidateMBDate(measurementBook, letterOfAcceptanceResponse.getLetterOfAcceptances().get(0),
					workOrderResponse.getWorkOrders().get(0), messages, isNew);
			vallidateMBAmount(measurementBook, letterOfAcceptanceResponse.getLetterOfAcceptances().get(0),
					workOrderResponse.getWorkOrders().get(0), messages);
			if (measurementBook.getFromPageNo() > measurementBook.getToPageNo())
				messages.put(Constants.KEY_MB_FROM_TO_PAGE_NUMBER, Constants.MSG_MB_FROM_TO_PAGE_NUMBER);
			validateMBDetails(measurementBook, letterOfAcceptanceResponse.getLetterOfAcceptances().get(0), messages,
					isNew, requestInfo);
			Boolean loaEstimateExists = false;
			for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptanceResponse.getLetterOfAcceptances()) {
				for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance
						.getLetterOfAcceptanceEstimates())
					if (letterOfAcceptanceEstimate.getId()
							.equalsIgnoreCase(measurementBook.getLetterOfAcceptanceEstimate().getId())) {
						loaEstimateExists = true;
						break;
					}
			}
			if (!loaEstimateExists)
				messages.put(Constants.KEY_MB_LOA_ESTIMATE_NOT_EXISTS, Constants.MSG_MB_LOA_ESTIMATE_NOT_EXISTS);
			if (measurementBook.getIsLegacyMB()
					&& !letterOfAcceptanceResponse.getLetterOfAcceptances().get(0).getSpillOverFlag())
				messages.put(Constants.KEY_MB_SPILLOVER_IF_LEGACY, Constants.MSG_MB_SPILLOVER_IF_LEGACY);

			if (!letterOfAcceptanceResponse.getLetterOfAcceptances().get(0).getSpillOverFlag()) {
				OfflineStatusResponse offlineStatusResponse = offlineStatusRepository.searchOfflineStatus(
						workOrderResponse.getWorkOrders().get(0).getWorkOrderNumber(), measurementBook.getTenantId(),
						requestInfo);
				if (offlineStatusResponse.getOfflineStatuses().isEmpty())
					messages.put(Constants.KEY_MB_WO_WORK_COMMENCED, Constants.KEY_MB_WO_WORK_COMMENCED);
				else {
					if (measurementBook.getMbDate() < offlineStatusResponse.getOfflineStatuses().get(0).getStatusDate())
						messages.put(Constants.KEY_MB_DATE_WORK_COMMENCED_DATE,
								Constants.MSG_MB_DATE_WORK_COMMENCED_DATE);
				}
			}
		}
		if (!messages.isEmpty())
	                throw new CustomException(messages);
	}

	private void vallidateMBAmount(MeasurementBook measurementBook, LetterOfAcceptance letterOfAcceptance,
			WorkOrder workOrder, Map<String, String> messages) {
		Double tenderPercentage = letterOfAcceptance.getTenderFinalizedPercentage();
		Double amount = 0D;
		if (measurementBook.getMbAmount().compareTo(BigDecimal.ZERO) <= 0)
			messages.put(Constants.KEY_MB_AMOUNT_LESS_THAN_ZERO, Constants.MSG_MB_AMOUNT_LESS_THAN_ZERO);
		for (MeasurementBookDetail detail : measurementBook.getMeasurementBookDetails())
			amount += detail.getAmount().doubleValue();
		amount = amount + (amount * tenderPercentage / 100);

		if (measurementBook.getMbAmount().compareTo(BigDecimal.valueOf(amount)) != 0)
			messages.put(Constants.KEY_MB_AMOUNT_SUM_DETAILS, Constants.MSG_MB_AMOUNT_SUM_DETAILS);
	}

	private void validateMBDetails(MeasurementBook measurementBook, LetterOfAcceptance letterOfAcceptance,
			Map<String, String> messages, Boolean isNew, RequestInfo requestInfo) {
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
				messages.put(Constants.KEY_MB_DETAILS_PART_REDUCED_RATE, Constants.MSG_MB_DETAILS_PART_REDUCED_RATE);

			if (detail.getMeasurementSheets() != null)
				validateMeasurementSheet(detail, letterOfAcceptance, messages, requestInfo);

		}
		for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance
				.getLetterOfAcceptanceEstimates()) {
			for (LOAActivity loaActivity : letterOfAcceptanceEstimate.getLoaActivities()) {
				if (loaActivity.getLoaMeasurements() != null && !loaActivity.getLoaMeasurements().isEmpty()) {
					for (MeasurementBookDetail detail : measurementBook.getMeasurementBookDetails()) {
						if (detail.getLoaActivity().getId() != null
								&& detail.getLoaActivity().getId().equals(loaActivity.getId())
								&& !loaActivity.getLoaMeasurements().isEmpty()
								&& detail.getMeasurementSheets().isEmpty())
							messages.put(Constants.KEY_MB_MEASUREMENTS_MANDATORY_IF_LOA_MEASUREMENTS,
									Constants.MSG_MB_MEASUREMENTS_MANDATORY_IF_LOA_MEASUREMENTS);
					}
				}
			}
		}
		if (isNew) {
			if (measurementBook.getWorkFlowDetails() != null && !measurementBook.getWorkFlowDetails().getAction().equalsIgnoreCase(Constants.SAVE)
					&& measurementBook.getMeasurementBookDetails().isEmpty()
					&& measurementBook.getLumpSumMBDetails().isEmpty())
				messages.put(Constants.KEY_MB_DETAILS_MANDATORY, Constants.MSG_MB_DETAILS_MANDATORY);
		} else {
			if (measurementBook.getMeasurementBookDetails() != null && measurementBook.getMeasurementBookDetails().isEmpty()
					&& measurementBook.getLumpSumMBDetails() != null && measurementBook.getLumpSumMBDetails().isEmpty())
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

	private void validateMeasurementSheet(MeasurementBookDetail detail, LetterOfAcceptance letterOfAcceptance,
			Map<String, String> messages, RequestInfo requestInfo) {
		List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByIds(
				Arrays.asList(letterOfAcceptance.getLetterOfAcceptanceEstimates().get(0).getDetailedEstimate().getId()),
				detail.getTenantId(), requestInfo);
		Double mbSheetQuantity = 0D;
        if(detail.getMeasurementSheets() != null) {
            for (MBMeasurementSheet sheet : detail.getMeasurementSheets()) {
                String estimateSheetId = null;
                String identifier = null;
                for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance.getLetterOfAcceptanceEstimates()) {
                    for (LOAActivity loaActivity : letterOfAcceptanceEstimate.getLoaActivities()) {
                        if (loaActivity.getLoaMeasurements() != null) {
                            for (LOAMeasurementSheet loaMeasurementSheet : loaActivity.getLoaMeasurements()) {
                                if (loaMeasurementSheet.getId().equals(sheet.getLoaMeasurementSheet().getId()))
                                    estimateSheetId = loaMeasurementSheet.getEstimateMeasurementSheet();
                                else
                                    messages.put(Constants.KEY_MB_MEASUREMENTS_LOA_NOT_VALID,
                                            Constants.MSG_MB_MEASUREMENTS_LOA_NOT_VALID);
                            }
                        }
                    }
                }
                for (EstimateActivity estimateActivity : detailedEstimates.get(0).getEstimateActivities()) {
                    if (estimateActivity.getEstimateMeasurementSheets() != null) {
                        for (EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity.getEstimateMeasurementSheets()) {
                            if (estimateMeasurementSheet.getId().equals(estimateSheetId))
                                identifier = estimateMeasurementSheet.getIdentifier();
                        }
                    }
                }
                if ("A".equalsIgnoreCase(identifier))
                    mbSheetQuantity += sheet.getQuantity().doubleValue();
                else
                    mbSheetQuantity -= sheet.getQuantity().doubleValue();
            }
        }
		if (!mbSheetQuantity.equals(detail.getQuantity()))
			messages.put(Constants.KEY_MB_MEASUREMENTS_QUANTITY_NOT_EQUAL_DETAIL,
					Constants.MSG_MB_MEASUREMENTS_QUANTITY_NOT_EQUAL_DETAIL);
	}

	private void vallidateMBDate(MeasurementBook measurementBook, LetterOfAcceptance letterOfAcceptance,
			WorkOrder workOrder, Map<String, String> messages, Boolean isNew) {
		if (!measurementBook.getIsLegacyMB() && measurementBook.getMbDate() != null
				&& measurementBook.getMbDate() > new Date().getTime())
			messages.put(Constants.KEY_INVALID_MB_DATE, Constants.MSG_INVALID_MB_DATE);
		if (measurementBook.getMbDate() != null && (measurementBook.getMbDate() < letterOfAcceptance.getLoaDate()
				|| measurementBook.getMbDate() < workOrder.getWorkOrderDate()))
			messages.put(Constants.KEY_MB_DATE_LOA_WO, Constants.MSG_MB_DATE_LOA_WO);
		if (measurementBook.getMbIssuedDate() != null
				&& measurementBook.getMbIssuedDate() > measurementBook.getMbDate())
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
