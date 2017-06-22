package org.egov.demand.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillAccountDetail;
import org.egov.demand.model.BillDetail;
import org.egov.demand.repository.querybuilder.BillQueryBuilder;
import org.egov.demand.web.contract.BillRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BillRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(BillRepository.class);

	@Autowired
	private BillQueryBuilder billQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public void saveBill(BillRequest billRequest){
		
		RequestInfo requestInfo = billRequest.getRequestInfo();
		List<Bill> bills = billRequest.getBill();
		
		log.info("saveBill requestInfo:"+requestInfo);
		log.info("saveBill bills:"+bills);
		jdbcTemplate.batchUpdate(billQueryBuilder.INSERT_BILL_QUERY, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Bill bill = bills.get(index);

				ps.setString(1, bill.getId());
				ps.setString(2, bill.getTenantId());
				ps.setString(3, bill.getPayeeName());
				ps.setString(4, bill.getPayeeAddress());
				ps.setString(5, bill.getPayeeEmail());
				ps.setBoolean(6, bill.getIsActive());
				ps.setBoolean(7, bill.getIsCancelled());
				ps.setString(8, requestInfo.getUserInfo().getId().toString());
				ps.setLong(9, new Date().getTime());
				ps.setString(10, requestInfo.getUserInfo().getId().toString());
				ps.setLong(11, new Date().getTime());
			}
			
			@Override
			public int getBatchSize() {
				return bills.size();
			}
		});
		saveBillDeails(billRequest);
	}
	
	public void saveBillDeails(BillRequest billRequest){
		
		RequestInfo requestInfo = billRequest.getRequestInfo();
		List<Bill> bills = billRequest.getBill();
		List<BillDetail> billDetails = new ArrayList<BillDetail>();
		List<BillAccountDetail> billAccountDetails = new ArrayList<BillAccountDetail>();
		for(Bill bill:bills){
			List<BillDetail> tempBillDetails  = bill.getBillDetails();
			billDetails.addAll(tempBillDetails);
			
			for(BillDetail billDetail : tempBillDetails){
				billAccountDetails.addAll(billDetail.getBillAccountDetails());
			}
		}
		log.info("saveBillDeails tempBillDetails:"+billDetails);
		//final List<BillDetail> billDetails = tempBillDetails;	
		jdbcTemplate.batchUpdate(billQueryBuilder.INSERT_BILLDETAILS_QUERY, new BatchPreparedStatementSetter() {
				
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				BillDetail billDetail = billDetails.get(index);
					

				ps.setString(1, billDetail.getId());
				ps.setString(2, billDetail.getTenantId());
				ps.setString(3, billDetail.getBill());
				ps.setString(4, billDetail.getBusinessService());
				ps.setString(5, billDetail.getBillNumber());
				ps.setLong(6, billDetail.getBillDate());
				ps.setString(7, billDetail.getConsumerCode());
				ps.setString(8, billDetail.getConsumerType());
				ps.setString(9, billDetail.getBillDescription());
				ps.setString(10, billDetail.getDisplayMessage());
				ps.setObject(11, billDetail.getMinimumAmount());
				ps.setObject(12, billDetail.getTotalAmount());
				ps.setBoolean(13, billDetail.getCallBackForApportioning());
				ps.setBoolean(14, billDetail.getPartPaymentAllowed());
				//TODO
				//ps.setString(15, billDetail.getCollectionModesNotAllowed().toString());
				ps.setString(15, null);
				ps.setString(16, requestInfo.getUserInfo().getId().toString());
				ps.setLong(17, new Date().getTime());
				ps.setString(18, requestInfo.getUserInfo().getId().toString());
				ps.setLong(19, new Date().getTime());
			}

				
			@Override
			public int getBatchSize() {
				return billDetails.size();
			}
		});
		
		saveBillAccountDetail(billAccountDetails, requestInfo);
	}
	
	public void saveBillAccountDetail(List<BillAccountDetail> billAccountDetails, RequestInfo requestInfo){
		log.info("saveBillAccountDetail billAccountDetails:"+billAccountDetails);
		//final List<BillDetail> billDetails = tempBillDetails;	
		jdbcTemplate.batchUpdate(billQueryBuilder.INSERT_BILLACCOUNTDETAILS_QUERY, new BatchPreparedStatementSetter() {
				
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				BillAccountDetail billAccountDetail = billAccountDetails.get(index);
				
				String purpose = null;
				if(billAccountDetail.getPurpose()!=null)
					purpose = billAccountDetail.getPurpose().toString();
					
				ps.setString(1, billAccountDetail.getId());
				ps.setString(2, billAccountDetail.getTenantId());
				ps.setString(3, billAccountDetail.getBillDetail());
				ps.setString(4, billAccountDetail.getGlcode());
				ps.setObject(5, billAccountDetail.getOrder());
				ps.setString(6, billAccountDetail.getAccountDescription());
				ps.setObject(7, billAccountDetail.getCreditAmount());
				ps.setObject(8, billAccountDetail.getDebitAmount());
				ps.setObject(9, billAccountDetail.getIsActualDemand());
				ps.setString(10, purpose);
				ps.setString(11, requestInfo.getUserInfo().getId().toString());
				ps.setLong(12, new Date().getTime());
				ps.setString(13, requestInfo.getUserInfo().getId().toString());
				ps.setLong(14, new Date().getTime());
			}
				
			@Override
			public int getBatchSize() {
				return billAccountDetails.size();
			}
		});
	}
}
