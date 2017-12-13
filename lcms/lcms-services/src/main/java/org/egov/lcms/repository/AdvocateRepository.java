package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.models.Agency;
import org.egov.lcms.models.PersonDetails;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.repository.builder.AdvocateBuilders;
import org.egov.lcms.repository.rowmapper.AdvocateRowMapper;
import org.egov.lcms.repository.rowmapper.AgencyRowMapper;
import org.egov.lcms.repository.rowmapper.PersonDetailRowMapper;
import org.egov.lcms.util.ConstantUtility;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AdvocateRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	AdvocateRowMapper advocateRowMapper;

	@Autowired
	PersonDetailRowMapper personDetailRowMapper;

	@Autowired
	AgencyRowMapper agencyRowMapper;

	public List<Advocate> search(AdvocateSearchCriteria advocateSearchCriteria) {

		if (advocateSearchCriteria.getPageNumber() == null || advocateSearchCriteria.getPageNumber() == 0)
			advocateSearchCriteria.setPageNumber(Integer.valueOf(propertiesManager.getDefaultPageNumber().trim()));

		if (advocateSearchCriteria.getPageSize() == null)
			advocateSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String selectQuery = AdvocateBuilders.getSearchQuery(advocateSearchCriteria, preparedStatementValues);

		List<Advocate> advocates = new ArrayList<Advocate>();
		try {
			advocates = jdbcTemplate.query(selectQuery, preparedStatementValues.toArray(), advocateRowMapper);
		} catch (final Exception exception) {
			log.info("the exception in advocate search :" + exception);
			throw new CustomException(propertiesManager.getAdvocateErrorCode(),
					propertiesManager.getAdvocateErrorMsg());
		}

		return advocates;
	}

	/**
	 * This will give the case codes for the given advocateName
	 * 
	 * @param advocateCode
	 * @return {@link String} AdvocateName
	 */
	public List<String> getcaseCodeByAdvocateCode(String advocateCode) {
		String searchQuery = AdvocateBuilders.SEARCH_CASE_CODE_BY_ADVOCATE;
		List<String> caseCodes = jdbcTemplate.queryForList(searchQuery, new Object[] { advocateCode }, String.class);
		return caseCodes;

	}

	public List<PersonDetails> getPersonalDetailsUsingCode(String tenantId, String code) {
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		String searchQuery = AdvocateBuilders.getAgencyFieldsSearchQuery(tenantId, code,
				ConstantUtility.PERSONAL_DETAILS_TABLE_NAME, Boolean.FALSE, preparedStatementValues);
		List<PersonDetails> personDetails = jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(),
				personDetailRowMapper);
		return personDetails;
	}

	public List<Advocate> getAdvocatesUsingCode(String tenantId, String code, Boolean isIndividual) {
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		String searchQuery = AdvocateBuilders.getAgencyFieldsSearchQuery(tenantId, code,
				ConstantUtility.ADVOCATE_TABLE_NAME, isIndividual, preparedStatementValues);
		List<Advocate> advocates = jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(),
				advocateRowMapper);
		return advocates;
	}

	public void delete(String code, String tenantId, String tableName) {
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		String deleteQuery = AdvocateBuilders.getDeleteQuery(code, tenantId, tableName, preparedStatementValues);
		jdbcTemplate.update(deleteQuery, preparedStatementValues.toArray());
	}

	public List<Agency> searchAgencies(String tenantId, String code, Boolean isIndividual, String advocateName,
			String agencyName, RequestInfoWrapper requestInfoWrapper) {

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agency> agencies = new ArrayList<Agency>();

		if (code != null && !code.isEmpty()) {
			if (code.contains(propertiesManager.getAgencySubStringCode()))
				isIndividual = false;

			else if (code.contains(propertiesManager.getAdvocateSubStringCode()))
				isIndividual = true;
		}

		if (isIndividual == null || !isIndividual) {
			String agencySearchQuery = AdvocateBuilders.getAgencySearchQuery(tenantId, code, isIndividual, agencyName,
					preparedStatementValues);

			agencies = jdbcTemplate.query(agencySearchQuery, preparedStatementValues.toArray(), agencyRowMapper);
			getPersonDetails(agencies);
			getAdvocates(agencies, advocateName);

		} else {
			final List<Object> advocateValues = new ArrayList<Object>();
			String advocateSearchQuery = AdvocateBuilders.getAdvocateSearchQuery(tenantId, isIndividual, advocateName,
					code, agencyName, advocateValues);
			List<Advocate> advocates = jdbcTemplate.query(advocateSearchQuery, advocateValues.toArray(),
					advocateRowMapper);
			Agency agency = new Agency();
			agency.setIsIndividual(Boolean.TRUE);
			agencies.add(agency);
			agencies.get(0).setAdvocates(advocates);

		}
		return agencies;
	}

	private void getAdvocates(List<Agency> agencies, String advocateName) {

		List<Advocate> advocates = new ArrayList<Advocate>();
		for (Agency agency : agencies) {
			final List<Object> preparedStatementValues = new ArrayList<Object>();
			String searchQuery = AdvocateBuilders.getAdvocateSearchQuery(agency.getTenantId(), agency.getIsIndividual(),
					advocateName, agency.getCode(), agency.getName(), preparedStatementValues);
			advocates = jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(), advocateRowMapper);
			agency.setAdvocates(advocates);
		}
	}

	private void getPersonDetails(List<Agency> agencies) {

		List<PersonDetails> personDetails = new ArrayList<PersonDetails>();

		for (Agency agency : agencies) {
			final List<Object> preparedStatementValues = new ArrayList<Object>();
			String searchQuery = AdvocateBuilders.getAgencyFieldsSearchQuery(agency.getTenantId(), agency.getCode(),
					ConstantUtility.PERSONAL_DETAILS_TABLE_NAME, Boolean.FALSE, preparedStatementValues);
			personDetails = jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(), personDetailRowMapper);
			agency.setPersonDetails(personDetails);
		}
	}
}
