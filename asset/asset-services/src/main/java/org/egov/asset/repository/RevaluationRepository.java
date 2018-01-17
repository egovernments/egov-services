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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.builder.RevaluationQueryBuilder;
import org.egov.asset.repository.rowmapper.RevaluationRowMapper;
import org.egov.asset.service.AssetMasterService;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class RevaluationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RevaluationQueryBuilder revaluationQueryBuilder;

    @Autowired
    private RevaluationRowMapper revaluationRowMapper;

    @Autowired
    private AssetMasterService assetMasterService;

    public void create(final RevaluationRequest revaluationRequest) {

        log.info("RevaluationRepository create revaluationRequest:" + revaluationRequest);
        final Revaluation revaluation = revaluationRequest.getRevaluation();
        final RequestInfo requestInfo = revaluationRequest.getRequestInfo();

        final String sql = RevaluationQueryBuilder.INSERT_QUERY;

        final String status = getRevaluationStatus(AssetStatusObjectName.REVALUATION, Status.APPROVED,
                revaluation.getTenantId());

        final Object[] obj = new Object[] { revaluation.getId(), revaluation.getTenantId(), revaluation.getAssetId(),
                revaluation.getCurrentCapitalizedValue(), revaluation.getTypeOfChange().toString(),
                revaluation.getRevaluationAmount(), revaluation.getValueAfterRevaluation(),
                revaluation.getRevaluationDate(), requestInfo.getUserInfo().getId().toString(),
                revaluation.getReasonForRevaluation(), revaluation.getFixedAssetsWrittenOffAccount(),
                revaluation.getFunction(), revaluation.getFund(), revaluation.getScheme(), revaluation.getSubScheme(),
                revaluation.getComments(), status, requestInfo.getUserInfo().getId(), new Date().getTime(),
                requestInfo.getUserInfo().getId(), new Date().getTime(), revaluation.getVoucherReference() };
        try {
            jdbcTemplate.update(sql, obj);
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.info("RevaluationRepository create:" + ex.getMessage());
        }
    }

    public String getRevaluationStatus(final AssetStatusObjectName objectName, final Status code,
            final String tenantId) {
        final List<AssetStatus> assetStatuses = assetMasterService.getStatuses(objectName, code, tenantId);
        if (assetStatuses != null && !assetStatuses.isEmpty())
            return assetStatuses.get(0).getStatusValues().get(0).getCode();
        else
            throw new RuntimeException("Status is not present Revaluation for tenant id : " + tenantId);
    }

    public List<Revaluation> search(final RevaluationCriteria revaluationCriteria) {

        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = revaluationQueryBuilder.getQuery(revaluationCriteria, preparedStatementValues);
        List<Revaluation> revaluations = null;
        try {
            log.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
            revaluations = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), revaluationRowMapper);
            log.info("RevaluationRepository::" + revaluations);
        } catch (final Exception ex) {
            log.info("the exception from findforcriteria : " + ex);
        }
        return revaluations;
    }

}
