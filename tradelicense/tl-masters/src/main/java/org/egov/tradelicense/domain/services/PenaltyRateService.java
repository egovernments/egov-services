package org.egov.tradelicense.domain.services;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.PenaltyRateRequest;
import org.egov.tl.commons.web.response.PenaltyRateResponse;

/**
 * Service class for PenaltyRate master
 * 
 * @author Pavan Kumar Kamma
 *
 */
public interface PenaltyRateService {

	/**
	 * Description : service method for creating penaltyRate master
	 * 
	 * @param tenantId
	 * @param PenaltyRateRequest
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	public PenaltyRateResponse createPenaltyRateMaster(String tenantId, PenaltyRateRequest penaltyRateRequest);

	/**
	 * Description : service method for updating penaltyRate master
	 * 
	 * @param PenaltyRateRequest
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	public PenaltyRateResponse updatePenaltyRateMaster(PenaltyRateRequest penaltyRateRequest);

	/**
	 * Description : service method for searching penaltyRate master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param applicationTypeId
	 * @param pageSize
	 * @param offSet
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	public PenaltyRateResponse getPenaltyRateMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String applicationType, Integer pageSize, Integer offSet);

	/**
	 * Description : service method for creating penaltyRate master
	 * 
	 * @param tenantId
	 * @param PenaltyRateRequest
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	public void createPenaltyRate(PenaltyRateRequest penaltyRateRequest);

	/**
	 * Description : service method for updating penaltyRate master
	 * 
	 * @param PenaltyRateRequest
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	public void updatePenaltyRate(PenaltyRateRequest penaltyRateRequest);

}