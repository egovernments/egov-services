package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HRStatusSearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PropertiesManager propertiesManager;

    public String searchURL(final String objectName, final String code, final Long id, final String tenantId) {
        final String baseURL = propertiesManager.getHrMastersServiceHostName()
                + propertiesManager.getHrMastersServiceHRMastersBasePath()
                + propertiesManager.getHrMastersServiceHRStatusBasePath()
                + propertiesManager.getHrMastersServiceStatusesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseURL + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        searchURL.append("&objectName=" + objectName);

        if (code != null)
            searchURL.append("&code=" + code);
        if (id != null)
            searchURL.append("&id=" + id);

        searchURL.append("&pageSize=" + applicationProperties.employeeMovementSearchPageSizeMax());

        return searchURL.toString();
    }
}