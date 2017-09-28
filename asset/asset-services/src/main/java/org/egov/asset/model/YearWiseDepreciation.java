package org.egov.asset.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class YearWiseDepreciation {
    
    private Long id;

    private Double depreciationRate;
    
    @NotNull
    private String financialYear;

    private Long assetId;

    private Long usefulLifeInYears;
    
    private String tenantId;
}
