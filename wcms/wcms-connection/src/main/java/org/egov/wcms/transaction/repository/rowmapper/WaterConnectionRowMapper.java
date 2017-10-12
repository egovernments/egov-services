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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.ConnectionDocument;
import org.egov.wcms.transaction.model.ConnectionOwner;
import org.egov.wcms.transaction.model.Meter;
import org.egov.wcms.transaction.model.MeterReading;
import org.egov.wcms.transaction.model.Property;
import org.egov.wcms.transaction.web.contract.Boundary;
import org.egov.wcms.transaction.web.contract.ConnectionLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class WaterConnectionRowMapper {

	public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionRowMapper.class);
	public static final String METERED = "METERED";
	
	public class ConnectionMeterRowMapper implements RowMapper<Meter> {
		public Map<Long, Map<Long, Meter>> meterReadingMap = new HashMap<>();
		@Override
		public Meter mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			if(meterReadingMap.containsKey(rs.getLong("connectionid"))) { 
				Map<Long, Meter> innerMap = meterReadingMap.get(rs.getLong("connectionid")); 
				if(innerMap.containsKey(rs.getLong("meterid"))) { 
					Meter meter= innerMap.get(rs.getLong("meterid"));
					List<MeterReading> meterReadingList = meter.getMeterReadings(); 
					MeterReading reading = prepareMeterReadingObject(rs);
					meterReadingList.add(reading);
				} else { 
					Meter meter = prepareMeterObject(rs);
					innerMap.put(rs.getLong("meterid"), meter); 
				}
			} else { 
				Meter meter = prepareMeterObject(rs);
				Map<Long, Meter> innerMap = new HashMap<>();
				innerMap.put(rs.getLong("meterid"), meter);
				meterReadingMap.put(rs.getLong("connectionid"), innerMap);
			}
			return null; 
		}
		
		private Meter prepareMeterObject(ResultSet rs) {
			Meter meter = new Meter();
			try {
				meter.setId(rs.getLong("meterid"));
				meter.setMeterMake(rs.getString("metermake"));
				meter.setInitialMeterReading(rs.getString("initialmeterreading"));
				meter.setMeterSlNo(rs.getString("meterslno"));
				meter.setMeterCost(rs.getString("metercost"));
				meter.setMeterModel(rs.getString("metermodel"));
				meter.setMaximumMeterReading(rs.getString("maximummeterreading"));
				meter.setMeterStatus(rs.getString("meterstatus"));
				meter.setTenantId(rs.getString("metertenant")); 
				meter.setMeterOwner(rs.getString("meterowner"));
				MeterReading reading = prepareMeterReadingObject(rs);
				List<MeterReading> meterReadingList = new ArrayList<>(); 
				meterReadingList.add(reading);
				meter.setMeterReadings(meterReadingList);
			} catch (Exception e) {
				LOGGER.error("Encountered Exception while creating Meter Object : " + e.getMessage());
			}
			return meter;
		}
		
		private MeterReading prepareMeterReadingObject(ResultSet rs) {
			MeterReading reading = new MeterReading();
			try {
				reading.setMeterId(rs.getLong("meterid"));
				reading.setReading(rs.getLong("reading"));
				reading.setReadingDate(rs.getLong("readingdate"));
				reading.setGapCode(rs.getString("gapcode"));
				if(StringUtils.isNotBlank(rs.getString("consumption"))) { 
					reading.setConsumption(Long.parseLong(rs.getString("consumption")));
				}
				if(StringUtils.isNotBlank(rs.getString("consumptionadjusted"))) { 
					reading.setConsumptionAdjusted(Long.parseLong(rs.getString("consumptionadjusted")));
				}
				if(StringUtils.isNotBlank(rs.getString("numberofdays"))) { 
					reading.setNumberOfDays(Long.parseLong(rs.getString("numberofdays")));
				}
				reading.setResetFlag(rs.getBoolean("resetflag"));
			} catch (Exception e) {
				LOGGER.error("Encountered Exception while creating Meter Reading Object : " + e.getMessage());
			}
			return reading; 
		}
	}
	
	public class WaterConnectionPropertyRowMapper implements RowMapper<Connection> {
		@Override
		public Connection mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final Connection connection = prepareConnectionObject(rs);
			Property prop = new Property();
			prop.setPropertyIdentifier(rs.getString("conn_propid"));
			ConnectionLocation connLoc = ConnectionLocation.builder()
					.buildingName(StringUtils.isNotBlank(rs.getString("buildingname")) ? rs.getString("buildingname") : "")
					.billingAddress(StringUtils.isNotBlank(rs.getString("billingaddress")) ? rs.getString("billingaddress") : "")
					.roadName(StringUtils.isNotBlank(rs.getString("roadname")) ? rs.getString("roadname") : "")
					.gisNumber(StringUtils.isNotBlank(rs.getString("gisnumber")) ? rs.getString("gisnumber") : "")
					.build();
			connection.setConnectionLocation(connLoc);
			connection.setProperty(prop);
			connection.setWithProperty(true);
			return connection;
		}
	}
	

    public class WaterConnectionDocumentRowMapper implements RowMapper<ConnectionDocument> {
             @Override
              public ConnectionDocument mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                      ConnectionDocument document = new ConnectionDocument();
                     document.setId(rs.getLong("id"));
                     document.setDocumentType(rs.getString("documenttype"));
                     document.setReferenceNumber(rs.getString("referencenumber"));
                     document.setConnectionId(rs.getLong("connectionid"));
                     document.setFileStoreId(rs.getString("filestoreid"));
                     document.setTenantId(rs.getString("tenantid"));
                     document.setCreatedBy(rs.getLong("createdby"));
                     document.setCreatedDate(rs.getLong("createddate"));
                     document.setLastModifiedBy(rs.getLong("lastmodifiedby"));
                     document.setLastModifiedDate(rs.getLong("lastmodifieddate"));
                     return document;
               }
       }
	
	public class WaterConnectionWithoutPropertyRowMapper implements RowMapper<Connection> {
		@Override
		public Connection mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final Connection connection = prepareConnectionObject(rs);
			Property prop = new Property();
			prop.setPropertyIdentifier(rs.getString("conn_propid"));
			connection.setProperty(prop);
		/*	connection.getConnectionOwner().setIsPrimaryOwner(rs.getBoolean("isprimaryowner"));
			if (rs.getBoolean("isprimaryowner")) {
				connection.getConnectionOwner().setIsSecondaryOwner(Boolean.FALSE);
			} else {
				connection.getConnectionOwner().setIsSecondaryOwner(Boolean.TRUE);
			}*/
			/*Address addr = new Address();
			addr.setCity(rs.getString("city"));
			addr.setPinCode(rs.getString("pincode"));
			addr.setAddressLine1(rs.getString("addressline1"));
			connection.setAddress(addr);*/
			connection.setWithProperty(false);
			ConnectionLocation connLoc = ConnectionLocation.builder()
					.revenueBoundary(new Boundary(rs.getLong("revenueboundary"), String.valueOf(rs.getLong("revenueboundary")),null))
					.locationBoundary(new Boundary(rs.getLong("locationboundary"), String.valueOf(rs.getLong("locationboundary")),null))
					.adminBoundary(new Boundary(rs.getLong("adminboundary"),String.valueOf(rs.getLong("adminboundary")) ,null))
					.buildingName(StringUtils.isNotBlank(rs.getString("buildingname")) ? rs.getString("buildingname") : "")
					.billingAddress(StringUtils.isNotBlank(rs.getString("billingaddress")) ? rs.getString("billingaddress") : "")
					.roadName(StringUtils.isNotBlank(rs.getString("roadname")) ? rs.getString("roadname") : "")
					.gisNumber(StringUtils.isNotBlank(rs.getString("gisnumber")) ? rs.getString("gisnumber") : "")
					.build();
			connection.setConnectionLocation(connLoc);
			return connection;
		}
		
	}
	
	public class WaterConnectionWithoutPropertyOwnerRowMapper implements RowMapper<ConnectionOwner> 
	{
	    public List<Long> connectionIdList = new ArrayList<>();
		@Override
		public ConnectionOwner mapRow(final ResultSet rs, final int rowNum) throws SQLException  {
		ConnectionOwner connectionOwner = new ConnectionOwner ();
		connectionOwner.setId(rs.getLong("id"));
		connectionOwner.setOwnerid(rs.getLong("ownerid"));
		connectionOwner.setWaterConnectionId(rs.getLong("waterconnectionid"));
		
		connectionOwner.setPrimaryOwner(rs.getBoolean("primaryowner"));
		connectionIdList.add(rs.getLong("waterconnectionid"));
		return connectionOwner;
		}	
	}

	private Connection prepareConnectionObject(ResultSet rs) {
		Connection connection = new Connection();
		try {
			connection.setId(rs.getLong("conn_id"));
			connection.setTenantId(rs.getString("conn_tenant"));
			connection.setStorageReservoirId(rs.getString("conn_storagereservoir"));
			connection.setUsageTypeId(rs.getString("conn_usgtype"));
			connection.setSubUsageTypeId(rs.getString("conn_subusagetype"));
			connection.setSupplyTypeId(rs.getString("conn_suply"));
			connection.setPipesizeId(rs.getString("conn_pipesize"));
			connection.setSourceTypeId(rs.getString("conn_sourceType"));
			connection.setWaterTreatmentId(rs.getString("conn_watertreatmentid"));
			connection.setConnectionType(rs.getString("conn_connType"));
			connection.setBillingType(rs.getString("conn_billtype"));
			connection.setConnectionStatus(rs.getString("conn_constatus"));
			connection.setApplicationType(rs.getString("conn_applntype"));
			connection.setSumpCapacity(rs.getLong("conn_sumpcap"));
			connection.setDonationCharge(rs.getLong("conn_doncharge"));
			connection.setStatus(rs.getString("status"));
			connection.setNumberOfTaps(rs.getInt("conn_nooftaps"));
			connection.setNumberOfFamily(rs.getInt("numberoffamily"));
			connection.setNumberOfPersons(rs.getInt("conn_noofperson"));
			connection.setStateId(rs.getLong("conn_stateid"));
			connection.setParentConnectionId(rs.getLong("conn_parentconnectionid"));
			connection.setHouseNumber(rs.getString("housenumber"));
			connection.setLegacyConsumerNumber(rs.getString("conn_legacyconsumernumber"));
			connection.setManualConsumerNumber(rs.getString("manualconsumernumber"));
			connection.setIsLegacy(rs.getBoolean("conn_islegacy"));
			connection.setAcknowledgementNumber(rs.getString("conn_acknumber"));
			connection.setConsumerNumber(rs.getString("conn_consumerNum"));
			connection.setPropertyIdentifier(rs.getString("conn_propid"));
			connection.setCreatedDate(rs.getString("createdtime"));
			connection.setPlumberName(rs.getString("plumbername"));
			connection.setManualReceiptNumber(rs.getString("manualreceiptnumber"));
			connection.setManualReceiptDate(rs.getLong("manualreceiptdate"));
			if(rs.getDouble("sequencenumber") > 0) {
				DecimalFormat df = new DecimalFormat("####0.0000");
				df.format(rs.getDouble("sequencenumber"));
				connection.setBillSequenceNumber(Double.parseDouble(df.format(rs.getDouble("sequencenumber"))));
			}
			connection.setOutsideULB(rs.getBoolean("outsideulb"));
			//connection.setMeterOwner(rs.getString("meterowner"));
			//connection.setMeterModel(rs.getString("metermodel"));
			Long execDate = rs.getLong("execdate");
			if (null != execDate) {
				connection.setExecutionDate(execDate);
			}
			/*if(rs.getString("conn_billtype").equals(METERED) && rs.getBoolean("conn_islegacy")) { 
				Meter meter = Meter.builder().meterMake(rs.getString("metermake"))
						.meterCost(rs.getString("metercost"))
						.meterSlNo(rs.getString("meterslno"))
						.initialMeterReading(rs.getString("initialmeterreading"))
						.build();
				List<Meter> meterList = new ArrayList<>();
				meterList.add(meter);
				connection.setMeter(meterList);
			}*/
		} catch (Exception ex) {
			LOGGER.error("Exception encountered while mapping the Result Set in Mapper : " + ex);
		}
		return connection;
	}
	
	
}
