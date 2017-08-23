package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.web.contract.CompensatoryLeaveSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class EmployeeSearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PropertiesManager propertiesManager;

    public String searchURL(final CompensatoryLeaveSearchRequest compensatoryLeaveSearchRequest, final String url) {
        final StringBuilder searchURL = new StringBuilder(url + "?");

        if (compensatoryLeaveSearchRequest.getTenantId() == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + compensatoryLeaveSearchRequest.getTenantId());

        if (!StringUtils.isEmpty(compensatoryLeaveSearchRequest.getCode()))
            searchURL.append("&code=" + compensatoryLeaveSearchRequest.getCode());

        if (compensatoryLeaveSearchRequest.getDepartmentId() != null)
            searchURL.append("&departmentId=" + compensatoryLeaveSearchRequest.getDepartmentId());

        if (compensatoryLeaveSearchRequest.getDesignationId() != null)
            searchURL.append("&designationId=" + compensatoryLeaveSearchRequest.getDesignationId());

        if (compensatoryLeaveSearchRequest.getEmployeeType() != null)
            searchURL.append("&type=" + compensatoryLeaveSearchRequest.getEmployeeType());

        searchURL.append("&pageSize=" + applicationProperties.hrLeaveSearchPageSizeMax());

        return searchURL.toString();
    }

}