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

package org.egov.wcms.repository.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WaterConnectionQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(WaterConnectionQueryBuilder.class);

    public static String insertDocumentQuery() {
        return "INSERT INTO egwtr_documentowner(document,name,filestoreid,connectionid,tenantid) values "
                + "(?,?,?,?,?)";
    }

    public static String insertMeterReadingQuery() {
        return "INSERT INTO egwtr_meterreading(connectionid,reading,tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime) values "
                + "(?,?,?,?,?,?,?)";
    }

    public static String insertMeterQuery() {

        return "INSERT INTO egwtr_meter(metermake,connectionid,meterreading,tenantid,createdby,createdtime,lastmodifiedby,lastmodifiedtime) values(?,?,?,?,?,?,?,?)";
    }

    public static String insertConnectionQuery() {

        return "INSERT INTO egwtr_waterconnection (tenantid, connectiontype, billingtype, categorytype, hscpipesizetype, supplytype, "
                + "sourcetype, connectionstatus, sumpcapacity, numberofftaps, numberofpersons, acknowledgmentnumber, createdby, "
                + "lastmodifiedby, createdtime, lastmodifiedtime, propertyid, usagetype, propertytype, propertyaddress, donationcharge) values"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String insertLegacyConnectionQuery() {
        return "INSERT INTO egwtr_waterconnection(tenantid, connectiontype, billingtype, categorytype, hscpipesizetype, supplytype, "
                + "sourcetype, connectionstatus, sumpcapacity, numberofftaps, numberofpersons, acknowledgmentnumber, createdby, "
                + "lastmodifiedby, createdtime, lastmodifiedtime, propertyid, usagetype, propertytype, propertyaddress,donationcharge,"
                + "legacyconsumernumber,consumernumber) values"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String insertAdditionalConnectionQuery() {

        return "INSERT INTO egwtr_waterconnection(tenantid, connectiontype, billingtype, categorytype, hscpipesizetype, supplytype, "
                + "sourcetype, connectionstatus, sumpcapacity, numberofftaps, numberofpersons, acknowledgmentnumber, createdby, "
                + "lastmodifiedby, createdtime, lastmodifiedtime, propertyid, usagetype, propertytype, propertyaddress,donationcharge,"
                + "legacyconsumernumber,consumernumber,parentconnectionid) values"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
}
