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

package org.egov.wcms.transaction.repository.builder;

import java.util.List;

import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WaterConnectionQueryBuilder {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionQueryBuilder.class);

    
    /*private static final String SOURCE_QUERY = "SELECT DISTINCT connection.id as conn_id , connection.tenantid as conn_tenant, connection.connectiontype as conn_connType,  connection.billingtype as conn_billtype, connection.categorytype  as conn_catgtype, connection.createdtime as createdtime, "   
    		+ " connection.hscpipesizetype as conn_pipesize, connection.applicationType as conn_applntype, connection.consumerNumber as conn_consumerNum, connection.supplytype as conn_suply, connection.sourcetype as conn_sourceType, connection.connectionstatus as conn_constatus, connection.sumpcapacity as conn_sumpcap, " 
    		+ " connection.numberofftaps as conn_nooftaps, connection.parentconnectionid as conn_parentconnectionid, connection.watertreatmentid as conn_watertreatmentid, connection.legacyconsumernumber as conn_legacyconsumernumber, connection.numberofpersons as conn_noofperson, connection.acknowledgmentnumber as conn_acknumber, connection.propertyidentifier as conn_propid, connection.usagetype as conn_usgtype, connection.propertytype as conn_proptype, connection.address " 
    		+ " as conn_propaddress, connection.islegacy as conn_islegacy, connection.donationcharge as conn_doncharge, category.id as category_id, category.code as category_code, category.name as category_name, category.description as category_description,category.active as category_active, category.tenantId as category_tenantId, watersource.id as watersource_id, watersource.code as watersource_code, " 
    		+ " watersource.name as watersource_name, watersource.description as watersource_description,watersource.active as watersource_active, watersource.tenantId as watersource_tenantId, supplytype.id as supplytype_id, supplytype.code as supplytype_code, supplytype.name as supplytype_name, supplytype.description as supplytype_description,supplytype.active as supplytype_active, "  
    		+ " supplytype.tenantId as supplytype_tenantId, pipesize.id as pipesize_id, pipesize.code as pipesize_code, pipesize.sizeinmilimeter as pipesize_sizeinmilimeter, pipesize.sizeininch as pipesize_sizeininch,pipesize.active as pipesize_active, pipesize.tenantId as pipesize_tenantId ,proptype.code as propertytypecode, usagetype.code as usagetypecode, propertyuserdetail.property_owner as propertyowner, plant.name as watertreatmentname  from egwtr_waterconnection connection , egwtr_category category,egwtr_water_source_type watersource,egwtr_supply_type supplytype, "  
    		+ " egwtr_pipesize pipesize, egpt_mstr_propertytype proptype, egpt_mstr_usage usagetype, (select prop.id as property_id, eguser.name property_owner  from egpt_property prop, egpt_property_owner propowner, eg_user eguser where prop.id = propowner.property AND propowner.owner = eguser.id ) propertyuserdetail, egwtr_treatment_plant plant   WHERE NULLIF(connection.categorytype, '')::int = category.id AND NULLIF(connection.hscpipesizetype, '')::int=pipesize.id AND NULLIF(connection.sourcetype, '')::int=watersource.id AND NULLIF(connection.supplytype, '')::int=supplytype.id AND NULLIF(connection.propertytype, '')::int = proptype.id AND NULLIF(connection.usagetype, '')::int = usagetype.id AND NULLIF(connection.watertreatmentid, '')::int = plant.id "; 
    */
	private static final String SOURCE_QUERY = "SELECT DISTINCT connection.id as conn_id , connection.tenantid as conn_tenant, connection.connectiontype as conn_connType, " 
			+ " connection.billingtype as conn_billtype, connection.categorytype  as conn_catgtype, connection.createdtime as createdtime, " 
			+ " connection.hscpipesizetype as conn_pipesize, connection.applicationType as conn_applntype, connection.consumerNumber as conn_consumerNum, " 
			+ " connection.supplytype as conn_suply, connection.sourcetype as conn_sourceType, connection.connectionstatus as conn_constatus,  "
			+ " connection.sumpcapacity as conn_sumpcap, connection.numberofftaps as conn_nooftaps, connection.parentconnectionid as conn_parentconnectionid, " 
			+ " connection.watertreatmentid as conn_watertreatmentid, connection.legacyconsumernumber as conn_legacyconsumernumber, connection.numberofpersons as conn_noofperson, "   
			+ " connection.acknowledgmentnumber as conn_acknumber, connection.propertyidentifier as conn_propid, connection.usagetype as conn_usgtype, "
			+ " connection.propertytype as conn_proptype, connection.address as conn_propaddress, connection.islegacy as conn_islegacy, connection.donationcharge as conn_doncharge, "  
			+ " connection.executiondate as execdate, connection.stateid as conn_stateid, category.id as category_id, category.code as category_code, category.name as category_name, category.description as category_description,category.active as category_active, " 
			+ " connection.subusagetype as subusagetype, 'NA' as subusagename, connection.numberoffamily as numberoffamily, connection.plumbername as plumbername, connection.billsequencenumber as sequencenumber, connection.outsideulb as outsideulb, connection.meterowner as meterowner, connection.metermodel as metermodel,  "
			+ " category.tenantId as category_tenantId, watersource.id as watersource_id, watersource.code as watersource_code, watersource.name as watersource_name, "
			+ " watersource.description as watersource_description,watersource.active as watersource_active, watersource.tenantId as watersource_tenantId, supplytype.id as supplytype_id, " 
			+ " supplytype.code as supplytype_code, supplytype.name as supplytype_name, supplytype.description as supplytype_description,supplytype.active as supplytype_active,  "
			+ " supplytype.tenantId as supplytype_tenantId, pipesize.id as pipesize_id, pipesize.code as pipesize_code, pipesize.sizeinmilimeter as pipesize_sizeinmilimeter,  "
			+ " pipesize.sizeininch as pipesize_sizeininch,pipesize.active as pipesize_active, pipesize.tenantId as pipesize_tenantId ,propertyuserdetail.property_owner as propertyowner, "
			+ " propertyuserdetail.aadhaarnumber, propertyuserdetail.mobilenumber, propertyuserdetail.emailid, propertyuserdetail.propertylocation, " 
			+ " plant.name as watertreatmentname from egwtr_waterconnection connection , egwtr_category category,egwtr_water_source_type watersource,egwtr_supply_type supplytype, " 
			+ " egwtr_pipesize pipesize, (select prop.id as property_id, prop.upicnumber as prop_upicnumber, eguser.name property_owner, " 
			+ " eguser.aadhaarnumber as aadhaarnumber, eguser.mobilenumber as mobilenumber, eguser.emailid as emailid, proploc.locationboundary as propertylocation "   
			+ " from egpt_property prop, egpt_property_owner propowner, eg_user eguser, egpt_propertylocation proploc " 
			+ " where prop.id = propowner.property AND propowner.owner = eguser.id AND prop.id = proploc.property ) propertyuserdetail, egwtr_treatment_plant plant WHERE NULLIF(connection.categorytype, '')::int = category.id AND NULLIF(connection.hscpipesizetype, '')::int=pipesize.id AND " 
			+ " NULLIF(connection.sourcetype, '')::int=watersource.id AND NULLIF(connection.supplytype, '')::int=supplytype.id AND connection.propertyidentifier =propertyuserdetail.prop_upicnumber AND "  
			+ " NULLIF(connection.watertreatmentid, '')::int = plant.id " ;  
	
	       private static final String QUERY_WITHOUT_PROP = "SELECT DISTINCT conndetails.id as conn_id , conndetails.tenantid as conn_tenant, conndetails.connectiontype as conn_connType, "  
                       + " conndetails.billingtype as conn_billtype, conndetails.categorytype  as conn_catgtype, conndetails.createdtime as createdtime, " 
                       + " conndetails.hscpipesizetype as conn_pipesize, conndetails.applicationType as conn_applntype, conndetails.consumerNumber as conn_consumerNum, "  
                       + " conndetails.supplytype as conn_suply, conndetails.sourcetype as conn_sourceType, conndetails.connectionstatus as conn_constatus, " 
                       + " conndetails.sumpcapacity as conn_sumpcap, conndetails.numberofftaps as conn_nooftaps, conndetails.parentconnectionid as conn_parentconnectionid, "  
                       + " conndetails.watertreatmentid as conn_watertreatmentid, conndetails.legacyconsumernumber as conn_legacyconsumernumber, conndetails.numberofpersons as conn_noofperson, "   
                       + " conndetails.acknowledgmentnumber as conn_acknumber, conndetails.propertyidentifier as conn_propid, conndetails.usagetype as conn_usgtype, "
                       + " conndetails.propertytype as conn_proptype, conndetails.address as conn_propaddress, conndetails.islegacy as conn_islegacy, conndetails.donationcharge as conn_doncharge, " 
                       + " conndetails.executiondate as execdate, conndetails.stateid as conn_stateid, category.id as category_id, category.code as category_code, category.name as category_name, category.description as category_description,category.active as category_active, "
                       + " conndetails.subusagetype as subusagetype, 'NA' as subusagename, conndetails.numberoffamily as numberoffamily, conndetails.plumbername as plumbername, conndetails.billsequencenumber as sequencenumber, conndetails.outsideulb as outsideulb, conndetails.meterowner as meterowner, conndetails.metermodel as metermodel, "
                       + " category.tenantId as category_tenantId, watersource.id as watersource_id, watersource.code as watersource_code, watersource.name as watersource_name, "
                       + " watersource.description as watersource_description,watersource.active as watersource_active, watersource.tenantId as watersource_tenantId, supplytype.id as supplytype_id, " 
                       + " supplytype.code as supplytype_code, supplytype.name as supplytype_name, supplytype.description as supplytype_description,supplytype.active as supplytype_active,  "
                       + " supplytype.tenantId as supplytype_tenantId, pipesize.id as pipesize_id, pipesize.code as pipesize_code, pipesize.sizeinmilimeter as pipesize_sizeinmilimeter, "
                       + " pipesize.sizeininch as pipesize_sizeininch,pipesize.active as pipesize_active, pipesize.tenantId as pipesize_tenantId , "
                       + " plant.name as watertreatmentname, useraddress.address as addressline1 ,useraddress.pincode as pincode, useraddress.city as city, connloc.revenueboundary as revenueboundary, connloc.locationboundary as locationboundary, " 
                       + " connloc.adminboundary as adminboundary, eguser.name as name, eguser.username as username, eguser.mobilenumber as mobilenumber, eguser.emailid as emailid, eguser.gender as gender, " 
                       + " eguser.aadhaarnumber as aadhaarnumber, meter.metermake as metermake, meter.initialmeterreading as initialmeterreading, meter.meterslno as meterslno, meter.metercost as metercost from "
                       + " egwtr_waterconnection conndetails "
                       + " left join egwtr_connectionlocation connloc on conndetails.locationid = connloc.id "
                       + " left join eg_user eguser  on conndetails.userid = eguser.id"
                       + "  left join EG_USER_ADDRESS useraddress  on useraddress.userid = eguser.id "
                       + " left join egwtr_category category on  NULLIF(conndetails.categorytype, '')::int = category.id " 
                       + " left join egwtr_water_source_type watersource ON NULLIF(conndetails.sourcetype, '')::int=watersource.id  left join egwtr_supply_type supplytype "
                       + " ON NULLIF(conndetails.supplytype, '')::int=supplytype.id left join egwtr_pipesize pipesize on NULLIF(conndetails.hscpipesizetype, '')::int=pipesize.id " 
                       + " left join egwtr_treatment_plant plant ON NULLIF(conndetails.watertreatmentid, '')::int = plant.id left join egwtr_meter meter ON meter.connectionid = conndetails.id  "
                       + " where useraddress.type='PERMANENT' " ; 		
    public static String insertDocumentQuery() {
        return "INSERT INTO egwtr_documentowner(id,document,name,filestoreid,connectionid,tenantid) values "
                + "(nextval('seq_egwtr_documentowner'),?,?,?,?,?)";
    }

    public static String insertMeterReadingQuery() {
        return "INSERT INTO egwtr_meterreading(id,meterid,reading,readingDate,tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime) values "
                + "(nextval('seq_egwtr_meterreading'),?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?)";
    }

    public static String insertMeterQuery() {

        return "INSERT INTO egwtr_meter(id,connectionid,metermake,initialmeterreading,meterSlNo,meterCost,"
                + "tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime)"
                + " values(nextval('seq_egwtr_meter'),?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?)";
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

    public static String getDocumentForConnection() {

        return "SELECT id, document, name, filestoreid, connectionid, tenantid FROM egwtr_documentowner WHERE connectionid = ? AND tenantid = ?";
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
                + "assetidentifier,waterTreatmentId,islegacy,status,numberOfFamily,subusagetype,"
                + "plumbername,billsequencenumber,meterowner,metermodel,outsideulb) values"
                + "(nextval('seq_egwtr_waterconnection'),?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?)";
    }

    public static String insertDemandConnection() {

        return "INSERT INTO egwtr_demand_connection("
                + "id,connectionid,demandid,"
                + "tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime)"
                + " values(nextval('seq_egwtr_demand_connection'),?,?,?,?,?,?,?)";
    }

    public static String insertLegacyConnectionQuery() {
        return "INSERT INTO egwtr_waterconnection(id,tenantid, connectiontype,applicationType, billingtype, "
                + "categorytype, hscpipesizetype, supplytype, sourcetype, connectionstatus,"
                + " sumpcapacity, numberofftaps, numberofpersons, acknowledgmentnumber, createdby,"
                + " lastmodifiedby, createdtime, lastmodifiedtime,propertyidentifier, usagetype, "
                + "propertytype, address, donationcharge,assetidentifier,waterTreatmentId,"
                + "islegacy,status,numberOfFamily,subusagetype,plumbername,"
                + "billsequencenumber,meterowner,metermodel,outsideulb,legacyconsumernumber,"
                + "consumerNumber,executionDate,noOfFlats,manualconsumernumber,housenumber) values"
                + "(nextval('seq_egwtr_waterconnection'),?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?,"
                + " ?,?,?,?,?,"
                + "?,?,?,?,?)";
    }

    public static String insertAdditionalConnectionQuery() {

        return "INSERT INTO egwtr_waterconnection(id,tenantid, connectiontype,applicationType, billingtype, categorytype, hscpipesizetype, supplytype, "
                + "sourcetype, connectionstatus, sumpcapacity, numberofftaps, numberofpersons, acknowledgmentnumber, createdby, "
                + "lastmodifiedby, createdtime, lastmodifiedtime, propertyidentifier, usagetype, propertytype, propertyaddress,donationcharge,"
                + "legacyconsumernumber,consumernumber,parentconnectionid) values"
                + "(nextval('seq_egwtr_waterconnection'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String updateConnectionQuery() {

    	return "UPDATE egwtr_waterconnection SET stateid = ? where acknowledgmentnumber = ?"; 
        /*return "UPDATE egwtr_waterconnection SET connectiontype = ?, applicationType = ?,billingtype = ?,"
                + "categorytype = ?,hscpipesizetype = ?,sourcetype = ?,connectionstatus =?,"
                + " sumpcapacity=?,numberofftaps=?,numberofpersons=?,lastmodifiedby =?,lastmodifiedtime =?,stateid=? ,numberOfFamily=?,"
                + " status=?, estimationnumber=?, workordernumber=?, consumernumber=?"
                + " where acknowledgmentnumber = ?";*/
    }

    public static String updateConnectionAfterWorkFlowQuery() {

        return "UPDATE egwtr_waterconnection SET lastmodifiedtime =?,status =? "
                + " where acknowledgmentnumber = ?";
    }
    
    public static String updateValuesForNoPropertyConnections() { 
    	return "UPDATE egwtr_waterconnection SET userid = ?, addressid = ? , locationid = ? WHERE acknowledgmentnumber = ? " ; 
    }

    public static String persistEstimationNoticeQuery() {
        return "INSERT INTO egwtr_estimationnotice_audit_log (id, waterconnectionid, tenantid, dateofletter, letternumber, letterto, letterintimationsubject, applicationnumber, applicationdate, applicantname, servicename, waternumber, sladays, chargeDescription1, chargeDescription2, createdby, createdtime) "
                + "values (nextval('seq_egwtr_estimationnotice_audit_log'),:waterConnectionId,:tenantId,:dateOfLetter,:letterNumber,:letterTo,:letterIntimationSubject,:applicationNumber,:applicationDate,:applicantName,:serviceName,:waterNumber,:slaDays,:chargeDescription1,:chargeDescription2,:createdBy,:createdDate) ";
    }

    public static String persistWorkOrderQuery() {
        return "INSERT INTO egwtr_workorder_audit_log (id, waterconnectionid, tenantid, workordernumber, workorderdate, watertapownername, acknumber,  acknumberdate, hscnumber, hscnumberdate, servicename, plumbername, createdby, createdtime) "
                + "values (nextval('seq_egwtr_workorder_audit_log'),:waterConnectionId,:tenantId,:workOrderNumber,:workOrderDate,:waterTapOwnerName,:ackNumber,:ackNumberDate,:hscNumber,:hscNumberDate,:serviceName,:plumberName,:createdBy,:createdDate) ";
    }

    public static String getWaterConnectionByacknowledgenumber() {
        return " select * from egwtr_waterconnection connection " + " WHERE connection.acknowledgmentnumber = ? ";
    }

    public static String getWaterConnectionByConsumerNumber() {
        return " select * from egwtr_waterconnection connection " + " WHERE  connection.islegacy=true and  connection.consumernumber = ?";
    }
    
    public static String getWaterConnectionAddressQueryForInsert() { 
    	return " INSERT INTO egwtr_address (id, tenantid, latitude, longitude, addressId, addressNumber, addressLine1, addressLine2, landmark, doorno, city, pincode, detail, route, street, area, roadname, createdby, createdtime) " 
    			+" values (nextval('seq_egwtr_address'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  " ;
    }
    
    public static String getWaterConnectionLocationQueryForInsert() { 
    	return " INSERT INTO egwtr_connectionlocation (id, revenueboundary, locationboundary, adminboundary, createdby, createdtime) " 
    			+ " VALUES (nextval('seq_egwtr_connectionlocation'), ?, ?, ?, ?, ?) "; 
    }

    public static String getConnectionDetails() {
        return SOURCE_QUERY;
    }
    
    public String getSecondQuery(final WaterConnectionGetReq waterConnectionGetReq, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(QUERY_WITHOUT_PROP);
        addSecondQueryWhereClause(selectQuery, preparedStatementValues, waterConnectionGetReq);
        LOGGER.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings("rawtypes")
    public String getQuery(final WaterConnectionGetReq waterConnectionGetReq, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(SOURCE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, waterConnectionGetReq);
        LOGGER.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
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

        if (waterConnectionGetReq.getAcknowledgementNumber() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" connection.acknowledgmentnumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getAcknowledgementNumber());
        }
        if (waterConnectionGetReq.getStateId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" connection.stateid = ?");
            preparedStatementValues.add(waterConnectionGetReq.getStateId());
        }

        if (null != waterConnectionGetReq.getConsumerNumber()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" connection.consumerNumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getConsumerNumber());
        }


        /*
         * if (null != waterConnectionGetReq.getAsgineeId()) { isAppendAndClause = true; selectQuery.append(
         * " connection.legacyconsumernumber = ?"); preparedStatementValues.add(waterConnectionGetReq.getAsgineeId()); }
         */
        /*
         * if (null != waterConnectionGetReq.getName()) { isAppendAndClause = true; selectQuery.append(
         * " connection.legacyconsumernumber = ?"); preparedStatementValues.add(waterConnectionGetReq.getName()); }
         */

        /*
         * if (null != waterConnectionGetReq.getMobileNumber()) { isAppendAndClause = true; selectQuery.append(
         * " connection.legacyconsumernumber = ?"); preparedStatementValues.add(waterConnectionGetReq.getMobileNumber()); } if
         * (null != waterConnectionGetReq.getLocality()) { isAppendAndClause = true; selectQuery.append(
         * " connection.legacyconsumernumber = ?"); preparedStatementValues.add(waterConnectionGetReq.getLocality()); } if (null
         * != waterConnectionGetReq.getRevenueWard()) { isAppendAndClause = true; selectQuery.append(
         * " connection.legacyconsumernumber = ?"); preparedStatementValues.add(waterConnectionGetReq.getRevenueWard()); } if
         * (null != waterConnectionGetReq.getDoorNumber()) { isAppendAndClause = true; selectQuery.append(
         * " connection.legacyconsumernumber = ?"); preparedStatementValues.add(waterConnectionGetReq.getDoorNumber()); }
         */

        if (null != waterConnectionGetReq.getId()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" connection.id IN " + getIdQuery(waterConnectionGetReq.getId()));
        }

        if (null != waterConnectionGetReq.getPropertyIdentifierList()
                && waterConnectionGetReq.getPropertyIdentifierList().size() > 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" connection.propertyidentifier IN "
                    + getPropertyIdentifierQuery(waterConnectionGetReq.getPropertyIdentifierList()));
        }
        
        if(((null != waterConnectionGetReq.getName() && !waterConnectionGetReq.getName().isEmpty()) 
				||  (null != waterConnectionGetReq.getMobileNumber() && !waterConnectionGetReq.getMobileNumber().isEmpty()) 
				||  (null != waterConnectionGetReq.getLocality() && !waterConnectionGetReq.getLocality().isEmpty())
				||  (null != waterConnectionGetReq.getDoorNumber() && !waterConnectionGetReq.getDoorNumber().isEmpty()) 
				||  (null != waterConnectionGetReq.getRevenueWard() && !waterConnectionGetReq.getRevenueWard().isEmpty()))
				&& (waterConnectionGetReq.getPropertyIdentifierList().size() <= 0)) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
        	selectQuery.append(" connection.propertyidentifier IN ('') ");
        }
        
        /*
         * if (serviceGroupRequest.getName() != null) { isAppendAndClause = addAndClauseIfRequired(isAppendAndClause,
         * selectQuery); selectQuery.append(" name = ?"); preparedStatementValues.add(serviceGroupRequest.getName()); }
         */

    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addSecondQueryWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final WaterConnectionGetReq waterConnectionGetReq) {

        if (waterConnectionGetReq.getTenantId() == null)
            return;

        selectQuery.append(" AND ");
        boolean isAppendAndClause = false;

        if (null != waterConnectionGetReq.getTenantId()) {
            isAppendAndClause = true;
            selectQuery.append(" conndetails.tenantid = ?");
            preparedStatementValues.add(waterConnectionGetReq.getTenantId());
        }

        if (null != waterConnectionGetReq.getLegacyConsumerNumber()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.legacyconsumernumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getLegacyConsumerNumber());
        }

        if (waterConnectionGetReq.getAcknowledgementNumber() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.acknowledgmentnumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getAcknowledgementNumber());
        }
        if (waterConnectionGetReq.getStateId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.stateid = ?");
            preparedStatementValues.add(waterConnectionGetReq.getStateId());
        }

        if (null != waterConnectionGetReq.getConsumerNumber()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.consumerNumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getConsumerNumber());
        }
        
        if (null != waterConnectionGetReq.getName() && !waterConnectionGetReq.getName().isEmpty()) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" eguser.name = ?");
            preparedStatementValues.add(waterConnectionGetReq.getName());
        }
        
        if (null != waterConnectionGetReq.getMobileNumber() && !waterConnectionGetReq.getMobileNumber().isEmpty()) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" eguser.mobilenumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getMobileNumber());
        }
        
        if (null != waterConnectionGetReq.getLocality() && !waterConnectionGetReq.getLocality().isEmpty()) { 
        	isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" (connloc.revenueboundary = ? OR connloc.locationboundary = ? OR connloc.adminboundary = ? )");
            preparedStatementValues.add(waterConnectionGetReq.getLocality());
            preparedStatementValues.add(waterConnectionGetReq.getLocality());
            preparedStatementValues.add(waterConnectionGetReq.getLocality());
        }

        if (null != waterConnectionGetReq.getId()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.id IN " + getIdQuery(waterConnectionGetReq.getId()));
        }
    }

    /**
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
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
                query.append(",'" + idList.get(i) + "'");
        }
        return query.append(")").toString();
    }
}
