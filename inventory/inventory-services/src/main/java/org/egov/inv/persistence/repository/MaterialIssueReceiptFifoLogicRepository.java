package org.egov.inv.persistence.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.inv.model.Material;
import org.egov.inv.model.Store;
import org.egov.inv.persistence.entity.FifoEntity;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class MaterialIssueReceiptFifoLogicRepository {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static final String query = "select * from (select materialreceipt.id as receiptid,rctdtl.id as receiptdetailid,rctdtl.podetailid as receiptdetailpodetailid ,"
			+ "receiptdate ,rctdtl.mrnnumber as mrnnumber, material, uomno,"
			+ " (COALESCE(addinfo.quantity,acceptedqty) -" + " COALESCE (case when addinfo.id is not null then"
			+ " (select sum(issuereceipt.quantity) from materialissuedfromreceipt"
			+ " issuereceipt where addinfo.id=issuereceipt.receiptdetailaddnlinfoid"
			+ " and issuereceipt.receiptdetailid=rctdtl.id and issuereceipt.status=true"
			+ " and (issuereceipt.deleted=false or issuereceipt.deleted is null) )"
			+ " else (select sum(issuereceipt.quantity) from materialissuedfromreceipt"
			+ " issuereceipt where issuereceipt.receiptdetailid=rctdtl.id"
			+ " and issuereceipt.status=true and (issuereceipt.deleted=false"
			+ " or issuereceipt.deleted is null) ) end,0)) as balance ,"
			+ " unitrate, addinfo.id as receiptdetailaddninfoid from"
			+ " materialreceipt left outer join materialreceiptdetail rctdtl"
			+ " on materialreceipt.mrnnumber = rctdtl.mrnnumber left outer join"
			+ " materialreceiptdetailaddnlinfo  addinfo on rctdtl.id= addinfo.receiptdetailid"
			+ " where  (isscrapitem IS NULL or isscrapitem=false) and (rctdtl.deleted=false or"
			+ " rctdtl.deleted is null ) and receivingstore= :store  and materialreceipt.tenantid= :tenantId"
			+ " and  material= :material and mrnstatus in ('APPROVED') and receiptdate <= :date";

	public List<FifoEntity> implementFifoLogic(Store store, Material material, Long issueDate, String tenantId) {
		Map<String, Object> paramValues = new HashMap<>();
		StringBuilder baseQuery = new StringBuilder(query);
		baseQuery.append(" order by addinfo.expirydate,addinfo.receiveddate,receiptdate)as fifo where balance >0");
		if(store.getCode() == null || material.getCode() == null || issueDate == null || tenantId == null)
		    throw new CustomException("required.fields","please provide all fields required for the api");
		else{
		  paramValues.put("store", store.getCode());
		  paramValues.put("tenantId", tenantId);
		  paramValues.put("material", material.getCode());
		  paramValues.put("date", issueDate);
		 
		}
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(FifoEntity.class);
		List<FifoEntity> listOfFifoEntities = namedParameterJdbcTemplate.query(baseQuery.toString(), paramValues, row);
		return listOfFifoEntities;

	}

	public List<FifoEntity> implementFifoLogicForReturnMaterial(Store store, Material material, Long issueDate,
			String tenantId, String mrnNumber) {
		Map<String, Object> paramValues = new HashMap<>();
		StringBuilder baseQuery = new StringBuilder(query);
		baseQuery.append(" and materialreceipt.mrnNumber= :mrnNumber order by addinfo.expirydate,addinfo.receiveddate,receiptdate)as fifo where balance >0");
		if(store.getCode() == null || material.getCode() == null || issueDate == null || tenantId == null || mrnNumber == null)
		    throw new CustomException("required.fields","please provide all fields required for the api");
		else{
			  paramValues.put("store", store.getCode());
			  paramValues.put("tenantId", tenantId);
			  paramValues.put("material", material.getCode());
			  paramValues.put("date", issueDate);
			  paramValues.put("mrnNumber", mrnNumber);
			}
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(FifoEntity.class);
		List<FifoEntity> listOfFifoEntities = namedParameterJdbcTemplate.query(baseQuery.toString(), paramValues, row);
		return listOfFifoEntities;
	}
	
	
}
