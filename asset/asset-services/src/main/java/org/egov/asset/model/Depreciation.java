package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Depreciation extends DepreciationCriteria {

    private Long id;

    @NotNull
    private String tenantId;

    @JsonProperty("AuditDetails")
    private AuditDetails auditDetails;

    @JsonProperty("DepreciationDetail")
    private List<DepreciationDetail> depreciationDetails = new ArrayList<>();

    @Builder
    private Depreciation(final DepreciationCriteria depreciationCriteria, final String tenantId,
            final AuditDetails auditDetails, final List<DepreciationDetail> depreciationDetails) {
        super(depreciationCriteria);
        this.tenantId = tenantId;
        this.auditDetails = auditDetails;
        this.depreciationDetails = depreciationDetails;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Depreciation other = (Depreciation) obj;
        if (auditDetails == null) {
            if (other.auditDetails != null)
                return false;
        } else if (!auditDetails.equals(other.auditDetails))
            return false;
        if (depreciationDetails == null) {
            if (other.depreciationDetails != null)
                return false;
        } else if (!depreciationDetails.equals(other.depreciationDetails))
            return false;
        return true;
    }

}
