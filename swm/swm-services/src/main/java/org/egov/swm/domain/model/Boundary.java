package org.egov.swm.domain.model;

import java.time.LocalDate;

import javax.validation.Valid;

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
public class Boundary {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("name")
	private String name = null;
	
	@JsonProperty("code")
	private String code = null;

	@Valid
	@JsonProperty("parent")
	private Boundary parent = null;

	@JsonProperty("boundary_num")
	private String boundaryNum = null;

	@Valid
	@JsonProperty("from_date")
	private LocalDate fromDate = null;

	@Valid
	@JsonProperty("to_date")
	private LocalDate toDate = null;

	@JsonProperty("is_history")
	private String isHistory = null;

	@JsonProperty("bndry_id")
	private String bndryId = null;

	@JsonProperty("local_name")
	private String localName = null;

	@JsonProperty("longitude")
	private String longitude = null;

	@JsonProperty("latitude")
	private String latitude = null;

	@Valid
	@JsonProperty("boundaryType")
	private BoundaryType boundaryType = null;

	@JsonProperty("materialized_path")
	private String materializedPath = null;

}
