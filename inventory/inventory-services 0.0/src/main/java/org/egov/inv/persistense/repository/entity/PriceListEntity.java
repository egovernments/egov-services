package org.egov.inv.persistense.repository.entity;

import io.swagger.model.AuditDetails;
import io.swagger.model.PriceList;
import io.swagger.model.PriceListDetails;
import io.swagger.model.Supplier;

import java.util.ArrayList;
import java.util.List;

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
public class PriceListEntity {

    protected String createdBy;
    
    private String id;
    
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
                .tenantId(tenantId)
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedTime(lastModifiedTime)
                .build();
    }
}
