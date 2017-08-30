package org.egov.collection.repository.rowmapper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    LegacyReceiptDetails legacyReceiptDetails = new LegacyReceiptDetails();
    legacyReceiptDetails.setId((Long)rs.getObject("lrd_id"));
    legacyReceiptDetails.setBillNo(rs.getString("lrd_billNo"));
    legacyReceiptDetails.setBillId((Long)rs.getObject("lrd_billId"));
    legacyReceiptDetails.setBillYear((Long)rs.getObject("lrd_billYear"));
    legacyReceiptDetails.setTaxId((Long)rs.getObject("lrd_taxId"));
    legacyReceiptDetails.setDescription(rs.getString("lrd_description"));
    legacyReceiptDetails.setCurrDemand((Double)rs.getObject("lrd_currDemand"));
    legacyReceiptDetails.setArrDemand((Double)rs.getObject("lrd_arrDemand"));
    legacyReceiptDetails.setCurrCollection((Double)rs.getObject("lrd_currCollection"));
    legacyReceiptDetails.setArrCollection((Double)rs.getObject("lrd_arrCollection"));
    legacyReceiptDetails.setCurrBalance((Double)rs.getObject("lrd_currBalance"));
    legacyReceiptDetails.setArrBalance((Double)rs.getObject("lrd_arrBalance"));
    legacyReceiptDetails.setId_receipt_header((Long)rs.getObject("lrd_id_receipt_header"));
    legacyReceiptDetails.setTenantid(rs.getString("lrd_tenantid"));
    try {
        Date date = isEmpty((Long) rs.getObject("lrd_billDate")) ? null
                        : sdf.parse(sdf.format((Long) rs
                                        .getObject("lrd_billDate")));
        legacyReceiptDetails.setBillDate(date);
} catch (ParseException e) {
        LOGGER.error("Parse exception while parsing date" + e);
}
    return legacyReceiptDetails;

}
}
