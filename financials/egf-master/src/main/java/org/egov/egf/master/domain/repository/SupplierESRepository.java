package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Supplier;
import org.egov.egf.master.web.contract.SupplierSearchContract;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SupplierESRepository {

	private static final String DEFAULT_SORT_FIELD = "name";
	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public SupplierESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<Supplier> search(SupplierSearchContract supplierSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(supplierSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToSupplierList(searchResponse,supplierSearchContract);
	}


    @SuppressWarnings("deprecation")
	private Pagination<Supplier> mapToSupplierList(SearchResponse searchResponse,SupplierSearchContract supplierSearchContract) {
		Pagination<Supplier> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<Supplier> suppliers = new ArrayList<Supplier>();
		Supplier supplier=null;
		for (SearchHit hit : searchResponse.getHits()) {
			
			ObjectMapper mapper = new ObjectMapper();
			//JSON from file to Object
			try {
			    supplier = mapper.readValue(hit.sourceAsString(), Supplier.class);
			} catch (JsonParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			suppliers.add(supplier);
		}
		
		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(suppliers);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(SupplierSearchContract criteria) {
		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchSupplier(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(Supplier.class.getSimpleName().toLowerCase()).setTypes(Supplier.class.getSimpleName().toLowerCase())
				.addSort(DEFAULT_SORT_FIELD, SortOrder.ASC).setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}
