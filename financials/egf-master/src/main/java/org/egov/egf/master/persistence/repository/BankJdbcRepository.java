package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankSearch;
import org.egov.egf.master.persistence.entity.BankEntity;
import org.egov.egf.master.persistence.entity.BankSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class BankJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(BankJdbcRepository.class);

	static {
		LOG.debug("init bank");
		init(BankEntity.class);
		LOG.debug("end init bank");
	}

	public BankEntity create(BankEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public BankEntity update(BankEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<Bank> search(BankSearch domain) {
		BankSearchEntity bankSearchEntity = new BankSearchEntity();
		bankSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", BankEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (bankSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", bankSearchEntity.getId());
		}
		if (bankSearchEntity.getCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code =:code");
			paramValues.put("code", bankSearchEntity.getCode());
		}
		if (bankSearchEntity.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name =:name");
			paramValues.put("name", bankSearchEntity.getName());
		}
		if (bankSearchEntity.getDescription() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("description =:description");
			paramValues.put("description", bankSearchEntity.getDescription());
		}
		if (bankSearchEntity.getActive() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("active =:active");
			paramValues.put("active", bankSearchEntity.getActive());
		}
		if (bankSearchEntity.getType() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("type =:type");
			paramValues.put("type", bankSearchEntity.getType());
		}

		if (bankSearchEntity.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name=:name");
			paramValues.put("name", bankSearchEntity.getName());
		}

		if (bankSearchEntity.getCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code=:code");
			paramValues.put("code", bankSearchEntity.getCode());
		}

		Pagination<Bank> page = new Pagination<>();
		if (bankSearchEntity.getOffset() != null)
			page.setOffset(bankSearchEntity.getOffset());
		if (bankSearchEntity.getPageSize() != null)
			page.setPageSize(bankSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BankEntity.class);

		List<BankEntity> bankEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(bankEntities.size());

		List<Bank> banks = new ArrayList<Bank>();
		for (BankEntity bankEntity : bankEntities) {

			banks.add(bankEntity.toDomain());
		}
		page.setPagedData(banks);

		return page;
	}

	public BankEntity findById(BankEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<BankEntity> banks = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(BankEntity.class));
		if (banks.isEmpty()) {
			return null;
		} else {
			return banks.get(0);
		}

	}

}