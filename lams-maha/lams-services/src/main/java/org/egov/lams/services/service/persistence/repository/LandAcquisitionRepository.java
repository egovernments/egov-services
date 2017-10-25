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
				landAcquisitionQueryBuilder.buildSearchQuery(landAcquisitionSearchCriteria),
				getDetailNamedQuery(landAcquisitionSearchCriteria), new BeanPropertyRowMapper<>(LandAcquisition.class));
		System.out.println(landAcquisitionQueryBuilder.buildSearchQuery(landAcquisitionSearchCriteria));

		return LandAcquisitionResponse.builder()
		.landAcquisitions(landAcquisitionList)
		.build();
		
    }
	private HashMap<String, String> getDetailNamedQuery(LandAcquisitionSearchCriteria landAcquisitionSearchCriteria) {
		HashMap<String, String> parametersMap = new HashMap<String, String>();

		parametersMap.put("tenantId", landAcquisitionSearchCriteria.getTenantId());
		parametersMap.put("ctsNumber", landAcquisitionSearchCriteria.getCtsNumber());

		return parametersMap;
	}
	
	private List<LandAcquisition> getLandAcquisitionList(String sql, HashMap<String, String> searchNamedQuery,
			BeanPropertyRowMapper<LandAcquisition> rowMapper) {
		return namedParameterJdbcTemplate.query(sql, searchNamedQuery, rowMapper);
	}
}
