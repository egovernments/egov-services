package org.egov.mr.repository;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.MarriageDocumentType;
import org.egov.mr.model.enums.ApplicationType;
import org.egov.mr.model.enums.DocumentProof;
import org.egov.mr.model.enums.ServiceConfigurationKeys;
import org.egov.mr.repository.querybuilder.MarriageDocumentTypeQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageDocumentTypeRowMapper;
import org.egov.mr.service.ServiceConfigurationService;
import org.egov.mr.web.contract.MarriageDocumentTypeSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MarriageDocumentTypeRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private MarriageDocumentTypeRowMapper rowMapper;

	@Mock
	private MarriageDocumentTypeQueryBuilder marriageDocumentTypeQueryBuilder;

	@InjectMocks
	private MarriageDocumentTypeRepository marriageDocumentTypeRepository;

	@Mock
	private ServiceConfigurationService serviceConfigurationService;

	@SuppressWarnings("unchecked")
	@Test
	public void testSearch() {
		MarriageDocumentTypeSearchCriteria marriageDocumentTypeSearchCriteria = new MarriageDocumentTypeSearchCriteria();
		marriageDocumentTypeSearchCriteria.setApplicationType(ApplicationType.REGISTRATION.toString());
		marriageDocumentTypeSearchCriteria.setIsActive(true);
		marriageDocumentTypeSearchCriteria.setTenantId("ap.kurnool");

		List<MarriageDocumentType> marriageDocumentTypes = getMarriageDocumentTypes();
		when(marriageDocumentTypeQueryBuilder.getSelectQuery(any(MarriageDocumentTypeSearchCriteria.class),
				any(List.class))).thenReturn(getQueryGenerator());
		when(jdbcTemplate.query(any(String.class), Matchers.<Object[]>any(), any(MarriageDocumentTypeRowMapper.class)))
				.thenReturn(marriageDocumentTypes);
		marriageDocumentTypeRepository.search(marriageDocumentTypeSearchCriteria);

	}

	@Test
	public void testGetIds() {
		List<Long> ids = new ArrayList<>();
		ids.add(Long.valueOf("2"));
		ids.add(Long.valueOf("6"));
		when(jdbcTemplate.queryForList(any(String.class), Matchers.<Class<Long>>any())).thenReturn(ids);

		List<Long> idList = marriageDocumentTypeRepository.getIds(getMarriageDocumentTypes());
		assertTrue(idList.equals(ids));
	}

	@Test
	public void testUpdate() {
		int[] value = new int[] { 1, 2 };
		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(value);
		when(serviceConfigurationService.getServiceConfigValueByKeyAndTenantId(Matchers.any(), Matchers.anyString()))
				.thenReturn("500");
		marriageDocumentTypeRepository.update(getMarriageDocumentTypes());
	}

	@Test
	public void testCreate() {
		int[] value = new int[] { 1, 2 };
		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(value);
		when(serviceConfigurationService.getServiceConfigValueByKeyAndTenantId(Matchers.any(), Matchers.anyString()))
				.thenReturn("500");

		marriageDocumentTypeRepository.create(getMarriageDocumentTypes());
	}

	/**
	 * @Helper_Methods
	 * 
	 * @return
	 */
	private String getQueryGenerator() {
		StringBuilder query = new StringBuilder(marriageDocumentTypeQueryBuilder.BATCH_INSERT_QUERY);
		return query.toString();
	}

	private String getBASEQUERY() {
		StringBuilder query = new StringBuilder(marriageDocumentTypeQueryBuilder.BASEQUERY);
		return query.toString();
	}

	private List<MarriageDocumentType> getMarriageDocumentTypes() {

		List<MarriageDocumentType> marriageDocumentTypes = new ArrayList<>();
		MarriageDocumentType marriageDocumentType2 = new MarriageDocumentType();
		marriageDocumentType2.setId(Long.valueOf("6"));
		marriageDocumentType2.setCode("00015");
		marriageDocumentType2.setName("MarriageDocumentType");
		marriageDocumentType2.setProof(DocumentProof.ADDRESS_PROOF);
		marriageDocumentType2.setTenantId("ap.kurnool");
		marriageDocumentType2.setApplicationType(ApplicationType.REGISTRATION);
		marriageDocumentType2.setIsActive(true);
		marriageDocumentType2.setIsIndividual(true);
		marriageDocumentType2.setIsRequired(false);

		MarriageDocumentType marriageDocumentType6 = new MarriageDocumentType();
		marriageDocumentType6.setId(Long.valueOf("6"));
		marriageDocumentType6.setCode("00015");
		marriageDocumentType6.setName("MarriageDocumentType");
		marriageDocumentType6.setProof(DocumentProof.ADDRESS_PROOF);
		marriageDocumentType6.setTenantId("ap.kurnool");
		marriageDocumentType6.setApplicationType(ApplicationType.REGISTRATION);
		marriageDocumentType6.setIsActive(true);
		marriageDocumentType6.setIsIndividual(true);
		marriageDocumentType6.setIsRequired(false);

		marriageDocumentTypes.add(marriageDocumentType2);
		marriageDocumentTypes.add(marriageDocumentType6);
		return marriageDocumentTypes;
	}
}
