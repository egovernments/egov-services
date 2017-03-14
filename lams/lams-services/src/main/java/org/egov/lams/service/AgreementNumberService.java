package org.egov.lams.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AgreementNumberService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public String generateAgrementNumber() {

		String ulbNumber = propertiesManager.getUlbNumber();
		String agreementNumberSequence = propertiesManager.getAgreementNumberSequence();
		String lamsPrefix = propertiesManager.getLamsPrefix();
		String sql = "SELECT nextval('" + agreementNumberSequence + "')";

		StringBuilder baseValue = new StringBuilder(lamsPrefix);

		LocalDateTime localDateTime = LocalDateTime.now();
		Integer year = localDateTime.getYear();
		baseValue.append('-' + year.toString().substring(2, 4));
		baseValue.append('-' + ulbNumber);
		String resultSet = jdbcTemplate.queryForObject(sql, String.class);
		baseValue.append(String.format("%04d", resultSet));
		System.err.println(baseValue.toString());
		return baseValue.toString().toUpperCase();
	}

}
