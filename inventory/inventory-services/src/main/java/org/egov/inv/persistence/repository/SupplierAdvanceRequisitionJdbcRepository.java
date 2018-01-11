package org.egov.inv.persistence.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.SupplierAdvanceRequisition;
import org.egov.inv.model.SupplierAdvanceRequisitionSearch;
import org.egov.inv.model.SupplierBillAdvanceAdjustmentSearch;
import org.egov.inv.persistence.entity.SupplierAdvanceRequisitionEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SupplierAdvanceRequisitionJdbcRepository extends JdbcRepository {


    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SupplierAdvanceRequisitionJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    static {
        init(SupplierAdvanceRequisitionEntity.class);
    }

    public Pagination<SupplierAdvanceRequisition> search(SupplierAdvanceRequisitionSearch supplierAdvanceRequisitionSearch) {
        String searchQuery = "select * from supplieradvancerequisition :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (supplierAdvanceRequisitionSearch.getSortBy() != null && !supplierAdvanceRequisitionSearch.getSortBy().isEmpty()) {
            validateSortByOrder(supplierAdvanceRequisitionSearch.getSortBy());
            validateEntityFieldName(supplierAdvanceRequisitionSearch.getSortBy(), SupplierBillAdvanceAdjustmentSearch.class);
        }
        String orderBy = "order by id";

        if (supplierAdvanceRequisitionSearch.getSortBy() != null && !supplierAdvanceRequisitionSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + supplierAdvanceRequisitionSearch.getSortBy();
        }

        if (supplierAdvanceRequisitionSearch.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", supplierAdvanceRequisitionSearch.getIds());
        }

        if (supplierAdvanceRequisitionSearch.getSupplier() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("supplier = :supplier");
            paramValues.put("supplier", supplierAdvanceRequisitionSearch.getSupplier());
        }

        if (supplierAdvanceRequisitionSearch.getPurchaseOrder() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("purchaseorder = :purchaseOrder");
            paramValues.put("purchaseOrder", supplierAdvanceRequisitionSearch.getPurchaseOrder());
        }

        if (supplierAdvanceRequisitionSearch.getStateId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("stateid = :stateId");
            paramValues.put("stateId", supplierAdvanceRequisitionSearch.getStateId());
        }

        if (supplierAdvanceRequisitionSearch.getSarStatus() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("status = :status");
            paramValues.put("status", supplierAdvanceRequisitionSearch.getSarStatus());
        }

        if (supplierAdvanceRequisitionSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", supplierAdvanceRequisitionSearch.getTenantId());
        }

        Pagination<SupplierAdvanceRequisition> page = new Pagination<>();
        if (supplierAdvanceRequisitionSearch.getPageSize() != null)
            page.setPageSize(supplierAdvanceRequisitionSearch.getPageSize());

        if (supplierAdvanceRequisitionSearch.getOffset() != null)
            page.setOffset(supplierAdvanceRequisitionSearch.getOffset());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<SupplierAdvanceRequisition>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(SupplierAdvanceRequisitionEntity.class);

        List<SupplierAdvanceRequisition> supplierBillAdvanceAdjustments = new ArrayList<>();

        List<SupplierAdvanceRequisitionEntity> advanceRequisitionEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (SupplierAdvanceRequisitionEntity supplierAdvanceRequisitionEntity : advanceRequisitionEntities) {

            supplierBillAdvanceAdjustments.add(supplierAdvanceRequisitionEntity.toDomain());
        }

        page.setTotalResults(supplierBillAdvanceAdjustments.size());

        page.setPagedData(supplierBillAdvanceAdjustments);

        return page;
    }
    
	public boolean checkPOValidity(String purchaseordernumber, String tenantId) {
		String poValidityQuery = "select * from purchaseorder where purchaseordernumber = :purchaseordernumber and lower(status) != 'rejected' and  advanceamount > 0 and totalamount > 0 and (totaladvancepaidamount is null or totaladvancepaidamount != totalamount) and advanceamount < totalamount and isdeleted is not true and tenantId = :tenantId";
	    Map params=new HashMap<String,Object>();
	    params.put("tenantId",tenantId);
		params.put("purchaseordernumber",purchaseordernumber);
	    BigDecimal count = namedParameterJdbcTemplate.queryForObject(poValidityQuery, params, BigDecimal.class);
	    if(count.compareTo(BigDecimal.ZERO) > 0 )
	    	return true;
	    else
	    	return false;
	}
	
}
