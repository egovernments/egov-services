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
