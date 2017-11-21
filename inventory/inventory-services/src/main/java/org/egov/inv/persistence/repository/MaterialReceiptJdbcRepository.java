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
package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceiptSearch;
import org.egov.inv.persistence.entity.MaterialReceiptEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MaterialReceiptJdbcRepository extends JdbcRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MaterialReceipt.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static {
        init(MaterialReceiptEntity.class);
    }

    public Pagination<MaterialReceipt> search(MaterialReceiptSearch materialReceiptSearch) {
        String searchQuery = "select * from materialreceipt" +
                " :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();

        if (materialReceiptSearch.getSortBy() != null && !materialReceiptSearch.getSortBy().isEmpty()) {
            validateSortByOrder(materialReceiptSearch.getSortBy());
            validateEntityFieldName(materialReceiptSearch.getSortBy(), MaterialReceiptSearch.class);
        }

        String orderBy = "order by mrnnumber";

        if (materialReceiptSearch.getSortBy() != null && !materialReceiptSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + materialReceiptSearch.getSortBy();
        }

        if (materialReceiptSearch.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", materialReceiptSearch.getIds());
        }

        if (materialReceiptSearch.getMrnNumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("mrnnumber in (:mrnNumber)");
            paramValues.put("mrnNumber", materialReceiptSearch.getMrnNumber());
        }

        if (materialReceiptSearch.getReceiptDate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("receiptdate = :receiptDate");
            paramValues.put("receiptDate", materialReceiptSearch.getReceiptDate());
        }

        if (materialReceiptSearch.getReceiptType() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("receipttype = :receiptType");
            paramValues.put("receiptType", materialReceiptSearch.getReceiptType());
        }

        if (materialReceiptSearch.getReceivingStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("receivingstore = :receivingStore");
            paramValues.put("receivingStore", materialReceiptSearch.getReceivingStore());
        }

        if (materialReceiptSearch.getSupplierCode() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("suppliercode = :supplierCode");
            paramValues.put("supplierCode", materialReceiptSearch.getSupplierCode());
        }

        if (materialReceiptSearch.getIssueingStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("issueingstore = :issueingStore");
            paramValues.put("issueingStore", materialReceiptSearch.getIssueingStore());
        }

        if (materialReceiptSearch.getReceiptPurpose() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("receiptpurpose = :receiptPurpose");
            paramValues.put("receiptPurpose", materialReceiptSearch.getReceiptPurpose());
        }

        if (materialReceiptSearch.getFinancialYear() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("financialyear = :financialYear");
            paramValues.put("financialYear", materialReceiptSearch.getFinancialYear());
        }

        if (materialReceiptSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", materialReceiptSearch.getTenantId());
        }

        Pagination<MaterialReceipt> page = new Pagination<>();
        if (materialReceiptSearch.getPageSize() != null)
            page.setPageSize(materialReceiptSearch.getPageSize());
        if (materialReceiptSearch.getOffset() != null)
            page.setOffset(materialReceiptSearch.getOffset());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        page = (Pagination<MaterialReceipt>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialReceiptEntity.class);

        List<MaterialReceipt> materialReceipts = new ArrayList<>();

        List<MaterialReceiptEntity> materialReceiptEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (MaterialReceiptEntity materialReceiptEntity : materialReceiptEntities) {

            materialReceipts.add(materialReceiptEntity.toDomain());
        }

        page.setTotalResults(materialReceipts.size());

        page.setPagedData(materialReceipts);

        return page;
    }


}
