package org.egov.tradelicense.services;

import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.Category;
import org.egov.models.CategoryDetail;
import org.egov.models.CategoryRequest;
import org.egov.models.CategoryResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.exception.DuplicateIdException;
import org.egov.tradelicense.exception.InvalidInputException;
import org.egov.tradelicense.repository.CategoryRepository;
import org.egov.tradelicense.repository.helper.CategoryHelper;
import org.egov.tradelicense.repository.helper.UtilityHelper;
import org.egov.tradelicense.utility.ConstantUtility;
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
	CategoryHelper categoryHelper;

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	private PropertiesManager propertiesManager;

	@Override
	@Transactional
	public CategoryResponse createCategoryMaster(CategoryRequest categoryRequest) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();
		AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);
		for (Category category : categoryRequest.getCategories()) {

			Long ParentId = category.getParentId();
			Long categoryId = null;
			// checking for existence of duplicate record
			Boolean isExists = utilityHelper.checkWhetherDuplicateRecordExits(category.getTenantId(),
					category.getCode(), ConstantUtility.CATEGORY_TABLE_NAME, null);

			if (isExists) {
				throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
			}

			try {

				category.setAuditDetails(auditDetails);
				categoryId = categoryRepository.createCategory(category);
				category.setId(categoryId);

			} catch (Exception e) {

				throw new InvalidInputException(requestInfo);
			}

			if (ParentId != null) {

				Boolean isParentExists = categoryHelper.checkWhetherParentRecordExits(category.getParentId(),
						ConstantUtility.CATEGORY_TABLE_NAME);

				if (isParentExists) {

					try {

						for (CategoryDetail categoryDetail : category.getDetails()) {

							categoryDetail.setCategoryId(categoryId);
							Boolean isCategoryDetailExists = categoryHelper
									.checkWhetherDuplicateCategoryDetailRecordExits(categoryDetail,
											ConstantUtility.CATEGORY_DETAIL_TABLE_NAME, null);

							if (isCategoryDetailExists) {
								throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
							}

							Boolean isUomExists = categoryHelper.checkWhetherUomExists(categoryDetail);

							if (!isUomExists) {

								throw new InvalidInputException(requestInfo);
							}

							Long categoryDetailId = categoryRepository.createCategoryDetail(categoryDetail);
							categoryDetail.setId(categoryDetailId);
						}
					} catch (Exception e) {
						throw new InvalidInputException(requestInfo);
					}
				} else {
					throw new InvalidInputException(requestInfo);
				}
			}
		}

		CategoryResponse categoryResponse = new CategoryResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		categoryResponse.setCategories(categoryRequest.getCategories());
		categoryResponse.setResponseInfo(responseInfo);

		return categoryResponse;
	}

	@Override
	@Transactional
	public CategoryResponse updateCategoryMaster(CategoryRequest categoryRequest) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();

		for (Category category : categoryRequest.getCategories()) {

			Long ParentId = category.getParentId();
			Long categoryId = category.getId();

			if (categoryId == null) {
				throw new InvalidInputException(requestInfo);
			}

			Boolean isExists = utilityHelper.checkWhetherDuplicateRecordExits(category.getTenantId(),
					category.getCode(), ConstantUtility.CATEGORY_TABLE_NAME, categoryId);

			if (isExists) {
				throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
			}

			if (ParentId != null) {

				Boolean isParentExists = categoryHelper.checkWhetherParentRecordExits(category.getParentId(),
						ConstantUtility.CATEGORY_TABLE_NAME);

				if (isParentExists) {

					for (CategoryDetail categoryDetail : category.getDetails()) {

						Boolean isCategoryDetailExists = categoryHelper.checkWhetherDuplicateCategoryDetailRecordExits(
								categoryDetail, ConstantUtility.CATEGORY_DETAIL_TABLE_NAME, categoryDetail.getId());

						if (isCategoryDetailExists) {

							throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
						}
						Boolean isUomExists = categoryHelper.checkWhetherUomExists(categoryDetail);

						if (!isUomExists) {

							throw new InvalidInputException(requestInfo);
						}
						try {

							categoryDetail = categoryRepository.updateCategoryDetail(categoryDetail);
						} catch (Exception e) {

							throw new InvalidInputException(requestInfo);
						}
					}

				} else {

					throw new InvalidInputException(requestInfo);
				}
			}

			try {

				AuditDetails auditDetails = category.getAuditDetails();
				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
				category.setAuditDetails(auditDetails);
				category = categoryRepository.updateCategory(category);

			} catch (Exception e) {

				throw new InvalidInputException(requestInfo);
			}
		}

		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setCategories(categoryRequest.getCategories());
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		categoryResponse.setResponseInfo(responseInfo);

		return categoryResponse;
	}

	@Override
	public CategoryResponse getCategoryMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String type, Integer categoryId, Integer pageSize, Integer offSet) {

		CategoryResponse categoryResponse = new CategoryResponse();
		try {

			List<Category> categories = categoryRepository.searchCategory(tenantId, ids, name, code, type, categoryId,
					pageSize, offSet);

			if (type != null && !type.isEmpty() && type.equalsIgnoreCase("SUBCATEGORY") || (categoryId != null)) {

				for (int i = 0; i < categories.size(); i++) {

					Category category = categories.get(i);
					Long ParentId = category.getParentId();

					if (ParentId != null) {

						Boolean isParentExists = categoryHelper.checkWhetherParentRecordExits(ParentId,
								ConstantUtility.CATEGORY_TABLE_NAME);

						if (isParentExists) {

							List<CategoryDetail> categoryDetails = categoryRepository
									.getCategoryDetailsByCategoryId(category.getId(), pageSize, offSet);

							category.setDetails(categoryDetails);

						} else {
							throw new InvalidInputException(requestInfo);
						}
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