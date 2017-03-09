package org.egov.asset.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AssetCategoryRowMapper implements RowMapper<AssetCategory>{

	@Override
	public AssetCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		AssetCategory assetCategory=new AssetCategory();
		assetCategory.setAccumulatedDepreciationAccount(rs.getLong("accumulateddepreciationaccount"));
		assetCategory.setAssetAccount(rs.getLong("assetaccount"));
		assetCategory.setAssetCategoryType(AssetCategoryType.fromValue(rs.getString("assetcategorytype")));
		assetCategory.setCode(rs.getString("code"));
		String json=rs.getString("customfields");
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		AssetCategory obj=null;
		try {
			obj = objectMapper.readValue(json, AssetCategory.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(obj!=null) 
		assetCategory.setCustomFields(obj.getCustomFields());
		
		assetCategory.setDepreciationExpenseAccount(rs.getLong("depreciationexpenseaccount"));
		assetCategory.setDepreciationMethod(DepreciationMethod.fromValue(rs.getString("depreciationmethod")));
		//Todo
		//	assetCategory.setDepreciationRate(rs.getInt("depreciationrate"));
		assetCategory.setId(rs.getLong("id"));
		assetCategory.setName(rs.getString("name"));
		assetCategory.setParent(rs.getLong("parentid"));
		assetCategory.setRevaluationReserveAccount(rs.getLong("revaluationreserveaccount"));
		assetCategory.setUnitOfMeasurement(rs.getLong("unitofmeasurement"));
		
		return assetCategory;
	}

}
