package org.egov.collection.persistence.repository.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.repository.QueryBuilder.ReceiptDetailQueryBuilder;
import org.junit.Test;

public class ReceiptQueryBuilderTest {
	@Test
	public void no_input_test() {
		ReceiptSearchCriteria receiptCriteria = new ReceiptSearchCriteria();
		ReceiptDetailQueryBuilder receiptQueryBuilder = new ReceiptDetailQueryBuilder();

		assertEquals(
				"Select rh.id as rh_id,rh.payeename as rh_payeename,"
						+ "rh.payeeAddress as rh_payeeAddress,rh.payeeEmail as rh_payeeEmail,rh.paidBy as rh_paidBy,"
						+ "rh.referenceNumber as rh_referenceNumber,rh.referenceDate as rh_referenceDate,"
						+ "rh.receiptType as rh_receiptType,rh.receiptNumber as rh_receiptNumber,rh.receiptDate"
						+ " as rh_receiptDate,rh.referenceDesc as rh_referenceDesc,rh.manualReceiptNumber"
						+ " as rh_manualReceiptNumber,rh.manualReceiptDate as rh_manualReceiptDate,"
						+ "rh.businessDetails as rh_businessDetails,rh.collectionType"
						+ " as rh_collectionType,rh.stateId as rh_stateId,rh.location as rh_location,"
						+ "rh.isReconciled as rh_isReconciled,rh.status as rh_status,rh.reasonForCancellation"
						+ " as rh_reasonForCancellation,rh.minimumAmount as rh_minimumAmount,rh.totalAmount"
						+ " as rh_totalAmount,rh.collModesNotAllwd as rh_collModesNotAllwd,"
						+ "rh.consumerCode as rh_consumerCode,rh.function as rh_function,"
						+ "rh.version as rh_version,rh.channel as rh_channel,rh.reference_ch_id as rh_reference_ch_id,rh.consumerType"
						+ " as rh_consumerType,rh.fund as rh_fund,rh.fundSource as rh_fundSource,"
						+ "rh.boundary as rh_boundary,rh.department as rh_department,rh.depositedBranch"
						+ " as rh_depositedBranch,rh.tenantId as rh_tenantId,rh.displayMsg as rh_displayMsg,"
						+ "rh.voucherheader as rh_voucherheader,"
						+ "rh.createdBy as rh_createdBy,rh.createdDate as rh_createdDate,"
						+ "rh.lastModifiedBy as rh_lastModifiedBy,rh.lastModifiedDate as rh_lastModifiedDate,"
						+ "rd.id as rd_id,rd.receiptHeader as rh_id,"
						+ "rd.dramount as rd_dramount,rd.cramount as rd_cramount,rd.actualcramountToBePaid as "
						+ "rd_actualcramountToBePaid,rd.ordernumber as rd_ordernumber,"
						+ "rd.description as rd_description,rd.chartOfAccount as rd_chartOfAccount,rd.isActualDemand"
						+ " as rd_isActualDemand,rd.financialYear as rd_financialYear,rd.purpose as rd_purpose,"
						+ "rd.tenantId as rd_tenantId"
						+ " from egcl_receiptheader rh FULL JOIN egcl_receiptdetails rd ON"
						+ " rh.id=rd.receiptHeader ORDER BY rh.receiptDate DESC",
				receiptQueryBuilder.getQuery(receiptCriteria, new ArrayList<>()));
	}

	@Test
	public void all_input_test() {
		ReceiptSearchCriteria receiptCriteria = new ReceiptSearchCriteria();
		ReceiptDetailQueryBuilder receiptQueryBuilder = new ReceiptDetailQueryBuilder();
		receiptCriteria.setBusinessCode("TL");
		receiptCriteria.setCollectedBy("1");
		receiptCriteria.setConsumerCode("CONSUMER123");
		receiptCriteria.setFromDate("2016-12-22 00:00:00");
		receiptCriteria.setToDate("2017-12-22 00:00:00");
		receiptCriteria.setStatus("CREATED");
		receiptCriteria.setTenantId("default");
		receiptCriteria.setSortBy("payeename");
		receiptCriteria.setSortOrder("DESC");
		receiptCriteria.setReceiptNumbers(Arrays.asList("RECNO567"));
		assertEquals("Select rh.id as rh_id,rh.payeename as rh_payeename,"
						+ "rh.payeeAddress as rh_payeeAddress,rh.payeeEmail as rh_payeeEmail,rh.paidBy as rh_paidBy,"
						+ "rh.referenceNumber as rh_referenceNumber,rh.referenceDate as rh_referenceDate,"
						+ "rh.receiptType as rh_receiptType,rh.receiptNumber as rh_receiptNumber,rh.receiptDate"
						+ " as rh_receiptDate,rh.referenceDesc as rh_referenceDesc,rh.manualReceiptNumber"
						+ " as rh_manualReceiptNumber,rh.manualReceiptDate as rh_manualReceiptDate,"
						+ "rh.businessDetails as rh_businessDetails,rh.collectionType"
						+ " as rh_collectionType,rh.stateId as rh_stateId,rh.location as rh_location,"
						+ "rh.isReconciled as rh_isReconciled,rh.status as rh_status,rh.reasonForCancellation"
						+ " as rh_reasonForCancellation,rh.minimumAmount as rh_minimumAmount,rh.totalAmount"
						+ " as rh_totalAmount,rh.collModesNotAllwd as rh_collModesNotAllwd,"
						+ "rh.consumerCode as rh_consumerCode,rh.function as rh_function,"
						+ "rh.version as rh_version,rh.channel as rh_channel,rh.reference_ch_id as rh_reference_ch_id,rh.consumerType"
						+ " as rh_consumerType,rh.fund as rh_fund,rh.fundSource as rh_fundSource,"
						+ "rh.boundary as rh_boundary,rh.department as rh_department,rh.depositedBranch"
						+ " as rh_depositedBranch,rh.tenantId as rh_tenantId,rh.displayMsg as rh_displayMsg,"
						+ "rh.voucherheader as rh_voucherheader,"
						+ "rh.createdBy as rh_createdBy,rh.createdDate as rh_createdDate,"
						+ "rh.lastModifiedBy as rh_lastModifiedBy,rh.lastModifiedDate as rh_lastModifiedDate,"
						+ "rd.id as rd_id,rd.receiptHeader as rh_id,"
						+ "rd.dramount as rd_dramount,rd.cramount as rd_cramount,rd.actualcramountToBePaid as "
						+ "rd_actualcramountToBePaid,rd.ordernumber as rd_ordernumber,"
						+ "rd.description as rd_description,rd.chartOfAccount as rd_chartOfAccount,rd.isActualDemand"
						+ " as rd_isActualDemand,rd.financialYear as rd_financialYear,rd.purpose as rd_purpose,"
						+ "rd.tenantId as rd_tenantId"
						+ " from egcl_receiptheader rh FULL JOIN egcl_receiptdetails rd ON"
						+ " rh.id=rd.receiptHeader WHERE rh.tenantId = ? AND rh.receiptNumber IN ('RECNO567') AND "
						+ "rh.consumerCode = ? AND rh.status = ? AND rh.createdBy = ? AND"
						+ " rh.receiptDate >= ? AND rh.receiptDate <= ? AND "
						+ "rh.businessDetails = ? ORDER BY rh.payeename DESC",receiptQueryBuilder.getQuery(receiptCriteria
								, new ArrayList<>()) );
		
		
		
		
	}
}
