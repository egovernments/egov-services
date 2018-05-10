/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.asset.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AssetCategoryRowMapper implements RowMapper<AssetCategory> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public AssetCategory mapRow(ResultSet rs, int rowNum) throws SQLException {

        AssetCategory assetCategory = new AssetCategory();
        assetCategory.setAccumulatedDepreciationAccount((Long) rs.getObject("accumulateddepreciationaccount"));
        assetCategory.setAssetAccount((Long) rs.getObject("assetaccount"));
        assetCategory.setAssetCategoryType(AssetCategoryType.fromValue(rs.getString("assetcategorytype")));
        assetCategory.setCode(rs.getString("code"));
        String json = rs.getString("customfields");

        AssetCategory obj = null;
        try {
            obj = objectMapper.readValue(json, AssetCategory.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (obj != null)
            assetCategory.setAssetFieldsDefination(obj.getAssetFieldsDefination());

        assetCategory.setDepreciationExpenseAccount((Long) rs.getObject("depreciationexpenseaccount"));
        assetCategory.setDepreciationMethod(DepreciationMethod.fromValue(rs.getString("depreciationmethod")));

        assetCategory.setDepreciationRate(rs.getDouble("depreciationrate"));
        assetCategory.setId((Long) rs.getObject("id"));
        assetCategory.setTenantId(rs.getString("tenantid"));
        assetCategory.setName(rs.getString("name"));
        assetCategory.setParent((Long) rs.getObject("parentid"));
        assetCategory.setRevaluationReserveAccount((Long) rs.getObject("revaluationreserveaccount"));
        assetCategory.setUnitOfMeasurement((Long) rs.getObject("unitofmeasurement"));
        assetCategory.setIsAssetAllow(rs.getBoolean("isassetallow"));
        assetCategory.setVersion(rs.getString("version"));
        assetCategory.setUsedForLease(rs.getBoolean("usedforlease"));
        assetCategory.setLifeOfTheAsset(rs.getLong("lifeoftheasset"));
        return assetCategory;
        
    }

}