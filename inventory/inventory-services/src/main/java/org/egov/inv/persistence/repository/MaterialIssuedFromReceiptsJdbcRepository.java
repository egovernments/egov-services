package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialIssuedFromReceipt;
import org.egov.inv.persistence.entity.MaterialIssuedFromReceiptEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;



@Service
public class MaterialIssuedFromReceiptsJdbcRepository extends JdbcRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	 private static final String updateQuery = "update materialissuedfromreceipt set status = false where id =:id and tenantId =:tenantId";
	
	public void  updateStatus(List<String> materialIssuedFromReceiptsIds, String tenantId){
		 List<Map<String,String>> batchValues = new ArrayList<>();
		 for(String id :materialIssuedFromReceiptsIds){
		 Map<String,String> paramValues = new HashMap<>();
		 paramValues.put("id", id);
		 paramValues.put("tenantId", tenantId);
		 batchValues.add(paramValues);
		 }
		 namedParameterJdbcTemplate.batchUpdate(updateQuery, batchValues.toArray(new Map
					[materialIssuedFromReceiptsIds.size()]));
	 }

	public Pagination<MaterialIssuedFromReceipt> search(String id, String tenantId) {
		String searchQuery = "select * from materialissuedfromreceipt :condition :orderby";
		StringBuffer params = new StringBuffer();
		String orderBy = "order by id";
		Map<String, Object> paramValues = new HashMap<>();
		if(id != null)
		{
			if (params.length() > 0)
				params.append(" and ");
			params.append("issuedetailid = :issuedetailid");
			paramValues.put("issuedetailid", id);
		}
		if(tenantId != null)
		{
			if (params.length() > 0)
				params.append(" and ");
			params.append("tenantId = :tenantId");
			paramValues.put("tenantId", tenantId);
		}
		if (params.length() > 0)
			searchQuery = searchQuery.replace(":condition", " where deleted is not true and " + params.toString());
		else
			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);
		Pagination<MaterialIssuedFromReceipt> page = new Pagination<>();
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialIssuedFromReceiptEntity.class);

		List<MaterialIssuedFromReceipt> materialIssuedFromReceiptList = new ArrayList<>();

		List<MaterialIssuedFromReceiptEntity> materialIssuedFromReceiptEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);

		for (MaterialIssuedFromReceiptEntity materialIssuedFromReceiptEntity : materialIssuedFromReceiptEntities) {

			materialIssuedFromReceiptList.add(materialIssuedFromReceiptEntity.toDomain());
		}

		page.setTotalResults(materialIssuedFromReceiptList.size());

		page.setPagedData(materialIssuedFromReceiptList);

		return page;
		
	}
	

}
