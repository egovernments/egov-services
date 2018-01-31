package org.egov.lams.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.egov.lams.model.AgreementCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class AgreementQueryBuilder {

    public static final Logger logger = LoggerFactory.getLogger(AgreementQueryBuilder.class);

    public static final String RENT_INCREMENT_TYPE_QUERY = "SELECT * FROM eglams_rentincrementtype rent WHERE rent.id = :rentId";

    public static final String AGREEMENT_QUERY = "SELECT id FROM eglams_agreement agreement WHERE "
            + "agreement.acknowledgementnumber = :acknowledgementNumber OR agreement.agreement_no = :agreementNumber";

    public static final String AGREEMENT_BY_ASSET_QUERY = "SELECT *,agreement.id as lamsagreementid  FROM eglams_agreement agreement WHERE "
            + "agreement.asset = :assetId";

    public static final String BASE_SEARCH_QUERY = "SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement "
            + "LEFT OUTER JOIN eglams_demand demand ON agreement.id = demand.agreementid "
            + "LEFT OUTER JOIN eglams_rentincrementtype rent ON agreement.rent_increment_method = rent.id";

	public static final String INSERT_AGREEMENT_QUERY = "INSERT INTO eglams_agreement (id,Agreement_Date,Agreement_No,Bank_Guarantee_Amount,Bank_Guarantee_Date,Case_No,Commencement_Date,Council_Date,Council_Number,Expiry_Date,Nature_Of_Allotment,Order_Date,Order_Details,Order_No,Payment_Cycle,Registration_Fee,Remarks,Rent,Rr_Reading_No,Security_Deposit,Security_Deposit_Date,solvency_certificate_date,solvency_certificate_no,status,tin_number,Tender_Date,Tender_Number,Trade_license_Number,created_by,last_modified_by,created_date,last_modified_date,allottee,asset,Rent_Increment_Method,AcknowledgementNumber,stateid,Tenant_id,goodwillamount,timeperiod,collectedsecuritydeposit,collectedgoodwillamount,source,reason,terminationDate,courtReferenceNumber,action,courtcase_no,courtcase_date,courtfixed_rent,effective_date,judgement_no,judgement_date,judgement_rent,remission_fee,remission_from_date,remission_to_date,remission_order_no,adjustment_start_date,is_under_workflow,first_allotment,gstin,municipal_order_no,municipal_order_date,govt_order_no,govt_order_date, renewal_date, base_allotment, res_category, old_agreement_no, referenceno, floorno)"
			+ " values (:agreementID,:agreementDate,:agreementNo,:bankGuaranteeAmount,:bankGuaranteeDate,:caseNo,:commencementDate,:councilDate,:councilNumber,:expiryDate,:natureOfAllotment,:orderDate,:orderDetails,:orderNumber,:paymentCycle,:registrationFee,:remarks,:rent,:rrReadingNo,:securityDeposit,:securityDepositDate,:solvencyCertificateDate,:solvencyCertificateNo,:status,:tinNumber,:tenderDate,:tenderNumber,:tradelicenseNumber,:createdBy,:lastmodifiedBy,:createdDate,:lastmodifiedDate,:allottee,:asset,:rentIncrement,:acknowledgementNumber,:stateId,:tenantId,:goodWillAmount,:timePeriod,:collectedSecurityDeposit,:collectedGoodWillAmount,:source,:reason,:terminationDate,:courtReferenceNumber,:action,:courtCaseNo,:courtCaseDate,:courtFixedRent,:effectiveDate,:judgementNo,:judgementDate,:judgementRent,:remissionRent,:remissionFromDate,:remissionToDate,:remissionOrder,:adjustmentStartDate,:isUnderWorkflow,:firstAllotment,:gstin,:municaipalOrderNo,:municipalOrderDate,:govtOrderNo,:govtOrderDate,:renewalDate, :basisAllotment, :resCategory, :oldAgreementNumber, :referenceNo,:floorNo)";

	public static final String UPDATE_AGREEMENT_QUERY = "UPDATE eglams_agreement SET Id=:agreementID,Agreement_Date=:agreementDate,Agreement_No=:agreementNo,Bank_Guarantee_Amount=:bankGuaranteeAmount,Bank_Guarantee_Date=:bankGuaranteeDate,Case_No=:caseNo,Commencement_Date=:commencementDate,Council_Date=:councilDate,Council_Number=:councilNumber,Expiry_Date=:expiryDate,Nature_Of_Allotment=:natureOfAllotment,Order_Date=:orderDate,Order_Details=:orderDetails,Order_No=:orderNumber,Payment_Cycle=:paymentCycle,Registration_Fee=:registrationFee,Remarks=:remarks,Rent=:rent,Rr_Reading_No=:rrReadingNo,Security_Deposit=:securityDeposit,Security_Deposit_Date=:securityDepositDate,solvency_certificate_date=:solvencyCertificateDate,solvency_certificate_no=:solvencyCertificateNo,status=:status,tin_number=:tinNumber,Tender_Date=:tenderDate,Tender_Number=:tenderNumber,Trade_license_Number=:tradelicenseNumber,created_by=:createdBy,last_modified_by=:lastmodifiedBy,last_modified_date=:lastmodifiedDate,allottee=:allottee,asset=:asset,Rent_Increment_Method=:rentIncrement,AcknowledgementNumber=:acknowledgementNumber,stateid=:stateId,Tenant_id=:tenantId,goodwillamount=:goodWillAmount,timeperiod=:timePeriod,collectedsecuritydeposit=:collectedSecurityDeposit,collectedgoodwillamount=:collectedGoodWillAmount,source=:source,reason=:reason,terminationDate=:terminationDate,courtReferenceNumber=:courtReferenceNumber,action=:action,courtcase_no=:courtCaseNo,courtcase_date=:courtCaseDate,courtfixed_rent=:courtFixedRent,effective_date=:effectiveDate,judgement_no=:judgementNo,judgement_date=:judgementDate,judgement_rent=:judgementRent,remission_fee=:remissionRent,remission_from_date=:remissionFromDate,remission_to_date=:remissionToDate,remission_order_no=:remissionOrder,adjustment_start_date=:adjustmentStartDate,is_under_workflow=:isUnderWorkflow,first_allotment=:firstAllotment,gstin=:gstin,municipal_order_no=:municaipalOrderNo,municipal_order_date=:municipalOrderDate,govt_order_no=:govtOrderNo,govt_order_date=:govtOrderDate,renewal_date=:renewalDate,base_allotment=:basisAllotment,res_category=:resCategory, old_agreement_no=:oldAgreementNumber, referenceno=:referenceNo, floorno=:floorNo"
			+ " WHERE acknowledgementNumber=:acknowledgementNumber and Tenant_id=:tenantId ";

    public static String getAgreementSearchQuery(AgreementCriteria agreementsModel,
                                                 @SuppressWarnings("rawtypes") Map params) {
        StringBuilder selectQuery = new StringBuilder(BASE_SEARCH_QUERY);
        appendParams(agreementsModel, params, selectQuery);
        appendLimitAndOffset(agreementsModel, params, selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings("unchecked")
    private static void appendParams(AgreementCriteria agreementsModel,
                                     @SuppressWarnings("rawtypes") Map params, StringBuilder selectQuery) {

        selectQuery.append(" WHERE agreement.id is not null");

        if (agreementsModel.getAgreementId() != null) {
            selectQuery.append(" and AGREEMENT.ID IN (:agreementId)");
            params.put("agreementId", agreementsModel.getAgreementId());
        }

        if (agreementsModel.getAgreementNumber() != null) {
            selectQuery.append(" and AGREEMENT.AGREEMENT_NO = :agreementNumber");
            params.put("agreementNumber", agreementsModel.getAgreementNumber());
        }

        if (agreementsModel.getStatus() != null) {
            selectQuery.append(" and AGREEMENT.STATUS = :status");
            params.put("status", agreementsModel.getStatus().toString());
        } else {
            selectQuery.append(" and AGREEMENT.STATUS IN ('ACTIVE','WORKFLOW','RENEWED','REJECTED')");
        }

        if (agreementsModel.getTenantId() != null) {
            selectQuery.append(" and AGREEMENT.TENANT_ID = :tenantId");
            params.put("tenantId", agreementsModel.getTenantId());
        }

        if (agreementsModel.getTenderNumber() != null) {
            selectQuery.append(" and AGREEMENT.TENDER_NUMBER = :tenderNumber");
            params.put("tenderNumber", agreementsModel.getTenderNumber());
        }

        if (agreementsModel.getTinNumber() != null) {
            selectQuery.append(" and AGREEMENT.TIN_NUMBER = :tinNumber");
            params.put("tinNumber", agreementsModel.getTinNumber());
        }

        if (agreementsModel.getTradelicenseNumber() != null) {
            selectQuery.append(" and AGREEMENT.TRADE_LICENSE_NUMBER = :tradeLicenseNumber");
            params.put("tradeLicenseNumber", agreementsModel.getTradelicenseNumber());
        }

        if (agreementsModel.getAcknowledgementNumber() != null) {
            selectQuery.append(" and AGREEMENT.acknowledgementnumber = :acknowledgementnumber");
            params.put("acknowledgementnumber", agreementsModel.getAcknowledgementNumber());
        }

        if (agreementsModel.getStateId() != null) {
            selectQuery.append(" and AGREEMENT.stateid = :stateId");
            params.put("stateId", agreementsModel.getStateId());
        }

        if (agreementsModel.getAsset() != null) {
            selectQuery.append(" and AGREEMENT.ASSET IN (:assetId)");
            params.put("assetId", agreementsModel.getAsset());
        }

        if (agreementsModel.getAllottee() != null) {
            selectQuery.append(" and AGREEMENT.ALLOTTEE IN (:allottee)");
            params.put("allottee", agreementsModel.getAllottee());
        }

		if (agreementsModel.getReferenceNumber() != null) {
			selectQuery.append(" and AGREEMENT.referenceno = :referenceNo");
			params.put("referenceNo", agreementsModel.getReferenceNumber());
		}
		
		if(StringUtils.isNotBlank(agreementsModel.getOldAgreementNumber())){
			selectQuery.append(" and AGREEMENT.old_agreement_no = :oldAgreementNumber");
			params.put("oldAgreementNumber", agreementsModel.getOldAgreementNumber());
		}

        if (agreementsModel.getFromDate() != null && agreementsModel.getToDate() != null) {

            if (agreementsModel.getToDate().compareTo(agreementsModel.getFromDate()) < 0)
                throw new RuntimeException("ToDate cannot be lesser than fromdate");
            selectQuery.append(" and AGREEMENT.CREATED_DATE >= :fromDate");
            params.put("fromDate", agreementsModel.getFromDate());
            selectQuery.append(" and AGREEMENT.CREATED_DATE <= :toDate");
            params.put("toDate", agreementsModel.getToDate());
        } else if ((agreementsModel.getFromDate() != null && agreementsModel.getToDate() == null) ||
                (agreementsModel.getToDate() != null && agreementsModel.getFromDate() == null))
            throw new RuntimeException("Invalid date Range, please enter Both fromDate and toDate");

    }

    @SuppressWarnings("unchecked")
    private static StringBuilder appendLimitAndOffset(AgreementCriteria agreementsModel,
                                                      @SuppressWarnings("rawtypes") Map params, StringBuilder selectQuery) {

        selectQuery.append(" ORDER BY AGREEMENT.ID");
        selectQuery.append(" LIMIT :pageSize");
        if (agreementsModel.getSize() != null)
            params.put("pageSize", agreementsModel.getSize());
        else
            params.put("pageSize", 500);

        selectQuery.append(" OFFSET :pageNumber");

        if (agreementsModel.getOffSet() != null)
            params.put("pageNumber", agreementsModel.getOffSet());
        else
            params.put("pageNumber", 0);
        logger.info("the complete select query for agreement search : " + selectQuery.toString());

        return selectQuery;
    }
    
    public static String getAgreementIdQuery(final List<Long> idList) {
        StringBuilder query = null;
        if (!idList.isEmpty()) {
            query = new StringBuilder(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append("," + idList.get(i));
        }
        return query.append(")").toString();
    }

}