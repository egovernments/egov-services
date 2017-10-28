package org.egov.swm.domain.model;

import java.util.List;

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
public class Route {

	@Length(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Length(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@Length(min = 1, max = 128)
	@JsonProperty("name")
	private String name = null;
	
	@JsonProperty("collectionType")
	private CollectionType collectionType = null;

	@JsonProperty("isEndingPointDumpingGround")
	private Boolean isEndingPointDumpingGround = null;
	
	@JsonProperty("startingCollectionPoint")
	private CollectionPoint startingCollectionPoint = null;

	@JsonProperty("endingCollectionPoint")
	private CollectionPoint endingCollectionPoint = null;
	
	@JsonProperty("endingDumpingGroundPoint")
	private DumpingGround endingDumpingGroundPoint = null;

	@JsonProperty("collectionPoints")
	private List<RouteCollectionPointMap> collectionPoints = null;

	@NotNull
	@JsonProperty("distance")
	private Double distance = null;
	
	@NotNull
	@JsonProperty("garbageEstimate")
	private Double garbageEstimate = null;
	
	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
