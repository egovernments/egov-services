package org.egov.demand.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillDetail;
import org.egov.demand.repository.querybuilder.BillQueryBuilder;
import org.egov.demand.web.contract.BillRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BillRepository {

	@Autowired
	private BillQueryBuilder billQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void saveBill(BillRequest billRequest){
		
		RequestInfo requestInfo = billRequest.getRequestInfo();
		List<Bill> bills = billRequest.getBillInfos();
		
		jdbcTemplate.batchUpdate(billQueryBuilder.INSERT_BILL_QUERY, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Bill bill = bills.get(index);
				
				ps.setLong(1, bill.getId());
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
		
	}
	
	public void saveBillDeails(BillRequest billRequest){
		
		RequestInfo requestInfo = billRequest.getRequestInfo();
		List<Bill> bills = billRequest.getBillInfos();
		for(Bill bill:bills){
		
			List<BillDetail> billDetails = bill.getBillDetails();
			
			jdbcTemplate.batchUpdate(billQueryBuilder.INSERT_BILLDETAILS_QUERY, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					BillDetail billDetail = billDetails.get(index);
					
					ps.setLong(1, billDetail.getId());
					ps.setString(2, bill.getTenantId());
					ps.setLong(3, bill.getId());
					ps.setString(4, billDetail.getBusinessService());
					ps.setString(5, billDetail.getBillNumber());
					ps.setLong(6, billDetail.getBillDate());
					ps.setString(7, billDetail.getConsumerCode());
					ps.setString(8, billDetail.getConsumerType());
					ps.setString(9, billDetail.getBillDescription());
					ps.setString(10, billDetail.getDisplayMessage());
					ps.setDouble(11, billDetail.getMinimumAmount());
					ps.setDouble(12, billDetail.getTotalAmount());
					ps.setBoolean(13, billDetail.getCallBackForApportioning());
					ps.setBoolean(14, billDetail.getPartPaymentAllowed());
					ps.setString(15, billDetail.getCollectionModesNotAllowed().toString());
					ps.setString(16, requestInfo.getUserInfo().getId().toString());
					ps.setLong(17, new Date().getTime());
					ps.setString(18, requestInfo.getUserInfo().getId().toString());
					ps.setLong(19, new Date().getTime());
				}
				
				@Override
				public int getBatchSize() {
					return bills.size();
				}
			});
		}
			
	}
	
	//TODO
	public void saveBillAccountDetail(BillRequest billRequest){
		 final String INSERT_BILLACCOUNTDETAILS_QUERY = "INSERT into egbs_billaccountdetail "
				+"(id,tenantid,billdetailid,glcode,orderno,accountdescription,creditamount,debitamount,isactualdemand,purpose,"
				+ "createdby,createddate,lastmodifiedby,lastmodifieddate)"
				+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
	}
}
