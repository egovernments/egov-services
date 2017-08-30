package org.egov.collection.repository.rowmapper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    LegacyReceiptHeader legacyReceiptHeader = new LegacyReceiptHeader();
    legacyReceiptHeader.setId((Long)rs.getObject("lrh_id"));
    legacyReceiptHeader.setLegacyReceiptId((Long)rs.getObject("lrh_legacyreceiptid"));
    legacyReceiptHeader.setReceiptNo(rs.getString("lrh_receiptNo"));
    legacyReceiptHeader.setDepartment(rs.getString("lrh_department"));
    legacyReceiptHeader.setServiceName(rs.getString("lrh_serviceName"));
    legacyReceiptHeader.setConsumerNo(rs.getString("lrh_consumerNo"));
    legacyReceiptHeader.setConsumerName(rs.getString("lrh_consumerName"));
    legacyReceiptHeader.setTotalAmount((Double)rs.getObject("lrh_totalAmount"));
    legacyReceiptHeader.setAdvanceAmount((Double)rs.getObject("lrh_advanceAmount"));
    legacyReceiptHeader.setAdjustmentAmount((Double)rs.getObject("lrh_adjustmentAmount"));
    legacyReceiptHeader.setConsumerAddress(rs.getString("lrh_consumerAddress"));
    legacyReceiptHeader.setPayeeName(rs.getString("lrh_payeeName"));
    legacyReceiptHeader.setInstrumentType(rs.getString("lrh_instrumentType"));
    legacyReceiptHeader.setInstrumentNo(rs.getString("lrh_instrumentNo"));
    legacyReceiptHeader.setBankName(rs.getString("lrh_bankName"));
    legacyReceiptHeader.setManualreceiptnumber(rs.getString("lrh_manualreceiptnumber"));
    legacyReceiptHeader.setTenantId(rs.getString("lrh_tenantid"));
    legacyReceiptHeader.setRemarks(rs.getString("lrh_remarks"));
    try {
        Date date = isEmpty((Long) rs.getObject("lrh_receiptDate")) ? null
                        : sdf.parse(sdf.format((Long) rs
                                        .getObject("lrh_receiptDate")));
        legacyReceiptHeader.setReceiptDate(date);
        date = isEmpty((Long) rs.getObject("lrh_instrumentDate")) ? null : sdf
                        .parse(sdf.format((Long) rs.getObject("lrh_instrumentDate")));
        legacyReceiptHeader.setInstrumentDate(date);
        date = isEmpty((Long) rs.getObject("lrh_manualreceiptDate")) ? null : sdf
                        .parse(sdf.format((Long) rs.getObject("lrh_manualreceiptDate")));
        legacyReceiptHeader.setManualreceiptDate(date);
} catch (ParseException e) {
        LOGGER.error("Parse exception while parsing date" + e);
}
    return legacyReceiptHeader;
    
}
}
