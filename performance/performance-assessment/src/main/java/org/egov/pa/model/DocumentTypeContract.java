package org.egov.pa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentTypeContract {

	private Long id;


	@JsonProperty("tenantId")
	private String tenantId ;
	
	@JsonProperty("code")
	private String code ;


	@JsonProperty("name")
	private String name ;

	@JsonProperty("mandatory")
	private Boolean mandatory;
	
	@JsonProperty("kpiCode") 
	private String kpiCode = null;
	
	private Boolean enabled ;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	@JsonProperty("active")
	private Boolean active = null;
	
	@JsonProperty("category")
	private String category;
	
	@JsonProperty("subCategory")
	private String subCategory;
	
	@JsonProperty("categoryName")
	private String categoryName;
	
	@JsonProperty("subCategoryName")
	private String subCategoryName;
	
	
}