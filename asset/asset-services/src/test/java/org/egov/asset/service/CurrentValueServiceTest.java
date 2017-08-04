package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetCurrentValueResponse;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.repository.CurrentValueRepository;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CurrentValueService.class)
public class CurrentValueServiceTest {
	
	@Mock
	private ResponseInfoFactory responseInfoFactory;

	@Mock
	private CurrentValueRepository currentValueRepository;

	@Mock
	private SequenceGenService sequenceGenService;

	@Mock
	private ApplicationProperties applicationProperties;
	
	@InjectMocks
	private CurrentValueService currentValueService;
	
	@Mock
	private ObjectMapper mapper;
	
	@Mock
	private FileUtils fileUtils;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateAsync() throws NumberFormatException, Exception {

		final CurrentValueService mock = PowerMockito.mock(CurrentValueService.class);

		final AssetCurrentValueRequest assetCurrentValueRequest = new AssetCurrentValueRequest();
		assetCurrentValueRequest.setAssetCurrentValues(getAssetCurrentValuesForCreateAsync());

		final List<AssetCurrentValueRequest> insertedAssetCurrentValueRequest = new ArrayList<AssetCurrentValueRequest>();
		insertedAssetCurrentValueRequest.add(assetCurrentValueRequest);
		AssetCurrentValueResponse assetCurrentValueResponse = new AssetCurrentValueResponse();
		assetCurrentValueResponse.setAssetCurrentValues(getAssetCurrentValuesForCreateAsync());
		System.err.println("response--"+assetCurrentValueResponse);
		mock.createCurrentValueAsync(assetCurrentValueRequest);
		assertEquals(assetCurrentValueResponse.getAssetCurrentValues().toString(),
				assetCurrentValueRequest.getAssetCurrentValues().toString());
	}


	private List<AssetCurrentValue> getAssetCurrentValuesForCreateAsync() {
		List<AssetCurrentValue> assetCurrentValues = new ArrayList<AssetCurrentValue>();
		AssetCurrentValue assetCurrentValue = new AssetCurrentValue();
		assetCurrentValue.setAssetId(2L);
		assetCurrentValue.setAssetTranType(TransactionType.DISPOSAL);
		assetCurrentValue.setAuditDetails(getAuditDetails());
		assetCurrentValue.setId(1L);
		assetCurrentValue.setTenantId("ap.kurnool");
		assetCurrentValues.add(assetCurrentValue);
		return assetCurrentValues;
	}

	private AssetCurrentValueResponse getAssetCurrentValue(String filePath) throws IOException {
		final String empJson = fileUtils.getFileContents(filePath);
		return mapper.readValue(empJson, AssetCurrentValueResponse.class);
	}
	
	private AuditDetails getAuditDetails() {
		final AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(String.valueOf("5"));
		auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
		auditDetails.setLastModifiedBy(String.valueOf("5"));
		auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
		return auditDetails;
	}

}
