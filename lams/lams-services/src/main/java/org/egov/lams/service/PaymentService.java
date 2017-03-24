package org.egov.lams.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.lams.contract.DemandSearchCriteria;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.RequestInfo;
import org.egov.lams.repository.BillRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.web.contract.BillDetailInfo;
import org.egov.lams.web.contract.BillInfo;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	@Autowired
	LamsConfigurationService lamsConfigurationService;

	@Autowired
	DemandRepository demandRepository;
	
	@Autowired
	BillRepository billRepository;
	
	@Autowired
	BillNumberService billNumberService;

	public String generateBillXml(Agreement agreement, RequestInfo requestInfo) {
		LamsConfigurationGetRequest lamsGetRequest = new LamsConfigurationGetRequest();
		List<BillInfo> billInfos = new ArrayList<>();
		BillInfo billInfo = new BillInfo();
		billInfo.setId( null);
		billInfo.setDemandId(1L);
		billInfo.setCitizenName(agreement.getAllottee().getName());
		billInfo.setCitizenAddress("Address");
		billInfo.setBillType("AUTO");
		billInfo.setIssuedDate(new Date());
		billInfo.setLastDate(new Date());
		lamsGetRequest.setName("MODULE_NAME");
		String moduleName = lamsConfigurationService
				.getLamsConfigurations(lamsGetRequest).get("MODULE_NAME").get(0);
		billInfo.setModuleName(moduleName);
		lamsGetRequest.setTenantId(agreement.getTenantId());
		lamsGetRequest.setName("FUND_CODE");
		String fundCode = lamsConfigurationService
				.getLamsConfigurations(lamsGetRequest).get("FUND_CODE").get(0);
		billInfo.setFundCode(fundCode);
		lamsGetRequest.setName("FUNCTIONARY_CODE");
		String functionaryCode = lamsConfigurationService
				.getLamsConfigurations(lamsGetRequest).get("FUNCTIONARY_CODE").get(0);
		billInfo.setFunctionaryCode(Long.valueOf(functionaryCode));
		lamsGetRequest.setName("FUNDSOURCE_CODE");
		String fundSourceCode = lamsConfigurationService
				.getLamsConfigurations(lamsGetRequest).get("FUNDSOURCE_CODE").get(0);
        billInfo.setFundSourceCode(fundSourceCode);
		lamsGetRequest.setName("DEPARTMENT_CODE");
		String departmentCode = lamsConfigurationService
				.getLamsConfigurations(lamsGetRequest).get("DEPARTMENT_CODE").get(0);
        billInfo.setDepartmentCode(departmentCode);
		billInfo.setCollModesNotAllowed("");
		billInfo.setBoundaryNumber(agreement.getAsset().getLocationDetails().getElectionWard());
		//TODO @iffath electward is of type Long 
		//billInfo.setBoundaryNumber(Integer.valueOf(agreement.getAsset().getLocationDetails().getElectionWard()));
		lamsGetRequest.setName("BOUNDARY_TYPE");
		String boundaryType = lamsConfigurationService
				.getLamsConfigurations(lamsGetRequest).get("BOUNDARY_TYPE").get(0);
		billInfo.setBoundaryType(boundaryType);
		lamsGetRequest.setName("SERVICE_CODE");
		String serviceCode = lamsConfigurationService
				.getLamsConfigurations(lamsGetRequest).get("SERVICE_CODE").get(0);
		billInfo.setServiceCode(serviceCode);
		billInfo.setPartPaymentAllowed('N');
		billInfo.setOverrideAccHeadAllowed('N');
		billInfo.setDescription("Leases And Agreements : "+ (StringUtils.isBlank(agreement.getAgreementNumber())? agreement.getAcknowledgementNumber():agreement.getAgreementNumber()));
		billInfo.setConsumerCode(StringUtils.isBlank(agreement.getAgreementNumber())? agreement.getAcknowledgementNumber():agreement.getAgreementNumber());
		billInfo.setCallbackForApportion('N');
		billInfo.setEmailId(agreement.getAllottee().getEmailId());
		billInfo.setConsumerType("Agreement");
		billInfo.setBillNumber(billNumberService.generateBillNumber());
		
		DemandSearchCriteria demandSearchCriteria= new DemandSearchCriteria();
		demandSearchCriteria.setDemandId(1L);
		
		Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria).getDemands().get(0);
		billInfo.setDisplayMessage(demand.getModuleName());
		billInfo.setMinAmountPayable(demand.getMinAmountPayable());
		
		lamsGetRequest.setName("FUNCTION_CODE");
		String functionCode = lamsConfigurationService
				.getLamsConfigurations(lamsGetRequest).get("FUNCTION_CODE")
				.get(0);
		BigDecimal totalAmount =BigDecimal.ZERO;
		List<BillDetailInfo> billDetailInfos = new ArrayList<>();
		int orderNo = 0;
		for(DemandDetails demandDetail : demand.getDemandDetails()) {
			orderNo++;
			totalAmount= totalAmount.add(demandDetail.getTaxAmount());
			billDetailInfos.addAll(getBilldetails(demandDetail,functionCode, orderNo));
		}
		billInfo.setTotalAmount(totalAmount.doubleValue());
		billInfo.setBillAmount(totalAmount.doubleValue());
		billInfo.setBillDetailInfos(billDetailInfos);
		billInfos.add(billInfo);
		final String billXml = billRepository.createBillAndGetXml(billInfos, requestInfo);
	    String collectXML = "";
        try {
            collectXML = URLEncoder.encode(billXml, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
		return collectXML;
	}

	public List<BillDetailInfo> getBilldetails(final DemandDetails demandDetail,
			String functionCode, int orderNo) {
		final List<BillDetailInfo> billDetails = new ArrayList<>();
		BillDetailInfo billdetail= new BillDetailInfo();
		//TODO: Fix me: As per the rules for the order no. 
		billdetail.setOrderNo(orderNo);
		billdetail.setCreditAmount(demandDetail.getTaxAmount());
		billdetail.setDebitAmount(BigDecimal.ZERO);
		billdetail.setGlCode(demandDetail.getGlCode().toString());
		billdetail.setDescription(demandDetail.getTaxReason());
		billdetail.setPeriod(demandDetail.getTaxPeriod());
		billdetail.setPurpose("CURRENT_AMOUNT");
		billdetail.setIsActualDemand(demandDetail.getIsActualDemand());
		billdetail.setFunctionCode(functionCode);
		billDetails.add(billdetail);
		return billDetails;
	}

}
