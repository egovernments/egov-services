package org.egov.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * OwnerInfo
 * Author : Narendra
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OwnerInfo extends User  {
  @JsonProperty("isPrimaryOwner")
  private Boolean isPrimaryOwner = null;

  @JsonProperty("isSecondaryOwner")
  private Boolean isSecondaryOwner = null;

  @JsonProperty("ownerShipPercentage")
  private Double ownerShipPercentage = null;

  @JsonProperty("ownerType")
  @Size(min=4,max=32)
  private String ownerType = null;
}

