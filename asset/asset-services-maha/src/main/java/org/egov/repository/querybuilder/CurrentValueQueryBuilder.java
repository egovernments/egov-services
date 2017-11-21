package org.egov.repository.querybuilder;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class CurrentValueQueryBuilder {

	public String getInsertQuery() {
		return "INSERT INTO egasset_current_value (id,assetid,tenantid,assettrantype,currentamount,createdby,"
				+ "createdtime,lastModifiedby,lastModifiedtime) VALUES (?,?,?,?,?,?,?,?,?)";
	}

	public String getCurrentValueQuery(final Set<Long> assetIds, final String tenantId) {

		String assetIdString = "";
		if (assetIds != null && !assetIds.isEmpty())
			assetIdString = "AND assetid IN " + getIdQueryForList(assetIds);

		return "SELECT ungroupedvalue.* FROM egasset_current_value ungroupedvalue "
				+ "INNER join (SELECT assetid,max(createdtime) AS createdtime FROM egasset_current_value "
				+ "WHERE tenantid='" + tenantId + "'" + assetIdString + " group by assetid) groupedvalue "
				+ "ON ungroupedvalue.assetid=groupedvalue.assetid and ungroupedvalue.createdtime=groupedvalue.createdtime "
				+ "where ungroupedvalue.tenantid='" + tenantId + "' " + assetIdString.replace(
						"assetid","ungroupedvalue.assetid");
	}
	
	public String getNonTransactedAssetQuery(final Set<Long> assetIds, final String tenantId) {
		

		return "select assetid from egasset_current_value where  assetid not in (select assetid from egasset_current_value where assettrantype!='CREATE');";
		
	}


	private static String getIdQueryForList(Set<Long> idList) {

		StringBuilder query = new StringBuilder("(");
		Long[] list = idList.toArray(new Long[idList.size()]);
		query.append(list[0]);
		for (int i = 1; i < idList.size(); i++) {
			query.append("," + list[i]);
		}
		return query.append(")").toString();
	}
}

