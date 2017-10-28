package org.egov.swm.domain.model;

import java.util.ArrayList;
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
public class DumpingGround {

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

	@NotNull
	@JsonProperty("ward")
	private Boundary ward = null;

	@NotNull
	@JsonProperty("zone")
	private Boundary zone = null;

	@NotNull
	@JsonProperty("street")
	private Boundary street = null;

	@NotNull
	@JsonProperty("colony")
	private Boundary colony = null;

	@NotNull
	@JsonProperty("area")
	private Double area = null;

	@NotNull
	@JsonProperty("capacity")
	private Double capacity = null;

	@NotNull
	@Length(min = 15, max = 500)
	@JsonProperty("address")
	private String address = null;

	@NotNull
	@JsonProperty("latitude")
	private Double latitude = null;

	@NotNull
	@JsonProperty("longitude")
	private Double longitude = null;

	@NotNull
	@JsonProperty("wasteTypes")
	private List<WasteType> wasteTypes = new ArrayList<WasteType>();

	@JsonProperty("mpcbAuthorisation")
	private Boolean mpcbAuthorisation = null;

	@JsonProperty("bankGuarantee")
	private Boolean bankGuarantee = null;

	@Length(min = 0, max = 256)
	@JsonProperty("bankName")
	private String bankName = null;

	@JsonProperty("bankValidityFrom")
	private Long bankValidityFrom = null;

	@JsonProperty("bankValidityTo")
	private Long bankValidityTo = null;

	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
