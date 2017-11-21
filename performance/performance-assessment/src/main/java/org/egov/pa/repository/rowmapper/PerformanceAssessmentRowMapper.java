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
import org.egov.pa.model.Document;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.KpiValueList;
import org.egov.pa.model.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class PerformanceAssessmentRowMapper {

	public static final Logger LOGGER = LoggerFactory.getLogger(PerformanceAssessmentRowMapper.class);

	public class KPIMasterRowMapper implements RowMapper<KPI> {
		public Map<String, KPI> kpiMap = new HashMap<>();
		public Map<String, List<Document>> docMap = new HashMap<>();
		public List<Long> docIdList = new ArrayList<>();
		public static final String TRUE_FLAG = "true";
		public static final String FALSE_FLAG = "false"; 

		@Override
		public KPI mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			if (!kpiMap.containsKey(rs.getLong("id"))) {
				KPI kpi = new KPI();
				kpi.setId(String.valueOf(rs.getLong("id")));
				kpi.setName(rs.getString("name"));
				kpi.setCode(rs.getString("code"));
				kpi.setFinancialYear(rs.getString("finYear"));
				AuditDetails audit = new AuditDetails();
				audit.setCreatedBy(rs.getLong("createdBy"));
				audit.setCreatedTime(rs.getLong("createdDate"));
				audit.setLastModifiedBy(rs.getLong("lastModifiedBy"));
				audit.setLastModifiedTime(rs.getLong("lastModifiedDate"));
				kpi.setTargetValue(rs.getLong("targetValue"));
				kpi.setTargetType(rs.getString("targetType"));
				kpi.setTargetDescription(rs.getString("targetDescription"));
				if(null != rs.getString("targetType") && rs.getString("targetType").equals(TargetType.OBJECTIVE.toString())) { 
					if(rs.getLong("targetValue") == 1) 
						kpi.setTargetDescription("Yes");
					else if(rs.getLong("targetValue") == 2)
						kpi.setTargetDescription("No");
					else if(rs.getLong("targetValue") == 3)
						kpi.setTargetDescription("In Progress");
				}
				if(null != rs.getString("targetType") && rs.getString("targetType").equals(TargetType.VALUE.toString())) { 
					kpi.setTargetDescription(String.valueOf(rs.getLong("targetValue")));
				}
				kpi.setInstructions(rs.getString("instructions"));
				kpi.setDepartmentId(rs.getLong("departmentId"));
				kpiMap.put(String.valueOf(rs.getLong("id")), kpi);
			}

			if (docMap.containsKey(String.valueOf(rs.getLong("id")))) {
				List<Document> docList = docMap.get(String.valueOf(rs.getLong("id")));
				if (StringUtils.isNotBlank(rs.getString("documentname")) && !docIdList.contains(rs.getLong("docid"))) { 
						Document doc = new Document();
						doc.setId(String.valueOf(rs.getLong("docid")));
						doc.setKpiCode(rs.getString("dockpicode"));
						doc.setCode(rs.getString("documentcode"));
						doc.setName(rs.getString("documentname"));
						doc.setActive(rs.getBoolean("mandatoryflag"));
						docList.add(doc);	
						docIdList.add(rs.getLong("docid"));
				}
			} else {
				List<Document> docList = new ArrayList<>();
				Document doc = new Document();
				doc.setId(String.valueOf(rs.getLong("docid")));
				doc.setKpiCode(rs.getString("dockpicode"));
				doc.setCode(rs.getString("documentcode"));
				doc.setName(rs.getString("documentname"));
				doc.setActive(rs.getBoolean("mandatoryflag"));
				if (StringUtils.isNotBlank(rs.getString("documentname"))) {
					docList.add(doc);
					docIdList.add(rs.getLong("docid"));
				}
				docMap.put(String.valueOf(rs.getLong("id")), docList);
			}
			return null;
		}
	}

	public class KPIValueListRowMapper implements RowMapper<KpiValueList> {
		@Override
		public KpiValueList mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			KpiValueList valueList = new KpiValueList();
			valueList.setTenantId(rs.getString("tenantId"));
			KPI kpi = new KPI();
			kpi.setCode(rs.getString("kpiCode"));
			kpi.setTargetValue(rs.getLong("targetValue"));
			kpi.setInstructions(rs.getString("instructions"));
			valueList.setKpi(kpi);
			valueList.setFinYear(rs.getString("finYear"));
			KpiValue value = new KpiValue();
			value.setId(String.valueOf(rs.getLong("valueId")));
			value.setResultValue(rs.getLong("actualValue"));
			value.setTenantId(rs.getString("tenantId"));
			List<KpiValue> kpiValueList = new ArrayList<>();
			kpiValueList.add(value);
			valueList.setKpiValue(kpiValueList);
			return valueList;
		}
	}
	
	public class KPIValueRowMapper implements RowMapper<KpiValue> {
		public static final String TRUE_FLAG = "true";
		public static final String FALSE_FLAG = "false"; 
		@Override
		public KpiValue mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			KpiValue value = new KpiValue(); 
			value.setId(String.valueOf(rs.getString("valueId"))); 
			value.setResultValue(rs.getLong("actualValue"));
			value.setResultDescription(rs.getString("actualDescription"));
			value.setTenantId(rs.getString("tenantId"));
			
			KPI kpi = new KPI();
			kpi.setId(rs.getString("kpiId"));
			kpi.setCode(rs.getString("kpiCode"));
			kpi.setInstructions(rs.getString("instructions"));
			kpi.setFinancialYear(rs.getString("finYear"));
			kpi.setName(rs.getString("kpiName"));
			kpi.setTargetValue(rs.getLong("targetValue"));
			kpi.setTargetType(rs.getString("targetType"));
			kpi.setTargetDescription(rs.getString("targetDescription"));
			if(null != rs.getString("targetType") && rs.getString("targetType").equals(TargetType.OBJECTIVE.toString())) { 
				if(rs.getLong("targetValue") == 1) kpi.setTargetDescription("Yes");
				else if(rs.getLong("targetValue") == 2) kpi.setTargetDescription("No");
				else if(rs.getLong("targetValue") == 3) kpi.setTargetDescription("In Progress");
				
				if(rs.getLong("actualValue") == 1) value.setResultDescription("Yes");
				else if (rs.getLong("actualValue") == 2) value.setResultDescription("No");
				else if (rs.getLong("actualValue") == 3) value.setResultDescription("In Progress");
			}
			if(null != rs.getString("targetType") && rs.getString("targetType").equals(TargetType.VALUE.toString())) { 
				kpi.setTargetDescription(String.valueOf(rs.getLong("targetValue")));
				value.setResultDescription(String.valueOf(rs.getLong("actualValue")));
			}
			value.setKpi(kpi);
			return value; 
		}
	}

}
	
