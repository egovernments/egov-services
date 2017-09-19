package org.egov.lams.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

/**
 * Categories defined under asset category type are shown in the drop down.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class AssetCategory   {
	
  @JsonProperty("id")
  private Long id;
  
  @JsonProperty("name")
  @NotNull
  private String name;

  @JsonProperty("code")
  private String code;
}

