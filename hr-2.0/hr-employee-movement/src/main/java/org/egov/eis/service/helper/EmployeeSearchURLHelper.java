package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeSearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PropertiesManager propertiesManager;

    public String searchURL(final Long id, final String code, final String tenantId) {
        final String baseUrl = propertiesManager.getHrEmployeeServiceHostName()
                + propertiesManager.getHrEmployeeServiceEmployeesBasePath()
                + propertiesManager.getHrEmployeeServiceEmployeesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseUrl + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (id != null)
            searchURL.append("&id=" + id);

        if (code != null)
            searchURL.append("&code=" + code);

        searchURL.append("&pageSize=" + applicationProperties.employeeMovementSearchPageSizeMax());

        return searchURL.toString();    }

    public String updateURL(final String tenantId) {
        final String baseUrl = propertiesManager.getHrEmployeeServiceHostName()
                + propertiesManager.getHrEmployeeServiceEmployeesBasePath()
                + propertiesManager.getHrEmployeeServiceEmployeesUpdatePath();
        final StringBuilder searchURL = new StringBuilder(baseUrl + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        return searchURL.toString();
    }

    public String createURL(final String tenantId) {
        final String baseUrl = propertiesManager.getHrEmployeeServiceHostName()
                + propertiesManager.getHrEmployeeServiceEmployeesBasePath()
                + propertiesManager.getHrEmployeeServiceEmployeesCreatePath();
        final StringBuilder searchURL = new StringBuilder(baseUrl + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        return searchURL.toString();
    }

    public String searchRecruitmentModeURL(final String name, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getHrMastersServiceHostName()
                + propertiesManager.getHrMastersServiceHRMastersBasePath()
                + propertiesManager.getHrMastersServiceRecruitmentModeBasePath()
                + propertiesManager.getHrMastersServiceStatusesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        appendRequestParams(name, id, tenantId, searchURL);

        return searchURL.toString();
    }

    public String searchRecruitmentTypeURL(final String name, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getHrMastersServiceHostName()
                + propertiesManager.getHrMastersServiceHRMastersBasePath()
                + propertiesManager.getHrMastersServiceRecruitmentTypeBasePath()
                + propertiesManager.getHrMastersServiceStatusesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        appendRequestParams(name, id, tenantId, searchURL);

        return searchURL.toString();
    }

    public String searchRecruitmentQuotaURL(final String name, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getHrMastersServiceHostName()
                + propertiesManager.getHrMastersServiceHRMastersBasePath()
                + propertiesManager.getHrMastersServiceRecruitmentQuotaBasePath()
                + propertiesManager.getHrMastersServiceStatusesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        appendRequestParams(name, id, tenantId, searchURL);

        return searchURL.toString();
    }

    public String searchEmployeeTypeURL(final String name, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getHrMastersServiceHostName()
                + propertiesManager.getHrMastersServiceHRMastersBasePath()
                + propertiesManager.getHrMastersServiceEmployeeTypeBasePath()
                + propertiesManager.getHrMastersServiceStatusesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        appendRequestParams(name, id, tenantId, searchURL);

        return searchURL.toString();
    }

    public String searchGroupURL(final String name, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getHrMastersServiceHostName()
                + propertiesManager.getHrMastersServiceHRMastersBasePath()
                + propertiesManager.getHrMastersServiceGroupBasePath()
                + propertiesManager.getHrMastersServiceStatusesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        appendRequestParams(name, id, tenantId, searchURL);

        return searchURL.toString();
    }

    public String searchDesignationURL(final String code, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getHrMastersServiceHostName()
                + propertiesManager.getHrMastersServiceHRMastersBasePath()
                + propertiesManager.getHrMastersServiceDesignationBasePath()
                + propertiesManager.getHrMastersServiceStatusesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (code != null)
            searchURL.append("&code=" + code);
        if (id != null)
            searchURL.append("&id=" + id);

        searchURL.append("&pageSize=" + applicationProperties.employeeMovementSearchPageSizeMax());

        return searchURL.toString();
    }

    public String searchReligionURL(final String name, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getCommonMastersServiceHostName()
                + propertiesManager.getCommonMastersServiceBasePath()
                + propertiesManager.getCommonMastersServiceReligionBasePath()
                + propertiesManager.getCommonMastersServiceSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        return appendRequestParams(name, id, tenantId, searchURL);
    }

    public String searchCommunityURL(final String name, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getCommonMastersServiceHostName()
                + propertiesManager.getCommonMastersServiceBasePath()
                + propertiesManager.getCommonMastersServiceCommunityBasePath()
                + propertiesManager.getCommonMastersServiceSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        return appendRequestParams(name, id, tenantId, searchURL);
    }

    public String searchCategoryURL(final String name, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getCommonMastersServiceHostName()
                + propertiesManager.getCommonMastersServiceBasePath()
                + propertiesManager.getCommonMastersServiceCategoryBasePath()
                + propertiesManager.getCommonMastersServiceSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        return appendRequestParams(name, id, tenantId, searchURL);
    }

    public String searchLanguageURL(final String name, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getCommonMastersServiceHostName()
                + propertiesManager.getCommonMastersServiceBasePath()
                + propertiesManager.getCommonMastersServiceLanguageBasePath()
                + propertiesManager.getCommonMastersServiceSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        return appendRequestParams(name, id, tenantId, searchURL);
    }

    public String searchBankURL(final String code, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getEgfMastersServiceHostName()
                + propertiesManager.getEgfMastersServiceBasePath()
                + propertiesManager.getEgfMastersServiceBankBasePath()
                + propertiesManager.getEgfMastersServiceSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (code != null)
            searchURL.append("&code=" + code);
        if (id != null)
            searchURL.append("&id=" + id);

        searchURL.append("&pageSize=" + applicationProperties.employeeMovementSearchPageSizeMax());

        return searchURL.toString();
    }

    public String searchBankBranchURL(final String code, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getEgfMastersServiceHostName()
                + propertiesManager.getEgfMastersServiceBasePath()
                + propertiesManager.getEgfMastersServiceBankBranchBasePath()
                + propertiesManager.getEgfMastersServiceSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (code != null)
            searchURL.append("&code=" + code);
        if (id != null)
            searchURL.append("&id=" + id);

        searchURL.append("&pageSize=" + applicationProperties.employeeMovementSearchPageSizeMax());

        return searchURL.toString();
    }

    private String appendRequestParams(final String name, final Long id, final String tenantId, final StringBuilder searchURL) {
        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (name != null)
            searchURL.append("&name=" + name);
        if (id != null)
            searchURL.append("&id=" + id);

        searchURL.append("&pageSize=" + applicationProperties.employeeMovementSearchPageSizeMax());

        return searchURL.toString();
    }
}