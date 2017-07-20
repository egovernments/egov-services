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

package org.egov.wcms.transanction.repository.builder;

import java.util.List;

import org.egov.wcms.transanction.web.contract.WaterConnectionGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WaterConnectionQueryBuilder {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionQueryBuilder.class);

    
    private static final String SOURCE_QUERY = "SELECT DISTINCT connection.id as conn_id , connection.tenantid as conn_tenant, connection.connectiontype as conn_connType,  connection.billingtype as conn_billtype, connection.categorytype  as conn_catgtype, "   
    		+ " connection.hscpipesizetype as conn_pipesize, connection.applicationType as conn_applntype, connection.consumerNumber as conn_consumerNum, connection.supplytype as conn_suply, connection.sourcetype as conn_sourceType, connection.connectionstatus as conn_constatus, connection.sumpcapacity as conn_sumpcap, " 
    		+ " connection.numberofftaps as conn_nooftaps, connection.parentconnectionid as conn_parentconnectionid, connection.watertreatmentid as conn_watertreatmentid, connection.legacyconsumernumber as conn_legacyconsumernumber, connection.numberofpersons as conn_noofperson, connection.acknowledgmentnumber as conn_acknumber, connection.propertyidentifier as conn_propid, connection.usagetype as conn_usgtype, connection.propertytype as conn_proptype, connection.address " 
    		+ " as conn_propaddress, connection.islegacy as conn_islegacy, connection.donationcharge as conn_doncharge, category.id as category_id, category.code as category_code, category.name as category_name, category.description as category_description,category.active as category_active, category.tenantId as category_tenantId, watersource.id as watersource_id, watersource.code as watersource_code, " 
    		+ " watersource.name as watersource_name, watersource.description as watersource_description,watersource.active as watersource_active, watersource.tenantId as watersource_tenantId, supplytype.id as supplytype_id, supplytype.code as supplytype_code, supplytype.name as supplytype_name, supplytype.description as supplytype_description,supplytype.active as supplytype_active, "  
    		+ " supplytype.tenantId as supplytype_tenantId, pipesize.id as pipesize_id, pipesize.code as pipesize_code, pipesize.sizeinmilimeter as pipesize_sizeinmilimeter, pipesize.sizeininch as pipesize_sizeininch,pipesize.active as pipesize_active, pipesize.tenantId as pipesize_tenantId from egwtr_waterconnection connection , egwtr_category category,egwtr_water_source_type watersource,egwtr_supply_type supplytype, "  
    		+ " egwtr_pipesize pipesize WHERE NULLIF(connection.categorytype, '')::int = category.id AND NULLIF(connection.hscpipesizetype, '')::int=pipesize.id AND NULLIF(connection.sourcetype, '')::int=watersource.id AND NULLIF(connection.supplytype, '')::int=supplytype.id  ";

    public static String insertDocumentQuery() {
        return "INSERT INTO egwtr_documentowner(id,document,name,filestoreid,connectionid,tenantid) values "
                + "(nextval('seq_egwtr_documentowner'),?,?,?,?,?)";
    }

    public static String insertMeterReadingQuery() {
        return "INSERT INTO egwtr_meterreading(id,connectionid,reading,tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime) values "
                + "(nextval('seq_egwtr_meterreading'),?,?,?,?,?,?,?)";
    }

    public static String insertMeterQuery() {

        return "INSERT INTO egwtr_meter(metermake,connectionid,meterreading,tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime)"
                + " values(nextval('seq_egwtr_meter'),?,?,?,?,?,?,?)";
    }
    public static String insertEstimationCharge() {

        return "INSERT INTO egwtr_estimationcharge("
                + "id,connectionid,existingdistributionpipeline,"
                + "pipelinetohomedistance,estimationcharges,supervisioncharges,"
                + "materialcharges,tenantid,createdby,lastmodifiedby,createdtime,lastmodifiedtime)"
                + " values(nextval('seq_egwtr_estimationcharge'),?,?,?,?,?,?,?,"
                + "?,?,?,?)";
    }
    public static String updateEstimationCharge() {

        return "Update egwtr_estimationcharge Set "
                + " existingdistributionpipeline=?"
                + "pipelinetohomedistance=?,estimationcharges=?,supervisioncharges=?,"
                + "materialcharges=?,tenantid=?,lastmodifiedby=?,lastmodifiedtime=? where id=?";
                
    }
    public static String insertMaterial() {

        return "INSERT INTO egwtr_material("
                + "id,estimationchargeid,name,"
                + "quantity,size,amountdetails,"
                + "tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime)"
                + " values(nextval('seq_egwtr_material'),?,?,?,?,?,?,?,"
                + "?,?,?)";
    }

    public static String insertConnectionQuery() {

        return "INSERT INTO egwtr_waterconnection (id,tenantid, connectiontype,"
                + "applicationType, billingtype, categorytype, hscpipesizetype, supplytype, "
                + "sourcetype, connectionstatus, sumpcapacity, numberofftaps, numberofpersons,"
                + " acknowledgmentnumber, createdby, lastmodifiedby, createdtime, lastmodifiedtime,"
                + " propertyidentifier, usagetype, propertytype, address, donationcharge,"
                + "assetidentifier,waterTreatmentId,islegacy,status,stateid,demandid) values"
                + "(nextval('seq_egwtr_waterconnection'),?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?)";
    }

    public static String insertLegacyConnectionQuery() {
        return "INSERT INTO egwtr_waterconnection(id,tenantid, connectiontype,"
                + "applicationType, billingtype, categorytype, hscpipesizetype, supplytype, "
                + "sourcetype, connectionstatus, sumpcapacity, numberofftaps, numberofpersons,"
                + " acknowledgmentnumber, createdby, lastmodifiedby, createdtime, lastmodifiedtime,"
                + " propertyidentifier, usagetype, propertytype, address, donationcharge,"
                + "assetidentifier,waterTreatmentId,islegacy,status,legacyconsumernumber,consumerNumber) values"
                + "(nextval('seq_egwtr_waterconnection'),?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?)";
    }

    public static String insertAdditionalConnectionQuery() {

        return "INSERT INTO egwtr_waterconnection(id,tenantid, connectiontype,applicationType, billingtype, categorytype, hscpipesizetype, supplytype, "
                + "sourcetype, connectionstatus, sumpcapacity, numberofftaps, numberofpersons, acknowledgmentnumber, createdby, "
                + "lastmodifiedby, createdtime, lastmodifiedtime, propertyidentifier, usagetype, propertytype, propertyaddress,donationcharge,"
                + "legacyconsumernumber,consumernumber,parentconnectionid) values"
                + "(nextval('seq_egwtr_waterconnection'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String updateConnectionQuery() {

        return "UPDATE egwtr_waterconnection SET connectiontype = ?, applicationType = ?,billingtype = ?,"
                + "categorytype = ?,hscpipesizetype = ?,sourcetype = ?,connectionstatus =?,"
                + " sumpcapacity=?,numberofftaps=?,numberofpersons=?,lastmodifiedby =?,lastmodifiedtime =?,status=?,stateid=?,demandid=? "
                + " where acknowledgmentnumber = ?";
    }


  public static String getWaterConnectionByacknowledgenumber() {
        return " select * from egwtr_waterconnection connection " + " WHERE connection.acknowledgmentnumber = ?";
    }
    
    public static String getConnectionDetails() { 
    	return SOURCE_QUERY;  
    }
    
    @SuppressWarnings("rawtypes")
	public  String getQuery(final WaterConnectionGetReq waterConnectionGetReq, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(SOURCE_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, waterConnectionGetReq);
		LOGGER.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private  void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final WaterConnectionGetReq waterConnectionGetReq) {

		if (waterConnectionGetReq.getTenantId() == null)
			return;

		selectQuery.append(" AND ");
		boolean isAppendAndClause = false;

		if (null != waterConnectionGetReq.getTenantId()) {
			isAppendAndClause = true;
			selectQuery.append(" connection.tenantid = ?");
			preparedStatementValues.add(waterConnectionGetReq.getTenantId());
		}
		
		if (null != waterConnectionGetReq.getLegacyConsumerNumber()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" connection.legacyconsumernumber = ?");
			preparedStatementValues.add(waterConnectionGetReq.getLegacyConsumerNumber());
		}
		
		if (null != waterConnectionGetReq.getAcknowledgementNumber()) { 
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" connection.acknowledgmentnumber = ?");
			preparedStatementValues.add(waterConnectionGetReq.getAcknowledgementNumber());
		}
		
		if (null != waterConnectionGetReq.getConsumerNumber()) { 
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" connection.consumerNumber = ?");
			preparedStatementValues.add(waterConnectionGetReq.getConsumerNumber());
		}
		
		if((null != waterConnectionGetReq.getName() && !waterConnectionGetReq.getName().isEmpty()) 
				|| (null != waterConnectionGetReq.getMobileNumber() && !waterConnectionGetReq.getMobileNumber().isEmpty()) 
				|| (null != waterConnectionGetReq.getLocality() && !waterConnectionGetReq.getLocality().isEmpty())  
				|| (null != waterConnectionGetReq.getRevenueWard() && !waterConnectionGetReq.getRevenueWard().isEmpty()) 
				|| (null != waterConnectionGetReq.getDoorNumber() && !waterConnectionGetReq.getDoorNumber().isEmpty())) { 
			
		}
		
	/*	if (null != waterConnectionGetReq.getAsgineeId()) { 
			isAppendAndClause = true;
			selectQuery.append(" connection.legacyconsumernumber = ?");
			preparedStatementValues.add(waterConnectionGetReq.getAsgineeId());
		}
*/
		/*if (null != waterConnectionGetReq.getName()) { 
			isAppendAndClause = true;
			selectQuery.append(" connection.legacyconsumernumber = ?");
			preparedStatementValues.add(waterConnectionGetReq.getName());
		}*/
		
		/*if (null != waterConnectionGetReq.getMobileNumber()) { 
			isAppendAndClause = true;
			selectQuery.append(" connection.legacyconsumernumber = ?");
			preparedStatementValues.add(waterConnectionGetReq.getMobileNumber());
		}
		
		if (null != waterConnectionGetReq.getLocality()) { 
			isAppendAndClause = true;
			selectQuery.append(" connection.legacyconsumernumber = ?");
			preparedStatementValues.add(waterConnectionGetReq.getLocality());
		}
		
		if (null != waterConnectionGetReq.getRevenueWard()) { 
			isAppendAndClause = true;
			selectQuery.append(" connection.legacyconsumernumber = ?");
			preparedStatementValues.add(waterConnectionGetReq.getRevenueWard());
		}
		
		if (null != waterConnectionGetReq.getDoorNumber()) { 
			isAppendAndClause = true;
			selectQuery.append(" connection.legacyconsumernumber = ?");
			preparedStatementValues.add(waterConnectionGetReq.getDoorNumber());
		}*/

		if (null != waterConnectionGetReq.getId()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" connection.id IN " + getIdQuery(waterConnectionGetReq.getId()));
		}
		
		if (null != waterConnectionGetReq.getPropertyIdentifierList() && waterConnectionGetReq.getPropertyIdentifierList().size() > 0) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" propertyidentifier.id IN " + getPropertyIdentifierQuery(waterConnectionGetReq.getPropertyIdentifierList()));
		}
/*
		if (serviceGroupRequest.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" name = ?");
			preparedStatementValues.add(serviceGroupRequest.getName());
		}*/

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
	
	private static String getPropertyIdentifierQuery(final List<Long> idList) {
		final StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append("'" + idList.get(0).toString() + "'");
			for (int i = 1; i < idList.size(); i++)
				query.append(",'" + idList.get(i)+"'");
		}
		return query.append(")").toString();
	}
}
