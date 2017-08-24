package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.contract.AssetConfigurationResponse;
import org.egov.asset.model.AssetConfigurationCriteria;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.repository.AssetConfigurationRepository;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssetConfigurationServiceTest {

    @Mock
    private AssetConfigurationRepository assetConfigurationRepository;

    @InjectMocks
    private AssetConfigurationService assetConfigurationService;

    @Test
    public void testSearch() {
        final AssetConfigurationResponse expectedAssetConfigurationResponse = getAssetConfigurationResponse();
        when(assetConfigurationRepository.findForCriteria(any(AssetConfigurationCriteria.class)))
                .thenReturn(expectedAssetConfigurationResponse.getAssetConfiguration());
        final AssetConfigurationResponse actualAssetConfigurationResponse = assetConfigurationService
                .search(any(AssetConfigurationCriteria.class));
        assertEquals(expectedAssetConfigurationResponse.toString(), actualAssetConfigurationResponse.toString());
    }

    @Test
    public void testGetAssetConfigValueByKeyAndTenantId() {
        final Map<String, List<String>> assetConfiguration = getAssetConfiguration();
        final String expectedConfigValue = assetConfiguration
                .get(AssetConfigurationKeys.ENABLEVOUCHERGENERATION.toString()).get(0);
        when(assetConfigurationRepository.findForCriteria(any(AssetConfigurationCriteria.class)))
                .thenReturn(assetConfiguration);
        final String actualConfigValue = assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ENABLEVOUCHERGENERATION, "ap.kurnool");
        assertEquals(expectedConfigValue, actualConfigValue);
    }

    @Test
    public void testGetEnabledVoucherGeneration() {
        final Map<String, List<String>> assetConfiguration = getAssetConfiguration();
        when(assetConfigurationRepository.findForCriteria(any(AssetConfigurationCriteria.class)))
                .thenReturn(assetConfiguration);
        final boolean actualConfigValue = assetConfigurationService
                .getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION, "ap.kurnool");
        assertEquals(true, actualConfigValue);
    }

    private AssetConfigurationResponse getAssetConfigurationResponse() {
        final AssetConfigurationResponse assetConfigurationResponse = new AssetConfigurationResponse();
        final Map<String, List<String>> assetConfiguration = getAssetConfiguration();
        assetConfigurationResponse.setAssetConfiguration(assetConfiguration);
        assetConfigurationResponse.setResponseInfo(new ResponseInfo());
        return assetConfigurationResponse;
    }

    private Map<String, List<String>> getAssetConfiguration() {
        final Map<String, List<String>> assetConfiguration = new HashMap<>();
        final List<String> configValues = new ArrayList<>();
        configValues.add("true");
        assetConfiguration.put(AssetConfigurationKeys.ENABLEVOUCHERGENERATION.toString(), configValues);
        return assetConfiguration;
    }

}
