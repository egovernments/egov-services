package org.egov.swm.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
public class RouteCollectionPointMap {

    @NotNull
    @Length(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("route")
    private String route = null;

    @NotNull
    @JsonProperty("collectionPoint")
    private String collectionPoint = null;

    @JsonProperty("routes")
    private String routes = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
