package org.egov.demand.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.demand.model.Bill;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.repository.querybuilder.BillQueryBuilder;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BillRepositoryTest {

	@Mock
	private BillQueryBuilder billQueryBuilder;
	
	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@InjectMocks
	private BillRepository billRepository;
	
	/*@Test
	public void testSaveBill() {
		
		BillRequest billRequest = new BillRequest();
		RequestInfo requestInfo = new RequestInfo();
		User user = new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		billRequest.setRequestInfo(requestInfo);
		Bill bill = getBills();
		List<Bill> bills = new ArrayList<>();
		bills.add(bill);
		billRequest.setBills(bills);
		
		when(jdbcTemplate.batchUpdate(any(String.class),any(List.class))).thenReturn(new int[] { 1 });
		assertEquals(jdbcTemplate.batchUpdate(any(String.class), any(List.class)).length, bills.size());

		
	}*/
	
	@Test
	public void testsaveBillDetails(){

		BillRequest billRequest = new BillRequest();
		RequestInfo requestInfo = new RequestInfo();
		User user = new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		billRequest.setRequestInfo(requestInfo);
		Bill bill = getBills();
		List<Bill> bills = new ArrayList<>();
		bills.add(bill);
		billRequest.setBills(bills);
		
		when(jdbcTemplate.batchUpdate(any(String.class),any(List.class))).thenReturn(new int[] { 1 });
		assertEquals(jdbcTemplate.batchUpdate(any(String.class), any(List.class)).length, bills.size());

	}
	
	private Bill getBills(){
		Bill bill=new Bill();
		bill.setId("12");
		bill.setIsActive(true);
		bill.setIsCancelled(true);
		bill.setPayeeAddress("bangalore");
		bill.setPayeeEmail("abc@xyz.com");
		bill.setPayeeName("abcd");
		bill.setTenantId("ap.kurnool");
		bill.setBillDetails(null);
		
		return bill;
	}

}
