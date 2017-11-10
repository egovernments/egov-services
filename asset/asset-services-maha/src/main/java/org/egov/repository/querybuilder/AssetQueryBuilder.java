package org.egov.repository.querybuilder;


import java.util.List;
import java.util.Set;

import org.egov.model.criteria.AssetCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetQueryBuilder {

	public final static String FINDBYNAMEQUERY = "SELECT asset.name FROM egasset_asset asset WHERE asset.name=? AND asset.tenantid=?";
	
    private static final String BASE_QUERY = "SELECT *,asd.code as landcode,asd.assetid as landassetid,currentval.currentamount "

    										+ "from egasset_asset asset left outer join egasset_asset_landdetails asd ON  asset.id=asd.assetid AND asset.tenantid=asd.tenantid "
    										
    										+ "left outer join (select current.assetid,current.tenantid,current.transactiondate,"
    										
    										+ "maxcurr.currentamount from egasset_current_value current inner join (select max(transactiondate) as transactiondate,currentamount,assetid"
    										
    										+ ",tenantid from egasset_current_value where tenantid=? GROUP BY assetid,tenantid,currentamount ) as maxcurr "
    										
    										+ "ON current.assetid=maxcurr.assetid AND current.tenantid=maxcurr.tenantid AND current.transactiondate=maxcurr.transactiondate) as currentval "
    										
    										+ "ON asset.id=currentval.assetid AND asset.tenantid=currentval.tenantid ";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getQuery(final AssetCriteria searchAsset, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		preparedStatementValues.add(searchAsset.getTenantId());
		log.info("get query");
		addWhereClause(selectQuery, preparedStatementValues, searchAsset);
		addPagingClause(selectQuery, preparedStatementValues, searchAsset);
		log.debug("Query from asset querybuilder for search : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final AssetCriteria searchAsset) {

		selectQuery.append("WHERE ASSET.tenantId = ?");
		preparedStatementValues.add(searchAsset.getTenantId());

		if (searchAsset.getId() != null && !searchAsset.getId().isEmpty()) {
			selectQuery.append(" AND ASSET.id IN (" + getIdQuery(searchAsset.getId()) + ")");
		}

		if (searchAsset.getAssetCategoryType() != null) {
			selectQuery.append(" AND ASSET.assetcategorytype = ?");
			preparedStatementValues.add(searchAsset.getAssetCategoryType().toString());
		}
		if (searchAsset.getName() != null) {
			selectQuery.append(" AND ASSET.name ilike ?");
			preparedStatementValues.add("%" + searchAsset.getName() + "%");
		}

		if (searchAsset.getCode() != null) {
			selectQuery.append(" AND ASSET.code like ?");
			preparedStatementValues.add("%" + searchAsset.getCode() + "%");
		}

		if (searchAsset.getDepartment() != null) {
			selectQuery.append(" AND ASSET.departmentcode = ?");
			preparedStatementValues.add(searchAsset.getDepartment());
		}

		if (searchAsset.getAssetCategory() != null) {
			selectQuery.append(" AND ASSET.assetCategory = ?");
			preparedStatementValues.add(searchAsset.getAssetCategory());
		}

		if (searchAsset.getAssetCreatedFrom() != null && searchAsset.getAssetCreatedto() != null) {
			selectQuery.append(" AND (ASSET.dateofcreation BETWEEN ? AND ?)");
			preparedStatementValues.add(searchAsset.getAssetCreatedFrom());
			preparedStatementValues.add(searchAsset.getAssetCreatedto());
		} else if (searchAsset.getAssetCreatedFrom() != null) {
			selectQuery.append(" AND ASSET.dateofcreation >= ?");
			preparedStatementValues.add(searchAsset.getAssetCreatedFrom());
		} else if (searchAsset.getAssetCreatedto() != null) {
			selectQuery.append(" AND ASSET.dateofcreation <= ?");
			preparedStatementValues.add(searchAsset.getAssetCreatedto());
		}

		if (searchAsset.getOriginalValueFrom() != null && searchAsset.getOriginalValueTo() != null) {
			selectQuery.append(" AND (ASSET.originalvalue BETWEEN ? AND ?)");
			preparedStatementValues.add(searchAsset.getOriginalValueFrom());
			preparedStatementValues.add(searchAsset.getOriginalValueTo());
		} else if (searchAsset.getOriginalValueFrom() != null) {
			selectQuery.append(" AND ASSET.originalvalue >= ?");
			preparedStatementValues.add(searchAsset.getOriginalValueFrom());
		} else if (searchAsset.getOriginalValueTo() != null) {
			selectQuery.append(" AND ASSET.originalvalue <= ?");
			preparedStatementValues.add(searchAsset.getOriginalValueTo());
		}

		if (searchAsset.getStatus() != null) {
			selectQuery.append(" AND ASSET.status = ?");
			preparedStatementValues.add(searchAsset.getStatus());
		}
		
		if(searchAsset.getToDate()!=null) {
			selectQuery.append(" AND asset.dateofcreation<?");
			preparedStatementValues.add(searchAsset.getToDate());
		}

		/*
		 * if (searchAsset.getLocality() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, selectQuery);
		 * selectQuery.append(" ASSET.locality = ?");
		 * preparedStatementValues.add(searchAsset.getLocality()); }
		 * 
		 * if (searchAsset.getZone() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, selectQuery);
		 * selectQuery.append(" ASSET.zone = ?");
		 * preparedStatementValues.add(searchAsset.getZone()); }
		 * 
		 * if (searchAsset.getRevenueWard() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, selectQuery);
		 * selectQuery.append(" ASSET.revenueWard = ?");
		 * preparedStatementValues.add(searchAsset.getRevenueWard()); }
		 * 
		 * if (searchAsset.getBlock() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, selectQuery);
		 * selectQuery.append(" ASSET.block = ?");
		 * preparedStatementValues.add(searchAsset.getBlock()); }
		 * 
		 * if (searchAsset.getStreet() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, selectQuery);
		 * selectQuery.append(" ASSET.street = ?");
		 * preparedStatementValues.add(searchAsset.getStreet()); }
		 */
		if (searchAsset.getElectionWard() != null) {
			selectQuery.append(" AND ASSET.electionWard = ?");
			preparedStatementValues.add(searchAsset.getElectionWard());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final AssetCriteria searchAsset) {

		List<String> sort = searchAsset.getSort();

		if (searchAsset.getSort() == null) {
			selectQuery.append(" ORDER BY asset.code");
		} else if (searchAsset.getSort() != null) {
			StringBuilder baseSort = new StringBuilder(" ORDER BY " + sort.get(0));
			selectQuery.append(baseSort);
			for (int i = 1; i <= searchAsset.getSort().size() - 1; i++) {
				selectQuery.append("," + sort.get(i));
			}
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

  
    private String getIdQuery(final Set<Long> idSet) {
		StringBuilder query = null;
		Long[] arr = new Long[idSet.size()];
		arr = idSet.toArray(arr);
		query = new StringBuilder(arr[0].toString());
		for (int i = 1; i < arr.length; i++)
			query.append("," + arr[i]);
		return query.toString();
	}
}

   
