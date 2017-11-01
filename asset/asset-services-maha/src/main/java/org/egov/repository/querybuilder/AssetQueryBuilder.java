package org.egov.repository.querybuilder;


import java.util.List;

import org.egov.config.ApplicationProperties;
import org.egov.model.criteria.AssetCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT *,asd.code as landcode from egasset_asset asset left outer join egasset_asset_landdetails asd ON "
    		+ " asset.id=asd.assetid AND asset.tenantid=asd.tenantid ";

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
        if (searchAsset.getDoorNo() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ASSET.doorno = ?");
            preparedStatementValues.add(searchAsset.getDoorNo());
        }
        }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final AssetCriteria searchAsset) {
        // handle limit(also called pageSize) here
    	// selectQuery.append(" ORDER BY asset.code");
    	
    	List<String> sort = searchAsset.getSort();
    	
    	if(searchAsset.getSort()==null) {
    		selectQuery.append(" ORDER BY asset.code");
    		}
    	
    	else if(searchAsset.getSort()!=null) {
    		System.out.println("searchAsset.getSort().get(0)"+searchAsset.getSort().get(0));
    		StringBuilder baseSort = new StringBuilder(" ORDER BY "+sort.get(0));
    		System.err.println("baseSort"+baseSort);
    		 selectQuery.append(baseSort);
    		 for(int i=1;i<=searchAsset.getSort().size()-1;i++) {
    			 selectQuery.append(","+sort.get(i));
    			 }
    		 System.err.println("selectQuery after"+selectQuery);
    		  StringBuilder baseSort1 = new StringBuilder(selectQuery);
    		  System.err.println("selectQuery baseSort1"+baseSort1);
    	
    	}
        selectQuery.append(" LIMIT ?");
        long pageSize = 500;

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

    public final static String FINDBYNAMEQUERY = "SELECT asset.name FROM egasset_asset asset WHERE asset.name=? AND asset.tenantid=?";
}

   