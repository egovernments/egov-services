package org.egov.collection.repository.rowmapper;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.collection.model.LegacyReceiptDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LegacyReceiptDetailRowMapper implements RowMapper<LegacyReceiptDetails> {
    public static final Logger LOGGER = LoggerFactory
            .getLogger(ReceiptRowMapper.class);

    @Override
    public LegacyReceiptDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        LegacyReceiptDetails legacyReceiptDetails = new LegacyReceiptDetails();
        legacyReceiptDetails.setId((Long) rs.getObject("lrd_id"));
        legacyReceiptDetails.setBillNo(rs.getString("lrd_billNo"));
        legacyReceiptDetails.setBillId((Long) rs.getObject("lrd_billId"));
        legacyReceiptDetails.setBillYear((Long) rs.getObject("lrd_billYear"));
        legacyReceiptDetails.setTaxId((Long) rs.getObject("lrd_taxId"));
        legacyReceiptDetails.setDescription(rs.getString("lrd_description"));
        if ((Double) rs.getObject("lrd_currDemand") != null)
            legacyReceiptDetails.setCurrDemand(BigDecimal.valueOf((Double) rs.getObject("lrd_currDemand")));
        if ((Double) rs.getObject("lrd_arrDemand") != null)
            legacyReceiptDetails.setArrDemand(BigDecimal.valueOf((Double) rs.getObject("lrd_arrDemand")));
        if ((Double) rs.getObject("lrd_currCollection") != null)
            legacyReceiptDetails.setCurrCollection(BigDecimal.valueOf((Double) rs.getObject("lrd_currCollection")));
        if ((Double) rs.getObject("lrd_arrCollection") != null)
            legacyReceiptDetails.setArrCollection(BigDecimal.valueOf((Double) rs.getObject("lrd_arrCollection")));
        if ((Double) rs.getObject("lrd_currBalance") != null)
            legacyReceiptDetails.setCurrBalance(BigDecimal.valueOf((Double) rs.getObject("lrd_currBalance")));
        if ((Double) rs.getObject("lrd_arrBalance") != null)
            legacyReceiptDetails.setArrBalance(BigDecimal.valueOf((Double) rs.getObject("lrd_arrBalance")));
        legacyReceiptDetails.setReceiptHeaderId((Long) rs.getObject("lrd_id_receipt_header"));
        legacyReceiptDetails.setTenantid(rs.getString("lrd_tenantid"));
        legacyReceiptDetails.setBillDate((Long) rs.getObject("lrd_billDate"));
        return legacyReceiptDetails;

    }
}