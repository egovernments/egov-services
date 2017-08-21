package org.egov.asset.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.enums.TransactionType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DisposalRowMapper implements RowMapper<Disposal> {

    @Override
    public Disposal mapRow(final ResultSet rs, final int rowNum) throws SQLException {

        final Disposal disposal = new Disposal();
        disposal.setId((Long) rs.getObject("id"));
        disposal.setTenantId(rs.getString("tenantid"));
        disposal.setAssetId(rs.getLong("assetid"));
        disposal.setBuyerName(rs.getString("buyername"));
        disposal.setBuyerAddress(rs.getString("buyeraddress"));
        disposal.setDisposalReason(rs.getString("disposalreason"));
        disposal.setDisposalDate((Long) rs.getObject("disposaldate"));
        disposal.setPanCardNumber(rs.getString("pancardnumber"));
        disposal.setAadharCardNumber(rs.getString("aadharcardnumber"));

        disposal.setAssetCurrentValue(BigDecimal.valueOf(rs.getDouble("assetcurrentvalue")));
        disposal.setSaleValue(BigDecimal.valueOf(rs.getDouble("salevalue")));
        disposal.setTransactionType(TransactionType.fromValue(rs.getString("transactiontype")));
        disposal.setAssetSaleAccount((Long) rs.getObject("id"));

        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(rs.getString("createdby"));
        auditDetails.setCreatedDate(rs.getLong("createddate"));
        auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
        auditDetails.setLastModifiedDate(rs.getLong("lastmodifieddate"));
        disposal.setAuditDetails(auditDetails);

        disposal.setProfitLossVoucherReference(rs.getString("profitlossvoucherreference"));

        return disposal;
    }

}
