package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.egov.eis.model.Probation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ProbationTableRowMapper implements RowMapper<Probation> {

	@Override
	public Probation mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Probation probation = new Probation();

		probation.setId(rs.getLong("id"));
		probation.setDesignation(rs.getLong("designationid"));
		probation.setOrderNo(rs.getString("orderno"));
		probation.setRemarks(rs.getString("remarks"));
		probation.setCreatedBy(rs.getLong("createdby"));
		probation.setLastModifiedBy(rs.getLong("lastmodifiedby"));
		probation.setTenantId(rs.getString("tenantid"));
		try {
			probation.setDeclaredOn(sdf.parse(sdf.format(rs.getDate("declaredon"))));
			probation.setOrderDate(sdf.parse(sdf.format(rs.getDate("orderdate"))));
			probation.setCreatedDate(sdf.parse(sdf.format(rs.getDate("createddate"))));
			probation.setLastModifiedDate(sdf.parse(sdf.format(rs.getDate("lastmodifieddate"))));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}

		return probation;
	}
}
