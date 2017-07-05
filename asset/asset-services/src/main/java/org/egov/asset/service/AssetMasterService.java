package org.egov.asset.service;

import java.util.List;

import org.egov.asset.contract.AssetStatusResponse;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AssetStatusCriteria;
import org.egov.asset.repository.AssetMasterRepository;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetMasterService {

	@Autowired
	private AssetMasterRepository assetMasterRepository;

	public AssetStatusResponse search(final AssetStatusCriteria assetStatusCriteria, final RequestInfo requestInfo) {
		final AssetStatusResponse assetStatusResponse = new AssetStatusResponse();
		assetStatusResponse.setResponseInfo(new ResponseInfo());

		final List<AssetStatus> assetStatuses = assetMasterRepository.search(assetStatusCriteria);
		assetStatusResponse.setAssetStatus(assetStatuses);
		return assetStatusResponse;
	}
}