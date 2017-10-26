package org.egov.swm.domain.model;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FinancialYear {

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("id")
	private Long id = null;

	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@Size(min = 1, max = 25)
	@JsonProperty("finYearRange")
	private String finYearRange = null;

	@NotNull
	@Valid
	@JsonProperty("startingDate")
	private Date startingDate = null;

	@NotNull
	@Valid
	@JsonProperty("endingDate")
	private Date endingDate = null;

	@NotNull
	@JsonProperty("active")
	private Boolean active = null;

	@NotNull
	@JsonProperty("isActiveForPosting")
	private Boolean isActiveForPosting = null;

	@JsonProperty("isClosed")
	private Boolean isClosed = null;

	@JsonProperty("transferClosingBalance")
	private Boolean transferClosingBalance = null;

}
