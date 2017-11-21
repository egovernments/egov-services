package org.egov.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CurrentValAssetidRowMapper implements RowMapper<Long>{
	@Override
	public Long mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		return  (Long) rs.getObject("assetid");
	}


}
