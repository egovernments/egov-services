package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.BankAccount;
import org.egov.egf.master.domain.model.BankAccountSearch;
import org.egov.egf.master.persistence.entity.BankAccountEntity;
import org.egov.egf.master.persistence.entity.BankAccountSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class BankAccountJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(BankAccountJdbcRepository.class);

	static {
		LOG.debug("init bankAccount");
		init(BankAccountEntity.class);
		LOG.debug("end init bankAccount");
	}

	public BankAccountEntity create(BankAccountEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public BankAccountEntity update(BankAccountEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<BankAccount> search(BankAccountSearch domain) {
		BankAccountSearchEntity bankAccountSearchEntity = new BankAccountSearchEntity();
		bankAccountSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", BankAccountEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( bankAccountSearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =: id");
paramValues.put("id" ,bankAccountSearchEntity.getId());} 
if( bankAccountSearchEntity.getBankBranchId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "bankBranch =: bankBranch");
paramValues.put("bankBranch" ,bankAccountSearchEntity.getBankBranchId());} 
if( bankAccountSearchEntity.getChartOfAccountId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "chartOfAccount =: chartOfAccount");
paramValues.put("chartOfAccount" ,bankAccountSearchEntity.getChartOfAccountId());} 
if( bankAccountSearchEntity.getFundId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "fund =: fund");
paramValues.put("fund" ,bankAccountSearchEntity.getFundId());} 
if( bankAccountSearchEntity.getAccountNumber()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "accountNumber =: accountNumber");
paramValues.put("accountNumber" ,bankAccountSearchEntity.getAccountNumber());} 
if( bankAccountSearchEntity.getAccountType()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "accountType =: accountType");
paramValues.put("accountType" ,bankAccountSearchEntity.getAccountType());} 
if( bankAccountSearchEntity.getDescription()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "description =: description");
paramValues.put("description" ,bankAccountSearchEntity.getDescription());} 
if( bankAccountSearchEntity.getActive()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "active =: active");
paramValues.put("active" ,bankAccountSearchEntity.getActive());} 
if( bankAccountSearchEntity.getPayTo()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "payTo =: payTo");
paramValues.put("payTo" ,bankAccountSearchEntity.getPayTo());} 
if( bankAccountSearchEntity.getTypeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "type =: type");
paramValues.put("type" ,bankAccountSearchEntity.getTypeId());} 

		 

		Pagination<BankAccount> page = new Pagination<>();
		page.setOffSet(bankAccountSearchEntity.getOffset());
		page.setPageSize(bankAccountSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + bankAccountSearchEntity.getPageSize() + " offset "
				+ bankAccountSearchEntity.getOffset() * bankAccountSearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BankAccountEntity.class);

		List<BankAccountEntity> bankAccountEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(bankAccountEntities.size());

		List<BankAccount> bankaccounts = new ArrayList<BankAccount>();
		for (BankAccountEntity bankAccountEntity : bankAccountEntities) {

			bankaccounts.add(bankAccountEntity.toDomain());
		}
		page.setPagedData(bankaccounts);

		return page;
	}

	public BankAccountEntity findById(BankAccountEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<BankAccountEntity> bankaccounts = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<BankAccountEntity>());
		if (bankaccounts.isEmpty()) {
			return null;
		} else {
			return bankaccounts.get(0);
		}

	}

}