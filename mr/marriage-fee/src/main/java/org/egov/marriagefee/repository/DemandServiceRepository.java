package org.egov.marriagefee.repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.egov.marriagefee.model.TaxHeadMaster;
import org.egov.marriagefee.model.TaxPeriod;
import org.egov.marriagefee.model.enums.Source;
import org.egov.marriagefee.web.contract.BillRequest;
import org.egov.marriagefee.web.contract.BillResponse;
import org.egov.marriagefee.web.contract.DemandRequest;
import org.egov.marriagefee.web.contract.DemandResponse;
import org.egov.marriagefee.web.contract.MarriageRegnRequest;
import org.egov.marriagefee.web.contract.RequestInfoWrapper;
import org.egov.marriagefee.web.contract.TaxHeadMasterResponse;
import org.egov.marriagefee.web.contract.TaxPeriodResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
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

	public List<Demand> prepareDemand(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		log.info("  DemandServiceRepository  prepareDemand" + marriageRegnRequest.getMarriageRegn());
		log.info("DemandServiceRepository prepareDemand,  --> ");

		List<Demand> demandList = new ArrayList<Demand>();
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		Demand demand = new Demand();
		String tenantId = marriageRegn.getTenantId();

		if (marriageRegn.getSource().equals(Source.DATA_ENTRY)) {
			demandList.addAll(marriageRegn.getDemands());
			log.info(" prepareDemand demandList" + demandList);

		} else {

			final Calendar calendar = Calendar.getInstance();
			log.info(" String date1" + calendar);
			calendar.setTimeInMillis(marriageRegn.getMarriageDate());
			log.info(" String date2 after setting millisec" + calendar);
			final int year = calendar.get(Calendar.YEAR);
			String financialyear = year + "-" + (year + 1);
			log.info("  financialyear " + financialyear);

			String url = (propertiesManager.getBillingServiceHostName() + propertiesManager.getTaxPeriods() + "?"
					+ "tenantId=" + tenantId + "&" + "service=" + propertiesManager.getDemandBusinessService()+"&"+"financialYear=" + financialyear);
			log.info("DemandServiceRepository prepareDemand  urlfortaxperiod " + url);
			ResponseEntity<?> taxPeriodResponseEntity = restTemplate.postForEntity(url, requestInfoWrapper,
					TaxPeriodResponse.class);
			TaxPeriodResponse taxPeriod = (TaxPeriodResponse) taxPeriodResponseEntity.getBody();

			log.info("  DemandServiceRepository  prepareDemand taxPeriod"
					+ taxPeriod.getTaxPeriods().get(0).getFromDate());

			String urlForTaxHead = (propertiesManager.getBillingServiceHostName() + propertiesManager.getTaxHead() + "?"
					+ "tenantId=" + tenantId + "&" + "service=" + propertiesManager.getDemandBusinessService() + "&"
					+ "code=" + propertiesManager.getTaxHeadCode());
			log.info("DemandServiceRepository prepareDemand  urlForTaxHead " + urlForTaxHead);
			ResponseEntity<?> taxHeadResponse = restTemplate.postForEntity(urlForTaxHead, requestInfoWrapper,
					TaxHeadMasterResponse.class);
			TaxHeadMasterResponse TaxHeadMaster = (TaxHeadMasterResponse) taxHeadResponse.getBody();
			log.info("  DemandServiceRepository  prepareDemand TaxHeadMaster"
					+ TaxHeadMaster.getTaxHeadMasters().get(0).getCode());
			DemandDetail demandDetail = new DemandDetail();

			demandDetail.setTenantId(tenantId);
			demandDetail.setTaxHeadMasterCode(TaxHeadMaster.getTaxHeadMasters().get(0).getCode());
			demandDetail.setAuditDetail(marriageRegn.getAuditDetails());
			demandDetail.setTaxAmount(marriageRegn.getFee().getFee());
			List<DemandDetail> demandDetails = new ArrayList<>();
			demandDetails.add(demandDetail);
			log.info("demandDetail" + demandDetail);
			demand.setDemandDetails(demandDetails);

			demand.setTenantId(marriageRegn.getTenantId());
			demand.setBusinessService(propertiesManager.getDemandBusinessService());
			demand.setMinimumAmountPayable(BigDecimal.ONE);
			demand.setTaxPeriodFrom(taxPeriod.getTaxPeriods().get(0).getFromDate());
			demand.setTaxPeriodTo(taxPeriod.getTaxPeriods().get(0).getToDate());
			demand.setAuditDetail(marriageRegn.getAuditDetails());
			demand.setConsumerCode(marriageRegn.getApplicationNumber().toString());
			demand.setConsumerType("consumertype");
			Owner owner = new Owner();
			owner.setId(requestInfo.getUserInfo().getId());
			owner.setTenantId(tenantId);
			owner.setEmailId(requestInfo.getUserInfo().getEmailId());
			owner.setMobileNumber(requestInfo.getUserInfo().getMobileNumber());
			demand.setOwner(owner);

			demandList.add(demand);
		}

		log.info("  DemandServiceRepository  prepareDemand demandList" + demandList);

		return demandList;
	}

	public DemandResponse createDemand(List<Demand> demands, RequestInfo requestInfo) {
		log.info(" DemandServiceRepository createDemand requestInfo" + requestInfo);
		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemands(demands);

		log.info("DemandServiceRepository createDemand(), demands --> " + demands);

		String url = propertiesManager.getBillingServiceHostName() + propertiesManager.getBillingServiceCreatedDemand();
		log.info("DemandServiceRepository createDemand(), URL - > " + url + " \n demandRequest --> " + demandRequest);

		return restTemplate.postForObject(url, demandRequest, DemandResponse.class);
	}

/*	public ResponseEntity<?> generatebillForMarriageRegnDemand(List<Demand> demand, RequestInfo requestInfo) {
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
		String url = propertiesManager.getCollectionServiceHost()
				+ propertiesManager.getCollectionServiceGenerateReceipt();
		log.info("DemandServiceRepository  generaterecieptForMarriagerRegnPayment url" + url);

		return new ResponseEntity<>(restTemplate.postForObject(url, receiptReq, ReceiptRes.class), HttpStatus.CREATED);

	}*/

}
