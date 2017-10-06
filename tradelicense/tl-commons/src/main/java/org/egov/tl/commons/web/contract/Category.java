package org.egov.tl.commons.web.contract;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license category
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

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

	@JsonProperty("validityYears")
	private Long validityYears = null;

	@JsonProperty("parent")
	private String parent = null;

	@JsonProperty("active")
	private Boolean active = true;

	@JsonProperty("businessNature")
	private BusinessNatureEnum businessNature = null;

	@Valid
	@JsonProperty("details")
	private List<CategoryDetail> details;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}