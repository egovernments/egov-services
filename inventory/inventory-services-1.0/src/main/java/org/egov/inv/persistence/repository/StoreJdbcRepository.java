package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.Store;
import org.egov.inv.persistence.entity.StoreEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StoreJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(StoreJdbcRepository.class);

	static {
		LOG.debug("init store");
		init(StoreEntity.class);
		LOG.debug("end init store");
	}

	public StoreJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public StoreEntity create(StoreEntity entity) {
		super.create(entity);
		return entity;
	}

	public StoreEntity update(StoreEntity entity) {
		super.update(entity);
		return entity;

	}

	public boolean delete(StoreEntity entity, String reason) {
		super.delete(entity, reason);
		return true;

	}

/*	public Pagination<Store> search(StoreSearch domain) {
		StoreSearchEntity storeSearchEntity = new StoreSearchEntity();
		storeSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (storeSearchEntity.getSortBy() != null && !storeSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(storeSearchEntity.getSortBy());
			validateEntityFieldName(storeSearchEntity.getSortBy(), StoreEntity.class);
		}

		String orderBy = "order by name";
		if (storeSearchEntity.getSortBy() != null && !storeSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + storeSearchEntity.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", StoreEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (storeSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", storeSearchEntity.getId());
		}
		if (storeSearchEntity.getTenantId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", storeSearchEntity.getTenantId());
		}
		if (storeSearchEntity.getCode() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("code =:code");
			paramValues.put("code", storeSearchEntity.getCode());
		}
		if (storeSearchEntity.getName() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("name =:name");
			paramValues.put("name", storeSearchEntity.getName());
		}
		if (storeSearchEntity.getDescription() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("description =:description");
			paramValues.put("description", storeSearchEntity.getDescription());
		}
		if (storeSearchEntity.getDepartmentId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("department =:department");
			paramValues.put("department", storeSearchEntity.getDepartmentId());
		}
		if (storeSearchEntity.getOfficeLocationId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("officeLocation =:officeLocation");
			paramValues.put("officeLocation", storeSearchEntity.getOfficeLocationId());
		}
		if (storeSearchEntity.getBillingAddress() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("billingAddress =:billingAddress");
			paramValues.put("billingAddress", storeSearchEntity.getBillingAddress());
		}
		if (storeSearchEntity.getDeliveryAddress() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("deliveryAddress =:deliveryAddress");
			paramValues.put("deliveryAddress", storeSearchEntity.getDeliveryAddress());
		}
		if (storeSearchEntity.getContactNo1() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("contactNo1 =:contactNo1");
			paramValues.put("contactNo1", storeSearchEntity.getContactNo1());
		}
		if (storeSearchEntity.getContactNo2() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("contactNo2 =:contactNo2");
			paramValues.put("contactNo2", storeSearchEntity.getContactNo2());
		}
		if (storeSearchEntity.getEmail() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("email =:email");
			paramValues.put("email", storeSearchEntity.getEmail());
		}
		if (storeSearchEntity.getStoreInChargeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("storeInCharge =:storeInCharge");
			paramValues.put("storeInCharge", storeSearchEntity.getStoreInChargeId());
		}
		if (storeSearchEntity.getIsCentralStore() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("isCentralStore =:isCentralStore");
			paramValues.put("isCentralStore", storeSearchEntity.getIsCentralStore());
		}
		if (storeSearchEntity.getActive() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("active =:active");
			paramValues.put("active", storeSearchEntity.getActive());
		}
		if (storeSearchEntity.getAuditDetailsId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("auditDetails =:auditDetails");
			paramValues.put("auditDetails", storeSearchEntity.getAuditDetailsId());
		}
		if (storeSearchEntity.getIds() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("ids =:ids");
			paramValues.put("ids", storeSearchEntity.getIds());
		}

		if (storeSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", storeSearchEntity.getTenantId());
		}
		if (storeSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", storeSearchEntity.getId());
		}
		if (storeSearchEntity.getCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code =:code");
			paramValues.put("code", storeSearchEntity.getCode());
		}
		if (storeSearchEntity.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name =:name");
			paramValues.put("name", storeSearchEntity.getName());
		}
		if (storeSearchEntity.getIdentifier() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("description =:description");
			paramValues.put("description", storeSearchEntity.getIdentifier());
		}
		if (storeSearchEntity.getActive() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("active =:active");
			paramValues.put("active", storeSearchEntity.getActive());
		}
		if (storeSearchEntity.getLevel() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("type =:type");
			paramValues.put("type", storeSearchEntity.getLevel());
		}

		Pagination<Store> page = new Pagination<>();
		if (storeSearchEntity.getOffset() != null) {
			page.setOffset(storeSearchEntity.getOffset());
		}
		if (storeSearchEntity.getPageSize() != null) {
			page.setPageSize(storeSearchEntity.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<Store>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(StoreEntity.class);

		List<StoreEntity> storeEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(storeEntities.size());

		List<Store> stores = new ArrayList<>();
		for (StoreEntity storeEntity : storeEntities) {

			stores.add(storeEntity.toDomain());
		}
		page.setPagedData(stores);

		return page;
	}*/

	public StoreEntity findById(StoreEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<StoreEntity> stores = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(StoreEntity.class));
		if (stores.isEmpty()) {
			return null;
		} else {
			return stores.get(0);
		}

	}

}