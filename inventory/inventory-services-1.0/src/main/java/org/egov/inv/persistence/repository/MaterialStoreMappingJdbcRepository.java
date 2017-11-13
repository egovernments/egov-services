package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialStoreMapping;
import org.egov.inv.persistence.entity.MaterialStoreMappingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MaterialStoreMappingJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(MaterialStoreMappingJdbcRepository.class);

	static {
		LOG.debug("init materialStoreMapping");
		init(MaterialStoreMappingEntity.class);
		LOG.debug("end init materialStoreMapping");
	}

	public MaterialStoreMappingJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public MaterialStoreMappingEntity create(MaterialStoreMappingEntity entity) {
		super.create(entity);
		return entity;
	}

	public MaterialStoreMappingEntity update(MaterialStoreMappingEntity entity) {
		super.update(entity);
		return entity;

	}

	public boolean delete(MaterialStoreMappingEntity entity, String reason) {
		super.delete(entity, reason);
		return true;

	}

	/*public Pagination<MaterialStoreMapping> search(MaterialStoreMappingSearch domain) {
		MaterialStoreMappingSearchEntity materialStoreMappingSearchEntity = new MaterialStoreMappingSearchEntity();
		materialStoreMappingSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (materialStoreMappingSearchEntity.getSortBy() != null
				&& !materialStoreMappingSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(materialStoreMappingSearchEntity.getSortBy());
			validateEntityFieldName(materialStoreMappingSearchEntity.getSortBy(), MaterialStoreMappingEntity.class);
		}

		String orderBy = "order by name";
		if (materialStoreMappingSearchEntity.getSortBy() != null
				&& !materialStoreMappingSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + materialStoreMappingSearchEntity.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", MaterialStoreMappingEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (materialStoreMappingSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", materialStoreMappingSearchEntity.getId());
		}
		if (materialStoreMappingSearchEntity.getMaterialId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("material =:material");
			paramValues.put("material", materialStoreMappingSearchEntity.getMaterialId());
		}
		if (materialStoreMappingSearchEntity.getStoreId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("store =:store");
			paramValues.put("store", materialStoreMappingSearchEntity.getStoreId());
		}
		if (materialStoreMappingSearchEntity.getChartofAccountId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("chartofAccount =:chartofAccount");
			paramValues.put("chartofAccount", materialStoreMappingSearchEntity.getChartofAccountId());
		}
		if (materialStoreMappingSearchEntity.getActive() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("active =:active");
			paramValues.put("active", materialStoreMappingSearchEntity.getActive());
		}
		if (materialStoreMappingSearchEntity.getAuditDetailsId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("auditDetails =:auditDetails");
			paramValues.put("auditDetails", materialStoreMappingSearchEntity.getAuditDetailsId());
		}
		if (materialStoreMappingSearchEntity.getIds() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("ids =:ids");
			paramValues.put("ids", materialStoreMappingSearchEntity.getIds());
		}

		if (materialStoreMappingSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", materialStoreMappingSearchEntity.getTenantId());
		}
		if (materialStoreMappingSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", materialStoreMappingSearchEntity.getId());
		}
		if (materialStoreMappingSearchEntity.getCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code =:code");
			paramValues.put("code", materialStoreMappingSearchEntity.getCode());
		}
		if (materialStoreMappingSearchEntity.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name =:name");
			paramValues.put("name", materialStoreMappingSearchEntity.getName());
		}
		if (materialStoreMappingSearchEntity.getIdentifier() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("description =:description");
			paramValues.put("description", materialStoreMappingSearchEntity.getIdentifier());
		}
		if (materialStoreMappingSearchEntity.getActive() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("active =:active");
			paramValues.put("active", materialStoreMappingSearchEntity.getActive());
		}
		if (materialStoreMappingSearchEntity.getLevel() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("type =:type");
			paramValues.put("type", materialStoreMappingSearchEntity.getLevel());
		}

		Pagination<MaterialStoreMapping> page = new Pagination<>();
		if (materialStoreMappingSearchEntity.getOffset() != null) {
			page.setOffset(materialStoreMappingSearchEntity.getOffset());
		}
		if (materialStoreMappingSearchEntity.getPageSize() != null) {
			page.setPageSize(materialStoreMappingSearchEntity.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<MaterialStoreMapping>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialStoreMappingEntity.class);

		List<MaterialStoreMappingEntity> materialStoreMappingEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(materialStoreMappingEntities.size());

		List<MaterialStoreMapping> materialstoremappings = new ArrayList<>();
		for (MaterialStoreMappingEntity materialStoreMappingEntity : materialStoreMappingEntities) {

			materialstoremappings.add(materialStoreMappingEntity.toDomain());
		}
		page.setPagedData(materialstoremappings);

		return page;
	}*/

	public MaterialStoreMappingEntity findById(MaterialStoreMappingEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<MaterialStoreMappingEntity> materialstoremappings = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(MaterialStoreMappingEntity.class));
		if (materialstoremappings.isEmpty()) {
			return null;
		} else {
			return materialstoremappings.get(0);
		}

	}

}