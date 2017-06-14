package org.egov.pgr.repository.builder;

import java.util.List;

import org.egov.pgr.web.contract.RouterTypeGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RouterQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(RouterQueryBuilder.class);
	
	private static final String BASE_QUERY = "SELECT router.id, router.position, router.tenantid, router.bndryid, comp.id complaintid, comp.code servicecode, comp.name servicename, "
			+ " comp.description servicedescription, comp.category, adef.code attributecode, adef.datatype, adef.datatypedescription, adef.description attributedescription, "
			+ " adef.variable, adef.required, vdef.key, vdef.name FROM egpgr_router router LEFT JOIN egpgr_complainttype comp ON router.complainttypeid = comp.id " 
			+ " LEFT JOIN service_definition sdef ON sdef.code = comp.code LEFT JOIN attribute_definition adef ON sdef.code = adef.servicecode LEFT JOIN value_definition vdef ON vdef.attributecode = adef.code " ; 

	private static final String INSERT_ROUTER = "INSERT INTO egpgr_router(complainttypeid, position, bndryid, version, createdby, createddate, lastmodifiedby, lastmodifieddate,tenantid) values"
			+ "(?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_ROUTER = "update egpgr_router SET position =?, version=?, createdby=?, createddate=?, lastmodifiedby=?, lastmodifieddate=?, tenantid=? where bndryid = ? and complainttypeid= ? and id= ?";
	
	private static final String CHECK_DUPLICATE = "select * from egpgr_router where complainttypeid = ? and bndryid = ?";
	
	public static String insertRouter() {
		return INSERT_ROUTER;
	}

	public static String validateRouter() {
		return CHECK_DUPLICATE;
	}

	public static String updateRouter() {
		return UPDATE_ROUTER;
	}
	
	public String getRouterDetail(){
		return BASE_QUERY;
	}
	
	@SuppressWarnings("rawtypes")
	public String getQuery(final RouterTypeGetReq routerTypeRequest, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, routerTypeRequest);
		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final RouterTypeGetReq routerTypeRequest) {

		if (routerTypeRequest.getId() == null && routerTypeRequest.getBoundaryid() == null
				&& routerTypeRequest.getServiceid() == null && routerTypeRequest.getTenantId() == null && 
				routerTypeRequest.getDesignationid() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (routerTypeRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" router.tenantid = ?");
			preparedStatementValues.add(routerTypeRequest.getTenantId());
		}

		if (routerTypeRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" comp.id IN " + getIdQuery(routerTypeRequest.getId()));
		}

		if (routerTypeRequest.getBoundaryid() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" router.bndryid = ?");
			preparedStatementValues.add(routerTypeRequest.getBoundaryid());
		}

		if (routerTypeRequest.getServiceid() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" servicecode = ?");
			preparedStatementValues.add(routerTypeRequest.getServiceid());
		}
		
		if (routerTypeRequest.getDesignationid() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" router.position = ?");
			preparedStatementValues.add(routerTypeRequest.getDesignationid());
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
	private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}

	private static String getIdQuery(final List<Long> idList) {
		final StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++)
				query.append(", " + idList.get(i));
		}
		return query.append(")").toString();
	}
	
	
	/* Query not to be removed till the SEARCH API is complete
	 * SELECT * FROM egpgr_router router LEFT JOIN egpgr_complainttype comp ON router.complainttypeid = comp.id
			LEFT JOIN service_definition sdef ON sdef.code = comp.code 
			LEFT JOIN attribute_definition adef ON sdef.code = adef.servicecode
			LEFT JOIN value_definition vdef ON vdef.attributecode = adef.code
			WHERE 
			router.tenantid = ?
			AND
			router.boundaryid = ?
			AND 
			comp.id = ? 
			AND 
			router.position = ? */

	

}

