package org.egov.egf.voucher.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.voucher.domain.model.LedgerDetail;
import org.egov.egf.voucher.persistence.entity.LedgerDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class LedgerDetailJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(LedgerDetailJdbcRepository.class);

	static {
		LOG.debug("init ledgerDetail");
		init(LedgerDetailEntity.class);
		LOG.debug("end init ledgerDetail");
	}

	public LedgerDetailEntity create(LedgerDetailEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public LedgerDetailEntity update(LedgerDetailEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<LedgerDetail> search(LedgerDetail domain) {
		LedgerDetailEntity ledgerDetailSearchEntity = new LedgerDetailEntity();
		ledgerDetailSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", LedgerDetailEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( ledgerDetailSearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =:id");
paramValues.put("id" ,ledgerDetailSearchEntity.getId());} 
if( ledgerDetailSearchEntity.getAccountDetailTypeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "accountDetailType =:accountDetailType");
paramValues.put("accountDetailType" ,ledgerDetailSearchEntity.getAccountDetailTypeId());} 
if( ledgerDetailSearchEntity.getAccountDetailKeyId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "accountDetailKey =:accountDetailKey");
paramValues.put("accountDetailKey" ,ledgerDetailSearchEntity.getAccountDetailKeyId());} 
if( ledgerDetailSearchEntity.getAmount()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "amount =:amount");
paramValues.put("amount" ,ledgerDetailSearchEntity.getAmount());} 

		 

		Pagination<LedgerDetail> page = new Pagination<>();
	//	page.setOffSet(ledgerDetailSearchEntity.getOffset());
	//	page.setPageSize(ledgerDetailSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = (Pagination<LedgerDetail>) getPagination(searchQuery, page,paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + Pagination.DEFAULT_PAGE_SIZE + " offset "
				+ Pagination.DEFAULT_PAGE_OFFSET * Pagination.DEFAULT_PAGE_SIZE);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(LedgerDetailEntity.class);

		List<LedgerDetailEntity> ledgerDetailEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(ledgerDetailEntities.size());

		List<LedgerDetail> ledgerdetails = new ArrayList<LedgerDetail>();
		for (LedgerDetailEntity ledgerDetailEntity : ledgerDetailEntities) {

			ledgerdetails.add(ledgerDetailEntity.toDomain());
		}
		page.setPagedData(ledgerdetails);

		return page;
	}

	public LedgerDetailEntity findById(LedgerDetailEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<LedgerDetailEntity> ledgerdetails = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<LedgerDetailEntity>());
		if (ledgerdetails.isEmpty()) {
			return null;
		} else {
			return ledgerdetails.get(0);
		}

	}

}