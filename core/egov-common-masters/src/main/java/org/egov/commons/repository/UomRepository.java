package org.egov.commons.repository;

import java.util.List;

import org.egov.commons.model.Uom;
import org.egov.commons.repository.rowmapper.UomRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UomRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Uom> search(){
		String query="SELECT * from eg_uom";
		return jdbcTemplate.query(query, new UomRowMapper());
	}
}
