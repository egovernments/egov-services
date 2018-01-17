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
