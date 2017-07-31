package org.egov.tradelicense.services;

import org.egov.models.CategoryRequest;
import org.egov.models.CategoryResponse;
import org.egov.models.RequestInfo;

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
	public CategoryResponse createCategoryMaster(CategoryRequest categoryRequest);

	/**
	 * Description : service method for updating category master
	 * 
	 * @param CategoryRequest
	 * @return CategoryResponse
	 */
	public CategoryResponse updateCategoryMaster(CategoryRequest categoryRequest);

	/**
	 * Description : service method for searching category master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param type
	 * @param pageSize
	 * @param offSet
	 * @return CategoryResponse
	 */
	public CategoryResponse getCategoryMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String type, Integer pageSize, Integer offSet);
}