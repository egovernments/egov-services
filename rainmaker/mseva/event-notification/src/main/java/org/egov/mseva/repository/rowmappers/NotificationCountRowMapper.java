package org.egov.mseva.repository.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

@Service
public class NotificationCountRowMapper implements ResultSetExtractor <Long>  {

	@Override
	public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
		Long count = 0l;
		while(rs.next()) {
			count = rs.getLong("count");
		}
		return count;
	}
	

}
