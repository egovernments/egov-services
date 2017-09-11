package org.egov.tradelicense.domain.service;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation Generator class for Trade License Number
 * 
 * @author Manoj Kulkarni
 *
 */
@Service
public class TradeLicenseNumberGeneratorServiceImpl implements TradeLicenseNumberGeneratorService {

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
		return idGenService.generate(tenantId, propertiesManager.getIdTLNumberGenNameServiceTopic(),
				propertiesManager.getIdTLNumberGenFormatServiceTopic(), requestInfo);
	}
}
