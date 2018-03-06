
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
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.Department;
import org.egov.asset.model.Document;
import org.egov.asset.model.Location;
import org.egov.asset.model.YearWiseDepreciation;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetRowMapper implements ResultSetExtractor<List<Asset>> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ArrayList<Asset> extractData(final ResultSet rs) throws SQLException, DataAccessException {
        final Map<Long, Asset> map = new HashMap<Long, Asset>();

        while (rs.next()) {
            final Long assetId = (Long) rs.getObject("id");

            log.debug("assetid in row mapper" + assetId);

            Asset asset = map.get(assetId);
            if (asset == null) {
                asset = new Asset();
                asset.setId(assetId);
                asset.setName(rs.getString("assetname"));
                asset.setCode(rs.getString("assetcode"));
                asset.setAssetDetails(rs.getString("assetDetails"));
                asset.setTenantId(rs.getString("tenantId"));
                asset.setModeOfAcquisition(ModeOfAcquisition.fromValue(rs.getString("modeofacquisition")));
                asset.setStatus(rs.getString("status"));
                asset.setDescription(rs.getString("description"));
                asset.setDateOfCreation(rs.getLong("dateOfCreation"));
                asset.setRemarks(rs.getString("remarks"));
                asset.setLength(rs.getString("length"));
                asset.setWidth(rs.getString("width"));
                asset.setTotalArea(rs.getString("totalArea"));
                asset.setEnableYearWiseDepreciation(rs.getBoolean("enableyearwisedepreciation"));
                asset.setDepreciationRate(rs.getDouble("depreciationrate"));
                asset.setSurveyNumber(rs.getString("surveynumber"));
                asset.setFunction(rs.getString("function"));
                asset.setScheme(rs.getString("scheme"));
                asset.setSubScheme(rs.getString("subscheme"));
                asset.setCurrentValue(rs.getBigDecimal("currentamount"));

                final BigDecimal marketValue = rs.getBigDecimal("marketValue");
                if (marketValue == BigDecimal.ZERO)
                    asset.setMarketValue(null);
                else
                    asset.setMarketValue(marketValue);

                final BigDecimal accumulatedDepreciation = rs.getBigDecimal("accumulateddepreciation");
                if (accumulatedDepreciation == BigDecimal.ZERO)
                    asset.setAccumulatedDepreciation(null);
                else
                    asset.setAccumulatedDepreciation(accumulatedDepreciation);

                final BigDecimal grossValue = rs.getBigDecimal("grossvalue");
                if (grossValue == BigDecimal.ZERO)
                    asset.setGrossValue(null);
                else
                    asset.setGrossValue(grossValue);
                asset.setAssetReference((Long) rs.getObject("assetreference"));
                asset.setVersion(rs.getString("version"));

                final String properties = rs.getString("properties");
                Asset asset2 = null;

                try {
                    asset2 = objectMapper.readValue(properties, Asset.class);
                } catch (final JsonParseException e) {
                    e.printStackTrace();
                } catch (final JsonMappingException e) {
                    e.printStackTrace();
                } catch (final IOException e) {
                    e.printStackTrace();
                }

                asset.setAssetAttributes(asset2.getAssetAttributes());

                final Department department = new Department();
                department.setId((Long) rs.getObject("department"));
                asset.setDepartment(department);

                List<Document> docList = new ArrayList<>();
                Document documents = new Document();
                documents.setAsset((Long) rs.getObject("asset"));
                documents.setFileStore(rs.getString("filestore"));
                documents.setId((Long) rs.getObject("documentsId"));
                if (documents.getId() == null)
                    asset.setDocuments(new ArrayList<>());
                else
                    docList.add(documents);
                asset.setDocuments(docList);

                final Location location = new Location();
                location.setBlock((Long) rs.getObject("block"));
                location.setLocality((Long) rs.getObject("locality"));
                location.setDoorNo(rs.getString("doorNo"));
                location.setElectionWard((Long) rs.getObject("electionWard"));
                location.setRevenueWard((Long) rs.getObject("revenueWard"));
                location.setPinCode((Long) rs.getObject("pincode"));
                location.setZone((Long) rs.getObject("zone"));
                location.setStreet((Long) rs.getObject("street"));
                asset.setLocationDetails(location);

                final AssetCategory assetCategory = new AssetCategory();
                assetCategory.setId((Long) rs.getObject("assetcategoryId"));
                assetCategory.setAccumulatedDepreciationAccount((Long) rs.getObject("accumulatedDepreciationAccount"));
                assetCategory.setAssetCategoryType(AssetCategoryType.fromValue(rs.getString("assetcategorytype")));
                assetCategory.setAssetAccount((Long) rs.getObject("assetAccount"));
                assetCategory.setName(rs.getString("assetCategoryName"));
                assetCategory.setCode(rs.getString("assetcategorycode"));
                assetCategory.setParent((Long) rs.getObject("parentId"));
                assetCategory.setDepreciationRate(rs.getDouble("assetcategory_depreciationrate"));
                assetCategory.setDepreciationExpenseAccount((Long) rs.getObject("depreciationExpenseAccount"));
                assetCategory.setDepreciationMethod(DepreciationMethod.fromValue(rs.getString("depreciationMethod")));
                assetCategory.setAccumulatedDepreciationAccount((Long) rs.getObject("accumulatedDepreciationAccount"));
                assetCategory.setRevaluationReserveAccount((Long) rs.getObject("revaluationReserveAccount"));
                assetCategory.setUnitOfMeasurement((Long) rs.getObject("unitOfMeasurement"));
                assetCategory.setTenantId(rs.getString("tenantId"));
                assetCategory.setUsedForLease(rs.getBoolean("usedforlease"));
                asset.setAssetCategory(assetCategory);

                log.debug("AssetRowMapper asset:: " + asset);
                map.put(assetId, asset);
            }

            final YearWiseDepreciation ywdObject = new YearWiseDepreciation();
            ywdObject.setId((Long) rs.getObject("ywd_id"));
            ywdObject.setAssetId((Long) rs.getObject("assetid"));
            ywdObject.setDepreciationRate(rs.getDouble("ywd_depreciationrate"));
            ywdObject.setFinancialYear(rs.getString("financialyear"));
            ywdObject.setUsefulLifeInYears((Long) rs.getObject("usefullifeinyears"));
            ywdObject.setTenantId(rs.getString("tenantId"));

            final List<YearWiseDepreciation> ywd = asset.getYearWiseDepreciation();
            if (ywd == null)
                asset.setYearWiseDepreciation(new ArrayList<>());
            else
                ywd.add(ywdObject);

            asset.setYearWiseDepreciation(ywd);

        }

        return new ArrayList<Asset>(map.values());
    }
}