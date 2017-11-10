package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.inv.domain.model.ReceiptNotesSearchCriteria;
import org.egov.inv.persistence.entity.ReceiptNoteApiEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import io.swagger.model.MaterialReceipt;
import io.swagger.model.Pagination;

@Service
public class RecieptNoteApiJdbcRepository extends JdbcRepository{
	private static final Logger LOG = LoggerFactory.getLogger(RecieptNoteApiJdbcRepository.class);
	static {
		LOG.debug("init MaterialReceipt");
		init(ReceiptNoteApiEntity.class);
		LOG.debug("end init MaterialReceipt");
	}
	
	public Pagination<MaterialReceipt> search(ReceiptNotesSearchCriteria receiptRequest) {
		
		String searchQuery = "select * from MaterialReceipt :condition :orderby";
		StringBuffer params = new StringBuffer();
		Map<String, Object> paramValues = new HashMap<>();
		if (receiptRequest.getSortBy() != null && !receiptRequest.getSortBy().isEmpty()) {
			validateSortByOrder(receiptRequest.getSortBy());
			validateEntityFieldName(receiptRequest.getSortBy(), ReceiptNotesSearchCriteria.class);
		}
		String orderBy = "order by id";
		if (receiptRequest.getSortBy() != null && !receiptRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + receiptRequest.getSortBy();
		}
		if (receiptRequest.getTenantId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("tenantId = :tenantId");
			paramValues.put("tenantId", receiptRequest.getTenantId());
		}
		if (receiptRequest.getMrnNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("mrnNumber = :mrnNumber");
			paramValues.put("mrnNumber", receiptRequest.getMrnNumber());
		}
		if (receiptRequest.getReceiptType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("receiptType = :receiptType");
			paramValues.put("receiptType", receiptRequest.getReceiptType());
		}
		if (receiptRequest.getMrnStatus() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("mrnStatus = :mrnStatus");
			paramValues.put("mrnStatus", receiptRequest.getMrnStatus());
		}
		if (receiptRequest.getReceivingStore() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("receivingStore = :receivingStore");
			paramValues.put("receivingStore", receiptRequest.getReceivingStore());
		}

		if (receiptRequest.getSupplierCode() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("supplierCode = :supplierCode");
			paramValues.put("supplierCode", receiptRequest.getSupplierCode());
		}
		if (receiptRequest.getReceiptDateFrom() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("receiptDateFrom = :receiptDateFrom");
			paramValues.put("receiptDateFrom", receiptRequest.getReceiptDateFrom());
		}

		if (receiptRequest.getReceiptDateT0() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("receiptDateT0 = :receiptDateT0");
			paramValues.put("receiptDateT0", receiptRequest.getReceiptDateT0());
		}
		Pagination<MaterialReceipt> page = new Pagination<>();
		if (receiptRequest.getPageSize() != null)
			page.setPageSize(receiptRequest.getPageSize());
		if (receiptRequest.getOffset() != null)
			page.setOffset(receiptRequest.getOffset());
		if (params.length() > 0)
			searchQuery = searchQuery.replace(":condition", " where " + params.toString());
		else
			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);
		page = (Pagination<MaterialReceipt>) getPagination(searchQuery, page, paramValues);

		searchQuery = searchQuery + " :pagination";
		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(ReceiptNoteApiEntity.class);

		List<MaterialReceipt> materialList = new ArrayList<>();

		List<ReceiptNoteApiEntity> receiptEntity = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
				row);
		for (ReceiptNoteApiEntity receiptEntitys : receiptEntity) {

			materialList.add(receiptEntitys.toDomain());
		}

		page.setTotalResults(materialList.size());

		page.setPagedData(materialList);

		return page;
	}
	
	

}
