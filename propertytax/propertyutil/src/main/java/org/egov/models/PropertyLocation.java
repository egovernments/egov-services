package org.egov.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Boundary details for a property. &#39;revenueBoundary&#39; is granular level
 * Revenue heirarchy boundary, &#39;locationBoundary&#39; is granular level
 * Location heirarchy boundary, &#39;adminBoundary&#39; is granular level
 * Administration heirarchy boundary. Author: Narendra
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyLocation {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("revenueBoundary")
	private Boundary revenueBoundary = null;

	@JsonProperty("locationBoundary")
	private Boundary locationBoundary = null;

	@JsonProperty("adminBoundary")
	private Boundary adminBoundary = null;

	@JsonProperty("guidanceValueBoundary")
	private String guidanceValueBoundary = null;

	@JsonProperty("northBoundedBy")
	@Size(min = 1, max = 256)
	private String northBoundedBy = null;

	@JsonProperty("eastBoundedBy")
	@Size(min = 1, max = 256)
	private String eastBoundedBy = null;

	@JsonProperty("westBoundedBy")
	@Size(min = 1, max = 256)
	private String westBoundedBy = null;

	@JsonProperty("southBoundedBy")
	@Size(min = 1, max = 256)
	private String southBoundedBy = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}
