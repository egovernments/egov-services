package org.egov.inv.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Material;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.Uom;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceListDetailsEntity {
	
    public static final String TABLE_NAME = "pricelistdetails";
    public static final String SEQUENCE_NAME = "seq_pricelistdetails";
    public static final String ALIAS = "pricelistdetails";

    protected String createdBy;
    
    private String id;
    
    private String material;
    
    private String priceList;
    
    private Double ratePerUnit;
    
    private Double quantity;
    
    private String uom;
    
    private Long fromDate;
    
    private Long toDate;
    
    private Boolean active;
    
    private Boolean deleted;
    
    private String tenantId;

    private String lastModifiedBy;

    private Long createdTime;

    private Long lastModifiedTime;

    public PriceListDetails toDomain() {
        return PriceListDetails.builder()
                .id(id)
                .material(getMaterial(material))
                .uom(getUom(uom))
                .quantity(quantity)
                .ratePerUnit(ratePerUnit)
                .fromDate(fromDate)
                .toDate(toDate)
                .active(null != active ? active : null )
                .auditDetails(mapAuditDetails(tenantId, createdBy, createdTime, lastModifiedBy, lastModifiedTime))
                .build();
    }

    private Material getMaterial(String id) {
    	Material material = new Material();
    	material.setCode(id);
        return material;
    }
    
    private Uom getUom(String id) {
    	Uom uom = new Uom();
    	uom.setCode(id);
        return uom;
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
