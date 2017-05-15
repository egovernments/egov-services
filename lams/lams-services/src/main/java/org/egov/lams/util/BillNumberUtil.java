package org.egov.lams.util;

import org.egov.lams.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BillNumberUtil {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public String generateBillNumber() {

		String billNumberSequence = propertiesManager.getBillNumberSequence();
		String lamsBillNumberPrefix = propertiesManager.getLamsBillNumberPrefix();
		String sql = "SELECT nextval('" + billNumberSequence + "')";

		StringBuilder billNumber = new StringBuilder(lamsBillNumberPrefix);
		String resultSet = jdbcTemplate.queryForObject(sql, String.class);
		billNumber.append(String.format("%04d", Integer.valueOf(resultSet)));
		System.err.println(billNumber.toString());
		return billNumber.toString().toUpperCase();
	}

}
