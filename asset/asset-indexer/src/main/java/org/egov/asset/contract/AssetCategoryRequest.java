
package org.egov.asset.contract;

import javax.validation.Valid;

import org.egov.asset.model.AssetCategory;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AssetCategoryRequest   {
 

  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("AssetCategory")
  @Valid
  private AssetCategory assetCategory = null;

}
