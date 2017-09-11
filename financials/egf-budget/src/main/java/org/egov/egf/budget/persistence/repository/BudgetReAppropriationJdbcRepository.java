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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BudgetReAppropriationJdbcRepository extends JdbcRepository {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetReAppropriationJdbcRepository.class);

    static {
        LOG.debug("init budgetReAppropriation");
        init(BudgetReAppropriationEntity.class);
        LOG.debug("end init budgetReAppropriation");
    }

    public BudgetReAppropriationJdbcRepository(final NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public BudgetReAppropriationEntity create(final BudgetReAppropriationEntity entity) {

        entity.setId(UUID.randomUUID().toString().replace("-", ""));
        super.create(entity);
        return entity;
    }

    public BudgetReAppropriationEntity update(final BudgetReAppropriationEntity entity) {
        super.update(entity);
        return entity;

    }
    
    public BudgetReAppropriationEntity delete(final BudgetReAppropriationEntity entity) {
        super.delete(entity.TABLE_NAME, entity.getId());
        return entity;
    }

    public Pagination<BudgetReAppropriation> search(final BudgetReAppropriationSearch domain) {
        final BudgetReAppropriationSearchEntity budgetReAppropriationSearchEntity = new BudgetReAppropriationSearchEntity();
        budgetReAppropriationSearchEntity.toEntity(domain);

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (budgetReAppropriationSearchEntity.getSortBy() != null
                && !budgetReAppropriationSearchEntity.getSortBy().isEmpty()) {
            validateSortByOrder(budgetReAppropriationSearchEntity.getSortBy());
            validateEntityFieldName(budgetReAppropriationSearchEntity.getSortBy(), BudgetEntity.class);
        }

        String orderBy = "order by id";
        if (budgetReAppropriationSearchEntity.getSortBy() != null
                && !budgetReAppropriationSearchEntity.getSortBy().isEmpty())
            orderBy = "order by " + budgetReAppropriationSearchEntity.getSortBy();

        searchQuery = searchQuery.replace(":tablename", BudgetReAppropriationEntity.TABLE_NAME);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        // implement jdbc specfic search
        if (budgetReAppropriationSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", budgetReAppropriationSearchEntity.getTenantId());
		}
        if (budgetReAppropriationSearchEntity.getId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id =:id");
            paramValues.put("id", budgetReAppropriationSearchEntity.getId());
        }
        if (budgetReAppropriationSearchEntity.getBudgetDetailId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("budgetDetailid =:budgetDetail");
            paramValues.put("budgetDetail", budgetReAppropriationSearchEntity.getBudgetDetailId());
        }
        if (budgetReAppropriationSearchEntity.getAdditionAmount() != null
                && 0 != budgetReAppropriationSearchEntity.getAdditionAmount().compareTo(BigDecimal.ZERO)) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("additionAmount =:additionAmount");
            paramValues.put("additionAmount", budgetReAppropriationSearchEntity.getAdditionAmount());
        }
        if (budgetReAppropriationSearchEntity.getDeductionAmount() != null
                && 0 != budgetReAppropriationSearchEntity.getDeductionAmount().compareTo(BigDecimal.ZERO)) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("deductionAmount =:deductionAmount");
            paramValues.put("deductionAmount", budgetReAppropriationSearchEntity.getDeductionAmount());
        }
        if (budgetReAppropriationSearchEntity.getOriginalAdditionAmount() != null
                && 0 != budgetReAppropriationSearchEntity.getOriginalAdditionAmount().compareTo(BigDecimal.ZERO)) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("originalAdditionAmount =:originalAdditionAmount");
            paramValues.put("originalAdditionAmount", budgetReAppropriationSearchEntity.getOriginalAdditionAmount());
        }
        if (budgetReAppropriationSearchEntity.getOriginalDeductionAmount() != null
                && 0 != budgetReAppropriationSearchEntity.getOriginalDeductionAmount().compareTo(BigDecimal.ZERO)) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("originalDeductionAmount =:originalDeductionAmount");
            paramValues.put("originalDeductionAmount", budgetReAppropriationSearchEntity.getOriginalDeductionAmount());
        }
        if (budgetReAppropriationSearchEntity.getAnticipatoryAmount() != null
                && 0 != budgetReAppropriationSearchEntity.getAnticipatoryAmount().compareTo(BigDecimal.ZERO)) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("anticipatoryAmount =:anticipatoryAmount");
            paramValues.put("anticipatoryAmount", budgetReAppropriationSearchEntity.getAnticipatoryAmount());
        }
        if (budgetReAppropriationSearchEntity.getStatusId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("statusid =:status");
            paramValues.put("status", budgetReAppropriationSearchEntity.getStatusId());
        }
        if (budgetReAppropriationSearchEntity.getAsOnDate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("asOnDate =:asOnDate");
            paramValues.put("asOnDate", budgetReAppropriationSearchEntity.getAsOnDate());
        }

        Pagination<BudgetReAppropriation> page = new Pagination<>();
        if (budgetReAppropriationSearchEntity.getOffset() != null)
            page.setOffset(budgetReAppropriationSearchEntity.getOffset());
        if (budgetReAppropriationSearchEntity.getPageSize() != null)
            page.setPageSize(budgetReAppropriationSearchEntity.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<BudgetReAppropriation>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(BudgetReAppropriationEntity.class);

        final List<BudgetReAppropriationEntity> budgetReAppropriationEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        page.setTotalResults(budgetReAppropriationEntities.size());

        final List<BudgetReAppropriation> budgetreappropriations = new ArrayList<BudgetReAppropriation>();
        for (final BudgetReAppropriationEntity budgetReAppropriationEntity : budgetReAppropriationEntities)
            budgetreappropriations.add(budgetReAppropriationEntity.toDomain());
        page.setPagedData(budgetreappropriations);

        return page;
    }

    public BudgetReAppropriationEntity findById(final BudgetReAppropriationEntity entity) {
        final List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

        final Map<String, Object> paramValues = new HashMap<>();

        for (final String s : list)
            paramValues.put(s, getValue(getField(entity, s), entity));

        final List<BudgetReAppropriationEntity> budgetreappropriations = namedParameterJdbcTemplate.query(
                getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
                new BeanPropertyRowMapper(BudgetReAppropriationEntity.class));
        if (budgetreappropriations.isEmpty())
            return null;
        else
            return budgetreappropriations.get(0);

    }

}
