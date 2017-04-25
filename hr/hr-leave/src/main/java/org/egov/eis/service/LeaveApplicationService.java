/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.broker.LeaveApplicationProducer;
import org.egov.eis.model.LeaveApplication;
import org.egov.eis.model.enums.LeaveStatus;
import org.egov.eis.repository.LeaveApplicationRepository;
import org.egov.eis.web.contract.LeaveApplicationGetRequest;
import org.egov.eis.web.contract.LeaveApplicationSingleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LeaveApplicationService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LeaveApplicationService.class);

    @Value("${kafka.topics.leaveapplication.create.name}")
    private String leaveApplicationCreateTopic;

    @Value("${kafka.topics.leaveapplication.create.key}")
    private String leaveApplicationCreateKey;

    @Value("${kafka.topics.leaveapplication.update.name}")
    private String leaveApplicationUpdateTopic;

    @Value("${kafka.topics.leaveapplication.update.key}")
    private String leaveApplicationUpdateKey;

    @Autowired
    private LeaveApplicationProducer leaveApplicationProducer;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    public List<LeaveApplication> getLeaveApplications(final LeaveApplicationGetRequest leaveApplicationGetRequest) {
        return leaveApplicationRepository.findForCriteria(leaveApplicationGetRequest);
    }

    public LeaveApplication createLeaveApplication(final LeaveApplicationSingleRequest leaveApplicationRequest) {
        final LeaveApplication leaveApplication = leaveApplicationRequest.getLeaveApplication();
        leaveApplication.setStatus(LeaveStatus.APPLIED);
        String leaveApplicationRequestJson = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            leaveApplicationRequestJson = mapper.writeValueAsString(leaveApplicationRequest);
            LOGGER.info("leaveApplicationRequestJson::" + leaveApplicationRequestJson);
        } catch (final JsonProcessingException e) {
            LOGGER.error("Error while converting Leave Application to JSON", e);
            e.printStackTrace();
        }
        try {
            leaveApplicationProducer.sendMessage(leaveApplicationCreateTopic, leaveApplicationCreateKey,
                    leaveApplicationRequestJson);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        return leaveApplication;
    }

    public LeaveApplicationSingleRequest create(final LeaveApplicationSingleRequest leaveApplicationRequest) {
        return leaveApplicationRepository.saveLeaveApplication(leaveApplicationRequest);
    }

    public LeaveApplication updateLeaveApplication(final LeaveApplicationSingleRequest leaveApplicationRequest) {
        final LeaveApplication leaveApplication = leaveApplicationRequest.getLeaveApplication();
        final LeaveApplicationGetRequest leaveApplicationGetRequest = new LeaveApplicationGetRequest();
        final List<Long> ids = new ArrayList<>();
        ids.add(leaveApplication.getId());
        leaveApplicationGetRequest.setId(ids);
        final List<LeaveApplication> leaveApplications = leaveApplicationRepository.findForCriteria(leaveApplicationGetRequest);
        leaveApplication.setStatus(leaveApplications.get(0).getStatus());
        leaveApplication.setStateId(leaveApplications.get(0).getStateId());
        String leaveApplicationRequestJson = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            leaveApplicationRequestJson = mapper.writeValueAsString(leaveApplicationRequest);
            LOGGER.info("leaveApplicationRequestJson::" + leaveApplicationRequestJson);
        } catch (final JsonProcessingException e) {
            LOGGER.error("Error while converting Leave Application to JSON", e);
            e.printStackTrace();
        }
        try {
            leaveApplicationProducer.sendMessage(leaveApplicationUpdateTopic, leaveApplicationUpdateKey,
                    leaveApplicationRequestJson);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        leaveApplicationStatusChange(leaveApplication);
        return leaveApplication;
    }

    private void leaveApplicationStatusChange(LeaveApplication leaveApplication) {
        final String workFlowAction = leaveApplication.getWorkflowDetails().getAction();
        if ("Approve".equalsIgnoreCase(workFlowAction))
            leaveApplication.setStatus(LeaveStatus.APPROVED);
        else if ("Reject".equalsIgnoreCase(workFlowAction))
            leaveApplication.setStatus(LeaveStatus.REJECTED);
        else if ("Cancel".equalsIgnoreCase(workFlowAction))
            leaveApplication.setStatus(LeaveStatus.CANCELLED);
        else if ("submit".contains(workFlowAction))
            leaveApplication.setStatus(LeaveStatus.APPLIED);
    }

    public LeaveApplication update(final LeaveApplicationSingleRequest leaveApplicationRequest) {
        return leaveApplicationRepository.updateLeaveApplication(leaveApplicationRequest);
    }

}