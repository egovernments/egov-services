package org.egov.wcms.model;

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
public class ServiceChargeDetails {
    public static final String SEQ_SERVICECHARGEDETAILS = "SEQ_EGWTR_SERVICECHARGE_DETAILS";
    @NotNull
    private Long id;
    
    @NotNull
    @Size(max=20)
    private String code;
    
    @NotNull
    private Double uomFrom;
    
    @NotNull
    private Double uomTo;
    
    @NotNull
    private Double amountOrpercentage;
    
    @NotNull
    private Long serviceCharge;
    
    @NotNull
    @Size(max=250)
    private String tenantId;

}
