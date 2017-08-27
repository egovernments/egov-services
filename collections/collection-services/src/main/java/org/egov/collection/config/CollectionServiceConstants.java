/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CollectionServiceConstants {

	public static final String INVALID_RECEIPT_REQUEST = "Receipt create request is invalid";
	public static final String INVALID_DEPT_REQUEST = "Get departments request is invalid";
	public static final String INVALID_DESIG_REQUEST = "Get designation request is invalid";
	public static final String INVALID_USERS_REQUEST = "Get users request is invalid";
	public static final String INVALID_WF_REQUEST = "Start Workflow request is invalid";
	public static final String INVALID_REQ_DESC = "Some fields entered are either syntactically or logically incorrect";
	public static final String INVALID_BD = "Fund, FundSource, Function, Department or glcode may be invalid.";
	public static final String RECEIPT_FAIL = "Receipt persistence failed due to internal error";
	public static final String RECEIPT_FAIL_DESC = "Receipt persistence failed due to an error while persistence or due to error in other dependent services";
	public static final String BUSINESS_KEY="ReceiptHeader";



	public static final String TENANT_ID_MISSING_CODE = "egcl_001";
	public static final String TENANT_ID_MISSING_FIELD = "tenantId or receiptHeaderId";
	public static final String TENANT_ID_MISSING_MESSAGE = "Tenant id or ReceiptHeader id is mising";

	public static final String PAYEE_NAME_MISSING_CODE = "egcl_002";
	public static final String PAYEE_NAME_MISSING_FIELD = "payeeName";
	public static final String PAYEE_NAME_MISSING_MESSAGE = "Payee Name is mising";

	public static final String PAID_BY_MISSING_CODE = "egcl_003";
	public static final String PAID_BY_MISSING_FIELD = "paidBy";
	public static final String PAID_BY_MISSING_MESSAGE = "Paid by id is mising";

	public static final String RECEIPT_TYPE_MISSING_CODE = "egcl_004";
	public static final String RECEIPT_TYPE_MISSING_FIELD = "receiptType";
	public static final String RECEIPT_TYPE_MISSING_MESSAGE = "Receipt Type can only be: Adhoc, BillBased, Challan";

	public static final String RECEIPT_DATE_MISSING_CODE = "egcl_005";
	public static final String RECEIPT_DATE_MISSING_FIELD = "receiptDate";
	public static final String RECEIPT_DATE_MISSING_MESSAGE = "Receipt Date is mising";

	public static final String BD_CODE_MISSING_CODE = "egcl_006";
	public static final String BD_CODE_MISSING_FIELD = "businessService";
	public static final String BD_CODE_MISSING_MESSAGE = "Business Details Code is mising";

	public static final String COA_MISSING_CODE = "egcl_007";
	public static final String COA_MISSING_FIELD = "glcode";
	public static final String COA_MISSING_MESSAGE = "Chart of Account Code/ GL Code is mising";

	public static final String PURPOSE_MISSING_CODE = "egcl_008";
	public static final String PURPOSE_MISSING_FIELD = "purpose";
	public static final String PURPOSE_MISSING_MESSAGE = "Purpose can only be: ARREAR_AMOUNT, CURRENT_AMOUNT, ADVANCE_AMOUNT"
			+ "ARREAR_LATEPAYMENT_CHARGES, CURRENT_LATEPAYMENT_CHARGES, CHEQUE_BOUNCE_PENALTY, REBATE, OTHERS";

	public static final String COLLECTIONTYPE_MISSING_CODE = "egcl_009";
	public static final String COLLECTIONTYPE_MISSING_FIELD = "collectionType";
	public static final String COLLECTIONTYPE_MISSING_MESSAGE = "Collection type can only be: Counter, Field, Online";
	
	public static final String COLL_MODES_NOT_ALLWD_MISSING_CODE = "egcl_010";
	public static final String COLL_MODES_NOT_ALLWD_MISSING_FIELD = "collectionModesNotAllowed";
	public static final String COLL_MODES_NOT_ALLWD_MISSING_MESSAGE = "Collection Modes Not Allowed is mising";

	public static final String COLL_ID_NAME = "collection.receiptno";
	public static final String COLL_ID_FORMAT = "[cy:MM]/[fy:yyyy-yy]/[SEQ_COLL_RCPT_NUM]";

    public static final String COLL_TRANSACTION_ID_NAME = "collection.transactionno";
    public static final String COLL_TRANSACTION_FORMAT = "[CITY.CODE][d{10}]";

    public static final String STATUS_MISSING_CODE = "egcl_011";
	public static final String STATUS_MISSING_MESSAGE = "status";
	public static final String STATUS_MISSING_FIELD = "Status is missing";

    public static final String COLL_DETAILS_DESCRIPTION_CODE = "egcl_012";
    public static final String COLL_DETAILS_DESCRIPTION_FIELD = "billDescription";
    public static final String COLL_DETAILS_DESCRIPTION_MESSAGE = "Bill details description is required";
    
    public static final String AMT_PAID_NOT_NULL_CODE = "egcl_013";
    public static final String AMT_PAID_NOT_NULL_FIELD = "amountPaid";
    public static final String AMT_PAID_NOT_NULL_MESSAGE = "Amount paid cannot be null";
    
	public static final String RCPT_TYPE_MISSING_CODE = "egcl_014";
	public static final String RCPT_TYPE_MISSING_FIELD = "receiptType";
	public static final String RCPT_TYPE_MISSING_MESSAGE = "Receipt type is missing";
	
	public static final String COLL_TYPE_MISSING_CODE = "egcl_015";
	public static final String COLL_TYPE_MISSING_FIELD = "collectionType";
	public static final String COLL_TYPE_MISSING_MESSAGE = "Collection Type is missing";
    
    public static final String INSTRUMENT_EXCEPTION_MSG="Instrument couldn't be fetched";
    public static final String INSTRUMENT_EXCEPTION_DESC="ISE while trying to fetch instrument id, creation of instrument failed!";
    
    public static final String RCPTNO_EXCEPTION_MSG="Receipt Number couldn't be generated";
    public static final String RCPTNO_EXCEPTION_DESC="ISE while trying to generate receipt no, id gen service failed to return receipt no";

    public static final String BUSINESSDETAILS_EXCEPTION_MSG="Businessdetails couldn't be fetched";
    public static final String BUSINESSDETAILS_EXCEPTION_DESC="ISE while trying to fetch buisness details, common masters failed to return fund, function, department and fundsource";

    public static final String KAFKA_PUSH_EXCEPTION_MSG="Data couldn't be pushed on the kafka queue";
    public static final String KAFKA_PUSH_EXCEPTION_DESC="ISE while pushing to the queue, check if the kafka server is running";
    
    public static final String POSITION_EXCEPTION_MSG="Positions couldn't be fetched";
    public static final String POSITION_EXCEPTION_DESC="ISE while fetching positions for asignee and initator, hr-employees failed to return values";
    
    public static final String ACCOUNT_CODE_EXCEPTION_MSG="glcode couldn't be fetched";
    public static final String ACCOUNT_CODE_EXCEPTION_DESC="ISE while fetching glcode for instrument type, instrument service failed to return values";
    
    public static final String DUPLICATE_RCPT_EXCEPTION_MSG="Duplicate Receipt";
    public static final String DUPLICATE_RCPT_EXCEPTION_DESC="Receipt number already exists, Receipt cannot be created";

    public static final String INVALID_BILL_EXCEPTION_MSG="Invalid Bill";
    public static final String INVALID_BILL_EXCEPTION_DESC="Bill is invalid, Receipt cannot be created,";

    public static final String CUTT_OFF_DATE_CODE = "egcl_0016";
    public static final String CUTT_OFF_DATE_FIELD = "manualReceiptDate";
    public static final String CUTT_OFF_DATE_MESSAGE = "Manual receipt date is greater than the cut-off date ";
    public static final String CUTT_OFF_DATE_MESSAGE_DESC = " for legacy data entry.Please enter proper date";
    
    public  static final String INSTRUMENT_TYPE_CASH = "Cash";
    public  static final String INSTRUMENT_TYPE_ONLINE = "ONLINE";
    
    public static final String INVALID_DATE_EXCEPTION_MSG="From Date is greater than To Date";
    
    public static final String RECEIPT_PREAPPROVED_OR_APPROVED_CONFIG_KEY="RECEIPT_PREAPPROVED_OR_APPROVED";
    
    public static final String PREAPPROVED_CONFIG_VALUE="PREAPPROVED";

    public static final String MANUAL_RECEIPT_DETAILS_REQUIRED_CONFIG_KEY="MANUAL_RECEIPT_DETAILS_REQUIRED_OR_NOT";

    public static final String MANUAL_RECEIPT_DETAILS_CUTOFF_DATE_CONFIG_KEY="CUTOFF_DATE_FOR_MANUAL_RECEIPT_DETAILS";

    public static final String COLLECTION_LEGACY_RECEIPT_CREATOR_ROLE ="Legacy Receipt Creator";
    public static final String COLLECTION_ONLINE_RECEIPT_ROLE ="CITIZEN";
    

}
