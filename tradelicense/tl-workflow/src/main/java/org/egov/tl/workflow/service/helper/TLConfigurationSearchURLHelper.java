package org.egov.tl.workflow.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TLConfigurationSearchURLHelper {

    @Value("${egov.services.tl_services.hostname}")
    private String tlServiceHostName;

    @Value("${egov.services.tl_services.configuration.basepath}")
    private String tlServiceConfigurationBasePath;

    @Value("${egov.services.tl_services.configurations.searchpath}")
    private String tlServiceConfigurationsSearchPath;

    public String searchURL(final String name, final String tenantId) {
        final String BASE_URL = tlServiceHostName
                + tlServiceConfigurationBasePath
                + tlServiceConfigurationsSearchPath;
        final StringBuilder searchURL = new StringBuilder(BASE_URL + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        searchURL.append("&name=" + name);

        return searchURL.toString();
    }
}