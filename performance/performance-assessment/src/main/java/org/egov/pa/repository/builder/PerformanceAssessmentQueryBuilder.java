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

package org.egov.pa.repository.builder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PerformanceAssessmentQueryBuilder {
	public static final Logger LOGGER = LoggerFactory.getLogger(PerformanceAssessmentQueryBuilder.class);

    public static final String SEARCH_KPI_BASE_QUERY = "SELECT docs.id as docid, docs.kpicode as dockpicode, docs.documentcode, docs.documentname, docs.mandatoryflag, master.id as id, master.name as name, master.code as code, master.finyear as finYear, master.createdby as createdBy, master.createddate as createdDate, "  
    		+ " master.tenantid as tenantId, master.lastmodifiedby as lastModifiedBy, master.lastmodifieddate as lastModifiedDate, master.targettype as targetType, master.targetvalue as targetValue, master.instructions as instructions, master.department as deptCode, " 
    		+ " dept.name as deptName, dept.id as departmentId, dept.active as deptActive "
    		+ " FROM egpa_kpi_master master LEFT JOIN eg_department dept ON dept.code = master.department " 
    		+ " LEFT JOIN egpa_kpi_master_document docs ON master.code = docs.kpicode WHERE master.active IS TRUE " ; 
    
    public static final String SEARCH_VALUE_BASE_QUERY = "SELECT value.tenantid as tenantId, master.code as kpiCode, master.finyear as finYear, "  
    		+  " master.targetvalue as targetValue, master.instructions, "
    		+  " value.id as valueId, value.kpicode as valueKpiCode, value.actualvalue as actualValue "  
    		+  " FROM egpa_kpi_value value LEFT JOIN egpa_kpi_master master ON value.kpicode = master.code " 
    		+  " WHERE value.tenantid IS NOT NULL "; 
    
    public static String persistKpiQuery() { 
    	return "INSERT INTO egpa_kpi_master (id, name, code, finyear, createdby, createddate) " 
    			+ " values (nextval('seq_egpa_kpi_master'), ?, ?, ?, ?, ?)";
    }
    
    public static String persistKpiTargetQuery() { 
    	return "INSERT INTO egpa_kpi_master_targets (id, kpiid, targetvalue, targetdescription, instructions, createdby, createddate) " 
    			+ " values (nextval('seq_egpa_kpi_master_targets'), ?, ?, ?, ?, ?, ?)";
    }
    
    public static String getNextKpiMasterId(int numberOfIds) {
    	return "select nextval('seq_egpa_kpi_master') FROM GENERATE_SERIES(1,:size)" ; 
    }
    
    public static String fetchKpiByNameOrCode() { 
    	return "SELECT * FROM egpa_kpi_master WHERE (name = :name and finyear = :finyear) OR (code = :code and finyear = :finyear) "; 
    }
    
    public static String fetchTargetForKpi() { 
    	return "SELECT id, name, code, finyear as financialYear, targetvalue as targetValue FROM egpa_kpi_master master where master.code = :kpiCode and master.finyear = :finYear and targetvalue IS NOT NULL " ;
    }
    
    public static String fetchKpiValueForCodeAndTenant() { 
    	return "SELECT value.id, value.kpicode as kpiCode, value.actualvalue as actualValue, value.tenantid as tenantId FROM egpa_kpi_value value " 
    			+ " LEFT JOIN egpa_kpi_master master on value.kpicode = master.code WHERE value.kpicode = :kpiCode and value.tenantid = :tenantId " ; 
    }
    
    public static String queryForSearchConfig() { 
    	return "SELECT possibility FROM egpa_search_config WHERE tenant = :tenant AND kpi = :kpi AND finyear = :finYear " ; 
    }
    
    public static String fetchKpiByCode() { 
    	return "SELECT id, name, code, finyear as financialYear, targetvalue as targetValue FROM egpa_kpi_master where code = :code ";  
    }
    
    
    
    public static String getQuery(KPIGetRequest kpiGetRequest) {
    	return SEARCH_KPI_BASE_QUERY; 
    }
    
    public String getKpiSearchQuery(KPIGetRequest kpiGetRequest, final List preparedStatementValues) {
    	final StringBuilder selectQuery = new StringBuilder(SEARCH_KPI_BASE_QUERY); 
		addKpiMasterWhereClause(selectQuery, preparedStatementValues, kpiGetRequest);
		LOGGER.info("Query : " + selectQuery);
		return selectQuery.toString();
    }
    
    public String getValueCompareSearchQuery(KPIValueSearchRequest kpiValueSearchReq, final List preparedStatementValues) { 
    	final StringBuilder selectQuery = new StringBuilder(SEARCH_VALUE_BASE_QUERY); 
		addKpiValueWhereClause(selectQuery, preparedStatementValues, kpiValueSearchReq);
		LOGGER.info("Query : " + selectQuery);
		return selectQuery.toString();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addKpiValueWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final KPIValueSearchRequest kpiValueSearchReq) {
    	List<String> finYearList = kpiValueSearchReq.getFinYear();
    	List<String> tenantIdList = kpiValueSearchReq.getTenantId(); 
    	List<String> kpiCodeList = kpiValueSearchReq.getKpiCodes(); 
        if (null == kpiValueSearchReq.getTenantId() || null == kpiValueSearchReq.getFinYear() 
        		|| null == kpiValueSearchReq.getKpiCodes())
            return;

        selectQuery.append(" AND ");
        boolean isAppendAndClause = false;

        if (null != finYearList && finYearList.size() > 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.finyear IN " + getStringQuery(finYearList));
        }
        
        if (null != tenantIdList && tenantIdList.size() > 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" value.tenantid IN " + getStringQuery(tenantIdList));
        }
        
        if (null != kpiCodeList && kpiCodeList.size() > 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.code IN " + getStringQuery(kpiCodeList));
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addKpiMasterWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final KPIGetRequest kpiGetRequest) {
        if (null == kpiGetRequest.getKpiCode() && null == kpiGetRequest.getKpiName())
            return;

        selectQuery.append(" AND ");
        boolean isAppendAndClause = false;

        if (StringUtils.isNotBlank(kpiGetRequest.getKpiCode())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.code = ? ");
            preparedStatementValues.add(kpiGetRequest.getKpiCode());
        }

        if (StringUtils.isNotBlank(kpiGetRequest.getKpiName())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.name = ? ");
            preparedStatementValues.add(kpiGetRequest.getKpiName());
        }
    }
    
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }

    private static String getStringQuery(final List<String> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append("'" + idList.get(0).toString() + "'");
            for (int i = 1; i < idList.size(); i++)
                query.append(",'" + idList.get(i) + "'");
        }
        return query.append(")").toString();
    }

}
