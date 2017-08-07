package org.egov.egf.instrument.domain.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.web.contract.InstrumentSearchContract;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.TransactionType;
import org.egov.egf.instrument.persistence.entity.InstrumentEntity;
import org.egov.egf.instrument.web.contract.InstrumentSearchContract;
import org.egov.egf.instrument.web.requests.InstrumentRequest;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentESRepositoryTest {

	@Captor
	private ArgumentCaptor<InstrumentRequest> captor;

	private RequestInfo requestInfo = new RequestInfo();

	@Mock
	private InstrumentESRepository instrumentESRepository;

	@Mock
	private SearchResponse searchResponse;

	@Mock
	private TransportClient esClient;

	@Mock
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	@Mock
	private SearchRequestBuilder searchRequestBuilder;
	@Mock
	private ListenableActionFuture listenableActionFuture;

	@Before
	public void setUp() {


	}


	@Test
	public void test_search() {

		Pagination<Instrument> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);

		Instrument instrument = new Instrument();
		instrument.setTransactionNumber("TN123");
		expectedResult.setPagedData(new ArrayList<>());
		expectedResult.getPagedData().add(instrument);

		Pagination<Instrument> actualResult = new Pagination<>();
		actualResult.setPageSize(500);
		actualResult.setOffset(0);
		actualResult.setPagedData(new ArrayList<>());
		actualResult.getPagedData().add(instrument);

		InstrumentSearchContract instrumentSearchContract = new InstrumentSearchContract();
		instrumentSearchContract.setPageSize(500);
		instrumentSearchContract.setOffset(0);
		when(searchRequestBuilder.execute()).thenReturn(listenableActionFuture);
			//	.setTypes(Instrument.class.getSimpleName().toLowerCase());
		when(instrumentESRepository.getSearchRequest(instrumentSearchContract)).thenReturn(searchRequestBuilder);
		when(listenableActionFuture.actionGet()).thenReturn(searchResponse);
		assertNotNull(searchRequestBuilder.execute().actionGet());
		when(instrumentESRepository.mapToInstrumentList(searchResponse)).thenReturn(actualResult);
        when(instrumentESRepository.search(instrumentSearchContract)).thenReturn(actualResult);
		assertEquals(actualResult,expectedResult);


	}

}
