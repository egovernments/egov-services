package org.egov.egf.bill.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.persistence.entity.ChecklistEntity;
import org.egov.egf.bill.persistence.entity.ChecklistSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChecklistJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory
			.getLogger(ChecklistJdbcRepository.class);
	static {
		LOG.debug("init checklist");
		init(ChecklistEntity.class);
		LOG.debug("end init checklist");
	}

	public ChecklistJdbcRepository(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.jdbcTemplate = jdbcTemplate;
	}

	public ChecklistEntity create(ChecklistEntity entity) {
		super.create(entity);
		return entity;
	}

	public ChecklistEntity update(ChecklistEntity entity) {
		super.update(entity);
		return entity;
	}

	public ChecklistEntity delete(final ChecklistEntity entity) {
		super.delete(entity.TABLE_NAME, entity.getId());
		return entity;
	}
	
	public boolean delete(ChecklistEntity entity, String reason) {
		super.delete(entity, reason);
		return true;
	}

	public Pagination<Checklist> search(ChecklistSearch domain) {
		ChecklistSearchEntity checklistSearchEntity = new ChecklistSearchEntity();
		checklistSearchEntity.toEntity(domain);
		
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";
		
		Map<String, Object> paramValues = new HashMap<>();
		
		StringBuffer params = new StringBuffer();
		
		if (checklistSearchEntity.getSortBy() != null
				&& !checklistSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(checklistSearchEntity.getSortBy());
			validateEntityFieldName(checklistSearchEntity.getSortBy(),
					ChecklistEntity.class);
		}
		
		String orderBy = "order by type";
		
		if (checklistSearchEntity.getSortBy() != null
				&& !checklistSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + checklistSearchEntity.getSortBy();
		}
		
		searchQuery = searchQuery.replace(":tablename", ChecklistEntity.TABLE_NAME);
		searchQuery = searchQuery.replace(":selectfields", " * ");
		
		// implement jdbc specfic search
		if (checklistSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", checklistSearchEntity.getId());
		}
		
		if (checklistSearchEntity.getType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("type =:type");
			paramValues.put("type", checklistSearchEntity.getType());
		}
		
		if (checklistSearchEntity.getSubType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("subType =:subType");
			paramValues.put("subType", checklistSearchEntity.getSubType());
		}
		
		if (checklistSearchEntity.getKey() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("key =:key");
			paramValues.put("key", checklistSearchEntity.getKey());
		}
		
		if (checklistSearchEntity.getDescription() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("description =:description");
			paramValues.put("description",
					checklistSearchEntity.getDescription());
		}
		
        if (checklistSearchEntity.getIds() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("id in (:ids)");
            paramValues.put("ids", new ArrayList<String>(Arrays.asList(checklistSearchEntity.getIds().split(","))));
        }
		
		if (checklistSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", checklistSearchEntity.getTenantId());
		}
		
		if (checklistSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", checklistSearchEntity.getId());
		}

		Pagination<Checklist> page = new Pagination<>();
		
		if (checklistSearchEntity.getOffset() != null) {
			page.setOffset(checklistSearchEntity.getOffset());
		}
		
		if (checklistSearchEntity.getPageSize() != null) {
			page.setPageSize(checklistSearchEntity.getPageSize());
		}
		
		if (params.length() > 0) {
			searchQuery = searchQuery.replace(":condition",
					" where " + params.toString());
		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}
		
		searchQuery = searchQuery.replace(":orderby", orderBy);
		
		page = (Pagination<Checklist>) getPagination(searchQuery, page, paramValues);
		
		searchQuery = searchQuery + " :pagination";
		searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
		
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(ChecklistEntity.class);
		
		List<ChecklistEntity> checklistEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
		
		page.setTotalResults(checklistEntities.size());
		
		List<Checklist> checklists = new ArrayList<>();
		
		for (ChecklistEntity checklistEntity : checklistEntities) {
			checklists.add(checklistEntity.toDomain());
		}
		
		page.setPagedData(checklists);
		
		return page;
	}

	public ChecklistEntity findById(ChecklistEntity entity) {
		
		List<String> list = allIdentitiferFields.get(entity.getClass()
				.getSimpleName());
		
		Map<String, Object> paramValues = new HashMap<>();
		
		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}
		
		List<ChecklistEntity> checklists = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(),
				paramValues, new BeanPropertyRowMapper(ChecklistEntity.class));
		
		if (checklists.isEmpty()) {
		
			return null;
		
		} else {
		
			return checklists.get(0);
		
		}
	}
}