package org.egov.works.estimate.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.persistence.repository.AbstractEstimateDetailsJdbcRepository;
import org.egov.works.estimate.persistence.repository.DetailedEstimateDeductionJdbcRepository;
import org.egov.works.estimate.persistence.repository.DetailedEstimateJdbcRepository;
import org.egov.works.estimate.persistence.repository.EstimateActivityJdbcRepository;
import org.egov.works.estimate.persistence.repository.EstimateAssetJdbcRepository;
import org.egov.works.estimate.persistence.repository.EstimateMeasurementSheetJdbcRepository;
import org.egov.works.estimate.persistence.repository.EstimateOverheadJdbcRepository;
import org.egov.works.estimate.persistence.repository.MultiYearEstimateJdbcRepository;
import org.egov.works.estimate.web.contract.AbstractEstimateDetails;
import org.egov.works.estimate.web.contract.AbstractEstimateDetailsSearchContract;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.DetailedEstimateDeductionSearchContract;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
import org.egov.works.estimate.web.contract.EstimateActivity;
import org.egov.works.estimate.web.contract.EstimateActivitySearchContract;
import org.egov.works.estimate.web.contract.EstimateAssetSearchContract;
import org.egov.works.estimate.web.contract.EstimateMeasurementSheet;
import org.egov.works.estimate.web.contract.EstimateMeasurementSheetSearchContract;
import org.egov.works.estimate.web.contract.EstimateOverheadSearchContract;
import org.egov.works.estimate.web.contract.MultiYearEstimateSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DetailedEstimateRepository {

	@Autowired
	private DetailedEstimateJdbcRepository detailedEstimateJdbcRepository;

	@Autowired
	private AbstractEstimateDetailsJdbcRepository abstractEstimateDetailsJdbcRepository;

	@Autowired
	private MultiYearEstimateJdbcRepository multiYearEstimateJdbcRepository;
	
	@Autowired
	private DetailedEstimateDeductionJdbcRepository detailedEstimateDeductionJdbcRepository;
	
	@Autowired
	private EstimateOverheadJdbcRepository estimateOverheadJdbcRepository;
	
	@Autowired
	private EstimateAssetJdbcRepository estimateAssetJdbcRepository;
	
	@Autowired
	private EstimateActivityJdbcRepository estimateActivityJdbcRepository;
	
	@Autowired
	private EstimateMeasurementSheetJdbcRepository estimateMeasurementSheetJdbcRepository;
	
	public List<DetailedEstimate> search(DetailedEstimateSearchContract detailedEstimateSearchContract) {
		DetailedEstimate detailedEstimate;
		List<DetailedEstimate> detailedEstimates = new ArrayList<>();
		AbstractEstimateDetailsSearchContract abstractEstimateDetailsSearchContract;
		MultiYearEstimateSearchContract multiYearEstimateSearchContract;
		DetailedEstimateDeductionSearchContract detailedEstimateDeductionSearchContract;
		EstimateOverheadSearchContract estimateOverheadSearchContract;
		EstimateAssetSearchContract estimateAssetSearchContract;
		EstimateActivitySearchContract estimateActivitySearchContract;
		EstimateMeasurementSheetSearchContract estimateMeasurementSheetSearchContract;
		for (DetailedEstimateHelper estimate : detailedEstimateJdbcRepository
				.search(detailedEstimateSearchContract)) {
			detailedEstimate = estimate.toDomain(estimate);
			
			abstractEstimateDetailsSearchContract = new AbstractEstimateDetailsSearchContract();
			abstractEstimateDetailsSearchContract.setTenantId(estimate.getTenantId());
			abstractEstimateDetailsSearchContract
					.setAbstractEstimateIds(Arrays.asList(estimate.getAbstractEstimateDetail()));
			
			multiYearEstimateSearchContract = new MultiYearEstimateSearchContract();
			multiYearEstimateSearchContract.setTenantId(estimate.getTenantId());
			multiYearEstimateSearchContract.setDetailedEstimateIds(Arrays.asList(estimate.getId()));

			estimateAssetSearchContract = new EstimateAssetSearchContract();
			estimateAssetSearchContract.setTenantId(estimate.getTenantId());
			estimateAssetSearchContract.setDetailedEstimateIds(Arrays.asList(estimate.getId()));

			detailedEstimateDeductionSearchContract = new DetailedEstimateDeductionSearchContract();
			detailedEstimateDeductionSearchContract.setDetailedEstimateIds(Arrays.asList(estimate.getId()));
			detailedEstimateDeductionSearchContract.setTenantId(estimate.getTenantId());
			
			estimateOverheadSearchContract = new EstimateOverheadSearchContract();
			estimateOverheadSearchContract.setDetailedEstimateIds(Arrays.asList(estimate.getId()));
			estimateOverheadSearchContract.setTenantId(estimate.getTenantId());
			
			estimateActivitySearchContract = new EstimateActivitySearchContract();
			estimateActivitySearchContract.setDetailedEstimateIds(Arrays.asList(estimate.getId()));
			estimateActivitySearchContract.setTenantId(estimate.getTenantId());
			
			
			
			List<EstimateActivity> estimateActivities = estimateActivityJdbcRepository.search(estimateActivitySearchContract);
			for(EstimateActivity estimateActivity : estimateActivities) {
				estimateMeasurementSheetSearchContract = new EstimateMeasurementSheetSearchContract();
				estimateMeasurementSheetSearchContract.setTenantId(estimate.getTenantId());
				estimateMeasurementSheetSearchContract.setEstimateActivityIds(Arrays.asList(estimateActivity.getId()));
				estimateActivity.setEstimateMeasurementSheets(estimateMeasurementSheetJdbcRepository.search(estimateMeasurementSheetSearchContract));
			}
			
			List<AbstractEstimateDetails> abstractEstimateDetails = abstractEstimateDetailsJdbcRepository.search(abstractEstimateDetailsSearchContract);
			if(!abstractEstimateDetails.isEmpty() && abstractEstimateDetails != null)
				detailedEstimate.setAbstractEstimateDetail(abstractEstimateDetails.get(0));
			detailedEstimate.setMultiYearEstimates(multiYearEstimateJdbcRepository.search(multiYearEstimateSearchContract));
			
			detailedEstimate.setDetailedEstimateDeductions(detailedEstimateDeductionJdbcRepository.search(detailedEstimateDeductionSearchContract));
			detailedEstimate.setEstimateOverheads(estimateOverheadJdbcRepository.search(estimateOverheadSearchContract));
			detailedEstimate.setAssets(estimateAssetJdbcRepository.search(estimateAssetSearchContract));
			detailedEstimate.setEstimateActivities(estimateActivities);
			detailedEstimates.add(detailedEstimate);
		}
		return detailedEstimates;
	}

}