package org.egov.asset.contract;

import java.util.List;

import org.egov.asset.contract.ResponseInfo;
import org.egov.asset.model.Asset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@NoArgsConstructor
@Setter
@ToString
public class AssetResponse {

	private ResponseInfo resposneInfo;
	private List<Asset> Asset;
	
}
