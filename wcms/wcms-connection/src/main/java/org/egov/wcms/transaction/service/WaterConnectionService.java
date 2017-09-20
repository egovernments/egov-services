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

package org.egov.wcms.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.demand.contract.Demand;
import org.egov.wcms.transaction.demand.contract.DemandResponse;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.DocumentOwner;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.Role;
import org.egov.wcms.transaction.model.User;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.model.enums.NewConnectionStatus;
import org.egov.wcms.transaction.repository.WaterConnectionRepository;
import org.egov.wcms.transaction.util.WcmsConnectionConstants;
import org.egov.wcms.transaction.validator.ConnectionValidator;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.BoundaryResponse;
import org.egov.wcms.transaction.web.contract.PropertyInfo;
import org.egov.wcms.transaction.web.contract.PropertyOwnerInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.UserRequestInfo;
import org.egov.wcms.transaction.web.contract.UserResponseInfo;
import org.egov.wcms.transaction.web.contract.WaterChargesConfigRes;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WaterConnectionService {

    @Autowired
    private DemandConnectionService demandConnectionService;

    @Autowired
    private WaterConnectionRepository waterConnectionRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RestConnectionService restConnectionService;

    @Autowired
    private ConfigurationManager configurationManager;

    @Autowired
    private ConnectionValidator connectionValidator;

    public static final String roleCode = "CITIZEN";
    public static final String roleName = "Citizen";

    public Connection pushConnectionToKafka(final String topic, final String key,
            final WaterConnectionReq waterConnectionRequest) {
        return sendRequestObjToProducer(topic, key, waterConnectionRequest);
    }

    public Connection updateWaterConnection(final String topic, final String key,
            final WaterConnectionReq waterConnectionRequest) {
        return sendRequestObjToProducer(topic, key, waterConnectionRequest);
    }

    protected Connection sendRequestObjToProducer(final String topic, final String key,
            final WaterConnectionReq waterConnectionRequest) {
        try {
            kafkaTemplate.send(topic, key, waterConnectionRequest);
        } catch (final Exception e) {
            log.error("Producer failed to post request to kafka queue", e);
            return waterConnectionRequest.getConnection();
        }
        if (waterConnectionRequest.getConnection().getProperty() != null
                && waterConnectionRequest.getConnection().getPropertyIdentifier() != null)
            waterConnectionRequest.getConnection().setWithProperty(Boolean.TRUE);
        else
            waterConnectionRequest.getConnection().setWithProperty(Boolean.FALSE);
        return waterConnectionRequest.getConnection();
    }

    public Connection persistBeforeKafkaPush(final WaterConnectionReq waterConnectionRequest) {
        log.info("Service API entry for create Connection");
        final Long connectionAddressId = 0L;
        Long connectionLocationId = 0L;
        try {
            if (waterConnectionRequest.getConnection() != null && waterConnectionRequest.getConnection().getIsLegacy())
                waterConnectionRequest.getConnection()
                        .setConnectionStatus(WcmsConnectionConstants.CONNECTIONSTATUSACTIVE);
            else
                waterConnectionRequest.getConnection().setConnectionStatus(WcmsConnectionConstants.CONNECTIONSTATUSCREAED);

            if (waterConnectionRequest.getConnection().getWithProperty())
                waterConnectionRepository.persistConnection(waterConnectionRequest);
            else {
                log.info("Creating User Id :: ");
                createUserId(waterConnectionRequest);

                log.info("Creating Location Id :: ");
                connectionLocationId = waterConnectionRepository.insertConnectionLocation(waterConnectionRequest);

                log.info("Persisting Connection Details :: ");
                waterConnectionRepository.persistConnection(waterConnectionRequest);
                log.info("Updating Water Connection :: ");
                waterConnectionRepository.updateValuesForNoPropertyConnections(waterConnectionRequest,
                        connectionAddressId, connectionLocationId);
            }
        } catch (final Exception e) {
            log.error("Persisting failed due to db exception", e);
            waterConnectionRequest.getConnection().setId(0L);
        }
        return waterConnectionRequest.getConnection();
    }

    private void createUserId(final WaterConnectionReq waterConnReq) {

        final String searchUrl = restConnectionService.getUserServiceSearchPath();
        final String createUrl = restConnectionService.getUserServiceCreatePath();

        UserResponseInfo userResponse = null;
        final Map<String, Object> userSearchRequestInfo = new HashMap<String, Object>();
        String mobileNumber;
        if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getMobileNumber()))
            mobileNumber = waterConnReq.getConnection().getConnectionOwner().getMobileNumber();
        else
            mobileNumber = generateUserMobileNumberForUserName(waterConnReq);
        userSearchRequestInfo.put("userName", mobileNumber);
        userSearchRequestInfo.put("type", roleCode);
        userSearchRequestInfo.put("tenantId", waterConnReq.getConnection().getTenantId());
        userSearchRequestInfo.put("RequestInfo", waterConnReq.getRequestInfo());

        log.info("User Service Search URL :: " + searchUrl + " \n userSearchRequestInfo  :: "
                + userSearchRequestInfo);
        if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getMobileNumber())
                || StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getEmailId())
                || StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getAadhaarNumber())
                || StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getUserName())) {
            userResponse = new RestTemplate().postForObject(searchUrl.toString(), userSearchRequestInfo, UserResponseInfo.class);
            log.info("User Service Search Response :: " + userResponse);

            if (null == userResponse || userResponse.getUser().size() == 0) {
                userSearchRequestInfo.put("name", waterConnReq.getConnection().getConnectionOwner().getName());
                if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getMobileNumber()))
                    userSearchRequestInfo.put("mobileNumber",
                            waterConnReq.getConnection().getConnectionOwner().getMobileNumber());

                if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getAadhaarNumber()))
                    userSearchRequestInfo.put("aadharNumber",
                            waterConnReq.getConnection().getConnectionOwner().getAadhaarNumber());

                if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getEmailId()))
                    userSearchRequestInfo.put("emailId", waterConnReq.getConnection().getConnectionOwner().getEmailId());
                log.info("User Service Search URL with Multiparam :: " + searchUrl + " \n userSearchRequestInfo :: "
                        + userSearchRequestInfo);
                userResponse = new RestTemplate().postForObject(searchUrl.toString(), userSearchRequestInfo,
                        UserResponseInfo.class);
            }
        }
        log.info("User Service Search Response :: " + userResponse);
        if (null == userResponse || userResponse.getUser().size() == 0) {
            final UserRequestInfo userRequestInfo = new UserRequestInfo();
            userRequestInfo.setRequestInfo(waterConnReq.getRequestInfo());
            final User user = buildUserObjectFromConnection(waterConnReq);
            userRequestInfo.setUser(user);
            log.info("User Object to create User : " + userRequestInfo);
            log.info("User Service Create URL :: " + createUrl + " \n userRequestInfo :: "
                    + userRequestInfo);
            UserResponseInfo userCreateResponse = new UserResponseInfo();
            try {
                userCreateResponse = new RestTemplate().postForObject(createUrl.toString(), userRequestInfo,
                        UserResponseInfo.class);
            } catch (final Exception ex) {
                log.error("Exception encountered while creating user ID : " + ex.getMessage());
            }
            if (userCreateResponse != null && userCreateResponse.getUser() !=null && !userCreateResponse.getUser().isEmpty()) {
                log.info("User Service Create User Response :: " + userCreateResponse);
                user.setId(userCreateResponse.getUser().get(0).getId());
                waterConnReq.getConnection().getConnectionOwner().setId(userCreateResponse.getUser().get(0).getId());
            }
        }

        if (userResponse != null) {
            log.info("User Response after Create and Search :: " + userResponse);
            if (null != userResponse.getUser() && userResponse.getUser().size() > 0)
                waterConnReq.getConnection().getConnectionOwner().setId(userResponse.getUser().get(0).getId());
        }
    }

    public String generateUserMobileNumberForUserName(final WaterConnectionReq waterConnectionRequest) {
        return restConnectionService.generateRequestedDocumentNumber(
                waterConnectionRequest.getConnection().getTenantId(), configurationManager.getUserNameService(),
                configurationManager.getUserNameFormat(), waterConnectionRequest.getRequestInfo());
    }

    private User buildUserObjectFromConnection(final WaterConnectionReq waterConnReq) {
        final Connection conn = waterConnReq.getConnection();
        String userName = null;
        if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getMobileNumber()))
            userName = conn.getConnectionOwner().getMobileNumber();
        else if (userName == null
                && StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getAadhaarNumber()))
            userName = conn.getConnectionOwner().getAadhaarNumber();
        else if (userName == null
                && StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getEmailId()))
            userName = conn.getConnectionOwner().getEmailId();
        else if (userName == null
                && StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getUserName()))
            userName = conn.getConnectionOwner().getUserName();
        else
            userName = restConnectionService.generateRequestedDocumentNumber(waterConnReq.getConnection().getTenantId(),
                    configurationManager.getUserNameService(), configurationManager.getUserNameFormat(),
                    waterConnReq.getRequestInfo());
        final Role role = Role.builder().code(roleCode).name(roleName).build();
        final List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        final User user = new User();
        user.setName(conn.getConnectionOwner().getName());
        user.setMobileNumber(userName);
        user.setUserName(userName);
        user.setActive(true);
        user.setTenantId(conn.getTenantId());
        user.setType(roleCode);
        user.setPassword(configurationManager.getDefaultPassword());
        user.setRoles(roleList);
        if (StringUtils.isNotBlank(conn.getConnectionOwner().getAadhaarNumber()))
            user.setAadhaarNumber(conn.getConnectionOwner().getAadhaarNumber());
        if (StringUtils.isNotBlank(conn.getConnectionOwner().getEmailId()))
            user.setEmailId(conn.getConnectionOwner().getEmailId());
        if (StringUtils.isNotBlank(conn.getAddress().getAddressLine1()))
            user.setPermanentAddress(conn.getAddress().getAddressLine1());
        if (StringUtils.isNotBlank(conn.getAddress().getPinCode()))
            user.setPermanentPincode(conn.getAddress().getPinCode());
        if (StringUtils.isNotBlank(conn.getAddress().getCity()))
            user.setPermanentCity(conn.getAddress().getCity());
        if (StringUtils.isNotBlank(conn.getConnectionOwner().getMobileNumber()))
            user.setMobileNumber(conn.getConnectionOwner().getMobileNumber());
        if (StringUtils.isNotBlank(conn.getConnectionOwner().getGender()))
            user.setGender(conn.getConnectionOwner().getGender());

        return user;
    }

    public Connection create(final WaterConnectionReq waterConnectionRequest) {
        log.info("Service API entry for update with initiate workflow Connection");
        try {

            if (waterConnectionRequest.getConnection().getIsLegacy() != null &&
                    waterConnectionRequest.getConnection().getIsLegacy().equals(Boolean.FALSE)
                    ) {
                //initiateWorkFow(waterConnectionRequest);
                waterConnectionRepository.updateConnectionWorkflow(waterConnectionRequest, null);
            }
        } catch (final Exception e) {
            log.error("workflow intiate and updating connection failed due to db exception", e);
        }
        return waterConnectionRequest.getConnection();
    }

    public Connection update(final WaterConnectionReq waterConnectionRequest) {
        log.info("Service API entry for update Connection");
        if (waterConnectionRequest.getConnection().getIsLegacy())
            try {
                updateWaterConnection(waterConnectionRequest);
            } catch (final Exception e) {
                log.error("update Connection failed due to db exception", e);
            }
        else
            try {
                final Connection connection = waterConnectionRequest.getConnection();
               
                final String status = connection.getStatus();
                if (status != null
                        && status.equalsIgnoreCase(NewConnectionStatus.CREATED.name())
                        && waterConnectionRequest.getConnection().getEstimationCharge() != null
                        && !waterConnectionRequest.getConnection().getEstimationCharge().isEmpty())
                    createDemand(waterConnectionRequest);
                if (status != null
                        && status.equalsIgnoreCase(NewConnectionStatus.CREATED.name()))
                    connection.setStatus(NewConnectionStatus.VERIFIED.name());

                if (status != null
                        && status.equalsIgnoreCase(NewConnectionStatus.VERIFIED.name())) {
                    waterConnectionRequest.getConnection().setEstimationNumber(
                            restConnectionService.generateRequestedDocumentNumber("default",
                                    configurationManager.getEstimateGenNameServiceTopic(),
                                    configurationManager.getEstimateGenFormatServiceTopic(),
                                    waterConnectionRequest.getRequestInfo()));
                    connection.setStatus(NewConnectionStatus.ESTIMATIONNOTICEGENERATED.name());
                }
                if (status != null
                        && (status.equalsIgnoreCase(NewConnectionStatus.ESTIMATIONNOTICEGENERATED.name()) ||
                                status.equalsIgnoreCase(NewConnectionStatus.ESTIMATIONAMOUNTCOLLECTED.name()))) {
                    connection.setStatus(NewConnectionStatus.APPROVED.name());
                    waterConnectionRequest.getConnection()
                            .setConsumerNumber(connectionValidator.generateConsumerNumber(waterConnectionRequest));

                }
                if (status != null
                        && status.equalsIgnoreCase(NewConnectionStatus.APPROVED.name())) {
                    waterConnectionRequest.getConnection().setWorkOrderNumber(
                            restConnectionService.generateRequestedDocumentNumber("default",
                                    configurationManager.getWorkOrderGenNameServiceTopic(),
                                    configurationManager.getWorkOrderGenFormatServiceTopic(),
                                    waterConnectionRequest.getRequestInfo()));
                    connection.setStatus(NewConnectionStatus.SANCTIONED.name());
                }
                waterConnectionRequest.setConnection(connection);

              //  updateWorkFlow(waterConnectionRequest);
                updateWaterConnection(waterConnectionRequest);
            } catch (final Exception e) {
                log.error("update Connection failed due to db exception", e);
            }
        return waterConnectionRequest.getConnection();
    }

    private void updateWaterConnection(final WaterConnectionReq waterConnectionRequest) {
        waterConnectionRepository.updateWaterConnection(waterConnectionRequest);
    }

    public Connection findByApplicationNmber(final String applicationNmber, final String tenantid) {
        List<Connection> tempConnList;
        Connection connectionObj = null;
        tempConnList = waterConnectionRepository.findByApplicationNmber(applicationNmber, tenantid);
        if (!tempConnList.isEmpty() && tempConnList.size() == 1)
            connectionObj = tempConnList.get(0);
        return connectionObj;

    }

    public Connection getWaterConnectionByConsumerNumber(final String consumerCode, final String legacyConsumerNumber,
            final String tenantid) {
        List<Connection> tempConnList;
        Connection connectionObj = null;
        tempConnList = waterConnectionRepository.getWaterConnectionByConsumerNumber(consumerCode, legacyConsumerNumber, tenantid);
        if (!tempConnList.isEmpty() && tempConnList.size() == 1)
            connectionObj = tempConnList.get(0);
        return connectionObj;
    }

    public void updateConnectionOnChangeOfDemand(final String demandId, final Connection waterConn,
            final RequestInfo requestInfo) {
        waterConnectionRepository.updateConnectionOnChangeOfDemand(demandId, waterConn, requestInfo);
    }

   

    private DemandResponse createDemand(final WaterConnectionReq waterConnectionReq) {

        final List<Demand> pros = demandConnectionService
                .prepareDemand(waterConnectionReq.getConnection().getDemand(), waterConnectionReq);
        final DemandResponse demandRes = demandConnectionService.createDemand(pros, waterConnectionReq.getRequestInfo());
        if (demandRes != null && demandRes.getDemands() != null && !demandRes.getDemands().isEmpty())
            waterConnectionReq.getConnection().setDemandid(demandRes.getDemands().get(0).getId());
        return demandRes;
    }

  

    public List<Connection> getConnectionDetails(final WaterConnectionGetReq waterConnectionGetReq,
            final RequestInfo requestInfo) {
        final String urlToInvoke = buildUrlToInvoke(waterConnectionGetReq);
        List<PropertyInfo> propertyInfoList = new ArrayList<>();
        if (StringUtils.isNotBlank(waterConnectionGetReq.getName())
                || StringUtils.isNotBlank(waterConnectionGetReq.getMobileNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getLocality())
                || StringUtils.isNotBlank(waterConnectionGetReq.getDoorNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getRevenueWard()))
            try {
                propertyInfoList = restConnectionService
                        .getPropertyDetailsByParams(restConnectionService.getRequestInfoWrapperWithoutAuth(), urlToInvoke);
            } catch (final Exception e) {
                log.error(
                        "Encountered an Exception while getting the property identifier from Property Module :" + e.getMessage());
            }
        if (null != propertyInfoList && propertyInfoList.size() > 0)
            waterConnectionGetReq
                    .setPropertyIdentifierList(propertyIdentifierListPreparator(waterConnectionGetReq, propertyInfoList));
        final List<Connection> connectionList = waterConnectionRepository.getConnectionDetails(waterConnectionGetReq, requestInfo,
                propertyInfoList);
        if (connectionList.size() == 1)
            for (final Connection conn : connectionList) {
                final List<DocumentOwner> documentList = getDocumentForConnection(conn);
                conn.setDocuments(documentList);
            }
        return connectionList;
    }

    public List<String> propertyIdentifierListPreparator(final WaterConnectionGetReq waterConnectionGetReq,
            final List<PropertyInfo> propertyInfoList) {
        final List<String> propertyIdentifierList = new ArrayList<>();
        for (final PropertyInfo pInfo : propertyInfoList)
            propertyIdentifierList.add(pInfo.getUpicNumber());
        return propertyIdentifierList;
    }

    @SuppressWarnings("static-access")
    public EstimationNotice getEstimationNotice(final String topic, final String key,
            final WaterConnectionGetReq waterConnectionGetReq,
            final RequestInfo requestInfo) {
        final List<PropertyInfo> propertyInfoList = new ArrayList<>();
        final List<Connection> connectionList = waterConnectionRepository.getConnectionDetails(waterConnectionGetReq, requestInfo,
                propertyInfoList);
        EstimationNotice estimationNotice = null;
        Connection connection = null;
        for (int i = 0; i < connectionList.size(); i++) {
            connection = connectionList.get(i);
            final List<String> chargeDescriptions = new ArrayList<>();
            chargeDescriptions.add(WcmsConnectionConstants.getChargeReasonToDisplay()
                    .get(WcmsConnectionConstants.ESIMATIONCHARGEDEMANDREASON)
                    .concat(" : " + Double.toString(connection.getDonationCharge())));
            new EstimationNotice();
            estimationNotice = EstimationNotice.builder()
                    .applicantName(connection.getProperty().getNameOfApplicant())
                    .applicationDate(connection.getCreatedDate())
                    .applicationNumber(connection.getAcknowledgementNumber()).dateOfLetter(new Date().toString())
                    .chargeDescription(chargeDescriptions).letterIntimationSubject("LetterIntimationSubject")
                    .letterNumber("LetterNumber").letterTo(connection.getProperty().getNameOfApplicant())
                    .serviceName("Water Department").slaDays(30L).ulbName(connection.getTenantId()).build();
        }
        final Demand demand = restConnectionService.getDemandEstimation(connection);
        if (null != demand) {
            log.info("Demand Details as received from Billing Service : " + demand.toString());
            if (null != demand.getDemandDetails())
                estimationNotice.getChargeDescription()
                        .add(WcmsConnectionConstants.getChargeReasonToDisplay()
                                .get(WcmsConnectionConstants.DONATIONCHARGEANDREASON).concat(
                                        " : " + demand.getDemandDetails().get(0).getCollectionAmount().toString()));
        }
        final PropertyResponse propertyResponse = restConnectionService
                .getPropertyDetailsByUpicNo(getWaterConnectionRequest(connection));
        if (null != propertyResponse) {
            log.info("Property Response as received from Property Service : " + propertyResponse.toString());
            if (null != propertyResponse.getProperties() && propertyResponse.getProperties().size() > 0) {
                final List<PropertyOwnerInfo> ownersList = propertyResponse.getProperties().get(0).getOwners();
                if (null != ownersList && ownersList.size() > 0) {
                    estimationNotice.setApplicantName(ownersList.get(0).getName());
                    estimationNotice.setLetterTo(ownersList.get(0).getName());
                }
            }
        }
        final boolean insertStatus = waterConnectionRepository.persistEstimationNoticeLog(estimationNotice,
                connection.getId(), connection.getTenantId());
        if (insertStatus)
            return estimationNotice;
        return new EstimationNotice();
    }

    public WorkOrderFormat getWorkOrder(final String topic, final String key, final WaterConnectionGetReq waterConnectionGetReq,
            final RequestInfo requestInfo) {
        // Fetch Connection Details using the Ack Number
        final List<PropertyInfo> propertyInfoList = new ArrayList<>();
        final List<Connection> connectionList = waterConnectionRepository.getConnectionDetails(waterConnectionGetReq, requestInfo,
                propertyInfoList);
        log.info("Fetched the List of Connection Objects for the Ack Number : " + connectionList.size());
        WorkOrderFormat workOrder = null;
        Connection connection = null;
        for (int i = 0; i < connectionList.size(); i++) {
            connection = connectionList.get(i);
            workOrder = WorkOrderFormat.builder().ackNumber(connection.getAcknowledgementNumber())
                    .ackNumberDate("AckNumberDate").connectionId(connection.getId())
                    .workOrderNumber(connection.getWorkOrderNumber())
                    .workOrderDate(new Date(new java.util.Date().getTime()).toString())
                    .hscNumber(connection.getConsumerNumber()).hscNumberDate("HSCNumberDate").tenantId(connection.getTenantId())
                    .build();
        }
        // Get Property Details to fetch the name of the owner
        final PropertyResponse propertyResponse = restConnectionService
                .getPropertyDetailsByUpicNo(getWaterConnectionRequest(connection));
        if (null != propertyResponse) {
            log.info("Property Response as received from Property Service : " + propertyResponse.toString());
            if (null != propertyResponse.getProperties() && propertyResponse.getProperties().size() > 0) {
                final List<PropertyOwnerInfo> ownersList = propertyResponse.getProperties().get(0).getOwners();
                if (null != ownersList && ownersList.size() > 0)
                    workOrder.setWaterTapOwnerName(ownersList.get(0).getName());
            }
        }

        // Sending the message to Kafka Producer
        if (sendDocumentObjToProducer(topic, key, workOrder))
            return workOrder;
        return new WorkOrderFormat();
    }

    private boolean sendDocumentObjToProducer(final String topic, final String key, final WorkOrderFormat workOrder) {
        try {
            kafkaTemplate.send(topic, key, workOrder);
        } catch (final Exception e) {
            log.error("Producer failed to post request to kafka queue", e);
            return false;
        }
        return true;
    }

    public boolean persistWorkOrderLog(final WorkOrderFormat workOrder) {
        return waterConnectionRepository.persistWorkOrderLog(workOrder);
    }

    public void updateWaterConnectionAfterCollection(final DemandResponse demandResponse) {
        Demand demand = null;
        if (demandResponse != null) {
            demand = demandResponse.getDemands().get(0);
            if (demand != null && demand.getBusinessService() != null && demand.getBusinessService().equals("WC")) {
                System.out.println(demand.getBusinessService());
                System.out.println(demand != null ? demand : "demand is nul in WTMS servicel");
                waterConnectionRepository.updateConnectionAfterWorkFlowQuery(demand.getConsumerCode());
            }
        }
    }

    public Long generateNextConsumerNumber() {
        return waterConnectionRepository.generateNextConsumerNumber();
    }

    private WaterConnectionReq getWaterConnectionRequest(final Connection connection) {
        final RequestInfo rInfo = new RequestInfo();
        return new WaterConnectionReq(rInfo, connection);
    }

    private List<DocumentOwner> getDocumentForConnection(final Connection connection) {
        return waterConnectionRepository.getDocumentForConnection(connection);
    }

    private String buildUrlToInvoke(final WaterConnectionGetReq waterConnectionGetReq) {
        final StringBuilder url = new StringBuilder();
        url.append(configurationManager.getPropertyServiceHostNameTopic())
                .append(configurationManager.getPropertyServiceSearchPathTopic())
                .append("?tenantId=").append(waterConnectionGetReq.getTenantId());
        if (null != waterConnectionGetReq.getName() && !waterConnectionGetReq.getName().isEmpty())
            url.append("&ownerName=" + waterConnectionGetReq.getName());
        if (null != waterConnectionGetReq.getMobileNumber() && !waterConnectionGetReq.getMobileNumber().isEmpty())
            url.append("&mobileNumber=" + waterConnectionGetReq.getMobileNumber());
        if (null != waterConnectionGetReq.getLocality() && !waterConnectionGetReq.getLocality().isEmpty())
            url.append("&locality=" + waterConnectionGetReq.getLocality());
        if (null != waterConnectionGetReq.getDoorNumber() && !waterConnectionGetReq.getDoorNumber().isEmpty())
            url.append("&houseNoBldgApt=" + waterConnectionGetReq.getDoorNumber());
        if (null != waterConnectionGetReq.getRevenueWard() && !waterConnectionGetReq.getRevenueWard().isEmpty())
            url.append("&revenueWard=" + waterConnectionGetReq.getDoorNumber());
        return url.toString();
    }

    public Boolean getBoundaryByZone(
            final WaterConnectionReq waterConnectionReq) {
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = restConnectionService.getBoundaryNum(
                WcmsConnectionConstants.ZONE,
                waterConnectionReq.getConnection().getAddress().getZone(),
                waterConnectionReq.getConnection().getTenantId());
        return boundaryRespose != null && !boundaryRespose.getBoundarys().isEmpty();
    }

    public Boolean getBoundaryByWard(final WaterConnectionReq waterConnectionReq) {
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = restConnectionService.getBoundaryNum(
                WcmsConnectionConstants.WARD,
                waterConnectionReq.getConnection().getAddress().getWard(),
                waterConnectionReq.getConnection().getTenantId());
        return boundaryRespose != null && !boundaryRespose.getBoundarys().isEmpty();
    }

    public Boolean getBoundaryByLocation(final WaterConnectionReq waterConnectionReq) {
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = restConnectionService.getBoundaryNum(
                WcmsConnectionConstants.LOCALITY,
                waterConnectionReq.getConnection().getAddress().getLocality(),
                waterConnectionReq.getConnection().getTenantId());
        return boundaryRespose != null && !boundaryRespose.getBoundarys().isEmpty();
    }

    public Boolean getWaterChargeConfigValues(final String tenantId) {
        Boolean isWaterConfigValues = Boolean.FALSE;

        WaterChargesConfigRes waterChargesConfigRes = null;
        waterChargesConfigRes = restConnectionService.getWaterChargesConfig(
                WcmsConnectionConstants.WORKFLOW_REQUIRED_CONFIG_KEY,
                tenantId);
        if (waterChargesConfigRes != null && !waterChargesConfigRes.getWaterConfigurationValue().isEmpty()
                && waterChargesConfigRes.getWaterConfigurationValue().get(0).getValue().equals("YES"))
            isWaterConfigValues = Boolean.TRUE;

        return isWaterConfigValues;
    }
    
    public void beforePersistTasks(final WaterConnectionReq waterConnectionRequest) {
        // Setting the Legacy Flag based on Consumer Number
        if (waterConnectionRequest.getConnection().getLegacyConsumerNumber() != null)
            waterConnectionRequest.getConnection().setIsLegacy(Boolean.TRUE);
        else
            waterConnectionRequest.getConnection().setIsLegacy(Boolean.FALSE);
        // Setting Property Flag as False for Without Property Cases
        if (null == waterConnectionRequest.getConnection().getWithProperty())
            waterConnectionRequest.getConnection().setWithProperty(Boolean.TRUE);

        // Setting the Number Of Family based on the Number of Persons
        waterConnectionRequest.getConnection()
                .setNumberOfFamily(waterConnectionRequest.getConnection().getNumberOfPersons() != 0
                        ? Math.round(waterConnectionRequest.getConnection().getNumberOfPersons() / 4 + 1) : null);

        // Setting Water Connection Created Date
        waterConnectionRequest.getConnection().setCreatedDate(Long.toString(new java.util.Date().getTime()));
    }

    public void generateIdsForWaterConnectionRequest(final WaterConnectionReq waterConnectionRequest) {
        if (waterConnectionRequest.getConnection().getIsLegacy()) {
            waterConnectionRequest.getConnection()
                    .setConsumerNumber("101633333");
            waterConnectionRequest.getConnection()
                    .setAcknowledgementNumber(waterConnectionRequest.getConnection().getConsumerNumber());
            log.info("Consumer Number Generated is : " + waterConnectionRequest.getConnection().getConsumerNumber());

        } else {
            waterConnectionRequest.getConnection().setAcknowledgementNumber(
                    connectionValidator.generateAcknowledgementNumber(waterConnectionRequest));
            log.info(
                    "Acknowledgement Number Generated is : " + waterConnectionRequest.getConnection().getAcknowledgementNumber());
        }
    }

    public Connection afterPersistTasks(final WaterConnectionReq waterConnectionRequest, final Connection connection) {
        if (waterConnectionRequest.getConnection().getIsLegacy()) {
            connection.setConsumerNumber(waterConnectionRequest.getConnection().getConsumerNumber() != null
                    ? waterConnectionRequest.getConnection().getAcknowledgementNumber() : null);
            connection.setIsLegacy(Boolean.TRUE);
        }
        connection.setStatus(waterConnectionRequest.getConnection().getStatus());
        return connection;
    }

}
