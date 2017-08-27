package org.egov.wcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.wcms.model.Gapcode;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class GapcodeRowMapper implements RowMapper<Gapcode> {
	  @Override
	  public Gapcode mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		  final Gapcode gapcode = new Gapcode();
		  gapcode.setId(rs.getLong("id"));
		  gapcode.setCode(rs.getString("code"));
		  gapcode.setName(rs.getString("name"));
		  gapcode.setActive(rs.getBoolean("active"));
		  gapcode.setOutSideUlb(rs.getBoolean("outSideUlb"));
		  gapcode.setNoOfMonths(rs.getString("noOfLastMonths"));
		  gapcode.setLogic(rs.getString("logic"));
		  gapcode.setDescription(rs.getString("description"));
		  gapcode.setCreatedDate((Long) rs.getObject("createdDate"));
		  gapcode.setCreatedBy((Long) rs.getObject("createdBy"));
		  gapcode.setLastUpdatedDate((Long) rs.getObject("lastmodifieddate"));
		  gapcode.setLastUpdatedBy((Long) rs.getObject("lastmodifiedby"));
		  gapcode.setTenantId(rs.getString("tenantid"));
		  return gapcode;
	  }
}
