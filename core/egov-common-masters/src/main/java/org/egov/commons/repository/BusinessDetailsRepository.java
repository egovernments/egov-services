package org.egov.commons.repository;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.egov.commons.model.BusinessAccountDetails;
import org.egov.commons.model.BusinessAccountSubLedgerDetails;
import org.egov.commons.model.BusinessDetails;
import org.egov.commons.model.BusinessDetailsCommonModel;
import org.egov.commons.model.BusinessDetailsCriteria;
import org.egov.commons.repository.builder.BusinessDetailsQueryBuilder;
import org.egov.commons.repository.rowmapper.BusinessAccountDetailsRowMapper;
import org.egov.commons.repository.rowmapper.BusinessAccountSubledgerDetailsRowMapper;
import org.egov.commons.repository.rowmapper.BusinessDetailsCombinedRowMapper;
import org.egov.commons.repository.rowmapper.BusinessDetailsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BusinessDetailsRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	BusinessAccountDetailsRowMapper businessAccountDetailsRowMapper;

	@Autowired
	BusinessDetailsCombinedRowMapper businessDetailsCombinedRowMapper;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	BusinessDetailsRowMapper businessDetailsRowMapper;

	@Autowired
	BusinessAccountSubledgerDetailsRowMapper businessAccountSubledgerDetailsRowMapper;

	@Autowired
	BusinessDetailsQueryBuilder businessDetailsQueryBuilder;
	public static final String SEQUENCEFORSUBLEDGER = "SEQ_EG_BUSINESS_SUBLEDGERINFO";
	public static final String TENANT = "tenantId";
	public static final String INSERT_BUSINESS_DETAILS = "Insert into eg_businessdetails"
			+ " (id,name,isEnabled,code,businessType,businessUrl,voucherCutOffDate,"
			+ "ordernumber,voucherCreation,isVoucherApproved,fund,department,"
			+ "fundSource,functionary,businessCategory,function,callBackForApportioning,tenantId,"
			+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate)" + " values (?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_BUSINESS_DETAILS = "Update eg_businessdetails"
			+ " set name=?,isEnabled=?,code=?,businessType=?,businessUrl=?,voucherCutOffDate=?,"
			+ "ordernumber=?,voucherCreation=?,isVoucherApproved=?,fund=?,department=?,"
			+ "fundSource=?,functionary=?,businessCategory=?,function=?,callBackForApportioning=?,"
			+ "tenantId=?,lastModifiedBy=?,lastModifiedDate=? where id=?";

	public static final String UPDATE_BUSINESS_ACCOUNT_DETAILS = "Update eg_business_accountdetails"
			+ " set businessDetails=?,chartOfAccount=?,amount=?,tenantId=?" + " where id=?";

	public static final String UPDATE_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS = "Update eg_business_subledgerinfo"
			+ " set amount=?,businessAccountDetail=?,accountDetailKey=?,accountDetailType=?,"
			+ " tenantId=? where id=?";

	public static final String GET_BUSINESS_ACCOUNTDETAILS_BY_BUSINESSDETAILS = "Select * from"
			+ " eg_business_accountdetails where businessDetails=? and" + " tenantId=?";

	public static final String GET_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS = "Select * from"
			+ " eg_business_subledgerinfo where businessAccountDetail IN (:id) and tenantId=:tenantId";

	public static final String DELETE_BUSINESS_ACCOUNT_DETAILS = "Delete from eg_business_accountdetails"
			+ " where id IN (:id) and tenantId=:tenantId";

	public static final String INSERT_BUSINESS_ACCOUNT_DETAILS = "Insert into eg_business_accountdetails"
			+ " (id,businessDetails,chartOfAccount,amount,tenantId)" + " values (?,?,?,?,?)";

	public static final String INSERT_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS = "insert into eg_business_subledgerinfo"
			+ " (id,amount,businessAccountDetail,accountDetailKey,accountDetailType,tenantId)"
			+ " values (?,?,?,?,?,?)";

	public static final String GET_BUSINESS_ACCOUNT_DETAILS_BY_ID_AND_TENANTID = "Select * from eg_business_"
			+ "accountdetails where id =? and tenantId=?";

	public static final String GET_BUSINESSDETAILS_BY_CODE_AND_TENANTID = "Select * from eg_businessdetails"
			+ " where code=? and tenantid=?";

	private static final String DELETE_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS = "Delete from eg_business_subledgerinfo"
			+ " where id IN (:id) and tenantId=:tenantId";

	private static final String GET_DETAILS_BY_NAME_AND_TENANTID = "Select * from eg_businessdetails"
			+ " where name=? and tenantId=?";
	private static final String GET_DETAILS_BY_NAME_TENANTID_AND_ID = "Select * from eg_businessdetails"
			+ " where name=? and tenantId=? and id != ?";
	private static final String GET_BUSINESSDETAILS_BY_CODE_AND_TENANTID_AND_ID = "Select * from eg_businessdetails"
			+ " where code=? and tenantId=? and id != ?";

	public void createBusinessDetails(BusinessDetails modelDetails,
			List<BusinessAccountDetails> listModelAccountDetails,
			List<BusinessAccountSubLedgerDetails> listModelAccountSubledger) {
		Long detailsId = generateSequence("SEQ_EG_BUSINESSDETAILS");
		Object[] obj = new Object[] { detailsId, modelDetails.getName(), modelDetails.getIsEnabled(),
				modelDetails.getCode(), modelDetails.getBusinessType(), modelDetails.getBusinessUrl(),
				modelDetails.getVoucherCutoffDate(), modelDetails.getOrdernumber(), modelDetails.getVoucherCreation(),
				modelDetails.getIsVoucherApproved(), modelDetails.getFund(), modelDetails.getDepartment(),
				modelDetails.getFundSource(), modelDetails.getFunctionary(), modelDetails.getBusinessCategory().getId(),
				modelDetails.getFunction(), modelDetails.getCallBackForApportioning(), modelDetails.getTenantId(),
				modelDetails.getCreatedBy(), new Date(new java.util.Date().getTime()), modelDetails.getLastModifiedBy(),
				new Date(new java.util.Date().getTime()) };
		jdbcTemplate.update(INSERT_BUSINESS_DETAILS, obj);
		List<Object[]> batchArgs = new ArrayList<>();
		List<Long> listOfAccountDetailsId = new ArrayList<>();
		for (BusinessAccountDetails accountDetails : listModelAccountDetails) {
			Long accountDetailsId = generateSequence("SEQ_EG_BUSINESS_ACCOUNTDETAILS");
			Object[] businessRecord = { accountDetailsId, detailsId, accountDetails.getChartOfAccount(),
					accountDetails.getAmount(), accountDetails.getTenantId() };
			listOfAccountDetailsId.add(accountDetailsId);
			batchArgs.add(businessRecord);
		}
		jdbcTemplate.batchUpdate(INSERT_BUSINESS_ACCOUNT_DETAILS, batchArgs);
		List<BusinessAccountDetails> accountDetailsFromDB = new ArrayList<>();
		for (Long accountId : listOfAccountDetailsId) {
			final List<Object> preparedStatementValues = new ArrayList<>();
			preparedStatementValues.add(accountId);
			preparedStatementValues.add(modelDetails.getTenantId());
			List<BusinessAccountDetails> accountDetails = jdbcTemplate.query(
					GET_BUSINESS_ACCOUNT_DETAILS_BY_ID_AND_TENANTID, preparedStatementValues.toArray(),
					businessAccountDetailsRowMapper);
			accountDetailsFromDB.add(accountDetails.get(0));
		}
		List<Object[]> batch = new ArrayList<>();
		for (BusinessAccountSubLedgerDetails subledger : listModelAccountSubledger) {
			for (BusinessAccountDetails accountDetails : accountDetailsFromDB) {
				if (subledger.getBusinessAccountDetail().getChartOfAccount()
						.equals(accountDetails.getChartOfAccount())) {
					Object[] subledgerRecord = { generateSequence(SEQUENCEFORSUBLEDGER), subledger.getAmount(),
							accountDetails.getId(), subledger.getAccountDetailKey(), subledger.getAccountDetailType(),
							subledger.getTenantId() };
					batch.add(subledgerRecord);
				}
			}
		}
		jdbcTemplate.batchUpdate(INSERT_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS, batch);

	}

	public Long generateSequence(String sequenceName) {
		return jdbcTemplate.queryForObject("SELECT nextval('" + sequenceName + "')", Long.class);
	}

	public void updateBusinessDetails(BusinessDetails modelDetails,
			List<BusinessAccountDetails> listModelAccountDetails,
			List<BusinessAccountSubLedgerDetails> listModelAccountSubledger) {
		Object[] obj = new Object[] { modelDetails.getName(), modelDetails.getIsEnabled(), modelDetails.getCode(),
				modelDetails.getBusinessType(), modelDetails.getBusinessUrl(), modelDetails.getVoucherCutoffDate(),
				modelDetails.getOrdernumber(), modelDetails.getVoucherCreation(), modelDetails.getIsVoucherApproved(),
				modelDetails.getFund(), modelDetails.getDepartment(), modelDetails.getFundSource(),
				modelDetails.getFunctionary(), modelDetails.getBusinessCategory().getId(), modelDetails.getFunction(),
				modelDetails.getCallBackForApportioning(), modelDetails.getTenantId(), modelDetails.getLastModifiedBy(),
				new Date(new java.util.Date().getTime()), modelDetails.getId() };
		jdbcTemplate.update(UPDATE_BUSINESS_DETAILS, obj);
		final List<Object> preparedStatementValue = new ArrayList<>();
		preparedStatementValue.add(modelDetails.getId());
		preparedStatementValue.add(modelDetails.getTenantId());
		List<BusinessAccountDetails> accountDetailsFromDB = jdbcTemplate.query(
				GET_BUSINESS_ACCOUNTDETAILS_BY_BUSINESSDETAILS, preparedStatementValue.toArray(),
				businessAccountDetailsRowMapper);
		HashMap<Long, Long> mapOfInsertedIdsInModelAndInDB = new HashMap<>();
		for (BusinessAccountDetails accountdetail : listModelAccountDetails) {
			if (needsInsert(accountdetail, accountDetailsFromDB)) {
				Long accountDetailIdFromDB = generateSequence("SEQ_EG_BUSINESS_ACCOUNTDETAILS");
				mapOfInsertedIdsInModelAndInDB.put(accountdetail.getId(), accountDetailIdFromDB);
				Object[] object = new Object[] { accountDetailIdFromDB, accountdetail.getBusinessDetails().getId(),
						accountdetail.getChartOfAccount(), accountdetail.getAmount(), accountdetail.getTenantId() };
				jdbcTemplate.update(INSERT_BUSINESS_ACCOUNT_DETAILS, object);
			} else if (needsUpdate(accountdetail, accountDetailsFromDB))
				updateAccountDetails(accountdetail);
		}
		List<BusinessAccountDetails> deletedAccountDetails = deleteAccountDetailsFromDBIfNotPresentInInput(
				listModelAccountDetails, accountDetailsFromDB);
		List<Long> deleteAccountDetailsId = deletedAccountDetails.stream().map(accountdetail -> accountdetail.getId())
				.collect(Collectors.toList());
		if (!deleteAccountDetailsId.isEmpty())
			deleteAccountDetails(deleteAccountDetailsId, modelDetails.getTenantId());
		List<Long> accountDetailsIds = listModelAccountDetails.stream()
				.map(modelAccountDetail -> modelAccountDetail.getId()).collect(Collectors.toList());
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("id", accountDetailsIds);
		namedParameters.put(TENANT, listModelAccountDetails.get(0).getTenantId());

		List<BusinessAccountSubLedgerDetails> subledgerfromDB = namedParameterJdbcTemplate.query(
				GET_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS, namedParameters, businessAccountSubledgerDetailsRowMapper);
		for (BusinessAccountSubLedgerDetails subledgerModel : listModelAccountSubledger) {
			if (subledgerNeedsInsert(subledgerModel, subledgerfromDB))
				insertSubledgerDetails(subledgerModel, mapOfInsertedIdsInModelAndInDB);
			else if (subledgerNeedsUpdate(subledgerModel, subledgerfromDB))
				updateSubledgerDetails(subledgerModel);
		}
		List<BusinessAccountSubLedgerDetails> accountSubledgerToBeDeleted = deleteSubledgerDetailsFromDBIfNotPresentInInput(
				subledgerfromDB, listModelAccountSubledger);
		List<Long> deleteSubledgerDetailsIds = accountSubledgerToBeDeleted.stream().map(subledger -> subledger.getId())
				.collect(Collectors.toList());
		if (!deleteSubledgerDetailsIds.isEmpty())
			deleteSubledgerDetails(deleteSubledgerDetailsIds, modelDetails.getTenantId());

	}

	private void deleteSubledgerDetails(List<Long> deleteSubledgerDetailsIds, String tenantId) {
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("id", deleteSubledgerDetailsIds);
		namedParameters.put(TENANT, tenantId);
		namedParameterJdbcTemplate.update(DELETE_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS, namedParameters);
	}

	private List<BusinessAccountSubLedgerDetails> deleteSubledgerDetailsFromDBIfNotPresentInInput(
			List<BusinessAccountSubLedgerDetails> subledgerfromDB,
			List<BusinessAccountSubLedgerDetails> listModelAccountSubledger) {
		List<BusinessAccountSubLedgerDetails> subledgerDetailsToBeDeletedFromDB = new ArrayList<>();
		for (BusinessAccountSubLedgerDetails subledgerDB : subledgerfromDB) {
			boolean found = false;
			for (BusinessAccountSubLedgerDetails subledgerModel : listModelAccountSubledger) {
				if (subledgerDB.getId().equals(subledgerModel.getId())) {
					found = true;
					break;
				}
			}
			if (!found)
				subledgerDetailsToBeDeletedFromDB.add(subledgerDB);

		}
		return subledgerDetailsToBeDeletedFromDB;
	}

	private void updateSubledgerDetails(BusinessAccountSubLedgerDetails subledgerModel) {
		Object obj[] = new Object[] { subledgerModel.getAmount(), subledgerModel.getBusinessAccountDetail().getId(),
				subledgerModel.getAccountDetailKey(), subledgerModel.getAccountDetailType(),
				subledgerModel.getTenantId(), subledgerModel.getId() };
		jdbcTemplate.update(UPDATE_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS, obj);
	}

	private boolean subledgerNeedsUpdate(BusinessAccountSubLedgerDetails subledgerModel,
			List<BusinessAccountSubLedgerDetails> subledgerfromDB) {
		for (BusinessAccountSubLedgerDetails subledgeDB : subledgerfromDB) {
			if (subledgerModel.equals(subledgeDB))
				return false;
		}
		return true;
	}

	private void insertSubledgerDetails(BusinessAccountSubLedgerDetails subledgerModel,
			HashMap<Long, Long> mapOfInsertedIdsInModelAndInDB) {
		Long businessDetailsId;

		if (mapOfInsertedIdsInModelAndInDB.containsKey(subledgerModel.getBusinessAccountDetail().getId())) {
			for (Map.Entry<Long, Long> entry : mapOfInsertedIdsInModelAndInDB.entrySet()) {
				if (entry.getKey().equals(subledgerModel.getBusinessAccountDetail().getId())) {
					businessDetailsId = entry.getValue();

					Object obj[] = new Object[] { generateSequence(SEQUENCEFORSUBLEDGER), subledgerModel.getAmount(),
							businessDetailsId, subledgerModel.getAccountDetailKey(),
							subledgerModel.getAccountDetailType(), subledgerModel.getTenantId() };
					jdbcTemplate.update(INSERT_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS, obj);

				}
			}
		}

		else {
			businessDetailsId = subledgerModel.getBusinessAccountDetail().getId();
			Object obj[] = new Object[] { generateSequence("SEQ_EG_BUSINESS_SUBLEDGERINFO"), subledgerModel.getAmount(),
					businessDetailsId, subledgerModel.getAccountDetailKey(), subledgerModel.getAccountDetailType(),
					subledgerModel.getTenantId() };
			jdbcTemplate.update(INSERT_BUSINESS_ACCOUNT_SUBLEDGER_DETAILS, obj);
		}
	}

	private boolean subledgerNeedsInsert(BusinessAccountSubLedgerDetails subledgerModel,
			List<BusinessAccountSubLedgerDetails> subledgerfromDB) {
		for (BusinessAccountSubLedgerDetails subledgerDB : subledgerfromDB) {
			if (subledgerModel.getId().equals(subledgerDB.getId()))
				return false;
		}
		return true;
	}

	private void deleteAccountDetails(List<Long> ids, String tenantId) {
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("id", ids);
		namedParameters.put(TENANT, tenantId);
		namedParameterJdbcTemplate.update(DELETE_BUSINESS_ACCOUNT_DETAILS, namedParameters);

	}

	private List<BusinessAccountDetails> deleteAccountDetailsFromDBIfNotPresentInInput(
			List<BusinessAccountDetails> listModelAccountSubledger, List<BusinessAccountDetails> accountDetailsFromDB) {
		List<BusinessAccountDetails> accountDetailsToBeDeletedFromDB = new ArrayList<>();

		for (BusinessAccountDetails dbDetail : accountDetailsFromDB) {
			boolean found = false;
			for (BusinessAccountDetails modelDetail : listModelAccountSubledger) {

				if (dbDetail.getId().equals(modelDetail.getId())) {
					found = true;
					break;
				}
			}
			if (!found)
				accountDetailsToBeDeletedFromDB.add(dbDetail);

		}
		return accountDetailsToBeDeletedFromDB;
	}

	private void updateAccountDetails(BusinessAccountDetails accountdetail) {
		Object[] object = new Object[] { accountdetail.getBusinessDetails().getId(), accountdetail.getChartOfAccount(),
				accountdetail.getAmount(), accountdetail.getTenantId(), accountdetail.getId() };
		jdbcTemplate.update(UPDATE_BUSINESS_ACCOUNT_DETAILS, object);
	}

	private boolean needsUpdate(BusinessAccountDetails accountdetail,
			List<BusinessAccountDetails> accountDetailsFromDB) {
		for (BusinessAccountDetails accountDetailFromDB : accountDetailsFromDB) {
			if (accountDetailFromDB.equals(accountdetail))
				return false;
		}
		return true;
	}

	private boolean needsInsert(BusinessAccountDetails accountdetail,
			List<BusinessAccountDetails> accountDetailsFromDB) {
		for (BusinessAccountDetails dbAccountDetail : accountDetailsFromDB) {
			if (accountdetail.getId().equals(dbAccountDetail.getId()))
				return false;
		}
		return true;
	}

	public BusinessDetailsCommonModel getForCriteria(BusinessDetailsCriteria detailsCriteria) {
		List<Object> preparedStatementValues = new ArrayList<>();
		String qryString = businessDetailsQueryBuilder.getQuery(detailsCriteria, preparedStatementValues);
		List<BusinessDetails> details = jdbcTemplate.query(qryString, preparedStatementValues.toArray(),
				businessDetailsCombinedRowMapper);
		List<BusinessAccountDetails> accountDetails = new ArrayList<>();
		List<BusinessAccountSubLedgerDetails> subledgerDetails = new ArrayList<>();
		for (BusinessDetails detail : details) {
			accountDetails.add(detail.getAccountDetails().get(0));
		}
		for (BusinessAccountDetails accountDetail : accountDetails) {
			subledgerDetails.add(accountDetail.getSubledgerDetails().get(0));
		}
		List<BusinessDetails> uniqueBusinessDetails = details.stream().filter(distinctByKey(p -> p.getId()))
				.collect(Collectors.toList());
		List<BusinessAccountDetails> uniqueBusinessAccountDetails = accountDetails.stream()
				.filter(accountdetail -> accountdetail.getId() != null)
				.collect(collectingAndThen(
						toCollection(() -> new TreeSet<>(comparingLong(BusinessAccountDetails::getId))),
						ArrayList::new));
		List<BusinessAccountSubLedgerDetails> uniqueBusinessAccountSubledgerDetails = subledgerDetails.stream()
				.filter(subledgerdetail -> subledgerdetail.getId() != null)
				.collect(collectingAndThen(
						toCollection(() -> new TreeSet<>(comparingLong(BusinessAccountSubLedgerDetails::getId))),
						ArrayList::new));

		List<BusinessDetails> businessDetails = uniqueBusinessDetails.stream()
				.map(unqdetail -> unqdetail.toDomainModel()).collect(Collectors.toList());
		List<BusinessAccountDetails> businessAccountDetails = uniqueBusinessAccountDetails.stream()
				.map(unqAccDetail -> unqAccDetail.toDomainModel()).collect(Collectors.toList());
		List<BusinessAccountSubLedgerDetails> businessAccountSubledger = uniqueBusinessAccountSubledgerDetails.stream()
				.map(unqAccountDetail -> unqAccountDetail.toDomainModel()).collect(Collectors.toList());
		return new BusinessDetailsCommonModel(businessDetails, businessAccountDetails, businessAccountSubledger);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public boolean checkDetailsByNameAndTenantIdExists(String name, String tenantId, Long id, Boolean isUpdate) {
		final List<Object> preparedStatementValue = new ArrayList<Object>();
		preparedStatementValue.add(name);
		preparedStatementValue.add(tenantId);
		List<BusinessDetails> detailsFromDb = new ArrayList<>();
		List<Object> preparedStatementValues = new ArrayList<Object>();
		preparedStatementValues.add(name);
		preparedStatementValues.add(tenantId);
		preparedStatementValues.add(id);

		if (isUpdate)
			detailsFromDb = jdbcTemplate.query(GET_DETAILS_BY_NAME_TENANTID_AND_ID, preparedStatementValues.toArray(),
					businessDetailsRowMapper);
		else
			detailsFromDb = jdbcTemplate.query(GET_DETAILS_BY_NAME_AND_TENANTID, preparedStatementValue.toArray(),
					businessDetailsRowMapper);
		if (!detailsFromDb.isEmpty())
			return false;
		else
			return true;

	}

	public boolean checkDetailsByCodeAndTenantIdExists(String code, String tenantId, Long id, Boolean isUpdate) {
		final List<Object> preparedStatementValue = new ArrayList<Object>();
		preparedStatementValue.add(code);
		preparedStatementValue.add(tenantId);
		List<BusinessDetails> detailsFromDb = new ArrayList<>();
		List<Object> preparedStatementValues = new ArrayList<Object>();
		preparedStatementValues.add(code);
		preparedStatementValues.add(tenantId);
		preparedStatementValues.add(id);

		if (isUpdate)
			detailsFromDb = jdbcTemplate.query(GET_BUSINESSDETAILS_BY_CODE_AND_TENANTID_AND_ID,
					preparedStatementValues.toArray(), businessDetailsRowMapper);
		else
			detailsFromDb = jdbcTemplate.query(GET_BUSINESSDETAILS_BY_CODE_AND_TENANTID,
					preparedStatementValue.toArray(), businessDetailsRowMapper);
		if (!detailsFromDb.isEmpty())
			return false;
		else
			return true;
	}
}
