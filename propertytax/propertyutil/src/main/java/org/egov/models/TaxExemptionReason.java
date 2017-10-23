package org.egov.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Yosadhara
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("data")
public class TaxExemptionReason {
	
	@JsonProperty("id")
	private Long id;

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")	
	private String tenantId;
	
	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("name")
	private String name;
	
	@NotNull
	@Size(min = 1, max = 64)
	@JsonProperty("code")
	private String code;
	
	@Size(min = 4, max = 512)
	@JsonProperty("description")
	private String description;
	
	@NotNull
	@JsonProperty("active")
	private Boolean active;
	
	@NotNull
	@JsonProperty("percentageRate")
	private Double percentageRate;
	
	@NotNull
	@JsonProperty("taxHeads")
	private List<String> taxHeads;	
	
	@JsonProperty("comments")
	private String comments;	
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
	
	@JsonProperty("data")
	private String data;
}
