package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class CollectionPoint {

	@Length(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Length(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("name")
	private String name = null;
	
	@NotNull
	@JsonProperty("ward")
	private Boundary ward = null;

	@NotNull
	@JsonProperty("zoneCode")
	private Boundary zoneCode = null;

	@NotNull
	@JsonProperty("street")
	private Boundary street = null;

	@JsonProperty("colony")
	private Boundary colony = null;

	@JsonProperty("binIdDetails")
	private List<BinIdDetails> binIdDetails = null;

	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
