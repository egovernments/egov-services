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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VendorContractJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_vendorcontract";

	public Pagination<VendorContract> search(VendorContractSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), VendorContractSearch.class);
		}

		String orderBy = "order by contractNo";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getContractNos() != null) {
			addAnd(params);
			params.append("contractNo in(:contractNos) ");
			paramValues.put("contractNos",
					new ArrayList<String>(Arrays.asList(searchRequest.getContractNos().split(","))));
		}

		if (searchRequest.getVendorNo() != null) {
			addAnd(params);
			params.append("vendor =:vendor");
			paramValues.put("vendor", searchRequest.getVendorNo());
		}

		if (searchRequest.getContractNo() != null) {
			addAnd(params);
			params.append("contractNo =:contractNo");
			paramValues.put("contractNo", searchRequest.getContractNo());
		}

		if (searchRequest.getContractDate() != null) {
			addAnd(params);
			params.append("contractDate =:contractDate");
			paramValues.put("contractDate", searchRequest.getContractDate());
		}

		if (searchRequest.getContractPeriodFrom() != null) {
			addAnd(params);
			params.append("contractPeriodFrom =:contractPeriodFrom");
			paramValues.put("contractPeriodFrom", searchRequest.getContractPeriodFrom());
		}

		if (searchRequest.getContractPeriodTo() != null) {
			addAnd(params);
			params.append("contractPeriodTo =:contractPeriodTo");
			paramValues.put("contractPeriodTo", searchRequest.getContractPeriodTo());
		}

		if (searchRequest.getSecurityDeposit() != null) {
			addAnd(params);
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

		for (VendorContractEntity entity : vendorContractEntities) {

			vendorContractList.add(entity.toDomain());
		}

		page.setTotalResults(vendorContractList.size());

		page.setPagedData(vendorContractList);

		return page;
	}

}