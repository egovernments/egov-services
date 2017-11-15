package org.egov.inv.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.inv.persistence.entity.IndentDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class IndentDetailJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(IndentDetailJdbcRepository.class);

	static {
		LOG.debug("init indentDetail");
		init(IndentDetailEntity.class);
		LOG.debug("end init indentDetail");
	}

	public IndentDetailJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public IndentDetailEntity create(IndentDetailEntity entity) {
		super.create(entity);
		return entity;
	}

	public IndentDetailEntity update(IndentDetailEntity entity) {
		super.update(entity);
		return entity;

	}

	public boolean delete(IndentDetailEntity entity, String reason) {
		super.delete(entity, reason);
		return true;

	}

	/*public Pagination<IndentDetail> search(IndentDetailSearch domain) {
		IndentDetailSearchEntity indentDetailSearchEntity = new IndentDetailSearchEntity();
		indentDetailSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (indentDetailSearchEntity.getSortBy() != null && !indentDetailSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(indentDetailSearchEntity.getSortBy());
			validateEntityFieldName(indentDetailSearchEntity.getSortBy(), IndentDetailEntity.class);
		}

		String orderBy = "order by name";
		if (indentDetailSearchEntity.getSortBy() != null && !indentDetailSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + indentDetailSearchEntity.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", IndentDetailEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (indentDetailSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", indentDetailSearchEntity.getId());
		}
		if (indentDetailSearchEntity.getTenantId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", indentDetailSearchEntity.getTenantId());
		}
		if (indentDetailSearchEntity.getMaterialId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("material =:material");
			paramValues.put("material", indentDetailSearchEntity.getMaterialId());
		}
		if (indentDetailSearchEntity.getUomId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("uom =:uom");
			paramValues.put("uom", indentDetailSearchEntity.getUomId());
		}
		if (indentDetailSearchEntity.getParentIndentLine() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("parentIndentLine =:parentIndentLine");
			paramValues.put("parentIndentLine", indentDetailSearchEntity.getParentIndentLine());
		}
		if (indentDetailSearchEntity.getOrderNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("orderNumber =:orderNumber");
			paramValues.put("orderNumber", indentDetailSearchEntity.getOrderNumber());
		}
		if (indentDetailSearchEntity.getProjectCodeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("projectCode =:projectCode");
			paramValues.put("projectCode", indentDetailSearchEntity.getProjectCodeId());
		}
		if (indentDetailSearchEntity.getAssetId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("asset =:asset");
			paramValues.put("asset", indentDetailSearchEntity.getAssetId());
		}
		if (indentDetailSearchEntity.getIndentQuantity() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentQuantity =:indentQuantity");
			paramValues.put("indentQuantity", indentDetailSearchEntity.getIndentQuantity());
		}
		if (indentDetailSearchEntity.getTotalProcessedQuantity() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("totalProcessedQuantity =:totalProcessedQuantity");
			paramValues.put("totalProcessedQuantity", indentDetailSearchEntity.getTotalProcessedQuantity());
		}
		if (indentDetailSearchEntity.getIndentIssuedQuantity() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("indentIssuedQuantity =:indentIssuedQuantity");
			paramValues.put("indentIssuedQuantity", indentDetailSearchEntity.getIndentIssuedQuantity());
		}
		if (indentDetailSearchEntity.getPoOrderedQuantity() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("poOrderedQuantity =:poOrderedQuantity");
			paramValues.put("poOrderedQuantity", indentDetailSearchEntity.getPoOrderedQuantity());
		}
		if (indentDetailSearchEntity.getInterstoreRequestQuantity() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("interstoreRequestQuantity =:interstoreRequestQuantity");
			paramValues.put("interstoreRequestQuantity", indentDetailSearchEntity.getInterstoreRequestQuantity());
		}
		if (indentDetailSearchEntity.getDeliveryTerms() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("deliveryTerms =:deliveryTerms");
			paramValues.put("deliveryTerms", indentDetailSearchEntity.getDeliveryTerms());
		}
		if (indentDetailSearchEntity.getRemarks() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("remarks =:remarks");
			paramValues.put("remarks", indentDetailSearchEntity.getRemarks());
		}
		if (indentDetailSearchEntity.getIds() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("ids =:ids");
			paramValues.put("ids", indentDetailSearchEntity.getIds());
		}

		if (indentDetailSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", indentDetailSearchEntity.getTenantId());
		}
		if (indentDetailSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", indentDetailSearchEntity.getId());
		}
		if (indentDetailSearchEntity.getCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code =:code");
			paramValues.put("code", indentDetailSearchEntity.getCode());
		}
		if (indentDetailSearchEntity.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name =:name");
			paramValues.put("name", indentDetailSearchEntity.getName());
		}
		if (indentDetailSearchEntity.getIdentifier() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("description =:description");
			paramValues.put("description", indentDetailSearchEntity.getIdentifier());
		}
		if (indentDetailSearchEntity.getActive() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("active =:active");
			paramValues.put("active", indentDetailSearchEntity.getActive());
		}
		if (indentDetailSearchEntity.getLevel() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("type =:type");
			paramValues.put("type", indentDetailSearchEntity.getLevel());
		}

		Pagination<IndentDetail> page = new Pagination<>();
		if (indentDetailSearchEntity.getOffset() != null) {
			page.setOffset(indentDetailSearchEntity.getOffset());
		}
		if (indentDetailSearchEntity.getPageSize() != null) {
			page.setPageSize(indentDetailSearchEntity.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<IndentDetail>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(IndentDetailEntity.class);

		List<IndentDetailEntity> indentDetailEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);

		page.setTotalResults(indentDetailEntities.size());

		List<IndentDetail> indentdetails = new ArrayList<>();
		for (IndentDetailEntity indentDetailEntity : indentDetailEntities) {

			indentdetails.add(indentDetailEntity.toDomain());
		}
		page.setPagedData(indentdetails);

		return page;
	}*/

	public IndentDetailEntity findById(IndentDetailEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<IndentDetailEntity> indentdetails = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(IndentDetailEntity.class));
		if (indentdetails.isEmpty()) {
			return null;
		} else {
			return indentdetails.get(0);
		}

	}

}