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
package org.egov.egf.instrument.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.egf.master.web.contract.BankAccountContract;
import org.egov.egf.master.web.contract.BankContract;
import org.ja.annotation.DrillDown;
import org.ja.annotation.DrillDownTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = { "ECSType", "instrumentVouchers", "instrumentType", "status" }, callSuper = false)
public class InstrumentHeader {

	/*
	 * id is the unique reference to Instrument Header entered in the system.
	 */
	private String id;
	
	/*
	 * instrumentNumber is the Cheque numbers assigned for the payment which is
	 * unique for a given bank account number and financial year. In case of
	 * receipt, it the cheque number entered in the receipt which is having no
	 * unique constraints as it will repeat. This will be null in case of RTGS
	 * payment.
	 */
	private String instrumentNumber;

	/*
	 * instrumentDate is the cheque date of instrument assigned in
	 * payment/receipt.
	 */
	private Date instrumentDate;


	/*
	 * instrumentAmount is the (total) amount entered for the instrument in the
	 * payment or receipt.
	 */
	private BigDecimal instrumentAmount;

	
	
	
	/*
	 * instrumentType specifies the type of the instrument - i.e Cheque/DD/RTGS.
	 * For receipt - Cheque/DD/RTGS
	 */
	private InstrumentType instrumentType;

	
	/*
	 * bankAccount is the reference of the Bank account from which the payment
	 * instrument is assigned
	 */
	private BankAccountContract bankAccount;

	/*
	 * status gives the current status of the instrument. (Receipt/Payment)
	 */
	private EgfStatus status;

	/*
	 * bankId reference to the bank from which the payment/Receipt is made.
	 */
	private BankContract bank;


	/*
	 * isPayCheque is the identifier to flag whether it is a payment instrument
	 * to receipt instrument.
	 */
	private String isPayCheque;

	
	/*
	 * detailTypeId is the reference of the sub ledger type to whom the
	 * instrument (Cheque/RTGS) is assigned with reference to the payment and
	 * receipts made.
	 */
	private String detailKeyId;

	
	/*
	 * payee in receipt, is the payee name entered in receipt by USER.
	 */
	private String payee;

	
	/*
	 * payTo is the payee name (entered in the payment - populated from
	 * sub-ledger or from the configuration specific to the transaction.
	 * Editable in payment screen.
	 */
	private String payTo;

	
	/*
	 * bankBranchName is the branch name entered in the collection Receipt.
	 */
	
	
	private String bankBranchName;

	/*
	 * surrendarReason is the reason from the defined list seleted while
	 * surrendering a payment cheque. Depending on the reason, the cheque can be
	 * re-used or not is decided.
	 */
	private String surrendarReason;

	/*
	 * FinancialYear serialNo is the series of the cheque numbers from which the
	 * instrument is assigned from. The cheque numbers in an account is defined
	 * based on Year, Bank account and tagged to a department.
	 */
	private String serialNo;

	/*
	 * 
	 * ECSType is the type of ECS receipt made. Ex - ICICI, ONLINE, etc,.
	 */
	private ECSType ECSType;

	/*
	 * instrumentVouchers is the reference to the payment voucher/s for which
	 * the instrument is attached.
	 */
	@DrillDownTable
	private Set<InstrumentVoucher> instrumentVouchers = new HashSet<InstrumentVoucher>(0);
	
	@DrillDown
	private InstrumentDetails instrumentDetails;
}
