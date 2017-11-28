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
package org.egov.pa.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.pa.model.AuditDetails;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiTarget;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.KpiValueDetail;
import org.egov.pa.model.KpiValueList;
import org.egov.pa.utils.PerformanceAssessmentConstants;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PerformanceAssessmentRowMapper {

	public class KPIMasterRowMapper implements RowMapper<KPI> {

		@Override
		public KPI mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			KPI kpi = new KPI(); 
			kpi.setId(rs.getString("id"));
			kpi.setName(rs.getString("name"));
			kpi.setCode(rs.getString("code"));
			kpi.setDepartmentId(rs.getLong("departmentId"));
			kpi.setFinancialYear(rs.getString("financialYear"));
			kpi.setInstructions(rs.getString("instructions"));
			kpi.setPeriodicity(rs.getString("periodicity"));
			kpi.setTargetType(rs.getString("targetType"));
			kpi.setAuditDetails(addAuditDetails(rs));
			if(StringUtils.isNotBlank(rs.getString("targetId"))) { 
				KpiTarget target = new KpiTarget();
				target.setId(rs.getString("targetId"));
				target.setKpiCode(rs.getString("kpiCode"));
				target.setTargetValue(rs.getString("targetValue"));
				target.setTenantId(rs.getString("tenantId"));
				kpi.setKpiTarget(target);
			}
			return kpi;
		}
	}
	
	public class KPIValueListRowMapper implements RowMapper<KpiValueList> {
		@Override
		public KpiValueList mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			KpiValueList valueList = new KpiValueList();
			valueList.setTenantId(rs.getString("tenantId"));
			KPI kpi = new KPI();
			kpi.setCode(rs.getString("code"));
			kpi.setInstructions(rs.getString("instructions"));
			valueList.setKpi(kpi);
			valueList.setFinYear(rs.getString("financialYear"));
			KpiValue value = new KpiValue();
			value.setId(String.valueOf(rs.getLong("valueId")));
			value.setTenantId(rs.getString("tenantId"));
			List<KpiValue> kpiValueList = new ArrayList<>();
			kpiValueList.add(value);
			// valueList.setKpiValue(value);
			return valueList;
		}
	}

	public class KPIValueRowMapper implements RowMapper<KpiValue> {
		public Map<String, KpiValue> valueMap = new HashMap<>();
		public Map<String, List<KpiValueDetail>> valueDetailMap = new HashMap<>();
		@Override
		public KpiValue mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			if(!valueMap.containsKey(rs.getString("id"))) { 
				KpiValue value = new KpiValue();
				value.setId(rs.getString("id"));
				value.setKpiCode(rs.getString("kpiCode"));
				value.setTenantId(rs.getString("tenantId"));
				value.setAuditDetails(addAuditDetails(rs));
				valueMap.put(rs.getString("id"), value); 
			}
			
			if(StringUtils.isNotBlank(rs.getString("valueId")) && !valueDetailMap.containsKey(rs.getString("valueId"))) { 
				List<KpiValueDetail> valueDetailList = new ArrayList<>();
				valueDetailList.add(addValueDetails(rs)); 
				valueDetailMap.put(rs.getString("valueId"), valueDetailList); 
			} else if(StringUtils.isNotBlank(rs.getString("valueId")) && valueDetailMap.containsKey(rs.getString("valueId"))) {
				List<KpiValueDetail> valueDetailList = valueDetailMap.get(rs.getString("valueId"));
				valueDetailList.add(addValueDetails(rs));
			} else if(StringUtils.isBlank(rs.getString("valueId"))) { 
				List<KpiValueDetail> valueDetailList = new ArrayList<>();
				KpiValueDetail detail =null ; 
				for(int i=0 ; i<PerformanceAssessmentConstants.MONTHLIST.size();i++) {
					detail = new KpiValueDetail();
					detail.setPeriod(PerformanceAssessmentConstants.MONTHLIST.get(i));
					detail.setValue("");
					valueDetailList.add(detail);
				}
				valueDetailMap.put(rs.getString("kpiCode").concat("_" + rs.getString("tenantId")), valueDetailList); 
			}
			return null; 
		}
	}
	
	private AuditDetails addAuditDetails(ResultSet rs) {
		AuditDetails audit = new AuditDetails();
		try { 
			audit.setCreatedBy(rs.getLong("createdBy"));
			audit.setCreatedTime(rs.getLong("createdDate"));
			audit.setLastModifiedBy(rs.getLong("lastModifiedBy"));
			audit.setLastModifiedTime(rs.getLong("lastModifiedDate"));
		} catch (Exception e) {
			log.error("Encountered an exception while adding Audit Details" + e);
		}
		return audit;
	}
	
	private KpiValueDetail addValueDetails(ResultSet rs) { 
		KpiValueDetail detail = new KpiValueDetail(); 
		try { 
			detail.setPeriod(rs.getString("period"));
			detail.setValue(rs.getString("value"));
			detail.setValueid(rs.getString("valueId"));
		}  catch (Exception e) {
			log.error("Encountered an exception while adding Value Details" + e);
		}
		return detail;
	}

}
	
