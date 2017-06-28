package org.egov.demand.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.Owner;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.enums.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
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
