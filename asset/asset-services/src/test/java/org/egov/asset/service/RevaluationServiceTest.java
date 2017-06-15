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
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.enums.RevaluationStatus;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.RevaluationRepository;
import org.egov.asset.util.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class RevaluationServiceTest {

	@Mock
	private RevaluationRepository revaluationRepository;

	@Mock
	private AssetProducer assetProducer;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private ApplicationProperties applicationProperties;

	@Mock
	private RevaluationCriteria revaluationCriteria;

	@InjectMocks
	private RevaluationService revaluationService;

	@Test
	public void testCreate() {
		RevaluationResponse revaluationResponse = null;
		try {
			revaluationResponse = getRevaluation("revaluation/revaluationServiceResponse.revaluation1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		RevaluationRequest revaluationRequest = new RevaluationRequest();
		revaluationRequest.setRevaluation(getRevaluationForCreateAsync());
		revaluationRequest.getRevaluation().setId(Long.valueOf("15"));

		doNothing().when(revaluationRepository).create(revaluationRequest);
		revaluationService.create(Matchers.any(RevaluationRequest.class));
		assertEquals(revaluationResponse.getRevaluations().get(0).toString(),
				revaluationRequest.getRevaluation().toString());
	}

	@Test
	public void testCreateAsync() {
		RevaluationRequest revaluationRequest = new RevaluationRequest();
		revaluationRequest.setRevaluation(getRevaluationForCreateAsync());

		final List<RevaluationRequest> insertedRevaluationRequest = new ArrayList<>();
		insertedRevaluationRequest.add(revaluationRequest);
		RevaluationResponse revaluationResponse = null;
		try {
			revaluationResponse = getRevaluation("revaluation/revaluationServiceResponse.revaluation1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		when(applicationProperties.getCreateAssetDisposalTopicName()).thenReturn("kafka.topics.save.disposal");
		when(revaluationRepository.getNextRevaluationId()).thenReturn(15);
		assertTrue(revaluationResponse.getRevaluations().get(0).getId().equals(Long.valueOf("15")));
		doNothing().when(assetProducer).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());
		revaluationService.createAsync(revaluationRequest);

		assertEquals(revaluationResponse.getRevaluations().get(0).toString(),
				revaluationRequest.getRevaluation().toString());
	}

	@Test
	public void testSearch() {
		List<Revaluation> revaluation = new ArrayList<>();
		revaluation.add(getRevaluationForSearch());

		RevaluationResponse revaluationResponse = new RevaluationResponse();
		revaluationResponse.setRevaluations(revaluation);

		when(revaluationRepository.search(any(RevaluationCriteria.class))).thenReturn(revaluation);
		RevaluationResponse expectedRevaluationResponse = revaluationService.search(any(RevaluationCriteria.class));

		assertEquals(revaluationResponse.toString(), expectedRevaluationResponse.toString());
	}

	private Revaluation getRevaluationForCreateAsync() {

		Revaluation revaluation = new Revaluation();
		revaluation.setTenantId("ap.kurnool");
		revaluation.setAssetId(Long.valueOf("31"));
		revaluation.setCurrentCapitalizedValue(Double.valueOf("100.68"));
		revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
		revaluation.setRevaluationAmount(Double.valueOf("10"));
		revaluation.setValueAfterRevaluation(Double.valueOf("90.68"));
		revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
		revaluation.setReevaluatedBy("5");
		revaluation.setReasonForRevaluation("reasonForRevaluation");
		revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
		revaluation.setFunction(Long.valueOf("2"));
		revaluation.setFund(Long.valueOf("3"));
		revaluation.setScheme(Long.valueOf("4"));
		revaluation.setSubScheme(Long.valueOf("5"));
		revaluation.setComments("coments");
		revaluation.setStatus(RevaluationStatus.ACTIVE);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("5");
		auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
		auditDetails.setLastModifiedBy("5");
		auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
		revaluation.setAuditDetails(auditDetails);

		return revaluation;
	}

	private Revaluation getRevaluationForSearch() {

		Revaluation revaluation = new Revaluation();
		revaluation.setTenantId("ap.kurnool");
		revaluation.setAssetId(Long.valueOf("31"));
		revaluation.setCurrentCapitalizedValue(Double.valueOf("100.68"));
		revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
		revaluation.setRevaluationAmount(Double.valueOf("10"));
		revaluation.setValueAfterRevaluation(Double.valueOf("90.68"));
		revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
		revaluation.setReevaluatedBy("5");
		revaluation.setReasonForRevaluation("reasonForRevaluation");
		revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
		revaluation.setFunction(Long.valueOf("2"));
		revaluation.setFund(Long.valueOf("3"));
		revaluation.setScheme(Long.valueOf("4"));
		revaluation.setSubScheme(Long.valueOf("5"));
		revaluation.setComments("coments");
		revaluation.setStatus(RevaluationStatus.ACTIVE);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("5");
		auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
		auditDetails.setLastModifiedBy("5");
		auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
		revaluation.setAuditDetails(auditDetails);

		return revaluation;
	}

	private RevaluationResponse getRevaluation(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, RevaluationResponse.class);
	}
}
