package org.egov.wcms.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ServiceCharge {
    
    public static final String SEQ_SERVICECHARGE = "SEQ_EGWTR_SERVICECHARGE";
    @NotNull
    private Long id;
    
    @NotNull
    @Size(max=20)
    private String code;
    
    @NotNull
    private String serviceType;
    
    @NotNull
    private Boolean serviceChargeApplicable;
    
    @NotNull
    private String serviceChargeType;
    
    @Size(max=1024)
    private String description;
    
    @NotNull
    private Long effectiveFrom;
    
    @NotNull
    private Long effectiveTo;
    
    private Boolean outsideUlb;
    
    private List<ServiceChargeDetails> chargeDetails;
    
    @NotNull
    @Size(max=250)
    private String tenantId;
    
    @NotNull
    private Long createdBy;
    
    
    private Long createdDate;
    
    @NotNull
    private Long lastModifiedBy;
    
    private Long lastModifiedDate;
    
}
