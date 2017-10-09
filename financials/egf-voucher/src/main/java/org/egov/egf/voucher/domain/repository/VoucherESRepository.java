package org.egov.egf.voucher.domain.repository;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.ESRepository;
import org.egov.common.util.ElasticSearchUtils;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.persistence.entity.VoucherEntity;
import org.egov.egf.voucher.web.contract.VoucherSearchContract;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VoucherESRepository extends ESRepository {

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private TransportClient esClient;
	private ElasticSearchUtils elasticSearchUtils;

	@Autowired
	public VoucherESRepository(TransportClient esClient, ElasticSearchUtils elasticSearchUtils) {

		this.esClient = esClient;
		this.elasticSearchUtils = elasticSearchUtils;

	}

	public Pagination<Voucher> search(VoucherSearchContract voucherSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(voucherSearchContract);
		System.out.println("Voucher searchRequestBuilder ---->" + searchRequestBuilder.toString());
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToVoucherList(searchResponse);
	}

	@SuppressWarnings("deprecation")
	private Pagination<Voucher> mapToVoucherList(SearchResponse searchResponse) {

		Pagination<Voucher> page = new Pagination<>();

		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}

		List<Voucher> vouchers = new ArrayList<Voucher>();
		Voucher voucher = null;

		for (SearchHit hit : searchResponse.getHits()) {

			ObjectMapper mapper = new ObjectMapper();
			// JSON from file to Object
			try {
				voucher = mapper.readValue(hit.sourceAsString(), Voucher.class);
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

			vouchers.add(voucher);
		}

		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(vouchers);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(VoucherSearchContract criteria) {

		List<String> orderByList = new ArrayList<>();

		if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {

			validateSortByOrder(criteria.getSortBy());
			validateEntityFieldName(criteria.getSortBy(), VoucherEntity.class);
			orderByList = prepareOrderBy(criteria.getSortBy());

		}

		final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForVouchers(criteria);
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(Voucher.class.getSimpleName().toLowerCase())
				.setTypes(Voucher.class.getSimpleName().toLowerCase());

		if (!orderByList.isEmpty()) {
			for (String orderBy : orderByList) {
				searchRequestBuilder = searchRequestBuilder.addSort(orderBy.split(" ")[0],
						orderBy.split(" ")[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
			}
		}

		searchRequestBuilder.setQuery(boolQueryBuilder);

		return searchRequestBuilder;

	}

	public BoolQueryBuilder boolQueryBuilderForVouchers(VoucherSearchContract voucherSearchContract) {

		BoolQueryBuilder boolQueryBuilder = boolQuery();

		if (voucherSearchContract.getIds() != null && !voucherSearchContract.getIds().isEmpty())
			elasticSearchUtils.in(new ArrayList<String>(Arrays.asList(voucherSearchContract.getIds().split(","))), "id",
					boolQueryBuilder);

		if (voucherSearchContract.getVoucherNumbers() != null && !voucherSearchContract.getVoucherNumbers().isEmpty())
			elasticSearchUtils.in(
					new ArrayList<String>(Arrays.asList(voucherSearchContract.getVoucherNumbers().split(","))),
					"voucherNumber", boolQueryBuilder);

		if (voucherSearchContract.getNames() != null && !voucherSearchContract.getNames().isEmpty())
			elasticSearchUtils.in(new ArrayList<String>(Arrays.asList(voucherSearchContract.getNames().split(","))),
					"name", boolQueryBuilder);

		if (voucherSearchContract.getStatuses() != null && !voucherSearchContract.getStatuses().isEmpty())
			elasticSearchUtils.in(new ArrayList<String>(Arrays.asList(voucherSearchContract.getStatuses().split(","))),
					"statusId", boolQueryBuilder);

		if (voucherSearchContract.getVoucherFromDate() != null)
			elasticSearchUtils.gte(sdf.format(voucherSearchContract.getVoucherFromDate()), "voucherDate",
					boolQueryBuilder);
		if (voucherSearchContract.getVoucherToDate() != null)
			elasticSearchUtils.lte(sdf.format(voucherSearchContract.getVoucherToDate()), "voucherDate",
					boolQueryBuilder);

		if (voucherSearchContract.getTypes() != null && !voucherSearchContract.getTypes().isEmpty())
			elasticSearchUtils.in(new ArrayList<String>(Arrays.asList(voucherSearchContract.getTypes().split(","))),
					"type", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getId(), "id", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getVoucherNumber(), "voucherNumber", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getBillNumber(), "billNumber", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getBudgetAppropriationNo(), "budgetAppropriationNo",
				boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getDescription(), "description", boolQueryBuilder);

		// elasticSearchUtils.add(voucherSearchContract.getBudgetCheckRequired(),
		// "budgetCheckRequired", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getModuleName(), "moduleName", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getName(), "name", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getOriginalVoucherNumber(), "originalVoucherNumber",
				boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getRefVoucherNumber(), "refVoucherNumber", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getTenantId(), "tenantId", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getType(), "type", boolQueryBuilder);

		if (voucherSearchContract.getDepartment() != null && voucherSearchContract.getDepartment().getId() != null)
			elasticSearchUtils.add(voucherSearchContract.getDepartment().getId(), "department.id", boolQueryBuilder);

		if (voucherSearchContract.getDivision() != null && voucherSearchContract.getDivision().getId() != null)
			elasticSearchUtils.add(voucherSearchContract.getDivision().getId(), "division.id", boolQueryBuilder);

		if (voucherSearchContract.getFunction() != null && voucherSearchContract.getFunction().getId() != null)
			elasticSearchUtils.add(voucherSearchContract.getFunction().getId(), "function.id", boolQueryBuilder);

		if (voucherSearchContract.getFunctionary() != null && voucherSearchContract.getFunctionary().getId() != null)
			elasticSearchUtils.add(voucherSearchContract.getFunctionary().getId(), "functionary.id", boolQueryBuilder);

		if (voucherSearchContract.getFund() != null && voucherSearchContract.getFund().getId() != null)
			elasticSearchUtils.add(voucherSearchContract.getFund().getId(), "fund.id", boolQueryBuilder);

		if (voucherSearchContract.getFundsource() != null && voucherSearchContract.getFundsource().getId() != null)
			elasticSearchUtils.add(voucherSearchContract.getFundsource().getId(), "fundsource.id", boolQueryBuilder);

		if (voucherSearchContract.getScheme() != null && voucherSearchContract.getScheme().getId() != null)
			elasticSearchUtils.add(voucherSearchContract.getScheme().getId(), "scheme.id", boolQueryBuilder);

		if (voucherSearchContract.getSubScheme() != null && voucherSearchContract.getSubScheme().getId() != null)
			elasticSearchUtils.add(voucherSearchContract.getSubScheme().getId(), "subScheme.id", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getSourcePath(), "sourcePath", boolQueryBuilder);

		if (voucherSearchContract.getStatus() != null && voucherSearchContract.getStatus().getId() != null)
			elasticSearchUtils.add(voucherSearchContract.getStatus().getId(), "status.id", boolQueryBuilder);

		if (voucherSearchContract.getVoucherDate() != null)
			elasticSearchUtils.add(sdf.format(voucherSearchContract.getVoucherDate()), "voucherDate", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getGlcode(), "ledgers.glcode", boolQueryBuilder);

		if (voucherSearchContract.getGlcodes() != null)
			elasticSearchUtils.in(new ArrayList<String>(Arrays.asList(voucherSearchContract.getGlcodes().split(","))),
					"ledgers.glcode", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getDebitAmount(), "ledgers.debitAmount", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getCreditAmount(), "ledgers.creditAmount", boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getSubLedgerAmount(), "ledgers.subLedgers.amount",
				boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getAccountDetailKeyId(), "ledgers.subLedgers.accountDetailKey.id",
				boolQueryBuilder);

		elasticSearchUtils.add(voucherSearchContract.getAccountDetailTypeId(),
				"ledgers.subLedgers.accountDetailType.id", boolQueryBuilder);

		return boolQueryBuilder;

	}

	public List<String> prepareOrderBy(String sortBy) {

		List<String> orderByList = new ArrayList<String>();
		List<String> sortByList = new ArrayList<String>();

		if (sortBy.contains(",")) {
			sortByList = Arrays.asList(sortBy.split(","));
		} else {
			sortByList = Arrays.asList(sortBy);
		}

		for (String s : sortByList) {

			if (s.contains(" ")
					&& (s.toLowerCase().trim().endsWith("asc") || s.toLowerCase().trim().endsWith("desc"))) {
				orderByList.add(s.trim());
			} else {

				orderByList.add(s.trim() + " asc");
			}

		}

		return orderByList;
	}

}
