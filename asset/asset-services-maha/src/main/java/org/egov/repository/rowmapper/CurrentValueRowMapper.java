package org.egov.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.model.CurrentValue;
import org.egov.model.AuditDetails;
import org.egov.model.enums.TransactionType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CurrentValueRowMapper implements RowMapper<CurrentValue>{

  @Override
    public CurrentValue mapRow(final ResultSet rs, final int rowNum) throws SQLException {

      final AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
                .createdDate(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
                .lastModifiedDate(rs.getLong("lastmodifiedtime")).build();

        return CurrentValue.builder().id((Long) rs.getObject("id")).auditDetails(auditDetails)
                .assetId(rs.getLong("assetid")).transactionDate(rs.getLong("transactiondate")).assetTranType(TransactionType.fromValue(rs.getString("assetTranType")))
                .tenantId(rs.getString("tenantId")).currentAmount(rs.getBigDecimal("currentamount")).build();
    }
}
