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
  private String version;

  @JsonProperty("indexName")
  private String indexName;

  @JsonProperty("fromTopicSave")
  private String fromTopicSave;
  
  @JsonProperty("fromTopicUpdate")
  private String fromTopicUpdate;
  
  
  @JsonProperty("indexType")
  private String indexType;

  @JsonProperty("jsonPath")
  private String jsonPath;
  
  @JsonProperty("indexID")
  private String indexID;
  
  @JsonProperty("isBulk")
  private Boolean isBulk;
  
  @JsonProperty("indexMap")
  private List<indexMap> indexMap;
  
  @JsonProperty("omitPaths")
  private List<String> omitPaths;
  
  @JsonProperty("maskPaths")
  private List<String> maskPaths;
  

  @JsonProperty("hashPaths")
  private List<String> hashPaths;
 
}
