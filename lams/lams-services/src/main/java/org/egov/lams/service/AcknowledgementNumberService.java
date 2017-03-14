package org.egov.lams.service;

import java.time.LocalDateTime;
import java.util.Random;
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

		String ackNumberSeq = propertiesManager.getAcknowledgementNumberSequence();
		String sql = "SELECT nextval('" + ackNumberSeq + "')";

		LocalDateTime localDateTime = LocalDateTime.now();
		Integer year = localDateTime.getYear();
		
		Random random = new Random();
		int k=random.nextInt(25)+65;
		int l = random.nextInt(25)+65;
		String suffix = new String((char)k+""+(char)l);
	
		String resultSet = jdbcTemplate.queryForObject(sql, String.class);
		StringBuilder baseValue = new StringBuilder(String.format("%05d", resultSet));
		baseValue.append("-" + year);
		baseValue.append(suffix);
		
		return baseValue.toString();
	}

}
