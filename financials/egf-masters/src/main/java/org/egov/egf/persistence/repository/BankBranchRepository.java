package org.egov.egf.persistence.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.persistence.queue.contract.BankBranchGetRequest;
import org.egov.egf.persistence.repository.builder.BankBranchQueryBuilder;
import org.egov.egf.persistence.repository.rowmapper.BankBranchRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BankBranchRepository {

	public static final String INSERT_BANKBRANCH_QUERY = "insert into egf_bankbranch"
			+ " (id,bankid,code,name,address,address2,city,state,pincode,phone,fax,contactPerson,active,description,micr,tenantId,createdBy, createdDate, lastModifiedBy, lastModifiedDate)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_BANKBRANCH_QUERY = "update egf_bankbranch"
			+ " set bankid=?,code=?,name=?,address=?,address2=?,city=?,state=?,pincode=?,phone=?,fax=?,contactPerson=?,active=?,description=?,micr=?,tenantId = ? , lastModifiedBy = ?, lastModifiedDate = ? where id = ? and tenantid = ? ";

	private JdbcTemplate jdbcTemplate;

	private BankBranchRowMapper bankBranchRowMapper;

	private BankBranchQueryBuilder bankBranchQueryBuilder;

	@Autowired
	public BankBranchRepository(final JdbcTemplate jdbcTemplate, BankBranchRowMapper bankBranchRowMapper,
			BankBranchQueryBuilder bankBranchQueryBuilder) {
		this.jdbcTemplate = jdbcTemplate;
		this.bankBranchRowMapper = bankBranchRowMapper;
		this.bankBranchQueryBuilder = bankBranchQueryBuilder;
	}

	public BankBranchContractRequest create(BankBranchContractRequest bankBranchContractRequest) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		String seq_query = "SELECT nextval (?) as nextval";
		preparedStatementValues.add("seq_egf_bankbranch");
		List<Object[]> batchArgs = new ArrayList<>();
		if (bankBranchContractRequest.getBankBranches() != null)
			for (BankBranchContract bankBranchContract : bankBranchContractRequest.getBankBranches()) {
				Long id = jdbcTemplate.queryForObject(seq_query, preparedStatementValues.toArray(), Long.class);
				bankBranchContract.setId(id);
				bankBranchContract.setCreatedBy(bankBranchContractRequest.getRequestInfo().getUserInfo().getId());
				bankBranchContract.setCreatedDate(new Date(System.currentTimeMillis()));
				bankBranchContract.setLastModifiedBy(bankBranchContractRequest.getRequestInfo().getUserInfo().getId());
				bankBranchContract.setLastModifiedDate(new Date(System.currentTimeMillis()));
				Object[] lobRecord = { id, bankBranchContract.getBank().getId(), bankBranchContract.getCode(),
						bankBranchContract.getName(), bankBranchContract.getAddress(), bankBranchContract.getAddress2(),
						bankBranchContract.getCity(), bankBranchContract.getState(), bankBranchContract.getPincode(),
						bankBranchContract.getPhone(), bankBranchContract.getFax(),
						bankBranchContract.getContactPerson(), bankBranchContract.getActive(),
						bankBranchContract.getDescription(), bankBranchContract.getMicr(),
						bankBranchContract.getTenantId(), bankBranchContract.getCreatedBy(),
						bankBranchContract.getCreatedDate(), bankBranchContract.getLastModifiedBy(),
						bankBranchContract.getLastModifiedDate() };
				batchArgs.add(lobRecord);
			}
		if (bankBranchContractRequest.getBankBranch() != null
				&& bankBranchContractRequest.getBankBranch().getName() != null) {
			Long id = jdbcTemplate.queryForObject(seq_query, preparedStatementValues.toArray(), Long.class);
			bankBranchContractRequest.getBankBranch().setId(id);
			bankBranchContractRequest.getBankBranch()
					.setCreatedBy(bankBranchContractRequest.getRequestInfo().getUserInfo().getId());
			bankBranchContractRequest.getBankBranch().setCreatedDate(new Date(System.currentTimeMillis()));
			bankBranchContractRequest.getBankBranch()
					.setLastModifiedBy(bankBranchContractRequest.getRequestInfo().getUserInfo().getId());
			bankBranchContractRequest.getBankBranch().setLastModifiedDate(new Date(System.currentTimeMillis()));
			Object[] lobRecord = { bankBranchContractRequest.getBankBranch().getId(),
					bankBranchContractRequest.getBankBranch().getBank().getId(),
					bankBranchContractRequest.getBankBranch().getCode(),
					bankBranchContractRequest.getBankBranch().getName(),
					bankBranchContractRequest.getBankBranch().getAddress(),
					bankBranchContractRequest.getBankBranch().getAddress2(),
					bankBranchContractRequest.getBankBranch().getCity(),
					bankBranchContractRequest.getBankBranch().getState(),
					bankBranchContractRequest.getBankBranch().getPincode(),
					bankBranchContractRequest.getBankBranch().getPhone(),
					bankBranchContractRequest.getBankBranch().getFax(),
					bankBranchContractRequest.getBankBranch().getContactPerson(),
					bankBranchContractRequest.getBankBranch().getActive(),
					bankBranchContractRequest.getBankBranch().getDescription(),
					bankBranchContractRequest.getBankBranch().getMicr(),
					bankBranchContractRequest.getBankBranch().getTenantId(),
					bankBranchContractRequest.getBankBranch().getCreatedBy(),
					bankBranchContractRequest.getBankBranch().getCreatedDate(),
					bankBranchContractRequest.getBankBranch().getLastModifiedBy(),
					bankBranchContractRequest.getBankBranch().getLastModifiedDate() };

			batchArgs.add(lobRecord);
		}

		try {
			jdbcTemplate.batchUpdate(INSERT_BANKBRANCH_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

		return bankBranchContractRequest;

	}

	public BankBranchContractRequest update(BankBranchContractRequest bankBranchContractRequest) {

		List<Object[]> batchArgs = new ArrayList<>();
		if (bankBranchContractRequest.getBankBranches() != null)
			for (BankBranchContract bankBranchContract : bankBranchContractRequest.getBankBranches()) {
				bankBranchContract.setLastModifiedBy(bankBranchContractRequest.getRequestInfo().getUserInfo().getId());
				bankBranchContract.setLastModifiedDate(new Date(System.currentTimeMillis()));
				Object[] lobRecord = { bankBranchContract.getBank().getId(), bankBranchContract.getCode(),
						bankBranchContract.getName(), bankBranchContract.getAddress(), bankBranchContract.getAddress2(),
						bankBranchContract.getCity(), bankBranchContract.getState(), bankBranchContract.getPincode(),
						bankBranchContract.getPhone(), bankBranchContract.getFax(),
						bankBranchContract.getContactPerson(), bankBranchContract.getActive(),
						bankBranchContract.getDescription(), bankBranchContract.getMicr(),
						bankBranchContract.getTenantId(), bankBranchContract.getLastModifiedBy(),
						bankBranchContract.getLastModifiedDate(), bankBranchContract.getId(),
						bankBranchContract.getTenantId() };
				batchArgs.add(lobRecord);
			}
		if (bankBranchContractRequest.getBankBranch() != null
				&& bankBranchContractRequest.getBankBranch().getName() != null) {
			bankBranchContractRequest.getBankBranch()
					.setLastModifiedBy(bankBranchContractRequest.getRequestInfo().getUserInfo().getId());
			bankBranchContractRequest.getBankBranch().setLastModifiedDate(new Date(System.currentTimeMillis()));
			Object[] lobRecord = { bankBranchContractRequest.getBankBranch().getBank().getId(),
					bankBranchContractRequest.getBankBranch().getCode(),
					bankBranchContractRequest.getBankBranch().getName(),
					bankBranchContractRequest.getBankBranch().getAddress(),
					bankBranchContractRequest.getBankBranch().getAddress2(),
					bankBranchContractRequest.getBankBranch().getCity(), 4,
					bankBranchContractRequest.getBankBranch().getState(),
					bankBranchContractRequest.getBankBranch().getPincode(),
					bankBranchContractRequest.getBankBranch().getPhone(),
					bankBranchContractRequest.getBankBranch().getFax(),
					bankBranchContractRequest.getBankBranch().getContactPerson(),
					bankBranchContractRequest.getBankBranch().getActive(),
					bankBranchContractRequest.getBankBranch().getDescription(),
					bankBranchContractRequest.getBankBranch().getMicr(),
					bankBranchContractRequest.getBankBranch().getTenantId(),
					bankBranchContractRequest.getBankBranch().getLastModifiedBy(),
					bankBranchContractRequest.getBankBranch().getLastModifiedDate(),
					bankBranchContractRequest.getBankBranch().getId(),
					bankBranchContractRequest.getBankBranch().getTenantId() };

			batchArgs.add(lobRecord);
		}

		try {
			jdbcTemplate.batchUpdate(UPDATE_BANKBRANCH_QUERY, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

		return bankBranchContractRequest;

	}

	public List<BankBranchContract> findForCriteria(BankBranchGetRequest bankBranchGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = bankBranchQueryBuilder.getQuery(bankBranchGetRequest, preparedStatementValues);
		List<BankBranchContract> bankBranches = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				bankBranchRowMapper);
		return bankBranches;
	}
}