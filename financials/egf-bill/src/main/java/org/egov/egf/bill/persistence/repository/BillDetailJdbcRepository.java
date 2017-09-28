package org.egov.egf.bill.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BillDetailJdbcRepository extends JdbcRepository {
    private static final Logger LOG = LoggerFactory.getLogger(BillDetailJdbcRepository.class);
    static {
	LOG.debug("init billDetail");
	init(BillDetailEntity.class);
	LOG.debug("end init billDetail");
    }

	public BillDetailJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.jdbcTemplate = jdbcTemplate;
	}

	public BillDetailEntity create(BillDetailEntity entity) {
		super.create(entity);
		return entity;
	}

	public BillDetailEntity update(BillDetailEntity entity) {
		super.update(entity);
		return entity;
	}

	public BillDetailEntity delete(final BillDetailEntity entity) {
		super.delete(entity.TABLE_NAME, entity.getId());
		return entity;
	}
	
	public boolean delete(BillDetailEntity entity, String reason) {
		super.delete(entity, reason);
		return true;
	}

   /* public Pagination<BillDetail> search(BillDetailSearch domain) {
	BillDetailSearchEntity billDetailSearchEntity = new BillDetailSearchEntity();
	billDetailSearchEntity.toEntity(domain);
	String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";
	Map<String, Object> paramValues = new HashMap<>();
	StringBuffer params = new StringBuffer();
	if (billDetailSearchEntity.getSortBy() != null && !billDetailSearchEntity.getSortBy().isEmpty()) {
	    validateSortByOrder(billDetailSearchEntity.getSortBy());
	    validateEntityFieldName(billDetailSearchEntity.getSortBy(), BillDetailEntity.class);
	}
	String orderBy = "order by name";
	if (billDetailSearchEntity.getSortBy() != null && !billDetailSearchEntity.getSortBy().isEmpty()) {
	    orderBy = "order by " + billDetailSearchEntity.getSortBy();
	}
	searchQuery = searchQuery.replace(":tablename", BillDetailEntity.TABLE_NAME);
	searchQuery = searchQuery.replace(":selectfields", " * ");
	// implement jdbc specfic search
	if (billDetailSearchEntity.getId() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("id =:id");
	    paramValues.put("id", billDetailSearchEntity.getId());
	}
	if (billDetailSearchEntity.getOrderId() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("orderId =:orderId");
	    paramValues.put("orderId", billDetailSearchEntity.getOrderId());
	}
	if (billDetailSearchEntity.getChartOfAccountId() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("chartOfAccount =:chartOfAccount");
	    paramValues.put("chartOfAccount", billDetailSearchEntity.getChartOfAccountId());
	}
	if (billDetailSearchEntity.getGlcode() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("glcode =:glcode");
	    paramValues.put("glcode", billDetailSearchEntity.getGlcode());
	}
	if (billDetailSearchEntity.getDebitAmount() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("debitAmount =:debitAmount");
	    paramValues.put("debitAmount", billDetailSearchEntity.getDebitAmount());
	}
	if (billDetailSearchEntity.getCreditAmount() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("creditAmount =:creditAmount");
	    paramValues.put("creditAmount", billDetailSearchEntity.getCreditAmount());
	}
	if (billDetailSearchEntity.getFunctionId() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("function =:function");
	    paramValues.put("function", billDetailSearchEntity.getFunctionId());
	}
	if (billDetailSearchEntity.getBillPayeeDetails() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("billPayeeDetails =:billPayeeDetails");
	    paramValues.put("billPayeeDetails", billDetailSearchEntity.getBillPayeeDetails());
	}
	if (billDetailSearchEntity.getIds() != null) {
	    if (params.length() > 0)
		params.append(" and ");
	    params.append("ids =:ids");
	    paramValues.put("ids", billDetailSearchEntity.getIds());
	}
	if (billDetailSearchEntity.getTenantId() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("tenantId =:tenantId");
	    paramValues.put("tenantId", billDetailSearchEntity.getTenantId());
	}
	if (billDetailSearchEntity.getId() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("id =:id");
	    paramValues.put("id", billDetailSearchEntity.getId());
	}
	if (billDetailSearchEntity.getCode() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("code =:code");
	    paramValues.put("code", billDetailSearchEntity.getCode());
	}
	if (billDetailSearchEntity.getName() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("name =:name");
	    paramValues.put("name", billDetailSearchEntity.getName());
	}
	if (billDetailSearchEntity.getIdentifier() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("description =:description");
	    paramValues.put("description", billDetailSearchEntity.getIdentifier());
	}
	if (billDetailSearchEntity.getActive() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("active =:active");
	    paramValues.put("active", billDetailSearchEntity.getActive());
	}
	if (billDetailSearchEntity.getLevel() != null) {
	    if (params.length() > 0) {
		params.append(" and ");
	    }
	    params.append("type =:type");
	    paramValues.put("type", billDetailSearchEntity.getLevel());
	}
	Pagination<BillDetail> page = new Pagination<>();
	if (billDetailSearchEntity.getOffset() != null) {
	    page.setOffset(billDetailSearchEntity.getOffset());
	}
	if (billDetailSearchEntity.getPageSize() != null) {
	    page.setPageSize(billDetailSearchEntity.getPageSize());
	}
	if (params.length() > 0) {
	    searchQuery = searchQuery.replace(":condition", " where " + params.toString());
	} else
	    searchQuery = searchQuery.replace(":condition", "");
	searchQuery = searchQuery.replace(":orderby", orderBy);
	page = (Pagination<BillDetail>) getPagination(searchQuery, page, paramValues);
	searchQuery = searchQuery + " :pagination";
	searchQuery = searchQuery.replace(":pagination",
		"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
	BeanPropertyRowMapper row = new BeanPropertyRowMapper(BillDetailEntity.class);
	List<BillDetailEntity> billDetailEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
		paramValues, row);
	page.setTotalResults(billDetailEntities.size());
	List<BillDetail> billdetails = new ArrayList<>();
	for (BillDetailEntity billDetailEntity : billDetailEntities) {
	    billdetails.add(billDetailEntity.toDomain());
	}
	page.setPagedData(billdetails);
	return page;
    }
*/
	public BillDetailEntity findById(BillDetailEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass()
				.getSimpleName());
		Map<String, Object> paramValues = new HashMap<>();
		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}
		List<BillDetailEntity> billdetails = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(),
				paramValues, new BeanPropertyRowMapper(BillDetailEntity.class));
		if (billdetails.isEmpty()) {
			return null;
		} else {
			return billdetails.get(0);
		}
	}
}