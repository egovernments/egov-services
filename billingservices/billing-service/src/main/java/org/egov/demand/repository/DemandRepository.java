/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.demand.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.DemandDetailCriteria;
import org.egov.demand.repository.querybuilder.DemandQueryBuilder;
import org.egov.demand.repository.rowmapper.DemandDetailRowMapper;
import org.egov.demand.repository.rowmapper.DemandRowMapper;
import org.egov.demand.web.contract.DemandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DemandRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public void save(DemandRequest demandRequest) {

		log.debug("the request object : " + demandRequest);
		List<Demand> demands = demandRequest.getDemands();
		List<DemandDetail> demandDetails = new ArrayList<>();
		for (Demand demand : demands) {
			demandDetails.addAll(demand.getDemandDetails());
		}

		String demandInsertQuery = DemandQueryBuilder.DEMAND_INSERT_QUERY;
		List<Object[]> demandBatchArgs = new ArrayList<>();

		for (Demand demand : demands) {

			AuditDetail auditDetail = demand.getAuditDetail();
			Object[] demandRecord = { demand.getId(), demand.getConsumerCode(), demand.getConsumerType(),
					demand.getBusinessService(), demand.getOwner().getId(), demand.getTaxPeriodFrom(),
					demand.getTaxPeriodTo(), demand.getMinimumAmountPayable(), auditDetail.getCreatedBy(),
					auditDetail.getLastModifiedBy(), auditDetail.getCreatedTime(), auditDetail.getLastModifiedTime(),
					demand.getTenantId() };
			demandBatchArgs.add(demandRecord);
		}
		jdbcTemplate.batchUpdate(demandInsertQuery, demandBatchArgs);

		String demandDetailInsertQuery = DemandQueryBuilder.DEMAND_DETAIL_INSERT_QUERY;
		List<Object[]> demandDetailBatchArgs = new ArrayList<>();

		for (DemandDetail demandDetail : demandDetails) {

			AuditDetail auditDetail = demandDetail.getAuditDetail();
			Object[] demandDetailRecord = { demandDetail.getId(), demandDetail.getDemandId(),
					demandDetail.getTaxHeadMasterCode(), demandDetail.getTaxAmount(),
					demandDetail.getCollectionAmount(), auditDetail.getCreatedBy(), auditDetail.getLastModifiedBy(),
					auditDetail.getCreatedTime(), auditDetail.getLastModifiedTime(), demandDetail.getTenantId() };
			demandDetailBatchArgs.add(demandDetailRecord);
		}
		jdbcTemplate.batchUpdate(demandDetailInsertQuery, demandDetailBatchArgs);
	}

	public void update(DemandRequest demandRequest) {

		List<Demand> demands = demandRequest.getDemands();
		List<DemandDetail> demandDetails = new ArrayList<>();
		for (Demand demand : demands) {
			demandDetails.addAll(demand.getDemandDetails());
		}

		String demandInsertQuery = DemandQueryBuilder.DEMAND_UPDATE_QUERY;
		List<Object[]> demandBatchArgs = new ArrayList<>();

		for (Demand demand : demands) {

			AuditDetail auditDetail = demand.getAuditDetail();
			String demandId = demand.getId();
			String demandtenantId = demand.getTenantId();

			Object[] demandRecord = { demandId, demand.getConsumerCode(), demand.getConsumerType(),
					demand.getBusinessService(), demand.getOwner().getId(), demand.getTaxPeriodFrom(),
					demand.getTaxPeriodTo(), demand.getMinimumAmountPayable(), auditDetail.getLastModifiedBy(),
					auditDetail.getLastModifiedTime(), demandtenantId, demandId, demandtenantId };
			demandBatchArgs.add(demandRecord);
		}
		jdbcTemplate.batchUpdate(demandInsertQuery, demandBatchArgs);

		String demandDetailInsertQuery = DemandQueryBuilder.DEMAND_DETAIL_UPDATE_QUERY;
		List<Object[]> demandDetailBatchArgs = new ArrayList<>();

		for (DemandDetail demandDetail : demandDetails) {

			AuditDetail auditDetail = demandDetail.getAuditDetail();
			String demandDetailId = demandDetail.getId();
			String demandTenantId = demandDetail.getTenantId();

			Object[] demandDetailRecord = { demandDetailId, demandDetail.getDemandId(), demandDetail.getTaxAmount(),
					demandDetail.getCollectionAmount(), auditDetail.getLastModifiedBy(),
					auditDetail.getLastModifiedTime(), demandTenantId, demandDetailId, demandTenantId };
			demandDetailBatchArgs.add(demandDetailRecord);
		}
		jdbcTemplate.batchUpdate(demandDetailInsertQuery, demandDetailBatchArgs);
	}

	public List<Demand> getDemands(DemandCriteria demandCriteria, Set<String> ownerIds) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String searchDemandQuery = DemandQueryBuilder.getDemandQuery(demandCriteria, ownerIds, preparedStatementValues);
		return jdbcTemplate.query(searchDemandQuery, preparedStatementValues.toArray(), new DemandRowMapper());
	}

	public List<DemandDetail> getDemandDetails(DemandDetailCriteria demandDetailCriteria) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String searchDemandDetailQuery = DemandQueryBuilder.getDemandDetailQuery(demandDetailCriteria,preparedStatementValues);
		return jdbcTemplate.query(searchDemandDetailQuery, preparedStatementValues.toArray(),new DemandDetailRowMapper());
	}
}
