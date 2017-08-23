package org.egov.tl.indexer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.LicenseStatus;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.TradeLicenseSearchContract;
import org.egov.tl.commons.web.contract.UOM;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.CategoryResponse;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tl.commons.web.response.TradeLicenseSearchResponse;
import org.egov.tl.commons.web.response.UOMResponse;
import org.egov.tl.indexer.client.JestClientEs;
import org.egov.tl.indexer.config.PropertiesManager;
import org.egov.tl.indexer.web.contract.Boundary;
import org.egov.tl.indexer.web.repository.BoundaryContractRepository;
import org.egov.tl.indexer.web.repository.CategoryContractRepository;
import org.egov.tl.indexer.web.response.BoundaryResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;

/**
 * 
 * @author Shubham pratap singh
 *
 */
@Service
public class SearchService {

	@Autowired
	private JestClientEs jestClient;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	CategoryContractRepository categoryContractRepository;

	@Autowired
	BoundaryContractRepository boundaryContractRepository;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	public TradeLicenseSearchResponse searchFromEs(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, String tradeLicenseId, String applicationNumber,
			String licenseNumber, String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String emailId,
			String propertyAssesmentNo, Integer adminWard, Integer locality, String ownerName, String tradeTitle,
			String tradeType, Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		TradeLicenseSearchResponse tradeLicenseSearchResponse = new TradeLicenseSearchResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		tradeLicenseSearchResponse.setResponseInfo(responseInfo);

		List<TradeLicenseSearchContract> tlList = new ArrayList<TradeLicenseSearchContract>();

		BoolQueryBuilder builder = SearchUtil.buildSearchQuery(tenantId, active, tradeLicenseId, applicationNumber,
				licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber, emailId, propertyAssesmentNo, adminWard,
				locality, ownerName, tradeTitle, tradeType, tradeCategory, tradeSubCategory, legacy, status);

		if (builder != null) {
			searchSourceBuilder.query(builder);

			if (pageNumber != null)
				searchSourceBuilder.from(pageNumber);

			if (pageSize != null)
				searchSourceBuilder.size(pageSize);

			if (sort != null && !sort.isEmpty()) {
				searchSourceBuilder.sort("id", SortOrder.ASC);
			}

			Search search = (Search) new Search.Builder(searchSourceBuilder.toString())
					.addIndex(propertiesManager.getEsIndex()).addType(propertiesManager.getEsIndexType()).build();

			SearchResult searchresult = null;
			try {
				searchresult = jestClient.getClient().execute(search);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (searchresult != null && !searchresult.isSucceeded() && searchresult.getErrorMessage() != null) {
				tradeLicenseSearchResponse.setLicenses(tlList);
				return tradeLicenseSearchResponse;
			}
			List<Hit<TradeLicenseSearchContract, Void>> hits = searchresult.getHits(TradeLicenseSearchContract.class);

			for (SearchResult.Hit<TradeLicenseSearchContract, Void> hit : hits) {

				tlList.add(hit.source);
			}
			// identifying unique field ids and getting the unique fields map
			// with id and name
			if (tlList != null && tlList.size() > 0) {
				Map<String, Map<String, String>> uniqueFieldsMap = identifyDependencyFields(requestInfo, tlList);
				for (TradeLicenseSearchContract TradeLicenseSearchContract : tlList) {
					String categoryName = null, subCategoryName = null, uomName = null, statusName = null,
							localityName = null, adminWardName = null, revenueWardName = null;

					if (uniqueFieldsMap.get("categoryIdAndNameMap") != null) {
						categoryName = uniqueFieldsMap.get("categoryIdAndNameMap")
								.get(getString(TradeLicenseSearchContract.getCategoryId()));
					}
					if (uniqueFieldsMap.get("subCategoryIdAndNameMap") != null) {
						subCategoryName = uniqueFieldsMap.get("subCategoryIdAndNameMap")
								.get(getString(TradeLicenseSearchContract.getSubCategoryId()));
					}
					if (uniqueFieldsMap.get("uomIdAndNameMap") != null) {
						uomName = uniqueFieldsMap.get("uomIdAndNameMap")
								.get(getString(TradeLicenseSearchContract.getUomId()));
					}
					if (uniqueFieldsMap.get("statusIdAndNameMap") != null) {
						statusName = uniqueFieldsMap.get("statusIdAndNameMap")
								.get(getString(TradeLicenseSearchContract.getStatus()));
					}
					if (uniqueFieldsMap.get("localityIdAndNameMap") != null) {
						localityName = uniqueFieldsMap.get("localityIdAndNameMap")
								.get(getString(TradeLicenseSearchContract.getLocalityId()));
					}
					if (uniqueFieldsMap.get("adminWardIdAndNameMap") != null) {
						adminWardName = uniqueFieldsMap.get("adminWardIdAndNameMap")
								.get(getString(TradeLicenseSearchContract.getAdminWardId()));
					}
					if (uniqueFieldsMap.get("revenueWardIdAndNameMap") != null) {
						revenueWardName = uniqueFieldsMap.get("revenueWardIdAndNameMap")
								.get(getString(TradeLicenseSearchContract.getRevenueWardId()));
					}

					TradeLicenseSearchContract.setCategory(categoryName);
					TradeLicenseSearchContract.setSubCategory(subCategoryName);
					TradeLicenseSearchContract.setUom(uomName);
					TradeLicenseSearchContract.setStatusName(statusName);
					TradeLicenseSearchContract.setLocalityName(localityName);
					TradeLicenseSearchContract.setAdminWardName(adminWardName);
					TradeLicenseSearchContract.setRevenueWardName(revenueWardName);
				}
			}

			tradeLicenseSearchResponse.setLicenses(tlList);
		}

		return tradeLicenseSearchResponse;

	}

	private Map<String, Map<String, String>> identifyDependencyFields(RequestInfo requestInfo,
			List<TradeLicenseSearchContract> tradeLicenseSearchContracts) {

		// preparing request info wrapper for the rest api calls
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		// getting the list of unique field ids
		Map<String, List<Long>> uniqueIds = getDependentFieldsUniqueIds(tradeLicenseSearchContracts);
		// map holds all field maps
		Map<String, Map<String, String>> uniqueFieldsIdAndNameMap = new HashMap<String, Map<String, String>>();
		// map holds individual field maps with unique ids and corresponding
		// names
		Map<String, String> categoryIdAndNameMap = new HashMap<String, String>();
		Map<String, String> subCategoryIdAndNameMap = new HashMap<String, String>();
		Map<String, String> uomIdAndNameMap = new HashMap<String, String>();
		Map<String, String> statusIdAndNameMap = new HashMap<String, String>();
		Map<String, String> localityIdAndNameMap = new HashMap<String, String>();
		Map<String, String> adminWardIdAndNameMap = new HashMap<String, String>();
		Map<String, String> revenueWardIdAndNameMap = new HashMap<String, String>();

		String tenantId = tradeLicenseSearchContracts.get(0).getTenantId();
		// building category unique ids map
		if (uniqueIds.get("categoryIds") != null) {

			String ids = uniqueIds.get("categoryIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			CategoryResponse categoryResponse = categoryContractRepository.findByCategoryIds(tenantId, ids,
					requestInfoWrapper);
			if (categoryResponse != null && categoryResponse.getCategories() != null
					&& categoryResponse.getCategories().size() > 0) {

				for (Category category : categoryResponse.getCategories()) {
					categoryIdAndNameMap.put(category.getId().toString(), category.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("categoryIdAndNameMap", categoryIdAndNameMap);
		}
		// building sub category unique ids map
		if (uniqueIds.get("subCategoryIds") != null) {

			String ids = uniqueIds.get("subCategoryIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			CategoryResponse categoryResponse = categoryContractRepository.findByCategoryIds(tenantId, ids,
					requestInfoWrapper);
			if (categoryResponse != null && categoryResponse.getCategories() != null
					&& categoryResponse.getCategories().size() > 0) {

				for (Category category : categoryResponse.getCategories()) {
					subCategoryIdAndNameMap.put(category.getId().toString(), category.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("subCategoryIdAndNameMap", subCategoryIdAndNameMap);
		}
		// building uom unique ids map
		if (uniqueIds.get("uomIds") != null) {

			String ids = uniqueIds.get("uomIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			UOMResponse uomResponse = categoryContractRepository.findByUomIds(tenantId, ids, requestInfoWrapper);
			if (uomResponse != null && uomResponse.getUoms() != null && uomResponse.getUoms().size() > 0) {

				for (UOM uom : uomResponse.getUoms()) {
					uomIdAndNameMap.put(uom.getId().toString(), uom.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("uomIdAndNameMap", uomIdAndNameMap);
		}
		// building status unique ids map
		if (uniqueIds.get("statusIds") != null) {

			String ids = uniqueIds.get("statusIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			LicenseStatusResponse licenseStatusResponse = categoryContractRepository.findByStatusIds(tenantId, ids,
					requestInfoWrapper);
			if (licenseStatusResponse != null && licenseStatusResponse.getLicenseStatuses() != null
					&& licenseStatusResponse.getLicenseStatuses().size() > 0) {

				for (LicenseStatus licenseStatus : licenseStatusResponse.getLicenseStatuses()) {
					statusIdAndNameMap.put(licenseStatus.getId().toString(), licenseStatus.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("statusIdAndNameMap", statusIdAndNameMap);
		}
		// building locality unique ids map
		if (uniqueIds.get("localityIds") != null) {

			String ids = uniqueIds.get("localityIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			BoundaryResponse boundaryResponse = boundaryContractRepository.findByBoundaryIds(tenantId, ids,
					requestInfoWrapper);
			if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
					&& boundaryResponse.getBoundarys().size() > 0) {

				for (Boundary boundary : boundaryResponse.getBoundarys()) {
					localityIdAndNameMap.put(boundary.getId().toString(), boundary.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("localityIdAndNameMap", localityIdAndNameMap);
		}
		// building adminWard unique ids map
		if (uniqueIds.get("adminWardIds") != null) {

			String ids = uniqueIds.get("adminWardIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			BoundaryResponse boundaryResponse = boundaryContractRepository.findByBoundaryIds(tenantId, ids,
					requestInfoWrapper);
			if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
					&& boundaryResponse.getBoundarys().size() > 0) {

				for (Boundary boundary : boundaryResponse.getBoundarys()) {
					adminWardIdAndNameMap.put(boundary.getId().toString(), boundary.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("adminWardIdAndNameMap", adminWardIdAndNameMap);
		}
		// building revenueWard unique ids map
		if (uniqueIds.get("revenueWardIds") != null) {

			String ids = uniqueIds.get("revenueWardIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			BoundaryResponse boundaryResponse = boundaryContractRepository.findByBoundaryIds(tenantId, ids,
					requestInfoWrapper);
			if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
					&& boundaryResponse.getBoundarys().size() > 0) {

				for (Boundary boundary : boundaryResponse.getBoundarys()) {
					revenueWardIdAndNameMap.put(boundary.getId().toString(), boundary.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("revenueWardIdAndNameMap", revenueWardIdAndNameMap);
		}

		return uniqueFieldsIdAndNameMap;
	}

	private Map<String, List<Long>> getDependentFieldsUniqueIds(
			List<TradeLicenseSearchContract> tradeLicenseSearchContracts) {

		Map<String, List<Long>> uniqueIds = new HashMap<String, List<Long>>();
		List<Long> categoryIds = new ArrayList<>();
		List<Long> subCategoryIds = new ArrayList<>();
		List<Long> uomIds = new ArrayList<>();
		List<Long> statusIds = new ArrayList<>();
		List<Long> localityIds = new ArrayList<>();
		List<Long> adminWardIds = new ArrayList<>();
		List<Long> revenueWardIds = new ArrayList<>();

		for (TradeLicenseSearchContract tradeLicenseSearchContract : tradeLicenseSearchContracts) {

			if (tradeLicenseSearchContract != null) {

				Long categoryId = tradeLicenseSearchContract.getCategoryId();
				Long subCategoryId = tradeLicenseSearchContract.getSubCategoryId();
				Long uomId = tradeLicenseSearchContract.getUomId();
				Long statusId = tradeLicenseSearchContract.getStatus();
				Long localityId = Long.valueOf(tradeLicenseSearchContract.getLocalityId().toString());
				Long adminWardId = Long.valueOf(tradeLicenseSearchContract.getAdminWardId().toString());
				Long revenueWardId = Long.valueOf(tradeLicenseSearchContract.getRevenueWardId().toString());

				// category ids
				if (categoryIds.size() > 0) {
					if (categoryId != null && !categoryIds.contains(categoryId)) {
						categoryIds.add(categoryId);
					}
				} else if (categoryId != null) {
					categoryIds.add(categoryId);
				}
				// sub Category ids
				if (subCategoryIds.size() > 0) {
					if (subCategoryId != null && !subCategoryIds.contains(subCategoryId)) {
						subCategoryIds.add(subCategoryId);
					}
				} else if (subCategoryId != null) {
					subCategoryIds.add(subCategoryId);
				}
				// uom ids
				if (uomIds.size() > 0) {
					if (uomIds != null && !uomIds.contains(uomId)) {
						uomIds.add(uomId);
					}
				} else if (uomIds != null) {
					uomIds.add(uomId);
				}
				// status ids
				if (statusIds.size() > 0) {
					if (statusIds != null && !statusIds.contains(statusId)) {
						statusIds.add(statusId);
					}
				} else if (statusIds != null) {
					statusIds.add(statusId);
				}
				// locality ids
				if (localityIds.size() > 0) {
					if (localityIds != null && !localityIds.contains(localityId)) {
						localityIds.add(localityId);
					}
				} else if (localityIds != null) {
					localityIds.add(localityId);
				}
				// adminWard ids
				if (adminWardIds.size() > 0) {
					if (adminWardIds != null && !adminWardIds.contains(adminWardId)) {
						adminWardIds.add(adminWardId);
					}
				} else if (adminWardIds != null) {
					adminWardIds.add(adminWardId);
				}
				// revenueWard ids
				if (revenueWardIds.size() > 0) {
					if (revenueWardIds != null && !revenueWardIds.contains(revenueWardId)) {
						revenueWardIds.add(revenueWardId);
					}
				} else if (revenueWardIds != null) {
					revenueWardIds.add(revenueWardId);
				}
			}

		}
		if (categoryIds.size() > 0) {
			uniqueIds.put("categoryIds", categoryIds);
		}
		if (subCategoryIds.size() > 0) {
			uniqueIds.put("subCategoryIds", subCategoryIds);
		}
		if (uomIds.size() > 0) {
			uniqueIds.put("uomIds", uomIds);
		}
		if (statusIds.size() > 0) {
			uniqueIds.put("statusIds", statusIds);
		}
		if (localityIds.size() > 0) {
			uniqueIds.put("localityIds", localityIds);
		}
		if (adminWardIds.size() > 0) {
			uniqueIds.put("adminWardIds", adminWardIds);
		}
		if (revenueWardIds.size() > 0) {
			uniqueIds.put("revenueWardIds", revenueWardIds);
		}

		return uniqueIds;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? "" : object.toString();
	}
}
