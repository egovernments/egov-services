/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.demand.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillAccountDetail;
import org.egov.demand.model.BillAccountDetail.PurposeEnum;
import org.egov.demand.model.BillDetail;
import org.egov.demand.model.BillSearchCriteria;
import org.egov.demand.model.BusinessServiceDetail;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.GenerateBillCriteria;
import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.egov.demand.model.TaxAndPayment;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.model.enums.Category;
import org.egov.demand.repository.BillRepository;
import org.egov.demand.util.Constants;
import org.egov.demand.util.SequenceGenService;
import org.egov.demand.util.Util;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
import org.egov.demand.web.contract.BusinessServiceDetailCriteria;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.User;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private ResponseFactory responseFactory;

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private DemandService demandService;

	@Autowired
	private BusinessServDetailService businessServDetailService;

	@Autowired
	private TaxHeadMasterService taxHeadService;
	
	@Autowired
	private GlCodeMasterService glCodeMasterService;
	
	@Autowired
	private SequenceGenService idGenService;
	
	@Autowired
	private Util util;

	public BillResponse searchBill(BillSearchCriteria billCriteria, RequestInfo requestInfo){
		
		return getBillResponse(billRepository.findBill(billCriteria));
	}
	
	public void create(BillRequest billRequest){
		billRepository.saveBill(billRequest);
	}

	/**
	 * Generate bill based on the given criteria
	 * 
	 * @param billCriteria
	 * @param requestInfo
	 * @return
	 */
	public BillResponse generateBill(GenerateBillCriteria billCriteria, RequestInfo requestInfo) {

		Set<String> demandIds = new HashSet<>();
		Set<String> consumerCodes = new HashSet<>();

		if (billCriteria.getDemandId() != null)
			demandIds.add(billCriteria.getDemandId());

		if (billCriteria.getConsumerCode() != null)
			consumerCodes.add(billCriteria.getConsumerCode());

		DemandCriteria demandCriteria = DemandCriteria.builder().businessService(billCriteria.getBusinessService())
				.consumerCode(consumerCodes).demandId(demandIds).email(billCriteria.getEmail()).receiptRequired(false)
				.mobileNumber(billCriteria.getMobileNumber()).tenantId(billCriteria.getTenantId()).build();

		/* Fetching demands for the given bill search criteria */
		DemandResponse demandResponse = demandService.getDemands(demandCriteria, requestInfo);

		List<Demand> demands = demandResponse.getDemands();

		List<Bill> bills;

		if (!demands.isEmpty())
			bills = prepareBill(demands, requestInfo);
		else
			throw new CustomException(Constants.EG_BS_BILL_NO_DEMANDS_FOUND_KEY,
					Constants.EG_BS_BILL_NO_DEMANDS_FOUND_MSG);

		return createAsync(BillRequest.builder().bills(bills).requestInfo(requestInfo).build());
	}

	/**
	 * Prepares the bill object from the list of given demands
	 * 
	 * @param demands demands for which bill should be generated
	 * @param requestInfo 
	 * @return
	 */
	private List<Bill> prepareBill(List<Demand> demands, RequestInfo requestInfo) {

		/* map to keep check on the values of total amount for each business in bill
		 */
		Map<String, TaxAndPayment> serviceCodeAndTaxAmountMap = new HashMap<>();
		User payer = demands.get(0).getPayer();
		
		
		List<BillDetail> billDetails = new ArrayList<>();
		int demandDetailsCount = 0;
		int demandCount = demands.size();
		/*
		 * Fetching Required master data
		 */
		String tenantId = demands.get(0).getTenantId();
		Set<String> businessCodes = new HashSet<>();
		Set<String> taxHeadCodes = new HashSet<>();
		
		for (Demand demand : demands) {

			businessCodes.add(demand.getBusinessService());
			demandDetailsCount = demandDetailsCount + demand.getDemandDetails().size();
			demand.getDemandDetails().forEach(detail -> taxHeadCodes.add(detail.getTaxHeadMasterCode()));
		}
		
		Map<String, TaxHeadMaster> taxHeadMap = getTaxHeadMaster(taxHeadCodes, tenantId, requestInfo);
		Map<String, BusinessServiceDetail> businessMap = getBusinessService(businessCodes, tenantId, requestInfo);

		/* Generating ids for bill */
		List<String> billDetailIds = idGenService.getIds(demandCount, appProps.getBillDetailSeqName());
		List<String> billAccDetailIds = idGenService.getIds(demandDetailsCount, appProps.getBillAccDetailSeqName());
		String billId = idGenService.getIds(demandDetailsCount, appProps.getBillSeqName()).get(0);

		/* looping demand to create bill-detail and account-details object 
		 * 
		 * setting ids to the same
		 */
		int billIndex = 0;
		int billAccDetailIndex = 0;
		for (Demand demand : demands) {

			BillDetail billDetail = getBillDetailForDemand(demand, taxHeadMap, businessMap);
			
			String billDetailId = billDetailIds.get(billIndex++);
			billDetail.setId(billDetailId);
			billDetail.setBill(billId);
			for (BillAccountDetail accDetail : billDetail.getBillAccountDetails()) {
				accDetail.setId(billAccDetailIds.get(billAccDetailIndex++));
				accDetail.setBillDetail(billDetailId);
			}
			
			updateServiceCodeAndTaxAmountMap(serviceCodeAndTaxAmountMap, billDetail);
			billDetails.add(billDetail);
		}
		
		Bill bill = Bill.builder()
				.taxAndPayments(new ArrayList<>(serviceCodeAndTaxAmountMap.values()))
				.auditDetails(util.getAuditDetail(requestInfo))
				.payerAddress(payer.getPermanentAddress())
				.mobileNumber(payer.getMobileNumber())
				.payerName(payer.getName())
				.billDetails(billDetails)
				.tenantId(tenantId)
				.isCancelled(null)
				.isActive(true)
				.id(billId)
				.build();
		
		return Arrays.asList(bill);
	}
	
	/**
	 * updates the total amount to be paid for each business service code
	 * 
	 * from the new bill detail
	 * 
	 * @param serviceCodeAndTaxAmountMap
	 * @param billDetail
	 */
	private void updateServiceCodeAndTaxAmountMap(Map<String, TaxAndPayment> serviceCodeAndTaxAmountMap,
			BillDetail billDetail) {

		String businessCode = billDetail.getBusinessService();
		BigDecimal amountFromBillDetail = billDetail.getTotalAmount();

		/* if business code already exists then add the amounts */
		if (serviceCodeAndTaxAmountMap.containsKey(businessCode)) {

			TaxAndPayment taxAndPayment = serviceCodeAndTaxAmountMap.get(businessCode);
			BigDecimal existingAmount = taxAndPayment.getTaxAmount();
			taxAndPayment.setTaxAmount(existingAmount.add(amountFromBillDetail));
		}else {
			/* if code not present already then put a new entry */
			TaxAndPayment taxAndPayment = TaxAndPayment.builder()
					.businessService(businessCode)
					.taxAmount(amountFromBillDetail)
					.build();
			
			serviceCodeAndTaxAmountMap.put(businessCode, taxAndPayment);
		}
	}

	/**
	 * 
	 * @param demand
	 * @param taxHeadMap
	 * @param businessDetailMap
	 * @return
	 */
	private BillDetail getBillDetailForDemand(Demand demand, Map<String, TaxHeadMaster> taxHeadMap,
			Map<String, BusinessServiceDetail> businessDetailMap) {
		
		Long startPeriod = demand.getTaxPeriodFrom();
		Long endPeriod = demand.getTaxPeriodTo();
		String tenantId = demand.getTenantId();

		BigDecimal collectedAmountForDemand = BigDecimal.ZERO;
		BigDecimal totalAmountForDemand = BigDecimal.ZERO;
		
		BusinessServiceDetail business = businessDetailMap.get(demand.getBusinessService());

		Map<String, BillAccountDetail> taxCodeAccountdetailMap = new HashMap<>();
		
		for(DemandDetail demandDetail : demand.getDemandDetails()) {
			
			
			TaxHeadMaster taxHead = taxHeadMap.get(demandDetail.getTaxHeadMasterCode());
			BigDecimal amountForAccDeatil = demandDetail.getTaxAmount().subtract(demandDetail.getCollectionAmount());

			addOrUpdateBillAccDetailInTaxCodeAccDetailMap(demand, taxCodeAccountdetailMap, demandDetail, taxHead);

			/* Total tax and collection for the whole demand/bill-detail */
			totalAmountForDemand = totalAmountForDemand.add(amountForAccDeatil);
			collectedAmountForDemand = collectedAmountForDemand.add(demandDetail.getCollectionAmount());
		}
		
		return BillDetail.builder()
				.billAccountDetails(new ArrayList<>(taxCodeAccountdetailMap.values()))
				.collectionModesNotAllowed(business.getCollectionModesNotAllowed())
				.minimumAmount(demand.getMinimumAmountPayable())
				.businessService(business.getBusinessService())
				.collectedAmount(collectedAmountForDemand)
				.consumerCode(demand.getConsumerCode())
				.consumerType(demand.getConsumerType())
				.billDate(System.currentTimeMillis())
				.totalAmount(totalAmountForDemand)
				.demandId(demand.getId())
				.fromPeriod(startPeriod)
				.toPeriod(endPeriod)
				.tenantId(tenantId)
				.billNumber(null)
				.status(null)
				.id(null)
				.build();
	}

	/**
	 * creates/ updates bill-account details based on the tax-head code
	 * in taxCodeAccDetailMap
	 * 
	 * @param startPeriod
	 * @param endPeriod
	 * @param tenantId
	 * @param taxCodeAccDetailMap
	 * @param demandDetail
	 * @param taxHead
	 * @param amountForAccDeatil
	 */
	private void addOrUpdateBillAccDetailInTaxCodeAccDetailMap(Demand demand, Map<String, BillAccountDetail> taxCodeAccDetailMap,
			DemandDetail demandDetail, TaxHeadMaster taxHead) {
		
		Long startPeriod = demand.getTaxPeriodFrom();
		Long endPeriod = demand.getTaxPeriodTo();
		String tenantId = demand.getTenantId();

		BigDecimal newAmountForAccDeatil = demandDetail.getTaxAmount().subtract(demandDetail.getCollectionAmount());
		/*
		 * 	BAD - BillAccountDetail
		 * 
		 *  To handle repeating tax-head codes in demand
		 *  
		 *  And merge them in to single BAD 
		 * 
		 *  if taxHeadCode found in map then add the amount to existing BAD
		 *  
		 *  else create and add a new BAD
		 */
		if (taxCodeAccDetailMap.containsKey(taxHead.getCode())) {

			BillAccountDetail existingAccDetail = taxCodeAccDetailMap.get(taxHead.getCode());
			BigDecimal existingAmtForAccDetail = existingAccDetail.getAmount();
			existingAccDetail.setAmount(existingAmtForAccDetail.add(newAmountForAccDeatil));
			
		} else {

			PurposeEnum purpose = getPurpose(taxHead.getCategory(), taxHead.getCode(), startPeriod, endPeriod);

			BillAccountDetail accountDetail = BillAccountDetail.builder()
					.isActualDemand(taxHead.getIsActualDemand())
					.demandDetailId(demandDetail.getId())
					.adjustedAmount(BigDecimal.ZERO)
					.taxHeadCode(taxHead.getCode())
					.amount(newAmountForAccDeatil)
					.order(taxHead.getOrder())
					.tenantId(tenantId)
					.purpose(purpose)
					.build();
		
			taxCodeAccDetailMap.put(taxHead.getCode(), accountDetail);
		}
	}

	/**
	 * Returns Applies the purpose on Bill Account Details
	 * 
	 * @param category
	 * @param taxHeadCode
	 * @param taxPeriodFrom
	 * @param taxPeriodTo
	 * @return
	 */
	private PurposeEnum getPurpose(Category category, String taxHeadCode, Long taxPeriodFrom, Long taxPeriodTo) {

		Long currDate = System.currentTimeMillis();

		if (category.equals(Category.TAX) || category.equals(Category.FEE) || category.equals(Category.CHARGES)) {

			if (taxPeriodFrom <= currDate && taxPeriodTo >= currDate)
				return PurposeEnum.CURRENT;
			else if (currDate > taxPeriodTo)
				return PurposeEnum.ARREAR;
			else
				return PurposeEnum.ADVANCE;
		} else if (category.equals(Category.ADVANCE_COLLECTION)) {
			return PurposeEnum.ADVANCE;
		} else {
			return PurposeEnum.OTHERS;
		}
	}

	/**
	 * Fetches the tax-head master data for the given tax-head codes
	 * 
	 * @param demands  list of demands for which tax-heads needs to searched
	 * @param tenantId tenant-id of the request
	 * @param info     RequestInfo object
	 * @return returns a map of tax-head code as key and tax-head object as value
	 */
	private Map<String, TaxHeadMaster> getTaxHeadMaster(Set<String> taxHeadCodes, String tenantId, RequestInfo info) {

		TaxHeadMasterCriteria taxHeadCriteria = TaxHeadMasterCriteria.builder().tenantId(tenantId).code(taxHeadCodes)
				.build();
		List<TaxHeadMaster> taxHeads = taxHeadService.getTaxHeads(taxHeadCriteria, info).getTaxHeadMasters();

		if (taxHeads.isEmpty())
			throw new CustomException("EG_BS_TAXHEADCODE_EMPTY", "No taxhead masters found for the given codes");

		return taxHeads.stream().collect(Collectors.toMap(TaxHeadMaster::getCode, Function.identity()));
	}

	
	/**
	 * To Fetch the businessServiceDetail master based on the business codes
	 * 
	 * @param businessService
	 * @param tenantId
	 * @param requestInfo
	 * @return returns a map with business code and businessDetail object
	 */
	private Map<String, BusinessServiceDetail> getBusinessService(Set<String> businessService, String tenantId, RequestInfo requestInfo) {
		List<BusinessServiceDetail> businessServiceDetails = businessServDetailService.searchBusinessServiceDetails(BusinessServiceDetailCriteria.builder().businessService(businessService).tenantId(tenantId).build(), requestInfo)
				.getBusinessServiceDetails();
		return businessServiceDetails.stream().collect(Collectors.toMap(BusinessServiceDetail::getBusinessService, Function.identity()));
	}
	
	public BillResponse getBillResponse(List<Bill> bills) {
		BillResponse billResponse = new BillResponse();
		billResponse.setBill(bills);
		return billResponse;
	}

	/*
	 * @deprecated methods 
	 * 
	 */

	public BillResponse createAsync(BillRequest billRequest) {

		//billHelper.getBillRequestWithIds(billRequest);

		try {
			kafkaTemplate.send(appProps.getCreateBillTopic(),appProps.getCreateBillTopicKey(),billRequest);
		} catch (Exception e) {
			log.debug("BillService createAsync:"+e);
			throw new RuntimeException(e);

		}
		return getBillResponse(billRequest.getBills());
	}
	
	@Deprecated
	private Map<String, List<GlCodeMaster>> getGlCodes(List<Demand> demands, String service,String tenantId, RequestInfo requestInfo) {

		List<DemandDetail> demandDetails = new ArrayList<>();
		for(Demand demand : demands){
			demandDetails.addAll(demand.getDemandDetails());
		}

		log.debug("getGlCodes demandDetails:"+demandDetails);

		Set<String>  taxHeadMasterCode = demandDetails.stream().
				map(demandDetail -> demandDetail.getTaxHeadMasterCode()).collect(Collectors.toSet());

		log.debug("getGlCodes taxHeadMasterCode:"+taxHeadMasterCode);
		List<GlCodeMaster> glCodeMasters = glCodeMasterService.getGlCodes(
				GlCodeMasterCriteria.builder().taxHead(taxHeadMasterCode).service(
				service).tenantId(tenantId).build(), requestInfo).getGlCodeMasters();
		log.debug("getGlCodes glCodeMasters:"+glCodeMasters);
		if(glCodeMasters.isEmpty())
			throw new RuntimeException("No GlcodeMasters found for the given criteria");
		Map<String, List<GlCodeMaster>> map = glCodeMasters.stream().collect(
				Collectors.groupingBy(GlCodeMaster::getTaxHead, Collectors.toList()));

		log.debug("getTaxHeadMaster map:"+map);
		return map;
	}
	
	@Deprecated
	public BillResponse apportion(BillRequest billRequest) {
		return new BillResponse(responseFactory.getResponseInfo(billRequest.getRequestInfo(), HttpStatus.OK), billRepository.apportion(billRequest));
	}
}
