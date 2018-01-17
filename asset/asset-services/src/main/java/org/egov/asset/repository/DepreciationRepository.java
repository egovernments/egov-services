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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.DepreciationInputs;
import org.egov.asset.model.DepreciationReportCriteria;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.repository.builder.DepreciationQueryBuilder;
import org.egov.asset.repository.builder.DepreciationReportQueryBuilder;
import org.egov.asset.repository.rowmapper.CalculationAssetDetailsRowMapper;
import org.egov.asset.repository.rowmapper.CalculationCurrentValueRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationDetailRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationInputRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationReportRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationSumRowMapper;
import org.egov.asset.service.AssetConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DepreciationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CalculationAssetDetailsRowMapper calculationAssetDetailsRowMapper;

    @Autowired
    private CalculationCurrentValueRowMapper calculationCurrentValueRowMapper;

    @Autowired
    private DepreciationQueryBuilder depreciationQueryBuilder;

    @Autowired
    private DepreciationDetailRowMapper depreciationDetailRowMapper;

    @Autowired
    private DepreciationReportQueryBuilder depreciationReportQueryBuilder;

    @Autowired
    private DepreciationSumRowMapper depreciationSumRowMapper;

    @Autowired
    private DepreciationReportRowMapper depreciationReportRowMapper;

    @Autowired
    private AssetConfigurationService assetConfigurationService;
    
    @Autowired
    private DepreciationInputRowMapper depreciationInputRowMapper;

    public List<DepreciationDetail> getDepreciationdetails(final DepreciationCriteria depreciationCriteria) {

        final List<Object> preparedStatementValues = new ArrayList<>();
        final String sql = depreciationQueryBuilder.getDepreciationSearchQuery(depreciationCriteria,
                preparedStatementValues);
        return jdbcTemplate.query(sql, preparedStatementValues.toArray(), depreciationDetailRowMapper);
    }

    public void saveDepreciation(final Depreciation depreciation) {

        final String tenantId = depreciation.getTenantId();
        final String sql = depreciationQueryBuilder.getInsertQuery();
        final List<DepreciationDetail> assetDepreciationvalues = depreciation.getDepreciationDetails();
        final AuditDetails auditDetails = depreciation.getAuditDetails();
        final int batchSize = Integer.parseInt(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETBATCHSIZE, tenantId));

        log.debug("Batch Size :: " + batchSize);

        for (int j = 0; j < assetDepreciationvalues.size(); j += batchSize) {

            final List<DepreciationDetail> batchList = assetDepreciationvalues.subList(j,
                    j + batchSize > assetDepreciationvalues.size() ? assetDepreciationvalues.size() : j + batchSize);

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int rowNum) throws SQLException {

                    final DepreciationDetail depreciationDetail = assetDepreciationvalues.get(rowNum);
                    String reasonForFailure = null;
                    if (depreciationDetail.getReasonForFailure() != null)
                        reasonForFailure = depreciationDetail.getReasonForFailure().toString();

                    ps.setLong(1, depreciationDetail.getId());
                    ps.setLong(2, depreciationDetail.getAssetId());
                    ps.setString(3, depreciation.getFinancialYear());
                    ps.setLong(4, depreciation.getFromDate());
                    ps.setLong(5, depreciation.getToDate());
                    ps.setObject(6, depreciationDetail.getVoucherReference());
                    ps.setString(7, depreciation.getTenantId());
                    ps.setString(8, depreciationDetail.getStatus().toString());
                    ps.setObject(9, depreciationDetail.getDepreciationRate());
                    ps.setBigDecimal(10, depreciationDetail.getValueBeforeDepreciation());
                    ps.setBigDecimal(11, depreciationDetail.getDepreciationValue());
                    ps.setBigDecimal(12, depreciationDetail.getValueAfterDepreciation());
                    ps.setString(13, auditDetails.getCreatedBy());
                    ps.setLong(14, auditDetails.getCreatedDate());
                    ps.setString(15, auditDetails.getLastModifiedBy());
                    ps.setLong(16, auditDetails.getLastModifiedDate());
                    ps.setString(17, reasonForFailure);
                }

                @Override
                public int getBatchSize() {
                    return batchList.size();
                }
            });
        }
    }

    public Map<Long, BigDecimal> getdepreciationSum(final String tenantId) {

        final String sql = depreciationQueryBuilder.getDepreciationSumQuery(tenantId);
        return jdbcTemplate.query(sql, depreciationSumRowMapper);
    }

    public List<CalculationAssetDetails> getCalculationAssetDetails(final DepreciationCriteria depreciationCriteria) {

        final String sql = depreciationQueryBuilder.getCalculationAssetDetailsQuery(depreciationCriteria);
        log.debug("the CalculationAssetDetails query-- " + sql);
        return jdbcTemplate.query(sql, calculationAssetDetailsRowMapper);
    }

    public List<CalculationCurrentValue> getCalculationCurrentvalue(final DepreciationCriteria depreciationCriteria) {

        final String tenantId = depreciationCriteria.getTenantId();
        final Integer year = Integer.parseInt(depreciationCriteria.getFinancialYear().substring(0, 4));
        final String[] sepDate = assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONSEPARATIONDATE, tenantId)
                .split("/");
        final Long seprationDate = Date
                .from(LocalDateTime.of(year, Integer.parseInt(sepDate[0]), Integer.parseInt(sepDate[1]),
                        Integer.parseInt(sepDate[2]), Integer.parseInt(sepDate[3]), Integer.parseInt(sepDate[4]))
                        .toInstant(ZoneOffset.UTC))
                .getTime();
        final String sql = depreciationQueryBuilder.getCalculationCurrentvalueQuery(depreciationCriteria.getAssetIds());
        final List<Object> pSValues = new ArrayList<>();
        pSValues.add(seprationDate);
        pSValues.add(tenantId);
        pSValues.add(seprationDate);
        pSValues.add(tenantId);
        log.debug("the calculation currentvalue query --" + sql + "\n --preparedstatementvalues---" + pSValues);

        return jdbcTemplate.query(sql, pSValues.toArray(), calculationCurrentValueRowMapper);
    }

    public List<DepreciationReportCriteria> getDepreciatedAsset(final DepreciationReportCriteria depreciationReportCriteria) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = depreciationReportQueryBuilder.getQuery(depreciationReportCriteria,
                preparedStatementValues);
        List<DepreciationReportCriteria> assets = new ArrayList<>();
        try {
            log.debug("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
            assets = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), depreciationReportRowMapper);
            log.debug("DepreciationRepository::" + depreciationReportCriteria);
        } catch (final Exception ex) {
            log.debug("the exception from findforcriteria : " + ex);
        }
        return assets;
    }
    
   
    
    public List<DepreciationInputs> getDepreciationInputs(DepreciationCriteria depreciationCriteria) {
    
            List<Object> preparedStatementValues = new ArrayList<>();
            String sql = depreciationQueryBuilder.getDepreciationQuery(depreciationCriteria, preparedStatementValues);
            log.info("the query for depreciation inputs : "+sql);
            log.info("prepared stsmt values : "+preparedStatementValues);
            return jdbcTemplate.query(sql, preparedStatementValues.toArray(), depreciationInputRowMapper);
    }

}
