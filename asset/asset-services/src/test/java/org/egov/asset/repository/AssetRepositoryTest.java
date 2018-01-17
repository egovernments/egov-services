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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Department;
import org.egov.asset.model.Location;
import org.egov.asset.model.StatusValue;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.builder.AssetQueryBuilder;
import org.egov.asset.repository.builder.DepreciationReportQueryBuilder;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(AssetRepository.class)
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

    @Mock
    private DepreciationReportQueryBuilder depreciationReportQueryBuilder;

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

        final List<AssetStatus> assetStatuses = getAssetStatuses();
        final List<Asset> assets = new ArrayList<Asset>();
        assets.add(asset);

        final AssetCriteria assetCriteria = AssetCriteria.builder().code("000002").department(Long.valueOf("5"))
                .build();
        when(assetMasterService.getStatuses(any(AssetStatusObjectName.class), any(Status.class), any(String.class)))
                .thenReturn(assetStatuses);
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(AssetRowMapper.class))).thenReturn(assets);
        when(assetQueryBuilder.getQuery(any(AssetCriteria.class), any(ArrayList.class))).thenReturn(StringUtils.EMPTY);
        when(assetRepository.findForCriteria(assetCriteria)).thenReturn(assets);
        when(assetRepository.findAssetByCode("000002","ap.kurnool")).thenReturn(assets);
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);

        assertTrue(asset.equals(assetRepository.update(assetRequest)));
    }

    private Asset getAsset() {
        final Asset asset = new Asset();
        asset.setTenantId("ap.kurnool");
        asset.setCode("000002");
        asset.setName("asset name");
        asset.setStatus(Status.CREATED.toString());
        asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);
        asset.setEnableYearWiseDepreciation(Boolean.TRUE);
        asset.setDepreciationRate(Double.valueOf("6.33"));
        asset.setSurveyNumber("123");
        asset.setMarketValue(new BigDecimal("700"));
        final Location location = new Location();
        location.setLocality(4l);
        location.setDoorNo("door no");

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setId(1l);
        assetCategory.setName("category name");
        assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
        asset.setLocationDetails(location);
        asset.setAssetCategory(assetCategory);

        final Department department = new Department();
        department.setId(Long.valueOf("5"));
        department.setCode("ENG");
        department.setName("ENGINEERING");
        asset.setDepartment(department);

        return asset;
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

}
