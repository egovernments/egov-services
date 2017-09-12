package org.egov.asset.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RevaluationRowMapper implements RowMapper<Revaluation> {

    @Override
    public Revaluation mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Revaluation revaluation = new Revaluation();
        try {
            revaluation.setId((Long) rs.getObject("id"));
            revaluation.setTenantId(rs.getString("tenantid"));
            revaluation.setAssetId((Long) rs.getObject("assetid"));
            revaluation.setCurrentCapitalizedValue(BigDecimal.valueOf(rs.getDouble("currentcapitalizedvalue")));
            revaluation.setTypeOfChange(TypeOfChangeEnum.fromValue(rs.getString("typeofchange")));
            revaluation.setRevaluationAmount(BigDecimal.valueOf(rs.getDouble("revaluationamount")));
            revaluation.setValueAfterRevaluation(BigDecimal.valueOf(rs.getDouble("valueafterrevaluation")));
            revaluation.setRevaluationDate(rs.getLong("revaluationdate"));
            revaluation.setReevaluatedBy(rs.getString("reevaluatedby"));
            revaluation.setReasonForRevaluation(rs.getString("reasonforrevaluation"));
            revaluation.setFixedAssetsWrittenOffAccount(rs.getLong("fixedassetswrittenoffaccount"));
            revaluation.setFunction(rs.getLong("function"));
            revaluation.setFund(rs.getLong("fund"));
            revaluation.setScheme(rs.getLong("scheme"));
            revaluation.setSubScheme(rs.getLong("subscheme"));
            revaluation.setComments(rs.getString("comments"));
            revaluation.setStatus(rs.getString("status"));

            final AuditDetails auditDetails = new AuditDetails();
            auditDetails.setCreatedBy(rs.getString("createdby"));
            auditDetails.setCreatedDate(rs.getLong("createddate"));
            auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
            auditDetails.setLastModifiedDate(rs.getLong("lastmodifieddate"));

            revaluation.setAuditDetails(auditDetails);
            revaluation.setVoucherReference(rs.getString("voucherreference"));

        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        return revaluation;
    }

}
