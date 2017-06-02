package org.egov.asset.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuditDetails {
	
	  @JsonProperty("createdBy")
	  private String createdBy = null;
	  
	  @JsonProperty("createdDate")
	  private Long createdDate = null;
	  
	  @JsonProperty("lastModifiedBy")
	  private String lastModifiedBy = null;
	  
	  @JsonProperty("lastModifiedDate")
	  private Long lastModifiedDate = null;

}
