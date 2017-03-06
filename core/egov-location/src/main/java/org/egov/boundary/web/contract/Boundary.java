package org.egov.boundary.web.contract;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Boundary {

	@NotEmpty
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("longitude")
	private Float longitude;
	@JsonProperty("latitude")
	private Float latitude;
	@JsonProperty("boundaryNum")
	private Long boundaryNum;

	private Boundary parent;

	public Boundary(org.egov.boundary.persistence.entity.Boundary entityBoundary) {
		this.id = entityBoundary.getId().toString();
		this.name = entityBoundary.getName();
		if (entityBoundary.getParent() != null) {
			this.setParent(new Boundary(entityBoundary.getParent()));
		}
		this.longitude = entityBoundary.getLongitude();
		this.latitude = entityBoundary.getLatitude();
		this.boundaryNum = entityBoundary.getBoundaryNum();
	}

}