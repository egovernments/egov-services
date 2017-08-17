package org.egov.asset.service;

import java.util.Map;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetIndex;
import org.egov.asset.model.Boundary;
import org.egov.asset.model.Location;
import org.egov.asset.model.Tenant;
import org.egov.asset.repository.AssetIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetIndexService {

    @Autowired
    private AssetIndexRepository assetRepository;

    @Autowired
    private AssetIndexCommonService assetIndexCommonService;

    public void postAsset(final AssetRequest assetRequest) {
        final AssetIndex assetIndex = prepareAssetIndex(assetRequest);
        log.info("the logged value of assetIndex ::" + assetIndex);
        assetRepository.saveAsset(assetIndex);
    }

    public void putAsset(final AssetRequest assetRequest) {
        final AssetIndex assetIndex = prepareAssetIndex(assetRequest);
        log.info("the logged value of assetIndex in update ::" + assetIndex);
        assetRepository.updateAsset(assetIndex);
    }

    public AssetIndex prepareAssetIndex(final AssetRequest assetRequest) {
        final AssetIndex assetIndex = new AssetIndex();
        final Asset asset = assetRequest.getAsset();
        final Location location = asset.getLocationDetails();
        assetIndex.setAssetData(asset);
        final Map<Long, Boundary> locationMap = assetRepository.getlocationsById(asset);
        assetIndex.setAssetLocation(location, locationMap);
        setTenantProperties(assetRequest.getRequestInfo(), assetIndex, asset.getTenantId());
        return assetIndex;
    }

    private void setTenantProperties(final RequestInfo requestInfo, final AssetIndex assetIndex,
            final String tenantId) {
        final Tenant tenant = assetIndexCommonService.getTenantData(requestInfo, tenantId).get(0);
        assetIndex.setCityName(tenant.getCity().getName());
        assetIndex.setUlbGrade(tenant.getCity().getUlbGrade());
        assetIndex.setLocalName(tenant.getCity().getLocalName());
        assetIndex.setDistrictCode(tenant.getCity().getDistrictCode());
        assetIndex.setDistrictName(tenant.getCity().getDistrictName());
        assetIndex.setRegionName(tenant.getCity().getRegionName());
    }
}
