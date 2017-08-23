package org.egov.tl.commons.web.contract;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in UOM
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UOM {

	private Long id = null;

	@JsonProperty("tenantId")
	@Pattern(regexp = ".*[^ ].*", message = "{error.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.tenantId.empty}")
	private String tenantId = null;

	@JsonProperty("name")
	@Pattern(regexp = ".*[^ ].*", message = "{error.name.emptyspaces}")
	@NotEmpty(message = "{error.name.empty}")
	@Length(min = 1, max = 100, message = "{error.name.empty}")
	private String name = null;

	@JsonProperty("code")
	@Pattern(regexp = ".*[^ ].*", message = "{error.code.emptyspaces}")
	@NotEmpty(message = "{error.code.empty}")
	@Length(min = 1, max = 20, message = "{error.code.empty}")
	private String code = null;

	private Boolean active = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}