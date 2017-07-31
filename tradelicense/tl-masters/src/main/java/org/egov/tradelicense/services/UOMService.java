package org.egov.tradelicense.services;

import org.egov.models.RequestInfo;
import org.egov.models.UOMRequest;
import org.egov.models.UOMResponse;

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
	public UOMResponse createUomMaster( UOMRequest uomRequest);

	/**
	 * Description : service method for updating UOM master
	 * 
	 * 
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
	public UOMResponse getUomMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name, String code,
			Boolean active, Integer pageSize, Integer offSet);

}