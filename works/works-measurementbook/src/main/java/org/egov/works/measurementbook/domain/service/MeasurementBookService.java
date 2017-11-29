package org.egov.works.measurementbook.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.measurementbook.config.PropertiesManager;
import org.egov.works.measurementbook.domain.repository.MeasurementBookRepository;
import org.egov.works.measurementbook.utils.MeasurementBookUtils;
import org.egov.works.measurementbook.web.contract.AuditDetails;
import org.egov.works.measurementbook.web.contract.DetailedEstimate;
import org.egov.works.measurementbook.web.contract.DetailedEstimateRequest;
import org.egov.works.measurementbook.web.contract.DocumentDetail;
import org.egov.works.measurementbook.web.contract.EstimateActivity;
import org.egov.works.measurementbook.web.contract.EstimateMeasurementSheet;
import org.egov.works.measurementbook.web.contract.LOAActivity;
import org.egov.works.measurementbook.web.contract.LOAMeasurementSheet;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptance;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceEstimate;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceRequest;
import org.egov.works.measurementbook.web.contract.MBMeasurementSheet;
import org.egov.works.measurementbook.web.contract.MeasurementBook;
import org.egov.works.measurementbook.web.contract.MeasurementBookDetail;
import org.egov.works.measurementbook.web.contract.MeasurementBookRequest;
import org.egov.works.measurementbook.web.contract.MeasurementBookResponse;
import org.egov.works.measurementbook.web.contract.MeasurementBookSearchContract;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MeasurementBookService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

//	 @Autowired
//	 private CommonUtils commonUtils;

	@Autowired
	private MeasurementBookUtils measurementBookUtils;

    @Autowired
    private MeasurementBookRepository measurementBookRepository;

	public MeasurementBookResponse create(MeasurementBookRequest measurementBookRequest) {
		for (MeasurementBook measurementBook : measurementBookRequest.getMeasurementBooks()) {
			measurementBook.setId(UUID.randomUUID().toString().replace("-", ""));
			for (MeasurementBookDetail measurementBookDetail : measurementBook.getMeasurementBookDetails()) {
				measurementBookDetail.setId(UUID.randomUUID().toString().replace("-", ""));
				for (MBMeasurementSheet sheet : measurementBookDetail.getMeasurementSheets()) {
					sheet.setId(UUID.randomUUID().toString().replace("-", ""));
					sheet.setAuditDetails(
							measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), false));
				}
				measurementBookDetail.setAuditDetails(
						measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), false));
			}

			for (DocumentDetail detail : measurementBook.getDocumentDetails()) {
				detail.setId(UUID.randomUUID().toString().replace("-", ""));
				detail.setObjectType("MeasurementBook");
				detail.setAuditDetails(
						measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), false));
			}
			measurementBook.setAuditDetails(
					measurementBookUtils.setAuditDetails(measurementBookRequest.getRequestInfo(), false));
			
			if (measurementBook.getEstimateActivities() != null && !measurementBook.getEstimateActivities().isEmpty())
				createRevisionEstimate(measurementBook, measurementBookRequest.getRequestInfo());
		}
		kafkaTemplate.send(propertiesManager.getWorksMBCreateUpdateTopic(), measurementBookRequest);
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		measurementBookResponse.setMeasurementBooks(measurementBookRequest.getMeasurementBooks());
		return measurementBookResponse;
	}

	private void createRevisionEstimate(MeasurementBook measurementBook, RequestInfo requestInfo) {
		DetailedEstimateRequest detailedEstimateRequest = new DetailedEstimateRequest();
		LetterOfAcceptanceRequest letterOfAcceptanceRequest = new LetterOfAcceptanceRequest();
		AuditDetails auditDetails = measurementBookUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), false);
		List<DetailedEstimate> detailedEstimates = new ArrayList<>();
		List<LetterOfAcceptance> letterOfAcceptances = new ArrayList<>();
		DetailedEstimate detailedEstimate = new DetailedEstimate();
		LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
		detailedEstimate.setId(UUID.randomUUID().toString().replace("-", ""));
		detailedEstimate.setTenantId(measurementBook.getTenantId());
		detailedEstimate.setParent(measurementBook.getLetterOfAcceptanceEstimate().getDetailedEstimate().getId());
		detailedEstimate.setEstimateActivities(measurementBook.getEstimateActivities());
		for (final EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
			estimateActivity.setId(UUID.randomUUID().toString().replace("-", ""));
			estimateActivity.setTenantId(measurementBook.getTenantId());
			estimateActivity.setAuditDetails(auditDetails);
			if (estimateActivity.getEstimateMeasurementSheets() != null) {
				for (final EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity
						.getEstimateMeasurementSheets()) {
					estimateMeasurementSheet.setId(UUID.randomUUID().toString().replace("-", ""));
					estimateMeasurementSheet.setTenantId(measurementBook.getTenantId());
					estimateMeasurementSheet.setAuditDetails(auditDetails);
				}
			}
		}
		detailedEstimates.add(detailedEstimate);
		detailedEstimateRequest.setRequestInfo(requestInfo);
		kafkaTemplate.send(propertiesManager.getWorksRECreateUpdateTopic(), detailedEstimateRequest);
		
		letterOfAcceptance.setId(UUID.randomUUID().toString().replace("-", ""));
		letterOfAcceptance.setTenantId(measurementBook.getTenantId());
		letterOfAcceptance.setParent(measurementBook.getLetterOfAcceptanceEstimate().getLetterOfAcceptance());
		List<LetterOfAcceptanceEstimate> letterOfAcceptanceEstimates = new ArrayList<>();
		LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = new LetterOfAcceptanceEstimate();
		letterOfAcceptanceEstimate.setId(UUID.randomUUID().toString().replace("-", ""));
		letterOfAcceptanceEstimate.setAuditDetails(auditDetails);
		letterOfAcceptanceEstimate.setDetailedEstimate(detailedEstimate);
		letterOfAcceptanceEstimate.setLetterOfAcceptance(letterOfAcceptance.getId());
		populateLOAActivities(letterOfAcceptanceEstimate, detailedEstimate, auditDetails);
		letterOfAcceptanceEstimates.add(letterOfAcceptanceEstimate);
		letterOfAcceptance.setLetterOfAcceptanceEstimates(letterOfAcceptanceEstimates);
		letterOfAcceptances.add(letterOfAcceptance);
		letterOfAcceptanceRequest.setLetterOfAcceptances(letterOfAcceptances);
		letterOfAcceptanceRequest.setRequestInfo(requestInfo);
		kafkaTemplate.send(propertiesManager.getWorksRevisionLOACreateUpdateTopic(), detailedEstimateRequest);
	}

	private void populateLOAActivities(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate,
			DetailedEstimate detailedEstimate, AuditDetails auditDetails) {
		List<LOAActivity> loaActivities = new ArrayList<>();
		for (EstimateActivity activity : detailedEstimate.getEstimateActivities()) {
			LOAActivity loaActivity = new LOAActivity();
			loaActivity.setId(UUID.randomUUID().toString().replace("-", ""));
			loaActivity.setTenantId(activity.getTenantId());
			loaActivity.setAuditDetails(auditDetails);
			loaActivity.setEstimateActivity(activity);
			loaActivity.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimate.getId());
			List<LOAMeasurementSheet> loaMeasurementSheets = new ArrayList<>();
			for (EstimateMeasurementSheet sheet : activity.getEstimateMeasurementSheets()) {
				LOAMeasurementSheet loaMeasurementSheet = new LOAMeasurementSheet();
				loaMeasurementSheet.setId(UUID.randomUUID().toString().replace("-", ""));
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
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		return measurementBookResponse;
	}

	public MeasurementBookResponse search(MeasurementBookSearchContract measurementBookSearchContract,
			RequestInfo requestInfo) {
        MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
        measurementBookResponse.setMeasurementBooks(measurementBookRepository.searchMeasurementBooks(measurementBookSearchContract,requestInfo));
        return measurementBookResponse;
	}
}
