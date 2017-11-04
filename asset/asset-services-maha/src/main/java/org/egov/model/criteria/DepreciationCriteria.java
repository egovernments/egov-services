package org.egov.model.criteria;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DepreciationCriteria {

    public DepreciationCriteria(final DepreciationCriteria depreciationCriteria) {
        assetIds = depreciationCriteria.assetIds;
        financialYear = depreciationCriteria.financialYear;
        fromDate = depreciationCriteria.fromDate;
        toDate = depreciationCriteria.toDate;
        tenantId = depreciationCriteria.tenantId;
    }

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("financialYear")
    private String financialYear;

    @JsonProperty("fromDate")
    private Long fromDate;

    @JsonProperty("toDate")
    private Long toDate;

    @JsonProperty("assetIds")
    private Set<Long> assetIds = new HashSet<>();
}
