/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.budget.domain.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.common.domain.model.Auditable;
import org.egov.egf.master.web.contract.FinancialStatusContract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BudgetReAppropriation extends Auditable {

    /**
     * id of the BudgetReAppropriation representing the unique value of each record getting saved.
     */
    private String id;

    /**
     * budgetDetail is the reference given for budget re appropriation to identify for witch budget line item this appropriation
     * is doing.
     */
    @NotNull
    private BudgetDetail budgetDetail;

    /**
     * additionAmount is the proposed extra amount to add for the given budget detail
     */
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal additionAmount = new BigDecimal("0.0");

    /**
     * deductionAmount is the proposed amount to deduct for the given budget detail
     */
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal deductionAmount = new BigDecimal("0.0");

    /**
     * originalAdditionAmount is the extra amount to add for the given budget detail
     */
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal originalAdditionAmount = new BigDecimal("0.0");

    /**
     * originalAdditionAmount is the amount to deduct for the given budget detail
     */
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal originalDeductionAmount = new BigDecimal("0.0");

    /**
     * anticipatoryAmount is the anticipated amount while processing the re-appropriations.
     */
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal anticipatoryAmount = new BigDecimal("0.0");

    /**
     * status gives the current status of the budget re appropriation line item.
     */
    private FinancialStatusContract status;

    /**
     * asOnDate is the date on witch date the current appropriation is done.
     */
    private Date asOnDate;

}
