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
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.wcms.transaction.config.ApplicationProperties;
import org.egov.wcms.transaction.web.contract.ConnectionDocumentGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterConnectionQueryBuilder {
	@Autowired
    private ApplicationProperties applicationProperties;

    public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionQueryBuilder.class);

    public static final String CONNECTION_TABLE_NAME = "egwtr_waterconnection";

    private static final String SOURCE_QUERY = "SELECT DISTINCT connection.id as conn_id , connection.tenantid as conn_tenant, connection.connectiontype as conn_connType, "
            + " connection.userid as conn_userid, connection.billingtype as conn_billtype, connection.createdtime as createdtime, connection.hscpipesizetype as conn_pipesize, connection.applicationType as conn_applntype, connection.consumerNumber as conn_consumerNum, "
            + " connection.supplytype as conn_suply, connection.sourcetype as conn_sourceType, connection.connectionstatus as conn_constatus, "
            + " connection.sumpcapacity as conn_sumpcap, connection.numberofftaps as conn_nooftaps, connection.parentconnectionid as conn_parentconnectionid, "
            + " connection.watertreatmentid as conn_watertreatmentid, connection.legacyconsumernumber as conn_legacyconsumernumber, connection.numberofpersons as conn_noofperson, "
            + " connection.acknowledgmentnumber as conn_acknumber, connection.propertyidentifier as conn_propid, connection.usagetype as conn_usgtype, "
            + " connection.islegacy as conn_islegacy, connection.donationcharge as conn_doncharge,connection.status as status, "
            + " connection.executiondate as execdate, connection.stateid as conn_stateid, connection.manualreceiptnumber as manualreceiptnumber, connection.manualreceiptdate as manualreceiptdate, connection.housenumber as housenumber, "
            + " connection.manualconsumernumber as manualconsumernumber, connection.subusagetype as conn_subusagetype,  connection.numberoffamily as numberoffamily, connection.plumbername as plumbername, connection.billsequencenumber as sequencenumber, "
            + " connection.outsideulb as outsideulb, connection.storagereservoir as conn_storagereservoir, connloc.revenueboundary as revenueboundary, connloc.locationboundary as locationboundary, connloc.adminboundary as adminboundary, connloc.buildingname as buildingname, connloc.billingaddress as billingaddress, connloc.roadname as roadname, connloc.gisnumber as gisnumber from egwtr_waterconnection connection "
            + " left join egwtr_connectionlocation connloc on connection.locationid = connloc.id "
            + " where connection.propertyidentifier is not null ";

    private static final String COUNT_WITH_PROP_QUERY = "SELECT count(*) from egwtr_waterconnection connection "
            + " left join egwtr_connectionlocation connloc on connection.locationid = connloc.id "
            + " where connection.propertyidentifier is not null ";

    private static final String QUERY_WITHOUT_PROP = "SELECT DISTINCT conndetails.id as conn_id , conndetails.tenantid as conn_tenant, conndetails.connectiontype as conn_connType, conndetails.billingtype as conn_billtype, conndetails.createdtime as createdtime, "
            + " conndetails.hscpipesizetype as conn_pipesize, conndetails.applicationType as conn_applntype, conndetails.consumerNumber as conn_consumerNum, conndetails.supplytype as conn_suply, conndetails.sourcetype as conn_sourceType, conndetails.connectionstatus as conn_constatus, "
            + " conndetails.sumpcapacity as conn_sumpcap, conndetails.numberofftaps as conn_nooftaps, conndetails.parentconnectionid as conn_parentconnectionid, conndetails.watertreatmentid as conn_watertreatmentid, conndetails.legacyconsumernumber as conn_legacyconsumernumber, "
            + " conndetails.numberofpersons as conn_noofperson, conndetails.acknowledgmentnumber as conn_acknumber, conndetails.propertyidentifier as conn_propid, conndetails.usagetype as conn_usgtype, "
            + " conndetails.islegacy as conn_islegacy, conndetails.donationcharge as conn_doncharge,conndetails.status as status, conndetails.executiondate as execdate, conndetails.stateid as conn_stateid, "
            + " conndetails.manualreceiptnumber as manualreceiptnumber, conndetails.manualreceiptdate as manualreceiptdate, conndetails.housenumber as housenumber, conndetails.manualconsumernumber as manualconsumernumber, conndetails.subusagetype as conn_subusagetype, conndetails.numberoffamily as numberoffamily, conndetails.plumbername as plumbername, conndetails.billsequencenumber as sequencenumber, conndetails.outsideulb as outsideulb, "
            + " connloc.revenueboundary as revenueboundary, connloc.locationboundary as locationboundary, connloc.adminboundary as adminboundary, connloc.buildingname as buildingname, connloc.billingaddress as billingaddress, connloc.roadname as roadname, connloc.gisnumber as gisnumber,  "
            + " meter.metermake as metermake, meter.initialmeterreading as initialmeterreading, meter.meterslno as meterslno, meter.metercost as metercost , "
            + " conndetails.storagereservoir as conn_storagereservoir from egwtr_waterconnection conndetails "
            + " left join egwtr_connectionlocation connloc on conndetails.locationid = connloc.id "
            + " left join egwtr_meter meter ON meter.connectionid = conndetails.id "
            + " where conndetails.propertyidentifier is null ";
    
    private static final String COUNT_WITHOUT_PROP_QUERY = "Select count(*) as count from egwtr_waterconnection conndetails "
            + " left join egwtr_connectionlocation connloc on conndetails.locationid = connloc.id "
            + " left join egwtr_meter meter ON meter.connectionid = conndetails.id "
            + " where conndetails.propertyidentifier is null ";
    
    

    public static String getConnectionMeterQueryForSearch() {
        return "select conn.id as connectionid, conn.acknowledgmentnumber, conn.consumernumber, conn.tenantid, "
                + " meter.id as meterid, meter.metermake, meter.initialmeterreading, meter.meterslno, meter.metercost, meter.meterowner, meter.metermodel, meter.maximummeterreading, meter.meterstatus, meter.tenantid as metertenant, "
                + " meterreading.reading, meterreading.readingdate, meterreading.gapcode, meterreading.consumption, meterreading.consumptionadjusted, meterreading.numberofdays, meterreading.resetflag "
                + " from egwtr_waterconnection conn left join egwtr_meter meter on conn.id = meter.connectionid "
                + " left join egwtr_meterreading meterreading on meter.id = meterreading.meterid "
                + " where conn.id =  ? ";
    }
    
    public static String BASE_CONNECTION_DOCUMENT_QUERY = "Select id,documenttype,referencenumber,connectionid,filestoreid,tenantid,createdby,createddate,lastmodifiedby,lastmodifieddate from egwtr_connectiondocument";

    public static String insertDocumentQuery() {
        return "INSERT INTO egwtr_documentowner(id,document,name,filestoreid,connectionid,tenantid) values "
                + "(nextval('seq_egwtr_documentowner'),?,?,?,?,?)";
    }

    public static String getConnectionObjById() {

        return ("select count(*) as count from " + CONNECTION_TABLE_NAME + " where id =:id");
    }


    public static String insertMeterReadingQuery() {
        return "INSERT INTO egwtr_meterreading(id,meterid,reading,readingDate,tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime,gapcode,"
                + "consumption,consumptionadjusted,numberofdays,resetflag) values "
                + "(nextval('seq_egwtr_meterreading'),?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?,"
                + "?)";
    }
    
    public static String removeMeterReadingQuery() { 
    	return "DELETE from egwtr_meterreading WHERE meterid = :meterId"; 
    }

    public static String insertMeterQuery() {

        return "INSERT INTO egwtr_meter(id,connectionid,metermake,initialmeterreading,meterSlNo,meterCost,"
                + "tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime,meterowner,metermodel,maximumMeterreading,meterstatus)"
                + " values(nextval('seq_egwtr_meter'),?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?,"
                + "?,?)";
    }
    
    public static String updateMeterQuery() { 
    	return  "UPDATE egwtr_meter SET metermake = :metermake, initialmeterreading = :initialmeterreading, meterSlNo = :meterSlNo, meterCost = :meterCost, " 
    			+ " lastmodifiedby = :lastmodifiedby, lastmodifiedtime = :lastmodifiedtime, meterowner = :meterowner, metermodel = :metermodel, maximummeterreading = :maximumMeterReading, meterstatus = :meterStatus " 
    			+ " where id = :meterId and connectionid = :connectionId and tenantid = :tenantId"; 
    }

 
    public static String getDocumentForConnection() {

        return "SELECT id, document, name, filestoreid, connectionid, tenantid FROM egwtr_documentowner WHERE connectionid = ? AND tenantid = ?";
    }


    public static String insertConnectionQuery() {

        return "INSERT INTO egwtr_waterconnection (id,tenantid,connectiontype,"
                + "applicationType, billingtype, hscpipesizetype, supplytype, "
                + "sourcetype, connectionstatus, sumpcapacity, numberofftaps, numberofpersons,"
                + " acknowledgmentnumber, createdby, lastmodifiedby, createdtime, lastmodifiedtime,"
                + " propertyidentifier, oldpropertyidentifier, usagetype, donationcharge,"
                + " waterTreatmentId,islegacy,status,numberOfFamily,subusagetype,"
                + "plumbername,billsequencenumber,outsideulb, storagereservoir) values"
                + "(nextval('seq_egwtr_waterconnection'),?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?)";
    }

    public static String insertDemandConnection() {

        return "INSERT INTO egwtr_demand_connection("
                + "id,connectionid,demandid,"
                + "tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime)"
                + " values(nextval('seq_egwtr_demand_connection'),?,?,?,?,?,?,?)";
    }

    public static String insertLegacyConnectionQuery() {
        return "INSERT INTO egwtr_waterconnection(id,tenantid, connectiontype,applicationType, billingtype, "
                + "hscpipesizetype, supplytype, sourcetype, connectionstatus,"
                + " sumpcapacity, numberofftaps, numberofpersons, acknowledgmentnumber, createdby,"
                + " lastmodifiedby, createdtime, lastmodifiedtime,propertyidentifier, oldpropertyidentifier,  usagetype, "
                + "donationcharge,waterTreatmentId,"
                + "islegacy,status,numberOfFamily,subusagetype,plumbername,"
                + "billsequencenumber,outsideulb,storagereservoir,legacyconsumernumber,"
                + "consumerNumber,executionDate,noOfFlats,manualconsumernumber,housenumber,manualreceiptnumber,manualreceiptdate) values"
                + "(nextval('seq_egwtr_waterconnection'),?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?"
                + ",?,?,?,?,?,"
                + " ?,?,?,?,?"
                + ",?,?,?)";
    }

    public static String insertAdditionalConnectionQuery() {

        return "INSERT INTO egwtr_waterconnection(id,tenantid, connectiontype,applicationType, billingtype, hscpipesizetype, supplytype, "
                + "sourcetype, connectionstatus, sumpcapacity, numberofftaps, numberofpersons, acknowledgmentnumber, createdby, "
                + "lastmodifiedby, createdtime, lastmodifiedtime, propertyidentifier, oldpropertyidentifier, usagetype, propertyaddress,donationcharge,"
                + "legacyconsumernumber,consumernumber,parentconnectionid) values"
                + "(nextval('seq_egwtr_waterconnection'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String updateConnectionQuery() {
        return "UPDATE egwtr_waterconnection SET stateid = ? where acknowledgmentnumber = ? and tenantid = ? ";
    }
    
    public static String updateConnection() {
        return "UPDATE egwtr_waterconnection SET   tenantid=?, hscpipesizetype=?, supplytype=?, "
                + " sourcetype=?, connectionstatus=?, sumpcapacity=?, numberofftaps=?, "
                + " numberofpersons=?, lastmodifiedby=?, lastmodifiedtime=?, "
                + "  usagetype=?, "
                + " waterTreatmentId=?,status=?,numberOfFamily=?,subusagetype=?, "
                + " plumbername=?,billsequencenumber=?,outsideulb=?, storagereservoir=?,stateid=?,"
                + " estimationnumber=?,workordernumber=?,consumernumber=? where acknowledgmentnumber = ?";
    }
    
    public static String updateLegacyConnectionQuery() { 
    	return "UPDATE egwtr_waterconnection SET hscpipesizetype=?, supplytype=?, "
                + " sourcetype=?, connectionstatus=?, sumpcapacity=?, numberofftaps=?, "
                + " numberofpersons=?, lastmodifiedby=?, lastmodifiedtime=?, "
                + "  usagetype=?, "
                + " waterTreatmentId=?,status=?,numberOfFamily=?,subusagetype=?, "
                + " plumbername=?,billsequencenumber=?,outsideulb=?, storagereservoir=?,stateid=?,"
                + " estimationnumber=?,workordernumber=?,consumernumber=?, legacyconsumernumber = ?,  " 
                + " manualconsumernumber = ?, propertyidentifier = ? where consumernumber = ? and tenantid = ? "; 
    }

    public static String updateConnectionAfterWorkFlowQuery() {

        return "UPDATE egwtr_waterconnection SET lastmodifiedtime =?,status =? "
                + " where acknowledgmentnumber = ?";
    }

    public static String updateValuesForNoPropertyConnections() {
        return "UPDATE egwtr_waterconnection SET addressid = ? , locationid = ? WHERE acknowledgmentnumber = ? and tenantid = ? ";
    }
    
    public static String updateValuesForWithPropertyConnections() { 
        return "UPDATE egwtr_waterconnection SET locationid = ? WHERE acknowledgmentnumber = ? and tenantid = ? ";
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
        return " select * from egwtr_waterconnection connection "
                + " WHERE connection.acknowledgmentnumber =:acknowledgeNumber and connection.tenantid=:tenantid  ";
    }

    public static String getWaterConnectionByConsumerNumber() {
        return " select * from egwtr_waterconnection connection "
                + " WHERE  connection.islegacy=true and  connection.consumernumber =:consumerNumber  and connection.tenantid=:tenantid";
    }

    public static String getWaterConnectionByLegacyConsumernumber() {
        return " select * from egwtr_waterconnection connection "
                + " WHERE  connection.islegacy=true and  connection.legacyconsumernumber =:legacyConsumerNumber  and connection.tenantid=:tenantid";
    }

    public static String getWaterConnectionAddressQueryForInsert() {
        return " INSERT INTO egwtr_address (id, tenantid, latitude, longitude, addressId, addressNumber, addressLine1, addressLine2, landmark, doorno, city, pincode, detail, route, street, area, roadname, createdby, createdtime) "
                + " values (nextval('seq_egwtr_address'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  ";
    }

    public static String getWaterConnectionLocationQueryForInsert() {
        return " INSERT INTO egwtr_connectionlocation (id, revenueboundary, locationboundary, adminboundary,"
                + " billingaddress, buildingname, gisnumber, roadname, createdby, createdtime) "
                + " VALUES (nextval('seq_egwtr_connectionlocation'), ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    }
    
    public static String getConnectionOwnersByConnectionId() {
        return " SELECT * FROM  egwtr_connection_owners where waterconnectionid =:waterconnectionid  and tenantid=:tenantid ";
    }

    public static String getNextConsumerNumberFromSequence() {
        return " SELECT nextval('seq_egwtr_consumernumber') as nextConsumerNumber ";
    }
    
    public static String getExecutionDateForAckNumber() { 
    	return " SELECT executiondate as object, periodcycle as key from egwtr_waterconnection where acknowledgmentnumber = ? and tenantid = ? " ; 
    }

    public  String getConnectionDetails(final WaterConnectionGetReq waterConnectionGetReq, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(SOURCE_QUERY);

		addPagingClause(selectQuery, preparedStatementValues, waterConnectionGetReq);
        LOGGER.debug("Query : " + selectQuery);

        return selectQuery.toString();
    }

	public String getSecondQuery(final WaterConnectionGetReq waterConnectionGetReq, final List preparedStatementValues,
			Boolean countQuery, List<Long> connectionIds) {
		final StringBuilder selectQuery;
		if (countQuery)
			selectQuery = new StringBuilder(COUNT_WITHOUT_PROP_QUERY);
		else
			selectQuery = new StringBuilder(QUERY_WITHOUT_PROP);
		addSecondQueryWhereClause(selectQuery, preparedStatementValues, waterConnectionGetReq, connectionIds);
		if (!countQuery) { 
			selectQuery.append(" order by conndetails.id desc");
			addPagingClause(selectQuery, preparedStatementValues, waterConnectionGetReq);
		}
		LOGGER.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

    @SuppressWarnings("rawtypes")
	public String getQuery(final WaterConnectionGetReq waterConnectionGetReq, final List preparedStatementValues,
			Boolean countQuery) {
		final StringBuilder selectQuery;
		if (countQuery)
			selectQuery = new StringBuilder(COUNT_WITH_PROP_QUERY);
		else
			selectQuery = new StringBuilder(SOURCE_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, waterConnectionGetReq);
		if (!countQuery) { 
			selectQuery.append(" order by connection.id desc");
			addPagingClause(selectQuery, preparedStatementValues, waterConnectionGetReq);
		}
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
            selectQuery
                    .append(" connection.legacyconsumernumber like '%" + waterConnectionGetReq.getLegacyConsumerNumber() + "%'");
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
            selectQuery.append(" connection.consumernumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getConsumerNumber());
        }

        if (null != waterConnectionGetReq.getManualConsumerNumber()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" connection.manualconsumernumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getManualConsumerNumber());
        }

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

        if ((StringUtils.isNotBlank(waterConnectionGetReq.getName())
                || StringUtils.isNotBlank(waterConnectionGetReq.getMobileNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getLocality())
                || StringUtils.isNotBlank(waterConnectionGetReq.getDoorNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getRevenueWard())
                || StringUtils.isNotBlank(waterConnectionGetReq.getAadhaarNumber()))
                && null == waterConnectionGetReq.getPropertyIdentifierList()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" connection.propertyidentifier IN ('') ");
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addSecondQueryWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final WaterConnectionGetReq waterConnectionGetReq, List<Long> connectionIds) {

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
            selectQuery
                    .append(" conndetails.legacyconsumernumber like '%" + waterConnectionGetReq.getLegacyConsumerNumber() + "%'");
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
            selectQuery.append(" conndetails.consumernumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getConsumerNumber());
        }

        if (null != waterConnectionGetReq.getManualConsumerNumber()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.manualconsumernumber = ?");
            preparedStatementValues.add(waterConnectionGetReq.getManualConsumerNumber());
        }

        if (null != waterConnectionGetReq.getLocality() && !waterConnectionGetReq.getLocality().isEmpty()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" (connloc.revenueboundary = ? OR connloc.locationboundary = ? OR connloc.adminboundary = ? )");
            preparedStatementValues.add(Integer.valueOf(waterConnectionGetReq.getLocality()));
            preparedStatementValues.add(Integer.valueOf(waterConnectionGetReq.getLocality()));
            preparedStatementValues.add(Integer.valueOf(waterConnectionGetReq.getLocality()));
        }
        
        if (StringUtils.isNotBlank(waterConnectionGetReq.getRevenueWard())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" (connloc.revenueboundary = ? OR connloc.locationboundary = ? OR connloc.adminboundary = ? )");
            preparedStatementValues.add(Integer.valueOf(waterConnectionGetReq.getRevenueWard()));
            preparedStatementValues.add(Integer.valueOf(waterConnectionGetReq.getRevenueWard()));
            preparedStatementValues.add(Integer.valueOf(waterConnectionGetReq.getRevenueWard()));
        }

        if (null != waterConnectionGetReq.getId()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.id IN " + getIdQuery(waterConnectionGetReq.getId()));
        }
        if(null != connectionIds && connectionIds.size()>0)
        {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.id IN " + getIdQuery(connectionIds));
            
        }
        
        if ((StringUtils.isNotBlank(waterConnectionGetReq.getName())
                || StringUtils.isNotBlank(waterConnectionGetReq.getMobileNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getDoorNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getAadhaarNumber()))
                && null != connectionIds && connectionIds.size() == 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.id IN (0)");
            
        }
        
      /*  if (null != waterConnectionGetReq.getUserIdList()
                && waterConnectionGetReq.getUserIdList().size() > 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.userid IN "
                    + getIdQuery(waterConnectionGetReq.getUserIdList()));
        }
        
        if ((StringUtils.isNotBlank(waterConnectionGetReq.getName())
                || StringUtils.isNotBlank(waterConnectionGetReq.getMobileNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getAadhaarNumber()))
                        && null == waterConnectionGetReq.getUserIdList()) {
                isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" conndetails.userid IN (0)" );
        }*/
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

    private static String getPropertyIdentifierQuery(final List<String> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append("'" + idList.get(0).toString() + "'");
            for (int i = 1; i < idList.size(); i++)
                query.append(",'" + idList.get(i) + "'");
        }
        return query.append(")").toString();
    }
    
	public static String insertConnectionUserQuery() {
		return "Insert into egwtr_connection_owners (id,waterconnectionid,ownerid,primaryowner,ordernumber,tenantid,"
				+ "createdby,lastmodifiedby,createdtime,lastmodifiedtime) values (nextval('seq_egwtr_connection_owners'),"
				+ ":waterconnectionid,:ownerid,:primaryowner,:ordernumber,:tenantid,:createdby,:lastmodifiedby,:createdtime,:lastmodifiedtime)";
	}
	
	public static String removeConnectionUserQuery() { 
		return "DELETE from egwtr_connection_owners where waterconnectionid = :waterconnectionid and tenantid = :tenantid " ; 
	}

public String getConnectionOwnerQuery() {
return 	"Select id,ownerid,primaryowner,waterconnectionid from egwtr_connection_owners where "
		+ "tenantid = :tenantid and ownerid in ";
}

public String getConnectionOwnerQueryWithId() {
return  "Select id,ownerid,primaryowner,waterconnectionid from egwtr_connection_owners where "
                + "tenantid = :tenantid and waterconnectionid = :waterconnectionid ";
}

public String getConnectionDocumentCreateQuery() {
 return "Insert into egwtr_connectiondocument(id,documenttype,referencenumber,connectionid,filestoreid,tenantid,createdby,createddate,lastmodifiedby,lastmodifieddate)"
                + " values(nextval('seq_egwtr_connectiondocument'),:documenttype,:referencenumber,:connectionid,:filestoreid,:tenantid,:createdby,:createddate,:lastmodifiedby,:lastmodifieddate)";     
}



public String getConnectionDocumentQuery(ConnectionDocumentGetReq connectionDocGetReq, Map<String, Object> documentValues) {
        StringBuilder searchQuery = new StringBuilder(BASE_CONNECTION_DOCUMENT_QUERY);
        addWhereClauseForDocument(connectionDocGetReq,searchQuery,documentValues);
        return searchQuery.toString();
}

private void addWhereClauseForDocument(ConnectionDocumentGetReq connectionDocGetReq, StringBuilder searchQuery, Map<String, Object> documentValues) {
    if (connectionDocGetReq.getTenantId() == null)
        return;
    searchQuery.append(" WHERE");
        boolean isAppendAndClause = false;

    if(connectionDocGetReq.getTenantId() !=null)
    {
        isAppendAndClause = true;
        searchQuery.append(" tenantid = :tenantid");
        documentValues.put("tenantid", connectionDocGetReq.getTenantId());
    }
    if(connectionDocGetReq.getDocumentType() !=null)
    {
        addAndClauseIfRequired(isAppendAndClause, searchQuery);
        searchQuery.append(" documenttype = :documenttype");
        documentValues.put("documenttype", connectionDocGetReq.getDocumentType());
    }
   
    if(connectionDocGetReq.getIds() !=null){
        addAndClauseIfRequired(isAppendAndClause, searchQuery);
        searchQuery.append(" id in (:ids)");
        documentValues.put("ids", connectionDocGetReq.getIds());
    }
    if(connectionDocGetReq.getReferenceNumber() !=null){
        addAndClauseIfRequired(isAppendAndClause, searchQuery);
        searchQuery.append(" referencenumber = :referencenumber");
        documentValues.put("referencenumber", connectionDocGetReq.getReferenceNumber());
    }
    if(connectionDocGetReq.getConnectionIds() !=null){
        addAndClauseIfRequired(isAppendAndClause, searchQuery);
        searchQuery.append(" connectionid IN (:connectionid)");
        documentValues.put("connectionid", connectionDocGetReq.getConnectionIds());
    }
        }

	public String getConnectionIdQuery() {
     return "Select id from egwtr_waterconnection where consumernumber in"
                + " (:consumernumber) and tenantid = :tenantid";

}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
        final WaterConnectionGetReq waterConnectionGetReq) {
    // handle limit(also called pageSize) here
    selectQuery.append(" LIMIT ?");
    long pageSize = Integer.parseInt(applicationProperties.wcmsSearchPageSizeDefault());
    if (waterConnectionGetReq.getPageSize() != null)
        pageSize = waterConnectionGetReq.getPageSize();
    preparedStatementValues.add(pageSize); // Set limit to pageSize

    // handle offset here
    selectQuery.append(" OFFSET ?");
    int pageNumber = 0; // Default pageNo is zero meaning first page
    if (waterConnectionGetReq.getPageNumber() != null)
        pageNumber = waterConnectionGetReq.getPageNumber() - 1;
    preparedStatementValues.add(pageNumber * pageSize); // Set offset to
    // pageNo * pageSize
}
}
