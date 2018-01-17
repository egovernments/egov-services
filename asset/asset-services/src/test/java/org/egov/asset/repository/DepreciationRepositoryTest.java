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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.DepreciationReportCriteria;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.repository.builder.DepreciationQueryBuilder;
import org.egov.asset.repository.builder.DepreciationReportQueryBuilder;
import org.egov.asset.repository.rowmapper.CalculationAssetDetailsRowMapper;
import org.egov.asset.repository.rowmapper.CalculationCurrentValueRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationDetailRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationReportRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationSumRowMapper;
import org.egov.asset.service.AssetConfigurationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DepreciationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private CalculationAssetDetailsRowMapper calculationAssetDetailsRowMapper;

    @Mock
    private CalculationCurrentValueRowMapper calculationCurrentValueRowMapper;

    @Mock
    private DepreciationQueryBuilder depreciationQueryBuilder;

    @Mock
    private DepreciationDetailRowMapper depreciationDetailRowMapper;

    @Mock
    private DepreciationSumRowMapper depreciationSumRowMapper;

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @InjectMocks
    private DepreciationRepository depreciationRepository;

    @Mock
    private DepreciationReportQueryBuilder depreciationReportQueryBuilder;

    @Mock
    private DepreciationReportRowMapper depreciationReportRowMapper;

    @SuppressWarnings("unchecked")
    @Test
    public void test_get_depreciation_details() {

        final ArrayList<DepreciationDetail> depreciationDetails = new ArrayList<>();
        when(depreciationQueryBuilder.getDepreciationSearchQuery(any(DepreciationCriteria.class), any(ArrayList.class)))
                .thenReturn(StringUtils.EMPTY);
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(DepreciationDetailRowMapper.class)))
                .thenReturn(depreciationDetails);

        assertTrue(
                depreciationDetails.equals(depreciationRepository.getDepreciationdetails(new DepreciationCriteria())));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetDepreciatedAssets() {

        final List<DepreciationReportCriteria> depreciationReport = new ArrayList<>();
        depreciationReport.add(getAssetDepreciationReport());

        when(depreciationReportQueryBuilder.getQuery(any(DepreciationReportCriteria.class), any(List.class)))
                .thenReturn(StringUtils.EMPTY);
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(DepreciationReportRowMapper.class)))
                .thenReturn(depreciationReport);

        final DepreciationReportCriteria depreciationReportCriteria = DepreciationReportCriteria.builder()
                .assetCategoryName("category name")
                .tenantId("ap.kurnool").build();
        final List<DepreciationReportCriteria> actualAssets = depreciationRepository
                .getDepreciatedAsset(depreciationReportCriteria);

        assertEquals(depreciationReport, actualAssets);
    }

    @Test
    public void test_saveDepreciation() {
        final Depreciation depreciation = Depreciation.builder().tenantId("ap.kurnool")
                .depreciationCriteria(getDepreciationCriteria()).depreciationDetails(getDepreciationDetails()).build();

        when(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETBATCHSIZE,
                "ap.kurnool")).thenReturn("500");
        depreciationRepository.saveDepreciation(depreciation);
    }

    @Test
    public void test_getCalculationCurrentvalue() {
        when(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(any(AssetConfigurationKeys.class),
                any(String.class))).thenReturn("9/30/23/59/59");
        final List<CalculationCurrentValue> actualCalculationCurrentValues = depreciationRepository
                .getCalculationCurrentvalue(getDepreciationCriteria());

        assertEquals(new ArrayList<>().toString(), actualCalculationCurrentValues.toString());
    }

    @Test
    public void test_getdepreciationSum() {
        final Map<Long, BigDecimal> expectedDepreciationSum = getDepreciationSumMap();

        final String query = "select assetid,SUM(depreciationValue) AS totaldepreciationvalue FROM egasset_depreciation "
                + " where tenantid='ap.kurnool' group by assetid;";
        when(depreciationQueryBuilder.getDepreciationSumQuery("ap.kurnool")).thenReturn(query);
        when(jdbcTemplate.query(any(String.class), any(DepreciationSumRowMapper.class)))
                .thenReturn(expectedDepreciationSum);
        final Map<Long, BigDecimal> actualDepreciationSum = depreciationRepository.getdepreciationSum("ap.kurnool");

        assertEquals(expectedDepreciationSum.toString(), actualDepreciationSum.toString());
    }

    @Test
    public void test_getCalculationAssetDetails() {
        final List<CalculationAssetDetails> expectedCalculationAssetDetails = getCalculationAssetDetailList();

        when(jdbcTemplate.query(any(String.class), any(CalculationAssetDetailsRowMapper.class)))
                .thenReturn(expectedCalculationAssetDetails);
        final List<CalculationAssetDetails> actualCalculationAssetDetails = depreciationRepository
                .getCalculationAssetDetails(getDepreciationCriteria());

        assertEquals(expectedCalculationAssetDetails.toString(), actualCalculationAssetDetails.toString());
    }

    private Map<Long, BigDecimal> getDepreciationSumMap() {
        final Map<Long, BigDecimal> depreciationSumMap = new HashMap<Long, BigDecimal>();
        depreciationSumMap.put(Long.valueOf("552"), new BigDecimal("3200"));
        return depreciationSumMap;
    }

    private DepreciationCriteria getDepreciationCriteria() {
        final DepreciationCriteria depreciationCriteria = new DepreciationCriteria();
        depreciationCriteria.setAssetIds(null);
        depreciationCriteria.setFinancialYear("2017-18");
        depreciationCriteria.setFromDate(null);
        depreciationCriteria.setToDate(null);
        depreciationCriteria.setTenantId("ap.kurnool");
        return depreciationCriteria;
    }

    private List<DepreciationDetail> getDepreciationDetails() {
        final List<DepreciationDetail> depreciationDetails = new ArrayList<DepreciationDetail>();
        final DepreciationDetail depreciationDetail = DepreciationDetail.builder().assetId(Long.valueOf("552"))
                .depreciationRate(Double.valueOf("20")).depreciationValue(new BigDecimal("3200"))
                .status(DepreciationStatus.SUCCESS).reasonForFailure(null)
                .valueBeforeDepreciation(new BigDecimal("16000")).valueAfterDepreciation(new BigDecimal("12800"))
                .voucherReference("1/GJV/00000219/08/2017-18").build();
        depreciationDetails.add(depreciationDetail);
        return depreciationDetails;
    }

    private List<CalculationAssetDetails> getCalculationAssetDetailList() {
        final List<CalculationAssetDetails> calculationAssetDetails = new ArrayList<CalculationAssetDetails>();
        final CalculationAssetDetails calculationAssetDetail = new CalculationAssetDetails();
        calculationAssetDetail.setAccumulatedDepreciation(BigDecimal.ZERO);
        calculationAssetDetail.setAccumulatedDepreciationAccount(Long.valueOf("1947"));
        calculationAssetDetail.setAssetCategoryDepreciationRate(null);
        calculationAssetDetail.setAssetCategoryId(Long.valueOf("196"));
        calculationAssetDetail.setAssetCategoryName("Kalyana Mandapam");
        calculationAssetDetail.setAssetDepreciationRate(Double.valueOf("16.53"));
        calculationAssetDetail.setAssetId(Long.valueOf("597"));
        calculationAssetDetail.setAssetReference(null);
        calculationAssetDetail.setDepartmentId(Long.valueOf("5"));
        calculationAssetDetail.setDepreciationExpenseAccount(Long.valueOf("1906"));
        calculationAssetDetail.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
        calculationAssetDetail.setEnableYearWiseDepreciation(true);
        calculationAssetDetail.setFinancialyear("2017-18");
        calculationAssetDetail.setGrossValue(new BigDecimal("15000"));
        calculationAssetDetail.setYearwisedepreciationrate(Double.valueOf("15"));
        calculationAssetDetails.add(calculationAssetDetail);
        return calculationAssetDetails;
    }

    private DepreciationReportCriteria getAssetDepreciationReport() {
        final DepreciationReportCriteria depreciation = new DepreciationReportCriteria();
        depreciation.setId(Long.valueOf("1"));
        depreciation.setTenantId("ap.kurnool");
        depreciation.setAssetId(null);
        depreciation.setFinancialYear("2017-18");
        depreciation.setAssetCategory(Long.valueOf("1"));
        depreciation.setAssetCategoryName("abcd");
        depreciation.setAssetCategoryType("IMMOVABLE");
        depreciation.setAssetCode("00001");
        depreciation.setAssetName("abcd");
        depreciation.setDepreciationRate(Double.valueOf("14"));
        depreciation.setDepreciationValue(new BigDecimal("700"));
        depreciation.setValueAfterDepreciation(new BigDecimal("4300"));
        return depreciation;
    }
}
