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
	final static String ID = "id" ;
	final static String NAME = "name"; 
	final static String CODE = "code" ;
	final static String KPICODE = "kpiCode"; 
	final static String FINANCIALYEAR = "financialYear"; 
	final static String TARGETTYPE = "targetType" ; 
	final static String TENANTID = "tenantId";  
	final static String TARGETVALUE = "targetValue" ; 
	final static String INSTRUCTIONS = "instructions"; 
	final static String PERIODICITY = "periodicity" ; 
	final static String DEPARTMENTID = "departmentId";
	final static String CATEGORYID = "categoryId" ;
	final static String DETAILVALUEID = "detailValueId" ; 
	final static String VALUEDETAILID = "valueDetailId" ;
	final static String VALUEID = "valueId" ; 
	final static String TARGETFINYEAR = "targetFinYear";
	final static String YES = "Yes" ; 
	final static String NO = "No" ;
	final static String WIP = "Work in Progress" ; 

	public class KPIMasterRowMapper implements RowMapper<KPI> {
		public Map<String, KPI> kpiMap = new HashMap<>(); 
		public Map<String, List<KpiTarget>> kpiTargetMap = new HashMap<>();

		@Override
		public KPI mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			 
			if(!kpiMap.containsKey(rs.getString(CODE))) { 
				kpiMap.put(rs.getString(CODE), constructKpiObject(rs)); 
			}
			
			if(!kpiTargetMap.containsKey(rs.getString(CODE))) { 
				List<KpiTarget> targetList = new ArrayList<>(); 
				targetList.add(constructKpiTargetObject(rs));
				kpiTargetMap.put(rs.getString(CODE), targetList); 
			} else { 
				List<KpiTarget> targetList = kpiTargetMap.get(rs.getString(CODE)); 
				targetList.add(constructKpiTargetObject(rs));
			}
			return null;
		}
		
		private KPI constructKpiObject(ResultSet rs) {
			KPI kpi = new KPI();
			try { 
				kpi.setId(rs.getString(ID));
				kpi.setName(rs.getString(NAME));
				kpi.setCode(rs.getString(CODE));
				kpi.setDepartmentId(rs.getLong(DEPARTMENTID));
				kpi.setFinancialYear(rs.getString(FINANCIALYEAR));
				kpi.setInstructions(rs.getString(INSTRUCTIONS));
				kpi.setPeriodicity(rs.getString(PERIODICITY));
				kpi.setTargetType(rs.getString(TARGETTYPE));
				kpi.setAuditDetails(addAuditDetails(rs));
				kpi.setCategoryId(rs.getString(CATEGORYID));
			} catch (Exception e) { 
				log.error("Encountered an exception while creating the KPI Object " + e );
			}
			return kpi;
		}
		
		private KpiTarget constructKpiTargetObject(ResultSet rs) { 
			KpiTarget target = new KpiTarget();
			try { 
				target.setId(rs.getString("targetId"));
				target.setKpiCode(rs.getString(KPICODE));
				target.setTargetValue(rs.getString(TARGETVALUE));
				if(rs.getString(TARGETTYPE).equals(TargetType.OBJECTIVE.toString())) { 
					if(rs.getString(TARGETVALUE).equals("1"))  
						target.setTargetDescription(YES);
					else if (rs.getString(TARGETVALUE).equals("2"))
						target.setTargetDescription(NO);
					else if (rs.getString(TARGETVALUE).equals("3"))
						target.setTargetDescription(WIP);
				} else {
					target.setTargetDescription(rs.getString(TARGETVALUE));
				}
				target.setTenantId(rs.getString(TENANTID));
				target.setFinYear(rs.getString(TARGETFINYEAR));
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
			valueList.setTenantId(rs.getString(TENANTID));
			KPI kpi = new KPI();
			kpi.setCode(rs.getString(CODE));
			kpi.setInstructions(rs.getString(INSTRUCTIONS));
			valueList.setKpi(kpi);
			valueList.setFinYear(rs.getString(FINANCIALYEAR));
			KpiValue value = new KpiValue();
			value.setId(String.valueOf(rs.getLong(VALUEID)));
			value.setTenantId(rs.getString(TENANTID));
			List<KpiValue> kpiValueList = new ArrayList<>();
			kpiValueList.add(value);
			return valueList;
		}
	}
	
	public class KPITargetRowMapper implements RowMapper<KpiTarget> {
		public Map<String, KPI> kpiMap = new HashMap<>();
		
		@Override
		public KpiTarget mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			if(!kpiMap.containsKey(rs.getString(CODE))) { 
				kpiMap.put(rs.getString(CODE), constructKpiObject(rs)); 
			}
			
			KpiTarget target = new KpiTarget(); 
			target.setId(rs.getString("targetId"));
			if(StringUtils.isNotBlank(rs.getString(KPICODE))) { 
				target.setKpiCode(rs.getString(KPICODE));
			} else { 
				target.setKpiCode(rs.getString(CODE));
			}
			target.setFinYear(rs.getString(TARGETFINYEAR));
			target.setTargetValue(rs.getString(TARGETVALUE));
			if(StringUtils.isNotBlank(rs.getString(TARGETTYPE)) && rs.getString(TARGETTYPE).equals(TargetType.OBJECTIVE.toString())) { 
				if(StringUtils.isNotBlank(rs.getString(TARGETTYPE)) && rs.getString(TARGETTYPE).equals("1")) 
					target.setTargetDescription(YES);
				else if (StringUtils.isNotBlank(rs.getString(TARGETTYPE)) && rs.getString(TARGETTYPE).equals("2")) 
					target.setTargetDescription(NO);
				else if (StringUtils.isNotBlank(rs.getString(TARGETTYPE)) && rs.getString(TARGETTYPE).equals("3"))
					target.setTargetDescription(WIP);
			} else {
				target.setTargetDescription(rs.getString(TARGETTYPE));
			}
			return target;
		}
		
		private KPI constructKpiObject(ResultSet rs) {
			KPI kpi = new KPI();
			try { 
				kpi.setId(rs.getString(ID));
				kpi.setName(rs.getString(NAME));
				kpi.setCode(rs.getString(CODE));
				kpi.setDepartmentId(rs.getLong(DEPARTMENTID));
				kpi.setFinancialYear(rs.getString(FINANCIALYEAR));
				kpi.setInstructions(rs.getString(INSTRUCTIONS));
				kpi.setPeriodicity(rs.getString(PERIODICITY));
				kpi.setTargetType(rs.getString(TARGETTYPE));
				kpi.setCategoryId(rs.getString(CATEGORYID));
			} catch (Exception e) { 
				log.error("Encountered an exception while creating the KPI Object " + e );
			}
			return kpi;
		}
	}
	
	public class KPIObjectiveReportMapper implements RowMapper<KpiValueList> {
		public Map<String, KPI> kpiMap = new HashMap<>();
		public Map<String, Map<String, Map<String, KpiValue>>> reportMap = new HashMap<>();
		public Map<String, List<KpiValueDetail>> kpiValueDetailMap = new HashMap<>();
		public Map<String, List<KpiValueDetail>> valueDetailMap = new HashMap<>();
		
		@Override
		public KpiValueList mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			if(!kpiMap.containsKey(rs.getString(CODE))) { 
				kpiMap.put(rs.getString(CODE), addKpi(rs)); 
			}
			
			if(reportMap.containsKey(rs.getString("valueTenantId"))) {
				Map<String, Map<String, KpiValue>> secondMap = reportMap.get(rs.getString("valueTenantId"));
				if(secondMap.containsKey(rs.getString(TARGETFINYEAR))) { 
					Map<String, KpiValue> thirdMap = secondMap.get(rs.getString(TARGETFINYEAR));
					if(!thirdMap.containsKey(rs.getString(CODE))) { 
						thirdMap.put(rs.getString(CODE), addKpiValue(rs)); 
					}
				} else { 
					Map<String, KpiValue> thirdMap = new HashMap<>();
					thirdMap.put(rs.getString(CODE), addKpiValue(rs)); 
					secondMap.put(rs.getString(TARGETFINYEAR), thirdMap);
				}
			} else { 
				Map<String, KpiValue> thirdMap = new HashMap<>();
				thirdMap.put(rs.getString(CODE), addKpiValue(rs)); 
				Map<String, Map<String, KpiValue>> secondMap = new HashMap<>();
				secondMap.put(rs.getString(TARGETFINYEAR), thirdMap);
				reportMap.put(rs.getString("valueTenantId"), secondMap);
			}
			if(!kpiValueDetailMap.containsKey(rs.getString(DETAILVALUEID))) { 
				List<KpiValueDetail> detailList = new ArrayList<>(); 
				detailList.add(prepareDetailObject(rs));
				kpiValueDetailMap.put(rs.getString(DETAILVALUEID), detailList); 
			} else { 
				List<KpiValueDetail> detailList  = kpiValueDetailMap.get(rs.getString(DETAILVALUEID)) ;
				detailList.add(prepareDetailObject(rs));
			}
			
			if(StringUtils.isBlank(rs.getString(DETAILVALUEID))) { 
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
		
		private KpiValueDetail prepareDetailObject(ResultSet rs) { 
			KpiValueDetail detail = new KpiValueDetail();
			try { 
				detail.setId(rs.getString(VALUEDETAILID));
				detail.setValue(rs.getString("value"));
				detail.setPeriod(rs.getString("period"));
				detail.setRemarks(rs.getString("valueDetailRemarks"));
			} catch(Exception e) { 
				log.error("Encountered an exception while creating the KPI Value Detail Object" + e);
			}
			return detail;
		}
		
		private KpiValue addKpiValue(ResultSet rs) {
			KpiValue value = new KpiValue(); 
			try { 
				value.setTenantId(rs.getString("valueTenantId"));
				value.setKpiCode(rs.getString("valueKpiCode"));
				value.setFinYear(rs.getString(TARGETFINYEAR));
				value.setId(rs.getString(DETAILVALUEID));
			} catch (Exception e) { 
				log.error("Encountered an exception while creating KpiValue Object " + e);
			}
			return value;
		}
		
		private KPI addKpi(ResultSet rs) { 
			KPI kpi = new KPI(); 
			try { 
				kpi.setName(rs.getString(NAME));
				kpi.setCode(rs.getString(CODE)); 
				kpi.setId(rs.getString(ID));
				kpi.setInstructions(rs.getString(INSTRUCTIONS));
				kpi.setPeriodicity(rs.getString(PERIODICITY));
				kpi.setTargetType(rs.getString(TARGETTYPE));
				kpi.setFinancialYear(rs.getString("finyear"));
				kpi.setCategoryId(rs.getString(CATEGORYID));
				KpiTarget target = new KpiTarget(); 
				target.setId(rs.getString("targetId"));
				target.setKpiCode(rs.getString("targetKpiCode"));
				target.setTargetValue(rs.getString(TARGETVALUE));
				if(StringUtils.isNotBlank(rs.getString(TARGETTYPE)) && rs.getString(TARGETTYPE).equals(TargetType.OBJECTIVE.toString())) { 
					if(rs.getString(TARGETVALUE).equals("1")) 
						target.setTargetDescription(YES);
					else if (rs.getString(TARGETVALUE).equals("2")) 
						target.setTargetDescription(NO);
					else if (rs.getString(TARGETVALUE).equals("3"))
						target.setTargetDescription(WIP);
				} else {
					target.setTargetDescription(rs.getString(TARGETVALUE));
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
			doc.setKpiCode(rs.getString(KPICODE));
			doc.setDocumentCode(rs.getString("documentCode"));
			doc.setDocumentName(rs.getString("documentName"));
			doc.setFileStoreId(rs.getString("fileStoreId"));
			doc.setValueId(rs.getString(VALUEDETAILID));
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
					Map<String, KpiValue> thirdMap = secondMap.get(rs.getString(TARGETFINYEAR));
					if(!thirdMap.containsKey(rs.getString(CODE))) { 
						thirdMap.put(rs.getString(CODE), addKpiValue(rs)); 
					} else { 
						KpiValue kpiValue = thirdMap.get(rs.getString(CODE)); 
						KpiValueDetail detail = addKpiValueDetail(rs);
						kpiValue.getValueList().add(detail);
					}
				} else { 
					Map<String, KpiValue> thirdMap = new HashMap<>();
					thirdMap.put(rs.getString(CODE), addKpiValue(rs)); 
					secondMap.put(rs.getString("targetFinYear"), thirdMap);
				}
			} else { 
				Map<String, KpiValue> thirdMap = new HashMap<>();
				thirdMap.put(rs.getString(CODE), addKpiValue(rs)); 
				Map<String, Map<String, KpiValue>> secondMap = new HashMap<>();
				secondMap.put(rs.getString(TARGETFINYEAR), thirdMap);
				reportMap.put(rs.getString("valueTenantId"), secondMap);
			}
			addKpi(rs);
			return null;
		}
		
		private KPI addKpi(ResultSet rs) {
			KPI kpi = new KPI();
			try {
				if(!kpiMap.containsKey(rs.getString(CODE).concat("_" + rs.getString(TARGETFINYEAR)))) {
					kpi.setName(rs.getString(NAME));
					kpi.setCode(rs.getString(CODE)); 
					kpi.setId(rs.getString(ID));
					kpi.setInstructions(rs.getString(INSTRUCTIONS));
					kpi.setPeriodicity(rs.getString(PERIODICITY));
					kpi.setTargetType(rs.getString(TARGETTYPE));
					kpi.setFinancialYear(rs.getString("finyear"));
					kpi.setCategoryId(rs.getString(CATEGORYID));
					KpiTarget target = new KpiTarget();
					target.setId(rs.getString("targetId"));
					target.setKpiCode(rs.getString("targetKpiCode"));
					target.setTargetValue(rs.getString(TARGETVALUE));
					if(StringUtils.isNotBlank(rs.getString(TARGETTYPE)) && rs.getString(TARGETTYPE).equals(TargetType.OBJECTIVE.toString())) { 
						if(rs.getString(TARGETVALUE).equals("1")) 
							target.setTargetDescription(YES);
						else if (rs.getString(TARGETVALUE).equals("2")) 
							target.setTargetDescription(NO);
						else if (rs.getString(TARGETVALUE).equals("3"))
							target.setTargetDescription(WIP);
					} else {
						target.setTargetDescription(rs.getString(TARGETVALUE));
					}
					target.setFinYear(rs.getString(TARGETFINYEAR));
					List<KpiTarget> targetList = new ArrayList<>();
					targetList.add(target); 
					kpi.setKpiTargets(targetList);
					kpiMap.put(rs.getString(CODE).concat("_" + rs.getString(TARGETFINYEAR)), kpi);
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
				value.setId(rs.getString(VALUEID));
				if(!valueListMap.containsKey(rs.getString(VALUEID))) { 
					List<KpiValueDetail> valueDetailList = new ArrayList<>(); 
					valueDetailList.add(prepareValueDetailObject(rs));
					valueListMap.put(rs.getString(VALUEID), valueDetailList); 
				} else { 
					List<KpiValueDetail> valueDetailList = valueListMap.get(rs.getString(VALUEID)); 
					valueDetailList.add(prepareValueDetailObject(rs));
				}
				value.setTenantId(rs.getString("valueTenantId"));
				value.setKpiCode(rs.getString("valueKpiCode"));
				value.setFinYear(rs.getString(TARGETFINYEAR));
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
				if(!valueListMap.containsKey(rs.getString(VALUEID))) { 
					List<KpiValueDetail> valueDetailList = new ArrayList<>(); 
					valueDetailList.add(prepareValueDetailObject(rs));
					valueListMap.put(rs.getString(VALUEID), valueDetailList); 
				} else { 
					List<KpiValueDetail> valueDetailList = valueListMap.get(rs.getString(VALUEID)); 
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
				detail.setId(rs.getString(VALUEDETAILID));
				detail.setValueid(rs.getString(VALUEID));
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
			
			if(!valueMap.containsKey(rs.getString(ID))) { 
				valueMap.put(rs.getString(ID), constructKpiValueObject(rs)); 
			}
			
			if(!detailDocumentMap.containsKey(rs.getString(VALUEDETAILID))) {
				List<ValueDocument> docList = new ArrayList<>(); 
				if(StringUtils.isNotBlank(rs.getString("fileStoreId"))) { 
					docList.add(constructValueDocumentObject(rs));
				}
				detailDocumentMap.put(rs.getString(VALUEDETAILID), docList); 
 			} else { 
 				List<ValueDocument> docList = detailDocumentMap.get(rs.getString(VALUEDETAILID));
 				if(StringUtils.isNotBlank(rs.getString("fileStoreId"))) { 
 	 				docList.add(constructValueDocumentObject(rs));
 				}
 			}
			
			if(StringUtils.isNotBlank(rs.getString(VALUEID)) && !valueDetailMap.containsKey(rs.getString(VALUEID))) { 
				List<KpiValueDetail> valueDetailList = new ArrayList<>();
				valueDetailList.add(addValueDetails(rs)); 
				valueDetailMap.put(rs.getString(VALUEID), valueDetailList); 
				detailList.add(rs.getString(VALUEDETAILID));
			} else if(StringUtils.isNotBlank(rs.getString(VALUEID)) && valueDetailMap.containsKey(rs.getString(VALUEID))) {
				List<KpiValueDetail> valueDetailList = valueDetailMap.get(rs.getString(VALUEID));
				if(!detailList.contains(rs.getString(VALUEDETAILID))) { 
					valueDetailList.add(addValueDetails(rs));
					detailList.add(rs.getString(VALUEDETAILID)); 
				}
			} else if(StringUtils.isBlank(rs.getString(VALUEID))) { 
				List<KpiValueDetail> valueDetailList = new ArrayList<>();
				KpiValueDetail detail =null ; 
				for(int i=0 ; i<PerformanceAssessmentConstants.MONTHLIST.size();i++) {
					detail = new KpiValueDetail();
					detail.setPeriod(PerformanceAssessmentConstants.MONTHLIST.get(i));
					detail.setValue("");
					valueDetailList.add(detail);
				}
				valueDetailMap.put(rs.getString(KPICODE).concat("_" + rs.getString(TENANTID)).concat("_"+ rs.getString("valueFinYear")), valueDetailList); 
			}
			return null; 
		}
	}
	
	private ValueDocument constructValueDocumentObject(ResultSet rs) {
		ValueDocument doc = new ValueDocument();
		try { 
			doc.setFileStoreId(rs.getString("fileStoreId"));
			doc.setDocumentCode(rs.getString("docDocumentCode"));
			doc.setDocumentName(rs.getString("docMasterDocName")); 
 			doc.setKpiCode(rs.getString("docKpiCode"));
		} catch(Exception e) { 
			log.error("Encountered an Exception while creating the Value Document Object : " + e);
		}
		return doc; 
	}
	
	private KpiValue constructKpiValueObject(ResultSet rs) { 
		KpiValue value = new KpiValue(); 
		try { 
			value.setId(rs.getString(ID));
			value.setKpiCode(rs.getString(KPICODE));
			value.setTenantId(rs.getString(TENANTID));
			value.setFinYear(rs.getString("valueFinYear"));
			value.setAuditDetails(addAuditDetails(rs));
		} catch (Exception e) { 
			log.error("Encountered an Exception while creating the KPI Value Object : " + e);
		}
		return value; 
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
			detail.setValueid(rs.getString(VALUEID));
			detail.setId(rs.getString(VALUEDETAILID));
			detail.setRemarks(rs.getString("valueRemarks"));
		}  catch (Exception e) {
			log.error("Encountered an exception while adding Value Details" + e);
		}
		return detail;
	}
	
	

}
	
