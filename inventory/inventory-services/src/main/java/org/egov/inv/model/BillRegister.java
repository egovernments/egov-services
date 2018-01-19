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
package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BillRegister {

    /**
     * tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is
     * created.
     *
     * @return tenantId
     **/
    @NotNull
    @Size(min = 0, max = 256)
    @JsonProperty("tenantId")
    private String tenantId = null;

    /**
     * billType is the type of the bill example is ExpenseBill,ContractorBill,PurchaseBill,SalaryBill etc
     *
     * @return billType
     **/
    @NotNull
    @Size(max = 50)
    @JsonProperty("billType")
    private String billType = null;

    /**
     * billSubType refers with each type of bill what is the subtype . for example ContractorBill will have subType as
     * \"FinalBill\"
     *
     * @return billSubType
     **/
    @Size(max = 50)
    @JsonProperty("billSubType")
    private String billSubType = null;

    /**
     * billNumber refers to the unique number generated for the bill.
     *
     * @return billNumber
     **/
    @Size(max = 50)
    @JsonProperty("billNumber")
    private String billNumber = null;

    /**
     * billDate is the date when the bill is created.
     *
     * @return billDate
     **/
    @NotNull
    @JsonProperty("billDate")
    private Long billDate = null;

    /**
     * billAmount is the total bill Amount . even though the bill is created for billAmount of x it may be passed for amount x-k
     *
     * @return billAmount
     **/
    @JsonProperty("billAmount")
    private Double billAmount = null;

    /**
     * passedAmount refers to the amount passed by ulb . even though the bill is created for billAmount of x it may be passed for
     * amount x-k
     *
     * @return passedAmount
     **/
    @JsonProperty("passedAmount")
    private Double passedAmount = null;

    /**
     * moduleName is the name of the module who is posting the bill in financials
     *
     * @return moduleName
     **/
    @Size(max = 50)
    @JsonProperty("moduleName")
    private String moduleName = null;

    /**
     * status refers to the status of the bill like ,created,approved etc
     *
     * @return status
     **/
    @JsonProperty("status")
    private BillStatus status = null;

    /**
     * fund refers to the fund master
     *
     * @return fund
     **/
    @JsonProperty("fund")
    private Fund fund = null;

    /**
     * function refers to the function master
     *
     * @return function
     **/
    @JsonProperty("function")
    private Function function = null;

    /**
     * fundsource of the BillRegister
     *
     * @return fundsource
     **/
    @JsonProperty("fundsource")
    private Fundsource fundsource = null;

    /**
     * scheme of the BillRegister
     *
     * @return scheme
     **/
    @JsonProperty("scheme")
    private Scheme scheme = null;

    /**
     * sub scheme of the BillRegister
     *
     * @return subScheme
     **/
    @JsonProperty("subScheme")
    private SubScheme subScheme = null;

    /**
     * functionary of the BillRegister
     *
     * @return functionary
     **/
    @JsonProperty("functionary")
    private Functionary functionary = null;

    /**
     * location of the BillRegister
     *
     * @return location
     **/
    @JsonProperty("location")
    private Boundary location = null;

    /**
     * department of the BillRegister
     *
     * @return department
     **/
    @JsonProperty("department")
    private Department department = null;

    /**
     * source path of the BillRegister
     *
     * @return sourcePath
     **/
    @JsonProperty("sourcePath")
    private String sourcePath = null;

    /**
     * budgetCheckRequired is a boolean field is the budget check is required or not default is true
     *
     * @return budgetCheckRequired
     **/
    @JsonProperty("budgetCheckRequired")
    private Boolean budgetCheckRequired = null;

    /**
     * budgetAppropriationNo is the number generated after budget check. This field will be null if the budget check not done.
     *
     * @return budgetAppropriationNo
     **/
    @Size(max = 50)
    @JsonProperty("budgetAppropriationNo")
    private String budgetAppropriationNo = null;

    /**
     * partyBillNumber is the manual bill number .
     *
     * @return partyBillNumber
     **/
    @Size(max = 50)
    @JsonProperty("partyBillNumber")
    private String partyBillNumber = null;

    /**
     * partyBillDate is the manual bill date .
     *
     * @return partyBillDate
     **/
    @JsonProperty("partyBillDate")
    private Long partyBillDate = null;

    /**
     * description is the more detailed information about the bill
     *
     * @return description
     **/
    @Size(max = 256)
    @JsonProperty("description")
    private String description = null;

    /**
     * bill details of the BillRegister
     *
     * @return billDetails
     **/
    @JsonProperty("billDetails")
    private List<BillDetail> billDetails = null;

    /**
     * check Lists of the BillRegister
     *
     * @return checkLists
     **/
    private List<BillChecklist> checkLists;

    /**
     * Get auditDetails
     *
     * @return auditDetails
     **/
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public BigDecimal getTotalAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        if (billDetails != null)
            for (final BillDetail detail : billDetails)
                amount = amount.add(detail.getDebitAmount());
        return amount;
    }
}
