package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Scrap;
import org.egov.inv.model.ScrapDetails;

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
public class ScrapDetailEntity {
	public static final String TABLE_NAME = "scrapDetails";
    public static final String SEQUENCE_NAME = "seq_scrapDetails";
    public static final String ALIAS = "scrapDetails";
    
    private String id;
    
    private String ScrapNumber;
    
    private String material;
    
    private String uom;
    
    private String lotnumber;
    
    private Integer expiryDate;
    
    private String scrapReason;
    
    private BigDecimal quantity;
    
    private BigDecimal disposalQuantity;
    
    private BigDecimal scrapValue;
    
    private String createdBy;

    private Long createdTime;

    private String lastModifiedBy;

    private Long lastModifiedTime;

    private String tenantId;

    public ScrapDetails toDomain() {
    	ScrapDetails scrapDetail = new ScrapDetails();

        return scrapDetail.id(id)
                .scrapNumber(ScrapNumber)
                .expiryDate(expiryDate)
                .scrapReason(ScrapDetails.ScrapReasonEnum.fromValue(scrapReason))
                .quantity(quantity)
                .disposalQuantity(disposalQuantity)
                .scrapValue(scrapValue)
                .tenantId(tenantId);

    }
    
}
