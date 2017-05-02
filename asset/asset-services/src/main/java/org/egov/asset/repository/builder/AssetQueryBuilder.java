
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

package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.AssetCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(AssetQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT *,"
			+ "asset.id AS assetId,assetcategory.id AS assetcategoryId,"
			+ "asset.name as assetname,asset.code as assetcode,"
			+ "assetcategory.name AS assetcategoryname,assetcategory.code AS assetcategorycode"
			+ " FROM egasset_asset asset "
			+ "INNER JOIN egasset_assetcategory assetcategory "
			+ "ON asset.assetcategory = assetcategory.id ";
	@SuppressWarnings("rawtypes")
	public String getQuery(AssetCriteria searchAsset, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		System.out.println("get query");
		addWhereClause(selectQuery, preparedStatementValues, searchAsset);
		addPagingClause(selectQuery, preparedStatementValues, searchAsset);

		logger.info("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues, AssetCriteria searchAsset) {

		if (searchAsset.getId() == null && searchAsset.getName() == null && searchAsset.getCode() == null
				&& searchAsset.getDepartment() == null && searchAsset.getAssetCategory() == null
				&& searchAsset.getTenantId() == null && searchAsset.getDoorNo() == null)
			//FIXME location object criterias need to be added to if block assetcategory is mandatory
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (searchAsset.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" ASSET.tenantId = ?");
			preparedStatementValues.add(searchAsset.getTenantId());
		}

		if (searchAsset.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.id IN ("+getIdQuery(searchAsset.getId()));
		}

		if (searchAsset.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.name = ?");
			preparedStatementValues.add(searchAsset.getName());
		}

		if (searchAsset.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.code = ?");
			preparedStatementValues.add(searchAsset.getCode());
		}

		if (searchAsset.getDepartment() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.department = ?");
			preparedStatementValues.add(searchAsset.getDepartment());
		}

		if (searchAsset.getAssetCategory() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.assetCategory = ?");
			preparedStatementValues.add(searchAsset.getAssetCategory());
		}
		
		if (searchAsset.getStatus() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.status = ?");
			preparedStatementValues.add(searchAsset.getStatus());
		}
		
		if (searchAsset.getLocality() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.locality = ?");
			preparedStatementValues.add(searchAsset.getLocality());
		}
		
		if (searchAsset.getZone() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.zone = ?");
			preparedStatementValues.add(searchAsset.getZone());
		}
		
		if (searchAsset.getRevenueWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.revenueWard = ?");
			preparedStatementValues.add(searchAsset.getRevenueWard());
		}
		
		if (searchAsset.getBlock() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.block = ?");
			preparedStatementValues.add(searchAsset.getBlock());
		}
	
		if (searchAsset.getStreet() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.street = ?");
			preparedStatementValues.add(searchAsset.getStreet());
		}
		
		if (searchAsset.getElectionWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.electionWard = ?");
			preparedStatementValues.add(searchAsset.getElectionWard());
		}
		
		if (searchAsset.getStatus() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.doorNo = ?");
			preparedStatementValues.add(searchAsset.getDoorNo());
		}
		
		if (searchAsset.getPinCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.pinCode = ?");
			preparedStatementValues.add(searchAsset.getPinCode());
		}
		
		if (searchAsset.getDoorNo() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.doorno = ?");
			preparedStatementValues.add(searchAsset.getDoorNo());
		}
		
	}
	

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues, AssetCriteria searchAsset) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.commonsSearchPageSizeDefault());
		if (searchAsset.getSize() != null)
			pageSize = searchAsset.getSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (searchAsset.getOffset() != null)
			pageNumber = searchAsset.getOffset() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
	}

	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 * 
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");
		return true;
	}
	
	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = null;
		if (idList.size() >= 1) {
			query = new StringBuilder(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + idList.get(i));
			}
		}
		return query.append(")").toString();
	}
	
	public String getInsertQuery(){
		String INSERT_QUERY="INSERT into egasset_asset "
				+"(id,assetcategory,name,code,department,assetdetails,description,"
				+ "dateofcreation,remarks,length,width,totalarea,modeofacquisition,status,tenantid,"
				+ "zone,revenueward,street,electionward,doorno,pincode,locality,block,properties,createdby,"
				+ "createddate,lastmodifiedby,lastmodifieddate,grossvalue,accumulateddepreciation,assetrefrance,version)"
				+"values(nextval('seq_egasset_asset'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return INSERT_QUERY;
	}

	public String getUpdateQuery(){
		String UPDATE_QUERY="UPDATE egasset_asset SET assetcategory=?,name=?,department=?,assetdetails=?,description=?,remarks=?,length=?,"
				+ "width=?,totalarea=?,modeofacquisition=?,status=?,zone=?,revenueward=?,street=?,electionward=?,doorno=?,pincode=?,locality=?,"
				+ "block=?,properties=?,lastmodifiedby=?,lastmodifieddate=?,grossvalue=?,accumulateddepreciation=?,assetrefrance=?,version=? "
				+ "WHERE code=? and tenantid=?";
				
		return UPDATE_QUERY;
	}
}
