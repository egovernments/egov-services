package org.egov.inv.persistence.entity;

import lombok.*;
import org.egov.inv.model.MaterialReceiptDetail;
import org.egov.inv.model.MaterialReceiptDetailAddnlinfo;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialDetailAddInfoEntity {

    private String id;

    private String tenantid;

    private String lotno;

    private String serialno;

    private BigDecimal manufacturedate;

    private String oldreceiptnumber;

    private BigDecimal receiveddate;

    private BigDecimal expirydate;

    private String receiptdetailid;


    public MaterialReceiptDetailAddnlinfo toDomain() {
        MaterialReceiptDetailAddnlinfo addnlinfo = new MaterialReceiptDetailAddnlinfo();

        return addnlinfo
                .id(id)
                .tenantId(tenantid)
                .lotNo(lotno)
                .serialNo(serialno)
                .manufactureDate(null != manufacturedate ? Long.valueOf(manufacturedate.toString()) : null)
                .oldReceiptNumber(oldreceiptnumber)
                .receivedDate(null != receiveddate ? Long.valueOf(receiveddate.toString()) : null)
                .expiryDate(null != expirydate ? Long.valueOf(expirydate.toString()) : null)
                .materialReceiptDetail(buildReceiptDetails());
    }

    private MaterialReceiptDetail buildReceiptDetails() {
        MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail();

        return materialReceiptDetail.id(receiptdetailid);
    }
}
