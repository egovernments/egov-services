package org.egov.mdms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.egov.receipt.consumer.model.BusinessService;
import org.egov.receipt.consumer.model.FinanceMdmsModel;
import org.egov.receipt.consumer.model.MasterDetail;
import org.egov.receipt.consumer.model.MdmsCriteria;
import org.egov.receipt.consumer.model.MdmsCriteriaReq;
import org.egov.receipt.consumer.model.ModuleDetail;
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
	@Autowired
	private PropertiesManager manager;
	@Autowired
	private ModuleDetail moduleDetail;
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
			throw new VoucherCustomException("FAILED","Error while parsing mdms data. Check the business/account head mapping json file.");
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
			throw new VoucherCustomException("FAILED","Error while parsing mdms data. Check the business/account head mapping json file.");
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
        ArrayList<MasterDetail> masterDetailsList = new ArrayList<>();
        this.prepareMasterDetailsArray(masterDetailsList, businessServiceCode);
        moduleDetail.setModuleName(FIN_MODULE_NAME);
        moduleDetail.setMasterDetails(masterDetailsList);
        mdmscriteria.setTenantId(tenantId);
        mdmscriteria.setModuleDetails(Arrays.asList(moduleDetail));
        mdmsrequest.setRequestInfo(requestInfo);
        mdmsrequest.setMdmsCriteria(mdmscriteria);
        try {
       		Map postForObject = mapper.convertValue(serviceRequestRepository.fetchResult(mdmsUrl, mdmsrequest, tenantId), Map.class);
       		finSerMdms.setFinanceServiceMdmsData(postForObject);
        } catch (ServiceCallException e) {
			
        } catch (Exception e) {
        	throw new VoucherCustomException("FAILED","Error Occured While calling the URL : "+mdmsUrl);
		}
	}
	
	private void prepareMasterDetailsArray(ArrayList<MasterDetail> masterDetailsList,String businessServiceCode){
		masterDetailsList.add(new MasterDetail("BusinessServiceMapping","[?(@.code=='" + businessServiceCode + "')]"));
		masterDetailsList.add(new MasterDetail("TaxHeadMasterGlCodeMapping","[?(@.billingservicecode=='" + businessServiceCode + "')]"));
	}
	
}
