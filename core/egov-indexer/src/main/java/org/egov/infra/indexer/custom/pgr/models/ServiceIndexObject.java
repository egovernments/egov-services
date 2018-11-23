package org.egov.infra.indexer.custom.pgr.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceIndexObject extends Service {
	
	  @JsonProperty("actionHistory")
	  @Valid
	  private List<ActionHistory> actionHistory = null;

}
