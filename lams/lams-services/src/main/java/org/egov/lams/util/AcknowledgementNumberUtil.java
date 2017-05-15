package org.egov.lams.util;

import java.time.LocalDateTime;
import java.util.Random;
import org.egov.lams.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AcknowledgementNumberUtil {
	
	public static final Logger logger = LoggerFactory.getLogger(AcknowledgementNumberUtil.class);

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
		String suffix = Character.toString((char) k)+(char)l;
		Long resultSet = null;
		try{
			//replace it with a generic agreement repository which will take query, input object,response object
			resultSet = jdbcTemplate.queryForObject(sql, Long.class);
		}catch (Exception e) {
			logger.info("AcknowledgementNumberService : "+ e.getMessage(),e);
			throw e;
		}
		StringBuilder baseValue = new StringBuilder(String.format("%05d", resultSet));
		baseValue.append("-" + year);
		baseValue.append("-"+suffix);
		logger.info("AcknowledgementNumberService : acknoValue"+baseValue);
		return baseValue.toString().toUpperCase();
	}

}
