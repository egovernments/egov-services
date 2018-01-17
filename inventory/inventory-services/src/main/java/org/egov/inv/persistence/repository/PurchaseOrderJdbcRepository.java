package org.egov.inv.persistence.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.Pagination;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.PriceListDetailsSearchRequest;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.model.PurchaseOrder;
import org.egov.inv.model.PurchaseOrderSearch;
import org.egov.inv.persistence.entity.PurchaseOrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderJdbcRepository extends org.egov.common.JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(PurchaseOrderJdbcRepository.class);

	@Autowired
	PriceListDetailJdbcRepository priceListDetailJdbcRepository;
	
	@Autowired
	PriceListJdbcRepository priceListJdbcRepository;
	
	static {
		LOG.debug("init purchase order");
		init(PurchaseOrderEntity.class);
		LOG.debug("end init purchase order");
	}
	
	public static synchronized void init(Class T) {
        String TABLE_NAME = "";

        List<String> insertFields = new ArrayList<>();
        List<String> updateFields = new ArrayList<>();
        List<String> uniqueFields = new ArrayList<>();

        String insertQuery = "";
        String updateQuery = "";
        String searchQuery = "";

        try {

            TABLE_NAME = (String) T.getDeclaredField("TABLE_NAME").get(null);
        } catch (Exception e) {

        }
        insertFields.addAll(fetchFields(T));
        uniqueFields.add("purchaseOrderNumber");
        uniqueFields.add("tenantId");
        insertFields.removeAll(uniqueFields);
        allInsertQuery.put(T.getSimpleName(), insertQuery(insertFields, TABLE_NAME, uniqueFields));
        updateFields.addAll(insertFields);
        updateFields.remove("createdBy");
        updateQuery = updateQuery(updateFields, TABLE_NAME, uniqueFields);
        System.out.println(T.getSimpleName() + "--------" + insertFields);
        allInsertFields.put(T.getSimpleName(), insertFields);
        allUpdateFields.put(T.getSimpleName(), updateFields);
        allIdentitiferFields.put(T.getSimpleName(), uniqueFields);
        // allInsertQuery.put(T.getSimpleName(), insertQuery);
        allUpdateQuery.put(T.getSimpleName(), updateQuery);
        getByIdQuery.put(T.getSimpleName(), getByIdQuery(TABLE_NAME, uniqueFields));
        System.out.println(allInsertQuery);
    }

	public PurchaseOrderJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public PurchaseOrderEntity create(PurchaseOrderEntity entity) {
		super.create(entity);
		return entity;
	}

	public PurchaseOrderEntity update(PurchaseOrderEntity entity) {
		super.update(entity);
		return entity;

	}

	public boolean delete(PurchaseOrderEntity entity, String reason) {
		super.delete(entity, reason);
		return true;

	}

	public PurchaseOrderEntity findById(PurchaseOrderEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<PurchaseOrderEntity> purchaseorders = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(PurchaseOrderEntity.class));
		if (purchaseorders.isEmpty()) {
			return null;
		} else {
			return purchaseorders.get(0);
		}

	}

	public Long getUsedQty(String supplier, String material, String ratetype){
	    String usedQtyQuery = "select sum(orderquantity) from purchaseorderdetail where material = :material and purchaseorder in (select purchaseordernumber from purchaseorder where isdeleted is not true and ratetype=:ratetype and supplier = :supplier)";
	    Map params=new HashMap<String,Object>();
		params.put("material",material);
		params.put("ratetype",ratetype);
		params.put("supplier",supplier);
	    Long usedQty=namedParameterJdbcTemplate.queryForObject(usedQtyQuery, params, Long.class);
	    if(usedQty == null)
	    	return 0l;
	    return usedQty;
	}
	
	//TODO : expose this api to web layer
	// Consider status not in rejected,cancelled

	public BigDecimal getTenderUsedQty(String material, String priceListId){

	String usedQtyQuery = "select sum(orderquantity) from purchaseorderdetail pod, purchaseorder po where po.purchaseordernumber = pod.purchaseorder and po.status != 'rejected' and pod.pricelist = :pricelistid and pod.material=:material";
	Map params=new HashMap<String,Object>();
	params.put("material",material);
	params.put("pricelistid",priceListId);
	BigDecimal usedQty=namedParameterJdbcTemplate.queryForObject(usedQtyQuery, params, BigDecimal.class);
	if(usedQty == null)
	return BigDecimal.ZERO;
	return usedQty;
	}
	
	public boolean getIsIndentValidForPOCreate(String indentNumber) {
		String validityQuery = "select count(*) from indentdetail where indentquantity=poorderedquantity and indentNumber=:indentNumber";
		Map params=new HashMap<String,Object>();
		params.put("indentNumber",indentNumber);
		Long count = namedParameterJdbcTemplate.queryForObject(validityQuery, params, Long.class);
		if(count > 0)
			return false;
		else
			return true;
	}
	
	public boolean getIsIndentPORaised(String indentnumber) {
		String totalProcessedQuery = "select count(*) from indentdetail where indentquantity>totalprocessedquantity and totalprocessedquantity!=null and isdeleted is not true and indentnumber = :indentnumber";
		Map params=new HashMap<String,Object>();
		params.put("indentnumber", indentnumber);
		Long count = namedParameterJdbcTemplate.queryForObject(totalProcessedQuery, params, Long.class);
		if(count > 0)
			return true;
		else
			return false;
	}

	public boolean isRateContractsExists(String supplier, String ratetype, String material){
		String rateContractQuery = "select count(*) from pricelist pl, pricelistdetails pld where pld.pricelist=pl.id and pl.active=true and pld.active=true and pld.deleted is not true and pl.supplier=:supplier and pl.rateType=:ratetype and pld.material = :material and extract(epoch from now())::bigint * 1000 between pl.agreementstartdate and pl.agreementenddate";
	    Map params=new HashMap<String,Object>();
		params.put("material",material);
		params.put("ratetype",ratetype);
		params.put("supplier",supplier);
		Long count = namedParameterJdbcTemplate.queryForObject(rateContractQuery, params, Long.class);
		if(count > 0)
			return true;
		else
			return false;
	}
	
	public String getRateContracts(String material, String supplier) {
		
		PriceListDetailsSearchRequest pldsr = PriceListDetailsSearchRequest.builder().material(material).build();
		PriceListSearchRequest plsr = PriceListSearchRequest.builder().supplierName(supplier).build();
		List<PriceListDetails> lplds = priceListDetailJdbcRepository.search(pldsr).getPagedData();
		List<PriceList> lpl = priceListJdbcRepository.search(plsr).getPagedData();
		
		List<PriceList> commonpl = new ArrayList<>();
		for(PriceList pl:lpl) {
			for(PriceListDetails pld:lplds) {
				if(pl.getId().equals(pld.getPriceList())) {
					commonpl.add(pl);
				}
			}
		}
		
		String rateContracts = "";
		for(PriceList pl:commonpl) {
			rateContracts+=pl.getRateContractNumber() + ",";
		}
		return rateContracts.replaceAll(",$", "");
	}
	
	public Pagination<PurchaseOrder> search(PurchaseOrderSearch purchaseOrderSearch) {

		if(purchaseOrderSearch.getSearchPoAdvReq())
			return searchPOsForAdvanceRequisition(purchaseOrderSearch.getTenantId());
		
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (purchaseOrderSearch.getSortBy() != null && !purchaseOrderSearch.getSortBy().isEmpty()) {
			validateSortByOrder(purchaseOrderSearch.getSortBy());
			validateEntityFieldName(purchaseOrderSearch.getSortBy(), PurchaseOrderEntity.class);
		}

		String orderBy = "order by purchaseOrderNumber ";
		if (purchaseOrderSearch.getSortBy() != null && !purchaseOrderSearch.getSortBy().isEmpty()) {
			orderBy = "order by " + purchaseOrderSearch.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", PurchaseOrderEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search

		if (purchaseOrderSearch.getTenantId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", purchaseOrderSearch.getTenantId());
		}
		if (purchaseOrderSearch.getStore() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("store =:store ");
			paramValues.put("store", purchaseOrderSearch.getStore());
		}

		if (purchaseOrderSearch.getPurchaseOrderDate() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("purchaseOrderDate =:purchaseOrderDate");
			paramValues.put("purchaseOrderDate", purchaseOrderSearch.getPurchaseOrderDate());
		}
		if (purchaseOrderSearch.getPurchaseOrderNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append(" purchaseOrderNumber =:purchaseOrderNumber");
			paramValues.put("purchaseOrderNumber", purchaseOrderSearch.getPurchaseOrderNumber());
		}
		if (purchaseOrderSearch.getPurchaseType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append(" purchaseType =:purchaseType");
			paramValues.put("purchaseType", purchaseOrderSearch.getPurchaseType());
		}
		if (purchaseOrderSearch.getSupplier() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("supplier =:supplier");
			paramValues.put("supplier", purchaseOrderSearch.getSupplier());
		}
		if (purchaseOrderSearch.getRateType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("rateType =:rateType");
			paramValues.put("rateType", purchaseOrderSearch.getRateType());
		}

		if (purchaseOrderSearch.getStatus() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("status =:status");
			paramValues.put("status", purchaseOrderSearch.getStatus());
		}

		if (purchaseOrderSearch.getStateId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("stateId =:stateId");
			paramValues.put("stateId", purchaseOrderSearch.getStateId());
		}

		Pagination<PurchaseOrder> page = new Pagination<>();
		if (purchaseOrderSearch.getPageNumber() != null) {
			page.setOffset(purchaseOrderSearch.getPageNumber() - 1);
		}
		if (purchaseOrderSearch.getPageSize() != null) {
			page.setPageSize(purchaseOrderSearch.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where isdeleted is not true and  " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);// orderBy
		System.out.println(searchQuery);
		page = (Pagination<PurchaseOrder>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(PurchaseOrderEntity.class);

		List<PurchaseOrderEntity> purchaseOrderEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);

		page.setTotalResults(purchaseOrderEntities.size());

		List<PurchaseOrder> purchaseOrders = new ArrayList<>();
		for (PurchaseOrderEntity poEntity : purchaseOrderEntities) {

			purchaseOrders.add(poEntity.toDomain());
		}
		page.setPagedData(purchaseOrders);

		return page;
	}
	
	public Pagination<PurchaseOrder> searchPOsForAdvanceRequisition(String tenantId) {
		
		String searchQuery = "select * from purchaseorder where lower(status) != 'rejected' and  advanceamount > 0 and totalamount > 0 and (totaladvancepaidamount is null or totaladvancepaidamount != totalamount) and advanceamount < totalamount and isdeleted is not true and tenantId = :tenantId";
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(PurchaseOrderEntity.class);
		Map params=new HashMap<String,Object>();
		params.put("tenantId",tenantId);
		
		List<PurchaseOrderEntity> purchaseOrderEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				params, row);

		Pagination<PurchaseOrder> page = new Pagination<>();
		page.setTotalResults(purchaseOrderEntities.size());

		List<PurchaseOrder> purchaseOrders = new ArrayList<>();
		for (PurchaseOrderEntity poEntity : purchaseOrderEntities) {

			purchaseOrders.add(poEntity.toDomain());
		}
		page.setPagedData(purchaseOrders);

		return page;
	}
	
    public List<PurchaseOrder> searchPOForAdvanceRequisition(PurchaseOrderSearch purchaseOrderSearchRequest) {
        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        String searchQuery = "select * from purchaseorder :condition  :orderby ";

        if (purchaseOrderSearchRequest.getTenantId() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("tenantid =:tenantId");
            paramValues.put("tenantId", purchaseOrderSearchRequest.getTenantId());
        }

        searchQuery = searchQuery.replace(":condition", " where totalAdvancePaidAmount>0 and isdeleted is not true and lower(status)='approved' and " + params.toString());

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(PurchaseOrderEntity.class);

        List<PurchaseOrderEntity> purchaseOrderEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
                row);

        List<PurchaseOrder> purchaseOrderList = new ArrayList<>();

        PurchaseOrder pl;

        for (PurchaseOrderEntity purchaseOrderEntity : purchaseOrderEntities) {
            pl = purchaseOrderEntity.toDomain();
            purchaseOrderList.add(pl);
        }

        return purchaseOrderList;
    }

}