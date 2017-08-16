package org.egov.tl.indexer.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.TradeLicenseResponse;
import org.egov.tl.indexer.client.JestClientEs;
import org.egov.tl.indexer.config.PropertiesManager;
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
	
	
	
	public TradeLicenseResponse searchFromEs(String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, String tradeLicenseId, String applicationNumber,
			String licenseNumber, String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String emailId, String propertyAssesmentNo,
			Integer adminWard, Integer locality, String ownerName, String tradeTitle, String tradeType,
			Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status){
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		TradeLicenseResponse tradeLicenseResponse =  new TradeLicenseResponse();
		
		List<TradeLicenseContract> tlList = new ArrayList<TradeLicenseContract>();
		
		BoolQueryBuilder builder = SearchUtil.buildSearchQuery( tenantId,  active, tradeLicenseId,
				applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber, emailId, propertyAssesmentNo,
				adminWard, locality, ownerName, tradeTitle, tradeType, tradeCategory, tradeSubCategory,
				legacy, status);
		
		if(builder != null ){
		searchSourceBuilder.query(builder);
		
		if(pageNumber != null)
			searchSourceBuilder.from(pageNumber);
		
		if(pageSize != null)
			searchSourceBuilder.size(pageSize);
		
		Search search = (Search) new Search.Builder(searchSourceBuilder.toString())
				.addIndex(propertiesManager.getEsIndex())
				.addType(propertiesManager.getEsIndexType())
				.build();
		
		if(sort != null && !sort.isEmpty()){
			searchSourceBuilder.sort("id", SortOrder.ASC);
		}
		SearchResult searchresult = null;
		try {
			searchresult = jestClient.getClient().execute(search);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Hit<TradeLicenseContract,Void>> hits =  searchresult.getHits(TradeLicenseContract.class);
		for (SearchResult.Hit<TradeLicenseContract,Void> hit : hits) {
			tlList.add(hit.source);
			
		}
		tradeLicenseResponse.setLicenses(tlList);
		}
		
		return tradeLicenseResponse;
		
	}
	
	
}
