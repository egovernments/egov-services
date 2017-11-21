package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.Asset;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.Material;
import org.egov.inv.model.ProjectCode;
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
public class IndentDetailEntity  {
	public static final String TABLE_NAME = "indentdetail";
	private String id;
	private String tenantId;
	private String material;
	private String uom;
	private String parentIndentLine;
	private BigDecimal orderNumber;
	private String projectCode;
	private String indentNumber;
	private String asset;
	private BigDecimal indentQuantity;
	private BigDecimal totalProcessedQuantity;
	private BigDecimal indentIssuedQuantity;
	private BigDecimal poOrderedQuantity;
	private BigDecimal interstoreRequestQuantity;
	private String deliveryTerms;
	private String remarks;

	public IndentDetail toDomain() {
		IndentDetail indentDetail = new IndentDetail();
		indentDetail.setId(this.id);
		indentDetail.setTenantId(this.tenantId);
		indentDetail.setMaterial(new Material().code(material));
		indentDetail.setUom(new Uom().code(uom));
		indentDetail.setParentIndentLine(this.parentIndentLine);
		indentDetail.setOrderNumber(this.orderNumber);
		indentDetail.setProjectCode(new ProjectCode().id(projectCode));
		indentDetail.setAsset(new Asset().code(asset));
		indentDetail.setIndentQuantity(this.indentQuantity);
		indentDetail.setTotalProcessedQuantity(this.totalProcessedQuantity);
		indentDetail.setIndentIssuedQuantity(this.indentIssuedQuantity);
		indentDetail.setPoOrderedQuantity(this.poOrderedQuantity);
		indentDetail.setInterstoreRequestQuantity(this.interstoreRequestQuantity);
		indentDetail.setDeliveryTerms(this.deliveryTerms);
		indentDetail.setRemarks(this.remarks);
		return indentDetail;
	}

	public IndentDetailEntity toEntity(IndentDetail indentDetail) {
		this.id = indentDetail.getId();
		this.tenantId = indentDetail.getTenantId();
		//this.indentNumber = indentDetail.ge();
		this.material = indentDetail.getMaterial() != null ? indentDetail.getMaterial().getCode() : null;
		this.uom = indentDetail.getUom() != null ? indentDetail.getUom().getCode() : null;
		this.parentIndentLine = indentDetail.getParentIndentLine();
		this.orderNumber = indentDetail.getOrderNumber();
		this.projectCode = indentDetail.getProjectCode() != null ? indentDetail.getProjectCode().getId() : null;
		this.asset = indentDetail.getAsset() != null ? indentDetail.getAsset().getCode() : null;
		this.indentQuantity = indentDetail.getIndentQuantity();
		this.totalProcessedQuantity = indentDetail.getTotalProcessedQuantity();
		this.indentIssuedQuantity = indentDetail.getIndentIssuedQuantity();
		this.poOrderedQuantity = indentDetail.getPoOrderedQuantity();
		this.interstoreRequestQuantity = indentDetail.getInterstoreRequestQuantity();
		this.deliveryTerms = indentDetail.getDeliveryTerms();
		this.remarks = indentDetail.getRemarks();
		return this;
	}

}
