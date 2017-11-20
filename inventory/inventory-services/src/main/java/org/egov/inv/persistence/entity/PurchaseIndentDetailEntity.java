package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.PurchaseIndentDetail;

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
public class PurchaseIndentDetailEntity  {
	public static final String TABLE_NAME = "PurchaseIndentDetail";
	private String id;
	private String tenantId;
	private String indentDetail;
	private BigDecimal quantity;
	private String purchaseOrderDetail;

	public PurchaseIndentDetail toDomain() {
		PurchaseIndentDetail po = new PurchaseIndentDetail();
		po.setId(this.id);
		po.setTenantId(this.tenantId);
		po.setIndentDetail(new IndentDetail().id(indentDetail));
		po.setQuantity(this.quantity);
		return po;
	}

	public PurchaseIndentDetailEntity toEntity(PurchaseIndentDetail po) {
		this.id = po.getId();
		this.tenantId = po.getTenantId();
		this.indentDetail = po.getIndentDetail() != null ? po.getIndentDetail().getId() : null;
		this.quantity = po.getQuantity() != null ? po.getQuantity() : null;
		return this;
	}

}
