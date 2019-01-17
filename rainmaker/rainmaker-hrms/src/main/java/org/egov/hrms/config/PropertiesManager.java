/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.empernments.org
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
 *  In case of any queries, you can reach eGovernments Foundation at contact@empernments.org.
 */

package org.egov.hrms.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PropertiesManager {

    @Value("${kafka.topics.employee.savedb.name}")
    private String saveEmployeeTopic;
    
    @Value("${kafka.topics.employee.finance.name}")
    private String financeEmployeeTopic;

    @Value("${kafka.topics.employee.updatedb.name}")
    private String updateEmployeeTopic;

    @Value("${kafka.topics.employee.savedb.key}")
    private String employeeSaveKey;

    @Value("${kafka.topics.employee.finance.key}")
    private String financeEmployeeKey;
    
    @Value("${kafka.topics.nominee.savedb.name}")
    private String saveNomineeTopic;

    @Value("${kafka.topics.nominee.savedb.key}")
    private String nomineeSaveKey;

    @Value("${kafka.topics.nominee.updatedb.name}")
    private String updateNomineeTopic;

    @Value("${kafka.topics.nominee.updatedb.key}")
    private String nomineeUpdateKey;

    @Value("${egov.services.hr_employee_service.hostname}")
    private String hrEmployeeServiceHostName;

    @Value("${egov.services.hr_employee_service.basepath}")
    private String hrEmployeeServiceBasePath;

    @Value("${egov.services.hr_employee_service.employee.createpath}")
    private String hrEmployeeServiceEmployeeCreatePath;

    @Value("${egov.services.users_service.hostname}")
    private String usersServiceHostName;

    @Value("${egov.services.users_service.users.basepath}")
    private String usersServiceUsersBasePath;

    @Value("${egov.services.users_service.users.searchpath}")
    private String usersServiceUsersSearchPath;

    @Value("${egov.services.users_service.users.createpath}")
    private String usersServiceUsersCreatePath;

    @Value("${egov.services.users_service.users.updatepath}")
    private String usersServiceUsersUpdatePath;

    @Value("${egov.services.egov_common_masters_service.hostname}")
    private String commonMastersServiceHostName;

    @Value("${egov.services.egov_common_masters_service.basepath}")
    private String commonMastersServiceBasePath;

    @Value("${egov.services.egov_common_masters_service.departments.searchpath}")
    private String commonMastersServiceDepartmentsSearchPath;

    @Value("${egov.services.hr_masters_service.hostname}")
    private String hrMastersServiceHostName;

    @Value("${egov.services.hr_masters_service.basepath}")
    private String hrMastersServiceBasePath;

    @Value("${egov.services.hr_masters_service.positions.searchpath}")
    private String hrMastersServicePositionsSearchPath;

    @Value("${egov.services.hr_masters_service.vacantpositions.searchpath}")
    private String hrMastersServiceVacantPositionsSearchPath;

    @Value("${egov.services.hr_masters_service.designations.searchpath}")
    private String hrMastersServiceDesignationsSearchPath;

    @Value("${egov.services.hr_masters_service.hr_configurations.searchpath}")
    private String hrMastersServiceHRConfigurationsSearchPath;

    @Value("${egov.services.hr_masters_service.empstatus.searchpath}")
    private String hrMastersEmployeeStatusSearchPath;

    @Value("${egov.services.hr_masters_service.emptype.searchpath}")
    private String hrMastersEmployeeTypeSearchPath;
    
    @Value("${kafka.topics.assignment.update.name}")
    private String updateAssignmentTopic;

    @Value("${kafka.topics.assignment.update.key}")
    private String updateAssignmentKey;

    @Value("${egov.services.data_sync_employee_service.hostname}")
    private String hybridDataSyncServiceHostName;

    @Value("${egov.services.data_sync_employee_service.basepath}")
    private String hybridDataSyncServiceBasePath;

    @Value("${egov.services.data_sync_employee_service.createpath}")
    private String hybridDataSyncServiceCreatePath;

    @Value("${egov.services.data_sync_employee.required}")
    private Boolean dataSyncEmployeeRequired;

    @Value("${egov.services.egov_idgen.hostname}")
    private String idGenServiceHostName;

    @Value("${egov.services.egov_idgen.createpath}")
    private String idGenServiceCreatePath;

    @Value("${egov.services.egov_idgen.emp.code.name}")
    private String idGenServiceEmpCodeName;

    @Value("${egov.services.egov_idgen.emp.code.format}")
    private String idGenServiceEmpCodeFormat;
    
    
    @Value("${egov.services.egov_mdms.hostname}")
    private String mdmsServiceHostname;

    @Value("${egov.services.egov_mdms.searchpath}")
    private String mdmsBySearchCriteriaUrl;

    @Value("${egov.services.hr_employee_service.default.password}")
    private String employeeDefaultPassword;

}