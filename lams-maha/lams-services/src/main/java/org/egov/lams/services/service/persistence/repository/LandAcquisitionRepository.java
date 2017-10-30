package org.egov.lams.services.service.persistence.repository;

import java.util.HashMap;
import java.util.List;

import org.egov.lams.common.web.contract.LandAcquisition;
import org.egov.lams.common.web.contract.LandAcquisitionSearchCriteria;
import org.egov.lams.common.web.response.LandAcquisitionResponse;
import org.egov.lams.services.service.persistence.queryBuilder.LandAcquisitionQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class LandAcquisitionRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private LandAcquisitionQueryBuilder landAcquisitionQueryBuilder;
    
    public LandAcquisitionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate ,
			LandAcquisitionQueryBuilder landAcquisitionQueryBuilder
            ) {
	this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	this.landAcquisitionQueryBuilder = landAcquisitionQueryBuilder;
}
    
	public LandAcquisitionResponse search(LandAcquisitionSearchCriteria landAcquisitionSearchCriteria) {
		
		List<LandAcquisition> landAcquisitionList = getLandAcquisitionList(
				landAcquisitionQueryBuilder.getQuery(landAcquisitionSearchCriteria),
				getDetailNamedQuery(landAcquisitionSearchCriteria), new BeanPropertyRowMapper<>(LandAcquisition.class));
		System.out.println(landAcquisitionQueryBuilder.getQuery(landAcquisitionSearchCriteria));

		return LandAcquisitionResponse.builder()
		.landAcquisition(landAcquisitionList)
		.build();
		
    }
	private HashMap<String, Object> getDetailNamedQuery(LandAcquisitionSearchCriteria landAcquisitionSearchCriteria) {
		HashMap<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("tenantId", landAcquisitionSearchCriteria.getTenantId());
		parametersMap.put("ctsNumber", landAcquisitionSearchCriteria.getCtsNumber());
		if(landAcquisitionSearchCriteria.getLandAcquisitionId() != null)
      {
		parametersMap.put("landAcquisitionId", Long.valueOf(landAcquisitionSearchCriteria.getLandAcquisitionId()));
      }
		parametersMap.put("landAcquisitionNumber", landAcquisitionSearchCriteria.getLandAcquisitionNumber());
		parametersMap.put("landOwnerName", landAcquisitionSearchCriteria.getLandOwnerName());
		parametersMap.put("surveyNo", landAcquisitionSearchCriteria.getSurveyNo());
		parametersMap.put("organizationName", landAcquisitionSearchCriteria.getOrganizationName());
		parametersMap.put("advocateName", landAcquisitionSearchCriteria.getAdvocateName());
		parametersMap.put("landType", landAcquisitionSearchCriteria.getLandType());
		parametersMap.put("pageSize", landAcquisitionSearchCriteria.getPageSize());
		parametersMap.put("pageNumber", landAcquisitionSearchCriteria.getPageNumber());

		return parametersMap;
	}
	
	private List<LandAcquisition> getLandAcquisitionList(String sql, HashMap<String, Object> searchNamedQuery,
			BeanPropertyRowMapper<LandAcquisition> rowMapper) {
		return namedParameterJdbcTemplate.query(sql, searchNamedQuery, rowMapper);
	}
}
