package org.egov.lams.services.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SequenceGenUtil {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<String> getIds(int rsSize, String sequenceName) {

		String demandIdQuery = "SELECT NEXTVAL('" + sequenceName + "') FROM GENERATE_SERIES(1,?)";
		List<String> idList = null;
		try {
			idList = jdbcTemplate.queryForList(demandIdQuery, new Object[] { rsSize }, String.class);
		} catch (Exception e) {
			log.error("the exception from demand ID gen : " + e);
		}
		return idList;
		/*String demandIdQuery = DemandQueryBuilder.SEQ_EGBS_QUERY.replace("sequencename",
		sequenceName);*/
	}
}
