package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.Pagination;
import org.egov.inv.model.Indent;
import org.egov.inv.model.Indent.IndentStatusEnum;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.persistence.entity.IndentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class IndentJdbcRepository extends org.egov.common.JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(IndentJdbcRepository.class);

	@Autowired
	private IndentDetailJdbcRepository indentDetailJdbcRepository;
	static {
		LOG.debug("init indent");
		init(IndentEntity.class);
		LOG.debug("end init indent");
	}

	public static synchronized void init(Class T) {
		String TABLE_NAME = "";

		List<String> insertFields = new ArrayList<>();
		List<String> updateFields = new ArrayList<>();
		List<String> uniqueFields = new ArrayList<>();

		String insertQuery = "";
		String updateQuery = "";
		String searchQuery = "";

		try {

			TABLE_NAME = (String) T.getDeclaredField("TABLE_NAME").get(null);
		} catch (Exception e) {

		}
		insertFields.addAll(fetchFields(T));
		uniqueFields.add("indentNumber");
		uniqueFields.add("tenantId");
		insertFields.removeAll(uniqueFields);
		allInsertQuery.put(T.getSimpleName(), insertQuery(insertFields, TABLE_NAME, uniqueFields));
		updateFields.addAll(insertFields);
		updateFields.remove("createdBy");
		updateQuery = updateQuery(updateFields, TABLE_NAME, uniqueFields);
		System.out.println(T.getSimpleName() + "--------" + insertFields);
		allInsertFields.put(T.getSimpleName(), insertFields);
		allUpdateFields.put(T.getSimpleName(), updateFields);
		allIdentitiferFields.put(T.getSimpleName(), uniqueFields);
		// allInsertQuery.put(T.getSimpleName(), insertQuery);
		allUpdateQuery.put(T.getSimpleName(), updateQuery);
		getByIdQuery.put(T.getSimpleName(), getByIdQuery(TABLE_NAME, uniqueFields));
		System.out.println(allInsertQuery);
	}

	public IndentJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public IndentEntity create(IndentEntity entity) {
		super.create(entity);
		return entity;
	}

	public IndentEntity update(IndentEntity entity) {
		super.update(entity);
		return entity;

	}

	public boolean delete(IndentEntity entity, String reason) {
		super.delete(entity, reason);
		return true;

	}

	 
	public IndentEntity findById(IndentEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<IndentEntity> indents = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(IndentEntity.class));
		if (indents.isEmpty()) {
			return null;
		} else {
			return indents.get(0);
		}

	}
	 

	public Pagination<Indent> search(IndentSearch indentSearch) {
	 
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (indentSearch.getSortBy() != null && !indentSearch.getSortBy().isEmpty()) {
			validateSortByOrder(indentSearch.getSortBy());
			validateEntityFieldName(indentSearch.getSortBy(), IndentEntity.class);
		}

		String orderBy = "order by indent.indentNumber";
		if (indentSearch.getSortBy() != null && !indentSearch.getSortBy().isEmpty()) {
			orderBy = "order by " + indentSearch.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", "indent indent,Store issueStore,indentdetail details" );

		searchQuery = searchQuery.replace(":selectfields", " distinct indent.*,issueStore.code as \"issueStore.code\" ,issueStore.name as \"issueStore.name\"  ");

		String conditions=" and issuestore.code=indent.issuestore and details.indentnumber=indent.indentnumber";
		// implement jdbc specfic search
	 
		if (indentSearch.getTenantId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indent.tenantId =:tenantId");
			paramValues.put("tenantId", indentSearch.getTenantId());
		}
		

		if (indentSearch.getIds() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indent.indentNumber in (:ids)");
			paramValues.put("ids", indentSearch.getIds());
		}
		
		if (indentSearch.getIssueStore () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("issueStore =:issueStore");
			paramValues.put("issueStore", indentSearch.getIssueStore ());
		}
		 
		if (indentSearch.getIndentDate() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentDate =:indentDate");
			paramValues.put("indentDate", indentSearch.getIndentDate());
		}
		if (indentSearch.getIndentNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indent.indentNumber =:indentNumber");
			paramValues.put("indentNumber", indentSearch.getIndentNumber());
		}
		if (indentSearch.getIndentType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentType =:indentType");
			paramValues.put("indentType", indentSearch.getIndentType ());
		}
		if (indentSearch.getIndentPurpose () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentPurpose =:indentPurpose");
			paramValues.put("indentPurpose", indentSearch.getIndentPurpose ());
		}
		if (indentSearch.getInventoryType () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("inventoryType =:inventoryType");
			paramValues.put("inventoryType", indentSearch.getInventoryType ());
		}
		 
	 
		if (indentSearch.getIndentStatus () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentStatus =:indentStatus");
			paramValues.put("indentStatus", indentSearch.getIndentStatus ());
		}
		 
		if (indentSearch.getDepartmentId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("department =:department");
			paramValues.put("department", indentSearch.getDepartmentId());
		}
		if (indentSearch.getTotalIndentValue() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("totalIndentValue =:totalIndentValue");
			paramValues.put("totalIndentValue", indentSearch.getTotalIndentValue());
		}
		 
		if (indentSearch.getIndentRaisedBy() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentCreatedBy =:indentCreatedBy");
			paramValues.put("indentCreatedBy", indentSearch.getIndentRaisedBy());
		}
		 
		if (indentSearch.getStateId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("stateId =:stateId");
			paramValues.put("stateId", indentSearch.getStateId());
		}
		
		//TODO : Handle the status for these
		if(indentSearch.getSearchPurpose()!=null  && indentSearch.getSearchPurpose().equalsIgnoreCase("PurchaseOrder"))
		{
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentStatus =:indentStatus");
			paramValues.put("indentStatus", IndentStatusEnum.APPROVED.name());
			if (params.length() > 0)
				params.append(" and  ");
			params.append(" (details.totalProcessedQuantity is null or details.indentQuantity - details.totalProcessedQuantity > :value)");
			paramValues.put("value", Integer.valueOf(0));
		} 
		
		if(indentSearch.getSearchPurpose()!=null  && indentSearch.getSearchPurpose().equalsIgnoreCase("IssueMaterial"))
		{
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentStatus !=:indentStatus");
			paramValues.put("indentStatus", IndentStatusEnum.ISSUED.name());
		
			
		}
		Pagination<Indent> page = new Pagination<>();
		if (indentSearch.getPageNumber() != null) {
			page.setOffset(indentSearch.getPageNumber()-1);
		}
		if (indentSearch.getPageSize() != null) {
			page.setPageSize(indentSearch.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where indent.isdeleted is not true and " + params.toString() +" "+ conditions);

		} else

			searchQuery = searchQuery.replace(":condition", "  "+conditions);

		searchQuery = searchQuery.replace(":orderby", orderBy);//orderBy
		System.out.println(searchQuery);
		page = (Pagination<Indent>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		//BeanPropertyRowMapper row = new BeanPropertyRowMapper(Indent.class);
		IndentRowMapper row=new IndentRowMapper();
		List<Indent> indents = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(indents.size());

		List<String> indentNumbers=new ArrayList<>();
	 
		//List<IndentDetailEntity> find = indentDetailJdbcRepository.find(indentNumbers,indentSearch.getTenantId());
	   
		 
		page.setPagedData(indents);

		return page;
	   }
	
}