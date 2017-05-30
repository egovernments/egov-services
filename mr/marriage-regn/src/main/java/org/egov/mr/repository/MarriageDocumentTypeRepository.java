package org.egov.mr.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.MarriageDocumentType;
import org.egov.mr.repository.querybuilder.MarriageDocumentTypeQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageDocumentTypeRowMapper;
import org.egov.mr.web.contract.MarriageDocumentTypeSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MarriageDocumentTypeRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(MarriageDocumentTypeRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MarriageDocumentTypeRowMapper rowMapper;

	@Autowired
	private MarriageDocumentTypeQueryBuilder marriageDocumentTypeQueryBuilder;

	public List<MarriageDocumentType> search(MarriageDocumentTypeSearchCriteria marriageDocumentTypeSearchCriteria) {
		List<Object> preparedStatementValues = new ArrayList<>();
		// Getting Query
		List<MarriageDocumentType> marriageDocumentTypesList = jdbcTemplate.query(marriageDocumentTypeQueryBuilder
				.getSelectQuery(marriageDocumentTypeSearchCriteria, preparedStatementValues),
				preparedStatementValues.toArray(), rowMapper);
		LOGGER.info(marriageDocumentTypesList.toString());
		return marriageDocumentTypesList;
	}
}
