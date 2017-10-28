package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Opinion;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.egov.lcms.repository.builder.OpinionQueryBuilder;
import org.egov.lcms.repository.rowmapper.OpinionRowMapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
/**
 * 
 * @author Veswanth
 *
 */
@Service
public class OpinionRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private OpinionRowMapper opinionRowMapper;

	@Autowired
	OpinionQueryBuilder opinionBuilder;
	
	@Autowired
	PropertiesManager propertiesManager;

	public List<Opinion> search(OpinionSearchCriteria opinionSearch) throws Exception {

		final List<Object> preparedStatementValues = new ArrayList<Object>();

		String searchQuery = opinionBuilder.getOpinionSearchQuery(opinionSearch, preparedStatementValues);
		List<Opinion> opinions = new ArrayList<Opinion>();
		try {
			opinions = jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(), opinionRowMapper);
		} catch (Exception ex) {
			throw new CustomException(propertiesManager.getOpinionSearchErrorCode(), ex.getMessage());
		}
		return opinions;
	}
}
