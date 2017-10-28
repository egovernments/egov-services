package org.egov.lams.services.service.persistence.repository;

import java.util.HashMap;
import java.util.List;

import org.egov.lams.common.web.contract.LandPossession;
import org.egov.lams.common.web.contract.LandPossessionResponse;
import org.egov.lams.common.web.contract.LandPossessionSearchCriteria;
import org.egov.lams.services.service.persistence.queryBuilder.LandPossessionQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class LandPossessionRepository {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private LandPossessionQueryBuilder landPossessionQueryBuilder;
    
    public LandPossessionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate ,
    		LandPossessionQueryBuilder landPossessionQueryBuilder
            ) {
	this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	this.landPossessionQueryBuilder = landPossessionQueryBuilder;
}
    
	public LandPossessionResponse search(LandPossessionSearchCriteria landPossessionSearchCriteria) {
		
		List<LandPossession> landPossissionList = getLandPossessionList(
				landPossessionQueryBuilder.getQuery(landPossessionSearchCriteria),
				getDetailNamedQuery(landPossessionSearchCriteria), new BeanPropertyRowMapper<>(LandPossession.class));
		System.out.println(landPossessionQueryBuilder.getQuery(landPossessionSearchCriteria));
		
		return LandPossessionResponse.builder()
		.landPossession(landPossissionList)
		.build();
		
    }
	
	private HashMap<String, Object> getDetailNamedQuery(LandPossessionSearchCriteria landPossessionSearchCriteria) {
		HashMap<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("tenantId", landPossessionSearchCriteria.getTenantId());
		parametersMap.put("landPossessionId", landPossessionSearchCriteria.getLandPossessionId());
		if(landPossessionSearchCriteria.getLandAquisitionNumber() != null)
		{
		parametersMap.put("landAquisitionNumber", Long.valueOf(landPossessionSearchCriteria.getLandAquisitionNumber()));
		}
		parametersMap.put("possessionNumber", landPossessionSearchCriteria.getPossessionNumber());
		parametersMap.put("proposalNumber", landPossessionSearchCriteria.getProposalNumber());
		if(landPossessionSearchCriteria.getPossessionDate()!=null)
		{
		parametersMap.put("possessionDate", Long.valueOf(landPossessionSearchCriteria.getPossessionDate()));
		}
		parametersMap.put("landType", landPossessionSearchCriteria.getLandType());
		parametersMap.put("pageSize", landPossessionSearchCriteria.getPageSize());
		parametersMap.put("pageNumber", landPossessionSearchCriteria.getPageNumber());

		return parametersMap;
	}
	
	private List<LandPossession> getLandPossessionList(String sql, HashMap<String, Object> searchNamedQuery,
			BeanPropertyRowMapper<LandPossession> rowMapper) {
		return namedParameterJdbcTemplate.query(sql, searchNamedQuery, rowMapper);
	}

}
