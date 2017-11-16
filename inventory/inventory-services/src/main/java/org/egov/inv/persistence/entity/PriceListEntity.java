package org.egov.inv.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.Supplier;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceListEntity {
	
    public static final String TABLE_NAME = "pricelist";
    public static final String SEQUENCE_NAME = "seq_pricelist";
    public static final String ALIAS = "pricelist";

    protected String createdBy;
    
    private String id;
    
    private String priceList;
    
    private String supplier;
    
    private String rateType;
    
    private String rateContractNumber;
    
    private Long rateContractDate;
    
    private String agreementNumber;
    
    private Long agreementDate;
    
    private Long agreementStartDate;
    
    private Long agreementEndDate;
    
    private boolean active;
    
    private String fileStoreId;
    
    private String priceListDetails;
    
    private String tenantId;

    private String lastModifiedBy;

    private Long createdTime;

    private Long lastModifiedTime;

    public PriceList toDomain() {
        return PriceList.builder()
                .id(id)
                .supplier(getSupplier(supplier))
                .rateType(PriceList.RateTypeEnum.valueOf(rateType.toUpperCase()))
                .rateContractNumber(rateContractNumber)
                .rateContractDate(rateContractDate)
                .agreementNumber(agreementNumber)
                .agreementDate(agreementDate)
                .agreementStartDate(agreementStartDate)
                .agreementEndDate(agreementEndDate)
                .active(active)
                .fileStoreId(fileStoreId)
                .priceListDetails(getPriceListDetails(priceListDetails))
                .auditDetails(mapAuditDetails(tenantId, createdBy, createdTime, lastModifiedBy, lastModifiedTime))
                .build();
    }

    private Supplier getSupplier(String id) {
    	Supplier supplier = new Supplier();
    	supplier.setCode(id);
        return supplier;
    }
    
    private List<PriceListDetails> getPriceListDetails(String id) {
    	List<PriceListDetails> priceListDetailsList = new ArrayList<>();
    	priceListDetailsList.add(PriceListDetails.builder().id(id).build());
        return priceListDetailsList;
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
