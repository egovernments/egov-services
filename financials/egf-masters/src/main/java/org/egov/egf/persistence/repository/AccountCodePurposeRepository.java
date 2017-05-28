package org.egov.egf.persistence.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContract;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractRequest;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeGetRequest;
import org.egov.egf.persistence.repository.builder.AccountCodePurposeQueryBuilder;
import org.egov.egf.persistence.repository.rowmapper.AccountCodePurposeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountCodePurposeRepository {

	public static final String INSERT_ACCOUNTCODEPURPOSE_QUERY = "INSERT INTO egf_accountcodepurpose"
			+ " (id, name,tenantId,createdBy, createdDate, lastModifiedBy, lastModifiedDate)"
			+ " VALUES (?,?,?,?,?,?,?)";

	public static final String UPDATE_ACCOUNTCODEPURPOSE_QUERY = "update egf_accountcodepurpose"
			+ " set name = ?,tenantId = ? , lastModifiedBy = ?, lastModifiedDate = ? where id = ? and tenantid = ? ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AccountCodePurposeRowMapper accountCodePurposeRowMapper;

	@Autowired
	private AccountCodePurposeQueryBuilder accountCodePurposeQueryBuilder;

	public AccountCodePurposeContractRequest create(
			AccountCodePurposeContractRequest accountCodePurposeContractRequest) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		String seq_query = "SELECT nextval (?) as nextval";
		preparedStatementValues.add("seq_egf_accountcodepurpose");
		List<Object[]> batchArgs = new ArrayList<>();
		if (accountCodePurposeContractRequest.getAccountCodePurposes() != null)
			for (AccountCodePurposeContract accountCodePurposeContract : accountCodePurposeContractRequest
					.getAccountCodePurposes()) {
				Long id = jdbcTemplate.queryForObject(seq_query, preparedStatementValues.toArray(), Long.class);
				accountCodePurposeContract.setId(id);
				accountCodePurposeContract
						.setCreatedBy(accountCodePurposeContractRequest.getRequestInfo().getUserInfo().getId());
				accountCodePurposeContract.setCreatedDate(new Date(System.currentTimeMillis()));
				accountCodePurposeContract
						.setLastModifiedBy(accountCodePurposeContractRequest.getRequestInfo().getUserInfo().getId());
				accountCodePurposeContract.setLastModifiedDate(new Date(System.currentTimeMillis()));
				Object[] lobRecord = { id, accountCodePurposeContract.getName(),
						accountCodePurposeContract.getTenantId(), accountCodePurposeContract.getCreatedBy(),
						accountCodePurposeContract.getCreatedDate(), accountCodePurposeContract.getLastModifiedBy(),
						accountCodePurposeContract.getLastModifiedDate() };
				batchArgs.add(lobRecord);
			}
		if (accountCodePurposeContractRequest.getAccountCodePurpose() != null
				&& accountCodePurposeContractRequest.getAccountCodePurpose().getName() != null) {
			Long id = jdbcTemplate.queryForObject(seq_query, preparedStatementValues.toArray(), Long.class);
			accountCodePurposeContractRequest.getAccountCodePurpose().setId(id);
			accountCodePurposeContractRequest.getAccountCodePurpose()
					.setCreatedBy(accountCodePurposeContractRequest.getRequestInfo().getUserInfo().getId());
			accountCodePurposeContractRequest.getAccountCodePurpose()
					.setCreatedDate(new Date(System.currentTimeMillis()));
			accountCodePurposeContractRequest.getAccountCodePurpose()
					.setLastModifiedBy(accountCodePurposeContractRequest.getRequestInfo().getUserInfo().getId());
			accountCodePurposeContractRequest.getAccountCodePurpose()
					.setLastModifiedDate(new Date(System.currentTimeMillis()));
			Object[] lobRecord = { accountCodePurposeContractRequest.getAccountCodePurpose().getId(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getName(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getTenantId(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getCreatedBy(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getCreatedDate(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getLastModifiedBy(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getLastModifiedDate() };

			batchArgs.add(lobRecord);
		}

		try {
			jdbcTemplate.batchUpdate(INSERT_ACCOUNTCODEPURPOSE_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

		return accountCodePurposeContractRequest;

	}

	public AccountCodePurposeContractRequest update(
			AccountCodePurposeContractRequest accountCodePurposeContractRequest) {

		List<Object[]> batchArgs = new ArrayList<>();
		if (accountCodePurposeContractRequest.getAccountCodePurposes() != null)
			for (AccountCodePurposeContract accountCodePurposeContract : accountCodePurposeContractRequest
					.getAccountCodePurposes()) {
				accountCodePurposeContract
						.setLastModifiedBy(accountCodePurposeContractRequest.getRequestInfo().getUserInfo().getId());
				accountCodePurposeContract.setLastModifiedDate(new Date(System.currentTimeMillis()));
				Object[] lobRecord = { accountCodePurposeContract.getName(), accountCodePurposeContract.getTenantId(),
						accountCodePurposeContractRequest.getAccountCodePurpose().getLastModifiedBy(),
						accountCodePurposeContractRequest.getAccountCodePurpose().getLastModifiedDate(),
						accountCodePurposeContract.getId(), accountCodePurposeContract.getTenantId() };
				batchArgs.add(lobRecord);
			}
		if (accountCodePurposeContractRequest.getAccountCodePurpose() != null
				&& accountCodePurposeContractRequest.getAccountCodePurpose().getId() != null) {
			accountCodePurposeContractRequest.getAccountCodePurpose()
					.setLastModifiedBy(accountCodePurposeContractRequest.getRequestInfo().getUserInfo().getId());
			accountCodePurposeContractRequest.getAccountCodePurpose()
					.setLastModifiedDate(new Date(System.currentTimeMillis()));
			Object[] lobRecord = { accountCodePurposeContractRequest.getAccountCodePurpose().getName(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getTenantId(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getLastModifiedBy(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getLastModifiedDate(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getId(),
					accountCodePurposeContractRequest.getAccountCodePurpose().getTenantId() };
			batchArgs.add(lobRecord);
		}

		try {
			jdbcTemplate.batchUpdate(UPDATE_ACCOUNTCODEPURPOSE_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

		return accountCodePurposeContractRequest;

	}

	public List<AccountCodePurpose> findForCriteria(AccountCodePurposeGetRequest accountCodePurposeGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = accountCodePurposeQueryBuilder.getQuery(accountCodePurposeGetRequest,
				preparedStatementValues);
		List<AccountCodePurpose> accountCodePurposes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				accountCodePurposeRowMapper);
		return accountCodePurposes;
	}

}