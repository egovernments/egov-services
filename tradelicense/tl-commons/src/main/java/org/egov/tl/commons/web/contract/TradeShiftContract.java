package org.egov.tl.commons.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TradeShiftContract {

	private Long id;

	@JsonProperty("tenantId")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.license.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.license.tenantId.empty}")
	private String tenantId;

	@JsonProperty("licenseId")
	private Long licenseId;

	@NotNull(message = "{error.license.shiftNo}")
	@JsonProperty("shiftNo")
	private Integer shiftNo;

	@NotNull(message = "{error.license.fromTime}")
	@JsonProperty("fromTime")
	private Long fromTime;

	@NotNull(message = "{error.license.toTime}")
	@JsonProperty("toTime")
	private Long toTime;

	@JsonProperty("remarks")
	private String remarks;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
}
