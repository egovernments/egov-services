package org.egov.tradelicense.services;

import org.egov.models.FeeMatrixRequest;
import org.egov.models.FeeMatrixResponse;
import org.egov.models.RequestInfo;

/**
 * Service class for FeeMatrix master
 * 
 * @author Pavan Kumar Kamma
 */
public interface FeeMatrixService {

	/**
	 * Description : service method for creating feeMatrix master
	 * 
	 * @param tenantId
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 */
	public FeeMatrixResponse createFeeMatrixMaster(String tenantId, FeeMatrixRequest feeMatrixRequest);

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
}