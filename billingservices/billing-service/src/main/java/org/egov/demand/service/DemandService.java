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
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.DemandDetailCriteria;
import org.egov.demand.model.Owner;
import org.egov.demand.repository.DemandRepository;
import org.egov.demand.repository.OwnerRepository;
import org.egov.demand.util.DemandEnrichmentUtil;
import org.egov.demand.util.SequenceGenService;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.DemandDetailResponse;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.UserSearchRequest;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemandService {

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

	public DemandResponse create(DemandRequest demandRequest) {

		log.debug("the demand service : " + demandRequest);
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
		log.debug("demand Request object : " + demandRequest);
		log.debug("demand detail list : " + demandDetails);
		kafkaTemplate.send(applicationProperties.getCreateDemandTopic(), demandRequest);
		return new DemandResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), demands);
	}
	
	public void updateDemandFromBill(BillRequest billRequest) {

		List<Bill> bills = billRequest.getBills();
		RequestInfo requestInfo = new RequestInfo();
		String tenantId = bills.get(0).getTenantId();
		Set<String> consumerCodes = new HashSet<>();
		for (Bill bill : bills) {
			for (BillDetail billDetail : bill.getBillDetails())
				consumerCodes.add(billDetail.getConsumerCode());
		}
		DemandCriteria demandCriteria = DemandCriteria.builder().consumerCode(consumerCodes).tenantId(tenantId).build();
		List<Demand> demands = getDemands(demandCriteria, requestInfo).getDemands();
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

					List<String> accDescription = Arrays.asList(accountDetail.getAccountDescription().split("-"));
					String taxHeadCode = accDescription.get(0);
					Long fromDate = Long.valueOf(accDescription.get(1));
					Long toDate = Long.valueOf(accDescription.get(2));

					for (DemandDetail demandDetail : detailsMap.get(taxHeadCode)) {
						Demand demand = demandIdMap.get(demandDetail.getDemandId());
						if (fromDate.equals(demand.getTaxPeriodFrom()) && toDate.equals(demand.getTaxPeriodTo())) {
							BigDecimal collectedAmount = accountDetail.getCreditAmount();
							demandDetail.setTaxAmount(demandDetail.getTaxAmount().subtract(collectedAmount));
							demandDetail.setCollectionAmount(demandDetail.getCollectionAmount().add(collectedAmount));
						}
					}
				}
			}
		}
		demandRepository.update(new DemandRequest(requestInfo,demands));
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
				demandDetail.setTaxAmount(tax);
				demandDetail.setCollectionAmount(demandDetail.getCollectionAmount().add(demandDetail2.getCollectionAmount()));
				}else{
					//TODO throw exception
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
				auditDetail.setLastModifiedTime(auditDetail.getLastModifiedTime());
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
		kafkaTemplate.send(applicationProperties.getUpdateDemandTopic(), demandRequest);
		return new DemandResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), demands);
	}

	public DemandResponse getDemands(DemandCriteria demandCriteria, RequestInfo requestInfo) {

		UserSearchRequest userSearchRequest = UserSearchRequest.builder().requestInfo(requestInfo)
				.tenantId(demandCriteria.getTenantId()).emailId(demandCriteria.getEmail())
				.mobileNumber(demandCriteria.getMobileNumber()).build();
		List<Owner> owners = ownerRepository.getOwners(userSearchRequest);
		Set<String> ownerIds = owners.stream().map(owner -> owner.getId().toString()).collect(Collectors.toSet());
		List<Demand> demands = demandRepository.getDemands(demandCriteria, ownerIds);
		if (!demands.isEmpty())
			demands = demandEnrichmentUtil.enrichOwners(demands, owners);
		return new DemandResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK), demands);
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
