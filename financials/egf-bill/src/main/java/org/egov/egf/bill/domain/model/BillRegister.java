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
package org.egov.egf.bill.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.domain.model.Auditable;
import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.Department;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = { "status", "fund", "function", "fundsource", "scheme", "subScheme", "functionary",
	"division", "department", "billDetails" }, callSuper = false)
public class BillRegister extends Auditable {
    @Length(max = 50)
    private String id;
    /**
     * billType is the type of the bill example is
     * ExpenseBill,ContractorBill,PurchaseBill,SalaryBill etc
     */
    @NotNull
    @Length(max = 50)
    private String billType;
    /**
     * billSubType refers with each type of bill what is the subtype .
     * 
     * for example ContractorBill will have subType as "FinalBill"
     */
    @Length(max = 50)
    private String billSubType;
    /**
     * billNumber refers to the unique number generated for the bill.
     * 
     */
    @Length(max = 50)
    private String billNumber;
    /**
     * billDate is the date when the bill is created.
     */
    @NotNull
    private Date billDate;
    /**
     * billAmount is the total bill Amount . even though the bill is created for
     * billAmount of x it may be passed for amount x-k
     */
    @NotNull
    private BigDecimal billAmount;
    /**
     * passedAmount refers to the amount passed by ulb . even though the bill is
     * created for billAmount of x it may be passed for amount x-k . This
     * defaults to the Bill Amount and can be less than or equal to the Bill
     * Amount.
     * 
     */
    private BigDecimal passedAmount;
    @Length(max = 50)
    /**
     * moduleName is the name of the module who is posting the bill in
     * financials
     */
    private String moduleName;
    /**
     * status refers to the status of the bill like ,created,approved etc
     */
    private FinancialStatusContract status;
    /**
     * fund refers to the fund master
     */
    private FundContract fund;
    /**
     * function refers to the function master
     */
    private FunctionContract function;
    /**
     * fundsource refers to the fundsounce master
     */
    private FundsourceContract fundsource;
    
    private SchemeContract scheme;
    
    private SubSchemeContract subScheme;
    
    private FunctionaryContract functionary;
    
    private Boundary division;
    
    private Department department;
    
    @Length(max = 256)
    private String sourcePath;
    /**
     * budgetCheckRequired is a boolean field is the budget check is required or
     * not default is true
     * 
     */
    private Boolean budgetCheckRequired;
    @Length(max = 50)
    /**
     * budgetAppropriationNo is the number generated after budget check. This
     * field will be null if the budget check not done.
     */
    private String budgetAppropriationNo;
    @Length(max = 50)
    /**
     * partyBillNumber is the manual bill number .
     */
    private String partyBillNumber;
    /**
     * partyBillDate is the manual bill date .
     */
    private Date partyBillDate;
    /**
     * description is the more detailed information about the bill
     */
    @Length(max = 256)
    private String description;
    
    private List<BillDetail> billDetails;
    
    private List<BillChecklist> checkLists;
    
    public BigDecimal getTotalAmount() {
	BigDecimal amount = BigDecimal.ZERO;
	if (billDetails != null)
	    for (final BillDetail detail : billDetails)
		amount = amount.add(detail.getDebitAmount());
	return amount;
    }
}
