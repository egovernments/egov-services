package org.egov.tradelicense.domain.services;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.CategoryRequest;
import org.egov.tl.commons.web.response.CategoryResponse;
import org.egov.tl.commons.web.response.CategorySearchResponse;

/**
 * Service class for Category master
 * 
 * @author Pavan Kumar Kamma
 *
 */
public interface CategoryService {

	/**
	 * Description : service method for creating category master
	 * 
	 * @param CategoryRequest
	 * @return CategoryResponse
	 */
	public CategoryResponse createCategoryMaster(CategoryRequest categoryRequest, String type);

	/**
	 * Description : service method for updating category master
	 * 
	 * @param CategoryRequest
	 * @return CategoryResponse
	 */
	public CategoryResponse updateCategoryMaster(CategoryRequest categoryRequest, String type);

	/**
	 * Description : service method for searching category master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param type
	 * @param categoryId
	 * @param pageSize
	 * @param offSet
	 * @return CategoryResponse
	 */
	public CategorySearchResponse getCategoryMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String active, String type, String businessNature, Integer categoryId, String rateType, String feeType, Integer uomId,
			Integer pageSize, Integer offSet);

	/**
	 * Description : service method for creating category master
	 * 
	 * @param CategoryRequest
	 * @return CategoryResponse
	 */
	public void createCategory(CategoryRequest categoryRequest);

	/**
	 * Description : service method for updating category master
	 * 
	 * @param CategoryRequest
	 * @return CategoryResponse
	 */
	public void updateCategory(CategoryRequest categoryRequest);
}