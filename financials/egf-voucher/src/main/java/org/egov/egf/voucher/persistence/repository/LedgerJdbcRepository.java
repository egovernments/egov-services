package org.egov.egf.voucher.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.voucher.domain.model.Ledger;
import org.egov.egf.voucher.persistence.entity.LedgerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LedgerJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(LedgerJdbcRepository.class);

	static {
		LOG.debug("init ledger");
		init(LedgerEntity.class);
		LOG.debug("end init ledger");
	}

	@Transactional
	public LedgerEntity create(LedgerEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public LedgerEntity update(LedgerEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<Ledger> search(Ledger domain) {
		LedgerEntity ledgerSearchEntity = new LedgerEntity();
		ledgerSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", LedgerEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (ledgerSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", ledgerSearchEntity.getId());
		}
		if (ledgerSearchEntity.getOrderId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("orderId =:orderId");
			paramValues.put("orderId", ledgerSearchEntity.getOrderId());
		}
		if (ledgerSearchEntity.getChartOfAccountId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("chartOfAccount =:chartOfAccount");
			paramValues.put("chartOfAccount", ledgerSearchEntity.getChartOfAccountId());
		}
		if (ledgerSearchEntity.getGlcode() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("glcode =:glcode");
			paramValues.put("glcode", ledgerSearchEntity.getGlcode());
		}
		if (ledgerSearchEntity.getDebitAmount() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("debitAmount =:debitAmount");
			paramValues.put("debitAmount", ledgerSearchEntity.getDebitAmount());
		}
		if (ledgerSearchEntity.getCreditAmount() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("creditAmount =:creditAmount");
			paramValues.put("creditAmount", ledgerSearchEntity.getCreditAmount());
		}
		if (ledgerSearchEntity.getFunctionId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("function =:function");
			paramValues.put("function", ledgerSearchEntity.getFunctionId());
		}
		
		Pagination<Ledger> page = new Pagination<>();
		//page.setOffSet(ledgerSearchEntity.getOffset());
		//page.setPageSize(ledgerSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = (Pagination<Ledger>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + 500 + " offset "
				+ 0 * 500);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(LedgerEntity.class);

		List<LedgerEntity> ledgerEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(ledgerEntities.size());

		List<Ledger> ledgers = new ArrayList<Ledger>();
		for (LedgerEntity ledgerEntity : ledgerEntities) {

			ledgers.add(ledgerEntity.toDomain());
		}
		page.setPagedData(ledgers);

		return page;
	}

	public LedgerEntity findById(LedgerEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<LedgerEntity> ledgers = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<LedgerEntity>());
		if (ledgers.isEmpty()) {
			return null;
		} else {
			return ledgers.get(0);
		}

	}

}