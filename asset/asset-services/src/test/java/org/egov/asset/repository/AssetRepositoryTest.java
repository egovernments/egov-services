package org.egov.asset.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.Department;
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.builder.AssetQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetRowMapper;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetMasterService;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AssetRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AssetRowMapper assetRowMapper;

    @Mock
    private AssetQueryBuilder assetQueryBuilder;

    @InjectMocks
    private AssetRepository assetRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AssetMasterService assetMasterService;

    @Mock
    private AssetCommonService assetCommonService;

    @Test
    public void testFindForCriteria() {

        final List<Asset> assets = new ArrayList<>();
        assets.add(getAsset());
        when(assetQueryBuilder.getQuery(any(AssetCriteria.class), any(List.class))).thenReturn(StringUtils.EMPTY);
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(AssetRowMapper.class))).thenReturn(assets);

        assertTrue(assets.equals(assetRepository.findForCriteria(new AssetCriteria())));
    }

    @Test
    public void testCreateAsset() {

        final AssetRequest assetRequest = new AssetRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        assetRequest.setRequestInfo(requestInfo);
        final Asset asset = getAsset();
        assetRequest.setAsset(asset);

        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(asset.equals(assetRepository.create(assetRequest)));
    }

    @Test
    public void testUpdateAsset() {
        final AssetRequest assetRequest = new AssetRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        assetRequest.setRequestInfo(requestInfo);
        final Asset asset = getAsset();
        assetRequest.setAsset(asset);
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);

        assertTrue(asset.equals(assetRepository.update(assetRequest)));
    }

    private Asset getAsset() {
        final Asset asset = new Asset();
        asset.setTenantId("ap.kurnool");
        asset.setName("asset name");
        asset.setStatus(Status.CREATED.toString());
        asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);
        asset.setEnableYearWiseDepreciation(Boolean.FALSE);
        asset.setDepreciationRate(Double.valueOf("6.33"));

        final Location location = new Location();
        location.setLocality(4l);
        location.setDoorNo("door no");

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setId(1l);
        assetCategory.setName("category name");

        asset.setLocationDetails(location);
        asset.setAssetCategory(assetCategory);

        final Department department = new Department();
        asset.setDepartment(department);

        return asset;
    }

}
