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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxPeriod;
import org.egov.demand.repository.querybuilder.TaxPeriodQueryBuilder;
import org.egov.demand.repository.rowmapper.TaxPeriodRowMapper;
import org.egov.demand.web.contract.TaxPeriodCriteria;
import org.egov.demand.web.contract.TaxPeriodRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TaxPeriodRepository {

    private static final Logger logger = LoggerFactory.getLogger(TaxPeriodRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TaxPeriodRowMapper taxPeriodRowMapper;

    @Autowired
    private TaxPeriodQueryBuilder taxPeriodQueryBuilder;

    public List<TaxPeriod> searchTaxPeriods(final TaxPeriodCriteria taxPeriodCriteria) {

        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = taxPeriodQueryBuilder.prepareSearchQuery(taxPeriodCriteria, preparedStatementValues);
        List<TaxPeriod> taxPeriods = new ArrayList<>();
        try {
            logger.info("queryStr -> " + queryStr + "preparedStatementValues -> " + preparedStatementValues.toString());
            taxPeriods = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), taxPeriodRowMapper);
            logger.info("TaxPeriodRepository taxPeriods -> " + taxPeriods);
        } catch (final Exception ex) {
            logger.info("the exception from searchTaxPeriods : " + ex);
        }
        return taxPeriods;
    }
    
    public List<TaxPeriod> create(TaxPeriodRequest taxPeriodRequest){
    	List<TaxPeriod> taxPeriods = taxPeriodRequest.getTaxPeriods();
    	RequestInfo requestInfo = taxPeriodRequest.getRequestInfo();
    	
    	jdbcTemplate.batchUpdate(taxPeriodQueryBuilder.insertQuery, new BatchPreparedStatementSetter() {
    		@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				TaxPeriod taxPeriod = taxPeriods.get(index);

				ps.setString(1, taxPeriod.getId());
				ps.setString(2, taxPeriod.getService());
				ps.setString(3, taxPeriod.getCode());
				ps.setLong(4, taxPeriod.getFromDate());
				ps.setLong(5, taxPeriod.getToDate());
				ps.setString(6, taxPeriod.getFinancialYear());
				ps.setLong(7, new Date().getTime());
				ps.setLong(8, new Date().getTime());
				ps.setString(9, requestInfo.getUserInfo().getId().toString());
				ps.setString(10, requestInfo.getUserInfo().getId().toString());
				ps.setString(11, taxPeriod.getTenantId());
				
				if(null != taxPeriod.getPeriodCycle())
					ps.setString(12, taxPeriod.getPeriodCycle().toString());
			}
			
			@Override
			public int getBatchSize() {
				return taxPeriods.size();
			}
		});	
    	
    	return taxPeriods;
    }
    
    public List<TaxPeriod> update(TaxPeriodRequest taxPeriodRequest) {
    	List<TaxPeriod> taxPeriods = taxPeriodRequest.getTaxPeriods();
    	RequestInfo requestInfo = taxPeriodRequest.getRequestInfo();
    	
    	jdbcTemplate.batchUpdate(taxPeriodQueryBuilder.updateQuery, new BatchPreparedStatementSetter() {
    		@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				TaxPeriod taxPeriod = taxPeriods.get(index);

				ps.setString(1, taxPeriod.getService());
				ps.setString(2, taxPeriod.getCode());
				ps.setLong(3, taxPeriod.getFromDate());
				ps.setLong(4, taxPeriod.getToDate());
				ps.setString(5, taxPeriod.getFinancialYear());
				ps.setLong(6, new Date().getTime());
				ps.setString(7, requestInfo.getUserInfo().getId().toString());
				ps.setString(8, taxPeriod.getTenantId());
				
				if(taxPeriod.getPeriodCycle().toString()!=null)
					ps.setString(9, taxPeriod.getPeriodCycle().toString());
				ps.setString(10, taxPeriod.getTenantId());
				ps.setString(11, taxPeriod.getId());
			}
			
			@Override
			public int getBatchSize() {
				return taxPeriods.size();
			}
		});	

    	
    	return taxPeriods;
    }

   /* public List<TaxPeriod> create(TaxPeriodRequest taxPeriodRequest){
        List<TaxPeriod> taxPeriods = taxPeriodRequest.getTaxPeriods();

        if(!taxPeriods.isEmpty()){
            String query = taxPeriodQueryBuilder.getInsertQuery();
            List<Object[]> argsList = new ArrayList<>();
            RequestInfo requestInfo = taxPeriodRequest.getRequestInfo();
            for(int i = 0; i < taxPeriods.size(); i++){
                Object[] values = { taxPeriods.get(i).getId(), taxPeriods.get(i).getService(), taxPeriods.get(i).getCode(), taxPeriods.get(i).getFromDate(),
                        taxPeriods.get(i).getToDate(), taxPeriods.get(i).getFinancialYear(), new Date().getTime(), new Date().getTime(),
                        requestInfo.getUserInfo().getId(),
                        requestInfo.getUserInfo().getId(), taxPeriods.get(i).getTenantId()};
                argsList.add(values);
            }
            try {
                jdbcTemplate.batchUpdate(query, argsList);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.getMessage());
            }
        }
        return taxPeriods;
    }*/

    /*public List<TaxPeriod> update(TaxPeriodRequest taxPeriodRequest) {
        List<TaxPeriod> taxPeriods = taxPeriodRequest.getTaxPeriods();
        if(!taxPeriods.isEmpty()){
            String query = taxPeriodQueryBuilder.getUpdateQuery();
            List<Object[]> argsList = new ArrayList<>();
            RequestInfo requestInfo = taxPeriodRequest.getRequestInfo();
           
            
            for(int i=0;i<taxPeriods.size();i++){
                Object[] values = {taxPeriods.get(i).getService(), taxPeriods.get(i).getCode(), taxPeriods.get(i).getFromDate(),
                        taxPeriods.get(i).getToDate(), taxPeriods.get(i).getFinancialYear(), new Date().getTime(),
                        requestInfo.getUserInfo().getId(), taxPeriods.get(i).getTenantId(), taxPeriods.get(i).getId()};
                argsList.add(values);
            }
            try {
                jdbcTemplate.batchUpdate(query, argsList);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.getMessage());
            }
        }
        return taxPeriods;
    }*/

    public boolean checkForDuplicates(List<TaxPeriod> taxPeriodList, String mode){
        boolean duplicatesExist = false;
        String query = taxPeriodQueryBuilder.prepareQueryForValidation(taxPeriodList, mode);
        logger.info("the query for taxperiodseacrh : "+query);
        try {
            duplicatesExist = jdbcTemplate.queryForObject(query, Boolean.class);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
        return duplicatesExist;
    }
}
