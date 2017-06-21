package org.egov.demand.repository;

import java.util.List;

import org.egov.demand.repository.rowmapper.BillIdRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class GenIdServiceUtil {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private BillIdRowMapper billIdRowMapper;
	
	public List<String> getNextValue(String seqName,int noOfNextSeq){
		String query ="SELECT nextval('"+seqName+"') AS id" +
										" from generate_series(1, ?)";
	    return jdbcTemplate.query(query, new Object[]{noOfNextSeq}, billIdRowMapper);
	}

}
