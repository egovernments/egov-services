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
	
  @JsonProperty("version")
  private String version = null;

  @JsonProperty("indexName")
  private String indexName = null;

  @JsonProperty("fromTopic")
  private String fromTopic = null;
  
  @JsonProperty("indexType")
  private String indexType = null;

  @JsonProperty("jsonPath")
  private String jsonPath = null;
  
  @JsonProperty("indexID")
  private String indexID = null;
  
  @JsonProperty("indexMap")
  private List<indexMap> indexMap = null;
  
  @JsonProperty("omitPaths")
  private List<String> omitPaths = null;
  
  @JsonProperty("maskPaths")
  private List<String> maskPaths = null;
  

  @JsonProperty("hashPaths")
  private List<String> hashPaths = null;
 
}
