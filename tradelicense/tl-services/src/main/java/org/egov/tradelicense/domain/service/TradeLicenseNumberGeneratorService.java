package org.egov.tradelicense.domain.service;

import org.egov.tl.commons.web.contract.RequestInfo;

/**
 * Generator class for Trade License Number
 * 
 * @author Manoj Kulkarni
 *
 */
public interface TradeLicenseNumberGeneratorService {
	/**
	 * Method to generate Trade License Number
	 * 
	 * @return generated number
	 */
	public String generate(final String tenantId, final RequestInfo requestInfo);
}
