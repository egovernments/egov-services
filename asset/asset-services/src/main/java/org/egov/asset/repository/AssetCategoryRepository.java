
package org.egov.asset.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.repository.builder.AssetCategoryQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetCategoryRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class AssetCategoryRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private AssetCategoryQueryBuilder assetCategoryQueryBuilder;
	
	public List<AssetCategory> search(AssetCategoryCriteria assetCategoryCriteria){
		
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = assetCategoryQueryBuilder.getQuery(assetCategoryCriteria, preparedStatementValues);
		
		List<AssetCategory> assetCategory = null;
		try{
			assetCategory=jdbcTemplate.query(queryStr,preparedStatementValues.toArray(), new AssetCategoryRowMapper());
		}catch(Exception exception){
			exception.printStackTrace();
		}
	   return assetCategory;
	}
	
	public String getAssetCategoryCode(){
		String query = "SELECT nextval('seq_egasset_categorycode')";
		Integer result = jdbcTemplate.queryForObject(query,Integer.class);
		System.out.println("result:"+result);
		//String code=String.format("%03d", result);
		StringBuilder code=null;
		try{
		 code = new StringBuilder(String.format("%03d", result));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return code.toString();
	}
	
	public AssetCategory create(AssetCategoryRequest assetCategoryRequest){
		
		RequestInfo requestInfo=assetCategoryRequest.getRequestInfo();
		AssetCategory assetCategory=assetCategoryRequest.getAssetCategory();
		String queryStr = assetCategoryQueryBuilder.getInsertQuery();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		AssetCategory assetCategory2=new AssetCategory();
		assetCategory2.setCustomFields(assetCategory.getCustomFields());
		
		String customFields = null;
		String assetCategoryType = null;
		String depreciationMethod = null;
		
		if(assetCategory.getAssetCategoryType()!=null)
			assetCategoryType = assetCategory.getAssetCategoryType().toString();
			
		if(assetCategory.getDepreciationMethod()!=null)
			depreciationMethod = assetCategory.getDepreciationMethod().toString();
		
		try {
			customFields = mapper.writeValueAsString(assetCategory2);
			System.out.println("customFields:::"+customFields);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		//TODO depreciationrate as of now hardcoded as  3L
		Object[] obj = new Object[] {assetCategory.getName(), assetCategory.getCode(), assetCategory.getParent(),
				assetCategoryType, depreciationMethod,3L, assetCategory.getAssetAccount(), assetCategory.getAccumulatedDepreciationAccount(),
				assetCategory.getRevaluationReserveAccount(), assetCategory.getDepreciationExpenseAccount(), assetCategory.getUnitOfMeasurement(),
				customFields, assetCategory.getTenantId(), requestInfo.getMsgId(), new Date(), requestInfo.getMsgId(), new Date()};

		try {
			 jdbcTemplate.update(queryStr, obj);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return assetCategory;
	}
}
