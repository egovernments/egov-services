package org.egov.marriagefee.repository;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.egov.common.contract.request.RequestInfo;
import org.egov.marriagefee.config.PropertiesManager;
import org.egov.marriagefee.model.Bill;
import org.egov.marriagefee.model.Demand;
import org.egov.marriagefee.model.DemandDetail;
import org.egov.marriagefee.model.GenerateBillCriteria;
import org.egov.marriagefee.model.MarriageRegn;
import org.egov.marriagefee.model.Owner;
import org.egov.marriagefee.model.Receipt;
import org.egov.marriagefee.model.ReceiptReq;
import org.egov.marriagefee.model.ReceiptRes;
import org.egov.marriagefee.web.contract.BillRequest;
import org.egov.marriagefee.web.contract.BillResponse;
import org.egov.marriagefee.web.contract.DemandRequest;
import org.egov.marriagefee.web.contract.DemandResponse;
import org.egov.marriagefee.web.contract.MarriageRegnRequest;
import org.egov.marriagefee.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DemandServiceRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public List<Demand> prepareDemand(MarriageRegn marriageRegn) {
		System.err.println("  DemandServiceRepository  prepareDemand" + marriageRegn);
		log.info("DemandServiceRepository prepareDemand,  --> ");
		List<Demand> demandList = new ArrayList<Demand>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		Demand demand;
		DemandDetail demandDetail;
		String tenantId = marriageRegn.getTenantId();
		demand = new Demand();
		demand.setTenantId(marriageRegn.getDemands().get(0).getTenantId());
		demand.setBusinessService(marriageRegn.getDemands().get(0).getBusinessService());
		demand.setMinimumAmountPayable(BigDecimal.ONE);
		demand.setDemandDetails(marriageRegn.getDemands().get(0).getDemandDetails());
		demand.setConsumerCode(marriageRegn.getDemands().get(0).getConsumerCode());
		demand.setConsumerType(marriageRegn.getDemands().get(0).getConsumerType());
		// try {
		// log.info("Fee fromDate = " + marriageRegn.getFee().getFromDate()+ "
		// \n to"+marriageRegn.getFee().getToDate());
		// fromDate = sdf.
		// toDate = sdf.parse(marriageRegn.getFee().getToDate().toString());
		// log.info(" Dates, fromDate = "+fromDate+", toDate = "+toDate+
		// " \n Epoch values, fromDate = " + fromDate.getTime() + " \n toDate =
		// " + toDate.getTime());
		demand.setTaxPeriodFrom(marriageRegn.getDemands().get(0).getTaxPeriodFrom());
		demand.setTaxPeriodTo(marriageRegn.getDemands().get(0).getTaxPeriodTo());
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		demand.setAuditDetail(marriageRegn.getAuditDetails());
		demand.setOwner(marriageRegn.getDemands().get(0).getOwner());
		demandList.add(demand);
		System.err.println(" prepareDemand demandList" + demandList);
		return demandList;
	}

	public DemandResponse createDemand(List<Demand> demands, RequestInfo requestInfo) {
		System.err.println(" DemandServiceRepository createDemand requestInfo" + requestInfo);
		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemands(demands);

		log.info("DemandServiceRepository createDemand(), demands --> " + demands);

		String url = propertiesManager.getBillingServiceHostName() + propertiesManager.getBillingServiceCreatedDemand();
		log.info("DemandServiceRepository createDemand(), URL - > " + url + " \n demandRequest --> " + demandRequest);

		return restTemplate.postForObject(url, demandRequest, DemandResponse.class);
	}

	public ResponseEntity<?> generatebillForMarriageRegnDemand(List<Demand> demand, RequestInfo requestInfo) {
		log.info("DemandServiceRepository  generatebillForMarriageRegnDemand demand" + demand);
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		String url = (propertiesManager.getBillingServiceHostName() + propertiesManager.getBillingServiceGenerateBill()
				+ "?" + "tenantId=" + demand.get(0).getTenantId() + "&" + "mobileNumber="
				+ demand.get(0).getOwner().getMobileNumber());
		log.info("DemandServiceRepository  generatebillForMarriageRegnDemand url" + url);

		return new ResponseEntity<>(restTemplate.postForObject(url, requestInfoWrapper, BillResponse.class),
				HttpStatus.CREATED);
	}

	public ResponseEntity<?> generaterecieptForMarriagerRegnPayment(List<Bill> bills, RequestInfo requestInfo) {
		log.info("DemandServiceRepository  generaterecieptForMarriagerRegnPayment bills" + bills);
		Receipt receipt = new Receipt();
		receipt.setBill(bills);
		List<Receipt> receipts = new ArrayList<>();
		receipts.add(receipt);
		ReceiptReq receiptReq = new ReceiptReq();
		receiptReq.setRequestInfo(requestInfo);
		receiptReq.setReceipt(receipts);
		log.info("DemandServiceRepository  generaterecieptForMarriagerRegnPayment receiptReq" + receiptReq);
		String url =propertiesManager.getCollectionServiceHost()+ propertiesManager.getCollectionServiceGenerateReceipt();
		log.info("DemandServiceRepository  generaterecieptForMarriagerRegnPayment url" + url);

		return new ResponseEntity<>(restTemplate.postForObject(url,receiptReq , ReceiptRes.class),
				HttpStatus.CREATED);

	}

}
