package org.egov.egf.bill.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.persistence.entity.BillChecklistEntity;
import org.egov.egf.bill.persistence.entity.BillChecklistSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BillChecklistJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory
			.getLogger(BillChecklistJdbcRepository.class);
	static {
		LOG.debug("init billChecklist");
		init(BillChecklistEntity.class);
		LOG.debug("end init billChecklist");
	}

	public BillChecklistJdbcRepository(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public BillChecklistEntity create(BillChecklistEntity entity) {
		super.create(entity);
		return entity;
	}

	public BillChecklistEntity update(BillChecklistEntity entity) {
		super.update(entity);
		return entity;
	}

	public boolean delete(BillChecklistEntity entity, String reason) {
		super.delete(entity, reason);
		return true;
	}

	public Pagination<BillChecklist> search(BillChecklistSearch domain) {
		BillChecklistSearchEntity billChecklistSearchEntity = new BillChecklistSearchEntity();
		billChecklistSearchEntity.toEntity(domain);
		
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";
		
		Map<String, Object> paramValues = new HashMap<>();
		
		StringBuffer params = new StringBuffer();
		
		if (billChecklistSearchEntity.getSortBy() != null
				&& !billChecklistSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(billChecklistSearchEntity.getSortBy());
			validateEntityFieldName(billChecklistSearchEntity.getSortBy(),
					BillChecklistEntity.class);
		}
		
		String orderBy = "order by type";
		
		if (billChecklistSearchEntity.getSortBy() != null
				&& !billChecklistSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + billChecklistSearchEntity.getSortBy();
		}
		
		searchQuery = searchQuery.replace(":tablename", BillChecklistEntity.TABLE_NAME);
		searchQuery = searchQuery.replace(":selectfields", " * ");
		
		// implement jdbc specfic search
		if (billChecklistSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", billChecklistSearchEntity.getId());
		}
		
		if (billChecklistSearchEntity.getBillId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("billid =:bill");
			paramValues.put("bill", billChecklistSearchEntity.getBillId());
		}
		
		if (billChecklistSearchEntity.getChecklistId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("checklistid =:checklist");
			paramValues.put("checklist", billChecklistSearchEntity.getChecklistId());
		}
		
		if (billChecklistSearchEntity.getChecklistValue() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("checklistValue =:checklistValue");
			paramValues.put("checklistValue", billChecklistSearchEntity.getChecklistValue());
		}
		
        if (billChecklistSearchEntity.getIds() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("id in (:ids)");
            paramValues.put("ids", new ArrayList<String>(Arrays.asList(billChecklistSearchEntity.getIds().split(","))));
        }
		
		if (billChecklistSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", billChecklistSearchEntity.getTenantId());
		}
		
		if (billChecklistSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", billChecklistSearchEntity.getId());
		}

		Pagination<BillChecklist> page = new Pagination<>();
		
		if (billChecklistSearchEntity.getOffset() != null) {
			page.setOffset(billChecklistSearchEntity.getOffset());
		}
		
		if (billChecklistSearchEntity.getPageSize() != null) {
			page.setPageSize(billChecklistSearchEntity.getPageSize());
		}
		
		if (params.length() > 0) {
			searchQuery = searchQuery.replace(":condition",
					" where " + params.toString());
		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}
		
		searchQuery = searchQuery.replace(":orderby", orderBy);
		
		page = (Pagination<BillChecklist>) getPagination(searchQuery, page, paramValues);
		
		searchQuery = searchQuery + " :pagination";
		searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
		
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BillChecklistEntity.class);
		
		List<BillChecklistEntity> billChecklistEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
		
		page.setTotalResults(billChecklistEntities.size());
		
		List<BillChecklist> billChecklists = new ArrayList<>();
		
		for (BillChecklistEntity billChecklistEntity : billChecklistEntities) {
			billChecklists.add(billChecklistEntity.toDomain());
		}
		
		page.setPagedData(billChecklists);
		
		return page;
	}

	public BillChecklistEntity findById(BillChecklistEntity entity) {
		
		List<String> list = allIdentitiferFields.get(entity.getClass()
				.getSimpleName());
		
		Map<String, Object> paramValues = new HashMap<>();
		
		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}
		
		List<BillChecklistEntity> billChecklists = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(),
				paramValues, new BeanPropertyRowMapper(BillChecklistEntity.class));
		
		if (billChecklists.isEmpty()) {
		
			return null;
		
		} else {
		
			return billChecklists.get(0);
		
		}
	}
}