package org.egov.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class ModeOfAcquisition {
	
	@JsonProperty("id")
	private Long id;
	
	@NotNull
	@JsonProperty("code")
	private String code;
	
    @JsonProperty("name")
	private String name;
    
    @JsonProperty("tenantId")
	private String tenantId;
    
    @JsonProperty("isActive")
	private Boolean isActive;

	
	

}
