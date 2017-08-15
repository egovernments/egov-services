package org.egov.collection.repository.rowmapper;

import lombok.EqualsAndHashCode;
import org.egov.collection.model.ReceiptDetail;
import org.egov.collection.model.ReceiptHeader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@EqualsAndHashCode
@Component
public class ReceiptDetaiRowMapper implements RowMapper<ReceiptDetail> {
    @Override
    public ReceiptDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReceiptDetail receiptDetail = new ReceiptDetail();
        receiptDetail.setId((Long) rs.getObject("rd_id"));
        receiptDetail.setChartOfAccount(rs.getString("rd_chartOfAccount"));
        receiptDetail.setDramount((Double) rs.getObject("rd_dramount"));
        receiptDetail.setActualcramountToBePaid((Double) rs.getObject("rd_actualcramountToBePaid"));
        receiptDetail.setCramount((Double) rs.getObject("rd_cramount"));
        receiptDetail.setOrdernumber((Long) rs.getObject("rd_ordernumber"));
        receiptDetail.setDescription(rs.getString("rd_description"));
        receiptDetail.setIsActualDemand((Boolean) rs.getObject("rd_isActualDemand"));
        receiptDetail.setFinancialYear(rs.getString("rd_financialYear"));
        receiptDetail.setPurpose(rs.getString("rd_purpose"));
        receiptDetail.setTenantId(rs.getString("rd_tenantId"));
        ReceiptHeader header = new ReceiptHeader();
        header.setId((Long) rs.getObject("rh_id"));
        receiptDetail.setReceiptHeader(header);
        return receiptDetail;
    }
}
