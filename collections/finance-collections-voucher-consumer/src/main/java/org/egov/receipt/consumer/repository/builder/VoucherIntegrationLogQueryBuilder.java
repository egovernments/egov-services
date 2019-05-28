package org.egov.receipt.consumer.repository.builder;

import java.util.UUID;

import org.egov.receipt.consumer.entity.VoucherIntegrationLog;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class VoucherIntegrationLogQueryBuilder {
	
	public static final String INSERT_VOUCHER_LOG_SQL = "INSERT INTO egf_voucher_integration_log(id, referencenumber, status, vouchernumber, TYPE, description, requestjson, tenantid, createddate)"
			+ "VALUES (:id, :referencenumber, :status, :vouchernumber, :TYPE, :description, cast(:requestjson as jsonb), :tenantid,:createddate)";
	
	public static MapSqlParameterSource getParametersForVoucherIntegartionLog(VoucherIntegrationLog log){
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", UUID.randomUUID().toString());
		sqlParameterSource.addValue("referencenumber", log.getReferenceNumber());
		sqlParameterSource.addValue("status", log.getStatus());
		sqlParameterSource.addValue("vouchernumber", log.getVoucherNumber());
		sqlParameterSource.addValue("TYPE", log.getType());
		sqlParameterSource.addValue("description", log.getDescription());
		sqlParameterSource.addValue("requestjson", log.getRequestJson());
		sqlParameterSource.addValue("tenantid", log.getTenantId());
		sqlParameterSource.addValue("createddate", log.getCreatedDate());
		return sqlParameterSource;
	}
}
