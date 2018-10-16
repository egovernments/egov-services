/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.demand.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillAccountDetail;
import org.egov.demand.model.BillDetail;
import org.egov.demand.model.CollectedReceipt;
import org.egov.demand.model.ConsolidatedTax;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.DemandDetailCriteria;
import org.egov.demand.model.DemandDue;
import org.egov.demand.model.DemandDueCriteria;
import org.egov.demand.model.DemandUpdateMisRequest;
import org.egov.demand.model.Owner;
import org.egov.demand.model.enums.Status;
import org.egov.demand.producer.Producer;
import org.egov.demand.repository.DemandRepository;
import org.egov.demand.repository.OwnerRepository;
import org.egov.demand.util.DemandEnrichmentUtil;
import org.egov.demand.util.SequenceGenService;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.DemandDetailResponse;
import org.egov.demand.web.contract.DemandDueResponse;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.ReceiptRequest;
import org.egov.demand.web.contract.UserSearchRequest;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemandService {

	private static final Logger logger = LoggerFactory.getLogger(DemandService.class);

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private SequenceGenService sequenceGenService;

	@Autowired
	private DemandRepository demandRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ResponseFactory responseInfoFactory;

	@Autowired
	private DemandEnrichmentUtil demandEnrichmentUtil;
	
	@Autowired
	private Producer producer;
	
	public DemandResponse create(DemandRequest demandRequest) {

		logger.info("the demand service : " + demandRequest);
		RequestInfo requestInfo = demandRequest.getRequestInfo();
		List<Demand> demands = demandRequest.getDemands();
		List<DemandDetail> demandDetails = new ArrayList<>();
		AuditDetail auditDetail = getAuditDetail(demandRequest.getRequestInfo());

		int currentDemandId = 0;
		int demandsSize = demands.size();
		List<String> demandIds = sequenceGenService.getIds(demandsSize, applicationProperties.getDemandSeqName());

		for (Demand demand : demands) {
			String demandId = demandIds.get(currentDemandId++);
			demand.setId(demandId);
			if (demand.getMinimumAmountPayable() == null)
				demand.setMinimumAmountPayable(BigDecimal.ZERO);
			demand.setAuditDetail(auditDetail);
			String tenantId = demand.getTenantId();
			for (DemandDetail demandDetail : demand.getDemandDetails()) {
				demandDetail.setDemandId(demandId);
				demandDetail.setTenantId(tenantId);
				demandDetail.setAuditDetail(auditDetail);
				demandDetails.add(demandDetail);
			}
		}

		int demandDetailsSize = demandDetails.size();
		List<String> demandDetailIds = sequenceGenService.getIds(demandDetailsSize,
				applicationProperties.getDemandDetailSeqName());
		int currentDetailId = 0;
		for (DemandDetail demandDetail : demandDetails) {
			if (demandDetail.getCollectionAmount() == null)
				demandDetail.setCollectionAmount(BigDecimal.ZERO);
			demandDetail.setId(demandDetailIds.get(currentDetailId++));
		}
		save(demandRequest);
		//producer.push(applicationProperties.getDemandIndexTopic(), demandRequest);
		return new DemandResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), demands);
	}
	
	public DemandResponse  updateDemandFromReceipt(ReceiptRequest receiptRequest, Status status)
	{
	    BillRequest billRequest=new BillRequest();
	    if(receiptRequest !=null && receiptRequest.getReceipt() !=null && !receiptRequest.getReceipt().isEmpty()){
	    billRequest.setRequestInfo(receiptRequest.getRequestInfo());
	    List<Bill> bills=receiptRequest.getReceipt().get(0).getBill();
	    for(Bill bill:bills){
	    	for(BillDetail billDetail: bill.getBillDetails())
	    		billDetail.setStatus(status);
	    }
	    billRequest.setBills(bills);
	    }
	    return updateDemandFromBill(billRequest, false);
	    
	    
	}
	
	
	public DemandResponse updateDemandFromBill(BillRequest billRequest, Boolean isReceiptCancellation) {
	    
		log.info("THE recieved bill request object------"+billRequest);
	    if(billRequest !=null && billRequest.getBills()!=null){

		List<Bill> bills = billRequest.getBills();
		RequestInfo requestInfo = billRequest.getRequestInfo();
		String tenantId = bills.get(0).getTenantId();
		Set<String> consumerCodes = new HashSet<>();
		for (Bill bill : bills) {
			for (BillDetail billDetail : bill.getBillDetails())
				consumerCodes.add(billDetail.getConsumerCode());
		}
		DemandCriteria demandCriteria = DemandCriteria.builder().consumerCode(consumerCodes).receiptRequired(false).tenantId(tenantId).build();
		List<Demand> demands = getDemands(demandCriteria, requestInfo).getDemands();
		log.info("THE DEMAND FETCHED FROM DB FOR THE GIVEN RECIEPT--------"+demands);
		Map<String, Demand> demandIdMap = demands.stream()
				.collect(Collectors.toMap(Demand::getId, Function.identity()));
		Map<String, List<Demand>> demandListMap = new HashMap<>();
		for (Demand demand : demands) {

			if (demandListMap.get(demand.getConsumerCode()) == null) {
				List<Demand> demands2 = new ArrayList<>();
				demands2.add(demand);
				demandListMap.put(demand.getConsumerCode(), demands2);
			} else
				demandListMap.get(demand.getConsumerCode()).add(demand);
		}

		for (Bill bill : bills) {
			for (BillDetail billDetail : bill.getBillDetails()) {

				List<Demand> demands2 = demandListMap.get(billDetail.getConsumerCode());
				Map<String, List<DemandDetail>> detailsMap = new HashMap<>();
				for (Demand demand : demands2) {
					for (DemandDetail demandDetail : demand.getDemandDetails()) {
						if (detailsMap.get(demandDetail.getTaxHeadMasterCode()) == null) {
							List<DemandDetail> demandDetails = new ArrayList<>();
							demandDetails.add(demandDetail);
							detailsMap.put(demandDetail.getTaxHeadMasterCode(), demandDetails);
						} else
							detailsMap.get(demandDetail.getTaxHeadMasterCode()).add(demandDetail);
					}
				}
					for (BillAccountDetail accountDetail : billDetail.getBillAccountDetails()) {

						if (accountDetail.getAccountDescription() != null && accountDetail.getCreditAmount() != null) {
							String[] array = accountDetail.getAccountDescription().split("-");
							log.info("the string array of values--------" + array.toString());

							List<String> accDescription = Arrays.asList(array);
							String taxHeadCode = accDescription.get(0);
							Long fromDate = Long.valueOf(accDescription.get(1));
							Long toDate = Long.valueOf(accDescription.get(2));

							for (DemandDetail demandDetail : detailsMap.get(taxHeadCode)) {
								log.info("the current demand detail : " + demandDetail);
								Demand demand = demandIdMap.get(demandDetail.getDemandId());
								log.info("the respective deman" + demand);

								if (fromDate.equals(demand.getTaxPeriodFrom())
										&& toDate.equals(demand.getTaxPeriodTo())) {

									BigDecimal collectedAmount = accountDetail.getCreditAmount();
									log.info("the credit amt :" + collectedAmount);
									//demandDetail.setTaxAmount(demandDetail.getTaxAmount().subtract(collectedAmount));
									/*
									 * If receipt cancellation is true, it will subtract the creditAmount from the collectionAmount
									 */
									if(isReceiptCancellation) {
										demandDetail.setCollectionAmount(
												demandDetail.getCollectionAmount().subtract(collectedAmount));
									}else {
										demandDetail.setCollectionAmount(
												demandDetail.getCollectionAmount().add(collectedAmount));
									}
									log.info("the setTaxAmount ::: " + demandDetail.getTaxAmount());
									log.info("the setCollectionAmount ::: " + demandDetail.getCollectionAmount());
								}
							}
						}
					}
				}
		}
		
		demandRepository.update(new DemandRequest(requestInfo,demands));
	        DemandResponse demandResponse=new DemandResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK),demands);
                kafkaTemplate.send(applicationProperties.getUpdateDemandBillTopicName(), demandResponse);
                
                kafkaTemplate.send(applicationProperties.getSaveCollectedReceipts(), billRequest);
                
		return demandResponse;
	    }
            return null;
	}
	
	public void saveCollectedReceipts(BillRequest billRequest){
		List<BillDetail> billDetails=new ArrayList<>();
		for(Bill bill:billRequest.getBills()){
			for(BillDetail detail:bill.getBillDetails())
				billDetails.add(detail);
		}
		demandRepository.saveCollectedReceipts(billDetails,billRequest.getRequestInfo());
	}

	public DemandResponse updateCollection(DemandRequest demandRequest) {

		log.debug("the demand service : " + demandRequest);
		RequestInfo requestInfo = demandRequest.getRequestInfo();
		List<Demand> demands = demandRequest.getDemands();
		AuditDetail auditDetail = getAuditDetail(requestInfo);

		Map<String, Demand> demandMap = demands.stream().collect(Collectors.toMap(Demand::getId, Function.identity()));
		Map<String, DemandDetail> demandDetailMap = new HashMap<>();
		for (Demand demand : demands) {
			for (DemandDetail demandDetail : demand.getDemandDetails())
				demandDetailMap.put(demandDetail.getId(), demandDetail);
		}
		DemandCriteria demandCriteria = DemandCriteria.builder().demandId(demandMap.keySet())
				.tenantId(demands.get(0).getTenantId()).build();
		List<Demand> existingDemands = demandRepository.getDemands(demandCriteria, null);
		 
		for (Demand demand : existingDemands) {

			AuditDetail demandAuditDetail = demand.getAuditDetail();
			demandAuditDetail.setLastModifiedBy(auditDetail.getLastModifiedBy());
			demandAuditDetail.setLastModifiedTime(auditDetail.getLastModifiedTime());

			for (DemandDetail demandDetail : demand.getDemandDetails()) {
				DemandDetail demandDetail2 = demandDetailMap.get(demandDetail.getId());
				BigDecimal tax = demandDetail.getTaxAmount().subtract(demandDetail2.getCollectionAmount());
				if(tax.doubleValue()>=0){
				//demandDetail.setTaxAmount(tax);
				demandDetail.setCollectionAmount(demandDetail.getCollectionAmount().add(demandDetail2.getCollectionAmount()));
				}
				
				AuditDetail demandDetailAudit = demandDetail.getAuditDetail();
				demandDetailAudit.setLastModifiedBy(auditDetail.getLastModifiedBy());
				demandDetailAudit.setLastModifiedTime(auditDetail.getLastModifiedTime());
			}
		}
		demandRequest.setDemands(existingDemands);
		kafkaTemplate.send(applicationProperties.getUpdateDemandTopic(), demandRequest);
		return new DemandResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED),
				existingDemands);
	}
	
	public DemandResponse updateAsync(DemandRequest demandRequest) {

		log.debug("the demand service : " + demandRequest);
		RequestInfo requestInfo = demandRequest.getRequestInfo();
		List<Demand> demands = demandRequest.getDemands();
		AuditDetail auditDetail = getAuditDetail(requestInfo);

		List<Demand> newDemands = new ArrayList<>();
		List<DemandDetail> newDemandDetails = new ArrayList<>();

		for (Demand demand : demands) {
			if (demand.getId() == null) {
				String demandId = sequenceGenService.getIds(1, applicationProperties.getDemandSeqName()).get(0);
				demand.setId(demandId);
				demand.setAuditDetail(auditDetail);
				newDemands.add(demand);
				for (DemandDetail demandDetail : demand.getDemandDetails()) {
					demandDetail.setDemandId(demandId);
					demandDetail.setAuditDetail(auditDetail);
					newDemandDetails.add(demandDetail);
				}
			} else {
				AuditDetail auditDetailUpdate = demand.getAuditDetail();
				auditDetailUpdate.setLastModifiedBy(auditDetail.getLastModifiedBy());
				auditDetailUpdate.setLastModifiedTime(auditDetail.getLastModifiedTime());
				for (DemandDetail demandDetail : demand.getDemandDetails()) {
					if (demandDetail.getId() == null) {
						demandDetail.setDemandId(demand.getId());
						demandDetail.setAuditDetail(auditDetail);
						newDemandDetails.add(demandDetail);
					} else {
						AuditDetail demandDetailAudit = demandDetail.getAuditDetail();
						demandDetailAudit.setLastModifiedBy(auditDetail.getLastModifiedBy());
						demandDetailAudit.setLastModifiedTime(auditDetail.getLastModifiedTime());
					}
				}
			}
		}
		int demandSetailSize = newDemandDetails.size();
		List<String> DemandDetailsId = sequenceGenService.getIds(demandSetailSize,
				applicationProperties.getDemandDetailSeqName());
		int i = 0;
		for (DemandDetail demandDetail : newDemandDetails) {
			demandDetail.setId(DemandDetailsId.get(i++));
		}
		update(demandRequest);
		producer.push(applicationProperties.getDemandIndexTopic(), demandRequest);
		return new DemandResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), demands);
	}

	public DemandResponse getDemands(DemandCriteria demandCriteria, RequestInfo requestInfo) {
		
		UserSearchRequest userSearchRequest = null;
		List<Owner> owners = null;
		List<Demand> demands = null;
		List<CollectedReceipt> receipts=null;
		if (demandCriteria.getEmail() != null || demandCriteria.getMobileNumber() != null) {
			userSearchRequest = UserSearchRequest.builder().requestInfo(requestInfo)
					.tenantId(demandCriteria.getTenantId()).emailId(demandCriteria.getEmail())
					.mobileNumber(demandCriteria.getMobileNumber()).pageSize(500).userType("CITIZEN").build();
			// TODO GET PAGE SIZE VALUE FROM CONFIG DONT HARD CODE
			owners = ownerRepository.getOwners(userSearchRequest);
			Set<String> ownerIds = owners.stream().map(owner -> owner.getId().toString()).collect(Collectors.toSet());
			demands = demandRepository.getDemands(demandCriteria, ownerIds);
			demands.sort(Comparator.comparing(Demand::getTaxPeriodFrom));
		} else {
			demands = demandRepository.getDemands(demandCriteria, null);
			if (!demands.isEmpty()) {
				demands.sort(Comparator.comparing(Demand::getTaxPeriodFrom));
				List<Long> ownerIds = new ArrayList<>(demands.stream().filter(demand -> null != demand.getOwner().getId())
						.map(demand -> demand.getOwner().getId()).collect(Collectors.toSet()));

				if (!CollectionUtils.isEmpty(ownerIds)) {

					userSearchRequest = UserSearchRequest.builder().requestInfo(requestInfo)
							.tenantId(demandCriteria.getTenantId()).id(ownerIds).userType("CITIZEN").pageSize(500)
							.build();
					owners = ownerRepository.getOwners(userSearchRequest);
				}
			}
		}
		if (!CollectionUtils.isEmpty(demands) && !CollectionUtils.isEmpty(owners))
			demands = demandEnrichmentUtil.enrichOwners(demands, owners);
		for (Demand demand : demands) {
			demand.getDemandDetails().sort(Comparator.comparing(DemandDetail::getTaxHeadMasterCode));
		}
		if (demandCriteria.getReceiptRequired())
			receipts = demandRepository.getCollectedReceipts(demandCriteria);
		return new DemandResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK), demands, receipts);
	}

	public DemandDetailResponse getDemandDetails(DemandDetailCriteria demandDetailCriteria, RequestInfo requestInfo) {

		return new DemandDetailResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK),
				demandRepository.getDemandDetails(demandDetailCriteria));
	}

	public void save(DemandRequest demandRequest) {
		demandRepository.save(demandRequest);
	}

	public void update(DemandRequest demandRequest) {
		demandRepository.update(demandRequest);
	}
	
	//Demand update consumer code (update mis)
	public DemandResponse updateMISAsync(DemandUpdateMisRequest demandRequest) {

		kafkaTemplate.send(applicationProperties.getUpdateMISTopicName(), demandRequest);

		return new DemandResponse(responseInfoFactory.getResponseInfo(new RequestInfo(), HttpStatus.CREATED), null);
	}
	
	//update mis update method calling from kafka
	public void updateMIS(DemandUpdateMisRequest demandRequest){
		demandRepository.updateMIS(demandRequest);
	}
	
	public DemandDueResponse getDues(DemandDueCriteria demandDueCriteria, RequestInfo requestInfo) {
		
		Long currDate = new Date().getTime();
		Double currTaxAmt = 0d;
		Double currCollAmt = 0d;
		Double arrTaxAmt = 0d;
		Double arrCollAmt = 0d;

		DemandCriteria demandCriteria = DemandCriteria.builder().tenantId(demandDueCriteria.getTenantId())
				.businessService(demandDueCriteria.getBusinessService())
				.consumerCode(demandDueCriteria.getConsumerCode()).receiptRequired(false).build();
		
		List<Demand> demands = getDemands(demandCriteria, requestInfo).getDemands();
		for (Demand demand : demands) {
			if (demand.getTaxPeriodFrom() <= currDate && currDate <= demand.getTaxPeriodTo()) {
				for (DemandDetail detail : demand.getDemandDetails()) {
					currTaxAmt = currTaxAmt + detail.getTaxAmount().doubleValue();
					currCollAmt = currCollAmt + detail.getCollectionAmount().doubleValue();
				}
			} else if(currDate > demand.getTaxPeriodTo()){
				for (DemandDetail detail : demand.getDemandDetails()) {
					arrTaxAmt = arrTaxAmt + detail.getTaxAmount().doubleValue();
					arrCollAmt = arrCollAmt + detail.getCollectionAmount().doubleValue();
				}
			}
		}
		ConsolidatedTax consolidatedTax = ConsolidatedTax.builder().arrearsBalance(arrTaxAmt - arrCollAmt)
				.currentBalance(currTaxAmt - currCollAmt).arrearsDemand(arrTaxAmt).arrearsCollection(arrCollAmt)
				.currentDemand(currTaxAmt).currentCollection(currCollAmt).build();
		
		DemandDue due = DemandDue.builder().consolidatedTax(consolidatedTax).demands(demands).build();

		return new DemandDueResponse(responseInfoFactory.getResponseInfo(new RequestInfo(), HttpStatus.OK), due);
	}
	
	private AuditDetail getAuditDetail(RequestInfo requestInfo) {

		String userId = requestInfo.getUserInfo().getId().toString();
		Long currEpochDate = new Date().getTime();

		AuditDetail auditDetail = new AuditDetail();
		auditDetail.setCreatedBy(userId);
		auditDetail.setCreatedTime(currEpochDate);
		auditDetail.setLastModifiedBy(userId);
		auditDetail.setLastModifiedTime(currEpochDate);
		return auditDetail;
	}
	
}
