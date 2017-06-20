package org.egov.demand.service;

import java.util.List;

import org.egov.demand.repository.querybuilder.DemandQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SequenceGenService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<String> getIds(int rsSize,String sequenceName) {
		String demandIdQuery = DemandQueryBuilder.SEQ_EGBS_QUERY.replace("sequencename",
				sequenceName);
		Object[] preparedStatementValues = { rsSize };
		List<String> demandIdList = null;
		try {
			demandIdList = jdbcTemplate.queryForList(demandIdQuery, preparedStatementValues, String.class);
		} catch (Exception e) {
			log.error("the exception from demand ID gen : " + e);
			throw e;
		}
		return demandIdList;
	}
}
