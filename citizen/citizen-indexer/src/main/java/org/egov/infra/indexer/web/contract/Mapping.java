package org.egov.infra.indexer.web.contract;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Mapping   {
	
  @JsonProperty("topic")
  private String topic;

  @JsonProperty("indexes")
  private List<Index> indexes; 
}
