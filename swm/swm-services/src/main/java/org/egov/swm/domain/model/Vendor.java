package org.egov.swm.domain.model;

import java.util.List;

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

	@Length(min = 1, max = 256)
	@JsonProperty("vendorNo")
	private String vendorNo = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("name")
	private String name = null;

	@NotNull
	@JsonProperty("servicedLocations")
	private List<Boundary> servicedLocations = null;

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

	@JsonProperty("agreementDocument")
	private Document agreementDocument;

	@NotNull
	@JsonProperty("servicesOffered")
	private List<SwmProcess> servicesOffered = null;

	@Length(min = 10, max = 500)
	@JsonProperty("details")
	private String details = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
