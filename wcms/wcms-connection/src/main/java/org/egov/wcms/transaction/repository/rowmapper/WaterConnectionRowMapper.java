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
package org.egov.wcms.transaction.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.Property;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class WaterConnectionRowMapper implements RowMapper<Connection> {
    @Override
    public Connection mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Connection connection = new Connection();	
        connection.setId(rs.getLong("conn_id"));
        connection.setTenantId(rs.getString("conn_tenant")); 
        connection.setConnectionType(rs.getString("conn_connType")); 
        connection.setBillingType(rs.getString("conn_billtype")); 
        connection.setConnectionStatus(rs.getString("conn_constatus"));  
        connection.setApplicationType(rs.getString("conn_applntype"));
        connection.setSumpCapacity(rs.getLong("conn_sumpcap")); 
        connection.setDonationCharge(rs.getLong("conn_doncharge"));  
        connection.setNumberOfTaps(rs.getInt("conn_nooftaps")); 
        connection.setSupplyTypeId(rs.getString("supplytype_id")); 
        connection.setSupplyType(rs.getString("supplytype_name"));
        connection.setCategoryId(rs.getString("category_id")); 
        connection.setCategoryType(rs.getString("category_name"));  
        connection.setHscPipeSizeType(rs.getString("pipesize_sizeinmilimeter")); 
        connection.setPipesizeId(rs.getString("pipesize_id")); 
        connection.setSourceTypeId(rs.getString("watersource_id")); 
        connection.setSourceType(rs.getString("watersource_name"));
        connection.setNumberOfPersons(rs.getInt("conn_noofperson")); 
        connection.setStateId(rs.getLong("conn_stateid")); 
        connection.setParentConnectionId(rs.getLong("conn_parentconnectionid"));
        connection.setWaterTreatmentId(rs.getString("conn_watertreatmentid"));
        connection.setWaterTreatment((null!=rs.getString("watertreatmentname") && rs.getString("watertreatmentname")!="")? rs.getString("watertreatmentname")  : "" );
        connection.setLegacyConsumerNumber(rs.getString("conn_legacyconsumernumber"));
        connection.setIsLegacy(rs.getBoolean("conn_islegacy")); 
        connection.setAcknowledgementNumber(rs.getString("conn_acknumber"));  
        connection.setConsumerNumber(rs.getString("conn_consumerNum")); 
        connection.setPropertyIdentifier(rs.getString("conn_propid"));  
        connection.setCreatedDate(rs.getString("createdtime"));
        Property prop = new Property();
        prop.setUsageTypeId(rs.getString("conn_usgtype"));
        prop.setPropertyTypeId(rs.getString("conn_proptype"));
        prop.setAddress(rs.getString("conn_propaddress"));  
        prop.setPropertyidentifier(rs.getString("conn_propid"));
        if(null != rs.getString("propertyowner") && rs.getString("propertyowner")!= ""){
        	prop.setNameOfApplicant(rs.getString("propertyowner"));
        }
        connection.setProperty(prop);
        return connection;
    }
}
