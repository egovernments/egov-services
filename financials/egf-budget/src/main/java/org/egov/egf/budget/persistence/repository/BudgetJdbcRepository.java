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
package org.egov.egf.budget.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.entity.BudgetSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BudgetJdbcRepository extends JdbcRepository {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetJdbcRepository.class);

    static {
        LOG.debug("init budget");
        init(BudgetEntity.class);
        LOG.debug("end init budget");
    }

    public BudgetJdbcRepository(final NamedParameterJdbcTemplate namedParameterJdbcTemplate, final JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public BudgetEntity create(final BudgetEntity entity) {

        entity.setId(UUID.randomUUID().toString().replace("-", ""));
        super.create(entity);
        return entity;
    }

    public BudgetEntity update(final BudgetEntity entity) {
        super.update(entity);
        return entity;

    }
    
    public BudgetEntity delete(final BudgetEntity entity) {
        super.delete(entity.TABLE_NAME, entity.getId());
        return entity;
    }

    public Pagination<Budget> search(final BudgetSearch domain) {
        final BudgetSearchEntity budgetSearchEntity = new BudgetSearchEntity();
        budgetSearchEntity.toEntity(domain);

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        final Map<String, Object> paramValues = new HashMap<>();

        if (budgetSearchEntity.getSortBy() != null && !budgetSearchEntity.getSortBy().isEmpty()) {
            validateSortByOrder(budgetSearchEntity.getSortBy());
            validateEntityFieldName(budgetSearchEntity.getSortBy(), BudgetEntity.class);
        }

        String orderBy = "order by id";
        if (budgetSearchEntity.getSortBy() != null && !budgetSearchEntity.getSortBy().isEmpty())
            orderBy = "order by " + budgetSearchEntity.getSortBy();

        final StringBuffer params = new StringBuffer();

        searchQuery = searchQuery.replace(":tablename", BudgetEntity.TABLE_NAME);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        // implement jdbc specfic search
        if (budgetSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", budgetSearchEntity.getTenantId());
		}
        if (budgetSearchEntity.getId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id =:id");
            paramValues.put("id", budgetSearchEntity.getId());
        }
        if (budgetSearchEntity.getName() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("name =:name");
            paramValues.put("name", budgetSearchEntity.getName());
        }
        if (budgetSearchEntity.getFinancialYearId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("financialYearid =:financialYear");
            paramValues.put("financialYear", budgetSearchEntity.getFinancialYearId());
        }
        if (budgetSearchEntity.getEstimationType() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("estimationType =:estimationType");
            paramValues.put("estimationType", budgetSearchEntity.getEstimationType());
        }
        if (budgetSearchEntity.getParentId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("parentid =:parent");
            paramValues.put("parent", budgetSearchEntity.getParentId());
        }
        if (budgetSearchEntity.getActive() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("active =:active");
            paramValues.put("active", budgetSearchEntity.getActive());
        }
        if (budgetSearchEntity.getPrimaryBudget() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("primaryBudget =:primaryBudget");
            paramValues.put("primaryBudget", budgetSearchEntity.getPrimaryBudget());
        }
        if (budgetSearchEntity.getReferenceBudgetId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("referenceBudgetid =:referenceBudget");
            paramValues.put("referenceBudget", budgetSearchEntity.getReferenceBudgetId());
        }
        if (budgetSearchEntity.getStatusId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("statusid =:status");
            paramValues.put("status", budgetSearchEntity.getStatusId());
        }
        if (budgetSearchEntity.getDocumentNumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("documentNumber =:documentNumber");
            paramValues.put("documentNumber", budgetSearchEntity.getDocumentNumber());
        }
        if (budgetSearchEntity.getDescription() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("description =:description");
            paramValues.put("description", budgetSearchEntity.getDescription());
        }
        if (budgetSearchEntity.getMaterializedPath() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("materializedPath =:materializedPath");
            paramValues.put("materializedPath", budgetSearchEntity.getMaterializedPath());
        }

        Pagination<Budget> page = new Pagination<>();
        if (budgetSearchEntity.getOffset() != null)
            page.setOffset(budgetSearchEntity.getOffset());
        if (budgetSearchEntity.getPageSize() != null)
            page.setPageSize(budgetSearchEntity.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<Budget>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(BudgetEntity.class);

        final List<BudgetEntity> budgetEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

        page.setTotalResults(budgetEntities.size());

        final List<Budget> budgets = new ArrayList<Budget>();
        for (final BudgetEntity budgetEntity : budgetEntities)
            budgets.add(budgetEntity.toDomain());
        page.setPagedData(budgets);

        return page;
    }

    public BudgetEntity findById(final BudgetEntity entity) {
        final List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

        final Map<String, Object> paramValues = new HashMap<>();

        for (final String s : list)
            paramValues.put(s, getValue(getField(entity, s), entity));

        final List<BudgetEntity> budgets = namedParameterJdbcTemplate.query(
                getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
                new BeanPropertyRowMapper(BudgetEntity.class));
        if (budgets.isEmpty())
            return null;
        else
            return budgets.get(0);

    }

}
