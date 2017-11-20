package org.egov.inv.persistence.entity;

import lombok.*;
import org.egov.inv.model.*;
import org.egov.inv.model.MaterialReceipt.ReceiptTypeEnum;

import java.math.BigDecimal;
import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningBalanceEntity {

    private String id;
    private String financialYear;
    private String storeName;
    private String materialcode;
    private String mrnStatus;
    private String receiptType;
    private Long receiptDate;
    private String uom;
    private BigDecimal receivedQty;
    private String mrnNumber;
    private BigDecimal qty;
    private BigDecimal unitRate;
    private BigDecimal totalamount;
    private String remarks;
    private String tenantId;
    private String lastModifiedBy;
    private Long createdTime;
    private String createdBy;
    private Long lastModifiedTime;

    public MaterialReceipt toDomain() {
        MaterialReceipt materialReceipt = new MaterialReceipt();
        return materialReceipt.id(id)
                .financialYear(financialYear)
                .mrnNumber(mrnNumber)
                .receiptType(ReceiptTypeEnum.fromValue(receiptType))
                .receiptDate(receiptDate)
                .mrnStatus(null != MaterialReceipt.MrnStatusEnum.fromValue(mrnStatus) ? MaterialReceipt.MrnStatusEnum.fromValue(mrnStatus) : null)             
                .receiptDetails(Arrays.asList(mapMaterialReceiptDetail()))
                .auditDetails(mapAuditDetails(tenantId, createdBy, createdTime, lastModifiedBy, lastModifiedTime));
    }

    private MaterialReceiptDetail mapMaterialReceiptDetail() {
        MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail();
        return materialReceiptDetail.
                receivedQty(qty)
                //.mrnNumber(mrnNumber)
                .material(mapmaterial())
                .uom(mapUom())
                .acceptedQty(receivedQty)
                .unitRate(unitRate);
    }


    private Uom mapUom() {
        return Uom.builder()
                .code(uom)
                .build();
    }

    private Material mapmaterial() {
        return Material.builder()
                .code(materialcode)
                .build();
    }


    private AuditDetails mapAuditDetails(String tenantId, String createdBy, Long createdTime, String lastModifiedBy, Long lastModifiedTime) {
        return AuditDetails.builder()
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedTime(lastModifiedTime)
                .build();
    }

}
