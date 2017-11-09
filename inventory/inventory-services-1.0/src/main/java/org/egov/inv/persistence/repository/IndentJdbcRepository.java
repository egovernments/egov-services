package org.egov.inv.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.inv.persistence.entity.IndentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class IndentJdbcRepository extends org.egov.common.JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(IndentJdbcRepository.class);

	static {
		LOG.debug("init indent");
		init(IndentEntity.class);
		LOG.debug("end init indent");
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

	/*public Pagination<Indent> search(IndentSearch domain) {
		IndentEntity indentSearchEntity = new IndentEntity();
	//	indentSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (indentSearchEntity.getSortBy() != null && !indentSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(indentSearchEntity.getSortBy());
			validateEntityFieldName(indentSearchEntity.getSortBy(), IndentEntity.class);
		}

		String orderBy = "order by name";
		if (indentSearchEntity.getSortBy() != null && !indentSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + indentSearchEntity.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", IndentEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (indentSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", indentSearchEntity.getId());
		}
		if (indentSearchEntity.getTenantId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", indentSearchEntity.getTenantId());
		}
		if (indentSearchEntity.getIssueStore () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("issueStore =:issueStore");
			paramValues.put("issueStore", indentSearchEntity.getIssueStore ());
		}
		if (indentSearchEntity.getIndentStore () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentStore =:indentStore");
			paramValues.put("indentStore", indentSearchEntity.getIndentStore ());
		}
		if (indentSearchEntity.getIndentDate() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentDate =:indentDate");
			paramValues.put("indentDate", indentSearchEntity.getIndentDate());
		}
		if (indentSearchEntity.getIndentNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentNumber =:indentNumber");
			paramValues.put("indentNumber", indentSearchEntity.getIndentNumber());
		}
		if (indentSearchEntity.getIndentType () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentType =:indentType");
			paramValues.put("indentType", indentSearchEntity.getIndentType ());
		}
		if (indentSearchEntity.getIndentPurpose () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentPurpose =:indentPurpose");
			paramValues.put("indentPurpose", indentSearchEntity.getIndentPurpose ());
		}
		if (indentSearchEntity.getInventoryType () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("inventoryType =:inventoryType");
			paramValues.put("inventoryType", indentSearchEntity.getInventoryType ());
		}
		if (indentSearchEntity.getExpectedDeliveryDate() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("expectedDeliveryDate =:expectedDeliveryDate");
			paramValues.put("expectedDeliveryDate", indentSearchEntity.getExpectedDeliveryDate());
		}
		if (indentSearchEntity.getMaterialHandOverTo() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("materialHandOverTo =:materialHandOverTo");
			paramValues.put("materialHandOverTo", indentSearchEntity.getMaterialHandOverTo());
		}
		if (indentSearchEntity.getNarration() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("narration =:narration");
			paramValues.put("narration", indentSearchEntity.getNarration());
		}
		if (indentSearchEntity.getIndentStatus () != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentStatus =:indentStatus");
			paramValues.put("indentStatus", indentSearchEntity.getIndentStatus ());
		}
		 
		if (indentSearchEntity.getDepartmentId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("department =:department");
			paramValues.put("department", indentSearchEntity.getDepartmentId());
		}
		if (indentSearchEntity.getTotalIndentValue() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("totalIndentValue =:totalIndentValue");
			paramValues.put("totalIndentValue", indentSearchEntity.getTotalIndentValue());
		}
		if (indentSearchEntity.getFileStoreId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("fileStoreId =:fileStoreId");
			paramValues.put("fileStoreId", indentSearchEntity.getFileStoreId());
		}
		if (indentSearchEntity.getIndentCreatedBy() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentCreatedBy =:indentCreatedBy");
			paramValues.put("indentCreatedBy", indentSearchEntity.getIndentCreatedBy());
		}
		if (indentSearchEntity.getDesignation() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("designation =:designation");
			paramValues.put("designation", indentSearchEntity.getDesignation());
		}
		if (indentSearchEntity.getStateId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("stateId =:stateId");
			paramValues.put("stateId", indentSearchEntity.getStateId());
		}

		if (indentSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", indentSearchEntity.getTenantId());
		}
		if (indentSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", indentSearchEntity.getId());
		}
	 
		Pagination<Indent> page = new Pagination<>();
		if (indentSearchEntity.getOffset() != null) {
			page.setOffset(indentSearchEntity.getOffset());
		}
		if (indentSearchEntity.getPageSize() != null) {
			page.setPageSize(indentSearchEntity.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		//searchQuery = searchQuery.replace(":orderby", "");//orderBy

		page = (Pagination<Indent>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(IndentEntity.class);

		List<IndentEntity> indentEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(indentEntities.size());

		List<Indent> indents = new ArrayList<>();
		for (IndentEntity indentEntity : indentEntities) {

			indents.add(indentEntity.toDomain());
		}
		page.setPagedData(indents);

		return page;
	}*/

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

}