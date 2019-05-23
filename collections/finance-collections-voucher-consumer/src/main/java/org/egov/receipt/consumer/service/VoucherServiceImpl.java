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
import org.egov.receipt.consumer.model.AppConfigValues;
import org.egov.receipt.consumer.model.Bill;
import org.egov.receipt.consumer.model.BillAccountDetail;
import org.egov.receipt.consumer.model.BillDetail;
import org.egov.receipt.consumer.model.BusinessService;
import org.egov.receipt.consumer.model.EgModules;
import org.egov.receipt.consumer.model.FinanceMdmsModel;
import org.egov.receipt.consumer.model.Function;
import org.egov.receipt.consumer.model.Functionary;
import org.egov.receipt.consumer.model.Fund;
import org.egov.receipt.consumer.model.InstrumentAccountCodeContract;
import org.egov.receipt.consumer.model.InstrumentAccountCodeReq;
import org.egov.receipt.consumer.model.InstrumentAccountCodeResponse;
import org.egov.receipt.consumer.model.Receipt;
import org.egov.receipt.consumer.model.ReceiptReq;
import org.egov.receipt.consumer.model.RequestInfo;
import org.egov.receipt.consumer.model.Scheme;
import org.egov.receipt.consumer.model.TaxHeadMaster;
import org.egov.receipt.consumer.model.Voucher;
import org.egov.receipt.consumer.model.VoucherRequest;
import org.egov.receipt.consumer.model.VoucherResponse;
import org.egov.receipt.consumer.model.VoucherSearchRequest;
import org.egov.receipt.consumer.repository.ServiceRequestRepository;
import org.egov.receipt.custom.exception.VoucherCustomException;
import org.egov.reciept.consumer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	private PropertiesManager propertiesManager;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private MicroServiceUtil microServiceUtil;
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	@Autowired
	private ObjectMapper mapper;

	final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

	public static final String RECEIPTS_VOUCHER_NAME = "Other receipts";
	public static final String RECEIPTS_VOUCHER_TYPE = "Receipt";
	public static final String RECEIPTS_VOUCHER_DESCRIPTION = "Collection Module";
	public static final String COLLECTIONS_EG_MODULES_ID = "10";
	public static final Logger LOGGER = LoggerFactory.getLogger(VoucherServiceImpl.class);
	private static final String COLLECTION_MODULE_NAME = "Collections";
	LinkedHashMap<String, BigDecimal> amountMapwithGlcode;

	@Override
	/**
	 * This method is use to create the voucher specifically for receipt
	 * request.
	 */
	public VoucherResponse createReceiptVoucher(ReceiptReq receiptRequest, FinanceMdmsModel finSerMdms)
			throws Exception {
		Receipt receipt = receiptRequest.getReceipt().get(0);
		String tenantId = receipt.getTenantId();
		final StringBuilder voucher_create_url = new StringBuilder(propertiesManager.getErpURLBytenantId(tenantId)
				+ propertiesManager.getVoucherCreateUrl());
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAuthToken(tokenService.generateAdminToken(tenantId));
		VoucherRequest voucherRequest = new VoucherRequest();
		Voucher voucher = new Voucher();
		voucher.setTenantId(tenantId);
		this.setVoucherDetails(voucher, receipt, tenantId, receiptRequest.getRequestInfo(), finSerMdms);
		voucherRequest.setVouchers(Collections.singletonList(voucher));
		voucherRequest.setRequestInfo(requestInfo);
		voucherRequest.setTenantId(tenantId);
		return mapper.convertValue(serviceRequestRepository.fetchResult(voucher_create_url, voucherRequest, tenantId), VoucherResponse.class);
	}

	/**
	 * Function which is used to check whether the voucher creation is set to
	 * true or false in business mapping file.
	 */
	@Override
	public boolean isVoucherCreationEnabled(ReceiptReq req, FinanceMdmsModel finSerMdms) throws Exception {
		Receipt receipt = req.getReceipt().get(0);
		String tenantId = receipt.getTenantId();
		Bill bill = receipt.getBill().get(0);
		String bsCode = bill.getBillDetails().get(0).getBusinessService();
		List<BusinessService> serviceByCode = this.getBusinessServiceByCode(tenantId, bsCode, req.getRequestInfo(), finSerMdms);
		return serviceByCode != null && !serviceByCode.isEmpty() ? serviceByCode.get(0).isVoucherCreationEnabled()
				: false;
	}

	/**
	 * 
	 * @param receipt
	 * @return
	 * @throws VoucherCustomException
	 *             Function which is used to get the AccountCodeContract based
	 *             on the Account code from running Instrument Service. By which
	 *             we can get the mapped glcode for Account code.
	 */
	private InstrumentAccountCodeContract getInstrumentAccountCode(Receipt receipt, RequestInfo requestInfo) throws VoucherCustomException {
		String name = receipt.getInstrument().getInstrumentType().getName();
		InstrumentAccountCodeReq instAccCodeReq = new InstrumentAccountCodeReq();
		String tenantId = receipt.getTenantId();
		final StringBuilder instrument_account_code_url = new StringBuilder(propertiesManager.getInstrumentHostUrl()
				+ propertiesManager.getInstrumentAccountCodeUrl() + "?tenantId=" + tenantId + "&instrumentType.name="+name);
		instAccCodeReq.setTenantId(tenantId);
		instAccCodeReq.setRequestInfo(requestInfo);
		InstrumentAccountCodeResponse postForObject = mapper.convertValue(serviceRequestRepository.fetchResult(instrument_account_code_url, instAccCodeReq, tenantId), InstrumentAccountCodeResponse.class);
		if (postForObject != null && !postForObject.getInstrumentAccountCodes().isEmpty()) {
			List<InstrumentAccountCodeContract> instrumentAccountCodes = postForObject.getInstrumentAccountCodes()
					.stream().filter(iac -> iac.getInstrumentType().getName().equals(name))
					.collect(Collectors.toList());
			if (!instrumentAccountCodes.isEmpty()) {
				return instrumentAccountCodes.get(0);
			}
		}
		throw new VoucherCustomException("FAILED", "Account code mapping is missing for Instrument Type " + name);
	}

	/**
	 * Function is for cancelling the voucher based on voucher number
	 */
	@Override
	public VoucherResponse cancelReceiptVoucher(ReceiptReq receiptRequest) throws VoucherCustomException {
		Receipt receipt = receiptRequest.getReceipt().get(0);
		String tenantId = receipt.getTenantId();
		final StringBuilder voucher_cancel_url = new StringBuilder(propertiesManager.getErpURLBytenantId(tenantId)
				+ propertiesManager.getVoucherCancelUrl());
		try {
			VoucherSearchRequest vSearchReq = this.getVoucherSearchReq(receiptRequest);
			return mapper.convertValue(serviceRequestRepository.fetchResult(voucher_cancel_url, vSearchReq, tenantId), VoucherResponse.class);
		} catch (Exception e) {
			throw new VoucherCustomException("FAILED", "Failed to create voucher");
		}
	}

	/**
	 * 
	 * @param receiptRequest
	 * @return This function is use to set the voucher search params and return
	 *         the setted request
	 */
	private VoucherSearchRequest getVoucherSearchReq(ReceiptReq receiptRequest) {
		VoucherSearchRequest vSearchReq = new VoucherSearchRequest();
		Receipt receipt = receiptRequest.getReceipt().get(0);
		String tenantId = receipt.getTenantId();
		BillDetail billDetail = receipt.getBill().get(0).getBillDetails().get(0);
		String voucherNumber = billDetail.getVoucherHeader();
		vSearchReq.setVoucherNumbers(voucherNumber);
		vSearchReq.setTenantId(tenantId);
		vSearchReq.setRequestInfo(receiptRequest.getRequestInfo());
		return vSearchReq;
	}

	/**
	 * Function is used to check that whether the voucher is present/exist in
	 * Finance setup or not.
	 */
	@Override
	public boolean isVoucherExists(ReceiptReq receiptRequest) throws VoucherCustomException {
		Receipt receipt = receiptRequest.getReceipt().get(0);
		StringBuilder voucher_search_url = new StringBuilder(propertiesManager.getErpURLBytenantId(receipt.getTenantId())
				+ propertiesManager.getVoucherSearchUrl());
		VoucherSearchRequest vSearchReq = this.getVoucherSearchReq(receiptRequest);
		VoucherResponse response = null;
		try {
			response = mapper.convertValue(serviceRequestRepository.fetchResult(voucher_search_url, vSearchReq, receipt.getTenantId()), VoucherResponse.class);
		} catch (Exception e) {
			throw new VoucherCustomException("FAILED",
					"ERROR occured while calling " + voucher_search_url + " to check the existence of voucher");
		}
		boolean isExist = false;
		if (response != null) {
			isExist = response.getVouchers() != null ? !response.getVouchers().isEmpty() : false;
		}
		return isExist;
	}

	/**
	 * 
	 * @param voucher
	 * @param receipt
	 * @param tenantId
	 * @throws Exception
	 *             Function is use to set the specific details of voucher which
	 *             is mendatory to create the voucher.
	 */
	private void setVoucherDetails(Voucher voucher, Receipt receipt, String tenantId, RequestInfo requestInfo,
			FinanceMdmsModel finSerMdms) throws Exception {
		BillDetail billDetail = receipt.getBill().get(0).getBillDetails().get(0);
		String bsCode = billDetail.getBusinessService();
		List<BusinessService> serviceByCode = this.getBusinessServiceByCode(tenantId, bsCode, requestInfo, finSerMdms);
		List<TaxHeadMaster> taxHeadMasterByBusinessServiceCode = this.getTaxHeadMasterByBusinessServiceCode(tenantId,
				bsCode, requestInfo, finSerMdms);
		BusinessService businessService = serviceByCode.get(0);
		voucher.setName(bsCode);
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
		voucher.setDescription(bsCode + " Receipt");
		// checking Whether manualReceipt date will be consider as
		// voucherdate
		if (billDetail.getManualReceiptDate() != null && billDetail.getManualReceiptDate().longValue() != 0
				&& isManualReceiptDateEnabled(tenantId, requestInfo)) {
			voucher.setVoucherDate(dateFormatter.format(new Date(billDetail.getManualReceiptDate())));
		} else {
			voucher.setVoucherDate(dateFormatter.format(new Date(billDetail.getReceiptDate())));
		}
		EgModules egModules = this.getModuleIdByModuleName(COLLECTION_MODULE_NAME, tenantId, requestInfo);
		voucher.setModuleId(Long.valueOf(egModules != null ? egModules.getId().toString() : COLLECTIONS_EG_MODULES_ID));

		voucher.setSource(
				propertiesManager.getReceiptViewSourceUrl() + "?selectedReceipts=" + receipt.getReceiptNumber());

		voucher.setLedgers(new ArrayList<>());
		amountMapwithGlcode = new LinkedHashMap<>();
		// Setting glcode and amount in Map as key value pair.
		for (BillAccountDetail bad : billDetail.getBillAccountDetails()) {
			if (bad.getAdjustedAmount().compareTo(new BigDecimal(0)) != 0) {
				String taxHeadCode = bad.getTaxHeadCode();
				List<TaxHeadMaster> findFirst = taxHeadMasterByBusinessServiceCode.stream()
						.filter(tx -> tx.getTaxhead().equals(taxHeadCode)).collect(Collectors.toList());
				if (findFirst != null && findFirst.isEmpty())
					throw new VoucherCustomException("FAILED",
							"Taxhead code " + taxHeadCode + " is not mapped with BusinessServiceCode " + bsCode);
				String glcode = findFirst.get(0).getGlcode();
				if (amountMapwithGlcode.get(glcode) != null) {
					amountMapwithGlcode.put(glcode, amountMapwithGlcode.get(glcode).add(bad.getAdjustedAmount()));
				} else {
					amountMapwithGlcode.put(glcode, bad.getAdjustedAmount());
				}
			}
		}

		setNetReceiptAmount(voucher, receipt, requestInfo);
		LOGGER.debug("amountMapwithGlcode  ::: {}", amountMapwithGlcode);
		// Iterating map and setting the ledger details to voucher.
		amountMapwithGlcode.entrySet().stream().forEach(entry -> {
				AccountDetail accountDetail = new AccountDetail();
				accountDetail.setGlcode(entry.getKey());
				if (entry.getValue().compareTo(new BigDecimal(0)) == 1) {
					accountDetail.setCreditAmount(entry.getValue().doubleValue());
					accountDetail.setDebitAmount(0d);
				} else {
					accountDetail.setDebitAmount(-entry.getValue().doubleValue());
					accountDetail.setCreditAmount(0d);
				}
				accountDetail.setFunction(new Function());
				accountDetail.getFunction().setCode(businessService.getFunction());
				voucher.getLedgers().add(accountDetail);
		});
	}

	/**
	 * 
	 * @param tenantId
	 * @return Function is used to check the config value for manual receipt
	 *         date consideration.
	 * @throws VoucherCustomException 
	 */
	private boolean isManualReceiptDateEnabled(String tenantId, RequestInfo requestInfo) throws VoucherCustomException {
		VoucherRequest request = new VoucherRequest(tenantId, requestInfo, null);
		StringBuilder url = new StringBuilder(propertiesManager.getErpURLBytenantId(tenantId)
				+ propertiesManager.getManualReceiptDateConfigUrl());
		AppConfigValues convertValue = null;
		try {
			convertValue = mapper.convertValue(serviceRequestRepository.fetchResult(url, request, tenantId), AppConfigValues.class);
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error(
						"ERROR while checking the consideration of manual receipt date. So the receipt date will be consider as the voucher date");
		}
		return convertValue != null ? convertValue.getValue().equalsIgnoreCase("Yes") : false;
	}

	/**
	 * 
	 * @param voucher
	 * @param receipt
	 * @throws VoucherCustomException
	 *             Function is used to set the paid amount as debit in finance
	 *             system.
	 */
	private void setNetReceiptAmount(Voucher voucher, Receipt receipt, RequestInfo requestInfo) throws VoucherCustomException {
		InstrumentAccountCodeContract instrumentAccountCode = this.getInstrumentAccountCode(receipt, requestInfo);
		String glcode = instrumentAccountCode.getAccountCode().getGlcode();
		BigDecimal amountPaid = receipt.getBill().get(0).getBillDetails().get(0).getAmountPaid();
		amountMapwithGlcode.put(glcode, new BigDecimal(-amountPaid.doubleValue()));
	}

	/**
	 * 
	 * @param tenantId
	 * @param bsCode
	 * @return
	 * @throws Exception
	 *             Function is used to get the Business Services based on
	 *             business service code which is mapped in json file
	 */
	private List<BusinessService> getBusinessServiceByCode(String tenantId, String bsCode, RequestInfo requestInfo,
			FinanceMdmsModel finSerMdms) throws Exception {
		List<BusinessService> propertyTaxBusinessService = microServiceUtil.getBusinessService(tenantId, bsCode,
				requestInfo, finSerMdms);
		if (propertyTaxBusinessService.isEmpty()) {
			throw new VoucherCustomException("FAILED", "Business service is not mapped with business code : " + bsCode);
		}
		List<BusinessService> collect = propertyTaxBusinessService.stream().filter(bs -> bs.getCode().equals(bsCode))
				.collect(Collectors.toList());
		return collect;
	}

	/**
	 * 
	 * @param tenantId
	 * @param bsCode
	 * @return
	 * @throws Exception
	 *             Function is used to get the TaxHeadMaster data which is
	 *             mapped to business service code
	 */
	private List<TaxHeadMaster> getTaxHeadMasterByBusinessServiceCode(String tenantId, String bsCode,
			RequestInfo requestInfo, FinanceMdmsModel finSerMdms) throws Exception {
		List<TaxHeadMaster> taxHeadMasters = microServiceUtil.getTaxHeadMasters(tenantId, bsCode, requestInfo,
				finSerMdms);
		return taxHeadMasters;
	}

	/**
	 * 
	 * @param moduleName
	 * @param tenantId
	 * @return Function is used to return the module id which is configure in
	 *         erp setup based on module name
	 * @throws VoucherCustomException 
	 */
	private EgModules getModuleIdByModuleName(String moduleName, String tenantId, RequestInfo requestInfo) throws VoucherCustomException {
		VoucherRequest request = new VoucherRequest(tenantId, requestInfo, null);
		StringBuilder url = new StringBuilder(propertiesManager.getErpURLBytenantId(tenantId) + propertiesManager.getModuleIdSearchUrl() + "?moduleName=" + moduleName);
		try {
			return mapper.convertValue(serviceRequestRepository.fetchResult(url, request, tenantId), EgModules.class);
		} catch (Exception e) {
				LOGGER.error("ERROR while checking the module id for module name {}, default value 10 is considered.",
						moduleName);
		}
		return null;
	}
}
