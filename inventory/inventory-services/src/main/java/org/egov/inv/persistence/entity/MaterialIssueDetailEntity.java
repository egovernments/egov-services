package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialIssueDetail;
import org.egov.inv.model.Uom;
import org.egov.inv.model.MaterialIssue.IssueTypeEnum;
import org.egov.inv.persistence.entity.MaterialIssueEntity.MaterialIssueEntityBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialIssueDetailEntity {
	
	private String id ;
	
	private String tenantId;
	
	private Double orderNumber;
	
	private Double value;
	
	private String uom;
	
	private Double scrapValue;
	
	private String voucherHeader;
	
	private String materialIssueNumber;
	
	private String indentDetailId;
	
	private String materialCode;
	
	private Double quantityIssued;
	
	private String description;
	
	private BigDecimal userQuantityIssued;

	public MaterialIssueDetail toDomain(String type) {
		MaterialIssueDetail detail = new MaterialIssueDetail();
		detail.setId(id);
		detail.setTenantId(tenantId);
		detail.setOrderNumber(BigDecimal.valueOf(orderNumber));
		detail.setValue(BigDecimal.valueOf(value));
		Uom unit = new Uom();
		unit.setCode(uom);
		Material material = new Material();
		material.setCode(materialCode);
		if(null != type && (type.equals(IssueTypeEnum.INDENTISSUE.toString()) || (type.equals(IssueTypeEnum.MATERIALOUTWARD.toString())))){
		IndentDetail indentDetail = new IndentDetail();
		indentDetail.setId(indentDetailId);
		detail.setIndentDetail(indentDetail);
		}
		detail.setUom(unit);
		detail.setMaterial(material);
		if(scrapValue != null )
		detail.scrapValue(BigDecimal.valueOf(scrapValue));
		detail.setVoucherHeader(voucherHeader);
		detail.setUserQuantityIssued(userQuantityIssued);
		detail.setQuantityIssued(BigDecimal.valueOf(quantityIssued));
		detail.setDescription(description);
		return detail;
	}

}
