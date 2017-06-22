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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.helper.BillHelper;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillAccountDetail;
import org.egov.demand.model.BillDetail;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.GenerateBillCriteria;
import org.egov.demand.repository.BillRepository;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillService {
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private BillRepository billRepository;
	
	@Autowired
	private BillHelper billHelper;
	
	@Autowired
	private DemandService demandService;

	public BillResponse createAsync(BillRequest billRequest) { 
		
		billHelper.getBillRequestWithIds(billRequest);
		
		try {
			kafkaTemplate.send(applicationProperties.getCreateBillTopic(),applicationProperties.getCreateBillTopicKey(),billRequest);
		} catch (Exception e) {
			log.info("BillService createAsync:"+e);
			throw new RuntimeException(e);
			
		}
		return getBillResponse(billRequest.getBills());
	}
	
	public void create(BillRequest billRequest){		
		billRepository.saveBill(billRequest);
	}
	
	public BillResponse generateBill(GenerateBillCriteria billCriteria, RequestInfo requestInfo) {
		
		Set<String> ids = new HashSet<>();
		ids.add(billCriteria.getDemandId());
		DemandCriteria demandCriteria = DemandCriteria.builder().businessService(billCriteria.getBusinessService()).
				consumerCode(billCriteria.getConsumerCode()).demandId(ids).
				email(billCriteria.getEmail()).mobileNumber(billCriteria.getMobileNumber()).
				tenantId(billCriteria.getTenantId()).build();
		
		DemandResponse demandResponse = demandService.getDemands(demandCriteria,requestInfo);
		List<Demand> demands = demandResponse.getDemands();
		
		List<Bill> bills = null; 
		
		if(!demands.isEmpty())
			bills = prepareBill(demands, billCriteria.getTenantId());
		else 
			throw new RuntimeException("Invalid demand criteria");
			
		return createAsync(BillRequest.builder().bills(bills).requestInfo(requestInfo).build());
	}
	
	private List<Bill> prepareBill(List<Demand> demands,String tenantId){
		List<Bill> bills = new ArrayList<Bill>();
		
		Map<String, List<Demand>> map = demands.stream().collect(Collectors.groupingBy(Demand::getBusinessService, Collectors.toList()));
		
		Demand demand = demands.get(0);
		Bill bill = Bill.builder().isActive(true).isCancelled(false).payeeAddress(null).
				payeeEmail(demand.getOwner().getEmailId()).payeeName(demand.getOwner().getName()).tenantId(tenantId).build();
		
		List<BillDetail> billDetails = new ArrayList<>(); 
		
		for(Map.Entry<String, List<Demand>> entry : map.entrySet()){
			List<Demand> demands2 = entry.getValue();
			List<BillAccountDetail> billAccountDetails = new ArrayList<>();
			Demand demand3 = demands2.get(0);
			BigDecimal totalTaxAmount = BigDecimal.ZERO;
			BigDecimal totalMinAmount = BigDecimal.ZERO;
			BigDecimal totalCollectedAmount = BigDecimal.ZERO;
	
			for(Demand demand2 : demands2){
				List<DemandDetail> demandDetails = demand2.getDemandDetails();
				totalMinAmount = totalMinAmount.add(demand2.getMinimumAmountPayable());
				for(DemandDetail demandDetail : demandDetails) {
					totalTaxAmount = totalTaxAmount.add(demandDetail.getTaxAmount());
					totalCollectedAmount = totalCollectedAmount.add(demandDetail.getCollectionAmount());
					BillAccountDetail billAccountDetail = BillAccountDetail.builder().
							creditAmount(demandDetail.getTaxAmount().subtract(demandDetail.getCollectionAmount())).build();
					billAccountDetails.add(billAccountDetail);
				}
			}
			
			BillDetail billDetail = BillDetail.builder().businessService(demand3.getBusinessService()).consumerType(demand3.getConsumerType()).
					consumerCode(demand3.getConsumerCode()).minimumAmount(totalMinAmount).
					totalAmount(totalTaxAmount.subtract(totalCollectedAmount)).tenantId(tenantId).build();
			
			billDetails.add(billDetail);
			
		}
		bill.setBillDetails(billDetails);
		bills.add(bill);
		
		return bills;
	}
	
	public BillResponse getBillResponse(List<Bill> bills) {
		BillResponse billResponse = new BillResponse();
		billResponse.setBill(bills);
		return billResponse;
	}
	
	

}
