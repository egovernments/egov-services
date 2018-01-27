package org.egov.works.estimate.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.persistence.repository.*;
import org.egov.works.estimate.web.contract.*;
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

    @Autowired
    private EstimateTechnicalSanctionRepository estimateTechnicalSanctionRepository;
	
	public List<DetailedEstimate> search(DetailedEstimateSearchContract detailedEstimateSearchContract, final RequestInfo requestInfo) {
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
				.search(detailedEstimateSearchContract, requestInfo)) {
			detailedEstimate = estimate.toDomain(estimate);
			
			abstractEstimateDetailsSearchContract = AbstractEstimateDetailsSearchContract.builder().tenantId(estimate.getTenantId())
                    .abstractEstimateIds(Arrays.asList(estimate.getAbstractEstimateDetail())).build();
			
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

            TechnicalSanctionSearchContract technicalSanctionSearchContract = TechnicalSanctionSearchContract.builder()
                    .tenantId(estimate.getTenantId())
                    .detailedEstimateIds(Arrays.asList(estimate.getId()))
                    .technicalSanctionNumbers(detailedEstimateSearchContract.getTechnicalSanctionNumbers()).build();
			
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
            detailedEstimate.setEstimateTechnicalSanctions(estimateTechnicalSanctionRepository.search(technicalSanctionSearchContract));
			detailedEstimates.add(detailedEstimate);
		}
		return detailedEstimates;
	}

}