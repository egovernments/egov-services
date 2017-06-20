package org.egov.demand.service;

import java.util.List;

import org.egov.demand.config.ApplicationProperties;
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

	@Autowired
	private ApplicationProperties applicationProperties;

	public List<Long> getdemandIds(int demandsSize) {
		String demandIdQuery = DemandQueryBuilder.SEQ_EGBS_QUERY.replace("sequencename",
				applicationProperties.getDemandSeqName());
		Object[] preparedStatementValues = { demandsSize };
		List<Long> demandIdList = null;
		try {
			demandIdList = jdbcTemplate.queryForList(demandIdQuery, preparedStatementValues, Long.class);
		} catch (Exception e) {
			log.error("the exception from demand ID gen : " + e);
			throw e;
		}
		return demandIdList;
	}

	public List<Long> getdemandDetailIds(int demandDetailsSize) {

		String demandDetailIdQuery = DemandQueryBuilder.SEQ_EGBS_QUERY.replace("sequencename",
				applicationProperties.getDemandDetailSeqName());
		Object[] preparedStatementValues = { demandDetailsSize };
		List<Long> demandDetailIdList = null;
		try {
			demandDetailIdList = jdbcTemplate.queryForList(demandDetailIdQuery, preparedStatementValues, Long.class);
		} catch (Exception e) {
			log.error("the exception from demand Detail ID gen : " + e);
			throw e;
		}
		return demandDetailIdList;
	}
}
