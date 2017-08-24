package org.egov.tradelicense.domain.service;

import org.egov.tl.commons.web.contract.RequestInfo;

/**
 * Generator class for Application Number
 * 
 * @author Manoj Kulkarni
 *
 */
public interface ApplicationNumberGeneratorService {
	/**
	 * Method to generate Application Number
	 * 
	 * @return generated number
	 */
	public String generate(final String tenantId, final RequestInfo requestInfo);
}
