package org.egov.works.measurementbook.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.measurementbook.config.Constants;
import org.egov.works.measurementbook.config.PropertiesManager;
import org.egov.works.measurementbook.domain.repository.EstimateRepository;
import org.egov.works.measurementbook.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.measurementbook.domain.repository.MeasurementBookRepository;
import org.egov.works.measurementbook.domain.validator.MeasurementBookValidator;
import org.egov.works.measurementbook.utils.MeasurementBookUtils;
import org.egov.works.measurementbook.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.minidev.json.JSONArray;

/**
 * @author manoj
 *
 */
@Service
@Transactional(readOnly = true)
public class MeasurementBookService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	// @Autowired
	// private CommonUtils commonUtils;

	@Autowired
	private MeasurementBookUtils measurementBookUtils;

	@Autowired
	private MeasurementBookRepository measurementBookRepository;

	@Autowired
	private EstimateRepository estimateRepository;

	@Autowired
	private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

	@Autowired
	private MeasurementBookValidator measurementBookValidator;

	public MeasurementBookResponse create(MeasurementBookRequest measurementBookRequest) {
		measurementBookValidator.validateMB(measurementBookRequest, true);
        List<MeasurementBook> measurementBooks = new ArrayList<>();
		Boolean isWorkflowReq = isConfigRequired(CommonConstants.WORKFLOW_REQUIRED_APPCONFIG,
				measurementBookRequest.getRequestInfo(),
				measurementBookRequest.getMeasurementBooks().get(0).getTenantId());
		for (MeasurementBook measurementBook : measurementBookRequest.getMeasurementBooks()) {
			measurementBook.setId(UUID.randomUUID().toString().replace("-", ""));
			for (MeasurementBookDetail measurementBookDetail : measurementBook.getMeasurementBookDetails()) {
				measurementBookDetail.setId(UUID.randomUUID().toString().replace("-", ""));
				if(measurementBookDetail.getMeasurementSheets() != null && !measurementBookDetail.getMeasurementSheets().isEmpty())
				for (MBMeasurementSheet sheet : measurementBookDetail.getMeasurementSheets()) {
					sheet.setId(UUID.randomUUID().toString().replace("-", ""));
					sheet.setAuditDetails(
							measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), false));
				}
				measurementBookDetail.setAuditDetails(
						measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), false));
			}
			

			if(measurementBook.getDocumentDetails() != null && !measurementBook.getDocumentDetails().isEmpty())
			for (DocumentDetail detail : measurementBook.getDocumentDetails()) {
				detail.setId(UUID.randomUUID().toString().replace("-", ""));
				detail.setObjectType("MeasurementBook");
				detail.setAuditDetails(
						measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), false));
			}
			measurementBook.setAuditDetails(
					measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), false));

			if (measurementBook.getLumpSumMBDetails() != null && !measurementBook.getLumpSumMBDetails().isEmpty())
				createUpdateRevisionEstimate(measurementBook, measurementBookRequest.getRequestInfo(), false);

			if (measurementBook.getIsLegacyMB())
				measurementBook.setApprovedDate(measurementBook.getMbDate());
			if (measurementBook.getIsLegacyMB() && isWorkflowReq) {
				updateStatus(measurementBook, measurementBookRequest.getRequestInfo());

			} else {
                List<String> filterNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE,CommonConstants.MODULE_TYPE));
                List<String> filterValuesList = new ArrayList<>(Arrays.asList(Constants.STATUS_APPROVED, Constants.MEASUREMENTBOOK_OBJECT));
                JSONArray dBStatusArray = measurementBookUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filterNamesList,
                        filterValuesList, measurementBook.getTenantId(), measurementBookRequest.getRequestInfo(),
                        CommonConstants.MODULENAME_WORKS);
                if(dBStatusArray != null && !dBStatusArray.isEmpty()) {
                    WorksStatus status = new WorksStatus();
                    status.code(Constants.STATUS_APPROVED);
                    measurementBook.setStatus(status);
                }
            }

            List<LetterOfAcceptance> letterOfAcceptances =
                    letterOfAcceptanceRepository.searchLoaByLoaEstimateId(measurementBook.getTenantId(), measurementBook.getLetterOfAcceptanceEstimate(), measurementBookRequest.getRequestInfo());
            if(letterOfAcceptances != null && letterOfAcceptances.isEmpty())
                measurementBook.setLetterOfAcceptanceEstimate(letterOfAcceptances.get(0).getLetterOfAcceptanceEstimates().get(0));

            if(measurementBook.getStatus() != null && measurementBook.getStatus().equals(CommonConstants.STATUS_CREATED) ||
                    measurementBook.getStatus().equals(CommonConstants.STATUS_APPROVED)) {
                measurementBooks.add(measurementBook);
            }
		}
		kafkaTemplate.send(propertiesManager.getWorksMBCreateUpdateTopic(), measurementBookRequest);
        if(measurementBooks != null && !measurementBooks.isEmpty()) {
            MeasurementBookRequest backUpdateRequest = new MeasurementBookRequest();
            backUpdateRequest.setMeasurementBooks(measurementBooks);
            kafkaTemplate.send(propertiesManager.getWorksLoaBackUpdateOnCreateMB(), backUpdateRequest);
            kafkaTemplate.send(propertiesManager.getWorksLoaBackUpdateOnCreateMBAndNoBill(), backUpdateRequest);
        }
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		measurementBookResponse.setMeasurementBooks(measurementBookRequest.getMeasurementBooks());
		return measurementBookResponse;
	}

    private void updateStatus(MeasurementBook measurementBook, RequestInfo requestInfo) {
        String action = measurementBook.getWorkFlowDetails().getAction();
        List<String> filterNamesList = null;
        List<String> filterValuesList = null;
        WorksStatus status = null;
        if (!action.equalsIgnoreCase(Constants.SAVE)) {
            filterNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE,CommonConstants.MODULE_TYPE));
            filterValuesList = new ArrayList<>(Arrays.asList(Constants.STATUS_CREATED, Constants.MEASUREMENTBOOK_OBJECT));
            JSONArray dBStatusArray = measurementBookUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filterNamesList,
                    filterValuesList, measurementBook.getTenantId(), requestInfo,
                    CommonConstants.MODULENAME_WORKS);
            if(dBStatusArray != null && !dBStatusArray.isEmpty()) {
                status = new WorksStatus();
                status.setCode(Constants.STATUS_CREATED);
            }
        }
        else {
            filterNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE,CommonConstants.MODULE_TYPE));
            filterValuesList = new ArrayList<>(Arrays.asList(Constants.STATUS_SAVED, Constants.MEASUREMENTBOOK_OBJECT));
            JSONArray dBStatusArray = measurementBookUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filterNamesList,
                    filterValuesList, measurementBook.getTenantId(), requestInfo,
                    CommonConstants.MODULENAME_WORKS);
            if(dBStatusArray != null && !dBStatusArray.isEmpty()) {
                status = new WorksStatus();
                status.setCode(Constants.STATUS_SAVED);
            }
        }
        measurementBook.setStatus(status);
    }
    
	/**
	 * @param measurementBook
	 * @param requestInfo
	 * @param isUpdate
	 * 
	 * Create Revision DE and LOA if lumpsum activities present
	 */
	private void createUpdateRevisionEstimate(MeasurementBook measurementBook, RequestInfo requestInfo,
			Boolean isUpdate) {
		DetailedEstimateRequest detailedEstimateRequest = new DetailedEstimateRequest();
		LetterOfAcceptanceRequest letterOfAcceptanceRequest = new LetterOfAcceptanceRequest();
		Boolean deleted = true;
		AuditDetails auditDetails = measurementBookUtils.setAuditDetails(requestInfo, false);
		List<DetailedEstimate> detailedEstimates = new ArrayList<>();
		List<LetterOfAcceptance> letterOfAcceptances = new ArrayList<>();
		DetailedEstimate detailedEstimate = new DetailedEstimate();
		LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
		if (isUpdate)
			detailedEstimate.setId(measurementBook.getLumpSumMBDetails().get(0).getLoaActivity().getEstimateActivity()
					.getDetailedEstimate());
		detailedEstimate.setTenantId(measurementBook.getTenantId());
		detailedEstimate.setParent(measurementBook.getLetterOfAcceptanceEstimate().getDetailedEstimate().getId());
		for (MeasurementBookDetail detail : measurementBook.getLumpSumMBDetails())
			detailedEstimate.addEstimateActivitiesItem(detail.getLoaActivity().getEstimateActivity());
		detailedEstimate.setEstimateDate(new Date().getTime());
		detailedEstimate
				.setDepartment(measurementBook.getLetterOfAcceptanceEstimate().getDetailedEstimate().getDepartment());
		detailedEstimate.setEstimateValue(
				measurementBook.getLetterOfAcceptanceEstimate().getDetailedEstimate().getEstimateValue());
		for (final EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
			estimateActivity.setTenantId(measurementBook.getTenantId());
			estimateActivity.setAuditDetails(auditDetails);
			if (isUpdate && deleted && !estimateActivity.getDeleted())
				deleted = false;
			if (estimateActivity.getEstimateMeasurementSheets() != null) {
				for (final EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity
						.getEstimateMeasurementSheets()) {
					estimateMeasurementSheet.setTenantId(measurementBook.getTenantId());
					estimateMeasurementSheet.setAuditDetails(auditDetails);
				}
			}
		}
		if (isUpdate)
			detailedEstimate.setDeleted(deleted);
		detailedEstimates.add(detailedEstimate);
		detailedEstimateRequest.setDetailedEstimates(detailedEstimates);
		detailedEstimateRequest.setRequestInfo(requestInfo);

		DetailedEstimateResponse detailedEstimateResponse = estimateRepository
				.createUpdateDetailedEstimate(detailedEstimateRequest, isUpdate);

		letterOfAcceptance.setTenantId(measurementBook.getTenantId());
		letterOfAcceptance.setParent(measurementBook.getLetterOfAcceptanceEstimate().getLetterOfAcceptance());
		letterOfAcceptance.setLoaDate(new Date().getTime());
		letterOfAcceptance.setLoaAmount(detailedEstimateResponse.getDetailedEstimates().get(0).getEstimateValue());
        WorksStatus status = new WorksStatus();
        status.setCode(Constants.STATUS_APPROVED);
		letterOfAcceptance.setStatus(status);
		List<LetterOfAcceptanceEstimate> letterOfAcceptanceEstimates = new ArrayList<>();
		LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = new LetterOfAcceptanceEstimate();
		LetterOfAcceptanceResponse letterOfAcceptanceResponse = null;
		if (isUpdate && StringUtils.isNotBlank(measurementBook.getRevisionLOA())) {
			letterOfAcceptance.setId(measurementBook.getRevisionLOA());
			letterOfAcceptanceResponse = letterOfAcceptanceRepository.searchLOAById(
					Arrays.asList(measurementBook.getRevisionLOA()), measurementBook.getTenantId(), requestInfo);
			letterOfAcceptanceEstimate.setId(letterOfAcceptanceResponse.getLetterOfAcceptances().get(0)
					.getLetterOfAcceptanceEstimates().get(0).getId());
		}
		letterOfAcceptanceEstimate.setTenantId(measurementBook.getTenantId());
		letterOfAcceptanceEstimate.setAuditDetails(auditDetails);
		letterOfAcceptanceEstimate.setDetailedEstimate(detailedEstimateResponse.getDetailedEstimates().get(0));
		letterOfAcceptanceEstimate.setLetterOfAcceptance(letterOfAcceptance.getId());
		populateLOAActivities(letterOfAcceptanceEstimate, detailedEstimateResponse.getDetailedEstimates().get(0),
				auditDetails);
		if (isUpdate) {
			letterOfAcceptance.setDeleted(deleted);
			letterOfAcceptanceEstimate.setDeleted(deleted);
		}
		letterOfAcceptanceEstimates.add(letterOfAcceptanceEstimate);
		letterOfAcceptance.setLetterOfAcceptanceEstimates(letterOfAcceptanceEstimates);
		letterOfAcceptances.add(letterOfAcceptance);
		letterOfAcceptanceRequest.setLetterOfAcceptances(letterOfAcceptances);
		letterOfAcceptanceRequest.setRequestInfo(requestInfo);

		letterOfAcceptanceResponse = letterOfAcceptanceRepository.createUpdateLOA(letterOfAcceptanceRequest, isUpdate);
		measurementBook.setRevisionLOA(letterOfAcceptanceResponse.getLetterOfAcceptances().get(0).getId());

		updateLumpsumActivities(measurementBook, letterOfAcceptanceResponse.getLetterOfAcceptances().get(0), isUpdate,
				requestInfo);
	}

	
	/**
	 * @param measurementBook
	 * @param letterOfAcceptance
	 * @param isUpdate
	 * @param requestInfo
	 * 
	 * populate mb details with references after creating revision DE and LOA
	 */
	private void updateLumpsumActivities(MeasurementBook measurementBook, LetterOfAcceptance letterOfAcceptance,
			Boolean isUpdate, RequestInfo requestInfo) {
		if (isUpdate)
			for (MeasurementBookDetail detail : measurementBook.getLumpSumMBDetails()) {
				detail.setAuditDetails(measurementBookUtils.setAuditDetails(requestInfo, true));
				for (MBMeasurementSheet sheet : detail.getMeasurementSheets())
					sheet.setAuditDetails(measurementBookUtils.setAuditDetails(requestInfo, true));
				measurementBook.addMeasurementBookDetailsItem(detail);
			}
		else {
			for (LOAActivity loaActivity : letterOfAcceptance.getLetterOfAcceptanceEstimates().get(0)
					.getLoaActivities()) {
				final EstimateActivity activity = loaActivity.getEstimateActivity();
				final MeasurementBookDetail measurementBookDetail = new MeasurementBookDetail();
				measurementBookDetail.setId(UUID.randomUUID().toString().replace("-", ""));
				measurementBookDetail.setTenantId(activity.getTenantId());
				measurementBookDetail.setAmount(activity.getEstimateRate());
				measurementBookDetail.setLoaActivity(loaActivity);
				measurementBookDetail.setMeasurementBook(measurementBook.getId());
				List<MBMeasurementSheet> measurementSheets = new ArrayList<>();
				for (LOAMeasurementSheet loaMeasurementSheet : loaActivity.getLoaMeasurements()) {
					MBMeasurementSheet sheet = new MBMeasurementSheet();
					sheet.setId(UUID.randomUUID().toString().replace("-", ""));
					sheet.setDepthOrHeight(loaMeasurementSheet.getDepthOrHeight());
					sheet.setLength(loaMeasurementSheet.getLength());
					sheet.setLoaMeasurementSheet(loaMeasurementSheet);
					sheet.setMeasurementBookDetail(measurementBookDetail.getId());
					sheet.setNumber(loaMeasurementSheet.getNumber());
					sheet.setQuantity(loaMeasurementSheet.getQuantity());
					sheet.setWidth(loaMeasurementSheet.getWidth());
					sheet.setAuditDetails(measurementBookUtils.setAuditDetails(requestInfo, false));
					measurementSheets.add(sheet);
				}
				measurementBookDetail.setMeasurementSheets(measurementSheets);
				measurementBookDetail.setQuantity(loaActivity.getApprovedQuantity().doubleValue());
				measurementBookDetail.setRemarks(loaActivity.getRemarks());
				measurementBookDetail.setAuditDetails(measurementBookUtils.setAuditDetails(requestInfo, false));
			}
		}
	}

	
	/**
	 * @param letterOfAcceptanceEstimate
	 * @param detailedEstimate
	 * @param auditDetails
	 * 
	 * populate LOA activities after creating revision DE
	 */
	private void populateLOAActivities(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate,
			DetailedEstimate detailedEstimate, AuditDetails auditDetails) {
		List<LOAActivity> loaActivities = new ArrayList<>();
		for (EstimateActivity activity : detailedEstimate.getEstimateActivities()) {
			LOAActivity loaActivity = new LOAActivity();
			loaActivity.setTenantId(activity.getTenantId());
			loaActivity.setAuditDetails(auditDetails);
			loaActivity.setEstimateActivity(activity);
			loaActivity.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimate.getId());
			List<LOAMeasurementSheet> loaMeasurementSheets = new ArrayList<>();
			for (EstimateMeasurementSheet sheet : activity.getEstimateMeasurementSheets()) {
				LOAMeasurementSheet loaMeasurementSheet = new LOAMeasurementSheet();
				loaMeasurementSheet.setTenantId(sheet.getTenantId());
				loaMeasurementSheet.setAuditDetails(auditDetails);
				loaMeasurementSheet.setDepthOrHeight(sheet.getDepthOrHeight());
				loaMeasurementSheet.setEstimateMeasurementSheet(sheet.getId());
				loaMeasurementSheet.setLength(sheet.getLength());
				loaMeasurementSheet.setLoaActivity(loaActivity.getId());
				loaMeasurementSheet.setNumber(sheet.getNumber());
				loaMeasurementSheet.setQuantity(sheet.getQuantity());
				loaMeasurementSheet.setWidth(sheet.getWidth());

				loaMeasurementSheets.add(loaMeasurementSheet);
			}
			loaActivity.setLoaMeasurements(loaMeasurementSheets);
			loaActivities.add(loaActivity);
		}
		letterOfAcceptanceEstimate.setLoaActivities(loaActivities);
	}

	public MeasurementBookResponse update(MeasurementBookRequest measurementBookRequest) {
		measurementBookValidator.validateMB(measurementBookRequest, false);
        List<MeasurementBook> measurementBooks = new ArrayList<>();
		for (MeasurementBook measurementBook : measurementBookRequest.getMeasurementBooks()) {
			for (MeasurementBookDetail measurementBookDetail : measurementBook.getMeasurementBookDetails()) {
				if (measurementBookDetail.getId() == null)
					measurementBookDetail.setId(UUID.randomUUID().toString().replace("-", ""));
				for (MBMeasurementSheet sheet : measurementBookDetail.getMeasurementSheets()) {
					if (sheet.getId() == null)
						sheet.setId(UUID.randomUUID().toString().replace("-", ""));
					sheet.setAuditDetails(
							measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), true));
				}
				measurementBookDetail.setAuditDetails(
						measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), true));
			}

			for (DocumentDetail detail : measurementBook.getDocumentDetails()) {
				if (detail.getId() == null)
					detail.setId(UUID.randomUUID().toString().replace("-", ""));
				detail.setObjectType("MeasurementBook");
				detail.setAuditDetails(
						measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), true));
			}
			measurementBook.setAuditDetails(
					measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), true));

			if (measurementBook.getLumpSumMBDetails() != null && !measurementBook.getLumpSumMBDetails().isEmpty())
				createUpdateRevisionEstimate(measurementBook, measurementBookRequest.getRequestInfo(), true);

            List<LetterOfAcceptance> letterOfAcceptances =
                    letterOfAcceptanceRepository.searchLoaByLoaEstimateId(measurementBook.getTenantId(), measurementBook.getLetterOfAcceptanceEstimate(), measurementBookRequest.getRequestInfo());
            if(letterOfAcceptances != null && letterOfAcceptances.isEmpty())
                measurementBook.setLetterOfAcceptanceEstimate(letterOfAcceptances.get(0).getLetterOfAcceptanceEstimates().get(0));

            if(measurementBook.getStatus() != null && measurementBook.getStatus().getCode().equals(CommonConstants.STATUS_CANCELLED))
                measurementBooks.add(measurementBook);

		}
		kafkaTemplate.send(propertiesManager.getWorksMBCreateUpdateTopic(), measurementBookRequest);
        if(measurementBooks != null && !measurementBooks.isEmpty()) {
            MeasurementBookRequest backUpdateRequest = new MeasurementBookRequest();
            backUpdateRequest.setMeasurementBooks(measurementBooks);
            kafkaTemplate.send(propertiesManager.getWorksLoaBackUpdateOnCreateMB(), backUpdateRequest);
        }
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		measurementBookResponse.setMeasurementBooks(measurementBookRequest.getMeasurementBooks());
		return measurementBookResponse;
	}

	public MeasurementBookResponse search(MeasurementBookSearchContract measurementBookSearchContract,
			RequestInfo requestInfo) {
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		measurementBookResponse.setMeasurementBooks(
				measurementBookRepository.searchMeasurementBooks(measurementBookSearchContract, requestInfo));
		return measurementBookResponse;
	}

	private Boolean isConfigRequired(String keyName, RequestInfo requestInfo, final String tenantId) {
		Boolean isSpilloverWFReq = false;
		JSONArray responseJSONArray = measurementBookUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT,
				CommonConstants.CODE, keyName, tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
		if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
			Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
			if (jsonMap.get("value").equals("Yes"))
				isSpilloverWFReq = true;
		}
		return isSpilloverWFReq;
	}
}
