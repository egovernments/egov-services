package org.egov.demand.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.DemandDetailCriteria;
import org.egov.demand.model.DemandUpdateMisRequest;
import org.egov.demand.model.Owner;
import org.egov.demand.repository.querybuilder.DemandQueryBuilder;
import org.egov.demand.repository.rowmapper.DemandDetailRowMapper;
import org.egov.demand.repository.rowmapper.DemandRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

@RunWith(PowerMockRunner.class)
public class DemandRepositoryTest {

	public static final String DEMAND_INSERT_QUERY = "INSERT INTO egbs_demand "
			+ "(id,consumerCode,consumerType,businessService,owner,taxPeriodFrom,taxPeriodTo,"
			+ "minimumAmountPayable,type,createdby,lastModifiedby,createdtime,lastModifiedtime,tenantid) "
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

	public static final String DEMAND_DETAIL_INSERT_QUERY = "INSERT INTO egbs_demanddetail "
			+ "(id,demandid,taxHeadCode,taxamount,collectionamount,"
			+ "createdby,lastModifiedby,createdtime,lastModifiedtime,tenantid)" + " VALUES (?,?,?,?,?,?,?,?,?,?);";

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private DemandRepository demandRepository;
	
	@Mock
	private DemandQueryBuilder demandQueryBuilder;

	
	@Test
	@PrepareForTest({DemandQueryBuilder.class})
	public void testShouldGetDemands(){
		Demand demand = getDemand();
		List<Demand> demands = new ArrayList<>();
		demands.add(demand);
		String query ="";
		PowerMockito.mockStatic(DemandQueryBuilder.class);
		
		 PowerMockito.when(demandQueryBuilder.getDemandQuery(any(DemandCriteria.class),any(Set.class),any(List.class))).thenReturn(query);
		
		 when(jdbcTemplate.query(any(String.class),any(Object[].class),any(DemandRowMapper.class))).thenReturn(demands);

		 assertTrue(demands.equals(demandRepository.getDemands(new DemandCriteria(),new HashSet<String>())));

	}
	
	@Test
	@PrepareForTest({DemandQueryBuilder.class})
	public void testShouldGetDemandDetails(){
		List<DemandDetail> demandDetails = getDemandDetails();
		
		String query ="";
		PowerMockito.mockStatic(DemandQueryBuilder.class);
		
		 PowerMockito.when(demandQueryBuilder.getDemandDetailQuery(any(DemandDetailCriteria.class),any(List.class))).thenReturn(query);
		
		 when(jdbcTemplate.query(any(String.class), any(Object[].class), any(DemandDetailRowMapper.class))).thenReturn(demandDetails);
		assertTrue(demandDetails.equals(demandRepository.getDemandDetails(new DemandDetailCriteria())));

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void methodSaveCreateDemand() {

		Demand demand = getDemand();
		List<DemandDetail> demandDetails = demand.getDemandDetails();
		List<Demand> demands = new ArrayList<>();
		demands.add(demand);

		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(new int[] { 1 });
		assertEquals(jdbcTemplate.batchUpdate(any(String.class), any(List.class)).length, demands.size());

		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(new int[] { 1, 2 });
		System.err.println(jdbcTemplate.batchUpdate(any(String.class), any(List.class)).length);
		assertEquals(jdbcTemplate.batchUpdate(any(String.class), any(List.class)).length, demandDetails.size());
	}
	
	
	
	@Test
	public void methodShouldUpdate(){
		
		Demand demand = getDemand();
		List<DemandDetail> demandDetails = demand.getDemandDetails();
		List<Demand> demands = new ArrayList<>();
		demands.add(demand);

		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(new int[] { 1 });
		assertEquals(jdbcTemplate.batchUpdate(any(String.class), any(List.class)).length, demands.size());

		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(new int[] { 1, 2 });
		System.err.println(jdbcTemplate.batchUpdate(any(String.class), any(List.class)).length);
		assertEquals(jdbcTemplate.batchUpdate(any(String.class), any(List.class)).length, demandDetails.size());
		
	}
	
	@Test
	@PrepareForTest({DemandQueryBuilder.class})
	public void testShouldUpdateMIS(){
		DemandUpdateMisRequest demandRequest=DemandUpdateMisRequest.builder().tenantId("default").build();
		PowerMockito.mockStatic(DemandQueryBuilder.class);
		String query="";
		 PowerMockito.when(demandQueryBuilder.getDemandUpdateMisQuery(any(DemandUpdateMisRequest.class))).thenReturn(query);
		
		 when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
		
		demandRepository.updateMIS(demandRequest);
	}

	public ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setVer(requestInfo.getVer());
		responseInfo.setVer(requestInfo.getVer());
		return responseInfo;
	}

	public Demand getDemand() {

		Demand demand = new Demand();
		Owner owner = new Owner();
		owner.setId(1l);

		demand.setConsumerCode("consumercode");
		demand.setBusinessService("businessservice");
		demand.setConsumerType("consumertype");
		demand.setOwner(owner);
		demand.setMinimumAmountPayable(BigDecimal.valueOf(200d));
		demand.setTaxPeriodFrom(12345l);
		demand.setTaxPeriodTo(1234567890l);
		demand.setTenantId("ap.kurnool");
		demand.setDemandDetails(getDemandDetails());
		return demand;
	}

	public List<DemandDetail> getDemandDetails() {

		List<DemandDetail> demandDetails = new ArrayList<>();
		DemandDetail demandDetail = new DemandDetail();
		demandDetail.setTaxAmount(BigDecimal.valueOf(100d));
		demandDetail.setCollectionAmount(BigDecimal.ZERO);
		demandDetail.setTaxHeadMasterCode("0002");
		DemandDetail demandDetail1 = new DemandDetail();
		demandDetail1.setTaxAmount(BigDecimal.valueOf(200d));
		demandDetail1.setCollectionAmount(BigDecimal.ZERO);
		demandDetail1.setTaxHeadMasterCode("0003");
		demandDetails.add(demandDetail);
		demandDetails.add(demandDetail1);
		return demandDetails;
	}

	public RequestInfo getRequestInfo() {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setApiId("apiId");
		requestInfo.setVer("ver");
		requestInfo.setDid("did");
		return requestInfo;
	}

}
