package org.egov.repository.querybuilder;

import java.util.List;
import java.util.Set;

import org.egov.model.criteria.DepreciationCriteria;
import org.springframework.stereotype.Component;

@Component
public class DepreciationQueryBuilder {
	
	static private final String BASEDEPRECIATIONQUERY = "select asset.code assetcode,asset.id as assetid,asset.tenantid,asset.grossvalue,asset.accumulateddepreciation,asset.assetcategory,"
			
			+ "currentval.currentamount as currentvalue,depreciation.maxtodate as lastdepreciationdate,depreciation.depreciationvaluesum,asset.originalvalue,"
			
			+ "asset.openingdate,asset.assetaccount,asset.accumulateddepreciationaccount,asset.revaluationreserveaccount,asset.depreciationexpenseaccount "
			
			+ "from egasset_asset asset left outer join (select currval.assetid,currval.createdtime,currval.currentamount,currval.tenantid "
			
			+ "from egasset_current_value currval inner join (select assetid,tenantid,max(createdtime) as createdtime from egasset_current_value where tenantid=? "
			
			+ "group by assetid,tenantid) maxval ON currval.assetid=maxval.assetid AND currval.tenantid=maxval.tenantid AND currval.createdtime=maxval.createdtime) "
			
			+ "currentval ON asset.id=currentval.assetid AND asset.tenantid=currentval.tenantid left outer join "
			
			+ "(select dep.assetid as assetid,dep.tenantid,depsum.depreciationvaluesum,maxdep.maxtodate from egasset_depreciation dep inner join "
			
			+ "(select assetid,tenantid,sum(depreciationvalue) as depreciationvaluesum from egasset_depreciation where tenantid=? AND assetid NOT IN "
			
			+ "(select assetid from egasset_depreciation where todate>=? ) AND status='SUCCESS' group by assetid,tenantid) depsum ON dep.assetid=depsum.assetid "
			
			+ "AND dep.tenantid=depsum.tenantid inner join (select assetid,tenantid,max(todate) as maxtodate from egasset_depreciation dep "
			
			+ "where tenantid=? and status='SUCCESS' AND assetid NOT IN (select assetid from egasset_depreciation where todate>=?) "
			
			+ "group by assetid,tenantid) maxdep ON maxdep.assetid=dep.assetid AND maxdep.tenantid=dep.tenantid  AND maxdep.maxtodate=dep.todate) "
			
			+ "depreciation ON asset.id=depreciation.assetid AND asset.tenantid=depreciation.tenantid WHERE asset.assetcategorytype!='LAND' "
			
			+ "AND asset.tenantid=? AND id NOT IN (select assetid from egasset_depreciation where todate>=? AND status='SUCCESS') "
			
			+ "AND asset.openingdate<=? {assetids};";

	public String getDepreciationQuery(DepreciationCriteria depreciationCriteria,List<Object> preparedStatementValues) {

		preparedStatementValues.add(depreciationCriteria.getTenantId());
		preparedStatementValues.add(depreciationCriteria.getTenantId());
		preparedStatementValues.add(depreciationCriteria.getToDate());
		preparedStatementValues.add(depreciationCriteria.getTenantId());
		preparedStatementValues.add(depreciationCriteria.getToDate());
		preparedStatementValues.add(depreciationCriteria.getTenantId());
		preparedStatementValues.add(depreciationCriteria.getToDate());
		preparedStatementValues.add(depreciationCriteria.getToDate());

		if (depreciationCriteria.getAssetIds()!= null && !depreciationCriteria.getAssetIds().isEmpty())
			return BASEDEPRECIATIONQUERY.replace("{assetids}", " AND id IN (" + getIdQuery(depreciationCriteria.getAssetIds()) + ")");
		else
			return BASEDEPRECIATIONQUERY.replace("{assetids}", "");
	}
		
		private static String getIdQuery(final Set<Long> idSet) {
			StringBuilder query = null;
			Long[] arr = new Long[idSet.size()];
			arr = idSet.toArray(arr);
			query = new StringBuilder(arr[0].toString());
			for (int i = 1; i < arr.length; i++)
				query.append("," + arr[i]);
			return query.toString();
		}
}
