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
package org.egov.collection.persistence.repository.builder;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.repository.querybuilder.ReceiptDetailQueryBuilder;
import org.junit.Test;

public class ReceiptQueryBuilderTest {
	/*@Test
	public void no_input_test_for_select() throws ParseException {
		ReceiptSearchCriteria receiptCriteria = new ReceiptSearchCriteria();
		ReceiptDetailQueryBuilder receiptQueryBuilder = new ReceiptDetailQueryBuilder();

		assertEquals("Select rh.id as rh_id,rh.payeename as rh_payeename,"
				+ "rh.payeeAddress as rh_payeeAddress,rh.payeeEmail as rh_payeeEmail,rh.paidBy as rh_paidBy,"
				+ "rh.referenceNumber as rh_referenceNumber,rh.referenceDate as rh_referenceDate,"
				+ "rh.receiptType as rh_receiptType,rh.receiptNumber as rh_receiptNumber,rh.receiptDate"
				+ " as rh_receiptDate,rh.referenceDesc as rh_referenceDesc,rh.manualReceiptNumber"
				+ " as rh_manualReceiptNumber, rh.businessDetails as rh_businessDetails,rh.collectionType"
				+ " as rh_collectionType,rh.stateId as rh_stateId,rh.location as rh_location,"
				+ "rh.isReconciled as rh_isReconciled,rh.status as rh_status,rh.reasonForCancellation"
				+ " as rh_reasonForCancellation,rh.minimumAmount as rh_minimumAmount,rh.totalAmount"
				+ " as rh_totalAmount,rh.collModesNotAllwd as rh_collModesNotAllwd,"
				+ "rh.consumerCode as rh_consumerCode,rh.function as rh_function,"
				+ "rh.version as rh_version,rh.channel as rh_channel,rh.reference_ch_id as rh_reference_ch_id,rh.consumerType"
				+ " as rh_consumerType,rh.fund as rh_fund,rh.fundSource as rh_fundSource,"
				+ "rh.boundary as rh_boundary,rh.department as rh_department,rh.depositedBranch"
				+ " as rh_depositedBranch,rh.tenantId as rh_tenantId,rh.displayMsg as rh_displayMsg,"
				+ "rh.voucherheader as rh_voucherheader,rh.cancellationRemarks as rh_cancellationRemarks,"
				+ "rh.createdBy as rh_createdBy,rh.createdDate as rh_createdDate,"
				+ "rh.lastModifiedBy as rh_lastModifiedBy,rh.lastModifiedDate as rh_lastModifiedDate,rh.transactionid as rh_transactionid "
                 + " from egcl_receiptheader rh  ORDER BY rh.receiptDate DESC",
				receiptQueryBuilder.getQuery(receiptCriteria, new ArrayList<>()));
	}*/

	/*@Test
	public void all_input_test_for_selelct() throws ParseException {
		ReceiptSearchCriteria receiptCriteria = new ReceiptSearchCriteria();
		ReceiptDetailQueryBuilder receiptQueryBuilder = new ReceiptDetailQueryBuilder();
		receiptCriteria.setBusinessCode("TL");
		receiptCriteria.setCollectedBy("1");
		receiptCriteria.setConsumerCode("CONSUMER123");
		receiptCriteria.setFromDate(1502272932389L);
		receiptCriteria.setToDate(1502280236077L);
		receiptCriteria.setStatus("CREATED");
		receiptCriteria.setTenantId("default");
		receiptCriteria.setSortBy("payeename");
		receiptCriteria.setSortOrder("DESC");
		receiptCriteria.setReceiptNumbers(Arrays.asList("RECNO567"));
		receiptCriteria.setIds(Arrays.asList(1L));
		assertEquals("Select rh.id as rh_id,rh.payeename as rh_payeename,"
				+ "rh.payeeAddress as rh_payeeAddress,rh.payeeEmail as rh_payeeEmail,rh.paidBy as rh_paidBy,"
				+ "rh.referenceNumber as rh_referenceNumber,rh.referenceDate as rh_referenceDate,"
				+ "rh.receiptType as rh_receiptType,rh.receiptNumber as rh_receiptNumber,rh.receiptDate"
				+ " as rh_receiptDate,rh.referenceDesc as rh_referenceDesc,rh.manualReceiptNumber"
				+ " as rh_manualReceiptNumber, rh.businessDetails as rh_businessDetails,rh.collectionType"
				+ " as rh_collectionType,rh.stateId as rh_stateId,rh.location as rh_location,"
				+ "rh.isReconciled as rh_isReconciled,rh.status as rh_status,rh.reasonForCancellation"
				+ " as rh_reasonForCancellation,rh.minimumAmount as rh_minimumAmount,rh.totalAmount"
				+ " as rh_totalAmount,rh.collModesNotAllwd as rh_collModesNotAllwd,"
				+ "rh.consumerCode as rh_consumerCode,rh.function as rh_function,"
				+ "rh.version as rh_version,rh.channel as rh_channel,rh.reference_ch_id as rh_reference_ch_id,rh.consumerType"
				+ " as rh_consumerType,rh.fund as rh_fund,rh.fundSource as rh_fundSource,"
				+ "rh.boundary as rh_boundary,rh.department as rh_department,rh.depositedBranch"
				+ " as rh_depositedBranch,rh.tenantId as rh_tenantId,rh.displayMsg as rh_displayMsg,"
				+ "rh.voucherheader as rh_voucherheader,rh.cancellationRemarks as rh_cancellationRemarks,"
				+ "rh.createdBy as rh_createdBy,rh.createdDate as rh_createdDate,"
				+ "rh.lastModifiedBy as rh_lastModifiedBy,rh.lastModifiedDate as rh_lastModifiedDate,rh.transactionid as rh_transactionid "
				+ " from egcl_receiptheader rh  WHERE rh.tenantId = ? AND rh.receiptNumber ilike any  (array ['%RECNO567%']) AND "
				+ "rh.consumerCode = ? AND rh.status = ? AND rh.createdBy = ? AND"
				+ " rh.receiptDate >= ? AND rh.receiptDate <= ? AND "
				+ "rh.businessDetails = ? AND rh.id IN (1) ORDER BY rh.payeename DESC",
				receiptQueryBuilder.getQuery(receiptCriteria, new ArrayList<>()));

	}*/
	
	/*@Test
	public void no_input_test_for_update(){
		ReceiptDetailQueryBuilder recceiptDetailQueryBuilder=new ReceiptDetailQueryBuilder();
		assertEquals("Update egcl_receiptheader set lastModifiedBy = ? , lastModifiedDate = ? WHERE",recceiptDetailQueryBuilder.getQueryForUpdate(
				null, null, null, null));
	}*/
	
	
	/*@Test
	public void all_input_test_for_update(){
		ReceiptDetailQueryBuilder receiptDetailQueryBuilder=new ReceiptDetailQueryBuilder();
		assertEquals("Update egcl_receiptheader set stateId = ? ,"
				+ " status = ? , lastModifiedBy = ? , lastModifiedDate = ? WHERE id = ? AND tenantId = ?"
				,receiptDetailQueryBuilder.getQueryForUpdate(2L, "SUBMITTED", 1L, "default"));
	}*/
}