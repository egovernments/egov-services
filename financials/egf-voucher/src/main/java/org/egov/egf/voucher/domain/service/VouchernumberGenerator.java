/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.egf.voucher.domain.service;

import java.io.Serializable;
import java.util.Calendar;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.egov.egf.master.web.contract.FinancialYearSearchContract;
import org.egov.egf.master.web.repository.FinancialYearContractRepository;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.web.util.VoucherConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VouchernumberGenerator {

	enum voucherTypeEnum {
		JOURNALVOUCHER, CONTRA, RECEIPT, PAYMENT;
	}

	enum voucherSubTypeEnum {
		JOURNALVOUCHER, CONTRA, RECEIPT, PAYMENT, PURCHASEJOURNAL, PENSIONJOURNAL, PURCHASE, WORKS, CONTRACTORJOURNAL, FIXEDASSETJOURNAL, FIXEDASSET, PENSION, WORKSJOURNAL, CONTINGENTJOURNAL, SALARY, SALARYJOURNAL, EXPENSE, EXPENSEJOURNAL, JVGENERAL;
	}

	@Autowired
	private FinancialYearContractRepository financialYearContractRepository;

	@Autowired
	private JdbcRepository voucherJdbcRepository;

	/**
	 *
	 * Format fundcode/vouchertype/seqnumber/month/financialyear but sequence is
	 * running number for a year
	 *
	 */
	public String getNextNumber(final Voucher voucher) {

		String voucherNumber;

		String sequenceName;

		Calendar cal = Calendar.getInstance();
		cal.setTime(voucher.getVoucherDate());
		int month = cal.get(Calendar.MONTH);

		FinancialYearSearchContract financialYearSearchContract = new FinancialYearSearchContract();

		financialYearSearchContract.setAsOnDate(voucher.getVoucherDate());
		financialYearSearchContract.setTenantId(voucher.getTenantId());

		final FinancialYearContract financialYear = financialYearContractRepository
				.findByAsOnDate(financialYearSearchContract, new RequestInfo());
		if (financialYear == null)
			throw new InvalidDataException("voucherDate", "Financial Year invalid",
					"Financial Year is not defined for the voucher date");

		sequenceName = "sq_" + voucher.getFund().getCode() + "_"
				+ getVoucherNumberPrefix(voucher.getType(), voucher.getName()) + "_" + financialYear.getFinYearRange();

		final Serializable nextSequence = getSequenceValue(sequenceName.replace("-", "_"));

		voucherNumber = String.format("%s/%s/%08d/%02d/%s", voucher.getFund().getCode(),
				getVoucherNumberPrefix(voucher.getType(), voucher.getName()), Integer.parseInt(nextSequence.toString()),
				month, financialYear.getFinYearRange());

		return voucherNumber;
	}

	private Serializable getSequenceValue(String sequenceName) {
		Serializable nextSequence;
		try {

			nextSequence = voucherJdbcRepository.getSequence(sequenceName);

		} catch (Exception e) {

			voucherJdbcRepository.createSequence(sequenceName);
			nextSequence = voucherJdbcRepository.getSequence(sequenceName);
		}
		return nextSequence;
	}

	private String getVoucherNumberPrefix(final String type, String vsubtype) {

		// if sub type is null use type
		if (vsubtype == null || vsubtype.isEmpty())
			vsubtype = type;
		String subtype = vsubtype.toUpperCase().trim();
		String voucherNumberPrefix = null;
		String typetoCheck = subtype;

		if (subtype.equalsIgnoreCase("JOURNAL VOUCHER"))
			typetoCheck = "JOURNALVOUCHER";

		switch (voucherSubTypeEnum.valueOf(typetoCheck)) {
		case JVGENERAL:
			voucherNumberPrefix = VoucherConstants.JOURNAL_VOUCHERNO_TYPE_PREFIX;
			break;
		case JOURNALVOUCHER:
			voucherNumberPrefix = VoucherConstants.JOURNAL_VOUCHERNO_TYPE_PREFIX;
			break;
		case CONTRA:
			voucherNumberPrefix = VoucherConstants.CONTRA_VOUCHERNO_TYPE_PREFIX;
			break;
		case RECEIPT:
			voucherNumberPrefix = VoucherConstants.RECEIPT_VOUCHERNO_TYPE_PREFIX;
			break;
		case PAYMENT:
			voucherNumberPrefix = VoucherConstants.PAYMENT_VOUCHERNO_TYPE_PREFIX;
			break;
		case PURCHASEJOURNAL:
			voucherNumberPrefix = VoucherConstants.PURCHBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case WORKS:
			voucherNumberPrefix = VoucherConstants.WORKSBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case CONTRACTORJOURNAL:
			voucherNumberPrefix = VoucherConstants.WORKSBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case WORKSJOURNAL:
			voucherNumberPrefix = VoucherConstants.WORKSBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case FIXEDASSETJOURNAL:
			voucherNumberPrefix = VoucherConstants.FIXEDASSET_VOUCHERNO_TYPE_PREFIX;
			break;
		case CONTINGENTJOURNAL:
			voucherNumberPrefix = VoucherConstants.CBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case PURCHASE:
			voucherNumberPrefix = VoucherConstants.PURCHBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case EXPENSEJOURNAL:
			voucherNumberPrefix = VoucherConstants.CBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case EXPENSE:
			voucherNumberPrefix = VoucherConstants.CBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case SALARYJOURNAL:
			voucherNumberPrefix = VoucherConstants.SALBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case SALARY:
			voucherNumberPrefix = VoucherConstants.SALBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case FIXEDASSET:
			voucherNumberPrefix = VoucherConstants.FIXEDASSET_VOUCHERNO_TYPE_PREFIX;
			break;
		case PENSIONJOURNAL:
			voucherNumberPrefix = VoucherConstants.PENBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		case PENSION:
			voucherNumberPrefix = VoucherConstants.PENBILL_VOUCHERNO_TYPE_PREFIX;
			break;
		default: // if subtype is invalid then use type
			if (voucherNumberPrefix == null)
				voucherNumberPrefix = checkWithVoucherType(type);
			break;
		}
		return voucherNumberPrefix;

	}

	private String checkWithVoucherType(final String type) {
		String typetoCheck = type;

		if (type.equalsIgnoreCase("JOURNAL VOUCHER"))
			typetoCheck = "JOURNALVOUCHER";

		String voucherNumberPrefix = null;

		switch (voucherTypeEnum.valueOf(typetoCheck)) {
		case JOURNALVOUCHER:
			voucherNumberPrefix = VoucherConstants.JOURNAL_VOUCHERNO_TYPE_PREFIX;
			break;
		case CONTRA:
			voucherNumberPrefix = VoucherConstants.CONTRA_VOUCHERNO_TYPE_PREFIX;
			break;
		case RECEIPT:
			voucherNumberPrefix = VoucherConstants.RECEIPT_VOUCHERNO_TYPE_PREFIX;
			break;
		case PAYMENT:
			voucherNumberPrefix = VoucherConstants.PAYMENT_VOUCHERNO_TYPE_PREFIX;
			break;
		default:// do nothing
			break;
		}

		return voucherNumberPrefix;

	}
}
