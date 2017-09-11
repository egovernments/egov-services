package org.egov.lams.repository.builder;

import java.util.List;
import java.util.Set;

import org.egov.lams.model.AgreementCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgreementQueryBuilder {

	public static final Logger logger = LoggerFactory.getLogger(AgreementQueryBuilder.class);

	public static final String RENT_INCREMENT_TYPE_QUERY = "SELECT * FROM eglams_rentincrementtype rent WHERE rent.id=?";

	public static final String AGREEMENT_QUERY = "SELECT id FROM eglams_agreement agreement WHERE "
			+ "agreement.acknowledgementnumber=? OR agreement.agreement_no=?";
	
	public static final String AGREEMENT_BY_ASSET_QUERY = "SELECT *,agreement.id as lamsagreementid  FROM eglams_agreement agreement WHERE "
			+ "agreement.asset=?";
	
	public static final String BASE_SEARCH_QUERY =  "SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement "
			+ "LEFT OUTER JOIN eglams_demand demand ON agreement.id=demand.agreementid "
			+ "LEFT OUTER JOIN eglams_rentincrementtype rent ON agreement.rent_increment_method=rent.id";

	public static final String INSERT_AGREEMENT_QUERY = "INSERT INTO eglams_agreement (id,Agreement_Date,Agreement_No,Bank_Guarantee_Amount,Bank_Guarantee_Date,Case_No,Commencement_Date,Council_Date,Council_Number,Expiry_Date,Nature_Of_Allotment,Order_Date,Order_Details,Order_No,Payment_Cycle,Registration_Fee,Remarks,Rent,Rr_Reading_No,Security_Deposit,Security_Deposit_Date,solvency_certificate_date,solvency_certificate_no,status,tin_number,Tender_Date,Tender_Number,Trade_license_Number,created_by,last_modified_by,created_date,last_modified_date,allottee,asset,Rent_Increment_Method,AcknowledgementNumber,stateid,Tenant_id,goodwillamount,timeperiod,collectedsecuritydeposit,collectedgoodwillamount,source,reason,terminationDate,courtReferenceNumber,action,courtcase_no,courtcase_date,courtfixed_rent,effective_date,judgement_no,judgement_date,judgement_rent)"
			+ " values (:agreementID,:agreementDate,:agreementNo,:bankGuaranteeAmount,:bankGuaranteeDate,:caseNo,:commencementDate,:councilDate,:councilNumber,:expiryDate,:natureOfAllotment,:orderDate,:orderDetails,:orderNumber,:paymentCycle,:registrationFee,:remarks,:rent,:rrReadingNo,:securityDeposit,:securityDepositDate,:solvencyCertificateDate,:solvencyCertificateNo,:status,:tinNumber,:tenderDate,:tenderNumber,:tradelicenseNumber,:createdBy,:lastmodifiedBy,:createdDate,:lastmodifiedDate,:allottee,:asset,:rentIncrement,:acknowledgementNumber,:stateId,:tenantId,:goodWillAmount,:timePeriod,:collectedSecurityDeposit,:collectedGoodWillAmount,:source,:reason,:terminationDate,:courtReferenceNumber,:action,:courtCaseNo,:courtCaseDate,:courtFixedRent,:effectiveDate,:judgementNo,:judgementDate,:judgementRent)";

	public static final String UPDATE_AGREEMENT_QUERY = "UPDATE eglams_agreement SET Id=:agreementID,Agreement_Date=:agreementDate,Agreement_No=:agreementNo,Bank_Guarantee_Amount=:bankGuaranteeAmount,Bank_Guarantee_Date=:bankGuaranteeDate,Case_No=:caseNo,Commencement_Date=:commencementDate,Council_Date=:councilDate,Council_Number=:councilNumber,Expiry_Date=:expiryDate,Nature_Of_Allotment=:natureOfAllotment,Order_Date=:orderDate,Order_Details=:orderDetails,Order_No=:orderNumber,Payment_Cycle=:paymentCycle,Registration_Fee=:registrationFee,Remarks=:remarks,Rent=:rent,Rr_Reading_No=:rrReadingNo,Security_Deposit=:securityDeposit,Security_Deposit_Date=:securityDepositDate,solvency_certificate_date=:solvencyCertificateDate,solvency_certificate_no=:solvencyCertificateNo,status=:status,tin_number=:tinNumber,Tender_Date=:tenderDate,Tender_Number=:tenderNumber,Trade_license_Number=:tradelicenseNumber,created_by=:createdBy,created_date=:createdDate,last_modified_by=:lastmodifiedBy,last_modified_date=:lastmodifiedDate,allottee=:allottee,asset=:asset,Rent_Increment_Method=:rentIncrement,AcknowledgementNumber=:acknowledgementNumber,stateid=:stateId,Tenant_id=:tenantId,goodwillamount=:goodWillAmount,timeperiod=:timePeriod,collectedsecuritydeposit=:collectedSecurityDeposit,collectedgoodwillamount=:collectedGoodWillAmount,source=:source,reason=:reason,terminationDate=:terminationDate,courtReferenceNumber=:courtReferenceNumber,action=:action,courtcase_no=:courtCaseNo,courtcase_date=:courtCaseDate,courtfixed_rent=:courtFixedRent,effective_date=:effectiveDate,judgement_no=:judgementNo,judgement_date=:judgementDate,judgement_rent=:judgementRent"
			+ " WHERE acknowledgementNumber=:acknowledgementNumber and Tenant_id=:tenantId ";

	public static String getAgreementSearchQuery(AgreementCriteria agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(BASE_SEARCH_QUERY);

		if (!(agreementsModel.getAgreementId() == null && agreementsModel.getAgreementNumber() == null
				&& (agreementsModel.getFromDate() == null && agreementsModel.getToDate() == null)
				&& agreementsModel.getStatus() == null && agreementsModel.getTenderNumber() == null
				&& agreementsModel.getTinNumber() == null && agreementsModel.getTradelicenseNumber() == null
				&& agreementsModel.getAllottee() == null && agreementsModel.getAsset() == null
				&& agreementsModel.getTenantId() == null && agreementsModel.getAcknowledgementNumber() == null
				&& agreementsModel.getStateId() == null)) {
			
			appendParams(agreementsModel, preparedStatementValues, selectQuery);
		}
		appendLimitAndOffset(agreementsModel, preparedStatementValues, selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings("unchecked")
	private static void appendParams(AgreementCriteria agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues, StringBuilder selectQuery) {

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (agreementsModel.getAgreementId() != null) {
			selectQuery.append(" AGREEMENT.ID IN (" + getIdQuery(agreementsModel.getAgreementId()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
		}

		if (agreementsModel.getAgreementNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.AGREEMENT_NO=?");
			preparedStatementValues.add(agreementsModel.getAgreementNumber());
		}

		if (agreementsModel.getStatus() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.STATUS=?");
			preparedStatementValues.add(agreementsModel.getStatus().toString());
		}else{
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.STATUS IN ('ACTIVE','WORKFLOW','RENEWED','REJECTED')");
		}

		if (agreementsModel.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.TENANT_ID=?");
			preparedStatementValues.add(agreementsModel.getTenantId());
		}

		if (agreementsModel.getTenderNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.TENDER_NUMBER=?");
			preparedStatementValues.add(agreementsModel.getTenderNumber());
		}

		if (agreementsModel.getTinNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.TIN_NUMBER=?");
			preparedStatementValues.add(agreementsModel.getTinNumber());
		}

		if (agreementsModel.getTradelicenseNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.TRADE_LICENSE_NUMBER=?");
			preparedStatementValues.add(agreementsModel.getTradelicenseNumber());
		}

		if (agreementsModel.getAcknowledgementNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.acknowledgementnumber=?");
			preparedStatementValues.add(agreementsModel.getAcknowledgementNumber());
		}

		if (agreementsModel.getStateId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.stateid=?");
			preparedStatementValues.add(agreementsModel.getStateId());
		}

		if (agreementsModel.getAsset() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.ASSET IN (" + getIdQuery(agreementsModel.getAsset()));
		}

		if (agreementsModel.getAllottee() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.ALLOTTEE IN (" + getIdQuery(agreementsModel.getAllottee()));
		}

		if (agreementsModel.getFromDate() != null && agreementsModel.getToDate() != null) {

			if (agreementsModel.getToDate().compareTo(agreementsModel.getFromDate()) < 0)
				throw new RuntimeException("ToDate cannot be lesser than fromdate");
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.CREATED_DATE>=?");
			preparedStatementValues.add(agreementsModel.getFromDate());
			addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.CREATED_DATE<=?");
			preparedStatementValues.add(agreementsModel.getToDate());
		} else if (agreementsModel.getFromDate() == null || agreementsModel.getToDate() == null)
			throw new RuntimeException("Invalid date Range, please enter Both fromDate and toDate");

	}

	@SuppressWarnings("unchecked")
	private static StringBuilder appendLimitAndOffset(AgreementCriteria agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues, StringBuilder selectQuery) {

		selectQuery.append(" ORDER BY AGREEMENT.ID");
		selectQuery.append(" LIMIT ?");
		if (agreementsModel.getSize() != null)
			preparedStatementValues.add(agreementsModel.getSize());
		else
			preparedStatementValues.add(500);

		selectQuery.append(" OFFSET ?");

		if (agreementsModel.getOffSet() != null)
			preparedStatementValues.add(agreementsModel.getOffSet());
		else
			preparedStatementValues.add(0);
		logger.info("the complete select query for agreement search : " + selectQuery.toString());

		return selectQuery;
	}

	private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {

		if (appendAndClauseFlag) {
			queryString.append(" AND");
		}
		return true;
	}

	private static String getIdQuery(Set<Long> idList) {

		StringBuilder query = null;
		if (idList.size() >= 1) {

			Long[] list = idList.toArray(new Long[idList.size()]);
			query = new StringBuilder(list[0].toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + list[i]);
			}
		}
		return query.append(")").toString();
	}
}