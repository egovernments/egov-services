package org.egov.inv.persistence.repository;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.SupplierBillAdvanceAdjustment;
import org.egov.inv.model.SupplierBillAdvanceAdjustmentSearch;
import org.egov.inv.persistence.entity.SupplierBillAdvanceAdjustmentEntity;
import org.egov.inv.persistence.entity.SupplierBillReceiptEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierBillAdvanceAdjusmentJdbcRepository extends JdbcRepository {


    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SupplierBillAdvanceAdjusmentJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    static {
        init(SupplierBillAdvanceAdjustmentEntity.class);
    }

    public Pagination<SupplierBillAdvanceAdjustment> search(SupplierBillAdvanceAdjustmentSearch supplierBillAdvanceAdjustmentSearch) {
        String searchQuery = "select * from supplierbilladvanceadjustment :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (supplierBillAdvanceAdjustmentSearch.getSortBy() != null && !supplierBillAdvanceAdjustmentSearch.getSortBy().isEmpty()) {
            validateSortByOrder(supplierBillAdvanceAdjustmentSearch.getSortBy());
            validateEntityFieldName(supplierBillAdvanceAdjustmentSearch.getSortBy(), SupplierBillAdvanceAdjustmentSearch.class);
        }
        String orderBy = "order by id";

        if (supplierBillAdvanceAdjustmentSearch.getSortBy() != null && !supplierBillAdvanceAdjustmentSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + supplierBillAdvanceAdjustmentSearch.getSortBy();
        }

        if (supplierBillAdvanceAdjustmentSearch.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", supplierBillAdvanceAdjustmentSearch.getIds());
        }

        if (supplierBillAdvanceAdjustmentSearch.getSupplierBill() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("supplierbill = :supplierBill");
            paramValues.put("supplierBill", supplierBillAdvanceAdjustmentSearch.getSupplierBill());
        }

        if (supplierBillAdvanceAdjustmentSearch.getSupplierAdvanceRequisition() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("supplieradvancerequisition = :supplierAdvanceRequisition");
            paramValues.put("supplierAdvanceRequisition", supplierBillAdvanceAdjustmentSearch.getSupplierAdvanceRequisition());
        }

        if (supplierBillAdvanceAdjustmentSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", supplierBillAdvanceAdjustmentSearch.getTenantId());
        }

        Pagination<SupplierBillAdvanceAdjustment> page = new Pagination<>();
        if (supplierBillAdvanceAdjustmentSearch.getPageSize() != null)
            page.setPageSize(supplierBillAdvanceAdjustmentSearch.getPageSize());

        if (supplierBillAdvanceAdjustmentSearch.getOffset() != null)
            page.setOffset(supplierBillAdvanceAdjustmentSearch.getOffset());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<SupplierBillAdvanceAdjustment>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(SupplierBillAdvanceAdjustmentEntity.class);

        List<SupplierBillAdvanceAdjustment> supplierBillAdvanceAdjustments = new ArrayList<>();

        List<SupplierBillAdvanceAdjustmentEntity> billAdvanceAdjustmentEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (SupplierBillAdvanceAdjustmentEntity supplierBillAdvanceAdjustmentEntity : billAdvanceAdjustmentEntities) {

            supplierBillAdvanceAdjustments.add(supplierBillAdvanceAdjustmentEntity.toDomain());
        }

        page.setTotalResults(supplierBillAdvanceAdjustments.size());

        page.setPagedData(supplierBillAdvanceAdjustments);

        return page;
    }

}
