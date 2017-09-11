package org.egov.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AppConfiguration {
	@JsonProperty("id")
	private Long id;
	
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;
	
	@JsonProperty("keyName")
	@NotNull
	@Size(min=1,max=256)
	private String keyName;
	
	@JsonProperty("description")
	@Size(min=1,max=256)
	private String description;
	
	@JsonProperty("values")
	@NotNull
	private List<String> values;
	
	@JsonProperty("effectiveFrom")
	private String effectiveFrom;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
