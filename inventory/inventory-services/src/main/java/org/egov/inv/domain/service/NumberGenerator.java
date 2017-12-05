package org.egov.inv.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NumberGenerator {

	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private SequenceCreator sequenceCreator;

	public String getNextNumber(String sequenceName, Integer padding) {
		String id = "";
		sequenceName = sequenceName.replace("/", "_");
		sequenceName = sequenceName.replace("-", "_");

		id = sequenceCreator.getNextSequence(sequenceName);
		if (id == null) {
			sequenceCreator.createSequence(sequenceName);
			id = sequenceCreator.getNextSequence(sequenceName);
		}

		String s = "%0" + padding + "d";
		//String.format(s, Long.valueOf(id));
		return String.format(s, Long.valueOf(id));
	}

}
