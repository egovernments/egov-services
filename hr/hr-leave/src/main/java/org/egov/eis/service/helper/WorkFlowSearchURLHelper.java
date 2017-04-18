package org.egov.eis.service.helper;

import org.egov.eis.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkFlowSearchURLHelper {

    @Autowired
    private PropertiesManager propertiesManager;

    public String startURL() {
        return (propertiesManager.getCommonWorkFlowServiceHostName()
                + propertiesManager.getCommonWorkFlowServiceProcessBasePath()
                + propertiesManager.getCommonWorkFlowServiceProcessStartPath()).toString();
    }

    public String updateURL(final Long id) {
        return (propertiesManager.getCommonWorkFlowServiceHostName()
                + propertiesManager.getCommonWorkFlowServiceProcessBasePath()
                + propertiesManager.getCommonWorkFlowServiceProcessUpdatePath().replaceAll("\\{id\\}", id.toString())).toString();
    }
}
