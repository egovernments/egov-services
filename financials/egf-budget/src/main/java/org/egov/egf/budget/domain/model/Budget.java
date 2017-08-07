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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.common.domain.model.Auditable;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;

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
@EqualsAndHashCode(exclude = { "financialYear", "parent", "referenceBudget", "status" }, callSuper = false)
public class Budget extends Auditable {

    /**
     * id of the budget representing the unique value of each record getting saved.
     */
    private String id;

    /**
     * name given for budget in the tree structure. Generally Tree structure consist of 3 levels : 1. Root level : which is
     * basically defined as RE-2017-18 or BE-2017-18 which defines the budget type and the financial year. 2. Account type level :
     * which is defined under root level which will be combination of root level and account type - Revenue and Capital. Ex :
     * "RE-Rev-2017-18" 3. Department wise : which is defined under budget type level which will be combination of root level,
     * account type and department. Ex : "ACC-Rev-RE-2017-18" and then the budget detail under department level.
     */
    @Size(max = 250)
    @NotNull
    private String name;

    /**
     * financialYear is the attribute to identify to which year the Budget belongs is tagged.
     */
    @NotNull
    private FinancialYearContract financialYear;

    /**
     * estimationType is type of the budget definition - which signifies budget type i.e New budget (BE) or the Revised budget
     * (RE)
     */
    @NotNull
    private EstimationType estimationType;

    /**
     * parent is the node used to define in the budget hierarchy tree structure definition. The root node will not have any
     * parent. The lowest node is the budget under which the details are defined.
     */
    private Budget parent;

    /**
     * active provides flag denotes whether the budget is active or not. i.e all the detail budget defined under this tree will
     * not be accessible in transaction.
     */
    private Boolean active;

    /**
     * primaryBudget is the flag that identifies the root budget. (which has no parent).
     */
    @NotNull
    private Boolean primaryBudget;

    /**
     * referenceBudget is the previous year budget tree id reference to refer previous year budget. When the BE is created, the
     * previous year RE reference is mapped to the BE of current year or for the year for which BE is created.
     */
    private Budget referenceBudget;

    /**
     * status gives the current status of the budget Node. i.e collective status of the details. However the status at budget
     * detail also exist.
     */
    private FinancialStatusContract status;

    /**
     * documentNumber is the reference number to identify the attachments made to the budget definition.
     */
    @Size(max = 50)
    private String documentNumber;

    /**
     * description provides more information on budget line item and this is combination of department name, estimation
     * type,budget type and financial year. example description "ENGINEERING RE RevenueBudget for the year 2015-16"
     * (ENG-Engineering department,RE- Revision Estimate,Rev-Revenue,2017-18: financial year)
     */
    @Size(max = 250)
    private String description;

    /**
     * materializedPath is unique data by hierarchy level.
     */
    @Size(max = 25)
    private String materializedPath;

}
