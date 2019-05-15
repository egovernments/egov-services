package org.egov.receipt.consumer.repository.builder;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

import org.egov.receipt.consumer.entity.VoucherIntegrationLog;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class VoucherIntegrationLogQueryBuilder {
	
	public static final String INSERT_VOUCHER_LOG_SQL = "INSERT INTO egf_voucher_integration_log(id, referencenumber, status, vouchernumber, TYPE, description, requestjson, tenantid)"
			+ "VALUES (:id, :referencenumber, :status, :vouchernumber, :TYPE, :description, :requestjson, :tenantid)";
	
	public static MapSqlParameterSource getParametersForVoucherIntegartionLog(VoucherIntegrationLog log){
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", UUID.randomUUID().toString());
		sqlParameterSource.addValue("referencenumber", log.getReferenceNumber());
		sqlParameterSource.addValue("status", log.getStatus());
		sqlParameterSource.addValue("vouchernumber", log.getVoucherNumber());
		sqlParameterSource.addValue("TYPE", log.getType());
		sqlParameterSource.addValue("description", log.getDescription());
		sqlParameterSource.addValue("requestjson", getJsonb(log.getRequestJson()));
		sqlParameterSource.addValue("tenantid", log.getTenantId());
		return sqlParameterSource;
	}
	
	private static PGobject getJsonb(String node) {
        if (Objects.isNull(node))
            return null;

        PGobject pgObject = new PGobject();
        pgObject.setType("jsonb");
        try {
            pgObject.setValue(node);
            return pgObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
