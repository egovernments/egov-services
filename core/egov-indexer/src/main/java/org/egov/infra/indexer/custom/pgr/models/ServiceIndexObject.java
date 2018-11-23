package org.egov.infra.indexer.custom.pgr.models;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ServiceIndexObject extends Service {
	
	  @JsonProperty("actionHistory")
	  @Valid
	  private ActionHistory actionHistory = null;

}
