package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.eis.model.Regularisation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


@Component
public class RegularisationTableRowMapper implements RowMapper<Regularisation> {

		@Override
		public Regularisation mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
			Regularisation regularisation = new Regularisation();
			regularisation.setId(rs.getLong("id"));
			regularisation.setDesignation(rs.getLong("designationid"));
			regularisation.setDeclaredOn(rs.getDate("declaredon"));
			regularisation.setOrderNo(rs.getString("orderno"));
			regularisation.setOrderDate(rs.getDate("orderdate"));
			regularisation.setRemarks(rs.getString("remarks"));
			regularisation.setCreatedBy(rs.getLong("createdby"));
			regularisation.setCreatedDate(rs.getDate("createddate"));
			regularisation.setLastModifiedBy(rs.getLong("lastmodifiedby"));
			regularisation.setLastModifiedDate(rs.getDate("lastmodifieddate"));
			regularisation.setTenantId(rs.getString("tenantid"));
			
			return regularisation;
}
}
