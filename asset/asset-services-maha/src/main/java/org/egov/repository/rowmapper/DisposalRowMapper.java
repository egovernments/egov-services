package org.egov.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.model.AuditDetails;
import org.egov.model.Disposal;
import org.egov.model.enums.TransactionType;
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
        disposal.setOrderNumber(rs.getString("ordernumber"));
        disposal.setOrderDate(rs.getLong("orderdate"));
        disposal.setBuyerName(rs.getString("buyername"));
        disposal.setBuyerAddress(rs.getString("buyeraddress"));
        disposal.setDisposalReason(rs.getString("disposalreason"));
        disposal.setDisposalDate((Long) rs.getObject("disposaldate"));
        disposal.setPanCardNumber(rs.getString("pancardnumber"));
        disposal.setAadharCardNumber(rs.getString("aadharcardnumber"));
        disposal.setRemarks(rs.getString("remarks"));
        //disposal.setAssetCurrentValue(rs.getBigDecimal("assetcurrentvalue"));
        disposal.setAssetCurrentValue(rs.getBigDecimal("assetcurrentvalue"));
        disposal.setSaleValue(rs.getBigDecimal("salevalue"));
        disposal.setTransactionType(TransactionType.fromValue(rs.getString("transactiontype")));
        disposal.setAssetSaleAccount(rs.getString("assetsaleaccount"));
        disposal.setStatus(rs.getString("status"));
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