package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.AccountEntity;
import org.egov.egf.master.domain.model.AccountEntitySearch;
import org.egov.egf.master.persistence.entity.AccountEntityEntity;
import org.egov.egf.master.persistence.entity.AccountEntitySearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class AccountEntityJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(AccountEntityJdbcRepository.class);

	static {
		LOG.debug("init accountEntity");
		init(AccountEntityEntity.class);
		LOG.debug("end init accountEntity");
	}

	public AccountEntityEntity create(AccountEntityEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public AccountEntityEntity update(AccountEntityEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<AccountEntity> search(AccountEntitySearch domain) {
		AccountEntitySearchEntity accountEntitySearchEntity = new AccountEntitySearchEntity();
		accountEntitySearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", AccountEntityEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( accountEntitySearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =: id");
paramValues.put("id" ,accountEntitySearchEntity.getId());} 
if( accountEntitySearchEntity.getAccountDetailTypeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "accountDetailType =: accountDetailType");
paramValues.put("accountDetailType" ,accountEntitySearchEntity.getAccountDetailTypeId());} 
if( accountEntitySearchEntity.getCode()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "code =: code");
paramValues.put("code" ,accountEntitySearchEntity.getCode());} 
if( accountEntitySearchEntity.getName()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "name =: name");
paramValues.put("name" ,accountEntitySearchEntity.getName());} 
if( accountEntitySearchEntity.getActive()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "active =: active");
paramValues.put("active" ,accountEntitySearchEntity.getActive());} 
if( accountEntitySearchEntity.getDescription()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "description =: description");
paramValues.put("description" ,accountEntitySearchEntity.getDescription());} 

		 

		Pagination<AccountEntity> page = new Pagination<>();
		page.setOffSet(accountEntitySearchEntity.getOffset());
		page.setPageSize(accountEntitySearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page,paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + accountEntitySearchEntity.getPageSize() + " offset "
				+ accountEntitySearchEntity.getOffset() * accountEntitySearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(AccountEntityEntity.class);

		List<AccountEntityEntity> accountEntityEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(accountEntityEntities.size());

		List<AccountEntity> accountentities = new ArrayList<AccountEntity>();
		for (AccountEntityEntity accountEntityEntity : accountEntityEntities) {

			accountentities.add(accountEntityEntity.toDomain());
		}
		page.setPagedData(accountentities);

		return page;
	}

	public AccountEntityEntity findById(AccountEntityEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<AccountEntityEntity> accountentities = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<AccountEntityEntity>());
		if (accountentities.isEmpty()) {
			return null;
		} else {
			return accountentities.get(0);
		}

	}

}