package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.inv.domain.model.OpeningBalanceSearchCriteria;
import org.egov.inv.domain.model.ReceiptNotesSearchCriteria;
import org.egov.inv.persistence.entity.OpeningBalanceEntity;
import org.egov.inv.persistence.entity.ReceiptNoteApiEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import io.swagger.model.MaterialReceipt;
import io.swagger.model.MaterialReceipt;
import io.swagger.model.Pagination;

@Service
public class OpeningBalanceRepository extends JdbcRepository{
	private static final Logger LOG = LoggerFactory.getLogger(RecieptNoteApiJdbcRepository.class);
	static {
		LOG.debug("init supplier");
		init(ReceiptNoteApiEntity.class);
		LOG.debug("end init supplier");
	}
	
	public Pagination<MaterialReceipt> search(OpeningBalanceSearchCriteria openBalanceSearch) {
		
		String searchQuery = "select * from MaterialReceiptHeader :condition :orderby";
		StringBuffer params = new StringBuffer();
		Map<String, Object> paramValues = new HashMap<>();
		if (openBalanceSearch.getSortBy() != null && !openBalanceSearch.getSortBy().isEmpty()) {
			validateSortByOrder(openBalanceSearch.getSortBy());
			validateEntityFieldName(openBalanceSearch.getSortBy(), OpeningBalanceSearchCriteria.class);
		}
		String orderBy = "order by id";
		if (openBalanceSearch.getSortBy() != null && !openBalanceSearch.getSortBy().isEmpty()) {
			orderBy = "order by " + openBalanceSearch.getSortBy();
		}
		if (openBalanceSearch.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id in (:id)");
			paramValues.put("id", openBalanceSearch.getId());
		}
		
		if (openBalanceSearch.getTenantId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("tenantId = :tenantId");
			paramValues.put("tenantId", openBalanceSearch.getTenantId());
		}
		if (openBalanceSearch.getMrnNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("mrnNumber = :mrnNumber");
			paramValues.put("mrnNumber", openBalanceSearch.getMrnNumber());
		}
		if (openBalanceSearch.getReceiptNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("receiptNumber = :receiptNumber");
			paramValues.put("receiptNumber", openBalanceSearch.getReceiptNumber());
		}
		if (openBalanceSearch.getFinancialYear() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("finanncilaYear = :finanncilaYear");
			paramValues.put("finanncilaYear", openBalanceSearch.getFinancialYear());
		}
		if (openBalanceSearch.getMaterialName() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("materialName = :materialName");
			paramValues.put("materialName", openBalanceSearch.getMaterialName());
		}
		if (openBalanceSearch.getSupplierCode() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("supplierCode = :supplierCode");
			paramValues.put("supplierCode", openBalanceSearch.getSupplierCode());
		}
	
		Pagination<MaterialReceipt> page = new Pagination<>();
		if (openBalanceSearch.getPageSize() != null)
			page.setPageSize(openBalanceSearch.getPageSize());
		if (openBalanceSearch.getOffset() != null)
			page.setOffset(openBalanceSearch.getOffset());
		if (params.length() > 0)
			searchQuery = searchQuery.replace(":condition", " where " + params.toString());
		else
			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);
		page = (Pagination<MaterialReceipt>) getPagination(searchQuery, page, paramValues);

		searchQuery = searchQuery + " :pagination";
		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(OpeningBalanceEntity.class);

		List<MaterialReceipt> materialList = new ArrayList<>();

		List<OpeningBalanceEntity> openBalEntity = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
				row);
		for (OpeningBalanceEntity openBalEntitys : openBalEntity) {

			materialList.add(openBalEntitys.toDomain());
		}

		page.setTotalResults(materialList.size());

		page.setPagedData(materialList);

		return page;
	}
	

}
