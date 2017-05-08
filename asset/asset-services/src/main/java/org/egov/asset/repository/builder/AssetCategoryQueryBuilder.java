package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.model.AssetCategoryCriteria;
import org.springframework.stereotype.Component;

@Component
public class AssetCategoryQueryBuilder {
	
	private static final String SELECT_BASE_QUERY = "SELECT *"
			+ " FROM egasset_assetcategory assetcategory ";
	
	public String getQuery(AssetCategoryCriteria assetCategoryCriteria,List<Object> preparedStatementValues){
		StringBuilder selectQuery = new StringBuilder(SELECT_BASE_QUERY);
		
		addWhereClause(selectQuery, preparedStatementValues, assetCategoryCriteria);
		//addOrderByClause(selectQuery, assetCategoryCriteria);
		//addPagingClause(selectQuery, preparedStatementValues, assetCategoryCriteria);
		System.out.println("selectQuery::"+selectQuery);
		System.out.println(preparedStatementValues);
		return selectQuery.toString();
	}
	

	private void addWhereClause(StringBuilder selectQuery, List<Object> preparedStatementValues,
			AssetCategoryCriteria assetCategoryCriteria) {
		
		if (assetCategoryCriteria.getId() == null && assetCategoryCriteria.getName() == null
				&& assetCategoryCriteria.getCode() == null && assetCategoryCriteria.getTenantId() == null
				&& assetCategoryCriteria.getAssetCategoryType().isEmpty())
			return;
		
		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;
		
		if (assetCategoryCriteria.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" assetcategory.tenantId = ?");
			preparedStatementValues.add(assetCategoryCriteria.getTenantId());
		}

		if (assetCategoryCriteria.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" assetcategory.id = ?");
			preparedStatementValues.add(assetCategoryCriteria.getId());
		}
		
		if (assetCategoryCriteria.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" assetcategory.name = ?");
			preparedStatementValues.add(assetCategoryCriteria.getName());
		}
		
		if (assetCategoryCriteria.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" assetcategory.code = ?");
			preparedStatementValues.add(assetCategoryCriteria.getCode());
		}
		
		if (assetCategoryCriteria.getAssetCategoryType() != null && assetCategoryCriteria.getAssetCategoryType().size()!=0) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" assetcategory.assetcategorytype IN ("+getAssetCategoryTypeQuery(assetCategoryCriteria.getAssetCategoryType()));
		}

	
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
	
	private static String getAssetCategoryTypeQuery(List<String> categoryTypes) {
		StringBuilder query = null;
		if (categoryTypes.size() >= 1) {
			query = new StringBuilder("'"+categoryTypes.get(0).toString()+"'");
			for (int i = 1; i < categoryTypes.size(); i++) {
				query.append(",'" + categoryTypes.get(i)+"'");
			}
		}
		return query.append(")").toString();
	}

	public String getInsertQuery() {
		return "INSERT into egasset_assetcategory "
				+ "(id,name,code,parentid,assetcategorytype,depreciationmethod,depreciationrate,assetaccount,accumulateddepreciationaccount,"
				+ "revaluationreserveaccount,depreciationexpenseaccount,unitofmeasurement,customfields,tenantid,createdby,createddate,"
				+ "lastmodifiedby,lastmodifieddate,isassetallow,version)"
				+ "values(nextval('seq_egasset_assetcategory'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}
	
	/*private void addPagingClause(StringBuilder selectQuery, List<Object> preparedStatementValues,
	AssetCategoryCriteria assetCategoryCriteria) {

}

private void addOrderByClause(StringBuilder selectQuery, AssetCategoryCriteria assetCategoryCriteria) {
// TODO Auto-generated method stub

}*/
}
