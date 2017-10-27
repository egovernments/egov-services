package org.egov.lams.services.service.persistence.repository;

import java.util.HashMap;
import java.util.List;

import org.egov.lams.common.web.contract.LandTransfer;
import org.egov.lams.common.web.contract.LandTransferResponse;
import org.egov.lams.common.web.contract.LandTransferSearchCriteria;
import org.egov.lams.services.service.persistence.queryBuilder.LandTransferQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository 
public class LandTransferRepository {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private LandTransferQueryBuilder landTransferQueryBuilder;
    
    public LandTransferRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate ,
    		LandTransferQueryBuilder landTransferQueryBuilder
            ) {
	this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	this.landTransferQueryBuilder = landTransferQueryBuilder;
}
    
	public LandTransferResponse search(LandTransferSearchCriteria landTransferSearchCriteria) {
		
		List<LandTransfer> landTransferList = getLandTransferList(
				landTransferQueryBuilder.getQuery(landTransferSearchCriteria),
				getDetailNamedQuery(landTransferSearchCriteria), new BeanPropertyRowMapper<>(LandTransfer.class));
		System.out.println(landTransferQueryBuilder.getQuery(landTransferSearchCriteria));
		
		return LandTransferResponse.builder()
		.landTransfer(landTransferList)
		.build();
		
    }
	
	private HashMap<String, Object> getDetailNamedQuery(LandTransferSearchCriteria landTransferSearchCriteria) {
		HashMap<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("tenantId", landTransferSearchCriteria.getTenantId());
		parametersMap.put("transferNumber", landTransferSearchCriteria.getTransferNumber());
		if(landTransferSearchCriteria.getLandAquisitionNumber() != null)
		{
		parametersMap.put("landAquisitionNumber", Long.valueOf(landTransferSearchCriteria.getLandAquisitionNumber()));
		}
		parametersMap.put("resolutionNumber", landTransferSearchCriteria.getResolutionNumber());
		parametersMap.put("transferFrom", landTransferSearchCriteria.getTransferFrom());
		if(landTransferSearchCriteria.getTransferedToDate() != null )
		{
		parametersMap.put("transferedToDate", Long.valueOf(landTransferSearchCriteria.getTransferedToDate()));
	    }
		parametersMap.put("pageSize", landTransferSearchCriteria.getPageSize());
		parametersMap.put("pageNumber", landTransferSearchCriteria.getPageNumber());

		return parametersMap;
	}
	
	private List<LandTransfer> getLandTransferList(String sql, HashMap<String, Object> searchNamedQuery,
			BeanPropertyRowMapper<LandTransfer> rowMapper) {
		return namedParameterJdbcTemplate.query(sql, searchNamedQuery, rowMapper);
	}

}
