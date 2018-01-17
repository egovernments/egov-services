/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

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
