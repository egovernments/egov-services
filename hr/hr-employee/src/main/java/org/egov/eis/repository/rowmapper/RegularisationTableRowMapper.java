package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.egov.eis.model.Regularisation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RegularisationTableRowMapper implements RowMapper<Regularisation> {

	@Override
	public Regularisation mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Regularisation regularisation = new Regularisation();
		regularisation.setId(rs.getLong("id"));
		regularisation.setDesignation(rs.getLong("designationid"));
		regularisation.setDeclaredOn(rs.getDate("declaredon"));
		regularisation.setOrderNo(rs.getString("orderno"));
		regularisation.setRemarks(rs.getString("remarks"));
		regularisation.setCreatedBy(rs.getLong("createdby"));
		regularisation.setLastModifiedBy(rs.getLong("lastmodifiedby"));
		regularisation.setTenantId(rs.getString("tenantid"));
		try {
			regularisation.setOrderDate(sdf.parse(sdf.format(rs.getDate("orderdate"))));
			regularisation.setCreatedDate(sdf.parse(sdf.format(rs.getDate("createddate"))));
			regularisation.setLastModifiedDate(sdf.parse(sdf.format(rs.getDate("lastmodifieddate"))));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}

		return regularisation;
	}
}
