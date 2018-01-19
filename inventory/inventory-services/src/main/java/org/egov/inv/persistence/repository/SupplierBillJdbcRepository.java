package org.egov.inv.persistence.repository;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.CoaAmountGroup;
import org.egov.inv.model.SupplierBill;
import org.egov.inv.model.SupplierBillSearch;
import org.egov.inv.persistence.entity.SupplierBillEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierBillJdbcRepository extends JdbcRepository {


    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SupplierBillJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    static {
        init(SupplierBillEntity.class);
    }

    public Pagination<SupplierBill> search(SupplierBillSearch supplierBillSearch) {
        String searchQuery = "select * from supplierbill :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (supplierBillSearch.getSortBy() != null && !supplierBillSearch.getSortBy().isEmpty()) {
            validateSortByOrder(supplierBillSearch.getSortBy());
            validateEntityFieldName(supplierBillSearch.getSortBy(), SupplierBillSearch.class);
        }
        String orderBy = "order by id";

        if (supplierBillSearch.getSortBy() != null && !supplierBillSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + supplierBillSearch.getSortBy();
        }

        if (supplierBillSearch.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", supplierBillSearch.getIds());
        }

        if (supplierBillSearch.getStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("store = :store");
            paramValues.put("store", supplierBillSearch.getStore());
        }

        if (supplierBillSearch.getApprovedDate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("approveddate = :approvedDate");
            paramValues.put("approvedDate", supplierBillSearch.getApprovedDate());
        }

        if (supplierBillSearch.getInvoiceNumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("invoicenumber = :invoiceNumber");
            paramValues.put("invoiceNumber", supplierBillSearch.getInvoiceNumber());
        }

        if (supplierBillSearch.getInvoiceDate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("invoicedate = :invoiceDate");
            paramValues.put("invoiceDate", supplierBillSearch.getInvoiceDate());
        }

        if (supplierBillSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", supplierBillSearch.getTenantId());
        }

        Pagination<SupplierBill> page = new Pagination<>();
        if (supplierBillSearch.getPageSize() != null)
            page.setPageSize(supplierBillSearch.getPageSize());

        if (supplierBillSearch.getOffset() != null)
            page.setOffset(supplierBillSearch.getOffset());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<SupplierBill>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(SupplierBillEntity.class);

        List<SupplierBill> supplierBillList = new ArrayList<>();

        List<SupplierBillEntity> supplierBillEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (SupplierBillEntity supplierBillEntity : supplierBillEntities) {

            supplierBillList.add(supplierBillEntity.toDomain());
        }

        page.setTotalResults(supplierBillList.size());

        page.setPagedData(supplierBillList);

        return page;
    }


    public List<CoaAmountGroup> getGroupedCOAAmounts(List<String> mrnNumbers, String tenantId) {
        String query = "select msm.chartofaccount, (sum(mrd.acceptedqty * mrd.unitrate)) as amount\n" +
                "from materialstoremapping msm, materialreceipt mr , materialreceiptdetail mrd \n" +
                "where mrd.material = msm.material \n" +
                "and mrd.mrnnumber = mr.mrnnumber\n" +
                "and msm.tenantid = mrd.tenantid\n" +
                "and mrd.tenantid = mr.tenantid\n" +
                "and mr.mrnnumber in (:mrnNumbers) \n" +
                "and mr.tenantid = :tenantId \n" +
                "group by msm.chartofaccount;\n";

        Map params = new HashMap<String, Object>();
        params.put("tenantId", tenantId);
        params.put("mrnNumbers", mrnNumbers);
        List<CoaAmountGroup> coaAmountGroups = namedParameterJdbcTemplate.query(query, params, new BeanPropertyRowMapper<>(CoaAmountGroup.class));

        return coaAmountGroups;
    }
}
