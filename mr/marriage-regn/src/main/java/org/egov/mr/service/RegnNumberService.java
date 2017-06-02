package org.egov.mr.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.egov.mr.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegnNumberService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public String generateRegnNumber() {

		String regnNumberSeq = propertiesManager.getRegnNumberSequence();
		String sql = "SELECT nextval('" + regnNumberSeq + "')";

		LocalDateTime localDateTime = LocalDateTime.now();
		Integer year = localDateTime.getYear();
		
		Random random = new Random();
		int k=random.nextInt(25)+65;
		int l = random.nextInt(25)+65;
		String suffix = new String((char)k+""+(char)l);
	
		int resultSet = jdbcTemplate.queryForObject(sql, Integer.class);
		StringBuilder baseValue = new StringBuilder(String.format("%03d", resultSet));
		baseValue.append("-" + Integer.toString(year));
		baseValue.append(suffix);

		return baseValue.toString();
	}
}
