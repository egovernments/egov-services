package org.egov.inv.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SequenceCreator {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Transactional(propagation=Propagation.REQUIRES_NEW,noRollbackForClassName={"BadSqlGrammarException","PSQLException","UncategorizedSQLException"})
	public void createSequence(String seqName) {
		try {
			String seqQuery = "create sequence seq_" + seqName + "";
			jdbcTemplate.execute(seqQuery);
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			e.printStackTrace();
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW,noRollbackForClassName={"BadSqlGrammarException","PSQLException","UncategorizedSQLException"})
	public String getNextSequence(String seqName) {
		String id=null;
		try {
			String seqQuery = "select nextval('seq_" + seqName + "') ";
			id = jdbcTemplate.queryForObject(seqQuery, String.class);
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			e.printStackTrace();
		 
		}
		return id;
	}


}
