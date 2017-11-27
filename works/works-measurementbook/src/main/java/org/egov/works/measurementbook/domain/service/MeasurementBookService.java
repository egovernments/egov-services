package org.egov.works.measurementbook.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.measurementbook.config.PropertiesManager;
import org.egov.works.measurementbook.utils.MeasurementBookUtils;
import org.egov.works.measurementbook.web.contract.AuditDetails;
import org.egov.works.measurementbook.web.contract.DetailedEstimate;
import org.egov.works.measurementbook.web.contract.DetailedEstimateRequest;
import org.egov.works.measurementbook.web.contract.DocumentDetail;
import org.egov.works.measurementbook.web.contract.EstimateActivity;
import org.egov.works.measurementbook.web.contract.EstimateMeasurementSheet;
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

	// @Autowired
	// private CommonUtils commonUtils;

	@Autowired
	private MeasurementBookUtils measurementBookUtils;

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
		AuditDetails auditDetails = measurementBookUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), false);
		List<DetailedEstimate> detailedEstimates = new ArrayList<>();
		DetailedEstimate detailedEstimate = new DetailedEstimate();
		detailedEstimate.setId(UUID.randomUUID().toString().replace("-", ""));
		detailedEstimate.setParent(measurementBook.getLetterOfAcceptanceEstimate().getDetailedEstimate().getId());
		detailedEstimate.setEstimateActivities(measurementBook.getEstimateActivities());
		for (final EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
			estimateActivity.setId(UUID.randomUUID().toString().replace("-", ""));
			estimateActivity.setAuditDetails(auditDetails);
			if (estimateActivity.getEstimateMeasurementSheets() != null) {
				for (final EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity
						.getEstimateMeasurementSheets()) {
					estimateMeasurementSheet.setId(UUID.randomUUID().toString().replace("-", ""));
					estimateMeasurementSheet.setAuditDetails(auditDetails);
				}
			}
		}
		detailedEstimates.add(detailedEstimate);
		detailedEstimateRequest.setRequestInfo(requestInfo);
		kafkaTemplate.send(propertiesManager.getWorksRECreateUpdateTopic(), detailedEstimateRequest);
	}

	public MeasurementBookResponse update(MeasurementBookRequest measurementBookRequest) {
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		return measurementBookResponse;
	}

	public MeasurementBookResponse search(MeasurementBookSearchContract measurementBookSearchContract,
			RequestInfo requestInfo) {
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		return measurementBookResponse;
	}
}
