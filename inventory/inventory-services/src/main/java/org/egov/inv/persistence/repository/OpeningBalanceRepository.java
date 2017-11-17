package org.egov.inv.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.JdbcRepository;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.OpeningBalanceResponse;
import org.egov.inv.model.OpeningBalanceSearchCriteria;
import org.egov.inv.persistence.entity.MaterialEntity;
import org.egov.inv.persistence.entity.OpeningBalanceEntity;
import org.egov.inv.persistence.queryBuilder.OpeningbalanceQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OpeningBalanceRepository extends JdbcRepository{
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private OpeningbalanceQueryBuilder openingbalanceQueryBuilder;

    public OpeningBalanceRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate ,
			OpeningbalanceQueryBuilder openingbalanceQueryBuilder
            ) {
	this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	this.openingbalanceQueryBuilder = openingbalanceQueryBuilder;
}
	
public OpeningBalanceResponse search(OpeningBalanceSearchCriteria openBalSearchCriteria) {
		
		List<OpeningBalanceEntity> openingBalanceList = getOpenbalList(
				openingbalanceQueryBuilder.getQuery(openBalSearchCriteria),
				getDetailNamedQuery(openBalSearchCriteria), new BeanPropertyRowMapper<>(OpeningBalanceEntity.class));
        List<MaterialReceipt> materialList = openingBalanceList.stream().map(OpeningBalanceEntity::toDomain)
                .collect(Collectors.toList());		
        System.out.println(openingbalanceQueryBuilder.getQuery(openBalSearchCriteria));

		return OpeningBalanceResponse.builder()
		.materialReceipt(materialList)
		.build();
		
    }

private List<OpeningBalanceEntity> getOpenbalList(String sql, HashMap<String, Object> searchNamedQuery,
		BeanPropertyRowMapper<OpeningBalanceEntity> rowMapper) {
	return namedParameterJdbcTemplate.query(sql, searchNamedQuery, rowMapper);
}

private HashMap<String, Object> getDetailNamedQuery(OpeningBalanceSearchCriteria searchCriteria) {
	HashMap<String, Object> parametersMap = new HashMap<String, Object>();
	parametersMap.put("tenantId", searchCriteria.getTenantId());
	parametersMap.put("financialYear", searchCriteria.getFinancialYear());
	parametersMap.put("storeName", searchCriteria.getStoreName());
	parametersMap.put("materialName", searchCriteria.getMaterialName());
	parametersMap.put("materialTypeName", searchCriteria.getMaterialTypeName());
	parametersMap.put("id", searchCriteria.getId());
	parametersMap.put("pageSize", searchCriteria.getPageSize());
	parametersMap.put("pageNumber", searchCriteria.getPageNumber());

	return parametersMap;
}
	
}
