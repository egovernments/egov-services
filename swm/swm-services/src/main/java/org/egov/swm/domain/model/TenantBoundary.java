package org.egov.swm.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class TenantBoundary {

    @JsonProperty("hierarchyType")
    private HierarchyType hierarchyType;

    @JsonProperty("boundary")
    private Boundary boundary;

}
