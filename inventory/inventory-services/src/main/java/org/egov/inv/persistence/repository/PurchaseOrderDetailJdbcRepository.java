package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.Pagination;
import org.egov.inv.domain.service.UomService;
import org.egov.inv.model.PurchaseOrderDetail;
import org.egov.inv.model.PurchaseOrderDetailSearch;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.persistence.entity.PurchaseOrderDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderDetailJdbcRepository extends org.egov.common.JdbcRepository {
	
    @Autowired
    private UomService uomService;
	
    private static final Logger LOG = LoggerFactory.getLogger(PurchaseOrderDetailJdbcRepository.class);

    static {
        LOG.debug("init purchase order detail");
        init(PurchaseOrderDetailEntity.class);
        LOG.debug("end init purchase order detail");
    }

    public PurchaseOrderDetailJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public PurchaseOrderDetailEntity create(PurchaseOrderDetailEntity entity) {
        super.create(entity);
        return entity;
    }

    public PurchaseOrderDetailEntity update(PurchaseOrderDetailEntity entity) {
        super.update(entity);
        return entity;

    }

    public boolean delete(PurchaseOrderDetailEntity entity, String reason) {
        super.delete(entity, reason);
        return true;

    }

    public PurchaseOrderDetailEntity findById(PurchaseOrderDetailEntity entity) {
        List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

        Map<String, Object> paramValues = new HashMap<>();

        for (String s : list) {
            paramValues.put(s, getValue(getField(entity, s), entity));
        }

        List<PurchaseOrderDetailEntity> poDetails = namedParameterJdbcTemplate.query(
                getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
                new BeanPropertyRowMapper(PurchaseOrderDetailEntity.class));
        if (poDetails.isEmpty()) {
            return null;
        } else {
            return poDetails.get(0);
        }

    }

    public Pagination<PurchaseOrderDetail> search(PurchaseOrderDetailSearch purchaseOrderDetailSearch) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (purchaseOrderDetailSearch.getSortBy() != null && !purchaseOrderDetailSearch.getSortBy().isEmpty()) {
            validateSortByOrder(purchaseOrderDetailSearch.getSortBy());
            validateEntityFieldName(purchaseOrderDetailSearch.getSortBy(), PurchaseOrderDetailEntity.class);
        }

        String orderBy = "order by purchaseorder ";
        if (purchaseOrderDetailSearch.getSortBy() != null && !purchaseOrderDetailSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + purchaseOrderDetailSearch.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", PurchaseOrderDetailEntity.TABLE_NAME);

        searchQuery = searchQuery.replace(":selectfields", " * ");


        if (purchaseOrderDetailSearch.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", purchaseOrderDetailSearch.getIds());
        }

        if (purchaseOrderDetailSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", purchaseOrderDetailSearch.getTenantId());
        }

        if (purchaseOrderDetailSearch.getOrdernumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append(" ordernumber =:orderNumber");
            paramValues.put("orderNumber", purchaseOrderDetailSearch.getOrdernumber());
        }

        if (purchaseOrderDetailSearch.getPurchaseOrder() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append(" purchaseorder =:purchaseOrder");
            paramValues.put("purchaseOrder", purchaseOrderDetailSearch.getPurchaseOrder());
        }

        if (purchaseOrderDetailSearch.getMaterial() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("material =:material");
            paramValues.put("material", purchaseOrderDetailSearch.getMaterial());
        }

        Pagination<PurchaseOrderDetail> page = new Pagination<>();
        if (purchaseOrderDetailSearch.getPageNumber() != null) {
            page.setOffset(purchaseOrderDetailSearch.getPageNumber() - 1);
        }
        if (purchaseOrderDetailSearch.getPageSize() != null) {
            page.setPageSize(purchaseOrderDetailSearch.getPageSize());
        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where deleted is not true and  " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);// orderBy
        System.out.println(searchQuery);
        page = (Pagination<PurchaseOrderDetail>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(PurchaseOrderDetailEntity.class);

        List<PurchaseOrderDetailEntity> purchaseOrderDetailEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues, row);

        page.setTotalResults(purchaseOrderDetailEntities.size());

        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
        for (PurchaseOrderDetailEntity poEntity : purchaseOrderDetailEntities) {

            purchaseOrderDetails.add(poEntity.toDomain());
        }
		for (PurchaseOrderDetail eachPoDetail : purchaseOrderDetails) {
			eachPoDetail.setUom(uomService.getUom(purchaseOrderDetailSearch.getTenantId(), eachPoDetail.getUom().getCode(),new RequestInfo()));
			if (eachPoDetail.getIndentQuantity() != null) {
				eachPoDetail.setIndentQuantity(eachPoDetail.getIndentQuantity().divide(eachPoDetail.getUom().getConversionFactor()));
			}
			if (eachPoDetail.getTenderQuantity() != null) {
				eachPoDetail.setTenderQuantity(eachPoDetail.getTenderQuantity().divide(eachPoDetail.getUom().getConversionFactor()));
			}
			if (eachPoDetail.getUsedQuantity() != null) {
				eachPoDetail.setUsedQuantity(eachPoDetail.getUsedQuantity().divide(eachPoDetail.getUom().getConversionFactor()));
			}
			if (eachPoDetail.getUnitPrice() != null) {
				eachPoDetail.setUnitPrice(eachPoDetail.getUnitPrice().multiply(eachPoDetail.getUom().getConversionFactor()));
			}
		}
        page.setPagedData(purchaseOrderDetails);

        return page;
    }

}