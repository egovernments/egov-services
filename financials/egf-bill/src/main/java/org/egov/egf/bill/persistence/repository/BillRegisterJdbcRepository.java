package org.egov.egf.bill.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.egov.egf.bill.persistence.entity.BillRegisterSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BillRegisterJdbcRepository extends JdbcRepository {
    private static final Logger LOG = LoggerFactory.getLogger(BillRegisterJdbcRepository.class);
	
    static {
		LOG.debug("init billRegister");
		init(BillRegisterEntity.class);
		LOG.debug("end init billRegister");
	}

	public BillRegisterJdbcRepository(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public BillRegisterEntity create(BillRegisterEntity entity) {
		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public BillRegisterEntity update(BillRegisterEntity entity) {
		super.update(entity);
		return entity;
	}

	public boolean delete(BillRegisterEntity entity, String reason) {
		super.delete(entity, reason);
		return true;
	}

	public Pagination<BillRegister> search(BillRegisterSearch domain) {
		
		BillRegisterSearchEntity billRegisterSearchEntity = new BillRegisterSearchEntity();
		billRegisterSearchEntity.toEntity(domain);
		
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";
		
		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		
		if (billRegisterSearchEntity.getSortBy() != null
				&& !billRegisterSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(billRegisterSearchEntity.getSortBy());
			validateEntityFieldName(billRegisterSearchEntity.getSortBy(),
					BillRegisterEntity.class);
		}
		
		String orderBy = "order by billType";
		
		if (billRegisterSearchEntity.getSortBy() != null
				&& !billRegisterSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + billRegisterSearchEntity.getSortBy();
		}
		
		searchQuery = searchQuery.replace(":tablename",
				BillRegisterEntity.TABLE_NAME);
		searchQuery = searchQuery.replace(":selectfields", " * ");
		
		// implement jdbc specfic search
		if (billRegisterSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", billRegisterSearchEntity.getId());
		}
		
		if (billRegisterSearchEntity.getBillType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("billType =:billType");
			paramValues.put("billType", billRegisterSearchEntity.getBillType());
		}
		
		if (billRegisterSearchEntity.getBillSubType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("billSubType =:billSubType");
			paramValues.put("billSubType",
					billRegisterSearchEntity.getBillSubType());
		}
		
		if (billRegisterSearchEntity.getBillNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("billNumber =:billNumber");
			paramValues.put("billNumber",
					billRegisterSearchEntity.getBillNumber());
		}
		
		if (billRegisterSearchEntity.getBillDate() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("billDate =:billDate");
			paramValues.put("billDate", billRegisterSearchEntity.getBillDate());
		}
		
		if (billRegisterSearchEntity.getBillAmount() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("billAmount =:billAmount");
			paramValues.put("billAmount",
					billRegisterSearchEntity.getBillAmount());
		}
		
		if (billRegisterSearchEntity.getPassedAmount() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("passedAmount =:passedAmount");
			paramValues.put("passedAmount",
					billRegisterSearchEntity.getPassedAmount());
		}
		
		if (billRegisterSearchEntity.getModuleName() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("moduleName =:moduleName");
			paramValues.put("moduleName",
					billRegisterSearchEntity.getModuleName());
		}
		
		if (billRegisterSearchEntity.getStatusId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("status =:status");
			paramValues.put("status", billRegisterSearchEntity.getStatusId());
		}
		
		if (billRegisterSearchEntity.getFundId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("fund =:fund");
			paramValues.put("fund", billRegisterSearchEntity.getFundId());
		}
		
		if (billRegisterSearchEntity.getFunctionId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("function =:function");
			paramValues.put("function",
					billRegisterSearchEntity.getFunctionId());
		}
		
		if (billRegisterSearchEntity.getFundsourceId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("fundsource =:fundsource");
			paramValues.put("fundsource",
					billRegisterSearchEntity.getFundsourceId());
		}
		
		if (billRegisterSearchEntity.getSchemeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("scheme =:scheme");
			paramValues.put("scheme", billRegisterSearchEntity.getSchemeId());
		}
		
		if (billRegisterSearchEntity.getSubSchemeId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("subScheme =:subScheme");
			paramValues.put("subScheme",
					billRegisterSearchEntity.getSubSchemeId());
		}
		
		if (billRegisterSearchEntity.getFunctionaryId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("functionary =:functionary");
			paramValues.put("functionary",
					billRegisterSearchEntity.getFunctionaryId());
		}
		
		if (billRegisterSearchEntity.getDivisionId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("division =:division");
			paramValues.put("division",
					billRegisterSearchEntity.getDivisionId());
		}
		
		if (billRegisterSearchEntity.getDepartmentId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("department =:department");
			paramValues.put("department",
					billRegisterSearchEntity.getDepartmentId());
		}
		
		if (billRegisterSearchEntity.getSourcePath() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("sourcePath =:sourcePath");
			paramValues.put("sourcePath",
					billRegisterSearchEntity.getSourcePath());
		}
		
		if (billRegisterSearchEntity.getBudgetCheckRequired() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budgetCheckRequired =:budgetCheckRequired");
			paramValues.put("budgetCheckRequired",
					billRegisterSearchEntity.getBudgetCheckRequired());
		}
		
		if (billRegisterSearchEntity.getBudgetAppropriationNo() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("budgetAppropriationNo =:budgetAppropriationNo");
			paramValues.put("budgetAppropriationNo",
					billRegisterSearchEntity.getBudgetAppropriationNo());
		}
		
		if (billRegisterSearchEntity.getPartyBillNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("partyBillNumber =:partyBillNumber");
			paramValues.put("partyBillNumber",
					billRegisterSearchEntity.getPartyBillNumber());
		}
		
		if (billRegisterSearchEntity.getPartyBillDate() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("partyBillDate =:partyBillDate");
			paramValues.put("partyBillDate",
					billRegisterSearchEntity.getPartyBillDate());
		}
		
		if (billRegisterSearchEntity.getDescription() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("description =:description");
			paramValues.put("description",
					billRegisterSearchEntity.getDescription());
		}

		if (billRegisterSearchEntity.getIds() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("ids =:ids");
			paramValues.put("ids", billRegisterSearchEntity.getIds());
		}
	
		if (billRegisterSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", billRegisterSearchEntity.getTenantId());
		}
	
		if (billRegisterSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", billRegisterSearchEntity.getId());
		}

		Pagination<BillRegister> page = new Pagination<>();
	
		if (billRegisterSearchEntity.getOffset() != null) {
			page.setOffset(billRegisterSearchEntity.getOffset());
		}
		
		if (billRegisterSearchEntity.getPageSize() != null) {
			page.setPageSize(billRegisterSearchEntity.getPageSize());
		}
		
		if (params.length() > 0) {
			searchQuery = searchQuery.replace(":condition",
					" where " + params.toString());
		} else {
			searchQuery = searchQuery.replace(":condition", "");			
		}
		
		searchQuery = searchQuery.replace(":orderby", orderBy);
		
		page = (Pagination<BillRegister>) getPagination(searchQuery, page, paramValues);
		
		searchQuery = searchQuery + " :pagination";
		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset()
						* page.getPageSize());
		
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BillRegisterEntity.class);
		
		List<BillRegisterEntity> billRegisterEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
		
		page.setTotalResults(billRegisterEntities.size());
		List<BillRegister> billregisters = new ArrayList<>();
		
		for (BillRegisterEntity billRegisterEntity : billRegisterEntities) {
			billregisters.add(billRegisterEntity.toDomain());
		}
		
		page.setPagedData(billregisters);
		return page;
	}

	public BillRegisterEntity findById(BillRegisterEntity entity) {
		
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());
		
		Map<String, Object> paramValues = new HashMap<>();
		
		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}
		
		List<BillRegisterEntity> billregisters = namedParameterJdbcTemplate
				.query(getByIdQuery.get(entity.getClass().getSimpleName())
						.toString(), paramValues, new BeanPropertyRowMapper(
						BillRegisterEntity.class));
		
		if (billregisters.isEmpty()) {
			
			return null;

		} else {
		
			return billregisters.get(0);
		
		}
	}
}