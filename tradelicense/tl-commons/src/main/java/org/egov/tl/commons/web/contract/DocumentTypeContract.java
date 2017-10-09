package org.egov.tl.commons.web.contract;

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
public class DocumentTypeContract {

	private Long id;


	@JsonProperty("tenantId")
	@Pattern(regexp = ".*[^ ].*",message="{error.tenantId.emptyspaces}")
	@NotEmpty(message="{error.tenantId.empty}")
	@Length(min = 4, max = 128, message="{error.tenantId.empty}")
	private String tenantId ;


	@JsonProperty("name")
	@NotEmpty(message="{error.name.empty}")
	@Pattern(regexp = ".*[^ ].*",message="{error.name.emptyspaces}")
	@Length(min = 1, max = 100, message="{error.name.empty}")
	private String name ;

	private Boolean mandatory;

	
	private Boolean enabled ;

	
	private ApplicationTypeEnum applicationType;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	@JsonProperty("category")
	private String category;
	
	@JsonProperty("subCategory")
	private String subCategory;
	
	@JsonProperty("categoryName")
	private String categoryName;
	
	@JsonProperty("subCategoryName")
	private String subCategoryName;
}