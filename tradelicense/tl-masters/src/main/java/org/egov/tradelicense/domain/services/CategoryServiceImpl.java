package org.egov.tradelicense.domain.services;

import java.util.List;

import org.egov.models.Category;
import org.egov.models.CategoryDetail;
import org.egov.models.CategoryRequest;
import org.egov.models.CategoryResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.services.validator.CategoryValidator;
import org.egov.tradelicense.persistence.repository.CategoryRepository;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.producers.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CategoryService implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CategoryValidator categoryValidator;

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private Producer Producer;

	@Override
	@Transactional
	public CategoryResponse createCategoryMaster(CategoryRequest categoryRequest) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();
		categoryValidator.validateCategoryRequest(categoryRequest, true);
		Producer.send(propertiesManager.getCreateCategoryValidated(), categoryRequest);
		CategoryResponse categoryResponse = new CategoryResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		categoryResponse.setCategories(categoryRequest.getCategories());
		categoryResponse.setResponseInfo(responseInfo);

		return categoryResponse;
	}

	@Override
	@Transactional
	public void createCategory(CategoryRequest categoryRequest) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();

		for (Category category : categoryRequest.getCategories()) {

			try {

				Long categoryId = categoryRepository.createCategory(category);

				if (category.getParentId() != null) {

					for (CategoryDetail categoryDetail : category.getDetails()) {

						categoryDetail.setCategoryId(categoryId);
						Long categoryDetailId = categoryRepository.createCategoryDetail(categoryDetail);
						categoryDetail.setId(categoryDetailId);
					}
				}

			} catch (Exception e) {

				throw new InvalidInputException(requestInfo);
			}
		}
	}

	@Override
	@Transactional
	public CategoryResponse updateCategoryMaster(CategoryRequest categoryRequest) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();
		categoryValidator.validateCategoryRequest(categoryRequest, false);
		Producer.send(propertiesManager.getUpdateCategoryValidated(), categoryRequest);
		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setCategories(categoryRequest.getCategories());
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		categoryResponse.setResponseInfo(responseInfo);

		return categoryResponse;
	}

	@Override
	@Transactional
	public void updateCategory(CategoryRequest categoryRequest) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();

		for (Category category : categoryRequest.getCategories()) {

			try {

				categoryRepository.updateCategory(category);

				if (category.getParentId() != null) {

					for (CategoryDetail categoryDetail : category.getDetails()) {

						categoryRepository.updateCategoryDetail(categoryDetail);
					}
				}

			} catch (Exception e) {

				throw new InvalidInputException(requestInfo);
			}
		}
	}

	@Override
	public CategoryResponse getCategoryMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String active, String type, Integer categoryId, Integer pageSize, Integer offSet) {

		CategoryResponse categoryResponse = new CategoryResponse();
		try {

			List<Category> categories = categoryRepository.searchCategory(tenantId, ids, name, code, active, type,
					categoryId, pageSize, offSet);

			if (type != null && !type.isEmpty() && type.equalsIgnoreCase("SUBCATEGORY") || (categoryId != null)) {

				for (int i = 0; i < categories.size(); i++) {

					Category category = categories.get(i);
					Long ParentId = category.getParentId();

					if (ParentId != null) {

						List<CategoryDetail> categoryDetails = categoryRepository
								.getCategoryDetailsByCategoryId(category.getId(), pageSize, offSet);

						category.setDetails(categoryDetails);
					}
				}
			}

			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			categoryResponse.setCategories(categories);
			categoryResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		return categoryResponse;

	}
}