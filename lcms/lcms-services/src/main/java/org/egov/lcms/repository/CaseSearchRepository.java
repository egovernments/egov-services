package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.models.HearingDetails;
import org.egov.lcms.models.ParaWiseComment;
import org.egov.lcms.repository.builder.CaseBuilder;
import org.egov.lcms.repository.rowmapper.CaseRowMapper;
import org.egov.lcms.repository.rowmapper.HearingDetailsRowMapper;
import org.egov.lcms.repository.rowmapper.ParaWiseRowMapper;
import org.egov.lcms.utility.ConstantUtility;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Prasad
 *
 */
@Repository
@Slf4j
public class CaseSearchRepository {

	@Autowired
	CaseBuilder caseBuilder;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CaseRowMapper caseRowMapper;

	@Autowired
	ParaWiseRowMapper paraWiseRowMapper;

	@Autowired
	HearingDetailsRowMapper hearingDetailsRowMapper;

	@Autowired
	PropertiesManager propertiesManager;

	/**
	 * This will search the cases based on the given casesearchCriteria
	 * 
	 * @param caseSearchCriteria
	 * @return
	 */
	public List<Case> searchCases(CaseSearchCriteria caseSearchCriteria) {

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.getQuery(caseSearchCriteria, preparedStatementValues);

		List<Case> cases = new ArrayList<Case>();
		try {
			cases = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), caseRowMapper);
		} catch (final Exception exception) {
			log.info("the exception in case search :" + exception);
			throw new CustomException(propertiesManager.getCaseResponseErrorCode(), exception.getMessage());
		}

		List<ParaWiseComment> paraWiseComments = new ArrayList<>();
		List<HearingDetails> hearingDetails = new ArrayList<>();
		for (Case casee : cases) {
			paraWiseComments = searchParaWiseComments(casee);
			hearingDetails = searchHearingDetails(casee);

			casee.setParawiseComments(paraWiseComments);
			casee.setHearingDetails(hearingDetails);
		}

		return cases;
	}

	private List<ParaWiseComment> searchParaWiseComments(Case casee) {
		List<ParaWiseComment> paraWiseComments = new ArrayList<ParaWiseComment>();
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.searchByCaseCodeQuery(casee, ConstantUtility.PARAWISE_COMMENTS_TABLE_NAME,
				preparedStatementValues);

		try {
			paraWiseComments = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), paraWiseRowMapper);
		} catch (Exception ex) {
			throw new CustomException(propertiesManager.getParaWiseResponseErrorCode(), ex.getMessage());
		}
		return paraWiseComments;
	}

	private List<HearingDetails> searchHearingDetails(Case casee) {
		List<HearingDetails> hearingDetails = new ArrayList<HearingDetails>();
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.searchByCaseCodeQuery(casee, ConstantUtility.HEARING_DETAILS_TABLE_NAME,
				preparedStatementValues);

		try {
			hearingDetails = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), hearingDetailsRowMapper);
		} catch (Exception ex) {
			throw new CustomException(propertiesManager.getHearingDetailsResponseErrorCode(), ex.getMessage());
		}
		return hearingDetails;
	}
}
