/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.demand.repository.querybuilder;

import java.util.List;
import java.util.Set;

import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetailCriteria;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemandQueryBuilder {

	private DemandQueryBuilder() {
		//private constructor to avoid instantiation
	}

	public static final String SEQ_EGBS_QUERY = "SELECT NEXTVAL('sequencename') FROM GENERATE_SERIES(1,?)";

	public static final String BASE_DEMAND_QUERY = "SELECT demand.id AS did,demand.consumercode AS dconsumercode,"
			+ "demand.consumertype AS dconsumertype,demand.businessservice AS dbusinessservice,demand.owner AS downer,"
			+ "demand.taxperiodfrom AS dtaxperiodfrom,demand.taxperiodto AS dtaxperiodto,"
			+ "demand.minimumamountpayable AS dminimumamountpayable,demand.createdby AS dcreatedby,"
			+ "demand.lastmodifiedby AS dlastmodifiedby,demand.createdtime AS dcreatedtime,"
			+ "demand.lastmodifiedtime AS dlastmodifiedtime,demand.tenantid AS dtenantid,"

			+ "demanddetail.id AS dlid,demanddetail.demandid AS dldemandid,demanddetail.taxheadcode AS dltaxheadcode,"
			+ "demanddetail.taxamount AS dltaxamount,demanddetail.collectionamount AS dlcollectionamount,"
			+ "demanddetail.createdby AS dlcreatedby,demanddetail.lastModifiedby AS dllastModifiedby,"
			+ "demanddetail.createdtime AS dlcreatedtime,demanddetail.lastModifiedtime AS dllastModifiedtime,"
			+ "demanddetail.tenantid AS dltenantid " + "FROM egbs_demand demand "
			+ "INNER JOIN egbs_demanddetail demanddetail ON demand.id=demanddetail.demandid "
			+ "AND demand.tenantid=demanddetail.tenantid WHERE ";

	public static final String BASE_DEMAND_DETAIL_QUERY = "SELECT "
			+ "demanddetail.id AS dlid,demanddetail.demandid AS dldemandid,demanddetail.taxheadcode AS dltaxheadcode,"
			+ "demanddetail.taxamount AS dltaxamount,demanddetail.collectionamount AS dlcollectionamount,"
			+ "demanddetail.createdby AS dlcreatedby,demanddetail.lastModifiedby AS dllastModifiedby,"
			+ "demanddetail.createdtime AS dlcreatedtime,demanddetail.lastModifiedtime AS dllastModifiedtime,"
			+ "demanddetail.tenantid AS dltenantid " + " FROM egbs_demanddetail demanddetail "
					+ "INNER JOIN egbs_demand demand ON demanddetail.demandid=demand.id AND "
					+ "demanddetail.tenantid=demand.tenantid WHERE ";

	public static final String DEMAND_QUERY_ORDER_BY_CLAUSE = "demand.id";

	public static final String BASE_DEMAND_DETAIL_QUERY_ORDER_BY_CLAUSE = "demanddetail.id";

	public static final String DEMAND_INSERT_QUERY = "INSERT INTO egbs_demand "
			+ "(id,consumerCode,consumerType,businessService,owner,taxPeriodFrom,taxPeriodTo,"
			+ "minimumAmountPayable,createdby,lastModifiedby,createdtime,lastModifiedtime,tenantid) "
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";

	public static final String DEMAND_DETAIL_INSERT_QUERY = "INSERT INTO egbs_demanddetail "
			+ "(id,demandid,taxHeadCode,taxamount,collectionamount,"
			+ "createdby,lastModifiedby,createdtime,lastModifiedtime,tenantid)" 
			+ " VALUES (?,?,?,?,?,?,?,?,?,?);";

	// FIX ME REMOVE CREATED BY FROM UPDATE
	public static final String DEMAND_UPDATE_QUERY = "UPDATE TABLE egbs_demand SET "
			+ "id=?,consumerCode=?,consumerType=?,businessService=?,owner=?,taxPeriodFrom=?,"
			+ "taxPeriodTo=?,minimumAmountPayable=?,lastModifiedby=?," + "lastModifiedtime=?,tenantid=?) "
			+ "WHERE id=? AND tenantid=?;";

	public static final String DEMAND_DETAIL_UPDATE_QUERY = "UPDATE TABLE egbs_demanddetail SET "
			+ "id=?,demandid=?,taxHeadCode=?,taxamount=?,collectionamount=?,"
			+ "lastModifiedby=?,lastModifiedtime=?,tenantid=? WHERE id=? AND tenantid=?;";

	public static String getDemandQuery(DemandCriteria demandCriteria,Set<String> ownerIds, List<Object> preparedStatementValues) {

		StringBuilder demandQueryBuilder = new StringBuilder(BASE_DEMAND_QUERY);

		demandQueryBuilder.append("demand.tenantid=?");
		preparedStatementValues.add(demandCriteria.getTenantId());

		if (demandCriteria.getDemandId() != null && !demandCriteria.getDemandId().isEmpty()) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demand.id IN ("+getIdQueryForStrings(demandCriteria.getDemandId()));
		}
		if (ownerIds != null && !ownerIds.isEmpty()) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demand.owner IN ("+getIdQueryForStrings(ownerIds));
		}
		if (demandCriteria.getBusinessService() != null) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demand.businessservice=?");
			preparedStatementValues.add(demandCriteria.getBusinessService());
		}
		if (demandCriteria.getConsumerCode() != null) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demand.consumercode=?");
			preparedStatementValues.add(demandCriteria.getConsumerCode());
		}
		if (demandCriteria.getDemandFrom() != null) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demanddetail.taxamount>=?");
			preparedStatementValues.add(demandCriteria.getDemandFrom());
		}
		if (demandCriteria.getDemandTo() != null) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demanddetail.taxamount<=?");
			preparedStatementValues.add(demandCriteria.getDemandTo());
		}
		if (demandCriteria.getType() != null) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demand.type=?");
			preparedStatementValues.add(demandCriteria.getType());
		}
		addOrderByClause(demandQueryBuilder, DEMAND_QUERY_ORDER_BY_CLAUSE);
		addPagingClause(demandQueryBuilder, preparedStatementValues);

		log.debug("the query String for demand : " + demandQueryBuilder.toString());
		return demandQueryBuilder.toString();
	}

	public static String getDemandDetailQuery(DemandDetailCriteria demandDetailCriteria,
			List<Object> preparedStatementValues) {

		StringBuilder demandQueryBuilder = new StringBuilder(BASE_DEMAND_DETAIL_QUERY);

		demandQueryBuilder.append("demanddetail.tenantid=?");
		preparedStatementValues.add(demandDetailCriteria.getTenantId());

		if (demandDetailCriteria.getDemandId() != null) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demanddetail.demandid=?");
			preparedStatementValues.add(demandDetailCriteria.getDemandId());
		}
		if (demandDetailCriteria.getTaxHeadCode() != null) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demanddetail.taxheadcode=?");
			preparedStatementValues.add(demandDetailCriteria.getTaxHeadCode());
		}
		/*if (demandDetailCriteria.getPeriod() != null) {
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demand.taxPeriodFrom<=?");
			preparedStatementValues.add(demandDetailCriteria.getPeriod());
			addAndClause(demandQueryBuilder);
			demandQueryBuilder.append("demand.taxPeriodTo>=?");
			preparedStatementValues.add(demandDetailCriteria.getPeriod());
		}*/
		addOrderByClause(demandQueryBuilder, BASE_DEMAND_DETAIL_QUERY_ORDER_BY_CLAUSE);
		addPagingClause(demandQueryBuilder, preparedStatementValues);
		log.debug("the query String for demand detail: " + demandQueryBuilder.toString());
		return demandQueryBuilder.toString();
	}

	private static void addOrderByClause(StringBuilder demandQueryBuilder,String columnName) {
		demandQueryBuilder.append(" ORDER BY " + columnName);
	}

	private static void addPagingClause(StringBuilder demandQueryBuilder, List<Object> preparedStatementValues) {
		demandQueryBuilder.append(" LIMIT ?");
		preparedStatementValues.add(500);
		demandQueryBuilder.append(" OFFSET ?");
		preparedStatementValues.add(0);
	}

	private static boolean addAndClause(StringBuilder queryString) {
		queryString.append(" AND ");
		return true;
	}
	
	private static String getIdQueryForStrings(Set<String> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {

			String[] list = idList.toArray(new String[idList.size()]);
			query.append("'"+list[0]+"'");
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + "'"+list[i]+"'");
			}
		}
		return query.append(")").toString();
	}
}
