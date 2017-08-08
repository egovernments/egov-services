package org.egov.asset.service;

import java.util.List;
import java.util.Map;

import org.egov.asset.contract.AssetConfigurationResponse;
import org.egov.asset.model.AssetConfigurationCriteria;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.repository.AssetConfigurationRepository;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetConfigurationService {

    @Autowired
    private AssetConfigurationRepository assetConfigurationRepository;

    public AssetConfigurationResponse search(final AssetConfigurationCriteria assetConfigurationCriteria) {
        final AssetConfigurationResponse assetConfigurationResponse = new AssetConfigurationResponse();
        assetConfigurationResponse.setResponseInfo(new ResponseInfo());

        final Map<String, List<String>> assetConfiguration = assetConfigurationRepository
                .findForCriteria(assetConfigurationCriteria);
        assetConfigurationResponse.setAssetConfiguration(assetConfiguration);
        return assetConfigurationResponse;
    }

    public boolean getEnabledVoucherGeneration(final AssetConfigurationKeys assetConfigurationKey,
            final String tenantId) {
        final String value = getAssetConfigValueByKeyAndTenantId(assetConfigurationKey, tenantId);
        if (Boolean.TRUE.toString().equalsIgnoreCase(value))
            return true;
        else
            return false;
    }

    public String getAssetConfigValueByKeyAndTenantId(final AssetConfigurationKeys assetConfigurationKey,
            final String tenantId) {
        final AssetConfigurationCriteria assetConfigurationCriteria = AssetConfigurationCriteria.builder()
                .name(assetConfigurationKey.toString()).tenantId(tenantId).build();
        final Map<String, List<String>> assetConfiguration = assetConfigurationRepository
                .findForCriteria(assetConfigurationCriteria);
        if(assetConfiguration.isEmpty())
        	throw new RuntimeException("no asset configuration found for the key--"+assetConfigurationKey.toString()
        	+" tenant value--"+tenantId);
        return assetConfiguration.get(assetConfigurationKey.toString()).get(0);
    }

}
