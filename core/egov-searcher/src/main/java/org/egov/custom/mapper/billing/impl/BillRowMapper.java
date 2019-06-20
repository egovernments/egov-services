package org.egov.custom.mapper.billing.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.custom.mapper.billing.impl.BillAccountDetail.PurposeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BillRowMapper implements ResultSetExtractor<List<Bill>> {

	@Autowired
	private RestTemplate rest;
	
	@Value("${egov.user.contextpath}")
	private String userContext;
	
	@Value("${egov.user.searchpath}")
	private String userSearchPath;
		

	@Override
	public List<Bill> extractData(ResultSet rs) throws SQLException {
		Map<String, Bill> billMap = new LinkedHashMap<>();
		Map<String, BillDetail> billDetailMap = new HashMap<>();
		Set<String> userIds = new HashSet<>();

		while (rs.next()) {

			String billId = rs.getString("b_id");
			Bill bill = billMap.get(billId);

			if (bill == null) {

				bill = new Bill();
				bill.setId(billId);
				bill.setTenantId(rs.getString("b_tenantid"));
				bill.setPayerName(rs.getString("b_payername"));
				bill.setPayerAddress(rs.getString("b_payeraddress"));
				bill.setPayerEmail(rs.getString("b_payeremail"));
				bill.setIsActive(rs.getBoolean("b_isactive"));
				bill.setIsCancelled(rs.getBoolean("b_iscancelled"));
				bill.setMobileNumber(rs.getString("mobilenumber"));

				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setCreatedBy(rs.getString("b_createdby"));
				auditDetails.setCreatedTime((Long) rs.getObject("b_createddate"));
				auditDetails.setLastModifiedBy(rs.getString("b_lastmodifiedby"));
				auditDetails.setLastModifiedTime((Long) rs.getObject("b_lastmodifieddate"));

				bill.setAuditDetails(auditDetails);
				bill.setTaxAndPayments(new ArrayList<>());

				billMap.put(bill.getId(), bill);
			}

			String detailId = rs.getString("bd_id");
			BillDetail billDetail = billDetailMap.get(detailId);

			if (billDetail == null) {

				billDetail = new BillDetail();
				billDetail.setId(detailId);
				billDetail.setTenantId(rs.getString("bd_tenantid"));
				billDetail.setBill(rs.getString("bd_billid"));
				billDetail.setBusinessService(rs.getString("bd_businessservice"));
				billDetail.setBillNumber(rs.getString("bd_billno"));
				billDetail.setBillDate(rs.getLong("bd_billdate"));

				billDetail.setDemandId(rs.getString("demandid"));
				billDetail.setFromPeriod(rs.getLong("fromperiod"));
				billDetail.setToPeriod(rs.getLong("toperiod"));

				billDetail.setTotalAmount(rs.getBigDecimal("bd_totalamount"));
				billDetail.setCollectedAmount(rs.getBigDecimal("bd_collectedamount"));
				setTaxAndPayments(bill.getTaxAndPayments(), billDetail.getBusinessService(),
						billDetail.getTotalAmount());
				billDetail.setConsumerCode(rs.getString("bd_consumercode"));
				billDetail.setConsumerType(rs.getString("bd_consumertype"));
				billDetail.setMinimumAmount(rs.getBigDecimal("bd_minimumamount"));
				billDetail.setPartPaymentAllowed(rs.getBoolean("bd_partpaymentallowed"));
				billDetail.setIsAdvanceAllowed(rs.getBoolean("bd_isadvanceallowed"));
				billDetail.setExpiryDate(rs.getLong("bd_expirydate"));
				billDetail.setCollectionModesNotAllowed(
						Arrays.asList(rs.getString("bd_collectionmodesnotallowed").split(",")));

				Address address = new Address();
				address.setDoorNo(rs.getString("ptadd_doorno"));
				address.setAddressline1(rs.getString("ptadd_addressline1"));
				address.setAddressline2(rs.getString("ptadd_addressline2"));
				address.setCity(rs.getString("ptadd_city"));
				address.setLandmark(rs.getString("ptadd_landmark"));
				address.setPincode(rs.getString("ptadd_pincode"));
				address.setLocality(rs.getString("ptadd_locality"));
				billDetail.setAddress(address);

				billDetailMap.put(billDetail.getId(), billDetail);

				if (bill.getId().equals(billDetail.getBill()))
					bill.addBillDetailsItem(billDetail);

			}
			
			User user = new User();
			user.setId(rs.getString("ptown_userid"));
			billDetail.addUserItem(user);
			userIds.add(user.getId());

			BillAccountDetail billAccDetail = new BillAccountDetail();
			billAccDetail.setId(rs.getString("ad_id"));
			billAccDetail.setTenantId(rs.getString("ad_tenantid"));
			billAccDetail.setBillDetail(rs.getString("ad_billdetail"));
			billAccDetail.setGlcode(rs.getString("ad_glcode"));
			billAccDetail.setOrder(rs.getInt("ad_orderno"));
			billAccDetail.setIsActualDemand(rs.getBoolean("ad_isactualdemand"));
			billAccDetail.setPurpose(PurposeEnum.fromValue(rs.getString("ad_purpose")));

			billAccDetail.setAmount(rs.getBigDecimal("ad_amount"));
			billAccDetail.setAdjustedAmount(rs.getBigDecimal("ad_adjustedamount"));
			billAccDetail.setTaxHeadCode(rs.getString("ad_taxheadcode"));
			billAccDetail.setDemandDetailId(rs.getString("demanddetailid"));

			if (billDetail.getId().equals(billAccDetail.getBillDetail()))
				billDetail.addBillAccountDetailsItem(billAccDetail);

		}
		log.debug("converting map to list object ::: " + billMap.values());
		setUserValuesToBill(billDetailMap.values(), userIds);
		return new ArrayList<>(billMap.values());
	}

	private void setUserValuesToBill(Collection<BillDetail> details, Set<String> userIds) {

		UserSearchCriteria userCriteria = UserSearchCriteria.builder().uuid(userIds).build();
		UserResponse res = rest.postForObject(userContext.concat(userSearchPath), userCriteria, UserResponse.class);
		Map<String, String> usersMap = res.getUsers().stream().collect(Collectors.toMap(User::getUuid, User::getName));
		details.forEach(detail -> detail.getUsers().forEach(user -> user.setName(usersMap.get(user.getId()))));
	}

	private void setTaxAndPayments(List<TaxAndPayment> taxAndPayments, String businessService, BigDecimal totalAmount) {

		if (CollectionUtils.isEmpty(taxAndPayments)) {

			taxAndPayments.add(TaxAndPayment.builder().businessService(businessService).taxAmount(totalAmount).build());
		} else {

			boolean isServiceNotFound = true;

			for (TaxAndPayment taxPayment : taxAndPayments) {

				if (taxPayment.getBusinessService().equalsIgnoreCase(businessService)) {

					isServiceNotFound = false;
					taxPayment.setTaxAmount(taxPayment.getTaxAmount().add(totalAmount));
				}
			}
			if (isServiceNotFound)
				taxAndPayments
						.add(TaxAndPayment.builder().businessService(businessService).taxAmount(totalAmount).build());
		}
	}
}