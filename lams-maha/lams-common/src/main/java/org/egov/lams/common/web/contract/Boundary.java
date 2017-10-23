
package org.egov.lams.common.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Boundary
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Boundary {
  @NotNull
  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("longitude")
  private Float longitude;

  @JsonProperty("latitude")
  private Float latitude;

  @JsonProperty("boundaryNum")
  private Long boundaryNum;

  @JsonProperty("parent")
  private Boundary parent;

  @JsonProperty("tenantId")
  private String tenantId;

  @JsonProperty("boundaryType")
  private BoundaryType boundaryType;

  @JsonProperty("code")
  private String code;
}

