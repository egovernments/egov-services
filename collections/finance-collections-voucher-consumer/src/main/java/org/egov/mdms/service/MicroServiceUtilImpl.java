package org.egov.mdms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.receipt.consumer.model.BusinessService;
import org.egov.receipt.consumer.model.FinanceMdmsModel;
import org.egov.receipt.consumer.model.MasterDetail;
import org.egov.receipt.consumer.model.MdmsCriteria;
import org.egov.receipt.consumer.model.MdmsCriteriaReq;
import org.egov.receipt.consumer.model.ModuleDetail;
import org.egov.receipt.consumer.model.ProcessStatus;
import org.egov.receipt.consumer.model.RequestInfo;
import org.egov.receipt.consumer.model.TaxHeadMaster;
import org.egov.receipt.consumer.repository.ServiceRequestRepository;
import org.egov.receipt.custom.exception.VoucherCustomException;
import org.egov.reciept.consumer.config.PropertiesManager;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class MicroServiceUtilImpl implements MicroServiceUtil{
	private static final String FIN_MODULE_NAME = "FinanceModule";
	private static final String BILLSERVICE_MODULE_NAME = "BillingService";
	@Autowired
	private PropertiesManager manager;
	@Autowired
	private MdmsCriteria mdmscriteria;
	@Autowired
	private MdmsCriteriaReq mdmsrequest;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	@Override
	public List<BusinessService> getBusinessService(String tenantId, String code, RequestInfo requestInfo, FinanceMdmsModel finSerMdms) throws VoucherCustomException{
		if(finSerMdms.getFinanceServiceMdmsData() == null){
			this.getFinanceServiceMdmsData(tenantId, code, requestInfo, finSerMdms);
		}
		List<BusinessService> list = new ArrayList<>();
		try {
			if(finSerMdms.getFinanceServiceMdmsData() != null){
				list = mapper.convertValue(JsonPath.read(finSerMdms.getFinanceServiceMdmsData(), "$.MdmsRes.FinanceModule.BusinessServiceMapping"),new TypeReference<List<BusinessService>>(){});
			}			
		} catch (Exception e) {
			throw new VoucherCustomException(ProcessStatus.FAILED,"Error while parsing mdms data. Check the business/account head mapping json file.");
		}
		return list;
	}
	
	@Override
	public List<TaxHeadMaster> getTaxHeadMasters(String tenantId,String code, RequestInfo requestInfo, FinanceMdmsModel finSerMdms) throws VoucherCustomException {
		if(finSerMdms.getFinanceServiceMdmsData() == null){
			this.getFinanceServiceMdmsData(tenantId, code, requestInfo, finSerMdms);
		}
		List<TaxHeadMaster> list = new ArrayList<>();
		try {
			if(finSerMdms.getFinanceServiceMdmsData() != null){
				list = mapper.convertValue(JsonPath.read(finSerMdms.getFinanceServiceMdmsData(), "$.MdmsRes.FinanceModule.TaxHeadMasterGlCodeMapping"),new TypeReference<List<TaxHeadMaster>>(){});
			}			
		} catch (Exception e) {
			throw new VoucherCustomException(ProcessStatus.FAILED,"Error while parsing mdms data. Check the business/account head mapping json file.");
		}
		return list;
	}
	/**
	 * 
	 * @param tenantId
	 * @param businessServiceCode
	 * @return
	 * @throws VoucherCustomException
	 * Function which is used to fetch the finance service mdms data based on Business Service code.
	 */
	public void getFinanceServiceMdmsData(String tenantId,String businessServiceCode, RequestInfo requestInfo, FinanceMdmsModel finSerMdms) throws VoucherCustomException{
		StringBuilder mdmsUrl = new StringBuilder(manager.getMdmsHostUrl()+manager.getMdmsSearchUrl());
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		this.addFinanceModule(moduleDetails, businessServiceCode);
		this.addBillingServiceModule(moduleDetails, businessServiceCode);
        mdmscriteria.setTenantId(tenantId);
        mdmscriteria.setModuleDetails(moduleDetails);
        mdmsrequest.setRequestInfo(requestInfo);
        mdmsrequest.setMdmsCriteria(mdmscriteria);
        try {
       		Map postForObject = mapper.convertValue(serviceRequestRepository.fetchResult(mdmsUrl, mdmsrequest, tenantId), Map.class);
       		finSerMdms.setFinanceServiceMdmsData(postForObject);
        } catch (ServiceCallException e) {
			
        } catch (Exception e) {
        	throw new VoucherCustomException(ProcessStatus.FAILED,"Error Occured While calling the URL : "+mdmsUrl);
		}
	}
	
	private void addFinanceModule(List<ModuleDetail> moduleDetails,String businessServiceCode){
		ArrayList<MasterDetail> masterDetailsList = new ArrayList<>();
		masterDetailsList.add(new MasterDetail("BusinessServiceMapping","[?(@.code=='" + businessServiceCode + "')]"));
		masterDetailsList.add(new MasterDetail("TaxHeadMasterGlCodeMapping","[?(@.billingservicecode=='" + businessServiceCode + "')]"));
		moduleDetails.add(new ModuleDetail(FIN_MODULE_NAME, masterDetailsList));
	}

	private void addBillingServiceModule(List<ModuleDetail> moduleDetails,String businessServiceCode){
		ArrayList<MasterDetail> masterDetailsList = new ArrayList<>();
		masterDetailsList.add(new MasterDetail("BusinessService","[?(@.code=='" + businessServiceCode + "')]"));
		moduleDetails.add(new ModuleDetail(BILLSERVICE_MODULE_NAME, masterDetailsList));
	}

	@Override
	public String getBusinessServiceName(String tenantId,String code, RequestInfo requestInfo, FinanceMdmsModel finSerMdms) throws VoucherCustomException {
		if(finSerMdms.getFinanceServiceMdmsData() == null){
			this.getFinanceServiceMdmsData(tenantId, code, requestInfo, finSerMdms);
		}
		List<BusinessService> list = new ArrayList<>();
		try {
			if(finSerMdms.getFinanceServiceMdmsData() != null){
				list = mapper.convertValue(JsonPath.read(finSerMdms.getFinanceServiceMdmsData(), "$.MdmsRes.BillingService.BusinessService"),new TypeReference<List<BusinessService>>(){});
			}
		} catch (Exception e) {
			throw new VoucherCustomException(ProcessStatus.FAILED,"Error while parsing mdms data. Check the business/account head mapping json file.");
		}
		return !list.isEmpty() ? list.get(0).getBusinessService() : code;
	}
}
