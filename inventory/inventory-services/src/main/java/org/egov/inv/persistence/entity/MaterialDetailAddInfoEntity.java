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

    private String tenantId;

    private String lotNo;

    private String serialNo;

    private BigDecimal manufactureDate;

    private String oldReceiptNumber;

    private BigDecimal receivedDate;

    private BigDecimal expiryDate;

    private String receiptDetailId;

    private String batchNo;


    public MaterialReceiptDetailAddnlinfo toDomain() {
        MaterialReceiptDetailAddnlinfo addnlinfo = new MaterialReceiptDetailAddnlinfo();

        return addnlinfo
                .id(id)
                .tenantId(tenantId)
                .lotNo(lotNo)
                .serialNo(serialNo)
                .manufactureDate(null != manufactureDate ? Long.valueOf(manufactureDate.toString()) : null)
                .oldReceiptNumber(oldReceiptNumber)
                .receivedDate(null != receivedDate ? Long.valueOf(receivedDate.toString()) : null)
                .expiryDate(null != expiryDate ? Long.valueOf(expiryDate.toString()) : null)
                .receiptDetailId(receiptDetailId)
                .batchNo(batchNo);
    }
}
