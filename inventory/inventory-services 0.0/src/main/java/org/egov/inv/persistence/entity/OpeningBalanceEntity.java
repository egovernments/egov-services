package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import io.swagger.model.AuditDetails;
import io.swagger.model.MaterialReceipt;
import io.swagger.model.Uom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpeningBalanceEntity {
	public static final String TABLE_NAME = "materialReceiptHeader";
	public static final String SEQUENCE_NAME = "seq_materialReceiptHeader";
	public static final String ALIAS = "materialReceiptHeader";

	private String id;

    private String storeName;

    private String materialName;

    private String materialOldCOde;

    private Long expiryDate;

    private BigDecimal openingQty;

    private BigDecimal openingRate;
    
    private String lotNo;
    
    private String receiptNo;

    private Long recieptDate;
    
    private String uomValue;
    
    private String tenantId;
    
    private String createdBy;
    
    private Long createdTime;

    private String lastModifiedBy;

    private Long lastModifiedTime;
    
  
	public OpeningBalanceEntity toEntity(MaterialReceipt materialReceipt) {
        AuditDetails auditDetails = materialReceipt.getAuditDetails();
		return OpeningBalanceEntity.builder()
				.id(materialReceipt.getId())
				.storeName(materialReceipt.getReceivingStore().getCode())
				.openingRate(materialReceipt.getReceiptDetails().get(0).getUnitRate())
				.materialName(materialReceipt.getReceiptDetails().get(0).getMaterial().getCode())
				.materialOldCOde(materialReceipt.getReceiptDetails().get(0).getMaterial().getOldCode())
				.expiryDate(materialReceipt.getReceiptDetails().get(0).getReceiptDetailsAddnInfo().get(0).getExpiryDate())
				.openingQty(materialReceipt.getReceiptDetails().get(0).getAcceptedQty())
				.openingQty(materialReceipt.getReceiptDetails().get(0).getReceivedQty())
				.recieptDate(materialReceipt.getReceiptDate())
				.lotNo(materialReceipt.getReceiptDetails().get(0).getReceiptDetailsAddnInfo().get(0).getLotNo())
				.uomValue(materialReceipt.getReceiptDetails().get(0).getUom().getCode())
				.tenantId(materialReceipt.getTenantId())
				.createdBy(auditDetails.getCreatedBy())
				.createdTime(auditDetails.getCreatedTime())
				.lastModifiedBy(auditDetails.getLastModifiedBy())
				.lastModifiedTime(auditDetails.getLastModifiedTime())
				.build();
				
	}
	public MaterialReceipt toDomain() {

        return MaterialReceipt.builder()
        		.id(id)
        		.receiptDate(recieptDate)
                .auditDetails(buildAuditDetails())
                .build();
    }
	

	 private AuditDetails buildAuditDetails() {
	        return AuditDetails.builder()
	                .createdBy(createdBy)
	                .createdTime(createdTime)
	                .lastModifiedBy(lastModifiedBy)
	                .lastModifiedTime(lastModifiedTime)
	                .tenantId(tenantId)
	                .build();
	    }

}
