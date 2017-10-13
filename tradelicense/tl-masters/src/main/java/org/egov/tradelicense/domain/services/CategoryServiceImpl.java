package org.egov.tradelicense.domain.services;

import java.util.List;

import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.CategoryDetail;
import org.egov.tl.commons.web.contract.CategoryDetailSearch;
import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.CategoryRequest;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.CategoryResponse;
import org.egov.tl.commons.web.response.CategorySearchResponse;
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
	private Producer producer;

	@Override
	@Transactional
	public CategoryResponse createCategoryMaster(CategoryRequest categoryRequest, String type) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();
		categoryValidator.validateCategoryRequest(categoryRequest, true, type);
		producer.send(propertiesManager.getCreateCategoryValidated(), categoryRequest);
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

				categoryRepository.createCategory(category);

				if (category.getParent() != null && category.getDetails() != null) {

					for (CategoryDetail categoryDetail : category.getDetails()) {

						categoryDetail.setCategory(category.getCode());
						Long categoryDetailId = categoryRepository.createCategoryDetail(categoryDetail);
						categoryDetail.setId(categoryDetailId);
					}
				}

			} catch (Exception e) {

				throw new InvalidInputException(e.getMessage(), requestInfo);
			}
		}
	}

	@Override
	@Transactional
	public CategoryResponse updateCategoryMaster(CategoryRequest categoryRequest, String type) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();
		categoryValidator.validateCategoryRequest(categoryRequest, false, type);
		producer.send(propertiesManager.getUpdateCategoryValidated(), categoryRequest);
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

				if (category.getParent() != null && category.getDetails() != null) {

					for (CategoryDetail categoryDetail : category.getDetails()) {
						if (categoryDetail.getId() != null) {

							categoryRepository.updateCategoryDetail(categoryDetail);

						} else {

							categoryRepository.createCategoryDetail(categoryDetail);

						}
					}
				}

			} catch (Exception e) {

				throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
			}
		}
	}

	@Override
	public CategorySearchResponse getCategoryMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String[] codes, String name,  String active, String type, String businessNature, String category,
			String rateType, String feeType, String uom, Integer pageSize, Integer offSet) {

		CategorySearchResponse categoryResponse = new CategorySearchResponse();
		try {

			List<CategorySearch> categories = categoryRepository.searchCategory(tenantId, ids, codes, name,  active, type,
					businessNature, category, rateType, feeType, uom, pageSize, offSet);

			for (int i = 0; i < categories.size(); i++) {

				CategorySearch categoryObj = categories.get(i);
				String parent = categoryObj.getParent();

				if (parent != null) {

					List<CategoryDetailSearch> categoryDetails = categoryRepository
							.getCategoryDetailsByCategoryId(categoryObj.getCode(), pageSize, offSet);

					categoryObj.setDetails(categoryDetails);
				}
			}

			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			categoryResponse.setCategories(categories);
			categoryResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
		}

		return categoryResponse;

	}
}