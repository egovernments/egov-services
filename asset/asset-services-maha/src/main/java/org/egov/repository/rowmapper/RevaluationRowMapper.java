package org.egov.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.model.AuditDetails;
import org.egov.model.Revaluation;
import org.egov.model.enums.TypeOfChange;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RevaluationRowMapper implements RowMapper<Revaluation> {

    @Override
    public Revaluation mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Revaluation revaluation = new Revaluation();

        	revaluation.setId((Long) rs.getObject("id"));
            revaluation.setTenantId(rs.getString("tenantid"));
            revaluation.setAssetId((Long) rs.getObject("assetid"));
            revaluation.setOrderDate(rs.getLong("orderdate"));
            revaluation.setOrderNumber(rs.getString("ordernumber"));
            revaluation.setCurrentCapitalizedValue(rs.getBigDecimal("currentcapitalizedvalue"));
            revaluation.setTypeOfChange(TypeOfChange.fromValue(rs.getString("typeofchange")));
            revaluation.setRevaluationAmount(rs.getBigDecimal("revaluationamount"));
            revaluation.setValueAfterRevaluation(rs.getBigDecimal("valueafterrevaluation"));
            revaluation.setRevaluationDate(rs.getLong("revaluationdate"));
            revaluation.setRevaluatedBy(rs.getString("revaluatedby"));
            revaluation.setReasonForRevaluation(rs.getString("reasonforrevaluation"));
            revaluation.setFixedAssetsWrittenOffAccount(rs.getString("fixedassetswrittenoffaccount"));
            revaluation.setFunction(rs.getString("function"));
            revaluation.setFund(rs.getString("fund"));
            revaluation.setScheme(rs.getString("scheme"));
            revaluation.setSubScheme(rs.getString("subscheme"));
            revaluation.setStatus(rs.getString("status"));
            revaluation.setRemarks(rs.getString("remarks"));

            final AuditDetails auditDetails = new AuditDetails();
            auditDetails.setCreatedBy(rs.getString("createdby"));
            auditDetails.setCreatedDate(rs.getLong("createddate"));
            auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
            auditDetails.setLastModifiedDate(rs.getLong("lastmodifieddate"));

            revaluation.setAuditDetails(auditDetails);
            revaluation.setVoucherReference(rs.getString("voucherreference"));

        return revaluation;
    }

}