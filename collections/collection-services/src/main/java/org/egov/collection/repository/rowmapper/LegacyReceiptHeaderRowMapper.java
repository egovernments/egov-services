package org.egov.collection.repository.rowmapper;




import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.collection.model.LegacyReceiptHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LegacyReceiptHeaderRowMapper implements RowMapper<LegacyReceiptHeader> {
    public static final Logger LOGGER = LoggerFactory
            .getLogger(ReceiptRowMapper.class);

    @Override
    public LegacyReceiptHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
        LegacyReceiptHeader legacyReceiptHeader = new LegacyReceiptHeader();
        legacyReceiptHeader.setId((Long) rs.getObject("lrh_id"));
        legacyReceiptHeader.setLegacyReceiptId((Long) rs.getObject("lrh_legacyreceiptid"));
        legacyReceiptHeader.setReceiptNo(rs.getString("lrh_receiptNo"));
        legacyReceiptHeader.setDepartment(rs.getString("lrh_department"));
        legacyReceiptHeader.setServiceName(rs.getString("lrh_serviceName"));
        legacyReceiptHeader.setConsumerNo(rs.getString("lrh_consumerNo"));
        legacyReceiptHeader.setConsumerName(rs.getString("lrh_consumerName"));
        if ((Double) rs.getObject("lrh_totalAmount") != null)
            legacyReceiptHeader.setTotalAmount(BigDecimal.valueOf((Double) rs.getObject("lrh_totalAmount")));
        if ((Double) rs.getObject("lrh_advanceAmount") != null)
            legacyReceiptHeader.setAdvanceAmount(BigDecimal.valueOf((Double) rs.getObject("lrh_advanceAmount")));
        if ((Double) rs.getObject("lrh_adjustmentAmount") != null)
            legacyReceiptHeader.setAdjustmentAmount(BigDecimal.valueOf((Double) rs.getObject("lrh_adjustmentAmount")));
        legacyReceiptHeader.setConsumerAddress(rs.getString("lrh_consumerAddress"));
        legacyReceiptHeader.setPayeeName(rs.getString("lrh_payeeName"));
        legacyReceiptHeader.setInstrumentType(rs.getString("lrh_instrumentType"));
        legacyReceiptHeader.setInstrumentNo(rs.getString("lrh_instrumentNo"));
        legacyReceiptHeader.setBankName(rs.getString("lrh_bankName"));
        legacyReceiptHeader.setManualreceiptnumber(rs.getString("lrh_manualreceiptnumber"));
        legacyReceiptHeader.setTenantId(rs.getString("lrh_tenantid"));
        legacyReceiptHeader.setRemarks(rs.getString("lrh_remarks"));
        legacyReceiptHeader.setReceiptDate((Long) rs.getObject("lrh_receiptDate"));
        legacyReceiptHeader.setInstrumentDate((Long) rs.getObject("lrh_instrumentDate"));
        legacyReceiptHeader.setManualreceiptDate((Long) rs.getObject("lrh_manualreceiptDate"));

        return legacyReceiptHeader;

    }
}