package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.persistence.entity.VendorContractEntity;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendorContractJdbcRepository {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Pagination<VendorContract> search(VendorContractSearch searchRequest) {

		String searchQuery = "select * from egswm_vendorcontract :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), VendorContractSearch.class);
		}

		String orderBy = "order by code";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getContractNos() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("contractNo in(:contractNos) ");
			paramValues.put("contractNos",
					new ArrayList<String>(Arrays.asList(searchRequest.getContractNos().split(","))));
		}

		if (searchRequest.getVendorNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vendor =:vendor");
			paramValues.put("vendor", searchRequest.getVendorNo());
		}

		if (searchRequest.getContractNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("contractNo =:contractNo");
			paramValues.put("contractNo", searchRequest.getContractNo());
		}

		if (searchRequest.getContractDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("contractDate =:contractDate");
			paramValues.put("contractDate", searchRequest.getContractDate());
		}

		if (searchRequest.getContractPeriodFrom() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("contractPeriodFrom =:contractPeriodFrom");
			paramValues.put("contractPeriodFrom", searchRequest.getContractPeriodFrom());
		}

		if (searchRequest.getContractPeriodTo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("contractPeriodTo =:contractPeriodTo");
			paramValues.put("contractPeriodTo", searchRequest.getContractPeriodTo());
		}

		if (searchRequest.getSecurityDeposit() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("securityDeposit =:securityDeposit");
			paramValues.put("securityDeposit", searchRequest.getSecurityDeposit());
		}

		Pagination<VendorContract> page = new Pagination<>();
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

		page = (Pagination<VendorContract>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VendorContractEntity.class);

		List<VendorContract> vendorContractList = new ArrayList<>();

		List<VendorContractEntity> vendorContractEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);

		page.setTotalResults(vendorContractList.size());

		page.setPagedData(vendorContractList);

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