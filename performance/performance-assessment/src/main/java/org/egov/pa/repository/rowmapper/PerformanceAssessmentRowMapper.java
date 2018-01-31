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
import org.egov.pa.model.TargetType;
import org.egov.pa.model.ValueDocument;
import org.egov.pa.utils.PerformanceAssessmentConstants;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PerformanceAssessmentRowMapper {

	public class KPIMasterRowMapper implements RowMapper<KPI> {
		public Map<String, KPI> kpiMap = new HashMap<>(); 
		/*public Map<String, KpiTarget> kpiTargetMap = new HashMap<>();*/

		@Override
		public KPI mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			 
			if(!kpiMap.containsKey(rs.getString("code"))) { 
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
				kpi.setCategoryId(rs.getString("categoryId"));
				List<KpiTarget> targetList = new ArrayList<>(); 
				targetList.add(constructKpiTargetObject(rs));
				kpi.setKpiTargets(targetList);
				kpiMap.put(rs.getString("code").concat("_"+rs.getString("targetFinYear")), kpi); 
			}
			
			/*if(!kpiTargetMap.containsKey(rs.getString("code").concat("_"+rs.getString("targetFinYear")))) { 
				kpiTargetMap.put(rs.getString("code").concat("_"+rs.getString("targetFinYear")), constructKpiTargetObject(rs)); 
			} */
			return null;
		}
		
		private KpiTarget constructKpiTargetObject(ResultSet rs) { 
			KpiTarget target = new KpiTarget();
			try { 
				target.setId(rs.getString("targetId"));
				target.setKpiCode(rs.getString("kpiCode"));
				target.setTargetValue(rs.getString("targetValue"));
				if(rs.getString("targetType").equals(TargetType.OBJECTIVE.toString())) { 
					if(rs.getString("targetValue").equals("1"))  
						target.setTargetDescription("Yes");
					else if (rs.getString("targetValue").equals("2"))
						target.setTargetDescription("No");
					else if (rs.getString("targetValue").equals("3"))
						target.setTargetDescription("WIP");
				} else {
					target.setTargetDescription(rs.getString("targetValue"));
				}
				target.setTenantId(rs.getString("tenantId"));
				target.setFinYear(rs.getString("targetFinYear"));
			} catch (Exception e) { 
				log.error("Encountered an exception while creating the Target Object : " + e);
			}
			return target; 
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
	
	public class KPITargetRowMapper implements RowMapper<KpiTarget> {
		public Map<String, KPI> kpiMap = new HashMap<>();
		
		@Override
		public KpiTarget mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			if(!kpiMap.containsKey(rs.getString("code"))) { 
				KPI kpi = new KPI(); 
				kpi.setId(rs.getString("id"));
				kpi.setName(rs.getString("name"));
				kpi.setCode(rs.getString("code"));
				kpi.setDepartmentId(rs.getLong("departmentId"));
				kpi.setFinancialYear(rs.getString("financialYear"));
				kpi.setInstructions(rs.getString("instructions"));
				kpi.setPeriodicity(rs.getString("periodicity"));
				kpi.setTargetType(rs.getString("targetType"));
				kpi.setCategoryId(rs.getString("categoryId"));
				kpiMap.put(rs.getString("code"), kpi); 
			}
			
			KpiTarget target = new KpiTarget(); 
			target.setId(rs.getString("targetId"));
			if(StringUtils.isNotBlank(rs.getString("kpiCode"))) { 
				target.setKpiCode(rs.getString("kpiCode"));
			} else { 
				target.setKpiCode(rs.getString("code"));
			}
			target.setFinYear(rs.getString("targetFinYear"));
			target.setTargetValue(rs.getString("targetValue"));
			if(StringUtils.isNotBlank(rs.getString("targetType")) && rs.getString("targetType").equals(TargetType.OBJECTIVE.toString())) { 
				if(StringUtils.isNotBlank(rs.getString("targetValue")) && rs.getString("targetValue").equals("1")) 
					target.setTargetDescription("Yes");
				else if (StringUtils.isNotBlank(rs.getString("targetValue")) && rs.getString("targetValue").equals("2")) 
					target.setTargetDescription("No");
				else if (StringUtils.isNotBlank(rs.getString("targetValue")) && rs.getString("targetValue").equals("3"))
					target.setTargetDescription("Work In Progress");
			} else {
				target.setTargetDescription(rs.getString("targetValue"));
			}
			return target;
		}
	}
	
	public class KPIObjectiveReportMapper implements RowMapper<KpiValueList> {
		public Map<String, KPI> kpiMap = new HashMap<>();
		public Map<String, Map<String, Map<String, KpiValue>>> reportMap = new HashMap<>();
		public Map<String, List<KpiValueDetail>> kpiValueDetailMap = new HashMap<>();
		public Map<String, List<KpiValueDetail>> valueDetailMap = new HashMap<>();
		
		@Override
		public KpiValueList mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			if(!kpiMap.containsKey(rs.getString("code"))) { 
				kpiMap.put(rs.getString("code"), addKpi(rs)); 
			}
			
			if(reportMap.containsKey(rs.getString("valueTenantId"))) {
				Map<String, Map<String, KpiValue>> secondMap = reportMap.get(rs.getString("valueTenantId"));
				if(secondMap.containsKey(rs.getString("targetFinYear"))) { 
					Map<String, KpiValue> thirdMap = secondMap.get(rs.getString("targetFinYear"));
					if(!thirdMap.containsKey(rs.getString("code"))) { 
						thirdMap.put(rs.getString("code"), addKpiValue(rs)); 
					}
				} else { 
					Map<String, KpiValue> thirdMap = new HashMap<>();
					thirdMap.put(rs.getString("code"), addKpiValue(rs)); 
					secondMap.put(rs.getString("targetFinYear"), thirdMap);
				}
			} else { 
				Map<String, KpiValue> thirdMap = new HashMap<>();
				thirdMap.put(rs.getString("code"), addKpiValue(rs)); 
				Map<String, Map<String, KpiValue>> secondMap = new HashMap<>();
				secondMap.put(rs.getString("targetFinYear"), thirdMap);
				reportMap.put(rs.getString("valueTenantId"), secondMap);
			}
			if(!kpiValueDetailMap.containsKey(rs.getString("detailValueId"))) { 
				KpiValueDetail detail = new KpiValueDetail();
				detail.setId(rs.getString("valueDetailId"));
				log.info("VALUE DETAIL ID IS NOW AS : "  + rs.getString("valueDetailId"));
				detail.setValue(rs.getString("value"));
				detail.setId(rs.getString("detailId"));
				detail.setPeriod(rs.getString("period"));
				List<KpiValueDetail> detailList = new ArrayList<>(); 
				detailList.add(detail);
				kpiValueDetailMap.put(rs.getString("detailValueId"), detailList); 
			} else { 
				List<KpiValueDetail> detailList  = kpiValueDetailMap.get(rs.getString("detailValueId")) ;
				KpiValueDetail detail = new KpiValueDetail(); 
				detail.setId(rs.getString("valueDetailId"));
				log.info("VALUE DETAIL ID IS NOW AS : "  + rs.getString("valueDetailId"));
				detail.setValue(rs.getString("value"));
				detail.setId(rs.getString("detailId"));
				detail.setPeriod(rs.getString("period"));
				detailList.add(detail);
			}
			
			if(StringUtils.isBlank(rs.getString("detailValueId"))) { 
				List<KpiValueDetail> valueDetailList = new ArrayList<>();
				KpiValueDetail detail =null ; 
				for(int i=0 ; i<PerformanceAssessmentConstants.MONTHLIST.size();i++) {
					detail = new KpiValueDetail();
					detail.setPeriod(PerformanceAssessmentConstants.MONTHLIST.get(i));
					detail.setValue("");
					valueDetailList.add(detail);
				}
				valueDetailMap.put(rs.getString("valueKpiCode").concat("_" + rs.getString("valueTenantId")).concat("_"+ rs.getString("valueFinYear")), valueDetailList);
			}
			return null; 
		}
		
		private KpiValue addKpiValue(ResultSet rs) {
			KpiValue value = new KpiValue(); 
			try { 
				value.setTenantId(rs.getString("valueTenantId"));
				value.setKpiCode(rs.getString("valueKpiCode"));
				value.setFinYear(rs.getString("targetFinYear"));
				value.setId(rs.getString("detailValueId"));
			} catch (Exception e) { 
				log.error("Encountered an exception while creating KpiValue Object " + e);
			}
			return value;
		}
		
		private KPI addKpi(ResultSet rs) { 
			KPI kpi = new KPI(); 
			try { 
				kpi.setName(rs.getString("name"));
				kpi.setCode(rs.getString("code")); 
				kpi.setId(rs.getString("id"));
				kpi.setInstructions(rs.getString("instructions"));
				kpi.setPeriodicity(rs.getString("periodicity"));
				kpi.setTargetType(rs.getString("targettype"));
				kpi.setFinancialYear(rs.getString("finyear"));
				kpi.setCategoryId(rs.getString("categoryId"));
				KpiTarget target = new KpiTarget(); 
				target.setId(rs.getString("targetId"));
				target.setKpiCode(rs.getString("targetKpiCode"));
				target.setTargetValue(rs.getString("targetvalue"));
				if(StringUtils.isNotBlank(rs.getString("targetType")) && rs.getString("targetType").equals(TargetType.OBJECTIVE.toString())) { 
					if(rs.getString("targetValue").equals("1")) 
						target.setTargetDescription("Yes");
					else if (rs.getString("targetValue").equals("2")) 
						target.setTargetDescription("No");
					else if (rs.getString("targetValue").equals("3"))
						target.setTargetDescription("Work In Progress");
				} else {
					target.setTargetDescription(rs.getString("targetValue"));
				}
				List<KpiTarget> targetList = new ArrayList<>();
				targetList.add(target);
				kpi.setKpiTargets(targetList);
			} 
			catch (Exception e) { 
				log.error("Encountered an exception while creating KPI Object " + e);
			}
			return kpi;
		}
			
	}
	
	public class ValueDocumentMapper implements RowMapper<ValueDocument> {

		@Override
		public ValueDocument mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			ValueDocument doc = new ValueDocument(); 
			doc.setId(rs.getString("valueDocumentId"));
			doc.setKpiCode(rs.getString("kpiCode"));
			doc.setDocumentCode(rs.getString("documentCode"));
			doc.setFileStoreId(rs.getString("fileStoreId"));
			doc.setValueId(rs.getString("valueDetailId"));
			return doc;
		}
	}
	
	public class KPIValueReportMapper implements RowMapper<KpiValueList> {
		public Map<String, Map<String, Map<String, KpiValue>>> reportMap = new HashMap<>();
		public Map<String, KPI> kpiMap = new HashMap<>();
		public Map<String, List<KpiValueDetail>> valueListMap = new HashMap<>(); 
		
		@Override
		public KpiValueList mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			if(reportMap.containsKey(rs.getString("valueTenantId"))) {
				Map<String, Map<String, KpiValue>> secondMap = reportMap.get(rs.getString("valueTenantId"));
				if(secondMap.containsKey(rs.getString("targetFinYear"))) { 
					Map<String, KpiValue> thirdMap = secondMap.get(rs.getString("targetFinYear"));
					if(!thirdMap.containsKey(rs.getString("code"))) { 
						thirdMap.put(rs.getString("code"), addKpiValue(rs)); 
					} else { 
						KpiValue kpiValue = thirdMap.get(rs.getString("code")); 
						KpiValueDetail detail = addKpiValueDetail(rs);
						kpiValue.getValueList().add(detail);
					}
				} else { 
					Map<String, KpiValue> thirdMap = new HashMap<>();
					thirdMap.put(rs.getString("code"), addKpiValue(rs)); 
					secondMap.put(rs.getString("targetFinYear"), thirdMap);
				}
			} else { 
				Map<String, KpiValue> thirdMap = new HashMap<>();
				thirdMap.put(rs.getString("code"), addKpiValue(rs)); 
				Map<String, Map<String, KpiValue>> secondMap = new HashMap<>();
				secondMap.put(rs.getString("targetFinYear"), thirdMap);
				reportMap.put(rs.getString("valueTenantId"), secondMap);
			}
			addKpi(rs);
			return null;
		}
		
		private KPI addKpi(ResultSet rs) {
			KPI kpi = new KPI();
			try {
				if(!kpiMap.containsKey(rs.getString("code").concat("_" + rs.getString("targetFinYear")))) {
					kpi.setName(rs.getString("name"));
					kpi.setCode(rs.getString("code")); 
					kpi.setId(rs.getString("id"));
					kpi.setInstructions(rs.getString("instructions"));
					kpi.setPeriodicity(rs.getString("periodicity"));
					kpi.setTargetType(rs.getString("targettype"));
					kpi.setFinancialYear(rs.getString("finyear"));
					kpi.setCategoryId(rs.getString("categoryId"));
					KpiTarget target = new KpiTarget();
					target.setId(rs.getString("targetId"));
					target.setKpiCode(rs.getString("targetKpiCode"));
					target.setTargetValue(rs.getString("targetvalue"));
					if(StringUtils.isNotBlank(rs.getString("targetType")) && rs.getString("targetType").equals(TargetType.OBJECTIVE.toString())) { 
						if(rs.getString("targetValue").equals("1")) 
							target.setTargetDescription("Yes");
						else if (rs.getString("targetValue").equals("2")) 
							target.setTargetDescription("No");
						else if (rs.getString("targetValue").equals("3"))
							target.setTargetDescription("Work In Progress");
					} else {
						target.setTargetDescription(rs.getString("targetValue"));
					}
					target.setFinYear(rs.getString("targetFinYear"));
					List<KpiTarget> targetList = new ArrayList<>();
					targetList.add(target); 
					kpi.setKpiTargets(targetList);
					kpiMap.put(rs.getString("code").concat("_" + rs.getString("targetFinYear")), kpi);
				} 
			} 
			catch (Exception e) { 
				log.error("Encountered an exception while creating KPI Object " + e);
			}
			return kpi;
		}
		
		private KpiValue addKpiValue(ResultSet rs) {
			KpiValue value = new KpiValue(); 
			try { 
				value.setId(rs.getString("valueid"));
				if(!valueListMap.containsKey(rs.getString("valueid"))) { 
					List<KpiValueDetail> valueDetailList = new ArrayList<>(); 
					valueDetailList.add(prepareValueDetailObject(rs));
					valueListMap.put(rs.getString("valueid"), valueDetailList); 
				} else { 
					List<KpiValueDetail> valueDetailList = valueListMap.get(rs.getString("valueid")); 
					valueDetailList.add(prepareValueDetailObject(rs));
				}
				value.setTenantId(rs.getString("valueTenantId"));
				value.setKpiCode(rs.getString("valueKpiCode"));
				value.setFinYear(rs.getString("targetFinYear"));
				List<KpiValueDetail> valueDetailList = new ArrayList<>(); 
				value.setValueList(valueDetailList);
			} catch (Exception e) { 
				log.error("Encountered an exception while creating KpiValue Object " + e);
			}
			return value;
		}
		
		private KpiValueDetail addKpiValueDetail(ResultSet rs) {
			KpiValueDetail detail = new KpiValueDetail(); 
			try { 
				if(!valueListMap.containsKey(rs.getString("valueid"))) { 
					List<KpiValueDetail> valueDetailList = new ArrayList<>(); 
					valueDetailList.add(prepareValueDetailObject(rs));
					valueListMap.put(rs.getString("valueid"), valueDetailList); 
				} else { 
					List<KpiValueDetail> valueDetailList = valueListMap.get(rs.getString("valueid")); 
					valueDetailList.add(prepareValueDetailObject(rs));
				}
			} catch(Exception e) { 
				log.error("Encountered an exception while creating KpiValue Object " + e);
			}
			return detail;
		}
		
		private KpiValueDetail prepareValueDetailObject(ResultSet rs) { 
			KpiValueDetail detail = new KpiValueDetail();
			try { 
				detail.setId(rs.getString("valueDetailId"));
				detail.setValueid(rs.getString("valueid"));
				detail.setKpiCode(rs.getString("valueKpiCode"));
				detail.setValue(rs.getString("detailValue"));
				detail.setPeriod(rs.getString("detailPeriod"));
			} catch (Exception e) { 
				log.error("Encountered an exception while creating KpiValue Detail Object " + e);
			}
			return detail;
		}
	}

	public class KPIValueRowMapper implements RowMapper<KpiValue> {
		public Map<String, KpiValue> valueMap = new HashMap<>();
		public Map<String, List<KpiValueDetail>> valueDetailMap = new HashMap<>();
		public Map<String, List<ValueDocument>> detailDocumentMap = new HashMap<>();
		public List<String> detailList = new ArrayList<>();
		
		@Override
		public KpiValue mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			if(!valueMap.containsKey(rs.getString("id"))) { 
				KpiValue value = new KpiValue();
				value.setId(rs.getString("id"));
				value.setKpiCode(rs.getString("kpiCode"));
				value.setTenantId(rs.getString("tenantId"));
				value.setFinYear(rs.getString("valueFinYear"));
				value.setAuditDetails(addAuditDetails(rs));
				valueMap.put(rs.getString("id"), value); 
			}
			
			if(!detailDocumentMap.containsKey(rs.getString("valueDetailId"))) {
				List<ValueDocument> docList = new ArrayList<>(); 
				if(StringUtils.isNotBlank(rs.getString("fileStoreId"))) { 
					ValueDocument doc = new ValueDocument();
					doc.setFileStoreId(rs.getString("fileStoreId"));
					doc.setDocumentCode(rs.getString("docDocumentCode"));
					doc.setDocumentName(rs.getString("docMasterDocName")); 
 	 				doc.setKpiCode(rs.getString("docKpiCode"));
					docList.add(doc);
				}
				detailDocumentMap.put(rs.getString("valueDetailId"), docList); 
 			} else { 
 				List<ValueDocument> docList = detailDocumentMap.get(rs.getString("valueDetailId"));
 				if(StringUtils.isNotBlank(rs.getString("fileStoreId"))) { 
 					ValueDocument doc = new ValueDocument();
 	 				doc.setFileStoreId(rs.getString("fileStoreId"));
 	 				doc.setDocumentCode(rs.getString("docDocumentCode"));
					doc.setDocumentName(rs.getString("docMasterDocName"));	
 	 				doc.setKpiCode(rs.getString("docKpiCode"));
 	 				docList.add(doc);
 				}
 			}
			
			if(StringUtils.isNotBlank(rs.getString("valueId")) && !valueDetailMap.containsKey(rs.getString("valueId"))) { 
				List<KpiValueDetail> valueDetailList = new ArrayList<>();
				valueDetailList.add(addValueDetails(rs)); 
				valueDetailMap.put(rs.getString("valueId"), valueDetailList); 
				detailList.add(rs.getString("valueDetailId"));
			} else if(StringUtils.isNotBlank(rs.getString("valueId")) && valueDetailMap.containsKey(rs.getString("valueId"))) {
				List<KpiValueDetail> valueDetailList = valueDetailMap.get(rs.getString("valueId"));
				if(!detailList.contains(rs.getString("valueDetailId"))) { 
					valueDetailList.add(addValueDetails(rs));
					detailList.add(rs.getString("valueDetailId")); 
				}
			} else if(StringUtils.isBlank(rs.getString("valueId"))) { 
				List<KpiValueDetail> valueDetailList = new ArrayList<>();
				KpiValueDetail detail =null ; 
				for(int i=0 ; i<PerformanceAssessmentConstants.MONTHLIST.size();i++) {
					detail = new KpiValueDetail();
					detail.setPeriod(PerformanceAssessmentConstants.MONTHLIST.get(i));
					detail.setValue("");
					valueDetailList.add(detail);
				}
				valueDetailMap.put(rs.getString("kpiCode").concat("_" + rs.getString("tenantId")).concat("_"+ rs.getString("valueFinYear")), valueDetailList); 
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
			detail.setId(rs.getString("valueDetailId"));
			detail.setRemarks(rs.getString("valueRemarks"));
		}  catch (Exception e) {
			log.error("Encountered an exception while adding Value Details" + e);
		}
		return detail;
	}
	
	

}
	
