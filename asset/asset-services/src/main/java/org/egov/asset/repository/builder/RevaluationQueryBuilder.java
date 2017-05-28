package org.egov.asset.repository.builder;

import org.springframework.stereotype.Component;

@Component
public class RevaluationQueryBuilder {

	public static final String INSERT_QUERY="INSERT into egasset_revalution "
			+ "(id,tenantid,assetid,currentcapitalizedvalue,typeofchange,revaluationamount,valueafterrevaluation,"
			+ "revaluationdate,reevaluatedby,reasonforrevaluation,fixedassetswrittenoffaccount,"
			+ "function,fund,scheme,subscheme,comments,status,createdby,createddate,lastmodifiedby,lastmodifieddate)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
}
