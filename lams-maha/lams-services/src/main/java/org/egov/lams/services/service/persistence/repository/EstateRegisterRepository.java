package org.egov.lams.services.service.persistence.repository;

import java.util.List;

import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.EstateSearchCriteria;
import org.egov.lams.services.service.persistence.queryBuilder.EstateRegisterQueryBuilder;
import org.egov.lams.services.service.repository.rowmapper.EstateRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class EstateRegisterRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EstateRegisterQueryBuilder estateQueryBuilder;
	
	@Autowired
	private EstateRowMapper estateRowMapper;
	
	public List<EstateRegister> findForCriteria(EstateSearchCriteria estateSearchCriteria) {
		String queryStr = estateQueryBuilder.getQuery(estateSearchCriteria);
		log.info("query::"+queryStr);
		return jdbcTemplate.query(queryStr, estateRowMapper);
	}
}
