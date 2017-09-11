package org.egov.asset.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.repository.builder.CurrentValueQueryBuilder;
import org.egov.asset.repository.rowmapper.CurrentValueRowMapper;
import org.egov.asset.service.AssetConfigurationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class CurrentValueRepositoryTest {

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private CurrentValueQueryBuilder currentValueQueryBuilder;

    @Mock
    private CurrentValueRowMapper currentValueRowMapper;

    @InjectMocks
    private CurrentValueRepository currentValueRepository;

    @Test
    public void test_getCurrentValues() {
        final List<AssetCurrentValue> expectedAssetCurrentValues = getAssetCurrentValues();
        final Set<Long> assetIds = new HashSet<Long>();
        assetIds.add(Long.valueOf("2"));
        when(jdbcTemplate.query(any(String.class), any(CurrentValueRowMapper.class)))
                .thenReturn(expectedAssetCurrentValues);
        final List<AssetCurrentValue> actualAssetCurrentValues = currentValueRepository.getCurrentValues(assetIds,
                "ap.kurnool");

        assertEquals(expectedAssetCurrentValues.toString(), actualAssetCurrentValues.toString());

    }

    @Test
    public void test_create() {
        when(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETBATCHSIZE,
                "ap.kurnool")).thenReturn("500");
        currentValueRepository.create(getAssetCurrentValues());
    }

    private List<AssetCurrentValue> getAssetCurrentValues() {
        final List<AssetCurrentValue> assetCurrentValues = new ArrayList<AssetCurrentValue>();
        final AssetCurrentValue assetCurrentValue = new AssetCurrentValue();
        assetCurrentValue.setAssetId(2L);
        assetCurrentValue.setAssetTranType(TransactionType.DISPOSAL);
        assetCurrentValue.setAuditDetails(getAuditDetails());
        assetCurrentValue.setId(1L);
        assetCurrentValue.setTenantId("ap.kurnool");
        assetCurrentValues.add(assetCurrentValue);
        return assetCurrentValues;
    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(String.valueOf("5"));
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy(String.valueOf("5"));
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        return auditDetails;
    }

}
