package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.MaterialIssuedFromReceipt;
import org.egov.inv.model.MaterialReceiptDetail;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MaterialIssuedFromReceiptEntity {
	
	
	private String id;
	
	private String tenantId;
	
	private String 	receiptId;
	
	private String receiptDetailId;
	
	private String receiptDetailAddnlInfoId;
	
	private Double quantity;
	
	private Boolean status;
	
	private String issueDetailId;
	
	private Boolean deleted;
	

	public MaterialIssuedFromReceipt toDomain() {
		MaterialIssuedFromReceipt mifr = new MaterialIssuedFromReceipt();
		mifr.setId(id);
		MaterialReceiptDetail mrd = new MaterialReceiptDetail();
		mrd.setId(receiptDetailId);
		mifr.setMaterialReceiptDetail(mrd);
		mifr.setMaterialReceiptId(receiptId);
		mifr.setMaterialReceiptDetailAddnlinfoId(receiptDetailAddnlInfoId);
		mifr.setQuantity(BigDecimal.valueOf(quantity));
		mifr.setStatus(status);
		mifr.setTenantId(tenantId);
        return mifr;
		
	}
	
	

}
