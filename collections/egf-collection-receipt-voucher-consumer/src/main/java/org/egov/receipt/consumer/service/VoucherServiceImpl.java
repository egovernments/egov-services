package org.egov.receipt.consumer.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.mdms.service.MicroServiceUtil;
import org.egov.mdms.service.TokenService;
import org.egov.receipt.consumer.model.AccountDetail;
import org.egov.receipt.consumer.model.Bill;
import org.egov.receipt.consumer.model.BillAccountDetail;
import org.egov.receipt.consumer.model.BillDetail;
import org.egov.receipt.consumer.model.BusinessService;
import org.egov.receipt.consumer.model.Function;
import org.egov.receipt.consumer.model.Functionary;
import org.egov.receipt.consumer.model.Fund;
import org.egov.receipt.consumer.model.InstrumentAccountCodeContract;
import org.egov.receipt.consumer.model.InstrumentAccountCodeReq;
import org.egov.receipt.consumer.model.InstrumentAccountCodeResponse;
import org.egov.receipt.consumer.model.InstrumentAccountCodeSearchContract;
import org.egov.receipt.consumer.model.InstrumentTypeContract;
import org.egov.receipt.consumer.model.Receipt;
import org.egov.receipt.consumer.model.ReceiptReq;
import org.egov.receipt.consumer.model.RequestInfo;
import org.egov.receipt.consumer.model.Scheme;
import org.egov.receipt.consumer.model.TaxHeadMaster;
import org.egov.receipt.consumer.model.Voucher;
import org.egov.receipt.consumer.model.VoucherRequest;
import org.egov.receipt.consumer.model.VoucherResponse;
import org.egov.receipt.consumer.model.VoucherSearchRequest;
import org.egov.receipt.custom.exception.VoucherCustomException;
import org.egov.reciept.consumer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	private PropertiesManager propertiesManager;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private MicroServiceUtil microServiceUtil;
	@Autowired
	private RestTemplate restTemplate;

	final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	public static final String RECEIPTS_VOUCHER_NAME = "Other receipts";
	public static final String RECEIPTS_VOUCHER_TYPE = "Receipt";
	public static final String RECEIPTS_VOUCHER_DESCRIPTION = "Collection Module";;
	public static final String COLLECTIONS_EG_MODULES_ID = "10";

	public static final Logger LOGGER = LoggerFactory.getLogger(VoucherServiceImpl.class);
	private static final String RECEIPT_VIEW_SOURCEPATH = "/services/collection/receipts/receipt-viewReceipts.action?selectedReceipts=";
	LinkedHashMap<String, BigDecimal> amountMapwithGlcode;

	@Override
	public VoucherResponse createVoucher(ReceiptReq receiptRequest) throws Exception {
			Receipt receipt = receiptRequest.getReceipt().get(0);
			String tenantId = receipt.getTenantId();
			final String voucher_create_url = propertiesManager.getErpURLBytenantId(tenantId)
					+ propertiesManager.getVoucherCreateUrl();
			LOGGER.info("voucher_create_url:" + voucher_create_url);
			RequestInfo requestInfo = new RequestInfo();
			requestInfo.setAuthToken(tokenService.generateAdminToken(tenantId));
			VoucherRequest voucherRequest = new VoucherRequest();
			Voucher voucher = new Voucher();
			voucher.setTenantId(tenantId);
			this.setVoucherDetails(voucher, receipt, tenantId);
			voucherRequest.setVouchers(Collections.singletonList(voucher));
			voucherRequest.setRequestInfo(requestInfo);
			voucherRequest.setTenantId(tenantId);
			LOGGER.info("call:" + voucher_create_url);
			return restTemplate.postForObject(voucher_create_url, voucherRequest, VoucherResponse.class);
	}

	@Override
	public boolean isVoucherCretionEnabled(ReceiptReq req) throws Exception {
			Receipt receipt = req.getReceipt().get(0);
			String tenantId = receipt.getTenantId();
			Bill bill = receipt.getBill().get(0);
			String bsCode = bill.getBillDetails().get(0).getBusinessService();
			List<BusinessService> serviceByCode = this.getBusinessServiceByCode(tenantId, bsCode);
			return serviceByCode != null && !serviceByCode.isEmpty() ? serviceByCode.get(0).isVoucherCreationEnabled()
					: false;
	}
	
	private InstrumentAccountCodeContract getInstrumentAccountCode(Receipt receipt) throws VoucherCustomException{
		InstrumentAccountCodeSearchContract iAccCodeSearchContract = new InstrumentAccountCodeSearchContract();
		InstrumentTypeContract instrumentType = new InstrumentTypeContract();
		String name = receipt.getInstrument().getInstrumentType().getName();
		instrumentType.setName(name);
		iAccCodeSearchContract.setInstrumentType(instrumentType);
			InstrumentAccountCodeReq instAccCodeReq = new InstrumentAccountCodeReq();
			String tenantId = receipt.getTenantId();
			final String instrument_account_code_url = propertiesManager.getHostUrl()
					+ propertiesManager.getInstrumentAccountCodeUrl() + "?tenantId="+tenantId;
			LOGGER.info("instrument_account_code_url : " + instrument_account_code_url);
			RequestInfo requestInfo = new RequestInfo();
			requestInfo.setAuthToken(tokenService.generateAdminToken(tenantId));
			instAccCodeReq.setTenantId(tenantId);
			instAccCodeReq.setRequestInfo(requestInfo);
			instAccCodeReq.setInstrumentAccountCodeSearchContract(iAccCodeSearchContract);
			LOGGER.info("call:" + instrument_account_code_url);
			InstrumentAccountCodeResponse postForObject = restTemplate.postForObject(instrument_account_code_url, instAccCodeReq, InstrumentAccountCodeResponse.class);
			if(postForObject != null && !postForObject.getInstrumentAccountCodes().isEmpty()){
				List<InstrumentAccountCodeContract> collect = postForObject.getInstrumentAccountCodes().stream().filter(iac -> iac.getInstrumentType().getName().equals(name)).collect(Collectors.toList());
				if(!collect.isEmpty()){
					return collect.get(0);
				}
			}
			throw new VoucherCustomException("ERROR -> No any ChartOfAccount is mapped for InstrumentType "+name);	
	}

	@Override
	public VoucherResponse cancelVoucher(ReceiptReq receiptRequest) throws VoucherCustomException {
		Receipt receipt = receiptRequest.getReceipt().get(0);
		String tenantId = receipt.getTenantId();
		final String voucher_cancel_url = propertiesManager.getErpURLBytenantId(tenantId)
				+ propertiesManager.getVoucherCancelUrl();
		LOGGER.info("voucher_cancel_url:" + voucher_cancel_url);
		try {
			VoucherSearchRequest vSearchReq = this.getVoucherSearchReq(receiptRequest);
				LOGGER.info("call:" + voucher_cancel_url);
				return restTemplate.postForObject(voucher_cancel_url, vSearchReq, VoucherResponse.class);
		} catch (Exception e) {
			throw new VoucherCustomException("ERROR occured while cancelling voucher in VoucherServiceImpl.cancelVoucher method "+
					e.getMessage());
		}
	}
	
	private VoucherSearchRequest getVoucherSearchReq(ReceiptReq receiptRequest){
		VoucherSearchRequest vSearchReq = new VoucherSearchRequest();
		Receipt receipt = receiptRequest.getReceipt().get(0);
		String tenantId = receipt.getTenantId();
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAuthToken(tokenService.generateAdminToken(tenantId));
		BillDetail billDetail = receipt.getBill().get(0).getBillDetails().get(0);
		String voucherNumber = billDetail.getVoucherHeader();
		vSearchReq.setVoucherNumbers(voucherNumber);
		vSearchReq.setTenantId(tenantId);
		vSearchReq.setRequestInfo(requestInfo);
		return vSearchReq;
	}
	
	@Override
	public boolean isVoucherExists(ReceiptReq receiptRequest) throws VoucherCustomException {
		Receipt receipt = receiptRequest.getReceipt().get(0);
		String voucher_search_url = propertiesManager.getErpURLBytenantId(receipt.getTenantId()) + propertiesManager.getVoucherSearchUrl();
		VoucherSearchRequest vSearchReq = this.getVoucherSearchReq(receiptRequest);
		VoucherResponse response = null;
		try {
			response = restTemplate.postForObject(voucher_search_url , vSearchReq, VoucherResponse.class);			
		} catch (Exception e) {
			throw new VoucherCustomException("ERROR occured while calling "+voucher_search_url+" to check the existence of voucher");
		}
		boolean isExist = false;
		if(response != null){
			isExist = response.getVouchers() != null ? !response.getVouchers().isEmpty() : false;
		}
		return isExist;
	}

	private void setVoucherDetails(Voucher voucher, Receipt receipt, String tenantId) throws Exception {
		BillDetail billDetail = receipt.getBill().get(0).getBillDetails().get(0);
		String bsCode = billDetail.getBusinessService();
		List<BusinessService> businessServices = this.getBusinessServiceByCode(tenantId, bsCode);
		List<TaxHeadMaster> taxHeadMasterByBusinessServiceCode = this.getTaxHeadMasterByBusinessServiceCode(tenantId,
				bsCode);
		if (!businessServices.isEmpty()) {
			BusinessService businessService = businessServices.get(0);
			voucher.setName(RECEIPTS_VOUCHER_NAME);
			voucher.setType(RECEIPTS_VOUCHER_TYPE);
			voucher.setFund(new Fund());
			voucher.getFund().setCode(businessService.getFund());
			voucher.setFunction(new Function());
			voucher.getFunction().setCode(businessService.getFunction());
			voucher.setDepartment(businessService.getDepartment());
			voucher.setFunctionary(new Functionary());
			String functionaryCode = businessService.getFunctionary() != null
					& !StringUtils.isEmpty(businessService.getFunctionary()) ? businessService.getFunctionary() : null;
			voucher.getFunctionary().setCode(functionaryCode);
			voucher.setScheme(new Scheme());
			String schemeCode = businessService.getScheme() != null & !StringUtils.isEmpty(businessService.getScheme())
					? businessService.getScheme() : null;
			voucher.getScheme().setCode(schemeCode);
			voucher.setDescription(RECEIPTS_VOUCHER_DESCRIPTION);
			//checking Whether manualReceipt date will be consider as voucherdate
			if(billDetail.getManualReceiptDate() != null && billDetail.getManualReceiptDate().longValue() != 0 && getManualReceiptDateConsideredValue(tenantId)){
				voucher.setVoucherDate(format.format(new Date(billDetail.getManualReceiptDate())));
			}else{
				voucher.setVoucherDate(format.format(new Date(billDetail.getReceiptDate())));
			}
			voucher.setModuleId(Long.valueOf(COLLECTIONS_EG_MODULES_ID));
//			voucher.setSource(RECEIPT_VIEW_SOURCEPATH + receipt.getReceiptNumber());
			
			voucher.setLedgers(new ArrayList<>());
			amountMapwithGlcode = new LinkedHashMap<>();
			//Setting glcode and amount in Map as key value pair.
			for(BillAccountDetail bad : billDetail.getBillAccountDetails()){
				String taxHeadCode = bad.getTaxHeadCode();
				List<TaxHeadMaster> findFirst = taxHeadMasterByBusinessServiceCode.stream()
						.filter(tx -> tx.getTaxhead().equals(taxHeadCode)).collect(Collectors.toList());
				if(findFirst!=null && findFirst.isEmpty())
					throw new VoucherCustomException("ERROR -> Taxhead code "+taxHeadCode + " is not mapped with BusinessServiceCode "+bsCode);
				String glcode = findFirst.get(0).getGlcode();
				if(amountMapwithGlcode.get(glcode) != null){
					amountMapwithGlcode.put(glcode, amountMapwithGlcode.get(glcode).add(bad.getAdjustedAmount()));
				}else{
					amountMapwithGlcode.put(glcode, bad.getAdjustedAmount());
				}
				
			}
			
			setNetPayableAmount(voucher,receipt);
			
			LOGGER.info("amountMapwithGlcode  ::: "+amountMapwithGlcode);
			// Iterating map and setting the ledger details to voucher.
			amountMapwithGlcode.entrySet().stream().forEach(entry -> {
				AccountDetail accountDetail = new AccountDetail();
				accountDetail.setGlcode(entry.getKey());
				if(entry.getValue().compareTo(new BigDecimal(0)) != 0){
					if(entry.getValue().compareTo(new BigDecimal(0)) == 1){
						accountDetail.setCreditAmount(entry.getValue().doubleValue());
						accountDetail.setDebitAmount(0d);
					}else{
						accountDetail.setDebitAmount(-entry.getValue().doubleValue());
						accountDetail.setCreditAmount(0d);
					}
				}
				accountDetail.setFunction(new Function());
				accountDetail.getFunction().setCode(businessService.getFunction());
				voucher.getLedgers().add(accountDetail);
			});
		}
	}

	private boolean getManualReceiptDateConsideredValue(String tenantId) {
		RequestInfo requestInfo = new RequestInfo();
		try {
			requestInfo.setAuthToken(tokenService.generateAdminToken(tenantId));
			VoucherRequest request = new VoucherRequest(tenantId, requestInfo , null);
			String url = propertiesManager.getErpURLBytenantId(tenantId) + propertiesManager.getManualReceiptDateUrl();
			return restTemplate.postForObject(url , request, Boolean.class);			
		} catch (Exception e) {
			LOGGER.error("ERROR while checking the consideration of manualReceiptDate. So the receiptDate will be consider as the voucherDate");
		}
		return false;
	}

	private void setNetPayableAmount(Voucher voucher, Receipt receipt) throws VoucherCustomException {
			InstrumentAccountCodeContract instrumentAccountCode = this.getInstrumentAccountCode(receipt);
			String glcode = instrumentAccountCode.getAccountCode().getGlcode();
			BigDecimal amountPaid = receipt.getBill().get(0).getBillDetails().get(0).getAmountPaid();
			amountMapwithGlcode.put(glcode, new BigDecimal(-amountPaid.doubleValue()));
	}

	private List<BusinessService> getBusinessServiceByCode(String tenantId, String bsCode) throws Exception {
		List<BusinessService> propertyTaxBusinessService = microServiceUtil.getPropertyTaxBusinessService(tenantId,
				bsCode);
		if(propertyTaxBusinessService.isEmpty())
			throw new VoucherCustomException("ERROR -> No Business Service Found for Code : "+bsCode);
		List<BusinessService> collect = propertyTaxBusinessService.stream().filter(bs -> bs.getCode().equals(bsCode))
				.collect(Collectors.toList());
		return collect;
	}

	private List<TaxHeadMaster> getTaxHeadMasterByBusinessServiceCode(String tenantId, String bsCode) throws Exception {
		List<TaxHeadMaster> taxHeadMasters = microServiceUtil.getTaxHeadMasters(tenantId, bsCode);
		return taxHeadMasters;
	}
}
