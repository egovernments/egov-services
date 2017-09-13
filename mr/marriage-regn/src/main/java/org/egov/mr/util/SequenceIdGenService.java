package org.egov.mr.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SequenceIdGenService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * @SequenceIdGeneration
	 * 
	 * @param rsSize
	 * @param seqName
	 * @return
	 */
	public List<Long> idSeqGen(int rsSize, String seqName) {

		String query = "SELECT NEXTVAL('" + seqName + "') FROM GENERATE_SERIES(1,?)";
		List<Long> ids = null;
		try {
			ids = jdbcTemplate.queryForList(query, new Object[] { rsSize }, Long.class);
			log.debug("Generated Ids:: " + ids + " for Sequence: " + seqName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}

	/**
	 * @SequenceCodeGeneration
	 * 
	 * @param rsSize
	 * @param seqName
	 * @return
	 */
	public List<Long> codeSeqGen(int rsSize, String seqName) {

		String query = "SELECT NEXTVAL('" + seqName + "') FROM GENERATE_SERIES(1,?)";
		List<Long> ids = null;
		try {
			ids = jdbcTemplate.queryForList(query, new Object[] { rsSize }, Long.class);
			log.debug("Generated Ids:: " + ids + " for Sequence: " + seqName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}

	public List<String> getIds(int rsSize, String seqName) {

		String query = "SELECT NEXTVAL('" + seqName + "') FROM GENERATE_SERIES(1,?)";
		List<String> ids = null;
		try {
			ids = jdbcTemplate.queryForList(query, new Object[] { rsSize }, String.class);
			log.debug("Generated Ids:: " + ids + " for Sequence: " + seqName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}
}
