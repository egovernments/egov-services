package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Object holds the basic data for a property
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Property {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("upicNumber")
	@NotNull
	@Size(min = 6, max = 128)
	private String upicNumber = null;

	@JsonProperty("oldUpicNumber")
	@Size(min = 4, max = 128)
	private String oldUpicNumber = null;

	@JsonProperty("vltUpicNumber")
	@Size(min = 4, max = 128)
	private String vltUpicNumber = null;

	@JsonProperty("creationReason")
	@Size(min = 1, max = 256)
	private CreationReasonEnum creationReason = null;

	@JsonProperty("address")
	@NotNull
	private Address address = null;

	@JsonProperty("owners")
	@Valid
	@NotNull
	private List<OwnerInfo> owners = new ArrayList<OwnerInfo>();

	@JsonProperty("propertyDetail")
	@Valid
	@NotNull
	private PropertyDetail propertyDetail = null;

	@JsonProperty("vacantLand")
	@Valid
	private VacantLandDetail vacantLand = null;

	@JsonProperty("assessmentDate")
	private String assessmentDate = null;

	@JsonProperty("occupancyDate")
	@NotNull
	private String occupancyDate = null;

	@JsonProperty("gisRefNo")
	@Size(min = 4, max = 32)
	private String gisRefNo = null;

	@JsonProperty("isAuthorised")
	private Boolean isAuthorised = true;

	@JsonProperty("isUnderWorkflow")
	private Boolean isUnderWorkflow = false;

	@JsonProperty("boundary")
	@NotNull
	private PropertyLocation boundary = null;

	@JsonProperty("active")
	private Boolean active = true;

	@JsonProperty("channel")
	@NotNull
	@Size(min = 4, max = 16)
	private ChannelEnum channel = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
