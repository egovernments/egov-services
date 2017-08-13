package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.Category;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaxHeadMaster {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId = null;

	@JsonProperty("category")
	private Category category = null;

	@JsonProperty("service")
	@Size(min = 2, max = 64)
	private String service = null;

	@JsonProperty("name")
	@Size(min = 2, max = 64)
	private String name = null;

	@JsonProperty("code")
	@Size(min = 2, max = 64)
	private String code = null;

	@JsonProperty("glCode")
	@Size(min = 1, max = 64)
	private String glCode = null;

	@JsonProperty("isDebit")
	private Boolean isDebit = false;

	@JsonProperty("isActualDemand")
	private Boolean isActualDemand = null;

	@JsonProperty("validFrom")
	@NotNull
	private String validFrom = null;

	@JsonProperty("validTill")
	@NotNull
	private String validTill = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
