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

package org.egov.collection.repository.rowmapper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.EqualsAndHashCode;

import org.egov.collection.model.ReceiptHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@EqualsAndHashCode
@Component
public class ReceiptRowMapper implements RowMapper<ReceiptHeader> {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ReceiptRowMapper.class);

	@Override
	public ReceiptHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		ReceiptHeader receiptHeader = new ReceiptHeader();
		receiptHeader.setId((Long) rs.getObject("rh_id"));
		receiptHeader.setPayeename(rs.getString("rh_payeename"));
		receiptHeader.setPayeeAddress(rs.getString("rh_payeeAddress"));
		receiptHeader.setPayeeEmail(rs.getString("rh_payeeEmail"));
		receiptHeader.setPaidBy(rs.getString("rh_paidBy"));
		receiptHeader.setReference_ch_id((Long) rs
				.getObject("rh_reference_ch_id"));
		receiptHeader.setReferenceNumber(rs.getString("rh_referenceNumber"));
		receiptHeader.setFunction(rs.getString("rh_function"));
		receiptHeader.setVersion(rs.getLong("rh_version"));
		receiptHeader.setChannel(rs.getString("rh_channel"));
		receiptHeader.setReceiptType(rs.getString("rh_receiptType"));
		receiptHeader.setReceiptNumber(rs.getString("rh_receiptNumber"));
		receiptHeader.setReferenceDesc(rs.getString("rh_referenceDesc"));
		receiptHeader.setManualReceiptNumber(rs
				.getString("rh_manualReceiptNumber"));
		receiptHeader.setBusinessDetails(rs.getString("rh_businessDetails"));
		receiptHeader.setCollectionType(rs.getString("rh_collectionType"));
		receiptHeader.setStateId((Long) rs.getObject("rh_stateId"));
		receiptHeader.setLocation((Long) rs.getObject("rh_location"));
		receiptHeader
				.setIsReconciled((Boolean) rs.getObject("rh_isReconciled"));
		receiptHeader.setStatus(rs.getString("rh_status"));
		receiptHeader.setReasonForCancellation(rs
				.getString("rh_reasonForCancellation"));
		receiptHeader.setMinimumAmount((Double) rs
				.getObject("rh_minimumAmount"));
		receiptHeader.setTotalAmount((Double) rs.getObject("rh_totalAmount"));
		receiptHeader
				.setCollModesNotAllwd(rs.getString("rh_collModesNotAllwd"));
		receiptHeader.setConsumerCode(rs.getString("rh_consumerCode"));
		receiptHeader.setConsumerType(rs.getString("rh_consumerType"));
		receiptHeader.setFund(rs.getString("rh_fund"));
		receiptHeader.setFundSource(rs.getString("rh_fundSource"));
		receiptHeader.setBoundary(rs.getString("rh_boundary"));
		receiptHeader.setDepartment(rs.getString("rh_department"));
		receiptHeader.setDepositedBranch(rs.getString("rh_depositedBranch"));
		receiptHeader.setTenantId(rs.getString("rh_tenantId"));
		receiptHeader.setDisplayMsg(rs.getString("rh_displayMsg"));
		receiptHeader.setVoucherheader(rs.getString("rh_voucherheader"));
		receiptHeader.setCreatedBy((Long) rs.getObject("rh_createdBy"));
		receiptHeader.setLastModifiedBy((Long) rs
				.getObject("rh_lastModifiedBy"));
		receiptHeader.setCancellationRemarks(rs
				.getString("rh_cancellationRemarks"));
		receiptHeader.setTransactionId(rs.getString("rh_transactionid"));

		try {
			Date date = isEmpty((Long) rs.getObject("rh_referenceDate")) ? null
					: sdf.parse(sdf.format((Long) rs
							.getObject("rh_referenceDate")));
			receiptHeader.setReferenceDate(date);
			date = isEmpty((Long) rs.getObject("rh_receiptDate")) ? null : sdf
					.parse(sdf.format((Long) rs.getObject("rh_receiptDate")));
			receiptHeader.setReceiptDate(date);
			receiptHeader.setReceiptDateWithTimeStamp((Long) rs.getObject("rh_receiptDate"));
			date = isEmpty((Long) rs.getObject("rh_createdDate")) ? null : sdf
					.parse(sdf.format((Long) rs.getObject("rh_createdDate")));
			receiptHeader.setCreatedDate(date);
			date = isEmpty((Long) rs.getObject("rh_lastModifiedDate")) ? null
					: sdf.parse(sdf.format((Long) rs
							.getObject("rh_lastModifiedDate")));
			receiptHeader.setLastModifiedDate(date);
		} catch (ParseException e) {
			LOGGER.error("Parse exception while parsing date" + e);
		}

		return receiptHeader;
	}
}