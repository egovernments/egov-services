package org.egov.repository.querybuilder;

import java.util.List;
import java.util.Set;

import org.egov.model.criteria.AssetCriteria;
import org.egov.model.enums.AssetCategoryType;
import org.egov.model.enums.TransactionType;
import org.egov.service.AssetCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetQueryBuilder {
	
	@Autowired
	private AssetCommonService assetCommonService;

	public final static String FINDBYNAMEQUERY = "SELECT asset.name FROM egasset_asset asset WHERE asset.name=? AND asset.tenantid=?";

	private static final String BASE_QUERY = "SELECT *,asd.code as landcode,asd.assetid as landassetid,asd.id as landid,currentval.currentamount "

			+ "from egasset_asset asset left outer join egasset_asset_landdetails asd ON  asset.id=asd.assetid AND asset.tenantid=asd.tenantid "

			+ "left outer join (select current.assetid,current.tenantid,current.transactiondate,"

			+ "maxcurr.currentamount from egasset_current_value current inner join (select curr.currentamount,curr.assetid,curr.tenantid,curr.createdtime,"

			+ "curr.transactiondate from egasset_current_value "

			+ "curr inner join (select max(createdtime) as createdtime,assetid,tenantid from egasset_current_value where tenantid=? "

			+ " GROUP BY assetid,tenantid)  maxtrans ON  curr.assetid=maxtrans.assetid and "

			+ " curr.tenantid=maxtrans.tenantid and curr.createdtime=maxtrans.createdtime order by createdtime desc) as maxcurr "

			+ "ON current.assetid=maxcurr.assetid AND current.tenantid=maxcurr.tenantid AND current.transactiondate=maxcurr.transactiondate"

			+ " and current.createdtime=maxcurr.createdtime order by maxcurr.createdtime desc) as currentval "

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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getHistoryQuery(Set<Long> assetIds ,String tenantid) {

		String assetIdString = null;
		if (assetIds != null && !assetIds.isEmpty())
			assetIdString = "AND cv.assetid IN (" + assetCommonService.getIdQuery(assetIds) + ") ";
		
		return "select  * "
		+ "from egasset_current_value cv  " 
		+ "left outer join egasset_revaluation  rv on rv.assetid=cv.assetid and rv.valueafterrevaluation=cv.currentamount "  
		+ "left outer join egasset_depreciation  dv on dv.assetid=cv.assetid and dv.valueafterdepreciation=cv.currentamount "
		+ "where  cv.assettrantype !='CREATE' AND cv.tenantid='" + tenantid + "' " +assetIdString+ " order by cv.assetid,cv.id,cv.transactiondate,cv.createdtime; ";

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
			selectQuery.append(" AND ASSET.assetcategorytype IN ("
					+ getCategoryType(searchAsset.getAssetCategoryType()).toString() + ")");
			/*
			 * preparedStatementValues.add(searchAsset.getAssetCategoryType().toString());
			 */
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

		if (searchAsset.getAssetSubCategory() != null && !searchAsset.getAssetSubCategory().isEmpty()) {
			selectQuery.append(" AND ASSET.assetCategory IN ("+ getIdQuery(searchAsset.getAssetSubCategory())+") ");
		}
		if (searchAsset.getCodes() != null && !searchAsset.getCodes().isEmpty()) {
			selectQuery.append(" AND ASSET.code IN ("+ getString(searchAsset.getCodes())+") ");
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

		if (searchAsset.getToDate() != null) {
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

		if (searchAsset.getTransaction() != null) {
			if (searchAsset.getTransaction().toString().equals(TransactionType.REVALUATION.toString())) {
				selectQuery.append(" AND ASSET.status!='DISPOSED'  AND ASSET.assetcategorytype!='LAND' ");
			} else if (searchAsset.getTransaction().toString().equals(TransactionType.DISPOSAL.toString())) {
				selectQuery.append(" AND ASSET.status!='DISPOSED'  AND ASSET.assetcategorytype!='LAND'");
			} else if (searchAsset.getTransaction().toString().equals(TransactionType.DEPRECIATION.toString())) {
				selectQuery.append(" AND ASSET.status!='DISPOSED' AND ASSET.assetcategorytype!='LAND' ");
			}
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
			pageNumber = searchAsset.getOffset() < 1 ? 0 : searchAsset.getOffset() - 1;
        preparedStatementValues.add( pageNumber * pageSize); 
			/*pageNumber = searchAsset.getOffset() - 1;
		preparedStatementValues.add(pageNumber * pageSize);*/ // Set offset to
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

	private String getCategoryType(final List<AssetCategoryType> assetCategoryTypeList) {
		StringBuilder query = null;
		AssetCategoryType[] arr = new AssetCategoryType[assetCategoryTypeList.size()];
		arr = assetCategoryTypeList.toArray(arr);
		// String join = "'" + StringUtils.join(arr,"','") + "'";
		System.out.println("arr" + arr);
		query = new StringBuilder("'" + arr[0].toString() + "'");
		for (int i = 1; i < arr.length; i++)
			query.append("," + "'" + arr[i].toString() + "'");
		return query.toString();
	}

	private String getString(final List<String> strings) {
		StringBuilder query = null;
		String[] arr = new String[strings.size()];
		arr = strings.toArray(arr);
		// String join = "'" + StringUtils.join(arr,"','") + "'";
		System.out.println("arr" + arr);
		query = new StringBuilder("'" + arr[0].toString() + "'");
		for (int i = 1; i < arr.length; i++)
			query.append("," + "'" + arr[i].toString() + "'");
		return query.toString();
	}
	
}
