package org.egov.inv.domain.service;

import static org.springframework.util.StringUtils.isEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.inv.model.RequestInfo;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceipt.ReceiptTypeEnum;
import org.egov.inv.model.MaterialReceiptDetail;
import org.egov.inv.model.MaterialReceiptDetailAddnlinfo;
import org.egov.inv.model.MaterialReceiptSearch;
import org.egov.inv.model.OpeningBalanceRequest;
import org.egov.inv.model.OpeningBalanceResponse;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.repository.MaterialReceiptJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpeningBalanceService extends DomainService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Value("${inv.openbalance.save.topic}")
	private String createTopic;

	@Value("${inv.openbalance.update.topic}")
	private String updateTopic;

	@Value("${inv.openbal.idgen.name}")
	private String idGenNameForTargetNumPath;

	@Autowired
	private MaterialReceiptJdbcRepository jdbcRepository;

	@Autowired
	private MaterialReceiptService materialReceiptService;

	public List<MaterialReceipt> create(OpeningBalanceRequest openBalReq, String tenantId) {
		try {
			validate(openBalReq.getMaterialReceipt(), Constants.ACTION_CREATE);
			openBalReq.getMaterialReceipt().stream().forEach(materialReceipt -> {
				materialReceipt.setId(jdbcRepository.getSequence("seq_materialreceipt"));
				materialReceipt.setMrnStatus(MaterialReceipt.MrnStatusEnum.CREATED);
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
			validate(openBalReq.getMaterialReceipt(), Constants.ACTION_UPDATE);
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
		String code = "MRN/";
		int id = Integer.valueOf(jdbcRepository.getSequence(headerRequest));
		String idgen = String.format("%05d", id);
		String mrnNumber = code + idgen + "/" + year;
		return mrnNumber;
	}

	private void validate(List<MaterialReceipt> receipt, String method) {

		try {
			switch (method) {
				case Constants.ACTION_CREATE: {
					if (receipt == null) {
						throw new InvalidDataException("materialReceipt", ErrorCode.NOT_NULL.getCode(), null);
					} 
					else {
						receipt.stream().forEach(materialReceipt -> {
							checkDuplicateMaterialDetails(materialReceipt.getReceiptDetails());
						});
					}
				}
					break;
	
				case Constants.ACTION_UPDATE: {
					if (receipt == null) {
						throw new InvalidDataException("materialReceipt", ErrorCode.NOT_NULL.getCode(), null);
					}
				}
					break;
	
				}
			Long currentMilllis = System.currentTimeMillis();

			for (MaterialReceipt rcpt : receipt) 
				{
					int index = receipt.indexOf(rcpt) + 1;
					if (isEmpty(rcpt.getFinancialYear())) {
						throw new CustomException("financialYear", "Financial Year Is Required In Row " + index);
					}
					if (isEmpty(rcpt.getReceivingStore().getCode())) {
						throw new CustomException("receivingStore", "StoreName Is Required In Row " + index);
					}

					if (null != rcpt.getReceiptDetails()) {
						for (MaterialReceiptDetail detail : rcpt.getReceiptDetails()) {
							int detailIndex = rcpt.getReceiptDetails().indexOf(detail) + 1;

							if (isEmpty(detail.getMaterial().getCode())) {
								throw new CustomException("materialCode",
										"MaterialName Is Required In Row " + detailIndex);
							}
							if (isEmpty(detail.getUom().getCode())) {
								throw new CustomException("uomCode", "UOM Is Required In Row " + detailIndex);
							}
							if (isEmpty(detail.getReceivedQty())) {
								throw new CustomException("receivedQty", "Quantity Is Required In Row " + detailIndex);
							}
							if (detail.getReceivedQty().doubleValue() <= 0) {
								throw new CustomException("receivedQty",
										"Quantity Should Be Greater Than Zero In Row " + detailIndex);
							}
							if (detail.getUnitRate().doubleValue() <= 0) {
								throw new CustomException("unitRate",
										"UnitRate Should Be Greater Than Zero In Row " + detailIndex);
							}
							if (isEmpty(detail.getUnitRate())) {
								throw new CustomException("unitRate", "UnitRate Is Required In Row " + detailIndex);
							}
							if (null != detail.getReceiptDetailsAddnInfo()) {
								for (MaterialReceiptDetailAddnlinfo addInfo : detail.getReceiptDetailsAddnInfo()) {

									if (null != addInfo.getReceivedDate()
											&& Long.valueOf(addInfo.getReceivedDate()) >= currentMilllis) {
										throw new CustomException("ReceiptDate",
												"ReceiptDate  Must Be Less Than Or Equal To Today's Date In Row "
														+ detailIndex);
									}
									if (null != addInfo.getExpiryDate()
											&& Long.valueOf(addInfo.getExpiryDate()) <= currentMilllis) {
										throw new CustomException("ExpiryDate",
												"ExpiryDate  Must Be Greater Than Or Equal To Today's Date In Row "
														+ detailIndex);
									}
								}
							}

						}
					} else
						throw new CustomException("receiptDetail", "Please Enter Required Fields");

				}
			

		} catch (IllegalArgumentException e) {

		}
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
		Uom uom = getUom(tenantId, detail.getUom().getCode(), new RequestInfo());
		detail.setUom(uom);

		if (null != detail.getReceivedQty() && null != uom.getConversionFactor()) {
			Double convertedReceivedQuantity = getSaveConvertedQuantity(detail.getReceivedQty().doubleValue(),
					uom.getConversionFactor().doubleValue());
			detail.setReceivedQty(BigDecimal.valueOf(convertedReceivedQuantity));
		}

	}

	private void checkDuplicateMaterialDetails(List<MaterialReceiptDetail> materialReceiptDetails) {
		HashSet<String> hashSet = new HashSet<>();
		materialReceiptDetails.stream().forEach(materialReceiptDetail -> {
			if (false == hashSet.add(materialReceiptDetail.getMaterial().getCode())) {
				throw new CustomException("inv.0015",
						materialReceiptDetail.getMaterial().getCode() + " Combination Is Already Entered");
			}
		});
	}
}
