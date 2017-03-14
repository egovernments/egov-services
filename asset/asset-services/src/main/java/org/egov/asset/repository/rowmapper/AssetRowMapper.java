/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.asset.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.Department;
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AssetRowMapper implements RowMapper<Asset> {

	ObjectMapper mapper=new ObjectMapper();
	@Override
	public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
		Asset asset = new Asset();
		
		asset.setId(rs.getLong("assetId"));
		asset.setName(rs.getString("assetname"));
		asset.setCode(rs.getString("assetcode"));
		asset.setAssetDetails(rs.getString("assetDetails"));
		//TODO asset.setTenantId(rs.getString("tenantId"));
		asset.setModeOfAcquisition(ModeOfAcquisition.fromValue(rs.getString("modeofacquisition"))); 
		asset.setStatus(Status.fromValue("status"));
		asset.setDescription(rs.getString("description"));
		asset.setDateOfCreation(rs.getDate("dateOfCreation"));
		asset.setRemarks(rs.getString("remarks"));
		asset.setLength(rs.getString("length"));
		asset.setWidth(rs.getString("width"));
		asset.setTotalArea(rs.getString("totalArea"));
		
		String properties=rs.getString("properties");
		Asset asset2=null;
		
		try {
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			asset2=mapper.readValue(properties, Asset.class);
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
		
		asset.setProperties(asset2.getProperties());
		
		Department department=new Department();
		department.setId(rs.getLong("department"));
		asset.setDepartment(department);
		
		Location location=new Location();
		location.setBlock(rs.getLong("block"));
		location.setLocality(rs.getLong("locality"));
		location.setDoorNo(rs.getLong("doorNo"));
		location.setElectionWard(rs.getLong("electionWard"));
		location.setRevenueWard(rs.getLong("revenueWard"));
		location.setPinCode(rs.getLong("pincode"));
		location.setZone(rs.getLong("zone"));
		location.setStreet(rs.getLong("street"));
		asset.setLocationDetails(location);
		
		AssetCategory assetCategory=new AssetCategory();
		assetCategory.setId(rs.getLong("assetcategoryId"));
		assetCategory.setAccumulatedDepreciationAccount(rs.getLong("accumulatedDepreciationAccount"));
		assetCategory.setAssetAccount(rs.getLong("assetAccount"));
		assetCategory.setName(rs.getString("assetCategoryName"));
		assetCategory.setCode(rs.getString("assetcategorycode"));
		assetCategory.setParent(rs.getLong("parentId"));
		assetCategory.setDepreciationExpenseAccount(rs.getLong("depreciationExpenseAccount"));
		assetCategory.setDepreciationMethod(DepreciationMethod.fromValue(rs.getString("depreciationMethod")));
		assetCategory.setAccumulatedDepreciationAccount(rs.getLong("accumulatedDepreciationAccount"));
		assetCategory.setRevaluationReserveAccount(rs.getLong("revaluationReserveAccount"));
		assetCategory.setUnitOfMeasurement(rs.getLong("unitOfMeasurement"));
		//assetCategory.setCustomFields(rs.getString("customFields"));
		
		
		asset.setAssetCategory(assetCategory);
		
		return asset;
	}
}