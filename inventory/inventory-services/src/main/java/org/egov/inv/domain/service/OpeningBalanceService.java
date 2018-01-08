package org.egov.inv.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.FinancialYear;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceipt.ReceiptTypeEnum;
import org.egov.inv.model.MaterialReceiptDetail;
import org.egov.inv.model.MaterialReceiptDetailAddnlinfo;
import org.egov.inv.model.MaterialReceiptSearch;
import org.egov.inv.model.OpeningBalanceRequest;
import org.egov.inv.model.OpeningBalanceResponse;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Store;
import org.egov.inv.model.StoreGetRequest;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.repository.MaterialReceiptJdbcRepository;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;


@Service
public class OpeningBalanceService extends DomainService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Value("${inv.openbalance.save.topic}")
	private String createTopic;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Value("${inv.openbalance.update.topic}")
	private String updateTopic;

	@Value("${inv.openbal.idgen.name}")
	private String idGenNameForTargetNumPath;

	@Autowired
	private MaterialReceiptJdbcRepository jdbcRepository;
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private StoreJdbcRepository storeJdbcRepository;

	@Autowired
	private MaterialReceiptService materialReceiptService;

	public List<MaterialReceipt> create(OpeningBalanceRequest openBalReq, String tenantId) {
		try {
			validate(openBalReq.getMaterialReceipt(), Constants.ACTION_CREATE,tenantId,openBalReq.getRequestInfo());
			openBalReq.getMaterialReceipt().stream().forEach(materialReceipt -> {
				materialReceipt.setId(jdbcRepository.getSequence("seq_materialreceipt"));
				materialReceipt.setMrnStatus(MaterialReceipt.MrnStatusEnum.APPROVED);
				if (isEmpty(materialReceipt.getTenantId())) {
					materialReceipt.setTenantId(tenantId);
				}
				materialReceipt.setReceiptType(ReceiptTypeEnum.valueOf("OPENING_BALANCE"));
				String mrnNumber = appendString(materialReceipt);
				materialReceipt.setMrnNumber(mrnNumber);
				if (null != materialReceipt.getReceiptDetails()) {
					materialReceipt.getReceiptDetails().stream().forEach(detail -> {
						detail.setId(jdbcRepository.getSequence("seq_materialreceiptdetail"));
						setQuantity(tenantId, detail);
						convertRate(tenantId, detail);
						if (isEmpty(detail.getTenantId())) {
							detail.setTenantId(tenantId);
						}
						if (null != detail.getReceiptDetailsAddnInfo()) {
							detail.getReceiptDetailsAddnInfo().stream().forEach(addinfo -> {
								addinfo.setId(jdbcRepository.getSequence("seq_materialreceiptdetailaddnlinfo"));
								if (isEmpty(addinfo.getTenantId())) {
									addinfo.setTenantId(tenantId);
								}
							});
						}
					});
				}
			});
			for (MaterialReceipt material : openBalReq.getMaterialReceipt()) {
				material.setAuditDetails(getAuditDetails(openBalReq.getRequestInfo(), "CREATE"));
				material.setId(jdbcRepository.getSequence(material));
			}
			kafkaTemplate.send(createTopic, openBalReq);
			return openBalReq.getMaterialReceipt();
		} catch (CustomBindException e) {
			throw e;
		}
	}

	public List<MaterialReceipt> update(OpeningBalanceRequest openBalReq, String tenantId) {
		try {
			validate(openBalReq.getMaterialReceipt(), Constants.ACTION_UPDATE,tenantId,openBalReq.getRequestInfo());
			List<String> materialReceiptDetailIds = new ArrayList<>();
			List<String> materialReceiptDetailAddlnInfoIds = new ArrayList<>();
			openBalReq.getMaterialReceipt().stream().forEach(materialReceipt -> {
				if (isEmpty(materialReceipt.getTenantId())) {
					materialReceipt.setTenantId(tenantId);
				}
				materialReceipt.getReceiptDetails().stream().forEach(detail -> {
					if (isEmpty(detail.getTenantId())) {
						detail.setTenantId(tenantId);
					}
					setQuantity(tenantId, detail);
					if (isEmpty(detail.getId())) {
						setMaterialDetails(tenantId, detail);
					}
					materialReceiptDetailIds.add(detail.getId());

					detail.getReceiptDetailsAddnInfo().stream().forEach(addinfo -> {
						if (isEmpty(addinfo.getTenantId())) {
							addinfo.setTenantId(tenantId);
							materialReceiptDetailAddlnInfoIds.add(addinfo.getId());
						}
					});
				});
			});

			for (MaterialReceipt material : openBalReq.getMaterialReceipt()) {
				material.setAuditDetails(getAuditDetails(openBalReq.getRequestInfo(), "UPDATE"));
			}
			kafkaTemplate.send(updateTopic, openBalReq);
			return openBalReq.getMaterialReceipt();
		} catch (CustomBindException e) {
			throw e;
		}
	}

	public OpeningBalanceResponse search(MaterialReceiptSearch materialReceiptSearch) {
		Pagination<MaterialReceipt> materialReceiptPagination = materialReceiptService.search(materialReceiptSearch);
		OpeningBalanceResponse response = new OpeningBalanceResponse();
		return response.responseInfo(null).materialReceipt(materialReceiptPagination.getPagedData().size() > 0
				? materialReceiptPagination.getPagedData() : Collections.EMPTY_LIST);
	}

	private String appendString(MaterialReceipt headerRequest) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String code = "OPB/";
		int id = Integer.valueOf(jdbcRepository.getSequence(headerRequest));
		String idgen = String.format("%05d", id);
		String mrnNumber = code + idgen + "/" + year;
		return mrnNumber;
	}

	private void validate(List<MaterialReceipt> receipt, String method, String tenantId,RequestInfo info) {
		InvalidDataException errors = new InvalidDataException();

		try {
			switch (method) {
				case Constants.ACTION_CREATE: {
					if (receipt == null) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(),"materialReceipt", null);
					} 
				}
					break;
	
				case Constants.ACTION_UPDATE: {
					if (receipt == null) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(),"materialReceipt", null);
					}
				}
					break;
	
				}

			for (MaterialReceipt rcpt : receipt) 
				{
					int index = receipt.indexOf(rcpt) + 1;
					ObjectMapper mapper = new ObjectMapper();
					
					if (!isEmpty(rcpt.getFinancialYear())) {
					JSONArray finYears = mdmsRepository.getByCriteria(tenantId, "egf-master", "FinancialYear", "finYearRange",rcpt.getFinancialYear() , info);
					if(finYears != null && finYears.size() > 0)
					{
					for (int i = 0; i < finYears.size(); i++) {
						FinancialYear fin = mapper.convertValue(finYears.get(i), FinancialYear.class);
						if (getCurrentDate() >= fin.getStartingDate().getTime()  && getCurrentDate() <= fin.getEndingDate().getTime()) {
							rcpt.setFinancialYear(fin.getFinYearRange());
						}
						else
							errors.addDataError( ErrorCode.FIN_CUR_YEAR.getCode(),"Financial Year ("+rcpt.getFinancialYear()+")");
						}
					}else
						errors.addDataError( ErrorCode.FIN_YEAR_NOT_EXIST.getCode(),rcpt.getFinancialYear());
					}else{
						errors.addDataError( ErrorCode.FIN_YEAR_NOT_EXIST.getCode(),rcpt.getFinancialYear());
					}
					if (isEmpty(rcpt.getReceivingStore().getCode())) {
						errors.addDataError(ErrorCode.RECEIVING_STORE_NOT_EXIST.getCode(),rcpt.getReceivingStore().getCode());
					}else{
						if(validateStore(tenantId,rcpt)){
							errors.addDataError(ErrorCode.INVALID_ACTIVE_VALUE.getCode(),"Receiving Store "+ rcpt.getReceivingStore().getCode());
						}
					}

					if (null != rcpt.getReceiptDetails()) {
						for (MaterialReceiptDetail detail : rcpt.getReceiptDetails()) {
							int detailIndex = rcpt.getReceiptDetails().indexOf(detail) + 1;
							
							if (isEmpty(detail.getUom().getCode())) {
								errors.addDataError( ErrorCode.UOM_CODE_NOT_EXIST.getCode(),detail.getUom().getCode()+" at serial no."+ detailIndex);
							}
							
							if (isEmpty(detail.getMaterial().getCode())) {
								errors.addDataError(ErrorCode.MATERIAL_NAME_NOT_EXIST.getCode(),detail.getMaterial().getCode()+" at serial no."+ detailIndex);
							}
							
							if(!validateUom(tenantId,detail))
							{
								errors.addDataError(ErrorCode.CATGRY_MATCH.getCode(),detail.getMaterial().getCode(),detail.getUom().getCode(),"At Row "+detailIndex);
							}
							
							if (isEmpty(detail.getUserReceivedQty())) {
								errors.addDataError(ErrorCode.RCVED_QTY_NOT_EXIST.getCode(),detail.getUserReceivedQty()+" at serial no."+ detailIndex);	
							}else
								{
									int res1 =detail.getUserReceivedQty().compareTo(BigDecimal.ZERO);
								if (res1 != 1) {
									errors.addDataError(ErrorCode.RCVED_QTY_GT_ZERO.getCode(),detail.getUserReceivedQty()+" at serial no."+ detailIndex);
								}
								}
							if (isEmpty(detail.getUnitRate())) {
								errors.addDataError(ErrorCode.UNIT_RATE_NOT_EXIST.getCode(),detail.getUnitRate()+" at serial no."+ detailIndex);
							}else
							{
							int res =detail.getUnitRate().compareTo(BigDecimal.ZERO);
								if (res != 1) {
								errors.addDataError(ErrorCode.UNIT_RATE_GT_ZERO.getCode(),detail.getUnitRate()+" at serial no."+ detailIndex);
							}
							}
							
							Material material = materialService.fetchMaterial(tenantId, detail.getMaterial().getCode(), new org.egov.inv.model.RequestInfo());

							if (null != detail.getReceiptDetailsAddnInfo()) {
								for (MaterialReceiptDetailAddnlinfo addInfo : detail.getReceiptDetailsAddnInfo()) {

									
									if(null != material && material.getLotControl() == true && isEmpty(addInfo.getLotNo())){
										errors.addDataError(ErrorCode.LOT_NO_NOT_EXIST.getCode(),addInfo.getLotNo()+" at serial no."+ detailIndex);
									}
									
									if(null != material && material.getShelfLifeControl() == true && isEmpty(addInfo.getExpiryDate()) ||
											(!isEmpty(addInfo.getExpiryDate()) && !(addInfo.getExpiryDate().doubleValue() > 0))){
										errors.addDataError(ErrorCode.EXP_DATE_NOT_EXIST.getCode(),addInfo.getExpiryDate()+" at serial no."+ detailIndex);
									}
									
									
									if (null != addInfo.getReceivedDate()
											&& Long.valueOf(addInfo.getReceivedDate()) > getCurrentDate()) {
										String date = convertEpochtoDate(addInfo.getReceivedDate());
										errors.addDataError(ErrorCode.RCPT_DATE_LE_TODAY.getCode(),date+" at serial no."+ detailIndex);
												
									}
									if (null != addInfo.getExpiryDate()
											&& Long.valueOf(addInfo.getExpiryDate()) < getCurrentDate()) {
										String date = convertEpochtoDate(addInfo.getExpiryDate());
										errors.addDataError(ErrorCode.EXP_DATE_GE_TODAY.getCode(),date+" at serial no."+ detailIndex);
									}
								}
							}
						}
					} else
						errors.addDataError(ErrorCode.NULL_VALUE.getCode(),"receiptDetail" );

				}
			

		} catch (IllegalArgumentException e) {

		}
		if (errors.getValidationErrors().size() > 0)
			throw errors;
	}

	private void setMaterialDetails(String tenantId, MaterialReceiptDetail materialReceiptDetail) {
		materialReceiptDetail.setId(jdbcRepository.getSequence("seq_materialreceiptdetail"));
		if (isEmpty(materialReceiptDetail.getTenantId())) {
			materialReceiptDetail.setTenantId(tenantId);
		}

		materialReceiptDetail.getReceiptDetailsAddnInfo().forEach(materialReceiptDetailAddnlInfo -> {
			materialReceiptDetailAddnlInfo.setId(jdbcRepository.getSequence("seq_materialreceiptdetailaddnlinfo"));
			if (isEmpty(materialReceiptDetailAddnlInfo.getTenantId())) {
				materialReceiptDetailAddnlInfo.setTenantId(tenantId);
			}
		});
	}

	private void setQuantity(String tenantId, MaterialReceiptDetail detail) {
		Uom uom = (Uom) mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code", detail.getUom().getCode(), Uom.class);
		detail.setUom(uom);

		if (null != detail.getUserReceivedQty() && null != uom.getConversionFactor()) {
			BigDecimal convertedReceivedQuantity = getSaveConvertedQuantity(detail.getUserReceivedQty(),
					uom.getConversionFactor());
			detail.setReceivedQty(convertedReceivedQuantity);
		}

	}
	
	private boolean validateUom(String tenantId, MaterialReceiptDetail detail) {
		Material material = materialService.fetchMaterial(tenantId, detail.getMaterial().getCode(), new org.egov.inv.model.RequestInfo());
		String uomCategory= material.getBaseUom().getUomCategory();
		List<String> uomList = new ArrayList<>();
		List<Object> objectList= mdmsRepository.fetchObjectList(tenantId, "common-masters", "Uom",  "uomCategory", uomCategory, Uom.class);
		for(Object o : objectList){
			Uom uom = (Uom) o;
			uomList.add(uom.getCode());
		}
		return  uomList.stream().anyMatch(Collections.singletonList(detail.getUom().getCode()) ::contains);
	}
	
	private boolean validateStore(String tenantId, MaterialReceipt rcpt) {
		StoreGetRequest storeEntity = StoreGetRequest.builder()
											 .code(Collections.singletonList(rcpt.getReceivingStore().getCode()))
										     .tenantId(tenantId)
										     .active(true)
											 .build();
		Pagination<Store> store =storeJdbcRepository.search(storeEntity);
		if(store.getPagedData().size() > 0)
		{
			return false;
		}
		return  true;
	}
	
	private void convertRate(String tenantId, MaterialReceiptDetail detail) {
		Uom uom = (Uom) mdmsRepository.fetchObject(tenantId, "common-masters", "Uom",  "code", detail.getUom().getCode(), Uom.class);
        detail.setUom(uom);

		if (null != detail.getUnitRate() && null != uom.getConversionFactor()) {
			BigDecimal convertedRate = getSaveConvertedRate(detail.getUnitRate(),
					uom.getConversionFactor());
			detail.setUnitRate(convertedRate);
		}

	}
	
	 private Long getCurrentDate() {
	        return currentEpochWithoutTime() + (24 * 60 * 60) - 1;
	    }
	 
	 private String convertEpochtoDate(Long date)
	 {
		 Date epoch = new Date(date);
		 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		 String s2 = format.format(epoch);
		 return s2;
	 }
}
