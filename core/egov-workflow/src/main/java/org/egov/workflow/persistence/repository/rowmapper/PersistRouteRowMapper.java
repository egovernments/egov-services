package org.egov.workflow.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.workflow.domain.model.PersistRouter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;



@Component
public class PersistRouteRowMapper implements RowMapper<PersistRouter> {
	@Override
	public PersistRouter mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final PersistRouter persistRouter = new PersistRouter();
		persistRouter.setId(rs.getLong("id"));
		persistRouter.setBoundary(rs.getInt("bndryid"));
		persistRouter.setService(rs.getLong("complainttypeid"));
		persistRouter.setPosition(rs.getInt("position"));
		persistRouter.setTenantId(rs.getString("tenantid"));
		

		return persistRouter;
	}
}