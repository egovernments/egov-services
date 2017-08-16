package org.egov.tradelicense.domain.service;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation Generator class for Application Number
 * 
 * @author Manoj Kulkarni
 *
 */
public class ApplicationNumberGeneratorServiceImpl implements ApplicationNumberGeneratorService {

    @Autowired
    private IdGenService idGenService;

    @Autowired
    private PropertiesManager propertiesManager;

    /**
     * Implementation Method to generate Application Number
     * 
     * @return generated number
     */
    public String generate(final String tenantId, final RequestInfo requestInfo) {
        return idGenService.generate(tenantId,
                propertiesManager.getIdApplicationNumberGenNameServiceTopic(),
                propertiesManager.getIdApplicationNumberGenFormatServiceTopic(), requestInfo);
    }
}
