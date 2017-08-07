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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.common.domain.model.Auditable;
import org.egov.egf.budget.web.contract.Boundary;
import org.egov.egf.budget.web.contract.Department;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.hibernate.validator.constraints.Length;

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
public class BudgetDetail extends Auditable {

    /**
     * id of the budgetDeatil representing the unique value of each record getting saved.
     */
    private String id;

    /**
     * budget is node reference given for budget in the budget tree structure defined.
     */
    @NotNull
    private Budget budget;

    /**
     * budgetGroup is reference to the budget group which defines the set of COA or a COA at major, minor or detailed level with
     * its account type and budgeting type.
     */
    @NotNull
    private BudgetGroupContract budgetGroup;

    /**
     * usingDepartment is the department for which the budget line item is actually budgeted for.
     */
    private Department usingDepartment;

    /**
     * executingDepartment is the department which uses the budget to execute the work which is defined in the budget of using
     * department.
     */
    private Department executingDepartment;

    /**
     * fund is the MIS or financial transaction attribute which contributes the budget details definition
     */
    private FundContract fund;

    /**
     * function is the MIS or financial transaction attribute which contributes the budget details definition.
     */
    private FunctionContract function;

    /**
     * scheme is the MIS or financial transaction attribute which contributes the budget details definition. Scheme can be
     * optional
     */

    private SchemeContract scheme;

    /**
     * subScheme is the MIS or financial transaction attribute which contributes the budget details definition. sub Scheme can be
     * optional
     */
    private SubSchemeContract subScheme;

    /**
     * functionary is the MIS or financial transaction attribute which contributes the budget details definition
     */
    private FunctionaryContract functionary;

    /**
     * boundary is also the MIS attribute which is used incase the budget need to be defined and idetified by boundaries of the
     * organization.
     */
    private Boundary boundary;

    /**
     * anticipatoryAmount is the anticipated amount while processing the Estimate or re-appropriations.
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal anticipatoryAmount = new BigDecimal("0.0");

    /**
     * originalAmount is the budget amount for the given combination of budget detail
     */

    @NotNull
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal originalAmount = new BigDecimal("0.0");

    /**
     * approvedAmount final approved budget (post workflow) amount which is considered for budget checking.
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal approvedAmount = new BigDecimal("0.0");

    /**
     * planningPercent is the percentage which gives the inflated budget used in certain business modules to have the budget check
     * based on the inflated budget values. Ex: 150% in planning percentage for a budget line item is the 50% extra budget on
     * approved budget used to do the budget checking in works module.
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal planningPercent;

    /** budgetAvailable is the running balance of the budget line item */
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal budgetAvailable = new BigDecimal("0.0");

    /**
     * status gives the current status of the budget line item. (detailed level)
     */
    private FinancialStatusContract status;

    /**
     * documentNumber is the reference number to identify the attachments made to the budget definition.
     */
    private String documentNumber;

    /**
     * unique number is unique ref number to identify the budget line item. ie the combination of MIS attributes used for defining
     * budget and budget group. Ex - Fund, Function, dept and Group. This data is internal to the system.
     */
    @Length(max = 128)
    private String uniqueNo;

    /**
     * materializedPath is unique data by hierarchy level.This data is created internally
     */
    @Length(max = 10)
    private String materializedPath;

}
