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
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.repository.builder.DepreciationQueryBuilder;
import org.egov.asset.repository.rowmapper.CalculationAssetDetailsRowMapper;
import org.egov.asset.repository.rowmapper.CalculationCurrentValueRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationDetailRowMapper;
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

    @Test
    public void test_saveDepreciation() {
        final Depreciation depreciation = Depreciation.builder().depreciationCriteria(getDepreciationCriteria())
                .depreciationDetails(getDepreciationDetails()).build();

        when(applicationProperties.getBatchSize()).thenReturn("500");
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
}
