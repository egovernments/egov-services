package org.egov.mr.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Document;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.MarriageDocumentType;
import org.egov.mr.model.enums.ApplicationType;
import org.egov.mr.model.enums.DocumentProof;
import org.egov.mr.repository.MarriageDocumentTypeRepository;
import org.egov.mr.util.SequenceIdGenService;
import org.egov.mr.utils.FileUtils;
import org.egov.mr.web.contract.MarriageDocTypeRequest;
import org.egov.mr.web.contract.MarriageDocTypeResponse;
import org.egov.mr.web.contract.MarriageDocumentTypeSearchCriteria;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class MarriageDocumentTypeServiceTest {

	@Mock
	private PropertiesManager prosManager;

	@Mock
	private LogAwareKafkaTemplate<String, Object> _kafkaTemplate;

	@Mock
	private SequenceIdGenService genService;

	@Mock
	private MarriageDocumentTypeRepository marriageDocumentTypeRepository;

	@Mock
	private MarriageDocumentTypeSearchCriteria mdtSearchCriteria;

	@InjectMocks
	private MarriageDocumentTypeService marriageDocumentTypeService;

	/**
	 * @Test_CreateAsync
	 */
	@Test
	public void testCreateAsync() {
		/**
		 * @DB
		 */
		MarriageDocTypeResponse mdtResponse = null;
		try {
			mdtResponse = getResponse("org/egov/mr/service/marriageDocumentTypeCreateResponseAsDB.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		List<MarriageDocumentType> marriageDocumentType = getMarriageDocumentTypesForCreate();

		/**
		 * @RequestInfo
		 */
		MarriageDocTypeRequest marriageDocTypeRequest = new MarriageDocTypeRequest();
		marriageDocTypeRequest.setMarriageDocTypes(marriageDocumentType);
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setTs(Long.valueOf("987456321"));
		marriageDocTypeRequest.setRequestInfo(requestInfo);

		when(genService.idSeqGen(Matchers.anyInt(), Matchers.anyString())).thenReturn(getIds());
		when(genService.codeSeqGen(Matchers.anyInt(), Matchers.anyString())).thenReturn(getCodes());
		when(prosManager.getCreateMarriageDocumentTypeTopicName())
				.thenReturn("kafka.topics.create.marriagedocumenttype");
		when(_kafkaTemplate.send(Matchers.anyString(), Matchers.anyObject())).thenReturn(new SendResult<>(null, null));

		// resEntity.getBody()
		ResponseEntity<?> resEntity = marriageDocumentTypeService.createAsync(marriageDocTypeRequest);
		assertEquals(Long.valueOf("200").toString(), resEntity.getStatusCode().toString());
		System.err.println(mdtResponse);
		System.err.println(resEntity.getBody());
		assertEquals(mdtResponse, resEntity.getBody());

	}

	/**
	 * @Test_Search
	 */
	@Test
	public void testSearch() {
		/**
		 * @DB
		 */
		MarriageDocTypeResponse mdtResponse = null;
		try {
			mdtResponse = getResponse("org/egov/mr/service/marriageDocumentTypeSearchResponseAsDB.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		List<MarriageDocumentType> marriageDocumentTypes = getMarriageDocumentTypesForSearch();

		mdtSearchCriteria = MarriageDocumentTypeSearchCriteria.builder().applicationType("REISSUE")
				.tenantId("ap.kurnool").build();
		when(marriageDocumentTypeRepository.search(mdtSearchCriteria)).thenReturn(marriageDocumentTypes);

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setTs(Long.valueOf("987456321"));
		ResponseEntity<?> resEntity = marriageDocumentTypeService.search(mdtSearchCriteria, requestInfo);
		assertEquals(Long.valueOf("200").toString(), resEntity.getStatusCode().toString());
		assertEquals(mdtResponse, resEntity.getBody());
	}

	/**
	 * @Test_Update
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testForUpdateAsync() {
		MarriageDocTypeResponse marriageDocTypeResponse = null;
		try {

			marriageDocTypeResponse = getMarriageDocTypeResponse(
					"org/egov/mr/service/marriageDocumentTypeListForUpdate.json");
		} catch (Exception e) {
			e.printStackTrace();

		}

		List<MarriageDocumentType> marriageDocumentTypes = getMarriageDocumentTypesForUpdate();

		MarriageDocTypeRequest marriageDocTypeRequest = new MarriageDocTypeRequest();
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setTs(Long.valueOf("987456321"));
		marriageDocTypeRequest.setRequestInfo(requestInfo);
		marriageDocTypeRequest.setMarriageDocTypes(marriageDocumentTypes);

		/* createAsync */
		when(genService.idSeqGen(Matchers.anyInt(), Matchers.anyString())).thenReturn(getIds());
		when(genService.codeSeqGen(Matchers.anyInt(), Matchers.anyString())).thenReturn(getCodes());
		when(prosManager.getCreateMarriageDocumentTypeTopicName())
				.thenReturn("kafka.topics.create.marriagedocumenttype");
		when(_kafkaTemplate.send(Matchers.anyString(), Matchers.anyObject())).thenReturn(new SendResult<>(null, null));

		/* updateAsync */
		when(marriageDocumentTypeRepository.getIds(Matchers.any(List.class))).thenReturn(getIds());
		when(_kafkaTemplate.send(Matchers.anyString(), Matchers.anyObject())).thenReturn(new SendResult<>(null, null));

		ResponseEntity<?> resEntity = marriageDocumentTypeService.updateAsync(marriageDocTypeRequest);

		assertEquals(resEntity.getBody().toString(),
				marriageDocumentTypeService.updateAsync(marriageDocTypeRequest).getBody().toString());

	}

	/**
	 * 
	 * @SEARCH_Request
	 */
	private List<MarriageDocumentType> getMarriageDocumentTypesForSearch() {
		List<MarriageDocumentType> mdts = new ArrayList<>();
		mdts.add(MarriageDocumentType.builder().id(Long.valueOf("1")).name("ADDRESSPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REGISTRATION).isActive(true).isIndividual(true).isRequired(true)
				.code("00015").proof(DocumentProof.ADDRESS_PROOF).build());
		mdts.add(MarriageDocumentType.builder().id(Long.valueOf("2")).name("ADDRESSPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REISSUE).isActive(true).isIndividual(false).isRequired(true)
				.code("00015").proof(DocumentProof.ADDRESS_PROOF).build());
		mdts.add(MarriageDocumentType.builder().id(Long.valueOf("3")).name("AGEPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REGISTRATION).isActive(true).isIndividual(true).isRequired(false)
				.code("00015").proof(DocumentProof.AGE).build());
		mdts.add(MarriageDocumentType.builder().id(Long.valueOf("4")).name("AGEPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REISSUE).isActive(false).isIndividual(false).isRequired(false)
				.code("00015").proof(DocumentProof.AGE).build());
		mdts.add(MarriageDocumentType.builder().id(Long.valueOf("5")).name("COMMONPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REGISTRATION).isActive(false).isIndividual(true).isRequired(false)
				.code("00015").proof(DocumentProof.COMMON).build());
		mdts.add(MarriageDocumentType.builder().id(Long.valueOf("6")).name("COMMONPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REISSUE).isActive(false).isIndividual(false).isRequired(true)
				.code("00015").proof(DocumentProof.COMMON).build());
		return mdts;
	}

	/**
	 * @CREATE_Request
	 */
	private List<MarriageDocumentType> getMarriageDocumentTypesForCreate() {
		List<MarriageDocumentType> mdts = new ArrayList<>();
		mdts.add(MarriageDocumentType.builder().name("ADDRESSPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REGISTRATION).isActive(true).isIndividual(true).isRequired(true)
				.code("1").proof(DocumentProof.ADDRESS_PROOF).build());
		mdts.add(MarriageDocumentType.builder().name("ADDRESSPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REISSUE).isActive(true).isIndividual(true).isRequired(true)
				.code("2").proof(DocumentProof.ADDRESS_PROOF).build());
		return mdts;
	}

	/**
	 * 
	 * @UPDATE_Request
	 */
	private List<MarriageDocumentType> getMarriageDocumentTypesForUpdate() {
		List<MarriageDocumentType> mdts = new ArrayList<>();
		mdts.add(MarriageDocumentType.builder().id(Long.valueOf("1")).name("ADDRESSPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REGISTRATION).isActive(true).isIndividual(true).isRequired(true)
				.code("00015").proof(DocumentProof.ADDRESS_PROOF).build());
		mdts.add(MarriageDocumentType.builder().id(Long.valueOf("2")).name("DOCTYPEPROOF").tenantId("ap.kurnool")
				.applicationType(ApplicationType.REGISTRATION).isActive(true).isIndividual(true).isRequired(false)
				.code("00020").proof(DocumentProof.ADDRESS_PROOF).build());
		mdts.add(MarriageDocumentType.builder().name("AGEPROOF").id(Long.valueOf("73")).tenantId("ap.kurnool")
				.applicationType(ApplicationType.REISSUE).isActive(true).isIndividual(false).isRequired(false)
				.code("00065").proof(DocumentProof.ADDRESS_PROOF).build());
		return mdts;
	}

	private List<Long> getIds() {

		List<Long> ids = new ArrayList<>();

		ids.add(Long.valueOf("1"));
		ids.add(Long.valueOf("2"));
		ids.add(Long.valueOf("73"));

		return ids;
	}

	private List<Long> getCodes() {

		List<Long> ids = new ArrayList<>();

		ids.add(Long.valueOf("1"));
		ids.add(Long.valueOf("2"));
		ids.add(Long.valueOf("73"));

		return ids;
	}

	/**
	 * @Accessing the RegistrationUnitResponse Data from the JSON
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private MarriageDocTypeResponse getResponse(String filePath) throws IOException {
		String mrJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(mrJson, MarriageDocTypeResponse.class);
	}

	private MarriageDocTypeResponse getMarriageDocTypeResponse(String filePath) throws IOException {
		String marriageDocTypeResponseJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(marriageDocTypeResponseJson, MarriageDocTypeResponse.class);
	}

}
