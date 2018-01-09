package org.egov.inv.persistence.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceipt.ReceiptTypeEnum;
import org.egov.inv.model.MaterialReceiptDetail;
import org.egov.inv.model.Uom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private BigDecimal userQty;
    private BigDecimal acceptedQty;
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
                .receiptType(null != ReceiptTypeEnum.fromValue(receiptType) ? ReceiptTypeEnum.fromValue(receiptType) : null)
                .receiptDate(receiptDate)
                .mrnStatus(null != MaterialReceipt.MrnStatusEnum.fromValue(mrnStatus) ? MaterialReceipt.MrnStatusEnum.fromValue(mrnStatus) : null)
                .receiptDetails(Arrays.asList(mapMaterialReceiptDetail()))
                .auditDetails(mapAuditDetails(tenantId, createdBy, createdTime, lastModifiedBy, lastModifiedTime));
    }
    
    public OpeningBalanceEntity toEntity(MaterialReceipt materialReceipt){
        return OpeningBalanceEntity.builder()
        		.id(materialReceipt.getId())
        		.financialYear(materialReceipt.getFinancialYear())
        		.createdBy(materialReceipt.getAuditDetails().getCreatedBy())
                .createdTime(materialReceipt.getAuditDetails().getCreatedTime())
                .mrnNumber(materialReceipt.getMrnNumber())
                .receiptDate(materialReceipt.getReceiptDate())
        		.build();

    }
    
    public OpeningBalanceEntity toEntity(MaterialReceiptDetail materialReceiptDetail){
        return OpeningBalanceEntity.builder()
        		.id(materialReceiptDetail.getId())
        		.receivedQty(materialReceiptDetail.getAcceptedQty())
        		.userQty(materialReceiptDetail.getUserReceivedQty())
        		.receivedQty(materialReceiptDetail.getReceivedQty())
                .unitRate(materialReceiptDetail.getUnitRate())
        		.build();
    }
    
    private MaterialReceiptDetail mapMaterialReceiptDetail() {
        MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail();
        return materialReceiptDetail.
                receivedQty(qty)
                .material(mapmaterial())
                .uom(mapUom())
                .userReceivedQty(userQty)
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
