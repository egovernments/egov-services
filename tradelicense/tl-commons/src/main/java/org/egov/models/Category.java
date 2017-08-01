package org.egov.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.BusinessNatureEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
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
public class Category {

	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@NotNull
	@Size(min = 4, max = 256)
	private String name = null;

	@NotNull
	@Size(min = 4, max = 50)
	private String code = null;

	private Long parentId = null;

	private BusinessNatureEnum businessNature = null;

	private List<CategoryDetail> details;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}