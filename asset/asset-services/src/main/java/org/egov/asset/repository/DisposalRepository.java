package org.egov.asset.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.repository.builder.DisposalQueryBuilder;
import org.egov.asset.repository.rowmapper.DisposalRowMapper;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DisposalRepository {

    @Autowired
    private DisposalQueryBuilder disposalQueryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DisposalRowMapper disposalRowMapper;

    public List<Disposal> search(final DisposalCriteria disposalCriteria) {

        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = disposalQueryBuilder.getQuery(disposalCriteria, preparedStatementValues);
        List<Disposal> disposals = null;
        try {
            log.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
            disposals = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), disposalRowMapper);
            log.info("DisposalRepository::" + disposals);
        } catch (final Exception ex) {
            log.info("the exception from findforcriteria : " + ex);
        }
        return disposals;
    }

    public void create(final DisposalRequest disposalRequest) {

        final RequestInfo requestInfo = disposalRequest.getRequestInfo();
        final Disposal disposal = disposalRequest.getDisposal();

        final Object[] values = { disposal.getId(), disposal.getTenantId(), disposal.getAssetId(),
                disposal.getBuyerName(), disposal.getBuyerAddress(), disposal.getDisposalReason(),
                disposal.getDisposalDate(), disposal.getPanCardNumber(), disposal.getAadharCardNumber(),
                disposal.getAssetCurrentValue(), disposal.getSaleValue(), disposal.getTransactionType().toString(),
                disposal.getAssetSaleAccount(), requestInfo.getUserInfo().getId(), System.currentTimeMillis(),
                requestInfo.getUserInfo().getId(), System.currentTimeMillis(),
                disposal.getProfitLossVoucherReference() };

        try {
            jdbcTemplate.update(DisposalQueryBuilder.INSERT_QUERY, values);
        } catch (final Exception ex) {
            log.info("DisposalRepository:", ex);
            throw new RuntimeException(ex);
        }
    }

}
