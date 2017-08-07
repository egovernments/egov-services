package org.egov.asset.service;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;

import java.awt.List;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import org.apache.coyote.RequestInfo;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DepreciationResponse;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.repository.DepreciationRepository;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DepreciationService.class)
public class DepreciationServiceTest {
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private AssetConfigurationService assetConfigurationService;

	@Mock
	private DepreciationRepository depreciationRepository;
	
	@Mock
	private CurrentValueService currentValueService;
	
	@Mock
	private ResponseInfoFactory responseInfoFactory;

	@Mock
	private AssetDepreciator assetDepreciator;
	
	@Mock
	private SequenceGenService sequenceGenService;
	
	@Mock
	private ApplicationProperties applicationProperties;
	
	@Mock
	private AssetCommonService assetCommonService;
	
	@Mock
	private ObjectMapper mapper;
	
	@InjectMocks
	private DepreciationService depreciationService;
	
	@Test
	public void test_Depreciate_Asset(){
		DepreciationResponse depreciationResponse = null;

		AuditDetails auditDetails = getAuditDetails();

		DepreciationService dsMock = PowerMockito.mock(DepreciationService.class);
		AssetCommonService acsMock = PowerMockito.mock(AssetCommonService.class);
		AssetDepreciator assetDepreciator = PowerMockito.mock(AssetDepreciator.class);
		try {
			PowerMockito.doReturn(auditDetails).when(acsMock, "getAuditDetails", any(RequestInfo.class));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		assetDepreciator.depreciateAsset(any(DepreciationRequest.class), (java.util.List<CalculationAssetDetails>) any(List.class),
				any(Map.class), any(Map.class), (java.util.List<AssetCurrentValue>) any(List.class),
				(java.util.List<DepreciationDetail>) any(List.class));

		try {
			depreciationResponse = getDepreciationResponse("depreciation/depreciationscreateresponse.json");
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		DepreciationRequest depreciationRequest = new DepreciationRequest();
		depreciationRequest.setDepreciationCriteria(getDepreciationCriteria());
		
		Depreciation depreciation = getDepreciation(getDepreciationCriteria(),getAuditDetails());
		
		
		doNothing().when(depreciationRepository).saveDepreciation(depreciation);
		
	}


	private Depreciation getDepreciation(DepreciationCriteria depreciationCriteria,AuditDetails auditDetails) {
		ArrayList depreciationDetails = new ArrayList();
		depreciationDetails.add(getDepreciationDetail());
		Depreciation depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria).depreciationDetails(
				depreciationDetails).voucherReference(Long.valueOf("1")).auditDetails(auditDetails).build();
		return depreciation;
	}

	private DepreciationDetail getDepreciationDetail() {
		DepreciationDetail depreciationDetail = new DepreciationDetail();
		depreciationDetail.setAssetId(Long.valueOf("2"));
		depreciationDetail.setDepreciationRate(Double.valueOf("10"));
		depreciationDetail.setId(Long.valueOf("399"));
		depreciationDetail.setStatus(DepreciationStatus.SUCCESS);
		depreciationDetail.setValueBeforeDepreciation(new BigDecimal("0.18"));
		depreciationDetail.setDepreciationValue(new BigDecimal("1.068"));
		depreciationDetail.setValueAfterDepreciation(new BigDecimal("0.888"));
		depreciationDetail.setReasonForFailure(null);
		return depreciationDetail;
	}


	private AuditDetails getAuditDetails() {
		final AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setCreatedDate(Long.valueOf("1501756252639"));
		auditDetails.setLastModifiedBy("1");
		auditDetails.setLastModifiedDate(Long.valueOf("1501756252639"));
		return auditDetails;
	}

	private DepreciationCriteria getDepreciationCriteria() {
		DepreciationCriteria depreciationCriteria = new DepreciationCriteria();
		depreciationCriteria.setAssetIds(null);
		depreciationCriteria.setFinancialYear("2017");
		depreciationCriteria.setFromDate(Long.valueOf(0));
		depreciationCriteria.setToDate(Long.valueOf("0"));
		depreciationCriteria.setTenantId("ap.kurnool");
		return depreciationCriteria;
	}

	private DepreciationResponse getDepreciationResponse(String filePath) throws IOException {
		final String empJson = new FileUtils().getFileContents(filePath);
		return mapper.readValue(empJson, DepreciationResponse.class);
	}

}
