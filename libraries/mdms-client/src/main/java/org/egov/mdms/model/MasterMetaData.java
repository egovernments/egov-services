package org.egov.mdms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MasterMetaData {
	
	@JsonProperty("tenantId")
	public String tenantId;
	
	@JsonProperty("moduleName")
	public String moduleName;
	
	@JsonProperty("masterName")
	public String masterName;
	
	@JsonProperty("isValidate")
	public Boolean isValidate;
	
	@JsonProperty("masterData")
	public Object masterData;

}
