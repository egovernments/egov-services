package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.LicenseSearchContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.contract.TradeLicenseIndexerContract;
import org.egov.tl.commons.web.contract.TradeLicenseSearchContract;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.TradeLicenseIndexerRequest;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.TradeLicenseSearchResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.LicenseSearch;
import org.egov.tradelicense.persistence.entity.TradeLicenseSearchEntity;
import org.egov.tradelicense.persistence.repository.TradeLicenseJdbcRepository;
import org.egov.tradelicense.web.repository.TenantContractRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TradeLicenseESRepository {

	@Autowired
	private TradeLicenseJdbcRepository tradeLicenseJdbcRepository;

	@Autowired
	private TenantContractRepository tenantWebContract;

	@Autowired
	PropertiesManager propertiesManager;
	
	private RestTemplate restTemplate;

	public TradeLicenseESRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<TradeLicenseSearchContract> search(RequestInfo requestInfo, LicenseSearchContract licenseSearchContract) {
		
		StringBuilder url = new StringBuilder(propertiesManager.getTradeLicenseIndexerServiceHostName());
		url.append(propertiesManager.getTradeLicenseIndexerServiceBasePath());
		url.append(propertiesManager.getTradeLicenseIndexerLicenseSearchPath());
		final BoolQueryBuilder BoolQueryBuilder = getSearchRequest(licenseSearchContract);
		TradeLicenseSearchResponse tradeLicenseSearchResponse = new TradeLicenseSearchResponse();
		
		try {

			tradeLicenseSearchResponse = restTemplate.postForObject(url.toString(), BoolQueryBuilder,
					TradeLicenseSearchResponse.class);

		} catch (Exception e) {
			System.out.println("Error while connecting to the Tl-Indexer end point");
		}
		
		return tradeLicenseSearchResponse.getLicenses();
	}

	private BoolQueryBuilder getSearchRequest(LicenseSearchContract criteria) {

		final BoolQueryBuilder bool = QueryBuilders.boolQuery();

		if (criteria.getTenantId() != null && !criteria.getTenantId().isEmpty()) {

			bool.must(QueryBuilders.termQuery("tenantId", criteria.getTenantId().trim()));
		}

		if (criteria.getActive() != null && !criteria.getActive().isEmpty()) {

			Boolean isactive = Boolean.FALSE;
			if (criteria.getActive().equalsIgnoreCase("true")) {
				isactive = Boolean.TRUE;
			} else if (criteria.getActive().equalsIgnoreCase("false")) {
				isactive = Boolean.FALSE;
			}

			bool.must(QueryBuilders.termQuery("active", isactive));
		}

		if (criteria.getIds() != null)
			bool.must(QueryBuilders.termQuery("id", criteria.getIds()));

		if (criteria.getApplicationNumber() != null && !criteria.getApplicationNumber().isEmpty())
			bool.must(QueryBuilders.termQuery("applicationNumber", criteria.getApplicationNumber()));

		if (criteria.getOldLicenseNumber() != null && !criteria.getOldLicenseNumber().isEmpty())
			bool.must(QueryBuilders.termQuery("oldLicenseNumber", criteria.getOldLicenseNumber()));

		if (criteria.getLicenseNumber() != null && !criteria.getLicenseNumber().isEmpty())
			bool.must(QueryBuilders.termQuery("licenseNumber", criteria.getLicenseNumber()));

		if (criteria.getMobileNumber() != null && !criteria.getMobileNumber().isEmpty())
			bool.must(QueryBuilders.termQuery("mobileNumber", criteria.getMobileNumber()));

		if (criteria.getAadhaarNumber() != null && !criteria.getAadhaarNumber().isEmpty())
			bool.must(QueryBuilders.termQuery("adhaarNumber", criteria.getAadhaarNumber()));

		if (criteria.getEmailId() != null && !criteria.getEmailId().isEmpty())
			bool.must(QueryBuilders.termQuery("emailId", criteria.getEmailId().trim()));

		if (criteria.getPropertyAssesmentNo() != null && !criteria.getPropertyAssesmentNo().isEmpty())
			bool.must(QueryBuilders.termQuery("propertyAssesmentNo", criteria.getPropertyAssesmentNo()));

		if (criteria.getAdminWard() != null)
			bool.must(QueryBuilders.termQuery("adminWard", criteria.getAdminWard()));

		if (criteria.getLocality() != null)
			bool.must(QueryBuilders.termQuery("localityId", criteria.getLocality()));

		if (criteria.getOwnerName() != null && !criteria.getOwnerName().isEmpty())
			bool.must(QueryBuilders.termQuery("ownerName", criteria.getOwnerName()));

		if (criteria.getTradeTitle() != null && !criteria.getTradeTitle().isEmpty())
			bool.must(QueryBuilders.termQuery("tradeTitle", criteria.getTradeTitle()));

		if (criteria.getTradeType() != null && !criteria.getTradeType().isEmpty())
			bool.must(QueryBuilders.termQuery("tradeType", criteria.getTradeType()));

		if (criteria.getTradeCategory() != null)
			bool.must(QueryBuilders.termQuery("categoryId", criteria.getTradeCategory()));

		if (criteria.getTradeSubCategory() != null)
			bool.must(QueryBuilders.termQuery("subCategoryId", criteria.getTradeSubCategory()));

		if (criteria.getLegacy() != null && !criteria.getLegacy().isEmpty()) {

			Boolean isLegacy = Boolean.FALSE;

			if (criteria.getLegacy().equalsIgnoreCase("true")) {
				isLegacy = Boolean.TRUE;
			} else if (criteria.getLegacy().equalsIgnoreCase("false")) {
				isLegacy = Boolean.FALSE;
			}

			bool.must(QueryBuilders.termQuery("isLegacy", isLegacy));
		}

		if (criteria.getStatus() != null) {
			bool.must(QueryBuilders.termQuery("status", criteria.getStatus()));
		}

		return bool;
	}

	public TradeLicenseIndexerRequest getTradeLicenseIndexerRequest(TradeLicenseRequest request) {

		TradeLicenseIndexerRequest indexerRequest = new TradeLicenseIndexerRequest();
		indexerRequest.setRequestInfo(request.getRequestInfo());

		List<TradeLicenseIndexerContract> tradeLicenseIndexer = new ArrayList<TradeLicenseIndexerContract>();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(request.getRequestInfo());
		org.egov.models.City cityDetails = new org.egov.models.City();
		String tenantId = "";

		for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {

			
			/*List<TradeLicenseSearchEntity> tradeLicenseSearchEntity = tradeLicenseJdbcRepository
					.searchById(request.getRequestInfo(), tradeLicenseContract.getId().longValue());*/
			
			LicenseSearch domain = new LicenseSearch();
			domain.setTenantId(tradeLicenseContract.getTenantId());
			
			if(tradeLicenseContract.getId() != null){
				
				Integer [] ids = {Integer.valueOf(tradeLicenseContract.getId().toString())};
				domain.setIds(ids);
			}
			
			List<TradeLicenseSearchEntity> liceseEntities = tradeLicenseJdbcRepository.search(request.getRequestInfo(), domain);
			TradeLicenseSearchEntity tradeLicenseSearchEntity = new TradeLicenseSearchEntity();
			
			if(liceseEntities != null && !liceseEntities.isEmpty() && liceseEntities.size() > 0){
				tradeLicenseSearchEntity = liceseEntities.get(0);
			}
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			TradeLicenseIndexerContract tradeLicense = mapper.map(tradeLicenseSearchEntity.toDomain(),
					TradeLicenseIndexerContract.class);

			if (!tenantId.equalsIgnoreCase(tradeLicenseSearchEntity.getTenantId())) {
				tenantId = tradeLicenseSearchEntity.getTenantId();
				cityDetails = tenantWebContract.fetchTenantByCode(tenantId, requestInfoWrapper);
			}

			if (cityDetails != null) {
				tradeLicense.setCityName(cityDetails.getName());
				tradeLicense.setCityRegionName(cityDetails.getRegionName());
				tradeLicense.setCityGrade(cityDetails.getUlbGrade());
				tradeLicense.setCityDistrictName(cityDetails.getDistrictName());
				tradeLicense.setCityDistrictCode(cityDetails.getDistrictCode());
				tradeLicense.setCityCode(cityDetails.getCode());
			}

			tradeLicenseIndexer.add(tradeLicense);
		}
		indexerRequest.setLicenses(tradeLicenseIndexer);
		return indexerRequest;
	}
}