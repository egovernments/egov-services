package org.egov.workflow.persistence.repository.builder;

import org.egov.workflow.web.contract.Router;
import org.egov.workflow.web.contract.RouterRequest;
import org.egov.workflow.web.contract.RouterSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(RouterQueryBuilder.class);

    private static final String BASE_QUERY = "SELECT router.id, router.complainttypeid as service, router.position, router.tenantid, router.bndryid as boundary , router.active FROM egpgr_router router";

    private static final String INSERT_ROUTER = "INSERT INTO egpgr_router(id,complainttypeid, position, bndryid, version, createdby, createddate, lastmodifiedby, lastmodifieddate,tenantid) values"
        + "(nextval('seq_egpgr_router'),?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_ROUTER_WITHOUT_SERVICE = "INSERT INTO egpgr_router(id, position, bndryid, version, createdby, createddate, lastmodifiedby, lastmodifieddate,tenantid) values"
        + "(nextval('seq_egpgr_router'),?,?,?,?,?,?,?,?)";
    private static final String UPDATE_ROUTER = "update egpgr_router SET position =?, version=?, createdby=?, createddate=?, lastmodifiedby=?, lastmodifieddate=?, active = ? where bndryid = ? and complainttypeid= ? and tenantid=?";

    private static final String UPDATE_ROUTER_WITHOUT_SERVICE = "update egpgr_router SET position =?, version=?, createdby=?, createddate=?, lastmodifiedby=?, lastmodifieddate=?, active = ? where complainttypeid is null and bndryid = ? and tenantid=?";

    private static final String CHECK_DUPLICATE = "select * from egpgr_router where complainttypeid = ? and bndryid = ? and tenantid = ?";

    private static final String CHECK_DUPLICATE_WITHOUT_SERVICE = "select * from egpgr_router where complainttypeid IS NULL and bndryid = ? and tenantid=?";

    private static final String ROUTER_BY_HIERARCHY_TYPE = "select  cr.id, cr.position, cr.tenantid, cr.bndryid from egpgr_router cr, eg_boundary boundary , eg_boundary_type boundarytype ,eg_hierarchy_type hierarchytype " +
        "where boundary.boundarytype = boundarytype.id and hierarchytype.id = boundarytype.hierarchytype and cr.bndryid = boundary.id " +
        "and boundary.parent is null and cr.complainttypeid is null and hierarchytype.name= ? and cr.tenantid= ?";

    public static String insertRouter() {
        return INSERT_ROUTER;
    }

    public static String insertRouterWithoutService() {
        return INSERT_ROUTER_WITHOUT_SERVICE;
    }

    public static String validateRouter() {
        return CHECK_DUPLICATE;
    }

    public static String validateRouterWithoutService() {
        return CHECK_DUPLICATE_WITHOUT_SERVICE;
    }

    public static String updateRouter() {
        return UPDATE_ROUTER;
    }

    public static String updateRouterWithoutService() {
        return UPDATE_ROUTER_WITHOUT_SERVICE;
    }

    @SuppressWarnings("rawtypes")
    public String getQuery(final RouterSearchRequest routerTypeRequest, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, routerTypeRequest);
        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    public String getHierarchyTypeQuery() {
        final StringBuilder selectQuery = new StringBuilder(ROUTER_BY_HIERARCHY_TYPE);
        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                final RouterSearchRequest routerSearchRequest) {

        if (null == routerSearchRequest.getId() && null == routerSearchRequest.getBoundaryId()
            && null == routerSearchRequest.getServiceId() && null == routerSearchRequest.getTenantId()
            && null == routerSearchRequest.getHierarchyType()
            && null == routerSearchRequest.getBoundaryTypeId())
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (null != routerSearchRequest.getTenantId()) {
            isAppendAndClause = true;
            selectQuery.append(" router.tenantid = ?");
            preparedStatementValues.add(routerSearchRequest.getTenantId());
        }

        if (null != routerSearchRequest.getId()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" router.id IN " + getIdQuery(routerSearchRequest.getId()));
        }

        if (null != routerSearchRequest.getServiceId() && routerSearchRequest.getServiceId().size() > 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" router.complainttypeid IN " + getIdQuery(routerSearchRequest.getServiceId()));
        }

        if (null != routerSearchRequest.getBoundaryId()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" router.bndryid IN " + getIdQuery(routerSearchRequest.getBoundaryId()));
        }

        if (null != routerSearchRequest.getComplaintTypeCategory()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" router.complainttypeid in (select id from egpgr_complainttype where category = ? and tenantid = ?)");
            preparedStatementValues.add(routerSearchRequest.getComplaintTypeCategory());
            preparedStatementValues.add(routerSearchRequest.getTenantId());
        }

        if (null != routerSearchRequest.getBoundaryTypeId()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" router.bndryid IN (select id from eg_boundary where boundarytype in "
                + getIdQuery(routerSearchRequest.getBoundaryTypeId()) + ")");
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

    public String checkCombinationExistsQuery(RouterRequest routerTypeReq) {
        String query = " SELECT count(*) FROM egpgr_router WHERE tenantid = '" + routerTypeReq.getRouter().getTenantId()
            + "' AND position = " + routerTypeReq.getRouter().getPosition();
        Router rType = routerTypeReq.getRouter();
        StringBuilder builder = new StringBuilder(query);
        if (null != rType.getServices()) {
            builder.append(" AND complainttypeid IN (");
            for (int i = 0; i < rType.getServices().size(); i++) {
                if (i == 0 && i == rType.getServices().size() - 1) {
                    builder.append(rType.getServices().get(i));
                } else if (i == rType.getServices().size() - 1) {
                    builder.append(rType.getServices().get(i));
                } else {
                    builder.append("," + rType.getServices().get(i));
                }
            }
            builder.append(")");
        }
        if (null != rType.getBoundaries()) {
            builder.append(" AND bndryid IN (");
            for (int i = 0; i < rType.getBoundaries().size(); i++) {
                if (i == 0 && i == rType.getBoundaries().size() - 1) {
                    builder.append(rType.getBoundaries().get(i));
                } else if (i == rType.getBoundaries().size() - 1) {
                    builder.append(rType.getBoundaries().get(i));
                } else {
                    builder.append("," + rType.getBoundaries().get(i));
                }
            }
            builder.append(")");
        }
        return builder.toString();
    }

    public String checkIDQuery(RouterRequest routerTypeReq) {
        String query = " SELECT id FROM egpgr_router WHERE tenantid = '" + routerTypeReq.getRouter().getTenantId()
            + "' AND position = " + routerTypeReq.getRouter().getPosition();
        Router rType = routerTypeReq.getRouter();
        StringBuilder builder = new StringBuilder(query);
        if (null != rType.getServices()) {
            builder.append(" AND complainttypeid IN (");
            for (int i = 0; i < rType.getServices().size(); i++) {
                if (i == 0 && i == rType.getServices().size() - 1) {
                    builder.append(rType.getServices().get(i));
                } else if (i == rType.getServices().size() - 1) {
                    builder.append(rType.getServices().get(i));
                } else {
                    builder.append("," + rType.getServices().get(i));
                }
            }
            builder.append(")");
        }
        if (null != rType.getBoundaries()) {
            builder.append(" AND bndryid IN (");
            for (int i = 0; i < rType.getBoundaries().size(); i++) {
                if (i == 0 && i == rType.getBoundaries().size() - 1) {
                    builder.append(rType.getBoundaries().get(i));
                } else if (i == rType.getBoundaries().size() - 1) {
                    builder.append(rType.getBoundaries().get(i));
                } else {
                    builder.append("," + rType.getBoundaries().get(i));
                }
            }
            builder.append(")");
        }
        return builder.toString();
    }

}

