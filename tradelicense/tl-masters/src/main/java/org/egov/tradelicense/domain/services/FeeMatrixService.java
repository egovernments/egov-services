package org.egov.tradelicense.domain.services;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.commons.web.response.FeeMatrixResponse;

/**
 * Service class for FeeMatrix master
 * 
 * @author Pavan Kumar Kamma
 */
public interface FeeMatrixService {

	/**
	 * Description : service method for creating feeMatrix master
	 * 
	 * 
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 */
	public FeeMatrixResponse createFeeMatrixMaster(FeeMatrixRequest feeMatrixRequest);

	/**
	 * Description : service method for updating feeMatrix master
	 * 
	 * 
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 */
	public FeeMatrixResponse updateFeeMatrixMaster(FeeMatrixRequest feeMatrixRequest);

	/**
	 * Description : This api for searching feeMatrix master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param categoryId
	 * @param subCategoryId
	 * @param financialYear
	 * @param applicationType
	 * @param businessNature
	 * @param pageSize
	 * @param offSet
	 * @return FeeMatrixResponse
	 */
	public FeeMatrixResponse getFeeMatrixMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			Integer categoryId, Integer subCategoryId, String financialYear, String applicationType,
			String businessNature, Integer pageSize, Integer offSet);

	/**
	 * Description : service method for creating feeMatrix master
	 * 
	 * 
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 */
	public void persistNewFeeMatrix(FeeMatrixRequest objectReceived);

	/**
	 * Description : service method for updating feeMatrix master
	 * 
	 * 
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 */
	public void persistUpdatedFeeMatrix(FeeMatrixRequest objectReceived);
}