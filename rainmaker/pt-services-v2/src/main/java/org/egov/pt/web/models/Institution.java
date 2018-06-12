package org.egov.pt.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Institution {

  @JsonProperty("id")
  private String id;

  @JsonProperty("tenantId")
  private String tenantId;

  @JsonProperty("institutionName")
  private String institutionName;

  @JsonProperty("institutionType")
  private String institutionType;

  @JsonProperty("authorizedPersonName")
  private String authorizedPersonName;

  @JsonProperty("designation")
  private String designation;

}
