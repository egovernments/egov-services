package org.egov.tl.indexer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.LicenseFeeDetailContract;
import org.egov.tl.commons.web.contract.LicenseStatus;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.SupportDocumentSearchContract;
import org.egov.tl.commons.web.contract.TradeLicenseSearchContract;
import org.egov.tl.commons.web.contract.UOM;
import org.egov.tl.commons.web.requests.DocumentTypeV2Response;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.CategoryResponse;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tl.commons.web.response.TradeLicenseSearchResponse;
import org.egov.tl.commons.web.response.UOMResponse;
import org.egov.tl.indexer.client.JestClientEs;
import org.egov.tl.indexer.config.PropertiesManager;
import org.egov.tl.indexer.web.contract.Boundary;
import org.egov.tl.indexer.web.contract.FinancialYearContract;
import org.egov.tl.indexer.web.repository.BoundaryContractRepository;
import org.egov.tl.indexer.web.repository.CategoryContractRepository;
import org.egov.tl.indexer.web.repository.DocumentTypeContractRepository;
import org.egov.tl.indexer.web.repository.FinancialYearContractRepository;
import org.egov.tl.indexer.web.response.BoundaryResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Shubham pratap singh
 *
 */
@Service
@Slf4j
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

	@Autowired
	FinancialYearContractRepository financialYearRepository;

	@Autowired
	DocumentTypeContractRepository documentTypeRepository;

	public TradeLicenseSearchResponse searchFromEs(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, String tradeLicenseId, String applicationNumber,
			String licenseNumber, String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String emailId,
			String propertyAssesmentNo, Integer adminWard, Integer locality, String ownerName, String tradeTitle,
			String tradeType, Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		TradeLicenseSearchResponse tradeLicenseSearchResponse = new TradeLicenseSearchResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		tradeLicenseSearchResponse.setResponseInfo(responseInfo);

		// preparing request info wrapper for the rest api calls
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

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
				log.error("error executing elastic client search");
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

				for (TradeLicenseSearchContract tradeLicenseSearchContract : tlList) {
					String categoryName = null, subCategoryName = null, uomName = null, statusName = null,
							localityName = null, adminWardName = null, revenueWardName = null;

					if (uniqueFieldsMap.get("categoryCodeAndNameMap") != null) {
						categoryName = uniqueFieldsMap.get("categoryCodeAndNameMap")
								.get(getString(tradeLicenseSearchContract.getCategory()));
					}
					if (uniqueFieldsMap.get("subCategoryCodeAndNameMap") != null) {
						subCategoryName = uniqueFieldsMap.get("subCategoryCodeAndNameMap")
								.get(getString(tradeLicenseSearchContract.getSubCategory()));
					}
					if (uniqueFieldsMap.get("uomCodeAndNameMap") != null) {
						uomName = uniqueFieldsMap.get("uomCodeAndNameMap")
								.get(getString(tradeLicenseSearchContract.getUom()));
					}
					if (uniqueFieldsMap.get("statusCodeAndNameMap") != null) {
						statusName = uniqueFieldsMap.get("statusCodeAndNameMap")
								.get(getString(tradeLicenseSearchContract.getStatus()));
					}
					if (uniqueFieldsMap.get("localityIdAndNameMap") != null) {
						localityName = uniqueFieldsMap.get("localityIdAndNameMap")
								.get(getString(tradeLicenseSearchContract.getLocalityId()));
					}
					if (uniqueFieldsMap.get("adminWardIdAndNameMap") != null) {
						adminWardName = uniqueFieldsMap.get("adminWardIdAndNameMap")
								.get(getString(tradeLicenseSearchContract.getAdminWardId()));
					}
					if (uniqueFieldsMap.get("revenueWardIdAndNameMap") != null) {
						revenueWardName = uniqueFieldsMap.get("revenueWardIdAndNameMap")
								.get(getString(tradeLicenseSearchContract.getRevenueWardId()));
					}

					tradeLicenseSearchContract.setCategory(categoryName);
					tradeLicenseSearchContract.setSubCategory(subCategoryName);
					tradeLicenseSearchContract.setUom(uomName);
					tradeLicenseSearchContract.setStatusName(statusName);
					tradeLicenseSearchContract.setLocalityName(localityName);
					tradeLicenseSearchContract.setAdminWardName(adminWardName);
					tradeLicenseSearchContract.setRevenueWardName(revenueWardName);

					// set the fee detail financial year range from the id
					FinancialYearContract finYearContract;
					for (LicenseFeeDetailContract licenseFeeDetailContract : tradeLicenseSearchContract
							.getFeeDetails()) {

						finYearContract = financialYearRepository.findFinancialYearById(
								tradeLicenseSearchContract.getTenantId(), licenseFeeDetailContract.getFinancialYear(),
								requestInfoWrapper);
						if (finYearContract != null) {
							licenseFeeDetailContract.setFinancialYear(finYearContract.getFinYearRange());
						}
					}

					// set document name from the document id
					for (SupportDocumentSearchContract supportDocumentSearchContract : tradeLicenseSearchContract
							.getSupportDocuments()) {

						DocumentTypeV2Response documentTypeResponse;
						documentTypeResponse = documentTypeRepository.findById(requestInfoWrapper,
								tradeLicenseSearchContract.getTenantId(),
								supportDocumentSearchContract.getDocumentTypeId());

						if (documentTypeResponse != null && documentTypeResponse.getDocumentTypes().size() > 0) {
							supportDocumentSearchContract
									.setDocumentTypeName(documentTypeResponse.getDocumentTypes().get(0).getName());
						}
					}
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
		Map<String, List<String>> uniqueIds = getDependentFieldsUniqueIds(tradeLicenseSearchContracts);
		// map holds all field maps
		Map<String, Map<String, String>> uniqueFieldsIdAndNameMap = new HashMap<String, Map<String, String>>();
		// map holds individual field maps with unique codes and corresponding
		// names
		Map<String, String> categoryCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> subCategoryCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> uomCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> statusCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> localityIdAndNameMap = new HashMap<String, String>();
		Map<String, String> adminWardIdAndNameMap = new HashMap<String, String>();
		Map<String, String> revenueWardIdAndNameMap = new HashMap<String, String>();
		String tenantId = null;

		if (tradeLicenseSearchContracts != null && tradeLicenseSearchContracts.size() > 0) {
			tenantId = tradeLicenseSearchContracts.get(0).getTenantId();
		}

		// building category unique codes map
				if (uniqueIds.get("categoryCodes") != null) {

					String codes = uniqueIds.get("categoryCodes").toString();
					codes = codes.replace("[", "").replace("]", "");
					CategorySearchResponse categoryResponse = categoryContractRepository.findByCategoryCodes(tenantId, codes,
							requestInfoWrapper);
					if (categoryResponse != null && categoryResponse.getCategories() != null
							&& categoryResponse.getCategories().size() > 0) {

						for (CategorySearch category : categoryResponse.getCategories()) {
							categoryCodeAndNameMap.put(category.getCode().toString(), category.getName());
						}

					}
					uniqueFieldsIdAndNameMap.put("categoryCodeAndNameMap", categoryCodeAndNameMap);
				}
				// building sub category unique codes map
				if (uniqueIds.get("subCategoryCodes") != null) {

					String codes = uniqueIds.get("subCategoryCodes").toString();
					codes = codes.replace("[", "").replace("]", "");
					CategorySearchResponse categoryResponse = categoryContractRepository.findBySubCategoryCodes(tenantId, codes,
							requestInfoWrapper);
					if (categoryResponse != null && categoryResponse.getCategories() != null
							&& categoryResponse.getCategories().size() > 0) {

						for (CategorySearch category : categoryResponse.getCategories()) {
							subCategoryCodeAndNameMap.put(category.getCode().toString(), category.getName());
						}

					}
					uniqueFieldsIdAndNameMap.put("subCategoryCodeAndNameMap", subCategoryCodeAndNameMap);
				}
				// building uom unique codes map
				if (uniqueIds.get("uomCodes") != null) {

					String codes = uniqueIds.get("uomCodes").toString();
					codes = codes.replace("[", "").replace("]", "");
					UOMResponse uomResponse = categoryContractRepository.findByUomCodes(tenantId, codes, requestInfoWrapper);
					if (uomResponse != null && uomResponse.getUoms() != null && uomResponse.getUoms().size() > 0) {

						for (UOM uom : uomResponse.getUoms()) {
							uomCodeAndNameMap.put(uom.getCode().toString(), uom.getName());
						}

					}
					uniqueFieldsIdAndNameMap.put("uomCodeAndNameMap", uomCodeAndNameMap);
				}
				// building status unique codes map
				if (uniqueIds.get("statusCodes") != null) {

					String codes = uniqueIds.get("statusCodes").toString();
					codes = codes.replace("[", "").replace("]", "");
					LicenseStatusResponse licenseStatusResponse = categoryContractRepository.findByStatusCodes(tenantId, codes, requestInfoWrapper);
					if (licenseStatusResponse != null && licenseStatusResponse.getLicenseStatuses() != null
							&& licenseStatusResponse.getLicenseStatuses().size() > 0) {

						for (LicenseStatus licenseStatus : licenseStatusResponse.getLicenseStatuses()) {
							statusCodeAndNameMap.put(licenseStatus.getCode().toString(), licenseStatus.getName());
						}

					}
					uniqueFieldsIdAndNameMap.put("statusCodeAndNameMap", statusCodeAndNameMap);
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

	private Map<String, List<String>> getDependentFieldsUniqueIds(
			List<TradeLicenseSearchContract> tradeLicenseSearchContracts) {

		Map<String, List<String>> uniqueIds = new HashMap<String, List<String>>();
		List<String> categoryCodes = new ArrayList<>();
		List<String> subCategoryCodes = new ArrayList<>();
		List<String> uomCodes = new ArrayList<>();
		List<String> statusCodes = new ArrayList<>();
		List<String> localityIds = new ArrayList<>();
		List<String> adminWardIds = new ArrayList<>();
		List<String> revenueWardIds = new ArrayList<>();

		for (TradeLicenseSearchContract tradeLicenseSearchContract : tradeLicenseSearchContracts) {

			if (tradeLicenseSearchContract != null) {

				String category = tradeLicenseSearchContract.getCategory();
				String subCategory = tradeLicenseSearchContract.getSubCategory();
				String uom = tradeLicenseSearchContract.getUom();
				String status = tradeLicenseSearchContract.getStatus();
				String localityId = tradeLicenseSearchContract.getLocalityId().toString();
				String adminWardId = tradeLicenseSearchContract.getAdminWardId().toString();
				String revenueWardId = tradeLicenseSearchContract.getRevenueWardId().toString();

				// category codes
				if (categoryCodes.size() > 0) {
					if (category != null && !categoryCodes.contains(category)) {
						categoryCodes.add(category);
					}
				} else if (category != null) {
					categoryCodes.add(category);
				}
				// sub Category codes
				if (subCategoryCodes.size() > 0) {
					if (subCategory != null && !subCategoryCodes.contains(subCategory)) {
						subCategoryCodes.add(subCategory);
					}
				} else if (subCategory != null) {
					subCategoryCodes.add(subCategory);
				}
				// uom codes
				if (uomCodes.size() > 0) {
					if (uom != null && !uomCodes.contains(uom)) {
						uomCodes.add(uom);
					}
				} else if (uom != null) {
					uomCodes.add(uom);
				}
				// status codes
				if (statusCodes.size() > 0) {
					if (status != null && !statusCodes.contains(status)) {
						statusCodes.add(status);
					}
				} else if (status != null) {
					statusCodes.add(status);
				}
				// locality ids
				if (localityIds.size() > 0) {
					if (localityId != null && !localityIds.contains(localityId)) {
						localityIds.add(localityId);
					}
				} else if (localityId != null) {
					localityIds.add(localityId);
				}
				// adminWard ids
				if (adminWardIds.size() > 0) {
					if (adminWardId != null && !adminWardIds.contains(adminWardId)) {
						adminWardIds.add(adminWardId);
					}
				} else if (adminWardId != null) {
					adminWardIds.add(adminWardId);
				}
				// revenueWard ids
				if (revenueWardIds.size() > 0) {
					if (revenueWardId != null && !revenueWardIds.contains(revenueWardId)) {
						revenueWardIds.add(revenueWardId);
					}
				} else if (revenueWardId != null) {
					revenueWardIds.add(revenueWardId);
				}
			}
			
			if (categoryCodes.size() > 0) {
				uniqueIds.put("categoryCodes", categoryCodes);
			}
			if (subCategoryCodes.size() > 0) {
				uniqueIds.put("subCategoryCodes", subCategoryCodes);
			}
			if (uomCodes.size() > 0) {
				uniqueIds.put("uomCodes", uomCodes);
			}
			if (statusCodes.size() > 0) {
				uniqueIds.put("statusCodes", statusCodes);
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
