package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.eis.model.Probation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ProbationTableRowMapper implements RowMapper<Probation> {

	@Override
	public Probation mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {

		Probation probation = new Probation();

		probation.setId(rs.getLong("id"));
		probation.setDesignation(rs.getLong("designationid"));
		probation.setDeclaredOn(rs.getDate("declaredon"));
		probation.setOrderNo(rs.getString("orderno"));
		probation.setOrderDate(rs.getDate("orderdate"));
		probation.setRemarks(rs.getString("remarks"));
		probation.setCreatedBy(rs.getLong("createdby"));
		probation.setCreatedDate(rs.getDate("createddate"));
		probation.setLastModifiedBy(rs.getLong("lastmodifiedby"));
		probation.setLastModifiedDate(rs.getDate("lastmodifieddate"));
		probation.setTenantId(rs.getString("tenantid"));

		return probation;

	}
}
