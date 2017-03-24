package org.egov.lams.model;


import org.egov.lams.model.enums.Application;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentType   {
	
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  private String name = null;
 
  @JsonProperty("application")
  private Application application = null;
  
  @JsonProperty("tenantId")
  private String tenantId;
}
