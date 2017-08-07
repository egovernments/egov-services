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
package org.egov.egf.budget.persistence.entity;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class BudgetEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_budget";
    private String id;
    private String name;
    private String financialYearId;
    private String estimationType;
    private String parentId;
    private Boolean active;
    private Boolean primaryBudget;
    private String referenceBudgetId;
    private String statusId;
    private String documentNumber;
    private String description;
    private String materializedPath;

    public Budget toDomain() {
        final Budget budget = new Budget();
        super.toDomain(budget);
        budget.setId(id);
        budget.setName(name);
        budget.setFinancialYear(FinancialYearContract.builder().id(financialYearId).build());
        budget.setEstimationType(EstimationType.valueOf(estimationType));
        budget.setParent(Budget.builder().id(parentId).build());
        budget.setActive(active);
        budget.setPrimaryBudget(primaryBudget);
        budget.setReferenceBudget(Budget.builder().id(referenceBudgetId).build());
        budget.setStatus(FinancialStatusContract.builder().id(statusId).build());
        budget.setDocumentNumber(documentNumber);
        budget.setDescription(description);
        budget.setMaterializedPath(materializedPath);
        return budget;
    }

    public BudgetEntity toEntity(final Budget budget) {
        super.toEntity(budget);
        id = budget.getId();
        name = budget.getName();
        financialYearId = budget.getFinancialYear() != null ? budget.getFinancialYear().getId() : null;
        estimationType = budget.getEstimationType() != null ? budget.getEstimationType().name() : null;
        parentId = budget.getParent() != null ? budget.getParent().getId() : null;
        active = budget.getActive();
        primaryBudget = budget.getPrimaryBudget();
        referenceBudgetId = budget.getReferenceBudget() != null ? budget.getReferenceBudget().getId() : null;
        statusId = budget.getStatus() != null ? budget.getStatus().getId() : null;
        documentNumber = budget.getDocumentNumber();
        description = budget.getDescription();
        materializedPath = budget.getMaterializedPath();
        return this;
    }

}
