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
package org.egov.collection.persistence.repository.rowmapper;

import org.egov.collection.model.ReceiptDetail;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class ReceiptRowMapperTest {
	@Mock
	private ResultSet rs;

	@InjectMocks
	private ReceiptRowMapper receiptRowMapper;

	@Test
	public void test_should_map_result_set_to_entity() throws Exception {
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		when((Long) rs.getObject("rh_id")).thenReturn(1L);
		when(rs.getString("rh_payeename")).thenReturn("ram");
		when(rs.getString("rh_payeeAddress")).thenReturn("abc nagara");
		when(rs.getString("rh_payeeEmail")).thenReturn("abc@gmail.com");
		when(rs.getString("rh_paidBy")).thenReturn("ram");
		when((Long) rs.getObject("rh_reference_ch_id")).thenReturn(23L);
		when(rs.getString("rh_referenceNumber")).thenReturn("REF123");
		when(rs.getString("rh_function")).thenReturn("45");
		when(rs.getLong("rh_version")).thenReturn(0L);
		when(rs.getString("rh_channel")).thenReturn("cha123");
		when(rs.getString("rh_receiptType")).thenReturn("bill based");
		when(rs.getString("rh_receiptNumber")).thenReturn("REC_1234");
		when(rs.getString("rh_referenceDesc")).thenReturn("refde90");
		when(rs.getString("rh_manualReceiptNumber")).thenReturn("MAN567");
		when(rs.getString("rh_businessDetails")).thenReturn("TL");
		when(rs.getString("rh_collectionType")).thenReturn("B");
		when((Long) rs.getObject("rh_stateId")).thenReturn(34L);
		when((Long) rs.getObject("rh_location")).thenReturn(56L);
		when((Boolean) rs.getObject("rh_isReconciled")).thenReturn(true);
		when(rs.getString("rh_status")).thenReturn("CLOSED");
		when(rs.getString("rh_reasonForCancellation")).thenReturn("Documents not proper");
		when((Double) rs.getObject("rh_minimumAmount")).thenReturn(200.00);
		when((Double) rs.getObject("rh_totalAmount")).thenReturn(250.00);
		when(rs.getString("rh_collModesNotAllwd")).thenReturn("billcollection");
		when(rs.getString("rh_consumerCode")).thenReturn("CONS678789");
		when(rs.getString("rh_consumerType")).thenReturn("citizen");
		when(rs.getString("rh_fund")).thenReturn("34");
		when(rs.getString("rh_fundSource")).thenReturn("56");
		when(rs.getString("rh_boundary")).thenReturn("67");
		when(rs.getString("rh_department")).thenReturn("78");
		when(rs.getString("rh_depositedBranch")).thenReturn("ICICI");
		when(rs.getString("rh_tenantId")).thenReturn("default");
		when(rs.getString("rh_displayMsg")).thenReturn("done");
		when(rs.getString("rh_voucherheader")).thenReturn("VOUHEAD456");
		when((Long) rs.getObject("rh_createdBy")).thenReturn(1L);
		when((Long) rs.getObject("rh_lastModifiedBy")).thenReturn(1L);
		when(rs.getString("rh_cancellationRemarks")).thenReturn("receipt payee name not proper");

		when(rs.getTimestamp("rh_referenceDate")).thenReturn(null);
		when(rs.getTimestamp("rh_receiptDate")).thenReturn(null);
		when(rs.getTimestamp("rh_manualReceiptDate")).thenReturn(null);
		when(rs.getTimestamp("rh_createdDate")).thenReturn(null);
		when(rs.getTimestamp("rh_lastModifiedDate")).thenReturn(null);

		when((Long) rs.getObject("rd_id")).thenReturn(1L);
		when(rs.getString("rd_chartOfAccount")).thenReturn("567");
		when((Double) rs.getObject("rd_dramount")).thenReturn(2000.00);
		when((Double) rs.getObject("rd_actualcramountToBePaid")).thenReturn(2500.00);
		when((Double) rs.getObject("rd_cramount")).thenReturn(2000.00);
		when((Long) rs.getObject("rd_ordernumber")).thenReturn(2L);
		when(rs.getString("rd_description")).thenReturn("done");
		when((Boolean) rs.getObject("rd_isActualDemand")).thenReturn(true);
		when(rs.getString("rd_financialYear")).thenReturn("sixteen");
		when(rs.getString("rd_purpose")).thenReturn("creation");
		when(rs.getString("rd_tenantId")).thenReturn("default");
		ReceiptHeader actualReceiptHeader = receiptRowMapper.mapRow(rs, 1);
		ReceiptHeader expectedReceiptHeader = getReceiptHeader();
		assertThat(expectedReceiptHeader.equals(actualReceiptHeader));
	}

	private ReceiptHeader getReceiptHeader() {
		ReceiptHeader receiptHeader = new ReceiptHeader();
		receiptHeader.setId(1L);
		receiptHeader.setPayeename("ram");
		receiptHeader.setPayeeAddress("abc nagara");
		receiptHeader.setPayeeEmail("abc@gmail.com");
		receiptHeader.setPaidBy("ram");
		receiptHeader.setReference_ch_id(23L);
		receiptHeader.setReferenceNumber("REF123");
		receiptHeader.setFunction("45");
		receiptHeader.setVersion(0L);
		receiptHeader.setChannel("cha123");
		receiptHeader.setReceiptType("bill based");
		receiptHeader.setReceiptNumber("REC_1234");
		receiptHeader.setReferenceDesc("refde90");
		receiptHeader.setManualReceiptNumber("MAN567");
		receiptHeader.setBusinessDetails("TL");
		receiptHeader.setCollectionType("B");
		receiptHeader.setStateId(34L);
		receiptHeader.setLocation(56L);
		receiptHeader.setIsReconciled(true);
		receiptHeader.setStatus("CLOSED");
		receiptHeader.setReasonForCancellation("Documents not proper");
		receiptHeader.setMinimumAmount(200.00);
		receiptHeader.setTotalAmount(250.00);
		receiptHeader.setCollModesNotAllwd("billcollection");
		receiptHeader.setConsumerCode("CONS678789");
		receiptHeader.setConsumerType("citizen");
		receiptHeader.setFund("34");
		receiptHeader.setFundSource("56");
		receiptHeader.setBoundary("67");
		receiptHeader.setDepartment("78");
		receiptHeader.setDepositedBranch("ICICI");
		receiptHeader.setTenantId("default");
		receiptHeader.setDisplayMsg("done");
		receiptHeader.setVoucherheader("VOUHEAD456");
		receiptHeader.setCreatedBy(1L);
		receiptHeader.setLastModifiedBy(1L);
		receiptHeader.setReferenceDate(null);
		receiptHeader.setReceiptDate(null);
		receiptHeader.setManualReceiptDate(null);
		receiptHeader.setCreatedDate(null);
		receiptHeader.setLastModifiedDate(null);
		receiptHeader.setCancellationRemarks("receipt payee name not proper");
		Set<ReceiptDetail> detailSet = new HashSet<>();
		ReceiptDetail receiptDetail = new ReceiptDetail();
		receiptDetail.setId(1L);
		receiptDetail.setChartOfAccount("567");
		receiptDetail.setDramount(2000.00);
		receiptDetail.setActualcramountToBePaid(2500.00);
		receiptDetail.setCramount(2000.00);
		receiptDetail.setOrdernumber(2L);
		receiptDetail.setDescription("done");
		receiptDetail.setIsActualDemand(true);
		receiptDetail.setFinancialYear("sixteen");
		receiptDetail.setPurpose("creation");
		receiptDetail.setTenantId("default");
		detailSet.add(receiptDetail);
		receiptHeader.setReceiptDetails(detailSet);
		return receiptHeader;
	}
}