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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BillRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(BillRepository.class);

	@Autowired
	private BillQueryBuilder billQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
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
	
	/*public Long getNextValue(String seqName){
		String query = "SELECT nextval('"+seqName+"')";
		Integer result = null;
		try {
			result= jdbcTemplate.querforl(query, Integer.class);
			jdbcTemplate.
		}catch(Exception ex) {
			LOGGER.info("BillRepository getNextValue"+ex);
			throw new RuntimeException(ex);
		}
				
		return Long.valueOf(result.longValue());
	}
	*/
	//TODO
	public void saveBillAccountDetail(BillRequest billRequest){
		 final String INSERT_BILLACCOUNTDETAILS_QUERY = "INSERT into egbs_billaccountdetail "
				+"(id,tenantid,billdetailid,glcode,orderno,accountdescription,creditamount,debitamount,isactualdemand,purpose,"
				+ "createdby,createddate,lastmodifiedby,lastmodifieddate)"
				+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
	}
}
