package org.egov.asset.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuditDetails {
	
	  @JsonIgnore
	  private String createdBy = null;
	  
	  @JsonIgnore
	  private Long createdDate = null;
	  
	  @JsonIgnore
	  private String lastModifiedBy = null;
	  
	  @JsonIgnore
	  private Long lastModifiedDate = null;

}
