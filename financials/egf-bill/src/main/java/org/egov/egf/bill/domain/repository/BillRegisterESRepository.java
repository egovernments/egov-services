package org.egov.egf.bill.domain.repository;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.egov.egf.bill.persistence.repository.ESRepository;
import org.egov.egf.bill.util.ElasticSearchUtils;
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
public class BillRegisterESRepository extends ESRepository {

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    @Autowired
    private ObjectMapper mapper;

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Pagination<BillRegister> search(final BillRegisterSearch billRegisterSearch) {
        final SearchRequestBuilder searchRequestBuilder = getSearchRequest(billRegisterSearch);
        final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        return mapToBillRegisterList(searchResponse);
    }

    @SuppressWarnings("deprecation")
    private Pagination<BillRegister> mapToBillRegisterList(final SearchResponse searchResponse) {

        final Pagination<BillRegister> page = new Pagination<BillRegister>();

        if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L)
            return page;

        final List<BillRegister> billRegisters = new ArrayList<>();
        BillRegister billRegister = null;

        for (final SearchHit hit : searchResponse.getHits()) {

            // JSON from file to Object
            try {
                billRegister = mapper.readValue(hit.sourceAsString(), BillRegister.class);
            } catch (final JsonParseException e1) {

                e1.printStackTrace();

            } catch (final JsonMappingException e1) {

                e1.printStackTrace();

            } catch (final IOException e1) {

                e1.printStackTrace();

            }

            billRegisters.add(billRegister);
        }

        page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
        page.setPagedData(billRegisters);

        return page;
    }

    private SearchRequestBuilder getSearchRequest(final BillRegisterSearch criteria) {

        List<String> orderByList = new ArrayList<>();

        if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {

            validateSortByOrder(criteria.getSortBy());
            validateEntityFieldName(criteria.getSortBy(), BillRegisterEntity.class);
            orderByList = prepareOrderBy(criteria.getSortBy());

        }

        final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForBillRegisters(criteria);
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(BillRegister.class.getSimpleName().toLowerCase())
                .setTypes(BillRegister.class.getSimpleName().toLowerCase());

        if (!orderByList.isEmpty())
            for (final String orderBy : orderByList)
                searchRequestBuilder = searchRequestBuilder.addSort(orderBy.split(" ")[0],
                        orderBy.split(" ")[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);

        searchRequestBuilder.setQuery(boolQueryBuilder);

        return searchRequestBuilder;

    }

    public BoolQueryBuilder boolQueryBuilderForBillRegisters(final BillRegisterSearch billRegisterSearchContract) {

        final BoolQueryBuilder boolQueryBuilder = boolQuery();

        elasticSearchUtils.add(billRegisterSearchContract.getBillType(), "billType", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getBillSubType(), "billSubType", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getBillNumber(), "billNumber", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getBillDate(), "billDate", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getPassedAmount(), "passedAmount", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getBillAmount(), "billAmount", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getModuleName(), "moduleName", boolQueryBuilder);

        if (billRegisterSearchContract.getStatus() != null && billRegisterSearchContract.getStatus().getId() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getStatus().getId(), "status.id", boolQueryBuilder);

        if (billRegisterSearchContract.getFund() != null && billRegisterSearchContract.getFund().getId() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getFund().getId(), "fund.id", boolQueryBuilder);

        if (billRegisterSearchContract.getFunction() != null && billRegisterSearchContract.getFunction().getId() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getFunction().getId(), "function.id", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getBudgetAppropriationNo(), "budgetAppropriationNo",
                boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getTenantId(), "tenantId", boolQueryBuilder);

        if (billRegisterSearchContract.getDepartment() != null && billRegisterSearchContract.getDepartment().getId() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getDepartment().getId(), "department.id", boolQueryBuilder);

        if (billRegisterSearchContract.getLocation() != null && billRegisterSearchContract.getLocation().getId() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getLocation().getId(), "location.id", boolQueryBuilder);

        if (billRegisterSearchContract.getFunctionary() != null && billRegisterSearchContract.getFunctionary().getId() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getFunctionary().getId(), "functionary.id", boolQueryBuilder);

        if (billRegisterSearchContract.getFundsource() != null && billRegisterSearchContract.getFundsource().getId() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getFundsource().getId(), "fundsource.id", boolQueryBuilder);

        if (billRegisterSearchContract.getScheme() != null && billRegisterSearchContract.getScheme().getId() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getScheme().getId(), "scheme.id", boolQueryBuilder);

        if (billRegisterSearchContract.getSubScheme() != null && billRegisterSearchContract.getSubScheme().getId() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getSubScheme().getId(), "subScheme.id", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getSourcePath(), "sourcePath", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getBudgetCheckRequired(), "budgetCheckRequired", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getBudgetAppropriationNo(), "budgetAppropriationNo", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getPartyBillNumber(), "partyBillNumber", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getPartyBillDate(), "partyBillDate", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getDescription(), "description", boolQueryBuilder);

        if (billRegisterSearchContract.getGlcode() != null)
            elasticSearchUtils.add(billRegisterSearchContract.getGlcode(), "billDetails.glcode", boolQueryBuilder);

        if (billRegisterSearchContract.getGlcodes() != null)
            elasticSearchUtils.in(new ArrayList<>(Arrays.asList(billRegisterSearchContract.getGlcodes().split(","))),
                    "billDetails.glcode", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getDebitAmount(), "billDetails.debitAmount", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getCreditAmount(), "billDetails.creditAmount", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getAccountDetailKeyId(),
                "billDetails.billPayeeDetails.accountDetailKey.id", boolQueryBuilder);

        elasticSearchUtils.add(billRegisterSearchContract.getAccountDetailTypeId(),
                "billDetails.billPayeeDetails.accountDetailType.id", boolQueryBuilder);

        return boolQueryBuilder;

    }

    public List<String> prepareOrderBy(final String sortBy) {

        final List<String> orderByList = new ArrayList<>();
        List<String> sortByList = new ArrayList<>();

        if (sortBy.contains(","))
            sortByList = Arrays.asList(sortBy.split(","));
        else
            sortByList = Arrays.asList(sortBy);

        for (final String s : sortByList)
            if (s.contains(" ")
                    && (s.toLowerCase().trim().endsWith("asc") || s.toLowerCase().trim().endsWith("desc")))
                orderByList.add(s.trim());
            else
                orderByList.add(s.trim() + " asc");

        return orderByList;
    }

}
