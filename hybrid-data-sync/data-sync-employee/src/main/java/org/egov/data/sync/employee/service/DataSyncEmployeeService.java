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

package org.egov.data.sync.employee.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.data.sync.employee.config.PropertiesManager;
import org.egov.data.sync.employee.repository.DataSyncEmployeeRepository;
import org.egov.data.sync.employee.web.contract.EmployeeSync;
import org.egov.data.sync.employee.web.contract.EmployeeSyncRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class DataSyncEmployeeService {

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FileStoreService fileStorageService;

    @Autowired
    private DataSyncEmployeeRepository dataSyncEmployeeRepository;

    public void createAsync(EmployeeSyncRequest employeeSyncRequest) {
        log.info("EmployeeSyncRequest before sending to kafka :: " + employeeSyncRequest);
        //kafkaTemplate.send(propertiesManager.getSaveSignatureTopic(), employeeSyncRequest);
        create(employeeSyncRequest);
    }

    @Transactional
    public void create(EmployeeSyncRequest employeeSyncRequest) {
        EmployeeSync employeeSync = employeeSyncRequest.getEmployeeSync();
        dataSyncEmployeeRepository.executeProcedure(employeeSync.getCode(), employeeSync.getTenantId());

        if (!StringUtils.isEmpty(employeeSync.getSignature())) {
            byte[] fileData = fileStorageService.getFile(employeeSync);
            String[] tenant = employeeSync.getTenantId().split("\\.");
            String tenantId = tenant.length > 1 ? tenant[tenant.length - 1] : tenant[0];
            dataSyncEmployeeRepository.updateEmployeeSignature(fileData, employeeSync.getUserName(), tenantId);
        }
    }
}