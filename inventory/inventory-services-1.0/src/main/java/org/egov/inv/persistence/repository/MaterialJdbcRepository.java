package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.Material;
import org.egov.inv.persistence.entity.MaterialEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MaterialJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(MaterialJdbcRepository.class);

	static {
		LOG.debug("init material");
		init(MaterialEntity.class);
		LOG.debug("end init material");
	}

	public MaterialJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public MaterialEntity create(MaterialEntity entity) {
		super.create(entity);
		return entity;
	}

	public MaterialEntity update(MaterialEntity entity) {
		super.update(entity);
		return entity;

	}

	public boolean delete(MaterialEntity entity, String reason) {
		super.delete(entity, reason);
		return true;

	}

	/*public Pagination<Material> search(MaterialSearch domain) {
		MaterialSearchEntity materialSearchEntity = new MaterialSearchEntity();
		materialSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (materialSearchEntity.getSortBy() != null && !materialSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(materialSearchEntity.getSortBy());
			validateEntityFieldName(materialSearchEntity.getSortBy(), MaterialEntity.class);
		}

		String orderBy = "order by name";
		if (materialSearchEntity.getSortBy() != null && !materialSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + materialSearchEntity.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", MaterialEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (materialSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", materialSearchEntity.getId());
		}
		if (materialSearchEntity.getTenantId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", materialSearchEntity.getTenantId());
		}
		if (materialSearchEntity.getCode() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("code =:code");
			paramValues.put("code", materialSearchEntity.getCode());
		}
		if (materialSearchEntity.getName() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("name =:name");
			paramValues.put("name", materialSearchEntity.getName());
		}
		if (materialSearchEntity.getDescription() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("description =:description");
			paramValues.put("description", materialSearchEntity.getDescription());
		}
		if (materialSearchEntity.getOldCode() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("oldCode =:oldCode");
			paramValues.put("oldCode", materialSearchEntity.getOldCode());
		}
		if (materialSearchEntity.getMaterialTypeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("materialType =:materialType");
			paramValues.put("materialType", materialSearchEntity.getMaterialTypeId());
		}
		if (materialSearchEntity.getBaseUomId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("baseUom =:baseUom");
			paramValues.put("baseUom", materialSearchEntity.getBaseUomId());
		}
		if (materialSearchEntity.getInventoryTypeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("inventoryType =:inventoryType");
			paramValues.put("inventoryType", materialSearchEntity.getInventoryTypeId());
		}
		if (materialSearchEntity.getStatusId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("status =:status");
			paramValues.put("status", materialSearchEntity.getStatusId());
		}
		if (materialSearchEntity.getPurchaseUomId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("purchaseUom =:purchaseUom");
			paramValues.put("purchaseUom", materialSearchEntity.getPurchaseUomId());
		}
		if (materialSearchEntity.getExpenseAccountId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("expenseAccount =:expenseAccount");
			paramValues.put("expenseAccount", materialSearchEntity.getExpenseAccountId());
		}
		if (materialSearchEntity.getMinQuantity() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("minQuantity =:minQuantity");
			paramValues.put("minQuantity", materialSearchEntity.getMinQuantity());
		}
		if (materialSearchEntity.getMaxQuantity() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("maxQuantity =:maxQuantity");
			paramValues.put("maxQuantity", materialSearchEntity.getMaxQuantity());
		}
		if (materialSearchEntity.getStockingUomId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("stockingUom =:stockingUom");
			paramValues.put("stockingUom", materialSearchEntity.getStockingUomId());
		}
		if (materialSearchEntity.getMaterialClassId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("materialClass =:materialClass");
			paramValues.put("materialClass", materialSearchEntity.getMaterialClassId());
		}
		if (materialSearchEntity.getReorderLevel() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("reorderLevel =:reorderLevel");
			paramValues.put("reorderLevel", materialSearchEntity.getReorderLevel());
		}
		if (materialSearchEntity.getReorderQuantity() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("reorderQuantity =:reorderQuantity");
			paramValues.put("reorderQuantity", materialSearchEntity.getReorderQuantity());
		}
		if (materialSearchEntity.getMaterialControlTypeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("materialControlType =:materialControlType");
			paramValues.put("materialControlType", materialSearchEntity.getMaterialControlTypeId());
		}
		if (materialSearchEntity.getModel() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("model =:model");
			paramValues.put("model", materialSearchEntity.getModel());
		}
		if (materialSearchEntity.getManufacturePartNo() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("manufacturePartNo =:manufacturePartNo");
			paramValues.put("manufacturePartNo", materialSearchEntity.getManufacturePartNo());
		}
		if (materialSearchEntity.getTechincalSpecs() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("techincalSpecs =:techincalSpecs");
			paramValues.put("techincalSpecs", materialSearchEntity.getTechincalSpecs());
		}
		if (materialSearchEntity.getTermsOfDelivery() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("termsOfDelivery =:termsOfDelivery");
			paramValues.put("termsOfDelivery", materialSearchEntity.getTermsOfDelivery());
		}
		if (materialSearchEntity.getOverrideMaterialControlType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("overrideMaterialControlType =:overrideMaterialControlType");
			paramValues.put("overrideMaterialControlType", materialSearchEntity.getOverrideMaterialControlType());
		}
		if (materialSearchEntity.getScrapable() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("scrapable =:scrapable");
			paramValues.put("scrapable", materialSearchEntity.getScrapable());
		}
		if (materialSearchEntity.getAuditDetailsId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("auditDetails =:auditDetails");
			paramValues.put("auditDetails", materialSearchEntity.getAuditDetailsId());
		}
		if (materialSearchEntity.getIds() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("ids =:ids");
			paramValues.put("ids", materialSearchEntity.getIds());
		}

		if (materialSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", materialSearchEntity.getTenantId());
		}
		if (materialSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", materialSearchEntity.getId());
		}
		if (materialSearchEntity.getCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code =:code");
			paramValues.put("code", materialSearchEntity.getCode());
		}
		if (materialSearchEntity.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name =:name");
			paramValues.put("name", materialSearchEntity.getName());
		}
		if (materialSearchEntity.getIdentifier() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("description =:description");
			paramValues.put("description", materialSearchEntity.getIdentifier());
		}
		if (materialSearchEntity.getActive() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("active =:active");
			paramValues.put("active", materialSearchEntity.getActive());
		}
		if (materialSearchEntity.getLevel() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("type =:type");
			paramValues.put("type", materialSearchEntity.getLevel());
		}

		Pagination<Material> page = new Pagination<>();
		if (materialSearchEntity.getOffset() != null) {
			page.setOffset(materialSearchEntity.getOffset());
		}
		if (materialSearchEntity.getPageSize() != null) {
			page.setPageSize(materialSearchEntity.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<Material>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialEntity.class);

		List<MaterialEntity> materialEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
				row);

		page.setTotalResults(materialEntities.size());

		List<Material> materials = new ArrayList<>();
		for (MaterialEntity materialEntity : materialEntities) {

			materials.add(materialEntity.toDomain());
		}
		page.setPagedData(materials);

		return page;
	}*/

	public MaterialEntity findById(MaterialEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<MaterialEntity> materials = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(MaterialEntity.class));
		if (materials.isEmpty()) {
			return null;
		} else {
			return materials.get(0);
		}

	}

}