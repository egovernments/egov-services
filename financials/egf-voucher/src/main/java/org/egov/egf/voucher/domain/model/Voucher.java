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
 *         3) This license does not grant any rights to any Long of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.voucher.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.common.domain.exception.ErrorCode;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Auditable;
import org.egov.common.web.contract.TaskContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.voucher.web.contract.Boundary;
import org.egov.egf.voucher.web.contract.Department;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author mani
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = { "status", "fund", "function", "fundsource", "scheme", "subScheme", "functionary",
	"division", "department", "ledgers" }, callSuper = false)
/**
 * 
 * Voucher - A document which serves as an authorisation for any financial
 * transaction and forms the basis for recording the accounting entry for the
 * transaction in the books of original entry, e.g., Cash Receipt Voucher, Bank
 * Receipt Voucher, Journal Voucher, Payment Voucher, etc.
 *
 */
public class Voucher extends Auditable {
    /**
     * id is the unique identifier
     */
    @Length(max = 50)
    private String id;
    /**
     * 
     */
    @Length(max = 50)
    private VoucherType type;
    /**
     * name is like sub type of the voucher. examples are Contractor Journal
     * Voucher,Salary Journal Voucher
     */
    @Length(max = 50)
    private String name;
    @Length(max = 256)
    private String description;
    /**
     * voucherNumber is unique serial number generated per voucher. The vouchers
     * shall be numbered serially. Separate series of numbers shall be
     * maintained for cash transactions and for each bank account.
     * 
     */
    @Length(max = 50)
    private String voucherNumber;
    /**
     * voucherDate is Date on which voucher is created. Post dates are not
     * allowed. If the financial year is closed then those dates are not allowed
     * to create.
     */
    @NotNull
    private Date voucherDate;
    @Length(max = 50)
    private String originalVoucherNumber;
    @Length(max = 50)
    private String refVoucherNumber;
    @Length(max = 50)
    private String moduleName;
    @Length(max = 50)
    private String billNumber;
    private FinancialStatusContract status;
    private FundContract fund;
    private FunctionContract function;
    private FundsourceContract fundsource;
    private SchemeContract scheme;
    private SubSchemeContract subScheme;
    private FunctionaryContract functionary;
    private Boundary division;
    private Department department;
    @Length(max = 256)
    private String sourcePath;
    private Boolean budgetCheckRequired = true;
    @Length(max = 50)
    private String budgetAppropriationNo;
    /**
     * partial refers to reversal of voucher. if partial is true then part of
     * the voucher can be reversed else complete voucher will be reversed.
     */
    private Boolean partial;
    /**
     * ledgers - List of legders.
     */
    @NotNull
    @Min(2)
    private Set<Ledger> ledgers;
    private Set<Deduction> deductions;
    private TaskContract state;

    public BigDecimal getTotalAmount() {
	BigDecimal amount = BigDecimal.ZERO;
	if (ledgers != null)
	    for (final Ledger detail : ledgers)
		amount = amount.add(detail.getDebitAmount());
	return amount;
    }

    public Boolean validate() {
	ValidationErrors dataErrors = new ValidationErrors();
	List<String> ledgerFunctionList = new ArrayList<>();
	// validate activeness of all mis parameters
	if (!this.fund.getActive())
	    dataErrors.getErrors()
		    .add(new InvalidDataException("fund.active", ErrorCode.INACTIVE_REF_VALUE.getCode(), null));
	if (function != null && !this.function.getActive())
	    dataErrors.getErrors()
		    .add(new InvalidDataException("function.active", ErrorCode.INACTIVE_REF_VALUE.getCode(), null));
	int i = 0;
	BigDecimal creditSum = BigDecimal.ZERO;
	BigDecimal debitSum = BigDecimal.ZERO;
	// validate each row of ledger
	String ledgerFunctionKey = "";
	for (Ledger ledger : ledgers) {
	    ledgerFunctionKey = ledger.getChartOfAccount().getGlcode() + "-" + ledger.getFunction().getId();
	    if (ledgerFunctionList.contains(ledgerFunctionKey)) {
		dataErrors.getErrors().add(new InvalidDataException("ledgers[" + i + "].chartofAccount",
			ErrorCode.DUPLICATE_ACCOUNT_CODE.getCode(), ledger.getChartOfAccount().getGlcode()));
	    }
	    // validate coa is active for posting
	    if (!ledger.getChartOfAccount().getIsActiveForPosting())
		dataErrors.getErrors().add(new InvalidDataException("ledgers[" + i + "].chartofAccount",
			ErrorCode.INACTIVE_REF_VALUE.getCode(), ledger.getChartOfAccount().getGlcode()));
	    // validate coa is of classification 4
	    if (ledger.getChartOfAccount().getClassification() != 4)
		dataErrors.getErrors().add(new InvalidDataException("ledgers[" + i + "].chartofAccount",
			ErrorCode.INACTIVE_REF_VALUE.getCode(), ledger.getChartOfAccount().getGlcode()));
	    // validate both debit and credit can not have non zero value
	    if (ledger.getDebitAmount().compareTo(BigDecimal.ZERO) > 0
		    && ledger.getCreditAmount().compareTo(BigDecimal.ZERO) > 0)
		dataErrors.getErrors().add(new InvalidDataException("ledgers[" + i + "].chartofAccount",
			ErrorCode.AMOUNT_IN_DEBIT_AND_CREDIT.getCode(), ledger.getChartOfAccount().getGlcode()));
	    // validate both debit and credit can not have zero value
	    if (ledger.getDebitAmount().compareTo(BigDecimal.ZERO) == 0
		    && ledger.getCreditAmount().compareTo(BigDecimal.ZERO) == 0)
		dataErrors.getErrors().add(new InvalidDataException("ledgers[" + i + "].chartofAccount",
			ErrorCode.ZERO_AMOUNT_IN_DEBIT_AND_CREDIT.getCode(), ledger.getChartOfAccount().getGlcode()));
	    // validate subledger data if account code is control code
	    if (ledger.getChartOfAccount().getIsSubLedger() && ledger.getSubLedgers().isEmpty())
		dataErrors.getErrors().add(new InvalidDataException("ledgers[" + i + "].ledgerDetails",
			ErrorCode.SUBLEDGER_DATA_MISSING.getCode(), ledger.getChartOfAccount().getGlcode()));
	    debitSum.add(ledger.getDebitAmount());
	    creditSum.add(ledger.getCreditAmount());
	    BigDecimal subLedgerSum = BigDecimal.ZERO;
	    int j = 0;
	    // validate each row of subledger
	    for (SubLedger detail : ledger.getSubLedgers()) {
		// validate account detail type is active
		if (!detail.getAccountDetailType().getActive())
		    dataErrors.getErrors()
			    .add(new InvalidDataException(
				    "ledgers[" + i + "].ledgerDetails[" + j + "].accountDetailType",
				    ErrorCode.INACTIVE_REF_VALUE.getCode(), ledger.getChartOfAccount().getGlcode()));
		subLedgerSum.add(detail.getAmount());
		j++;
	    }
	    // validate subledger sum and ledger amount
	    BigDecimal ledgerAmount = ledger.getDebitAmount().compareTo(BigDecimal.ZERO) > 0 ? ledger.getDebitAmount()
		    : ledger.getCreditAmount();
	    if (ledgerAmount.compareTo(subLedgerSum) != 0) {
		dataErrors.getErrors().add(new InvalidDataException("ledgers[" + i + "].ledgerDetails",
			ErrorCode.SUBLEDGER_AMOUNT_MISS_MATCH.getCode(), ledger.getChartOfAccount().getGlcode()));
	    }
	    i++;
	}
	// validate sum(debit)-sum(credit) should be 0
	if (debitSum.compareTo(creditSum) != 0) {
	    dataErrors.getErrors().add(
		    new InvalidDataException("ledgers[" + i + "]", ErrorCode.LEDGER_AMOUNT_MISS_MATCH.getCode(), null));
	}
	return false;
    }
}
