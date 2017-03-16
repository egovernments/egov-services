
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
		assetCategory.setAccumulatedDepreciationAccount((Long)rs.getObject("accumulateddepreciationaccount"));
		assetCategory.setAssetAccount((Long)rs.getObject("assetaccount"));
		assetCategory.setAssetCategoryType(AssetCategoryType.fromValue(rs.getString("assetcategorytype")));
		assetCategory.setCode(rs.getString("code"));
		String json=rs.getString("customfields");
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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
		assetCategory.setCustomFields(obj.getCustomFields());
		
		assetCategory.setDepreciationExpenseAccount((Long)rs.getObject("depreciationexpenseaccount"));
		assetCategory.setDepreciationMethod(DepreciationMethod.fromValue(rs.getString("depreciationmethod")));
		//Todo DepreciationRate
		//	assetCategory.setDepreciationRate(rs.getInt("depreciationrate"));
		assetCategory.setId((Long)rs.getObject("id"));
		assetCategory.setName(rs.getString("name"));
		assetCategory.setParent((Long)rs.getObject("parentid"));
		assetCategory.setRevaluationReserveAccount((Long)rs.getObject("revaluationreserveaccount"));
		assetCategory.setUnitOfMeasurement((Long)rs.getObject("unitofmeasurement"));
		
		return assetCategory;
	}

}