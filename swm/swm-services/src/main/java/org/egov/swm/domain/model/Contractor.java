package org.egov.swm.domain.model;

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
public class Contractor {

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("tenantId")
	private String tenantId = null;

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

}
