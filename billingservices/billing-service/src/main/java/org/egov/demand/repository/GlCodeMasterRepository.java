package org.egov.demand.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.repository.querybuilder.GlCodeMasterQueryBuilder;
import org.egov.demand.repository.rowmapper.GlCodeMasterRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j

public class GlCodeMasterRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private GlCodeMasterQueryBuilder glCodeMasterQueryBuilder;
	
	@Autowired
	private GlCodeMasterRowMapper glCodeMasterRowMapper;
	
	
	public List<GlCodeMaster> findForCriteria(GlCodeMasterCriteria glCodeMasterCriteria) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String queryStr = glCodeMasterQueryBuilder.getQuery(glCodeMasterCriteria, preparedStatementValues);
		List<GlCodeMaster> glCodeMaster = null;
		try {
			log.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
			glCodeMaster = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), glCodeMasterRowMapper);
			log.info("GlCodeMasterRepository::" + glCodeMaster);
		} catch (Exception ex) {
			log.info("the exception from findforcriteria : " + ex);
		}
		return glCodeMaster;
	}


}
