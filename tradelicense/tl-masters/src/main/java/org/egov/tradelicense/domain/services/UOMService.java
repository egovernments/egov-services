package org.egov.tradelicense.domain.services;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.UOMRequest;
import org.egov.tl.commons.web.response.UOMResponse;

/**
 * Service class for UOM master
 * 
 * @author Pavan Kumar Kamma
 *
 */
public interface UOMService {

	/**
	 * Description : service method for creating UOM master
	 * 
	 * @param tenantId
	 * @param UOMRequest
	 * @return UOMResponse
	 */
	public UOMResponse createUomMaster(UOMRequest uomRequest);

	/**
	 * Description : service method for updating UOM master 
	 * @param UOMRequest
	 * @return UOMResponse
	 */
	public UOMResponse updateUomMaster(UOMRequest uomRequest);

	/**
	 * Description : service method for searching UOM master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param active
	 * @param pageSize
	 * @param offSet
	 * @return UOMResponse
	 * @throws Exception
	 */
	public UOMResponse getUomMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name, String[] codes,
			String active, Integer pageSize, Integer offSet);

	/**
	 * Description : service method for creating UOM master
	 * 
	 * @param tenantId
	 * @param UOMRequest
	 * @return UOMResponse
	 */
	public void createUom(UOMRequest uomRequest);

	/**
	 * Description : service method for updating UOM master
	 * 
	 * @param UOMRequest
	 * @return UOMResponse
	 */
	public void updateUom(UOMRequest uomRequest);

}