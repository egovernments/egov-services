package org.egov.lams.service;

import java.time.LocalDateTime;

import org.egov.lams.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AcknowledgementNumberService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public String generateAcknowledgeNumber() {

		StringBuilder baseValue = new StringBuilder();

		String ackNumberSeq = propertiesManager.getAcknowledgementNumberSequence();
		String sql = "SELECT nextval('" + ackNumberSeq + "')";

		LocalDateTime localDateTime = LocalDateTime.now();
		Integer year = localDateTime.getYear();

		String resultSet = jdbcTemplate.queryForObject(sql, String.class);
		baseValue.append(String.format("%05d", resultSet));
		baseValue.append("-" + year);
		// baseValue.append();

		return baseValue.toString();
	}

}
