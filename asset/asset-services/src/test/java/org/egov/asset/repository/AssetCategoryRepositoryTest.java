package org.egov.asset.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.repository.builder.AssetCategoryQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetCategoryRowMapper;
import org.egov.asset.service.AssetCommonService;
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
public class AssetCategoryRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AssetCategoryQueryBuilder assetCategoryQueryBuilder;

    @InjectMocks
    private AssetCategoryRepository assetCategoryRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AssetCommonService assetCommonService;

    @SuppressWarnings("unchecked")
    @Test
    public void testSearch() {

        final List<AssetCategory> assetCategories = new ArrayList<>();
        final AssetCategory assetCategory = getAssetCategory();
        assetCategories.add(assetCategory);

        when(assetCategoryQueryBuilder.getQuery(any(AssetCategoryCriteria.class), any(List.class)))
                .thenReturn(StringUtils.EMPTY);
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(AssetCategoryRowMapper.class)))
                .thenReturn(assetCategories);

        assertTrue(assetCategories.equals(assetCategoryRepository.search(new AssetCategoryCriteria())));
    }

    @Test
    public void testCreate() {

        final AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        assetCategoryRequest.setRequestInfo(requestInfo);
        final AssetCategory assetCategory = getAssetCategory();
        assetCategoryRequest.setAssetCategory(assetCategory);

        when(assetCategoryQueryBuilder.getInsertQuery()).thenReturn(StringUtils.EMPTY);
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);

        assertTrue(assetCategory.equals(assetCategoryRepository.create(assetCategoryRequest)));
    }

    @Test
    public void testUpdate() {

        final AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        assetCategoryRequest.setRequestInfo(requestInfo);
        final AssetCategory assetCategory = getAssetCategory();
        assetCategoryRequest.setAssetCategory(assetCategory);

        when(assetCategoryQueryBuilder.getUpdateQuery()).thenReturn(StringUtils.EMPTY);
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);

        assertTrue(assetCategory.equals(assetCategoryRepository.update(assetCategoryRequest)));
    }

    private AssetCategory getAssetCategory() {

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setTenantId("ap.kurnool");
        assetCategory.setVersion("v1");
        assetCategory.setName("assetcategory3");
        assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
        assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
        assetCategory.setDepreciationRate(Double.valueOf("6.33"));
        assetCategory.setIsAssetAllow(true);
        assetCategory.setAssetAccount(2l);

        return assetCategory;
    }
}
