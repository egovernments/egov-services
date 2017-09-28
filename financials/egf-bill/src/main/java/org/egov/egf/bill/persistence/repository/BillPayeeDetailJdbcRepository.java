package org.egov.egf.bill.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.bill.persistence.entity.BillPayeeDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BillPayeeDetailJdbcRepository extends JdbcRepository {
   
	private static final Logger LOG = LoggerFactory.getLogger(BillPayeeDetailJdbcRepository.class);
	
	static {
		LOG.debug("init billPayeeDetail");
		init(BillPayeeDetailEntity.class);
		LOG.debug("end init billPayeeDetail");
	}

	public BillPayeeDetailJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.jdbcTemplate = jdbcTemplate;
	}

	public BillPayeeDetailEntity create(BillPayeeDetailEntity entity) {
		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public BillPayeeDetailEntity update(BillPayeeDetailEntity entity) {
		super.update(entity);
		return entity;
	}
	
	public BillPayeeDetailEntity delete(final BillPayeeDetailEntity entity) {
		super.delete(entity.TABLE_NAME, entity.getId());
		return entity;
	}

	public boolean delete(BillPayeeDetailEntity entity, String reason) {
		super.delete(entity, reason);
		return true;
	}

  /*  public Pagination<BillPayeeDetail> search(BillPayeeDetailSearch domain) {
	BillPayeeDetailSearchEntity billPayeeDetailSearchEntity = new BillPayeeDetailSearchEntity();
	billPayeeDetailSearchEntity.toEntity(domain);
	String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";
	Map<String, Object> paramValues = new HashMap<>();
	StringBuffer params = new StringBuffer();
	if (billPayeeDetailSearchEntity.getSortBy() != null && !billPayeeDetailSearchEntity.getSortBy().isEmpty()) {
	    validateSortByOrder(billPayeeDetailSearchEntity.getSortBy());
	    validateEntityFieldName(billPayeeDetailSearchEntity.getSortBy(), BillPayeeDetailEntity.class);
	}
	String orderBy = "order by name";
	if (billPayeeDetailSearchEntity.getSortBy() != null && !billPayeeDetailSearchEntity.getSortBy().isEmpty()) {
	    orderBy = "order by " + billPayeeDetailSearchEntity.getSortBy();
	}
	searchQuery = searchQuery.replace(":tablename", BillPayeeDetailEntity.TABLE_NAME);
	searchQuery = searchQuery.replace(":selectfields", " * ");
	// implement jdbc specfic search
	if (billPayeeDetailSearchEntity.getId() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("id =:id");
	    paramValues.put("id", billPayeeDetailSearchEntity.getId());
	}
	if (billPayeeDetailSearchEntity.getAccountDetailTypeId() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("accountDetailType =:accountDetailType");
	    paramValues.put("accountDetailType", billPayeeDetailSearchEntity.getAccountDetailTypeId());
	}
	if (billPayeeDetailSearchEntity.getAccountDetailKeyId() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("accountDetailKey =:accountDetailKey");
	    paramValues.put("accountDetailKey", billPayeeDetailSearchEntity.getAccountDetailKeyId());
	}
	if (billPayeeDetailSearchEntity.getAmount() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("amount =:amount");
	    paramValues.put("amount", billPayeeDetailSearchEntity.getAmount());
	}
	if (billPayeeDetailSearchEntity.getIds() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("ids =:ids");
	    paramValues.put("ids", billPayeeDetailSearchEntity.getIds());
	}
	if (billPayeeDetailSearchEntity.getTenantId() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("tenantId =:tenantId");
	    paramValues.put("tenantId", billPayeeDetailSearchEntity.getTenantId());
	}
	if (billPayeeDetailSearchEntity.getId() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("id =:id");
	    paramValues.put("id", billPayeeDetailSearchEntity.getId());
	}
	if (billPayeeDetailSearchEntity.getCode() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("code =:code");
	    paramValues.put("code", billPayeeDetailSearchEntity.getCode());
	}
	if (billPayeeDetailSearchEntity.getName() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("name =:name");
	    paramValues.put("name", billPayeeDetailSearchEntity.getName());
	}
	if (billPayeeDetailSearchEntity.getIdentifier() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("description =:description");
	    paramValues.put("description", billPayeeDetailSearchEntity.getIdentifier());
	}
	if (billPayeeDetailSearchEntity.getActive() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("active =:active");
	    paramValues.put("active", billPayeeDetailSearchEntity.getActive());
	}
	if (billPayeeDetailSearchEntity.getLevel() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("type =:type");
	    paramValues.put("type", billPayeeDetailSearchEntity.getLevel());
	}
	Pagination<BillPayeeDetail> page = new Pagination<>();
	if (billPayeeDetailSearchEntity.getOffset() != null) {
	    page.setOffset(billPayeeDetailSearchEntity.getOffset());
	}
	if (billPayeeDetailSearchEntity.getPageSize() != null) {
	    page.setPageSize(billPayeeDetailSearchEntity.getPageSize());
	}
	if (params.length() > 0) {
	    searchQuery = searchQuery.replace(":condition", " where " + params.toString());
	} else
	    searchQuery = searchQuery.replace(":condition", "");
	searchQuery = searchQuery.replace(":orderby", orderBy);
	page = (Pagination<BillPayeeDetail>) getPagination(searchQuery, page, paramValues);
	searchQuery = searchQuery + " :pagination";
	searchQuery = searchQuery.replace(":pagination",
		"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
	BeanPropertyRowMapper row = new BeanPropertyRowMapper(BillPayeeDetailEntity.class);
	List<BillPayeeDetailEntity> billPayeeDetailEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
		paramValues, row);
	page.setTotalResults(billPayeeDetailEntities.size());
	List<BillPayeeDetail> billpayeedetails = new ArrayList<>();
	for (BillPayeeDetailEntity billPayeeDetailEntity : billPayeeDetailEntities) {
	    billpayeedetails.add(billPayeeDetailEntity.toDomain());
	}
	page.setPagedData(billpayeedetails);
	return page;
    }*/

	public BillPayeeDetailEntity findById(BillPayeeDetailEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());
		
		Map<String, Object> paramValues = new HashMap<>();
		
		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}
		
		List<BillPayeeDetailEntity> billpayeedetails = namedParameterJdbcTemplate
				.query(getByIdQuery.get(entity.getClass().getSimpleName())
						.toString(), paramValues, new BeanPropertyRowMapper(
						BillPayeeDetailEntity.class));
		
		if (billpayeedetails.isEmpty()) {
		
			return null;
		
		} else {
		
			return billpayeedetails.get(0);
		
		}
	}
}