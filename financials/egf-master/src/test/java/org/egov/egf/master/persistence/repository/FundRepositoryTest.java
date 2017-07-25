package org.egov.egf.master.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.FundSearch;
import org.egov.egf.master.domain.repository.FundRepository;
import org.egov.egf.master.persistence.entity.FundEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.requests.FundRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FundRepositoryTest {

	@Mock
	private FundJdbcRepository fundJdbcRepository;
	@Mock
	private MastersQueueRepository fundQueueRepository;

	@InjectMocks
	private FundRepository fundRepository;

	@Test
	public void find_by_id_test() {
		FundEntity entity = getFundEntity();
		Fund expectedResult = entity.toDomain();
		when(fundJdbcRepository.findById(any(FundEntity.class))).thenReturn(entity);
		Fund actualResult = fundRepository.findById(getFundDomin());
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void save_test() {

		FundEntity entity = getFundEntity();
		Fund expectedResult = entity.toDomain();
		when(fundJdbcRepository.create(any(FundEntity.class))).thenReturn(entity);
		Fund actualResult = fundRepository.save(getFundDomin());
		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void update_test() {

		FundEntity entity = getFundEntity();
		Fund expectedResult = entity.toDomain();
		when(fundJdbcRepository.update(any(FundEntity.class))).thenReturn(entity);
		Fund actualResult = fundRepository.update(getFundDomin());
		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void add_test() {
		Mockito.doNothing().when(fundQueueRepository).add(Mockito.any());
		FundRequest request = new FundRequest();
		request.setRequestInfo(getRequestInfo());
		request.setFunds(new ArrayList<>());
		request.getFunds().add(getFundContract());
		fundRepository.add(request);
		//verify(fundQueueRepository).add(request);

	}

	@Test
	public void test_search() {

		Pagination<Fund> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);
		when(fundJdbcRepository.search(any(FundSearch.class))).thenReturn(expectedResult);
		Pagination<Fund> actualResult = fundRepository.search(getFundSearch());
		assertEquals(expectedResult, actualResult);

	}

	private FundContract getFundContract() {
		return FundContract.builder().code("001").name("test").active(true).level(1l).isParent(false).build();
	}

	private Fund getFundDomin() {
		Fund fund = new Fund();
		fund.setCode("code");
		fund.setName("name");
		fund.setActive(true);
		fund.setLevel(1l);
		fund.setIsParent(false);
		fund.setTenantId("default");
		return fund;
	}

	private FundEntity getFundEntity() {
		FundEntity entity = new FundEntity();
		Fund fund = getFundDomin();
		entity.setCode(fund.getCode());
		entity.setName(fund.getName());
		entity.setActive(fund.getActive());
		entity.setLevel(fund.getLevel());
		entity.setIsParent(fund.getIsParent());
		entity.setTenantId(fund.getTenantId());
		return entity;
	}

	private RequestInfo getRequestInfo() {
		RequestInfo info = new RequestInfo();
		User user = new User();
		user.setId(1l);
		info.setAction("create ");
		info.setDid("did");
		info.setApiId("apiId");
		info.setKey("key");
		info.setMsgId("msgId");
	//	info.setRequesterId("requesterId");
	//	info.setTenantId("default");
		info.setTs(new Date());
		info.setUserInfo(user);
		info.setAuthToken("null");
		return info;
	}

	private FundSearch getFundSearch() {
		FundSearch fundSearch = new FundSearch();
		fundSearch.setPageSize(500);
		fundSearch.setOffset(0);
		return fundSearch;

	}

}
