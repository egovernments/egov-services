package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.ChannelEnum;
import org.egov.enums.CreationReasonEnum;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.postgresql.util.PGobject;

/**
 * A Object holds the basic data for a property
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Property {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("upicNumber")
	@Size(min = 6, max = 128)
	private String upicNumber = null;

	@JsonProperty("oldUpicNumber")
	@Size(min = 4, max = 128)
	private String oldUpicNumber = null;

	@JsonProperty("vltUpicNumber")
	@Size(min = 4, max = 128)
	private String vltUpicNumber = null;

	@JsonProperty("creationReason")
	@NotNull
	private CreationReasonEnum creationReason = null;

	@JsonProperty("address")
	@NotNull
	private Address address = null;

	@JsonProperty("owners")
	@Valid
	@NotNull
	@NotEmpty(message = "property.MIN_ONE_OWNER_REQUIRED")
	private List<User> owners = new ArrayList<User>();

	@JsonProperty("propertyDetail")
	@Valid
	@NotNull
	private PropertyDetail propertyDetail = null;

	@JsonProperty("vacantLand")
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
	private ChannelEnum channel = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("demands")
	private List<Demand> demands = null;

	@JsonProperty("sequenceNo")
	@NotNull
	private Integer sequenceNo = null;

	@JsonProperty("oldestUpicNumber")
	@Size(min = 1, max = 128)
	private String oldestUpicNumber = null;

	@JsonProperty("propertyAddress")
	private String propertyAddress = null;

	@JsonProperty("propertyOwner")
	private String propertyOwner = null;
	
	@JsonProperty("demandsJsonObject")
	private PGobject demandsJsonObject= null;

}
