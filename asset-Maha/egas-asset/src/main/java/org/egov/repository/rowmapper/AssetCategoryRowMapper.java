package org.egov.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.model.AssetCategory;
import org.egov.model.enums.AssetCategoryType;
import org.egov.model.enums.DepreciationMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AssetCategoryRowMapper implements RowMapper<AssetCategory>{

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public AssetCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		AssetCategory assetCategory=new AssetCategory();
		assetCategory.setAccumulatedDepreciationAccount(rs.getString("accumulateddepreciationaccount"));
		assetCategory.setAssetAccount(rs.getString("assetaccount"));
		assetCategory.setAssetCategoryType(AssetCategoryType.fromValue(rs.getString("assetcategorytype")));
		assetCategory.setCode(rs.getString("code"));
		String json=rs.getString("customfields");
	
		AssetCategory obj=null;
		
		try {
			obj = objectMapper.readValue(json, AssetCategory.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if(obj!=null) 
		assetCategory.setAssetFieldsDefination(obj.getAssetFieldsDefination());
		
		assetCategory.setDepreciationExpenseAccount(rs.getString("depreciationexpenseaccount"));
		assetCategory.setDepreciationMethod(DepreciationMethod.fromValue(rs.getString("depreciationmethod")));
	
		assetCategory.setDepreciationRate(rs.getDouble("depreciationrate"));
		assetCategory.setId((Long)rs.getObject("id"));
		assetCategory.setTenantId(rs.getString("tenantid"));
		assetCategory.setName(rs.getString("name"));
		assetCategory.setParent((Long)rs.getObject("parentid"));
		assetCategory.setRevaluationReserveAccount(rs.getString("revaluationreserveaccount"));
		assetCategory.setUnitOfMeasurement(rs.getString("unitofmeasurement"));
		assetCategory.setIsAssetAllow(rs.getBoolean("isassetallow"));
		assetCategory.setVersion(rs.getString("version"));
		assetCategory.setIsDepreciationApplicable(rs.getBoolean("isdepreciationapplicable"));
		return assetCategory;
	}
}