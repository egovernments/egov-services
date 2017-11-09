package org.egov.swm.persistence.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.persistence.entity.RefillingPumpStationEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RefillingPumpStationJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_refillingpumpstation";

	public RefillingPumpStationJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public Boolean checkForUniqueRecords(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {
		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public Pagination<RefillingPumpStation> search(RefillingPumpStationSearch refillingPumpStationSearch){

		if (refillingPumpStationSearch.getSortBy() != null && !refillingPumpStationSearch.getSortBy().isEmpty()) {
			validateSortByOrder(refillingPumpStationSearch.getSortBy());
			validateEntityFieldName(refillingPumpStationSearch.getSortBy(), RefillingPumpStationSearch.class);
		}

		return buildSearchQuery(refillingPumpStationSearch);
	}

	private Pagination<RefillingPumpStation> buildSearchQuery(RefillingPumpStationSearch searchRequest){

		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder query = new StringBuilder();

		query.append("SELECT * FROM egswm_refillingpumpstation ");

		if(searchRequest.getTenantId() != null && !searchRequest.getTenantId().isEmpty()){
			addWhereClause(query, "tenantid", "tenantid");
			paramsMap.put("tenantid", searchRequest.getTenantId());
		}

		if(searchRequest.getCode() != null && !searchRequest.getCode().isEmpty()){
			addWhereClauseWithAnd(query, "code", "code");
			paramsMap.put("code", searchRequest.getCode());
		}

		if(searchRequest.getName() != null && !searchRequest.getName().isEmpty()){
			addWhereClauseWithAnd(query,"name","name");
			paramsMap.put("name", searchRequest.getName());
		}

		if(searchRequest.getQuantity() != null){
			addWhereClauseWithAnd(query,"quantity", "quantity");
			paramsMap.put("quantity", searchRequest.getQuantity());
		}

		if(searchRequest.getLocationCode() != null && !searchRequest.getLocationCode().isEmpty()){
			addWhereClauseWithAnd(query,"location", "location");
			paramsMap.put("location", searchRequest.getLocationCode());
		}

		if(searchRequest.getTypeOfFuelCode() != null && !searchRequest.getTypeOfFuelCode().isEmpty()){
			addWhereClauseWithAnd(query,"typeoffuel", "typeoffuel");
			paramsMap.put("typeoffuel", searchRequest.getTypeOfFuelCode());
		}

		if(searchRequest.getTypeOfPumpCode() != null && !searchRequest.getTypeOfPumpCode().isEmpty()){
			addWhereClauseWithAnd(query,"typeofpump", "typeofpump");
			paramsMap.put("typeofpump", searchRequest.getTypeOfPumpCode());
		}

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			query.append("order by ").append(searchRequest.getSortBy());
		}

		Pagination<RefillingPumpStation> page = new Pagination<>();
		if (searchRequest.getOffset() != null) {
			page.setOffset(searchRequest.getOffset());
		}
		if (searchRequest.getPageSize() != null) {
			page.setPageSize(searchRequest.getPageSize());
		}

		page = (Pagination<RefillingPumpStation>) getPagination(query.toString(), page, paramsMap);

		query.append(" limit ").append(page.getPageSize()).append(" offset ").append(page.getOffset() * page.getPageSize());

		List<RefillingPumpStationEntity> refillingPumpStationEntityList = namedParameterJdbcTemplate
				.query(query.toString(), paramsMap, new BeanPropertyRowMapper(RefillingPumpStationEntity.class));

		List<RefillingPumpStation> refillingPumpStationList = refillingPumpStationEntityList.stream()
				.map(RefillingPumpStationEntity::toDomain)
				.collect(Collectors.toList());

		page.setTotalResults(refillingPumpStationList.size());

		page.setPagedData(refillingPumpStationList);

		return page;
	}

	private StringBuilder addWhereClause(StringBuilder query, String fieldName, String paramName){
		return query.append(" WHERE ").append(fieldName).append("= :").append(paramName);
	}

	private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
		return query.append(" AND ").append(fieldName).append("= :").append(paramName);
	}
}
