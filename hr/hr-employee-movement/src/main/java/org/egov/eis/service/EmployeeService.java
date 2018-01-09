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

import java.util.List;

import org.egov.eis.model.Movement;
import org.egov.eis.service.helper.EmployeeSearchURLHelper;
import org.egov.eis.service.helper.HRStatusSearchURLHelper;
import org.egov.eis.web.contract.BankBranchContract;
import org.egov.eis.web.contract.BankBranchContractResponse;
import org.egov.eis.web.contract.BankContract;
import org.egov.eis.web.contract.BankContractResponse;
import org.egov.eis.web.contract.Category;
import org.egov.eis.web.contract.CategoryResponse;
import org.egov.eis.web.contract.Community;
import org.egov.eis.web.contract.CommunityResponse;
import org.egov.eis.web.contract.Designation;
import org.egov.eis.web.contract.DesignationResponse;
import org.egov.eis.web.contract.Employee;
import org.egov.eis.web.contract.EmployeeInfo;
import org.egov.eis.web.contract.EmployeeInfoResponse;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.EmployeeResponse;
import org.egov.eis.web.contract.EmployeeType;
import org.egov.eis.web.contract.EmployeeTypeResponse;
import org.egov.eis.web.contract.Group;
import org.egov.eis.web.contract.GroupResponse;
import org.egov.eis.web.contract.HRStatus;
import org.egov.eis.web.contract.HRStatusResponse;
import org.egov.eis.web.contract.Language;
import org.egov.eis.web.contract.LanguageResponse;
import org.egov.eis.web.contract.MovementRequest;
import org.egov.eis.web.contract.RecruitmentMode;
import org.egov.eis.web.contract.RecruitmentModeResponse;
import org.egov.eis.web.contract.RecruitmentQuota;
import org.egov.eis.web.contract.RecruitmentQuotaResponse;
import org.egov.eis.web.contract.RecruitmentType;
import org.egov.eis.web.contract.RecruitmentTypeResponse;
import org.egov.eis.web.contract.Religion;
import org.egov.eis.web.contract.ReligionResponse;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeSearchURLHelper employeeSearchURLHelper;

    @Autowired
    private HRStatusSearchURLHelper hrStatusSearchURLHelper;

    public EmployeeInfo getEmployee(final MovementRequest movementRequest) {
        final String url = employeeSearchURLHelper.searchURL(movementRequest.getMovement().get(0).getEmployeeId(),
                movementRequest.getMovement().get(0).getTenantId());

        final RestTemplate restTemplate = new RestTemplate();
        final RequestInfoWrapper wrapper = new RequestInfoWrapper();
        wrapper.setRequestInfo(movementRequest.getRequestInfo());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);

        final EmployeeInfoResponse employeeResponse = restTemplate.postForObject(url, request,
                EmployeeInfoResponse.class);

        if(employeeResponse.getEmployees() != null && !employeeResponse.getEmployees().isEmpty())
            return employeeResponse.getEmployees().get(0);
        else
            return new EmployeeInfo();
    }

    public Employee updateEmployee(final Employee employee, final String tenantId, final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.updateURL(tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        final EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setRequestInfo(requestInfo);
        employeeRequest.setEmployee(employee);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<EmployeeRequest> request = new HttpEntity<>(employeeRequest);

        final EmployeeResponse employeeResponse = restTemplate.postForObject(url, request,
                EmployeeResponse.class);

        if(employeeResponse.getEmployee() != null)
            return employeeResponse.getEmployee();
        else
            return new Employee();
    }

    public Employee createEmployee(final Employee employee, final String tenantId, final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.createURL(tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        final EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setRequestInfo(requestInfo);
        employeeRequest.setEmployee(employee);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<EmployeeRequest> request = new HttpEntity<>(employeeRequest);

        final EmployeeResponse employeeResponse = restTemplate.postForObject(url, request,
                EmployeeResponse.class);

        if(employeeResponse.getEmployee() != null)
            return employeeResponse.getEmployee();
        else
            return new Employee();
    }

    public List<HRStatus> getHRStatuses(final String objectName, final String code, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = hrStatusSearchURLHelper.searchURL(objectName, code, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final HRStatusResponse hrStatusResponse = restTemplate.postForObject(url, request,
                HRStatusResponse.class);

        return hrStatusResponse.getHrStatus();
    }

    private HttpEntity<RequestInfoWrapper> setRequestHeaders(final RequestInfo requestInfo) {
        final RequestInfoWrapper wrapper = new RequestInfoWrapper();
        wrapper.setRequestInfo(requestInfo);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie",
                "_ga=GA1.2.441293708.1486471903; SESSIONID=b9ee6d73-0f61-494b-bad4-ff6752bea389; JSESSIONID=kpSgYmq1HKqntZtuGnuZWhWbTU_LMLi4L0bsbrrH.ip-10-0-0-100");

        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper, headers);
        return request;
    }

    public List<RecruitmentMode> getRecruitmentModes(final String name, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchRecruitmentModeURL(name, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final RecruitmentModeResponse recruitmentModeResponse = restTemplate.postForObject(url, request,
                RecruitmentModeResponse.class);

        return recruitmentModeResponse.getRecruitmentMode();
    }

    public List<RecruitmentType> getRecruitmentTypes(final String name, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchRecruitmentTypeURL(name, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final RecruitmentTypeResponse recruitmentTypeResponse = restTemplate.postForObject(url, request,
                RecruitmentTypeResponse.class);

        return recruitmentTypeResponse.getRecruitmentType();
    }

    public List<RecruitmentQuota> getRecruitmentQuotas(final String name, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchRecruitmentQuotaURL(name, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final RecruitmentQuotaResponse recruitmentQuotaResponse = restTemplate.postForObject(url, request,
                RecruitmentQuotaResponse.class);

        return recruitmentQuotaResponse.getRecruitmentQuota();
    }

    public List<EmployeeType> getEmployeeTypes(final String name, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchEmployeeTypeURL(name, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final EmployeeTypeResponse employeeTypeResponse = restTemplate.postForObject(url, request,
                EmployeeTypeResponse.class);

        return employeeTypeResponse.getEmployeeType();
    }

    public List<Group> getGroups(final String name, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchGroupURL(name, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final GroupResponse groupResponse = restTemplate.postForObject(url, request,
                GroupResponse.class);

        return groupResponse.getGroup();
    }

    public List<Designation> getDesignations(final String code, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchDesignationURL(code, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final DesignationResponse designationResponse = restTemplate.postForObject(url, request,
                DesignationResponse.class);

        return designationResponse.getDesignation();
    }

    public List<Religion> getReligions(final String name, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchReligionURL(name, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final ReligionResponse religionResponse = restTemplate.postForObject(url, request,
                ReligionResponse.class);

        return religionResponse.getReligion();
    }

    public List<Community> getCommunities(final String name, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchCommunityURL(name, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final CommunityResponse communityResponse = restTemplate.postForObject(url, request,
                CommunityResponse.class);

        return communityResponse.getCommunity();
    }

    public List<Category> getCategories(final String name, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchCategoryURL(name, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final CategoryResponse categoryResponse = restTemplate.postForObject(url, request,
                CategoryResponse.class);

        return categoryResponse.getCategory();
    }

    public List<Language> getLanguages(final String name, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchLanguageURL(name, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final LanguageResponse languageResponse = restTemplate.postForObject(url, request,
                LanguageResponse.class);

        return languageResponse.getLanguage();
    }

    public List<BankContract> getBanks(final String code, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchBankURL(code, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final BankContractResponse bankContractResponse = restTemplate.postForObject(url, request,
                BankContractResponse.class);

        return bankContractResponse.getBanks();
    }

    public List<BankBranchContract> getBankBranches(final String code, final Long id, final String tenantId,
            final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchBankBranchURL(code, id, tenantId);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = setRequestHeaders(requestInfo);

        final BankBranchContractResponse bankBranchContractResponse = restTemplate.postForObject(url, request,
                BankBranchContractResponse.class);

        return bankBranchContractResponse.getBankBranches();
    }

    public EmployeeInfo getEmployee(final Movement movement,final RequestInfo requestInfo) {
        final String url = employeeSearchURLHelper.searchURL(movement.getEmployeeId(),
                movement.getTenantId());

        final RestTemplate restTemplate = new RestTemplate();
        final RequestInfoWrapper wrapper = new RequestInfoWrapper();
        wrapper.setRequestInfo(requestInfo);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final EmployeeInfoResponse employeeResponse = restTemplate.postForObject(url, wrapper,
                EmployeeInfoResponse.class);

        if(employeeResponse.getEmployees() != null && !employeeResponse.getEmployees().isEmpty())
            return employeeResponse.getEmployees().get(0);
        else
            return new EmployeeInfo();
    }
}