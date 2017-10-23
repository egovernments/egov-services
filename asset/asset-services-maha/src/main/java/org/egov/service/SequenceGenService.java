package org.egov.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SequenceGenService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Long> getIds(int rsSize, String sequenceName) {

		String demandIdQuery = "SELECT NEXTVAL('" + sequenceName + "') FROM GENERATE_SERIES(1,?)";
		List<Long> idList = null;
		try {
			idList = jdbcTemplate.queryForList(demandIdQuery, new Object[] { rsSize }, Long.class);
		} catch (Exception e) {
			log.error("the exception from demand ID gen : " + e);
			throw e;
		}
		return idList;
	}
}
