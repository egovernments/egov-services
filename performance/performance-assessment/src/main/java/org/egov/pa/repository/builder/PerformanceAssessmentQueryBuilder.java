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
import org.egov.pa.web.contract.KPITargetGetRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PerformanceAssessmentQueryBuilder {
	public static final Logger LOGGER = LoggerFactory.getLogger(PerformanceAssessmentQueryBuilder.class);

    public static final String SEARCH_KPI_BASE_QUERY = "SELECT master.id, master.name, master.code, master.department as departmentId, master.finyear as financialYear, master.instructions, "
    		+ " master.periodicity, master.targettype as targetType, master.active, master.createdby as createdBy, master.createddate as createdDate, " 
    		+ " master.lastmodifiedby as lastModifiedBy, master.lastmodifieddate as lastModifiedDate, master.category as categoryId,  "
    		+ " target.id as targetId, target.kpicode as kpiCode, target.targetvalue as targetValue, target.tenantid as tenantId, target.finyear as targetFinYear "
    		+ " FROM egpa_kpi_master master LEFT JOIN egpa_kpi_master_target target ON master.code = target.kpicode WHERE master.id IS NOT NULL AND master.active IS TRUE" ; 
    
    public static final String SEARCH_VALUE_BASE_QUERY = "SELECT value.id, value.finyear as valueFinYear, value.kpicode as kpiCode, value.tenantid as tenantId, value.createdby as createdBy, value.createddate as createdDate, value.lastmodifiedby as lastModifiedBy, value.lastmodifieddate as lastModifiedDate, "  
    		+ " detail.id as valueDetailId, detail.id as valueDetailId, detail.valueid as valueId, detail.period, detail.value , detail.remarks as valueRemarks, docs.filestoreid as fileStoreId, docs.kpicode as docKpiCode, docs.documentcode as docDocumentCode , docs.documentname as docDocumentName , "  
    		+ " (select documentname from egpa_kpi_master_document where documentcode = docs.documentcode) as docMasterDocName FROM  "  
    		+ " egpa_kpi_value value LEFT JOIN egpa_kpi_value_detail detail ON value.id = detail.valueid " 
    		+ " LEFT JOIN egpa_kpi_value_documents docs ON detail.id = docs.valueid WHERE value.id IS NOT NULL  "; 
    
    /*public static final String COMPARE_SEARCH_BASE_QUERY = "SELECT master.id as id, master.name as name, master.code as code, master.department as  departmentId, master.finyear as financialYear, "  
    		+ " master.instructions as instructions, master.periodicity as periodicity, master.targettype as targetType, master.active as active,  " 
    		+ " target.id as targetId, target.kpicode as targetKpiCode, target.targetvalue as targetValue, target.tenantid as tenantId, "  
    		+ " value.id as valueId, value.kpicode as valieKpiCode, value.tenantid as valueTenantId, " 
    		+ " detail.id as detailId, detail.valueid as detailValueId, detail.period as period, detail.value as value  FROM egpa_kpi_master master LEFT JOIN egpa_kpi_master_target target ON master.code = target.kpicode "  
    		+ " LEFT JOIN egpa_kpi_value value ON master.code = value.kpicode " 
    		+ " LEFT JOIN egpa_kpi_value_detail detail ON value.id = detail.valueid WHERE master.id IS NOT NULL " ;*/
    
	public static final String COMPARE_SEARCH_BASE_QUERY = "SELECT master.id, master.name, master.code, master.department, master.finyear, master.instructions, master.periodicity, master.targettype, master.active, master.category as categoryId,  "
    		+ " target.id as targetId, target.kpicode as targetKpiCode, target.targetvalue, target.tenantid as targetTenantId, target.finyear as targetFinYear, " 
    		+ " value.id as valueId, value.kpicode as valueKpiCode, value.tenantid as valueTenantId,  "
    		+ " detail.value as detailValue, detail.period as detailPeriod, detail.id as valueDetailId FROM egpa_kpi_master master LEFT JOIN egpa_kpi_master_target target ON master.code = target.kpicode " 
    		+ " LEFT JOIN egpa_kpi_value value ON master.code = value.kpicode  AND target.finyear = value.finyear "
    		+ " LEFT JOIN egpa_kpi_value_detail detail ON value.id = detail.valueid " 
    		+ " WHERE master.targettype = 'VALUE' " ; 
    
    public static final String COMPARE_SEARCH_OBJECTIVE_BASE_QUERY = "SELECT master.id, master.name, master.code, master.targettype as targetType, master.instructions, master.finyear as finYear, master.department as departmentId, master.category as categoryId, master.periodicity,  "  
    		+ " target.id as targetId, target.kpicode as targetKpiCode, target.finyear as targetFinYear, target.targetvalue as targetValue, target.tenantid as targetTenantId, " 
    		+ " value.id as valueId, value.kpicode as valueKpiCode, value.tenantid as valueTenantId, value.finyear as valueFinYear, detail.id as detailId, detail.valueid as detailValueId, detail.value as value, detail.period as period, detail.id as valueDetailId, detail.remarks as valueDetailRemarks " 
    		+ " FROM egpa_kpi_master master LEFT JOIN egpa_kpi_master_target target ON master.code = target.kpicode LEFT JOIN egpa_kpi_value value ON master.code = value.kpicode AND value.finyear = target.finyear "  
    		+ " LEFT JOIN egpa_kpi_value_detail detail ON value.id = detail.valueid WHERE (master.targettype = 'OBJECTIVE' OR master.targettype = 'TEXT') " ;   
    		
    
    public static final String COMPARE_GROUP_BY = " GROUP BY master.id, master.name, master.code, master.department, master.finyear, master.instructions, master.periodicity, master.targettype, master.active, "
    		+ " target.id, target.kpicode, target.targetvalue, target.tenantid, "  
    		+ " value.id, value.kpicode, value.tenantid, detail.valueid , detail.value, detail.period, detail.id" ;
    
    public static String persistKpiQuery() { 
    	return "INSERT INTO egpa_kpi_master (id, name, code, finyear, createdby, createddate) " 
    			+ " values (nextval('seq_egpa_kpi_master'), ?, ?, ?, ?, ?)";
    }
    
    public static String persistKpiTargetQuery() { 
    	return "INSERT INTO egpa_kpi_master_targets (id, kpiid, targetvalue, targetdescription, instructions, createdby, createddate) " 
    			+ " values (nextval('seq_egpa_kpi_master_targets'), ?, ?, ?, ?, ?, ?)";
    }
    
    public static String getNextKpiMasterId() {
    	return "select nextval('seq_egpa_kpi_master') FROM GENERATE_SERIES(1,:size)" ; 
    }
    
    public static String getNextKpiValueId() {
    	return "select nextval('seq_egpa_kpi_value') FROM GENERATE_SERIES(1,:size)" ; 
    }
    
    public static String getNextKpiValueDetailId() {
    	return "select nextval('seq_egpa_kpi_value_detail') FROM GENERATE_SERIES(1,:size)" ; 
    }
    
    public static String numberOfDocsReqQuery() { 
    	return "SELECT count(*) FROM egpa_kpi_master_document WHERE kpicode = :kpiCode AND mandatoryflag IS TRUE " ;
    }
    
    public static String fetchKpiByNameOrCode() { 
    	return "SELECT id, name, code, finyear as financialYear, targettype as targetType, " 
    			+ " department as departmentId FROM egpa_kpi_master WHERE (name = :name and finyear = :finyear) OR (code = :code and finyear = :finyear) "; 
    }
    
    public static String fetchTargetForKpi() { 
    	return "SELECT id, name, code, finyear as financialYear, targetvalue as targetValue, targetdescription as targetDescription FROM egpa_kpi_master master where master.code = :kpiCode and master.finyear = :finYear and (targetvalue IS NOT NULL OR targetdescription IS NOT NULL) " ;
    }
    
    public static String fetchKpiValueForCodeAndTenant() { 
    	return "SELECT value.actualvalue as resultValue FROM egpa_kpi_value value " 
    			+ " LEFT JOIN egpa_kpi_master master on value.kpicode = master.code WHERE value.kpicode = :kpiCode and value.tenantid = :tenantId " ; 
    }
    
    public static String queryForSearchConfig() { 
    	return "SELECT possibility, graphtype FROM egpa_search_config WHERE tenant = :tenant AND kpi = :kpi AND finyear = :finYear " ; 
    }
    
    public static String fetchKpiByCode() { 
    	return "SELECT id, name, code, finyear as financialYear, targetvalue as targetValue FROM egpa_kpi_master where code = :code ";  
    }
    
    public static String getDocsForValueRecordQuery() { 
    	return " SELECT id as valueDocumentId, documentcode as documentCode, documentname as documentName, kpicode as kpiCode, valueid as valueDetailId, filestoreid as fileStoreId "  
    	 + " from egpa_kpi_value_documents where valueid IN (select id from egpa_kpi_value_detail where valueid IN (:valueIdList)) " ; 
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
    
    public String getValueSearchQuery(KPIValueSearchRequest kpiValueSearchReq, final List preparedStatementValues) { 
    	final StringBuilder selectQuery = new StringBuilder(SEARCH_VALUE_BASE_QUERY); 
		addKpiValueWhereClause(selectQuery, preparedStatementValues, kpiValueSearchReq);
		LOGGER.info("Query : " + selectQuery);
		return selectQuery.toString();
    }
    
    public String getValueCompareSearchQuery(KPIValueSearchRequest kpiValueSearchReq, final List preparedStatementValues) { 
    	final StringBuilder selectQuery = new StringBuilder(COMPARE_SEARCH_BASE_QUERY); 
    	addCompareWhereClause(selectQuery, preparedStatementValues, kpiValueSearchReq);
    	selectQuery.append(COMPARE_GROUP_BY);
		LOGGER.info("Query : " + selectQuery);
		return selectQuery.toString();
    }
    
    public String getValueCompareObjectiveSearchQuery(KPIValueSearchRequest kpiValueSearchReq, final List preparedStatementValues) { 
    	final StringBuilder selectQuery = new StringBuilder(COMPARE_SEARCH_OBJECTIVE_BASE_QUERY); 
    	addCompareWhereClause(selectQuery, preparedStatementValues, kpiValueSearchReq);
    	selectQuery.append(" ORDER BY detail.id "); 
		LOGGER.info("Query : " + selectQuery);
		return selectQuery.toString();
    }
    
    public String getTargetTypeForKpi() { 
    	return "SELECT distinct targettype FROM egpa_kpi_master WHERE code IN (:kpiCodeList) " ; 
    }
    
    
    public String getDocumentForKpiValue() { 
    	return " SELECT documentcode as code, kpicode as kpiCode, filestoreid as fileStoreId, id, valueid as valueId " 
    			+ " FROM egpa_kpi_value_documents WHERE valueid = :vid " ; 
    }
    
    public String getKpiTypeQuery() { 
    	return "SELECT targettype FROM egpa_kpi_master WHERE code = ? " ; 
    }
    
    public String getDocumentForKpi() { 
    	return "SELECT id, kpicode as kpiCode, documentcode as code, documentname as name, mandatoryflag as active FROM egpa_kpi_master_document WHERE kpicode IN (:kpiCode) " ; 
    }
    
    public String targetAvailableForKpi() { 
    	return "select targettype as targetType from egpa_kpi_master master where master.code = :kpiCode AND exists (select 1 from egpa_kpi_master_target where kpicode = master.code) " ;
    }
    
    public String checkActualValueForKpi() { 
    	return "SELECT COUNT(*) FROM egpa_kpi_value_detail WHERE valueid IN (select id from egpa_kpi_value where kpicode IN (:kpiCodeList)) AND value IS NOT NULL AND value <> ''  " ; 
    }
    
    private static final String GETKPIBYCODEFINYEAR = "SELECT master.id, master.name, master.code, master.department as departmentId, master.finyear as financialYear, master.instructions, master.category as categoryId, "  
			+ " master.periodicity, master.targettype as targetType, master.active, master.createdby as createdBy, master.createddate as createdDate, " 
			+ " master.lastmodifiedby as lastModifiedBy, master.lastmodifieddate as lastModifiedDate, target.id as targetId, target.targetvalue as targetValue, target.kpicode as kpiCode, target.tenantid as tenantId, target.finyear as targetFinYear "  
			+ " from egpa_kpi_master master LEFT JOIN egpa_kpi_master_target target ON master.code = target.kpicode WHERE master.id IS NOT NULL " ;
    
    public String getKpiByCode(List<String> kpiCodeList, List<String> finYearList, Long departmentId, Long categoryId,
    		List<Object> preparedStatementValues) { 
    	StringBuilder baseQuery = new StringBuilder(GETKPIBYCODEFINYEAR);
    	 getKpiByCodeWhereClause(baseQuery, kpiCodeList, finYearList, departmentId, categoryId, preparedStatementValues);
    	 return baseQuery.toString();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void getKpiByCodeWhereClause(final StringBuilder selectQuery, final List<String> kpiCodeList, 
            final List<String> finYearList, Long departmentId, Long categoryId, List<Object> preparedStatementValues) {
        if (!(null == kpiCodeList && null == finYearList && null == departmentId && null == categoryId))
        	selectQuery.append(" AND ");
        
        boolean isAppendAndClause = false;

        if (null != departmentId && departmentId > 0) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.department = ? ");
            preparedStatementValues.add(departmentId);
        }
        
        if (null != categoryId && categoryId > 0) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.category = ? ");
            preparedStatementValues.add(categoryId);
        }
        
        if (null != kpiCodeList && kpiCodeList.size() > 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.code IN " + getStringQuery(kpiCodeList));
            
        }

        if (null != finYearList && finYearList.size() > 0) { 
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" target.finyear IN " + getStringQuery(finYearList));
        }
    }
    
    public String getTargetSearchQuery(KPITargetGetRequest getReq, final List preparedStatementValues) { 
    	
    	final StringBuilder selectQuery = new StringBuilder("SELECT master.id, master.name, master.code, master.department as departmentId, master.instructions, master.periodicity, master.targettype as targetType, master.finyear as financialYear, master.category as categoryId,  " 
    			+ " target.id as targetId, target.kpicode as kpiCode, target.targetvalue as targetValue, target.tenantid as tenantId, target.finyear as targetFinYear " 
    			+ " FROM egpa_kpi_master master LEFT JOIN egpa_kpi_master_target target ON master.code = target.kpicode " 
    			+ " WHERE master.id IS NOT NULL AND master.active IS TRUE "); 
    	getTargetSearchWhereClause(selectQuery, preparedStatementValues, getReq);
		LOGGER.info("Query : " + selectQuery);
		return selectQuery.toString();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void getTargetSearchWhereClause(final StringBuilder selectQuery, List<Object> preparedStatementValues, KPITargetGetRequest getReq) {
        if (!(null == getReq.getKpiCode() && null == getReq.getFinYear() && null == getReq.getDepartmentId()))
        	selectQuery.append(" AND ");
        
        boolean isAppendAndClause = false;

        if (null != getReq.getKpiCode() && getReq.getKpiCode().size() > 0) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.code IN " + getStringQuery(getReq.getKpiCode()));
        }
        
        if (null != getReq.getFinYear() && getReq.getFinYear().size() > 0) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" target.finyear IN " + getStringQuery(getReq.getFinYear()));
        }
        
        if (null != getReq.getDepartmentId() && getReq.getDepartmentId().size() > 0) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.department IN " + getIdQuery(getReq.getDepartmentId()));
        }
        
        if (null != getReq.getCategoryId() && getReq.getCategoryId().size() > 0) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.category IN " + getStringQuery(getReq.getCategoryId()));
        }
        
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addKpiValueWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final KPIValueSearchRequest kpiValueSearchReq) {
		List<String> finYearList = kpiValueSearchReq.getFinYear();
		List<String> tenantIdList = kpiValueSearchReq.getTenantId();
		if (!(null == finYearList && null == tenantIdList))
			selectQuery.append(" AND ");
		
		boolean isAppendAndClause = false;
		
		if (null != tenantIdList && tenantIdList.size() > 0 && 
				!tenantIdList.get(0).equals("ALL")) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" value.tenantid IN " + getStringQuery(tenantIdList));
		}

		if (null != finYearList && finYearList.size() > 0 && 
				finYearList.size() == 1 && !finYearList.get(0).equals("ALL")) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" value.finyear IN " + getStringQuery(finYearList));
		}

	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addCompareWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final KPIValueSearchRequest kpiValueSearchReq) {
		if(null == kpiValueSearchReq.getFinYear() && null == kpiValueSearchReq.getKpiCodes() 
				&& null == kpiValueSearchReq.getUlbList() && null == kpiValueSearchReq.getDepartmentId()) { 
			selectQuery.append(COMPARE_GROUP_BY); 
			return;
		}
		
		selectQuery.append(" AND ");
		boolean isAppendAndClause = false;
		List<String> parameterList = kpiValueSearchReq.getUlbList(); 
		
		if (null != parameterList && parameterList.size() > 0 ) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" value.tenantid IN " + getStringQuery(parameterList));
		}
		
		parameterList = kpiValueSearchReq.getKpiCodes(); 
		
		if (null != parameterList && parameterList.size() > 0 ) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" master.code IN " + getStringQuery(parameterList));
		}
		
		parameterList = kpiValueSearchReq.getFinYear(); 
		
		if (null != parameterList && parameterList.size() > 0 ) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" target.finyear IN " + getStringQuery(parameterList));
		}
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addKpiMasterWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final KPIGetRequest kpiGetRequest) {
        if (null == kpiGetRequest.getKpiCode() && null == kpiGetRequest.getKpiName() 
        		&& null == kpiGetRequest.getDepartmentId() && null == kpiGetRequest.getFinYear()
        		&& null == kpiGetRequest.getCategoryId())
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
        
        /*if (StringUtils.isNotBlank(kpiGetRequest.getTenantId())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.tenantid = ? ");
            preparedStatementValues.add(kpiGetRequest.getTenantId());
        }*/
        
        if (null != kpiGetRequest.getDepartmentId() && kpiGetRequest.getDepartmentId() > 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.department = ? ");
            preparedStatementValues.add(kpiGetRequest.getDepartmentId());
        }
        
        if (StringUtils.isNotBlank(kpiGetRequest.getCategoryId())) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.category = ? ");
            preparedStatementValues.add(kpiGetRequest.getCategoryId());
        }
        
        if (StringUtils.isNotBlank(kpiGetRequest.getFinYear())) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" master.finyear = ? ");
            preparedStatementValues.add(kpiGetRequest.getFinYear());
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
    
    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
    }

}
