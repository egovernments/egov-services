package org.egov.asset.contract;

import java.util.ArrayList;
import java.util.List;

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
public class AssetCategoryResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("AssetCategory")
  private List<AssetCategory> assetCategory = new ArrayList<AssetCategory>();

}

