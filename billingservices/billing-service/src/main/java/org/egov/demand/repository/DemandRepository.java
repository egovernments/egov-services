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
import org.springframework.dao.DataAccessException;
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

		log.debug("the request object : "+demandRequest);
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
					demand.getTaxPeriodTo(), demand.getMinimumAmountPayable(),
					auditDetail.getCreatedBy(), auditDetail.getLastModifiedBy(), auditDetail.getCreatedTime(),
					auditDetail.getLastModifiedTime(),	demand.getTenantId() };
			demandBatchArgs.add(demandRecord);
		}
		try {
			jdbcTemplate.batchUpdate(demandInsertQuery, demandBatchArgs);
		} catch (DataAccessException ex) {
			log.error("the exception from demand insert operation : " + ex);
			throw new RuntimeException(ex.getMessage());
		}

		String demandDetailInsertQuery = DemandQueryBuilder.DEMAND_DETAIL_INSERT_QUERY;
		List<Object[]> demandDetailBatchArgs = new ArrayList<>();

		for (DemandDetail demandDetail : demandDetails) {

			AuditDetail auditDetail = demandDetail.getAuditDetail();
			Object[] demandDetailRecord = { demandDetail.getId(), demandDetail.getDemandId(),
					demandDetail.getTaxHeadMasterCode(), demandDetail.getTaxAmount(), demandDetail.getCollectionAmount(),
					auditDetail.getCreatedBy(), auditDetail.getLastModifiedBy(), auditDetail.getCreatedTime(),
					auditDetail.getLastModifiedTime(), demandDetail.getTenantId() };
			demandDetailBatchArgs.add(demandDetailRecord);
		}
		try {
			jdbcTemplate.batchUpdate(demandDetailInsertQuery, demandDetailBatchArgs);
		} catch (DataAccessException ex) {
			log.error("the exception from demandDetail insert operation : " + ex);
			throw new RuntimeException(ex.getMessage());
		}
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
					demand.getTaxPeriodTo(), demand.getMinimumAmountPayable(), 
					auditDetail.getLastModifiedBy(), auditDetail.getLastModifiedTime(), demandtenantId, demandId,
					demandtenantId };
			demandBatchArgs.add(demandRecord);
		}
		try {
			jdbcTemplate.batchUpdate(demandInsertQuery, demandBatchArgs);
		} catch (DataAccessException ex) {
			log.error("the exception from demand insert operation : " + ex);
			throw new RuntimeException(ex.getMessage());
		}

		String demandDetailInsertQuery = DemandQueryBuilder.DEMAND_DETAIL_UPDATE_QUERY;
		List<Object[]> demandDetailBatchArgs = new ArrayList<>();

		for (DemandDetail demandDetail : demandDetails) {

			AuditDetail auditDetail = demandDetail.getAuditDetail();
			String demandDetailId = demandDetail.getId();
			String demandTenantId = demandDetail.getTenantId();

			Object[] demandDetailRecord = { demandDetailId, demandDetail.getDemandId(),
					demandDetail.getTaxAmount(),demandDetail.getCollectionAmount(), auditDetail.getLastModifiedBy(),
					auditDetail.getLastModifiedTime(), demandTenantId, demandDetailId, demandTenantId };
			demandDetailBatchArgs.add(demandDetailRecord);
		}
		try {
			jdbcTemplate.batchUpdate(demandDetailInsertQuery, demandDetailBatchArgs);
		} catch (DataAccessException ex) {
			log.error("the exception from demandDetail insert operation : " + ex);
			throw new RuntimeException(ex.getMessage());
		}

	}

	public List<Demand> getDemands(DemandCriteria demandCriteria,Set<String> ownerIds) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String searchDemandQuery = DemandQueryBuilder.getDemandQuery(demandCriteria,ownerIds, preparedStatementValues);
		List<Demand> demands = new ArrayList<>();
		try {
			demands.addAll(
					jdbcTemplate.query(searchDemandQuery, preparedStatementValues.toArray(), new DemandRowMapper()));
		} catch (DataAccessException e) {
			log.error("the error from demand search jdbc : " + e);
			throw new RuntimeException(e);
		}
		return demands;
	}

	public List<DemandDetail> getDemandDetails(DemandDetailCriteria demandDetailCriteria) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String searchDemandDetailQuery = DemandQueryBuilder.getDemandDetailQuery(demandDetailCriteria,
				preparedStatementValues);
		List<DemandDetail> demandDetails = new ArrayList<>();
		try {
			demandDetails.addAll(jdbcTemplate.query(searchDemandDetailQuery, preparedStatementValues.toArray(),
					new DemandDetailRowMapper()));
		} catch (DataAccessException e) {
			log.error("the error from demand search jdbc : " + e);
			throw new RuntimeException(e);
		}
		return demandDetails;
	}
}
