package org.egov.wcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.wcms.model.MeterCost;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MeterCostRowMapper implements RowMapper<MeterCost> {
	@Override
	public MeterCost mapRow(ResultSet rs, int rowNum) throws SQLException {
		MeterCost meterCost = new MeterCost();
		meterCost.setId(rs.getLong("wmc_id"));
		meterCost.setCode(rs.getString("wmc_code"));
		meterCost.setMeterMake(rs.getString("wmc_metermake"));
		meterCost.setActive((Boolean) rs.getObject("wmc_active"));
		meterCost.setAmount((Double) rs.getObject("wmc_amount"));
		meterCost.setPipeSizeId((Long) rs.getObject("wmc_pipesizeid"));
		meterCost.setCreatedBy((Long) rs.getObject("wmc_createdby"));
		meterCost.setLastModifiedBy((Long) rs.getObject("wmc_lastmodifiedby"));
		meterCost.setTenantId(rs.getString("wmc_tenantid"));
		meterCost.setCreatedDate((Long) rs.getObject("wmc_createddate"));
		meterCost.setLastModifiedDate((Long) rs.getObject("wmc_lastmodifieddate"));
		return meterCost;
	}

}
