package org.egov.lams.contract;

import java.util.List;

import org.egov.lams.model.Asset;

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
public class AssetResponse {

	  @JsonProperty("ResponseInfo")
	  private ResponseInfo responseInfo;

	  @JsonProperty("Assets")
	  private List<Asset> assets;
}
