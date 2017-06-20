package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.DisposalRepository;
import org.egov.asset.util.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class DisposalServiceTest {

	@Mock
	private DisposalRepository disposalRepository;

	@Mock
	private AssetProducer assetProducer;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private DisposalCriteria disposalCriteria;

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private DisposalService disposalService;

	@Test
	public void testSearch() {
		List<Disposal> disposal = new ArrayList<>();
		disposal.add(getDisposalForSearch());

		DisposalResponse disposalResponse = new DisposalResponse();
		disposalResponse.setDisposals(disposal);

		RequestInfo requestInfo = new RequestInfo();
		when(disposalRepository.search(any(DisposalCriteria.class))).thenReturn(disposal);
		DisposalResponse expectedDisposalResponse = disposalService.search(Matchers.any(DisposalCriteria.class),
				requestInfo);

		assertEquals(disposalResponse.toString(), expectedDisposalResponse.toString());
	}

	@Test
	public void testCreateAsync() {
		DisposalRequest disposalRequest = new DisposalRequest();
		disposalRequest.setDisposal(getDisposalForCreateAsync());

		final List<DisposalRequest> insertedDisposalRequest = new ArrayList<>();
		insertedDisposalRequest.add(disposalRequest);
		DisposalResponse disposalResponse = null;
		try {
			disposalResponse = getDisposal("disposal/disposalServiceResponse.disposal1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		when(applicationProperties.getCreateAssetDisposalTopicName()).thenReturn("kafka.topics.save.disposal");
		when(disposalRepository.getNextDisposalId()).thenReturn(15);
		assertTrue(disposalResponse.getDisposals().get(0).getId().equals(Long.valueOf("15")));
		doNothing().when(assetProducer).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());
		disposalService.createAsync(disposalRequest);

		assertEquals(disposalResponse.getDisposals().get(0).toString(), disposalRequest.getDisposal().toString());
	}

	@Test
	public void testCreate() {
		DisposalResponse disposalResponse = null;
		try {
			disposalResponse = getDisposal("disposal/disposalServiceResponse.disposal1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		DisposalRequest disposalRequest = new DisposalRequest();
		disposalRequest.setDisposal(getDisposalForCreateAsync());
		disposalRequest.getDisposal().setId(Long.valueOf("15"));

		doNothing().when(disposalRepository).create(disposalRequest);
		disposalService.create(Matchers.any(DisposalRequest.class));
		assertEquals(disposalResponse.getDisposals().get(0).toString(), disposalRequest.getDisposal().toString());
	}

	private Disposal getDisposalForSearch() {

		Disposal disposal = new Disposal();
		disposal.setTenantId("ap.kurnool");
		disposal.setId(Long.valueOf("15"));
		disposal.setAssetId(Long.valueOf("31"));
		disposal.setBuyerName("Abhi");
		disposal.setBuyerAddress("Bangalore");
		disposal.setDisposalDate(Long.valueOf("1496564536178"));
		disposal.setDisposalReason("disposalReason");
		disposal.setPanCardNumber("baq1234567");
		disposal.setAadharCardNumber("12345678123456");
		disposal.setAssetCurrentValue(Double.valueOf("100.0"));
		disposal.setSaleValue(Double.valueOf("200.0"));
		disposal.setTransactionType(TransactionType.SALE);
		disposal.setAssetSaleAccount(Long.valueOf("15"));

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("2");
		auditDetails.setCreatedDate(Long.valueOf("1496746205544"));
		auditDetails.setLastModifiedBy("2");
		auditDetails.setLastModifiedDate(Long.valueOf("1496746205544"));
		disposal.setAuditDetails(auditDetails);

		return disposal;
	}

	private Disposal getDisposalForCreateAsync() {

		Disposal disposal = new Disposal();
		disposal.setTenantId("ap.kurnool");
		disposal.setAssetId(Long.valueOf("31"));
		disposal.setBuyerName("Abhi");
		disposal.setBuyerAddress("Bangalore");
		disposal.setDisposalDate(Long.valueOf("1496564536178"));
		disposal.setDisposalReason("disposalReason");
		disposal.setPanCardNumber("baq1234567");
		disposal.setAadharCardNumber("12345678123456");
		disposal.setAssetCurrentValue(Double.valueOf("100.0"));
		disposal.setSaleValue(Double.valueOf("200.0"));
		disposal.setTransactionType(TransactionType.SALE);
		disposal.setAssetSaleAccount(Long.valueOf("15"));

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("2");
		auditDetails.setCreatedDate(Long.valueOf("1496746205544"));
		auditDetails.setLastModifiedBy("2");
		auditDetails.setLastModifiedDate(Long.valueOf("1496746205544"));
		disposal.setAuditDetails(auditDetails);

		return disposal;
	}

	private DisposalResponse getDisposal(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, DisposalResponse.class);
	}
}
