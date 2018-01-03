package org.egov.inv.persistence.repository;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.SupplierBillReceipt;
import org.egov.inv.model.SupplierBillReceiptSearch;
import org.egov.inv.model.SupplierBillSearch;
import org.egov.inv.persistence.entity.SupplierBillReceiptEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierBillReceiptJdbcRepository extends JdbcRepository {


    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SupplierBillReceiptJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    static {
        init(SupplierBillReceiptEntity.class);
    }

    public Pagination<SupplierBillReceipt> search(SupplierBillReceiptSearch supplierBillReceiptSearch) {
        String searchQuery = "select * from supplierbillreceipt :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (supplierBillReceiptSearch.getSortBy() != null && !supplierBillReceiptSearch.getSortBy().isEmpty()) {
            validateSortByOrder(supplierBillReceiptSearch.getSortBy());
            validateEntityFieldName(supplierBillReceiptSearch.getSortBy(), SupplierBillSearch.class);
        }
        String orderBy = "order by id";

        if (supplierBillReceiptSearch.getSortBy() != null && !supplierBillReceiptSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + supplierBillReceiptSearch.getSortBy();
        }

        if (supplierBillReceiptSearch.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", supplierBillReceiptSearch.getIds());
        }

        if (supplierBillReceiptSearch.getSupplierBill() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("supplierbill = :supplierBill");
            paramValues.put("supplierBill", supplierBillReceiptSearch.getSupplierBill());
        }

        if (supplierBillReceiptSearch.getMaterialReceipt() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("materialreceipt = :materialReceipt");
            paramValues.put("materialReceipt", supplierBillReceiptSearch.getMaterialReceipt());
        }


        if (supplierBillReceiptSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", supplierBillReceiptSearch.getTenantId());
        }

        Pagination<SupplierBillReceipt> page = new Pagination<>();
        if (supplierBillReceiptSearch.getPageSize() != null)
            page.setPageSize(supplierBillReceiptSearch.getPageSize());

        if (supplierBillReceiptSearch.getOffset() != null)
            page.setOffset(supplierBillReceiptSearch.getOffset());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<SupplierBillReceipt>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(SupplierBillReceiptEntity.class);

        List<SupplierBillReceipt> supplierBillReceipts = new ArrayList<>();

        List<SupplierBillReceiptEntity> supplierBillReceiptEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (SupplierBillReceiptEntity supplierBillReceiptEntity : supplierBillReceiptEntities) {

            supplierBillReceipts.add(supplierBillReceiptEntity.toDomain());
        }

        page.setTotalResults(supplierBillReceipts.size());

        page.setPagedData(supplierBillReceipts);

        return page;
    }

}
