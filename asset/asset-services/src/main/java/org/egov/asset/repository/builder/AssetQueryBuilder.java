
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

package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT *, asset.id AS assetId,assetcategory.id AS assetcategoryId,"
            + "asset.name as assetname,asset.code as assetcode,documents.filestore as filestore ,documents.id as documentsId,"
            + "assetcategory.name AS assetcategoryname,assetcategory.assetcategorytype as assetcategorytype,assetcategory.code AS assetcategorycode, "
            + "assetcategory.depreciationrate as assetcategory_depreciationrate ,currentval.currentamount "
            + " FROM egasset_asset asset "
            + "INNER JOIN egasset_assetcategory assetcategory "
            + "ON asset.assetcategory = assetcategory.id " 
            + "LEFT OUTER JOIN egasset_document documents "
            + "ON asset.id = documents.asset "
            + "left outer join (select current.assetid,current.tenantid,"
            + " maxcurr.currentamount from egasset_current_value current inner join (select curr.currentamount,curr.assetid,curr.tenantid,curr.createdtime "
            + " from egasset_current_value "
            + "curr inner join (select max(createdtime) as createdtime,assetid,tenantid from egasset_current_value where tenantid=? "

            + " GROUP BY assetid,tenantid)  maxtrans ON  curr.assetid=maxtrans.assetid and "

            + " curr.tenantid=maxtrans.tenantid and curr.createdtime=maxtrans.createdtime order by createdtime desc) as maxcurr "

            + "ON current.assetid=maxcurr.assetid AND current.tenantid=maxcurr.tenantid "

            + " and current.createdtime=maxcurr.createdtime order by maxcurr.createdtime desc) as currentval "

            + "ON asset.id=currentval.assetid AND asset.tenantid=currentval.tenantid ";

    @SuppressWarnings("rawtypes")
    public String getQuery(final AssetCriteria searchAsset, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        log.info("get query");
        addWhereClause(selectQuery, preparedStatementValues, searchAsset);
        addPagingClause(selectQuery, preparedStatementValues, searchAsset);
        log.debug("Query from asset querybuilder for search : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final AssetCriteria searchAsset) {
        if (searchAsset.getId() == null && searchAsset.getName() == null && searchAsset.getCode() == null
                && searchAsset.getDepartment() == null && searchAsset.getAssetCategory() == null
                && searchAsset.getTenantId() == null && searchAsset.getDoorNo() == null)
            // FIXME location object criterias need to be added to if block
            // assetcategory is mandatory
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (searchAsset.getTenantId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.tenantId = ?");
            preparedStatementValues.add(searchAsset.getTenantId());
            preparedStatementValues.add(searchAsset.getTenantId());
        }

        if (searchAsset.getId() != null && !searchAsset.getId().isEmpty()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.id IN (" + getIdQuery(searchAsset.getId()));
        }

        if (searchAsset.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.name ilike ?");
            preparedStatementValues.add("%" + searchAsset.getName() + "%");
        }

        if (searchAsset.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.code like ?");
            preparedStatementValues.add("%" + searchAsset.getCode() + "%");
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

        if (searchAsset.getAssetCategoryType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetCategory.assetcategorytype = ?");
            preparedStatementValues.add(searchAsset.getAssetCategoryType());
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

        if (searchAsset.getAssetReference() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.assetreference = ?");
            preparedStatementValues.add(searchAsset.getAssetReference());
        }

        if (searchAsset.getDescription() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.description ilike ?");
            preparedStatementValues.add("%" + searchAsset.getDescription() + "%");
        }

        if (searchAsset.getGrossValue() != null && searchAsset.getFromCapitalizedValue() == null
                && searchAsset.getToCapitalizedValue() == null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.grossvalue = ?");
            preparedStatementValues.add(searchAsset.getGrossValue());
        }
        if (searchAsset.getGrossValue() == null && searchAsset.getFromCapitalizedValue() == null
                && searchAsset.getToCapitalizedValue() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.grossvalue BETWEEN 1 AND ?");
            preparedStatementValues.add(searchAsset.getToCapitalizedValue());
        }
        if (searchAsset.getGrossValue() == null && searchAsset.getFromCapitalizedValue() != null
                && searchAsset.getToCapitalizedValue() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.grossvalue BETWEEN ? AND ?");
            preparedStatementValues.add(searchAsset.getFromCapitalizedValue());
            preparedStatementValues.add(searchAsset.getToCapitalizedValue());
        }

        if (searchAsset.getAssetCreatedFrom() != null && searchAsset.getAssetCreatedTo() != null) {
            selectQuery.append(" AND (ASSET.dateofcreation BETWEEN ? AND ?)");
            preparedStatementValues.add(searchAsset.getAssetCreatedFrom());
            preparedStatementValues.add(searchAsset.getAssetCreatedTo());
        } else if (searchAsset.getAssetCreatedFrom() != null) {
            selectQuery.append(" AND ASSET.dateofcreation >= ?");
            preparedStatementValues.add(searchAsset.getAssetCreatedFrom());
        } else if (searchAsset.getAssetCreatedTo() != null) {
            selectQuery.append(" AND ASSET.dateofcreation <= ?");
            preparedStatementValues.add(searchAsset.getAssetCreatedTo());
        }

        if (searchAsset.getTransaction() != null)
            if (searchAsset.getTransaction().toString().equals(TransactionType.DEPRECIATION.toString())) {
                selectQuery.append(
                        " AND ASSET.status!='DISPOSED' AND ASSET.status='CAPITALIZED' AND assetcategory.assetcategorytype!='LAND' AND ASSET.id NOT in "
                                + "(select depr.assetid from egasset_depreciation depr where depr.todate >=? )");
                preparedStatementValues.add(searchAsset.getDateOfDepreciation());
            }

        if (searchAsset.getDateOfDepreciation() != null) {
            selectQuery.append(" AND asset.dateofcreation IS NOT NULL AND asset.dateofcreation<?");
            preparedStatementValues.add(searchAsset.getDateOfDepreciation());
        }
        if(searchAsset.getIsTransactionHistoryRequired()!=null){
            selectQuery.append( " AND ASSET.status IN ('CAPITALIZED','DISPOSED')");
            
        }

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final AssetCriteria searchAsset) {
        // handle limit(also called pageSize) here
        selectQuery.append(" ORDER BY asset.name");

        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.getSearchPageSizeDefault());

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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String getHistoryQuery(final List<Long> assetIds, final String tenantid) {

        String assetIdString = null;
        if (assetIds != null && !assetIds.isEmpty())
            assetIdString = "AND cv.assetid IN (" + getIdQuery(assetIds);

        return "select  *  "
                + "from egasset_current_value cv  "
                + "left outer join egasset_revalution  rv on rv.assetid=cv.assetid and rv.valueafterrevaluation=cv.currentamount "
                + "left outer join egasset_depreciation  dv on dv.assetid=cv.assetid and dv.valueafterdepreciation=cv.currentamount "
                + "left outer join egasset_disposal  dp on dp.assetid=cv.assetid "
                + "where  cv.assettrantype !='CREATE' AND cv.tenantid='" + tenantid + "' " + assetIdString
                + " order by cv.assetid,cv.id,cv.createdtime ";

    }

    /**
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }

    private static String getIdQuery(final List<Long> idList) {
        StringBuilder query = null;
        if (!idList.isEmpty()) {
            query = new StringBuilder(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append("," + idList.get(i));
        }
        return query.append(")").toString();
    }

    public String getInsertQuery() {
        return "INSERT into egasset_asset " + "(id,assetcategory,name,code,department,assetdetails,description,"
                + "dateofcreation,remarks,length,width,totalarea,modeofacquisition,status,tenantid,"
                + "zone,revenueward,street,electionward,doorno,pincode,locality,block,properties,createdby,"
                + "createddate,lastmodifiedby,lastmodifieddate,grossvalue,accumulateddepreciation,assetreference,version,"
                + "depreciationrate,surveynumber,marketvalue,function,scheme,subscheme)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public String getUpdateQuery() {
        return "UPDATE egasset_asset SET assetcategory=?,name=?,department=?,assetdetails=?,description=?,remarks=?,length=?,width=?,"
                + "totalarea=?,modeofacquisition=?,status=?,zone=?,revenueward=?,street=?,electionward=?,doorno=?,pincode=?,locality=?,"
                + "block=?,properties=?,lastmodifiedby=?,lastmodifieddate=?,grossvalue=?,accumulateddepreciation=?,assetreference=?,version=?,"
                + "surveynumber=?,marketvalue=?,function=?,scheme=?,subscheme=?"
                + "WHERE code=? and tenantid=?";
    }

   /* public final static String BATCHINSERTQUERY = "INSERT INTO egasset_yearwisedepreciation "
            + "(depreciationrate,financialyear,assetid,usefullifeinyears,tenantid,createdby,createddate,"
            + "lastmodifiedby,lastmodifieddate) values (?,?,?,?,?,?,?,?,?)";

    public final static String BATCHUPDATEQUERY = "UPDATE egasset_yearwisedepreciation SET"
            + " depreciationrate=?,usefullifeinyears=?,"
            + "createdby=?,createddate=?,lastmodifiedby=?,lastmodifieddate=? "
            + "WHERE  assetid=? AND financialyear=? AND tenantid=?";*/

    public final static String FINDBYNAMEQUERY = "SELECT asset.name FROM egasset_asset asset WHERE asset.name=? AND asset.tenantid=?";

    /*public final static String GETYEARWISEDEPRECIATIONQUERY = "select id,assetid,depreciationrate,financialyear,usefullifeinyears,tenantid from "
            + "egasset_yearwisedepreciation where assetid = ? and tenantid = ?";

    public final static String ASSETINCLUDEDEPRECIATIONRATEUPDATEQUERY = "UPDATE egasset_asset SET enableyearwisedepreciation = ?,depreciationrate=?"
            + " WHERE code = ? and tenantid = ?";
    public final static String ASSETEXCLUDEDEPRECIATIONRATEUPDATEQUERY = "UPDATE egasset_asset SET enableyearwisedepreciation = ?"
            + " WHERE code = ? and tenantid = ?";

    public final static String YEARWISEDEPRECIATIONDELETEQUERY = "DELETE FROM egasset_yearwisedepreciation ywd where "
            + "ywd.financialyear = ? and ywd.assetid = ? and ywd.tenantid = ?";*/
}
