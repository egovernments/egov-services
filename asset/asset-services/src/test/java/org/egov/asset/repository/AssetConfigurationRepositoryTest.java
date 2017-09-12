package org.egov.asset.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.model.AssetConfigurationCriteria;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.repository.builder.AssetConfigurationQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetConfigurationKeyValuesRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class AssetConfigurationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AssetConfigurationKeyValuesRowMapper assetConfigurationKeyValuesRowMapper;

    @Mock
    private AssetConfigurationQueryBuilder assetConfigurationQueryBuilder;

    @InjectMocks
    private AssetConfigurationRepository assetConfigurationRepository;

    @Test
    public void test_findForCriteria() {
        final Map<String, List<String>> expectedAssetConfigurationMap = getAssetConfigurationMap();
        final AssetConfigurationCriteria assetConfigurationCriteria = getAssetConfigurationCriteria();

        when(jdbcTemplate.query(any(String.class), any(Object[].class),
                any(AssetConfigurationKeyValuesRowMapper.class))).thenReturn(expectedAssetConfigurationMap);
        final Map<String, List<String>> actualAssetConfigurationMap = assetConfigurationRepository
                .findForCriteria(assetConfigurationCriteria);

        assertEquals(expectedAssetConfigurationMap.toString(), actualAssetConfigurationMap.toString());
    }

    private AssetConfigurationCriteria getAssetConfigurationCriteria() {
        final AssetConfigurationCriteria assetConfigurationCriteria = new AssetConfigurationCriteria();
        assetConfigurationCriteria.setName(AssetConfigurationKeys.ENABLEVOUCHERGENERATION.toString());
        assetConfigurationCriteria.setTenantId("ap.kurnool");
        return assetConfigurationCriteria;
    }

    private Map<String, List<String>> getAssetConfigurationMap() {
        final Map<String, List<String>> assetConfiguration = new HashMap<String, List<String>>();
        final List<String> configValues = new ArrayList<String>();
        configValues.add("true");
        assetConfiguration.put(AssetConfigurationKeys.ENABLEVOUCHERGENERATION.toString(), configValues);
        return assetConfiguration;
    }

}
