package org.egov.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.model.Revaluation;
import org.egov.model.criteria.RevaluationCriteria;
import org.egov.repository.querybuilder.RevaluationQueryBuilder;
import org.egov.repository.rowmapper.RevaluationRowMapper;
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

    //@Autowired private AssetMasterService assetMasterService;

   

  /*  public String getRevaluationStatus( AssetStatusObjectName objectName,  Status code,  String tenantId) {
        final List<AssetStatus> assetStatuses = assetMasterService.getStatuses(objectName, code, tenantId);
        if (assetStatuses != null && !assetStatuses.isEmpty())
            return assetStatuses.get(0).getStatusValues().get(0).getCode();
        else
            throw new RuntimeException("Status is not present Revaluation for tenant id : " + tenantId);
    }*/

    public List<Revaluation> search(final RevaluationCriteria revaluationCriteria) {

        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = revaluationQueryBuilder.getQuery(revaluationCriteria, preparedStatementValues);
        return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), revaluationRowMapper);
    }

}
