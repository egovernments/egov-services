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
package org.egov.wcms.transaction.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.ConnectionOwner;
import org.egov.wcms.transaction.model.DocumentOwner;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.Meter;
import org.egov.wcms.transaction.model.Property;
import org.egov.wcms.transaction.model.User;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.repository.builder.WaterConnectionQueryBuilder;
import org.egov.wcms.transaction.repository.rowmapper.ConnectionDocumentRowMapper;
import org.egov.wcms.transaction.repository.rowmapper.WaterConnectionRowMapper;
import org.egov.wcms.transaction.repository.rowmapper.WaterConnectionRowMapper.ConnectionMeterRowMapper;
import org.egov.wcms.transaction.util.ConnectionMasterAdapter;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.Address;
import org.egov.wcms.transaction.web.contract.PropertyInfo;
import org.egov.wcms.transaction.web.contract.PropertyOwnerInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.UserResponseInfo;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class WaterConnectionSearchRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionSearchRepository.class);
    public static final String roleCode = "CITIZEN"; 

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WaterConnectionQueryBuilder waterConnectionQueryBuilder;
    
    @Autowired
    private RestConnectionService restConnectionService; 



    public List<Connection> getConnectionDetails(final WaterConnectionGetReq waterConnectionGetReq, RequestInfo requestInfo, 
            List<PropertyInfo> propertyInfoList, List<User> userList) {
    final List<Object> preparedStatementValues = new ArrayList<>();
    final String fetchQuery = waterConnectionQueryBuilder.getQuery(waterConnectionGetReq, preparedStatementValues);
    LOGGER.info("Get Connection Details Query : " + fetchQuery);
    final List<Connection> connectionList = jdbcTemplate.query(fetchQuery, preparedStatementValues.toArray(),
                    new WaterConnectionRowMapper().new WaterConnectionPropertyRowMapper());
    LOGGER.info(connectionList.size() + " Connection Objects fetched from DB");
    addPropertyDetails(propertyInfoList, connectionList);
    if (connectionList.size() == 1 && propertyInfoList.size() == 0) {
            resolvePropertyIdentifierDetails(connectionList, requestInfo);
    }
    final List<Object> secondPreparedStatementValues = new ArrayList<>();
    final String secondFetchQuery = waterConnectionQueryBuilder.getSecondQuery(waterConnectionGetReq,
                    secondPreparedStatementValues);
    
    LOGGER.info("Get Connection Details Query for Without Property Cases : " + secondFetchQuery);
    try {
            final List<Connection> secondConnectionList = jdbcTemplate.query(secondFetchQuery,
                            secondPreparedStatementValues.toArray(),
                            new WaterConnectionRowMapper().new WaterConnectionWithoutPropertyRowMapper());
            LOGGER.info(secondConnectionList.size() + " Connection Objects fetched from DB");
            if (secondConnectionList.size() > 0) {
                    if(secondConnectionList.size() == 1)  { 
                            resolveUserDetails(secondConnectionList, requestInfo);
                    }
                    connectionList.addAll(secondConnectionList);
            }
    } catch (Exception ex) {
            LOGGER.error("Exception encountered while fetching the Connection list without Property : " + ex);
    }
    resolveMasterDetails(connectionList, requestInfo);
    // This condition is added to fetch the Meter Details only in single view case. Not in fetch all case 
    if(connectionList.size() == 1) { 
            getConnectionMeterDetails(connectionList);
    }
    return connectionList;
}
	
	private void resolveUserDetails(List<Connection> secondConnectionList, RequestInfo rInfo) {
		String searchUrl = restConnectionService.getUserServiceSearchPath();
		UserResponseInfo userResponse = null;
		Map<String, Object> userSearchRequestInfo = new HashMap<String, Object>();
		List<Long> userIds = new ArrayList<>();
		for (Connection conn : secondConnectionList) {
			Boolean isPrimary = conn.getConnectionOwner().getIsPrimaryOwner();
			Boolean isSecondary = conn.getConnectionOwner().getIsSecondaryOwner();
			userIds.add(conn.getConnectionOwner().getId());
			userSearchRequestInfo.put("tenantId", conn.getTenantId());
			userSearchRequestInfo.put("type", roleCode);
			userSearchRequestInfo.put("id", userIds);
			userSearchRequestInfo.put("RequestInfo", rInfo);
			LOGGER.info("User Service Search URL :: " + searchUrl + " \n userSearchRequestInfo  :: "
					+ userSearchRequestInfo);
			userResponse = new RestTemplate().postForObject(searchUrl.toString(), userSearchRequestInfo,
					UserResponseInfo.class);
			LOGGER.info("User Service Search Response :: " + userResponse);
			ConnectionOwner connOwner = null;
			Address address = null;
			if (null != userResponse && null != userResponse.getUser() && userResponse.getUser().size() > 0) {
				List<User> userList = userResponse.getUser();
				for (User eachUser : userList) {
					connOwner = new ConnectionOwner();
					connOwner.setIsPrimaryOwner(isPrimary);
					connOwner.setIsSecondaryOwner(isSecondary);
					connOwner.setName(eachUser.getName());
					connOwner.setPermanentAddress(eachUser.getPermanentAddress());
					connOwner.setUserName(eachUser.getUserName());
					connOwner.setEmailId(eachUser.getEmailId());
					connOwner.setMobileNumber(eachUser.getMobileNumber());
					connOwner.setAadhaarNumber(eachUser.getAadhaarNumber());
					connOwner.setGender(eachUser.getGender());
					address = new Address(); 
					address.setAddressLine1(eachUser.getPermanentAddress());
					address.setCity(eachUser.getPermanentCity());
                                        address.setPinCode(eachUser.getPermanentPinCode());
				}
			}
			if (null != connOwner && null != address) {
				conn.setConnectionOwner(connOwner);
				conn.setAddress(address);
			}
		}
	}
	
	private void resolvePropertyIdentifierDetails(List<Connection> connectionList, RequestInfo rInfo) {
		WaterConnectionReq wR = new WaterConnectionReq();
		wR.setRequestInfo(rInfo);
		PropertyResponse propResp = new PropertyResponse(); 
		for(Connection conn : connectionList) { 
			wR.setConnection(conn);
			propResp = restConnectionService.getPropertyDetailsByUpicNo(wR);
		}
		if(null != propResp.getProperties() && propResp.getProperties().size() > 0) { 
			addPropertyDetails(propResp.getProperties(), connectionList) ; 
		}
	}
	
	private void resolveMasterDetails(List<Connection> connectionList, RequestInfo requestInfo) {
		LOGGER.info("Resolving the names for the IDs");
		for(Connection conn : connectionList) { 
			if(StringUtils.isNotBlank(conn.getPipesizeId())) { 
				conn.setHscPipeSizeType(ConnectionMasterAdapter.getPipeSizeById(conn.getPipesizeId(), conn.getTenantId(), requestInfo));
			} 
			if(StringUtils.isNotBlank(conn.getSupplyTypeId())) { 
				conn.setSupplyType(ConnectionMasterAdapter.getSupplyTypeById(conn.getSupplyTypeId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getSourceTypeId())) { 
				conn.setSourceType(ConnectionMasterAdapter.getSourceTypeById(conn.getSourceTypeId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getWaterTreatmentId())) { 
				conn.setWaterTreatment(ConnectionMasterAdapter.getTreatmentPlantById(conn.getWaterTreatmentId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getStorageReservoirId())) { 
				conn.setStorageReservoir(ConnectionMasterAdapter.getStorageReservoiById(conn.getStorageReservoirId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getUsageTypeId())) { 
				conn.setUsageType(ConnectionMasterAdapter.getUsageTypeById(conn.getUsageTypeId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getSubUsageTypeId())) { 
				conn.setSubUsageType(ConnectionMasterAdapter.getSubUsageTypeById(conn.getSubUsageTypeId(), conn.getTenantId(), requestInfo));
			}
		}
	}
	
	private void addPropertyDetails(List<PropertyInfo> propertyInfoList, List<Connection> connectionWithProperty) {
		LOGGER.info("Adding Property Details Connection List");
		for(Connection conn : connectionWithProperty) { 
			if(null != propertyInfoList) { 
				for(PropertyInfo pInfo : propertyInfoList) { 
					if(StringUtils.isNotBlank(conn.getPropertyIdentifier()) && StringUtils.isNotBlank(pInfo.getUpicNumber()) && 
							conn.getPropertyIdentifier().equals(pInfo.getUpicNumber())) {
						conn.setHouseNumber(null != pInfo.getAddress() ? pInfo.getAddress().getAddressNumber() : "" );
						Property prop = new Property(); 
						prop.setPropertyidentifier(pInfo.getUpicNumber());
						if(null != pInfo.getBoundary() && null != pInfo.getBoundary().getLocationBoundary()){
							prop.setLocality(pInfo.getBoundary().getLocationBoundary().getId());
						}
						if(null != pInfo.getBoundary() && null != pInfo.getBoundary().getRevenueBoundary()){ 
							prop.setZone(pInfo.getBoundary().getRevenueBoundary().getId());
						}
						for(PropertyOwnerInfo owner : pInfo.getOwners()) { 
							prop.setNameOfApplicant(owner.getName());
							prop.setAddress(owner.getPermanentAddress());
							prop.setAdharNumber(owner.getAadhaarNumber());
							prop.setEmail(owner.getEmailId());
							prop.setMobileNumber(owner.getMobileNumber());
						}
						conn.setProperty(prop);
					}
				}
			}
		}
	}
	
	private void getConnectionMeterDetails(List<Connection> connectionList) {
		String meterDetailsQuery = WaterConnectionQueryBuilder.getConnectionMeterQueryForSearch();
		for(Connection conn : connectionList) { 
			final List<Object> preparedStatementValues = new ArrayList<>();
			preparedStatementValues.add(conn.getId());
			LOGGER.info("Get Meter Details Query : " + meterDetailsQuery);
			ConnectionMeterRowMapper mapper = new WaterConnectionRowMapper().new ConnectionMeterRowMapper(); 
			jdbcTemplate.query(meterDetailsQuery, preparedStatementValues.toArray(),
					mapper);
			sortMeterDetailsToConnection(conn, mapper);
		}
	}
	
	private void sortMeterDetailsToConnection(Connection conn, ConnectionMeterRowMapper mapper) { 
		Map<Long, Map<Long, Meter>> meterReadingMap = mapper.meterReadingMap; 
		Iterator<Entry<Long, Map<Long, Meter>>> itr = meterReadingMap.entrySet().iterator(); 
		while(itr.hasNext()) { 
			Entry<Long, Map<Long, Meter>> entry = itr.next();
			long connectionId = entry.getKey();
			if(conn.getId() == connectionId) { 
				Map<Long, Meter> innerMap = entry.getValue();
				Iterator<Entry<Long, Meter>> innerItr = innerMap.entrySet().iterator();
				List<Meter> meterList = new ArrayList<>(); 
				while(innerItr.hasNext()) { 
					Entry<Long, Meter> innerEntry = innerItr.next();
					Meter meter = innerEntry.getValue();
					meterList.add(meter); 
				}
				conn.setMeter(meterList);
			}
		}
	}
	

  

    public Map<String, Object> getObjectForInsertEstimationNotice(final EstimationNotice estimationNotice,
            final long connectionId,
            final String tenantId) {
        final Long createdBy = 1L;
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("waterConnectionId", connectionId);
        parameters.put("tenantId", tenantId);
        parameters.put("dateOfLetter", estimationNotice.getDateOfLetter());
        parameters.put("letterNumber", estimationNotice.getLetterNumber());
        parameters.put("letterTo", estimationNotice.getLetterTo());
        parameters.put("letterIntimationSubject", estimationNotice.getLetterIntimationSubject());
        parameters.put("applicationNumber", estimationNotice.getApplicationNumber());
        parameters.put("applicationDate", estimationNotice.getApplicationDate());
        parameters.put("applicantName", estimationNotice.getApplicantName());
        parameters.put("serviceName", "New water tap connection");
        parameters.put("waterNumber", estimationNotice.getApplicationNumber());
        parameters.put("slaDays", estimationNotice.getSlaDays());
        parameters.put("chargeDescription1", estimationNotice.getChargeDescription().get(0));
        parameters.put("chargeDescription2", estimationNotice.getChargeDescription().get(1));
        parameters.put("createdBy", createdBy);
        parameters.put("createdDate", new Date(new java.util.Date().getTime()).getTime());
        return parameters;
    }

    public Map<String, Object> getObjectForInsertWorkOrder(final WorkOrderFormat workOrder) {
        final Long createdBy = 1L;
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("waterConnectionId", workOrder.getConnectionId());
        parameters.put("tenantId", workOrder.getTenantId());
        parameters.put("workOrderNumber", workOrder.getWorkOrderNumber());
        parameters.put("workOrderDate", workOrder.getWorkOrderDate());
        parameters.put("waterTapOwnerName", workOrder.getWaterTapOwnerName());
        parameters.put("ackNumber", workOrder.getAckNumber());
        parameters.put("ackNumberDate", workOrder.getAckNumberDate());
        parameters.put("hscNumber", workOrder.getHscNumber());
        parameters.put("hscNumberDate", workOrder.getHscNumberDate());
        parameters.put("serviceName", "New water tap connection");
        // TODO
        parameters.put("plumberName", "To be added");
        //
        parameters.put("createdBy", createdBy);
        parameters.put("createdDate", new Date(new java.util.Date().getTime()).getTime());
        return parameters;
    }

    public Map<String, Object> getObjectForConnectionAddress(final WaterConnectionReq waterConnectionRequest) {
        final Connection conn = waterConnectionRequest.getConnection();
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("tenantid", conn.getId());
        parameters.put("latitude", conn.getAddress().getLatitude());
        parameters.put("longitude", conn.getAddress().getLongitude());
        parameters.put("addressId", conn.getAddress().getAddressId());
        parameters.put("addressNumber", conn.getAddress().getAddressNumber());
        parameters.put("addressLine1", conn.getAddress().getAddressLine1());
        parameters.put("addressLine2", conn.getAddress().getAddressLine2());
        parameters.put("landmark", conn.getAddress().getLandMark());
        parameters.put("doorno", conn.getAddress().getDoorNo());
        parameters.put("city", conn.getAddress().getCity());
        parameters.put("pincode", conn.getAddress().getPinCode());
        parameters.put("detail", conn.getAddress().getDetail());
        parameters.put("route", conn.getAddress().getRoute());
        parameters.put("street", conn.getAddress().getStreet());
        parameters.put("area", conn.getAddress().getArea());
        parameters.put("roadname", conn.getAddress().getRoadName());
        parameters.put("createdby", waterConnectionRequest.getRequestInfo().getUserInfo().getId());
        parameters.put("createdtime", new Date(new java.util.Date().getTime()).getTime());
        return parameters;
    }

    public List<DocumentOwner> getDocumentForConnection(final Connection connection) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String fetchQuery = WaterConnectionQueryBuilder.getDocumentForConnection();
        LOGGER.info("Get Document for Connection Query : " + fetchQuery);
        List<DocumentOwner> documentList = new ArrayList<>();
        preparedStatementValues.add(connection.getId());
        preparedStatementValues.add(connection.getTenantId());
        try { 
        	documentList = jdbcTemplate.query(fetchQuery, preparedStatementValues.toArray(),new ConnectionDocumentRowMapper());
        } catch(Exception ex) { 
        	LOGGER.error("Exception encountered while fetching documents : " +ex);
        }
                
        return documentList;
    }
}
