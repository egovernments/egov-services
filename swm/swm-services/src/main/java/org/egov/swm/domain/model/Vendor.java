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
public class Vendor {

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("name")
	private String name = null;

	@NotNull
	@Valid
	@JsonProperty("ward")
	private Boundary ward = null;

	@NotNull
	@Valid
	@JsonProperty("zone")
	private Boundary zone = null;

	@NotNull
	@Valid
	@JsonProperty("street")
	private Boundary street = null;

	@NotNull
	@Valid
	@JsonProperty("colony")
	private Boundary colony = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("registrationNo")
	private String registrationNo = null;

	@NotNull
	@Length(min = 7, max = 256)
	@JsonProperty("email")
	private String email = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("tinNumber")
	private String tinNumber = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("gst")
	private String gst = null;

	@Length(min = 10, max = 10)
	@JsonProperty("phoneNo")
	private String phoneNo = null;

	@NotNull
	@Length(min = 10, max = 10)
	@JsonProperty("contactNo")
	private String contactNo = null;

	@Length(min = 10, max = 10)
	@JsonProperty("faxNumber")
	private String faxNumber = null;

	@NotNull
	@Length(min = 10, max = 500)
	@JsonProperty("address")
	private String address = null;

	@NotNull
	@Length(max = 256)
	@JsonProperty("services")
	private String services = null;

	@Length(min = 10, max = 500)
	@JsonProperty("details")
	private String details = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
