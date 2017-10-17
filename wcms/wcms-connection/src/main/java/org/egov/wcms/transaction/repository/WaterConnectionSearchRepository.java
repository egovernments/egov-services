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

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import org.egov.wcms.transaction.repository.rowmapper.WaterConnectionRowMapper.WaterConnectionWithoutPropertyOwnerRowMapper;
import org.egov.wcms.transaction.utils.ConnectionMasterAdapter;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.Address;
import org.egov.wcms.transaction.web.contract.ConsolidatedTax;
import org.egov.wcms.transaction.web.contract.DemandDueResponse;
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class WaterConnectionSearchRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionSearchRepository.class);
	public static final String roleCode = "CITIZEN";
	public static final int FIRST_RECORD = 0; 

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private WaterConnectionQueryBuilder waterConnectionQueryBuilder;

	@Autowired
	private RestConnectionService restConnectionService;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Connection> getConnectionDetails(final WaterConnectionGetReq waterConnectionGetReq,
            RequestInfo requestInfo, List<PropertyInfo> propertyInfoList, List<User> userList) {
        Long totalCount = null;
        final List<Object> preparedStatementValues = new ArrayList<>();
        final List<Object> preparedCountStatementValues = new ArrayList<>();
        final List<Object> preparedCountForWithoutPropertyStatementValues = new ArrayList<>();

		// Search process for with property cases
        final String countQuery = waterConnectionQueryBuilder.getQuery(waterConnectionGetReq, preparedCountStatementValues,true);
        final String fetchQuery = waterConnectionQueryBuilder.getQuery(waterConnectionGetReq, preparedStatementValues,false);
        LOGGER.info("Get Connection Details Query : " + fetchQuery);
        final List<Connection> connectionList = jdbcTemplate.query(fetchQuery, preparedStatementValues.toArray(),
                new WaterConnectionRowMapper().new WaterConnectionPropertyRowMapper());
        Long countForWithProperty = jdbcTemplate.queryForObject(countQuery,preparedCountStatementValues.toArray(), Long.class);
        LOGGER.info(connectionList.size() + " Connection Objects fetched from DB for with property cases");
        
        // Adding already fetched property information
        addPropertyDetails(propertyInfoList, connectionList);
        if (connectionList.size() == 1 && propertyInfoList.size() == 0) {
        	// Fetching and  adding property information
            resolvePropertyIdentifierDetails(connectionList, requestInfo);
        } else if (connectionList.size() > 1 && propertyInfoList.size() == 0) {
            List<String> propertyIdentifierList = new ArrayList<>();
            for (Connection connection : connectionList) {
                propertyIdentifierList.add(connection.getProperty().getPropertyIdentifier());
            }
            addProperty(propertyIdentifierList,connectionList,requestInfo,connectionList.get(FIRST_RECORD).getTenantId());
        }
        
        // Search process for without property cases
        final List<Object> secondPreparedStatementValues = new ArrayList<>();
        List<Long> connectionIds = null;

        if(userList.size() >0)
            connectionIds = resolveUserDetailsWhenDetailsIsProvided(waterConnectionGetReq,requestInfo,userList);

        final String secondCountQuery = waterConnectionQueryBuilder.getSecondQuery(waterConnectionGetReq, preparedCountForWithoutPropertyStatementValues, true,connectionIds);
        Long countForWithOutProperty = jdbcTemplate.queryForObject(secondCountQuery,preparedCountForWithoutPropertyStatementValues.toArray(), Long.class);
        totalCount = countForWithProperty + countForWithOutProperty;
       
        final String secondFetchQuery = waterConnectionQueryBuilder.getSecondQuery(waterConnectionGetReq,
                secondPreparedStatementValues,false,connectionIds);
     
        LOGGER.info("Get Connection Details Query for Without Property Cases : " + secondFetchQuery);
        try {
            final List<Connection> secondConnectionList = jdbcTemplate.query(secondFetchQuery,
                    secondPreparedStatementValues.toArray(),
                    new WaterConnectionRowMapper().new WaterConnectionWithoutPropertyRowMapper());
            LOGGER.info(secondConnectionList.size() + " Connection Objects fetched from DB");

            if (secondConnectionList.size() > 0){
             
                    resolveUserDetails(secondConnectionList, requestInfo);
                connectionList.addAll(secondConnectionList);
            }
        }
         catch (Exception ex) {
            LOGGER.error("Exception encountered while fetching the Connection list without Property : " + ex);
        }
        if(connectionList !=null && connectionList.size()>0)
            connectionList.get(FIRST_RECORD).setTotalCount(totalCount);
        resolveMasterDetails(connectionList, requestInfo);
        // This condition is added to fetch the Meter Details only in single
        // view case. Not in fetch all case
        if (connectionList.size() == 1) {
            getConnectionMeterDetails(connectionList);
        }
        return connectionList;
    }

    private List<Long> resolveUserDetailsWhenDetailsIsProvided(WaterConnectionGetReq waterConnectionGetReq, RequestInfo requestInfo, List<User> userList) {
        Map<String, Object> preparedStatementValuesForOwner = new HashMap<>();
        StringBuilder connectionOwnerQuery = new StringBuilder(waterConnectionQueryBuilder.getConnectionOwnerQuery());
        connectionOwnerQuery.append(" ("); 
        if (userList.size() >= 1) {
            connectionOwnerQuery.append("'" + userList.get(FIRST_RECORD).getId() + "'");
            for (int i = 1; i < userList.size(); i++)
                connectionOwnerQuery.append(",'" + userList.get(i).getId() + "'");
            connectionOwnerQuery.append(")"); 
        }
            
        
            preparedStatementValuesForOwner.put("tenantid", waterConnectionGetReq.getTenantId());
            WaterConnectionWithoutPropertyOwnerRowMapper mapper = new WaterConnectionRowMapper().new WaterConnectionWithoutPropertyOwnerRowMapper();
            List<ConnectionOwner> connectionOwners = namedParameterJdbcTemplate.query(connectionOwnerQuery.toString(),
                    preparedStatementValuesForOwner,mapper);
            
            return mapper.connectionIdList ; 
     
    }

    private void addProperty(List<String> propertyIdentifierList, List<Connection> connectionList, RequestInfo requestInfo,
            String tenantId) {
        for(String propertyIdentifier : propertyIdentifierList){
        WaterConnectionReq waterConnectionReq = new WaterConnectionReq();
        waterConnectionReq.setRequestInfo(requestInfo);
       
        Connection connection = new Connection();
        connection.setTenantId(tenantId);
        connection.setPropertyIdentifier(propertyIdentifier);
        waterConnectionReq.setConnection(connection);
        PropertyResponse propertyResponse = restConnectionService.getPropertyDetailsByUpicNoForSearch(waterConnectionReq);
        addPropertyDetails(propertyResponse.getProperties(),connectionList);
        }
    }

	

    private void resolveUserDetails(List<Connection> secondConnectionList, RequestInfo rInfo) {
        String searchUrl = restConnectionService.getUserServiceSearchPath();
        UserResponseInfo userResponse = null;
        Map<String, Object> userSearchRequestInfo = new HashMap<String, Object>();
        Map<String, Object> preparedStatementValuesForOwner = new HashMap<>();
        String connectionOwnerQuery = waterConnectionQueryBuilder.getConnectionOwnerQueryWithId();
        for (Connection connection : secondConnectionList) {
            List<ConnectionOwner> connectionOws = new ArrayList<>();
            preparedStatementValuesForOwner.put("waterconnectionid", connection.getId());
            preparedStatementValuesForOwner.put("tenantid", connection.getTenantId());
            List<ConnectionOwner> connectionOwners = namedParameterJdbcTemplate.query(connectionOwnerQuery,
                    preparedStatementValuesForOwner,
                    new WaterConnectionRowMapper().new WaterConnectionWithoutPropertyOwnerRowMapper());
            connectionOws.addAll(connectionOwners);
            connection.setConnectionOwners(connectionOws);
        }
        for (Connection conn : secondConnectionList) {
        	List<Long> userIds = new ArrayList<>();
            userIds.addAll(
                    conn.getConnectionOwners().stream().map(owner -> owner.getOwnerid()).collect(Collectors.toList()));
            userSearchRequestInfo.put("tenantId", conn.getTenantId());
            userSearchRequestInfo.put("type", roleCode);
            userSearchRequestInfo.put("id", userIds);
            userSearchRequestInfo.put("RequestInfo", rInfo);
            LOGGER.info("User Service Search URL :: " + searchUrl + " \n userSearchRequestInfo  :: "
                    + userSearchRequestInfo);
            userResponse = new RestTemplate().postForObject(searchUrl.toString(), userSearchRequestInfo,
                    UserResponseInfo.class);
            LOGGER.info("User Service Search Response :: " + userResponse);
            List<ConnectionOwner> connOwners = conn.getConnectionOwners();
            String houseNumber = "";
            Address address = null;
            if (null != userResponse && null != userResponse.getUser() && userResponse.getUser().size() > 0) {
                List<User> userList = userResponse.getUser();
                for (User eachUser : userList) {
                    for (ConnectionOwner connOwner : connOwners) {
                        if (eachUser.getId().equals(connOwner.getOwnerid())) {
                            connOwner.setName(eachUser.getName());
                            connOwner.setPermanentAddress(eachUser.getPermanentAddress());
                            connOwner.setUserName(eachUser.getUserName());
                            connOwner.setEmailId(eachUser.getEmailId());
                            connOwner.setMobileNumber(eachUser.getMobileNumber());
                            connOwner.setAadhaarNumber(eachUser.getAadhaarNumber());
                            connOwner.setGender(eachUser.getGender());
                            connOwner.setTenantId(conn.getTenantId());
                        }
                    }
                    address = new Address();
                    address.setAddressLine1(eachUser.getPermanentAddress());
                    address.setCity(eachUser.getPermanentCity());
                    address.setPinCode(eachUser.getPermanentPinCode());
                    houseNumber = (null != eachUser.getCorrespondenceAddress()) ? eachUser.getCorrespondenceAddress()
                            : "";
                }
            }
            conn.setHouseNumber(houseNumber);
            if (null != address) {
                conn.setAddress(address);
            }
        }
    }

	private void resolveMasterDetails(List<Connection> connectionList, RequestInfo requestInfo) {
		LOGGER.info("Resolving the names for the IDs");
		for (Connection conn : connectionList) {
			if (StringUtils.isNotBlank(conn.getPipesizeId())) {
				conn.setHscPipeSizeType(
						ConnectionMasterAdapter.getPipeSizeById(conn.getPipesizeId(), conn.getTenantId(), requestInfo));
			}
			if (StringUtils.isNotBlank(conn.getSupplyTypeId())) {
				conn.setSupplyType(ConnectionMasterAdapter.getSupplyTypeById(conn.getSupplyTypeId(), conn.getTenantId(),
						requestInfo));
			}
			if (StringUtils.isNotBlank(conn.getSourceTypeId())) {
				conn.setSourceType(ConnectionMasterAdapter.getSourceTypeById(conn.getSourceTypeId(), conn.getTenantId(),
						requestInfo));
			}
			if (StringUtils.isNotBlank(conn.getWaterTreatmentId())) {
				conn.setWaterTreatment(ConnectionMasterAdapter.getTreatmentPlantById(conn.getWaterTreatmentId(),
						conn.getTenantId(), requestInfo));
			}
			if (StringUtils.isNotBlank(conn.getStorageReservoirId())) {
				conn.setStorageReservoir(ConnectionMasterAdapter.getStorageReservoirById(conn.getStorageReservoirId(),
						conn.getTenantId(), requestInfo));
			}
			if (StringUtils.isNotBlank(conn.getUsageTypeId())) {
				String nameAndCode = ConnectionMasterAdapter.getUsageTypeById(conn.getUsageTypeId(), conn.getTenantId(),
						requestInfo);
				if (StringUtils.isNotBlank(nameAndCode)) {
					conn.setUsageTypeName(nameAndCode.split("~")[0]);
					conn.setUsageType(nameAndCode.split("~")[1]);
				}
			}
			if (StringUtils.isNotBlank(conn.getSubUsageTypeId())) {
				String nameAndCode = ConnectionMasterAdapter.getSubUsageTypeById(conn.getSubUsageTypeId(),
						conn.getTenantId(), requestInfo);
				if (StringUtils.isNotBlank(nameAndCode)) {
					conn.setSubUsageTypeName(nameAndCode.split("~")[0]);
					conn.setSubUsageType(nameAndCode.split("~")[1]);
				}

			}
		}
	}

    private void resolvePropertyIdentifierDetails(List<Connection> connectionList, RequestInfo rInfo) {
        WaterConnectionReq wR = new WaterConnectionReq();
        wR.setRequestInfo(rInfo);
        PropertyResponse propResp = new PropertyResponse();
        for (Connection conn : connectionList) {
            wR.setConnection(conn);
            propResp = restConnectionService.getPropertyDetailsByUpicNoForSearch(wR);
            DemandDueResponse demandDueResponse = restConnectionService
                    .getPropertyTaxDueResponse(conn.getPropertyIdentifier(), conn.getTenantId());
            if (demandDueResponse != null && demandDueResponse.getDemandDue() != null
                    && demandDueResponse.getDemandDue().getDemands() != null
                    && demandDueResponse.getDemandDue().getDemands().size() > 0) {
                LOGGER.info("response obtained from billing service :" + demandDueResponse);
                ConsolidatedTax tax = demandDueResponse.getDemandDue().getConsolidatedTax();
                if (null != tax) {
                    conn.getProperty().setPropertyTaxDue(BigDecimal.valueOf(tax.getArrearsBalance())
                            .add(BigDecimal.valueOf(tax.getCurrentBalance())));
                }
            }
        }
        if (null != propResp.getProperties() && propResp.getProperties().size() > 0) {
            addPropertyDetails(propResp.getProperties(), connectionList);
        }
    }

    private void addPropertyDetails(List<PropertyInfo> propertyInfoList, List<Connection> connectionWithProperty) {
        LOGGER.info("Adding Property Details Connection List");
        for (Connection conn : connectionWithProperty) {
            BigDecimal propTaxDue = conn.getProperty().getPropertyTaxDue();
            if (null != propertyInfoList) {
                for (PropertyInfo pInfo : propertyInfoList) {
                    if (StringUtils.isNotBlank(conn.getPropertyIdentifier())
                            && StringUtils.isNotBlank(pInfo.getUpicNumber())
                            && conn.getPropertyIdentifier().equals(pInfo.getUpicNumber())) {
                        conn.setHouseNumber(null != pInfo.getAddress() ? pInfo.getAddress().getAddressNumber() : "");
                        Property prop = new Property();
                        prop.setPropertyIdentifier(pInfo.getUpicNumber());
                        prop.setPinCode(null != pInfo.getAddress() ? pInfo.getAddress().getPropertyPinCode() : "");
                        if (null != pInfo.getBoundary() && null != pInfo.getBoundary().getLocationBoundary()) {
                            prop.setLocality(Long.parseLong(StringUtils.isNotBlank(pInfo.getBoundary().getLocationBoundary().getCode()) ? pInfo.getBoundary().getLocationBoundary().getCode() : "0"));
                        }
                        if (null != pInfo.getBoundary() && null != pInfo.getBoundary().getRevenueBoundary()) {
                            prop.setZone(Long.parseLong(StringUtils.isNotBlank(pInfo.getBoundary().getRevenueBoundary().getCode()) ? pInfo.getBoundary().getRevenueBoundary().getCode() : "0"));
                        }
                        List<PropertyOwnerInfo> list = new ArrayList<>();
                        for (PropertyOwnerInfo owner : pInfo.getOwners()) {
                            list.add(owner);
                        }
                        prop.setPropertyOwner(list);
                        prop.setPropertyTaxDue(propTaxDue);
                        conn.setAddress(pInfo.getAddress());
                        conn.setProperty(prop);
                    }
                }
            }
        }
    }

    private void getConnectionMeterDetails(List<Connection> connectionList) {
        String meterDetailsQuery = WaterConnectionQueryBuilder.getConnectionMeterQueryForSearch();
        for (Connection conn : connectionList) {
            final List<Object> preparedStatementValues = new ArrayList<>();
            preparedStatementValues.add(conn.getId());
            LOGGER.info("Get Meter Details Query : " + meterDetailsQuery);
            ConnectionMeterRowMapper mapper = new WaterConnectionRowMapper().new ConnectionMeterRowMapper();
            jdbcTemplate.query(meterDetailsQuery, preparedStatementValues.toArray(), mapper);
            sortMeterDetailsToConnection(conn, mapper);
        }
    }

    private void sortMeterDetailsToConnection(Connection conn, ConnectionMeterRowMapper mapper) {
        Map<Long, Map<Long, Meter>> meterReadingMap = mapper.meterReadingMap;
        Iterator<Entry<Long, Map<Long, Meter>>> itr = meterReadingMap.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<Long, Map<Long, Meter>> entry = itr.next();
            long connectionId = entry.getKey();
            if (conn.getId() == connectionId) {
                Map<Long, Meter> innerMap = entry.getValue();
                Iterator<Entry<Long, Meter>> innerItr = innerMap.entrySet().iterator();
                List<Meter> meterList = new ArrayList<>();
                while (innerItr.hasNext()) {
                    Entry<Long, Meter> innerEntry = innerItr.next();
                    Meter meter = innerEntry.getValue();
                    meterList.add(meter);
                }
                conn.setMeter(meterList);
            }
        }
    }

    public Map<String, Object> getObjectForInsertEstimationNotice(final EstimationNotice estimationNotice,
            final long connectionId, final String tenantId) {
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
            documentList = jdbcTemplate.query(fetchQuery, preparedStatementValues.toArray(),
                    new ConnectionDocumentRowMapper());
        } catch (Exception ex) {
            LOGGER.error("Exception encountered while fetching documents : " + ex);
        }

        return documentList;
    }
}
