package org.egov.lcms.notification.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.notification.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseRepository {

	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public String getCaseNumbers(String tenantId, Set<String> summonRefNos, RequestInfo requestInfo) throws Exception {
		
		log.info("Get caseNo repository, summonRefNos"+ summonRefNos);
		StringBuilder sb= new StringBuilder();
		String filter = "";
		
		for(String refNo: summonRefNos) {
			 sb.append( "'"+refNo+"'," );
		}
		
		filter = sb.toString();
		filter = filter.substring(0, filter.length()-1);		
		String summonRefNumbers = "";
		Set<String> uniqueCaseNos = new HashSet<String>();
		String searchQuery = "SELECT caseNo FROM egov_lcms_case WHERE tenantid = ?, summonReferenceNo IN("+filter+")";
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(searchQuery, new Object[] {tenantId});
			for (Map<String, Object> row : rows) {
				uniqueCaseNos.add(getString(row.get("caseNo")));
			}
			for(String summonRefNumber : uniqueCaseNos) {
				summonRefNumbers = summonRefNumbers.concat(summonRefNumber);
			}
		} catch (Exception e) {
			log.info("Error occured while fetching case numbers!");
			e.printStackTrace();
		}		
		return summonRefNumbers;
	}
	
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}
}
