package org.egov.lams.notification.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * Categories defined under asset category type are shown in the drop down.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AssetCategory   {
	
  @JsonProperty("id")
  private Long id;
  
  @JsonProperty("name")
  @NotNull
  private String name;

  @JsonProperty("code")
  private String code;
}

