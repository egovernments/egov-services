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

package org.egov.eis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PropertiesManager {

    @Value("${egov.services.common_workflow_service.hostname}")
    private String commonWorkFlowServiceHostName;

    @Value("${egov.services.common_workflow_service.process.basepath}")
    private String commonWorkFlowServiceProcessBasePath;

    @Value("${egov.services.common_workflow_service.process.startpath}")
    private String commonWorkFlowServiceProcessStartPath;

    @Value("${egov.services.common_workflow_service.process.updatepath}")
    private String commonWorkFlowServiceProcessUpdatePath;

    @Value("${egov.services.workflow_service.transfer.businesskey}")
    private String workflowServiceTransferBusinessKey;

    @Value("${egov.services.workflow_service.promotion.businesskey}")
    private String workflowServicePromotionBusinessKey;

    @Value("${egov.services.hr_masters_service.hostname}")
    private String hrMastersServiceHostName;

    @Value("${egov.services.hr_masters_service.hrmasters.basepath}")
    private String hrMastersServiceHRMastersBasePath;

    @Value("${egov.services.hr_masters_service.hrstatus.basepath}")
    private String hrMastersServiceHRStatusBasePath;

    @Value("${egov.services.hr_masters_service.recruitmentmodes.basepath}")
    private String hrMastersServiceRecruitmentModeBasePath;

    @Value("${egov.services.hr_masters_service.recruitmenttypes.basepath}")
    private String hrMastersServiceRecruitmentTypeBasePath;

    @Value("${egov.services.hr_masters_service.recruitmentquotas.basepath}")
    private String hrMastersServiceRecruitmentQuotaBasePath;

    @Value("${egov.services.hr_masters_service.employeetypes.basepath}")
    private String hrMastersServiceEmployeeTypeBasePath;

    @Value("${egov.services.hr_masters_service.groups.basepath}")
    private String hrMastersServiceGroupBasePath;

    @Value("${egov.services.hr_masters_service.designations.basepath}")
    private String hrMastersServiceDesignationBasePath;

    @Value("${egov.services.hr_masters_service.hrstatuses.searchpath}")
    private String hrMastersServiceStatusesSearchPath;

    @Value("${egov.services.hr_masters_service.hrstatuses.key}")
    private String hrMastersServiceStatusesKey;
    
    @Value("${egov.services.hr_masters_service.hrstatuses.employees.key}")
    private String hrMastersServiceStatusesEmployeesKey;

    @Value("${egov.services.hr_employee_service.hostname}")
    private String hrEmployeeServiceHostName;

    @Value("${egov.services.hr_employee_service.employees.basepath}")
    private String hrEmployeeServiceEmployeesBasePath;

    @Value("${egov.services.hr_employee_service.vacantpositions.basepath}")
    private String hrEmployeeServiceVacantPositionsBasePath;

    @Value("${egov.services.hr_employee_service.employees.searchpath}")
    private String hrEmployeeServiceEmployeesSearchPath;

    @Value("${egov.services.hr_employee_service.employees.updatepath}")
    private String hrEmployeeServiceEmployeesUpdatePath;

    @Value("${egov.services.hr_employee_service.employees.createpath}")
    private String hrEmployeeServiceEmployeesCreatePath;

    @Value("${egov.services.common_masters_service.hostname}")
    private String commonMastersServiceHostName;

    @Value("${egov.services.common_masters_service.basepath}")
    private String commonMastersServiceBasePath;

    @Value("${egov.services.common_masters_service.religions.basepath}")
    private String commonMastersServiceReligionBasePath;

    @Value("${egov.services.common_masters_service.communities.basepath}")
    private String commonMastersServiceCommunityBasePath;

    @Value("${egov.services.common_masters_service.categories.basepath}")
    private String commonMastersServiceCategoryBasePath;

    @Value("${egov.services.common_masters_service.languages.basepath}")
    private String commonMastersServiceLanguageBasePath;

    @Value("${egov.services.common_masters_service.searchpath}")
    private String commonMastersServiceSearchPath;

    @Value("${egov.services.egf_masters_service.hostname}")
    private String egfMastersServiceHostName;

    @Value("${egov.services.egf_masters_service.basepath}")
    private String egfMastersServiceBasePath;

    @Value("${egov.services.egf_masters_service.banks.basepath}")
    private String egfMastersServiceBankBasePath;

    @Value("${egov.services.egf_masters_service.bankbranches.basepath}")
    private String egfMastersServiceBankBranchBasePath;

    @Value("${egov.services.egf_masters_service.searchpath}")
    private String egfMastersServiceSearchPath;

}