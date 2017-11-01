package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.BinDetailsSearch;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointDetailsSearch;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.persistence.entity.CollectionPointEntity;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CollectionPointJdbcRepository {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public BinDetailsJdbcRepository binIdDetailsJdbcRepository;

	@Autowired
	public CollectionPointDetailsJdbcRepository collectionPointDetailsJdbcRepository;

	public Pagination<CollectionPoint> search(CollectionPointSearch searchRequest) {

		String searchQuery = "select * from egswm_collectionpoint :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), CollectionPointSearch.class);
		}

		String orderBy = "order by name";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getCodes() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code in (:codes)");
			paramValues.put("codes", new ArrayList<String>(Arrays.asList(searchRequest.getCodes().split(","))));
		}
		if (searchRequest.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code =:code");
			paramValues.put("code", searchRequest.getCode());
		}

		if (searchRequest.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name =:name");
			paramValues.put("name", searchRequest.getName());
		}

		if (searchRequest.getLocation() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("location =:location");
			paramValues.put("location", searchRequest.getLocation());
		}

		Pagination<CollectionPoint> page = new Pagination<>();
		if (searchRequest.getOffset() != null) {
			page.setOffset(searchRequest.getOffset());
		}
		if (searchRequest.getPageSize() != null) {
			page.setPageSize(searchRequest.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<CollectionPoint>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(CollectionPointEntity.class);

		List<CollectionPoint> collectionPointList = new ArrayList<>();

		List<CollectionPointEntity> collectionPointEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);
		CollectionPoint cp;
		BinDetailsSearch bds;
		CollectionPointDetailsSearch cpds;
		for (CollectionPointEntity collectionPointEntity : collectionPointEntities) {

			cp = collectionPointEntity.toDomain();

			bds = new BinDetailsSearch();
			bds.setCollectionPoint(cp.getCode());
			bds.setTenantId(cp.getTenantId());
			cp.setBinDetails(binIdDetailsJdbcRepository.search(bds));
			collectionPointList.add(cp);

			cpds = new CollectionPointDetailsSearch();
			cpds.setTenantId(cp.getTenantId());
			cpds.setCollectionPoint(cp.getCode());

			cp.setCollectionPointDetails(collectionPointDetailsJdbcRepository.search(cpds));
		}

		page.setTotalResults(collectionPointList.size());

		page.setPagedData(collectionPointList);

		return page;
	}

	public Pagination<?> getPagination(String searchQuery, Pagination<?> page, Map<String, Object> paramValues) {
		String countQuery = "select count(*) from (" + searchQuery + ") as x";
		Long count = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), paramValues, Long.class);
		Integer totalpages = (int) Math.ceil((double) count / page.getPageSize());
		page.setTotalPages(totalpages);
		page.setCurrentPage(page.getOffset());
		return page;
	}

	public void validateSortByOrder(final String sortBy) {
		List<String> sortByList = new ArrayList<String>();
		if (sortBy.contains(",")) {
			sortByList = Arrays.asList(sortBy.split(","));
		} else {
			sortByList = Arrays.asList(sortBy);
		}
		for (String s : sortByList) {
			if (s.contains(" ")
					&& (!s.toLowerCase().trim().endsWith("asc") && !s.toLowerCase().trim().endsWith("desc"))) {

				throw new CustomException(s.split(" ")[0],
						"Please send the proper sortBy order for the field " + s.split(" ")[0]);
			}
		}

	}

	public void validateEntityFieldName(String sortBy, final Class<?> object) {
		List<String> sortByList = new ArrayList<String>();
		if (sortBy.contains(",")) {
			sortByList = Arrays.asList(sortBy.split(","));
		} else {
			sortByList = Arrays.asList(sortBy);
		}
		Boolean isFieldExist = Boolean.FALSE;
		for (String s : sortByList) {
			for (int i = 0; i < object.getDeclaredFields().length; i++) {
				if (object.getDeclaredFields()[i].getName().equals(s.contains(" ") ? s.split(" ")[0] : s)) {
					isFieldExist = Boolean.TRUE;
					break;
				} else {
					isFieldExist = Boolean.FALSE;
				}
			}
			if (!isFieldExist) {
				throw new CustomException(s.contains(" ") ? s.split(" ")[0] : s, "Please send the proper Field Names ");

			}
		}

	}

}