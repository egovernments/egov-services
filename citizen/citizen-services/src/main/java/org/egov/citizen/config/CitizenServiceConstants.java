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
package org.egov.citizen.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CitizenServiceConstants {

	public static final String INVALID_Service_REQUEST = "Service create request is invalid";

	public static final String TENANT_ID_MISSING_CODE = "egs_001";
	public static final String TENANT_ID_MISSING_FIELD = "tenantId";
	public static final String TENANT_ID_MISSING_MESSAGE = "Tenant id mising";

	public static final String DEMANDS_NOT_EXIST_CODE = "egs_002";
	public static final String PAYEE_NAME_MISSING_MESSAGE = "Demands Not Exist";
	
	
	public static final String BILL_INVALID_MSG = "Bill couln't be fetched";
	public static final String BILL_INVALID_DESC = "Billing service didn't return generated bill for the payment";
	
	public static final String RCPT_INVALID_MSG = "Receipt couldn't be created";
	public static final String RCPT_INVALID_DESC = "Receipt creation failed at the collection service, receipt couldn't be returned";
	
	public static final String FAIL_STATUS_MSG = "Payment failed";
	public static final String FAIL_STATUS_DESC = "Payment gateway returned an invalid hash for your transaction, "
					+ "receipt will not be generated";
	
	public static final String BILL_GEN_FAIL_MSG = "Bill couln't be generated";
	public static final String BILL_GEN_FAIL_DESC = "Billing service couldn't generate bill for the demand";

	public static final String SVCREQ_NOT_FOUND_MSG = "Service Requests not found";
	public static final String SVCREQ_NOT_FOUND_DESC = "Service Requests couldn't be fetched for the criteria";
	
	public static final String KAFKA_PUSH_FAIL_MSG = "Object couldn't be pushed to the kafka queue";
	public static final String KAFKA_PUSH_FAIL_DESC = "Try again by checking the request and the kafka connection";
	
	public static final String DUES_INVALID_MSG = "Dues couln't be fetched";
	public static final String DUES_INVALID_DESC = "Billing service didn't return updated dues after collection";
	
	public static final String DEMAND_NOT_CREATED_MSG = "Demand couldn't be created";
	public static final String DEMAND_NOT_CREATED_DESC = "Demand creation for application fee failed at the billing service";
}

