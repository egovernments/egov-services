package org.egov.mdms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.receipt.consumer.model.BusinessService;
import org.egov.receipt.consumer.model.FilterRequest;
import org.egov.receipt.consumer.model.MasterDetail;
import org.egov.receipt.consumer.model.MdmsCriteria;
import org.egov.receipt.consumer.model.MdmsCriteriaReq;
import org.egov.receipt.consumer.model.ModuleDetail;
import org.egov.receipt.consumer.model.RequestInfo;
import org.egov.receipt.consumer.model.TaxHeadMaster;
import org.egov.receipt.custom.exception.VoucherCustomException;
import org.egov.reciept.consumer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class MicroServiceUtilImpl implements MicroServiceUtil{
	private static final Logger LOGGER = LoggerFactory.getLogger(MicroServiceUtilImpl.class);
	private static final String FIN_MODULE_NAME = "FinanceModule";
	@Autowired
	private TokenService tokenService;
	@Autowired
	private PropertiesManager manager;
	@Autowired
	private ModuleDetail moduleDetail;
	@Autowired
	private MasterDetail masterDetails;
	@Autowired
	private RequestInfo requestInfo;
	@Autowired
	private MdmsCriteria mdmscriteria;
	@Autowired
	private MdmsCriteriaReq mdmsrequest;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper mapper;
	private Object financeServiceMdmsData = null;
	
	
	@Override
	public List<BusinessService> getBusinessService(String tenantId, String code) throws VoucherCustomException{
		financeServiceMdmsData  = financeServiceMdmsData != null ? financeServiceMdmsData : this.getFinanceServiceMdmsData(tenantId, code);
		if(LOGGER.isInfoEnabled())
			LOGGER.info("financeServiceMdmsData  :::  "+financeServiceMdmsData);
		List<BusinessService> list = new ArrayList<>();;
		try {
			if(financeServiceMdmsData != null){
				list = mapper.convertValue(JsonPath.read(financeServiceMdmsData, "$.MdmsRes.FinanceModule.BusinessServiceMapping"),new TypeReference<List<BusinessService>>(){});
				if(LOGGER.isInfoEnabled())
					LOGGER.info("List of business services ::: "+list);
			}			
		} catch (Exception e) {
			throw new VoucherCustomException("FAILED","Error while parsing mdms data. Check the business/account head mapping json file.");
		}
		return list;
	}
	
	@Override
	public List<TaxHeadMaster> getTaxHeadMasters(String tenantId,String code) throws VoucherCustomException {
		financeServiceMdmsData  = financeServiceMdmsData != null ? financeServiceMdmsData : this.getFinanceServiceMdmsData(tenantId, code);
		List<TaxHeadMaster> list = new ArrayList<>();;
		try {
			if(financeServiceMdmsData != null){
				list = mapper.convertValue(JsonPath.read(financeServiceMdmsData, "$.MdmsRes.FinanceModule.TaxHeadMasterGlCodeMapping"),new TypeReference<List<TaxHeadMaster>>(){});
				if(LOGGER.isInfoEnabled())
					LOGGER.info("List of TaxHeadMaster data : "+list);
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
	public Object getFinanceServiceMdmsData(String tenantId,String businessServiceCode) throws VoucherCustomException{
		String mdmsUrl = manager.getHostUrl() + manager.getMdmsSearchUrl();
		String authToken = tokenService.generateAdminToken(tenantId); 
        requestInfo.setAuthToken(authToken);
        ArrayList<MasterDetail> masterDetailsList = new ArrayList<>();
        this.prepareMasterDetailsArray(masterDetailsList, businessServiceCode);
        moduleDetail.setModuleName(FIN_MODULE_NAME);
        moduleDetail.setMasterDetails(masterDetailsList);
        mdmscriteria.setTenantId(tenantId);
        mdmscriteria.setModuleDetails(Arrays.asList(moduleDetail));
        mdmsrequest.setRequestInfo(requestInfo);
        mdmsrequest.setMdmsCriteria(mdmscriteria);
        try {
        	if(LOGGER.isInfoEnabled())
        		LOGGER.info("call : "+mdmsUrl);
            Map postForObject = restTemplate.postForObject(mdmsUrl, mdmsrequest, Map.class);
            return postForObject;
        } catch (Exception e) {
			throw new VoucherCustomException("FAILED","Error Occured While calling the URL : "+mdmsUrl);
        }
	}
	
	private void prepareMasterDetailsArray(ArrayList<MasterDetail> masterDetailsList,String businessServiceCode){
		masterDetailsList.add(new MasterDetail("BusinessServiceMapping","[?(@.code=='" + businessServiceCode + "')]"));
		masterDetailsList.add(new MasterDetail("TaxHeadMasterGlCodeMapping","[?(@.billingservicecode=='" + businessServiceCode + "')]"));
	}
	
}
