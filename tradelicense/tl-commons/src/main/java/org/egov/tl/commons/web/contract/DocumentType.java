package org.egov.tl.commons.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * DocumentType
 * 
 * @author Shubham pratap Singh
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentType {

	private Long id = null;

	@JsonProperty("tenantId")
	@Pattern(regexp = ".*[^ ].*", message = "{error.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.tenantId.empty}")
	private String tenantId = null;

	@JsonProperty("name")
	@NotEmpty(message = "{error.name.empty}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.name.emptyspaces}")
	@Length(min = 1, max = 100, message = "{error.name.empty}")
	private String name = null;

	private Boolean mandatory = true;

	private Boolean enabled = true;

	@NotNull(message = "{error.applicationType.null}")
	private ApplicationTypeEnum applicationType;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
	
	@JsonProperty("category")
	private String category;
	
	@JsonProperty("subCategory")
	private String subCategory;
	
	@JsonProperty("categoryName")
	private String categoryName;
	
	@JsonProperty("subCategoryName")
	private String subCategoryName;
	
}