package org.egov.asset.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.repository.rowmapper.CurrentValueRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CurrentValueRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<AssetCurrentValue> getCurrentValues(final Set<Long> assetIds, final String tenantId) {

		String idString = getIdQueryForStrings(assetIds);

		String sql = "SELECT ungroupedvalue.* FROM egasset_current_value ungroupedvalue "
				+ "INNER join (SELECT assetid,max(createdtime) AS createdtime FROM egasset_current_value "
				+ "WHERE tenantid='" + tenantId + "' and assetid IN " + idString + " group by assetid) groupedvalue "
				+ "ON ungroupedvalue.assetid=groupedvalue.assetid and ungroupedvalue.createdtime=groupedvalue.createdtime "
				+ "where ungroupedvalue.tenantid='" + tenantId + "' and ungroupedvalue.assetid IN " + idString;

		log.info("the query for fetching currentValues : " + sql);
		return jdbcTemplate.query(sql, new CurrentValueRowMapper());
	}

	public void create(List<AssetCurrentValue> assetCurrentValues) {
		
		
		String sql = "INSERT INTO egasset_current_value (id,assetid,tenantid,assettrantype,currentamount,createdby,"
				+ "createdtime,lastModifiedby,lastModifiedtime) VALUES (?,?,?,?,?,?,?,?,?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int rowNum) throws SQLException {
				
				AssetCurrentValue assetCurrentValue = assetCurrentValues.get(rowNum);
				AuditDetails auditDetails = assetCurrentValue.getAuditDetails();
				ps.setLong(1,assetCurrentValue.getId());
				ps.setLong(2,assetCurrentValue.getAssetId());
				ps.setString(3,assetCurrentValue.getTenantId());
				ps.setString(4, assetCurrentValue.getAssetTranType());
				ps.setBigDecimal(5,assetCurrentValue.getCurrentAmount());
				ps.setString(6, auditDetails.getCreatedBy());
				ps.setLong(7, auditDetails.getCreatedDate());
				ps.setString(8, auditDetails.getLastModifiedBy());
				ps.setLong(9, auditDetails.getLastModifiedDate());
			}
			
			@Override
			public int getBatchSize() {
					return assetCurrentValues.size();
			}
		});
	}
	
	private static String getIdQueryForStrings(Set<Long> idList) {
		
		StringBuilder query = new StringBuilder("(");
		if (!idList.isEmpty()) {
			Long[] list = idList.toArray(new Long[idList.size()]);
			query.append(list[0]);
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + list[i]);
			}
		}
		return query.append(")").toString();
	}
}
