package org.egov.asset.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AssetStatusCriteria;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.StatusValue;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.builder.AssetStatusQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetStatusRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class AssetMasterRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AssetStatusQueryBuilder assetStatusQueryBuilder;

    @Mock
    private AssetStatusRowMapper assetStatusRowMapper;

    @InjectMocks
    private AssetMasterRepository assetMasterRepository;

    @Test
    public void test_search() {
        final List<AssetStatus> expectedAssetStatuses = getAssetStatuses();
        final AssetStatusCriteria assetStatusCriteria = getAssetStatusCriteria();

        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(AssetStatusRowMapper.class)))
                .thenReturn(expectedAssetStatuses);
        final List<AssetStatus> actualAssetStauses = assetMasterRepository.search(assetStatusCriteria);

        assertEquals(expectedAssetStatuses.toString(), actualAssetStauses.toString());
    }

    private List<AssetStatus> getAssetStatuses() {
        final List<AssetStatus> assetStatus = new ArrayList<AssetStatus>();
        final List<StatusValue> statusValues = new ArrayList<StatusValue>();
        final StatusValue statusValue = new StatusValue();
        final AssetStatus asStatus = new AssetStatus();
        asStatus.setObjectName(AssetStatusObjectName.REVALUATION.toString());
        asStatus.setAuditDetails(getAuditDetails());
        statusValue.setCode(Status.APPROVED.toString());
        statusValue.setName(Status.APPROVED.toString());
        statusValue.setDescription("Asset Revaluation is created");
        statusValues.add(statusValue);
        asStatus.setStatusValues(statusValues);
        assetStatus.add(asStatus);
        return assetStatus;
    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(String.valueOf("5"));
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy(String.valueOf("5"));
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        return auditDetails;
    }

    private AssetStatusCriteria getAssetStatusCriteria() {
        final AssetStatusCriteria assetStatusCriteria = new AssetStatusCriteria();
        assetStatusCriteria.setCode(Status.APPROVED.toString());
        assetStatusCriteria.setObjectName(AssetStatusObjectName.REVALUATION.toString());
        assetStatusCriteria.setTenantId("ap.kurnool");
        return assetStatusCriteria;
    }

}
