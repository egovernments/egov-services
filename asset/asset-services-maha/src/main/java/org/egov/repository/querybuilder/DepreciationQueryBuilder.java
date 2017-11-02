package org.egov.repository.querybuilder;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DepreciationQueryBuilder {
	
		static private final String  BASEDEPRECIATIONQUERY = "select "
				+ "asset.id as assetid,asset.tenantid,asset.grossvalue,asset.accumulateddepreciation,asset.assetcategory,"
				+ "currentval.currentamount as currentvalue,depreciation.depreciationvaluesum from egasset_asset asset "
				+ "left outer join (select currval.assetid,currval.createdtime,currval.currentamount,currval.tenantid "
				+ "from egasset_current_value currval inner join " 
				+ "(select assetid,tenantid,max(createdtime) as createdtime from egasset_current_value where tenantid=? "
				+ "group by assetid,tenantid) maxval ON currval.assetid=maxval.assetid AND currval.tenantid=maxval.tenantid "
				+ "AND currval.createdtime=maxval.createdtime) currentval ON asset.id=currentval.assetid AND "
				+ "asset.tenantid=currentval.tenantid left outer join (select distinct(dep.assetid) as assetid,"
				+ "dep.tenantid,depsum.depreciationvaluesum from egasset_depreciation dep inner join "
				+ "(select assetid,tenantid,sum(depreciationvalue) as depreciationvaluesum from egasset_depreciation "
				+ "where tenantid=? AND assetid NOT IN (select assetid from egasset_depreciation where createdtime>=?) "
				+ "group by assetid,tenantid) depsum ON dep.assetid=depsum.assetid AND dep.tenantid=depsum.tenantid) "
				+ "depreciation ON asset.id=depreciation.assetid AND asset.tenantid=depreciation.tenantid WHERE "
				+ "asset.tenantid=? AND id NOT IN (select assetid from egasset_depreciation where createdtime>=?);";

		public String getDepreciationQuery(String tenantId,Long toDate,List<Object> preparedStatementValues) {
			
			preparedStatementValues.add(tenantId);
			preparedStatementValues.add(tenantId);
			preparedStatementValues.add(toDate);
			preparedStatementValues.add(tenantId);
			preparedStatementValues.add(toDate);
			return BASEDEPRECIATIONQUERY;
		}
}
