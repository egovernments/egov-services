package org.egov.lams.contract;

import java.util.List;

import org.egov.lams.model.Asset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssetResponse {

	private ResponseInfo responseInfo;
	private List<Asset> assets;
}
