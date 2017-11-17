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
        return MaterialReceipt.builder()
                .id(id)
                .financialYear(financialYear)
                .mrnNumber(mrnNumber)
                .receiptType(ReceiptTypeEnum.fromValue(receiptType))
                .receiptDate(receiptDate)
                .mrnStatus(mrnStatus)
                .receiptDetails(Arrays.asList(mapMaterialReceiptDetail()))
                .auditDetails(mapAuditDetails(tenantId, createdBy, createdTime, lastModifiedBy, lastModifiedTime))
                .build();
    }
    
    private MaterialReceiptDetail mapMaterialReceiptDetail() {
        return MaterialReceiptDetail.builder()
                .receivedQty(qty)
                .mrnNumber(mrnNumber)
                .material(mapmaterial())
                .uom(mapUom())
                .acceptedQty(receivedQty)
                .unitRate(unitRate)
                .build();
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
