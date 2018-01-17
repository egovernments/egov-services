package org.egov.inv.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.egov.inv.model.Material;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.PriceListRequest;
import org.egov.inv.model.PriceListResponse;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Supplier;
import org.egov.inv.model.SupplierGetRequest;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.PriceListEntity;
import org.egov.inv.persistence.repository.MaterialJdbcRepository;
import org.egov.inv.persistence.repository.PriceListDetailJdbcRepository;
import org.egov.inv.persistence.repository.PriceListJdbcRepository;
import org.egov.inv.persistence.repository.PriceListRepository;
import org.egov.inv.persistence.repository.PurchaseOrderJdbcRepository;
import org.egov.inv.persistence.repository.SupplierJdbcRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PriceListService extends DomainService {


    public static final String UPDATE = "UPDATE";
    public static final String CREATE = "CREATE";

    @Value("${inv.pricelists.save.topic}")
    private String saveTopic;

    @Value("${inv.pricelists.save.key}")
    private String saveKey;

    @Value("${inv.pricelists.update.topic}")
    private String updateTopic;

    @Value("${inv.pricelists.update.key}")
    private String updateKey;

    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private PriceListJdbcRepository priceListJdbcRepository;

    @Autowired
    private PriceListDetailJdbcRepository priceListDetailsJdbcRepository;
    
    @Autowired
    private PurchaseOrderJdbcRepository purchaseOrderJdbcRepository;

    @Autowired
    private MaterialJdbcRepository materialJdbcRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    SupplierJdbcRepository supplierJdbcRepository;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private MdmsRepository mdmsRepository;
	
    public static final String SEQ_PRICELIST = "seq_priceList";
    public static final String SEQ_PRICELISTDETAILS = "seq_pricelistdetails";

    public PriceListResponse save(PriceListRequest priceListRequest, String tenantId) {

        try {

            List<String> priceListIdList = priceListJdbcRepository.getSequence(PriceList.class.getSimpleName(), priceListRequest.getPriceLists().size());
            priceListRequest.getPriceLists().forEach(priceList -> {
            	priceList.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
            	if(priceList.getRateContractNumber()!=null)
            		priceList.setRateContractNumber(priceList.getRateContractNumber().toUpperCase());
            	if(priceList.getAgreementNumber() != null)
            		priceList.setAgreementNumber(priceList.getAgreementNumber().toUpperCase());
            });
            validate(priceListRequest.getPriceLists(), Constants.ACTION_CREATE,tenantId,priceListRequest);
            priceListRequest.getPriceLists().forEach(priceList -> {
                priceList.getPriceListDetails().forEach(priceListDetail -> {
                	
                	priceListDetail.setTenantId(priceList.getTenantId());
                    
                	priceListDetail.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
                    priceList.setRateContractNumber(priceList.getRateContractNumber().toUpperCase());
                    priceList.setAgreementNumber(priceList.getAgreementNumber().toUpperCase());

                    if (priceListDetail.getFromDate() == null) {
                        priceListDetail.setFromDate(priceList.getAgreementStartDate());
                    }
                    if (priceListDetail.getToDate() == null) {
                        priceListDetail.setToDate(priceList.getAgreementEndDate());
                    }
                    if (priceListDetail.getUom().getCode() != null) {
                        if (priceListDetail.getQuantity() != null)
                            priceListDetail.setQuantity((priceListDetail.getUom().getConversionFactor()).doubleValue() * priceListDetail.getQuantity());
                    }
                });
            });

			for (int i = 0; i <= priceListIdList.size() - 1; i++) {

				priceListRequest.getPriceLists().get(i).setId(priceListIdList.get(i).toString());

				if (priceListRequest.getPriceLists().get(i).getTenantId() == null) {
					priceListRequest.getPriceLists().get(i).setTenantId(tenantId);

				}
				for (PriceListDetails priceListDetail : priceListRequest.getPriceLists().get(i).getPriceListDetails()) {
					if (priceListDetail.getTenantId() == null) {
						priceListDetail.setTenantId(tenantId);
					}
				}

				List<String> priceListDetailsIdList = priceListJdbcRepository.getSequence(
						PriceListDetails.class.getSimpleName(),
						priceListRequest.getPriceLists().get(i).getPriceListDetails().size());
				for (int j = 0; j <= priceListDetailsIdList.size() - 1; j++) {
					priceListRequest.getPriceLists().get(i).getPriceListDetails().get(j)
							.setId(priceListDetailsIdList.get(j).toString());
				}
			}

            kafkaQue.send(saveTopic, saveKey, priceListRequest);

            PriceListResponse response = PriceListResponse.builder()
                    .priceLists(priceListRequest.getPriceLists())
                    .responseInfo(getResponseInfo(priceListRequest.getRequestInfo()))
                    .build();

            return response;
        } catch (CustomBindException e) {
            throw e;
        }
    }

    public PriceListResponse update(PriceListRequest priceListRequest,
                                    String tenantId) {

        try {
        	
        	// TODO: Do not allow them to modify supplier,rate type, agreement start and end date in modify level.
        	priceListRequest.getPriceLists().stream().forEach(priceList -> {
                priceList.setAuditDetails(mapAuditDetailsForUpdate(priceListRequest.getRequestInfo()));
            	if(priceList.getRateContractNumber()!=null)
            		priceList.setRateContractNumber(priceList.getRateContractNumber().toUpperCase());
            	if(priceList.getAgreementNumber() != null)
            		priceList.setAgreementNumber(priceList.getAgreementNumber().toUpperCase());
        	});
        	
            validate(priceListRequest.getPriceLists(), Constants.ACTION_UPDATE,tenantId,priceListRequest);
            List<String> ids = new ArrayList<String>();
            priceListRequest.getPriceLists().stream().forEach(priceList -> {
                priceList.setAuditDetails(mapAuditDetailsForUpdate(priceListRequest.getRequestInfo()));
                priceList.setRateContractNumber(priceList.getRateContractNumber().toUpperCase());
                priceList.setAgreementNumber(priceList.getAgreementNumber().toUpperCase());

                if (priceList.getTenantId() == null) {
                    priceList.setTenantId(tenantId);
                }

                for (PriceListDetails priceListDetail : priceList.getPriceListDetails()) {
                    if (priceListDetail.getId() == null) {
                        priceListDetail.setId(priceListDetailsJdbcRepository.getSequence(PriceListDetails.class.getSimpleName(), 1).get(0));
                        priceListDetail.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
                    }
                    if (priceListDetail.getTenantId() == null) {
                        priceListDetail.setTenantId(tenantId);
                    }
                    if (priceListDetail.getUom().getCode() != null) {
                        if (priceListDetail.getQuantity() != null)
                            priceListDetail.setQuantity((priceListDetail.getUom().getConversionFactor()).doubleValue() * priceListDetail.getQuantity());
                    }
                    if (priceListDetail.getFromDate() == null) {
                        priceListDetail.setFromDate(priceList.getAgreementStartDate());
                    }
                    if (priceListDetail.getToDate() == null) {
                        priceListDetail.setToDate(priceList.getAgreementEndDate());
                    }
					ids.add(priceListDetail.getId());
				}

				priceListDetailsJdbcRepository.markDeleted(ids, tenantId, "pricelistdetails", "pricelist",priceListRequest.getPriceLists().get(0).getId());

			});

            kafkaQue.send(updateTopic, updateKey, priceListRequest);
            
            PriceListResponse response = new PriceListResponse();
            response.setResponseInfo(getResponseInfo(priceListRequest
                    .getRequestInfo()));
            response.setPriceLists(priceListRequest.getPriceLists());

            return response;
        } catch (CustomBindException e) {
            throw e;
        }
    }

    public PriceListResponse search(
            PriceListSearchRequest priceListSearchRequest, RequestInfo requestInfo) {

        Pagination<PriceList> searchPriceLists = priceListRepository.search(priceListSearchRequest);

        PriceListResponse response = new PriceListResponse();
        response.setPriceLists(searchPriceLists.getPagedData().size() > 0 ? searchPriceLists.getPagedData() : Collections.emptyList());
        return response;
    }

    public PriceListResponse searchPriceList(
            PriceListSearchRequest priceListSearchRequest, RequestInfo requestInfo) {

        List<PriceList> searchPriceLists = priceListRepository.searchPriceList(priceListSearchRequest);

        PriceListResponse response = new PriceListResponse();
        response.setPriceLists(searchPriceLists.size() > 0 ? searchPriceLists : Collections.emptyList());
        return response;
    }

    private void validate(List<PriceList> priceLists, String method,String tenantId,PriceListRequest priceListRequest) {
        InvalidDataException errors = new InvalidDataException();
        try {

            switch (method) {
				case Constants.ACTION_CREATE: {
					if (priceLists == null) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(), "pricelists", "null");
					}
					for (PriceList pl : priceLists) {
						if (isEmpty(pl.getTenantId())) {
							pl.setTenantId(tenantId);
						}
						pl.getSupplier().setTenantId(tenantId);
						if(pl.getRateContractNumber() != null)
						if (!priceListJdbcRepository.uniqueCheck("rateContractNumber",
								new PriceListEntity().toEntity(pl))) {
							errors.addDataError(ErrorCode.CODE_ALREADY_EXISTS.getCode(), "Rate Contract Number",
									pl.getRateContractNumber().toUpperCase());
						}
						if (!priceListJdbcRepository.uniqueCheck("agreementNumber",
								new PriceListEntity().toEntity(pl))) {
							errors.addDataError(ErrorCode.CODE_ALREADY_EXISTS.getCode(), "Agreement Number",
									pl.getAgreementNumber().toUpperCase());
						}
					}
				}
                break;

                case Constants.ACTION_UPDATE: {
                    if (priceLists == null) {
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "pricelists", "null");
                    }
                    
				for (PriceList pl : priceLists) {
					if (isEmpty(pl.getTenantId())) {
						pl.setTenantId(tenantId);
						
					}
					pl.getSupplier().setTenantId(tenantId);
					
					if (pl.getId() == null) {
						throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), pl.getId());
					}
					if (!priceListJdbcRepository.uniqueCheck("rateContractNumber",
							new PriceListEntity().toEntity(pl))) {
						errors.addDataError(ErrorCode.CODE_ALREADY_EXISTS.getCode(), "Rate Contract Number",
								pl.getRateContractNumber().toUpperCase());
					}
					if (!priceListJdbcRepository.uniqueCheck("agreementNumber",
							new PriceListEntity().toEntity(pl))) {
						errors.addDataError(ErrorCode.CODE_ALREADY_EXISTS.getCode(), "Agreement Number",
								pl.getAgreementNumber().toUpperCase());
					}
				}
                }
                break;
            }
            
            Long currentMilllis = System.currentTimeMillis();

			for (PriceList pl : priceLists) {

				
				if (!Arrays.stream(PriceList.RateTypeEnum.values())
						.anyMatch((t) -> t.equals(PriceList.RateTypeEnum.fromValue(pl.getRateType().toString())))) {
					throw new CustomException("rateType", "Please enter a valid RateType");
				}

				for (PriceListDetails pld : pl.getPriceListDetails()) {
					// VALIDATE ITEM, UOM CODE BEFORE PUSHING DATA.
					pld.getMaterial().setTenantId(tenantId);
					pld.getUom().setTenantId(tenantId);

					Material material = materialService.fetchMaterial(tenantId, pld.getMaterial().getCode(),
							priceListRequest.getRequestInfo());
					Uom uom = (Uom) mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code",
							pld.getUom().getCode(), Uom.class);
					
					if (isEmpty(material.getCode())) {
						errors.addDataError(ErrorCode.MATERIAL_NAME_NOT_EXIST.getCode(),
								material.getCode() + " at serial no." + (pl.getPriceListDetails().indexOf(pld) + 1));
					}
					if (isEmpty(uom.getCode())) {
						errors.addDataError(ErrorCode.UOM_CODE_NOT_EXIST.getCode(), pld.getUom().getCode()
								+ " at serial no." + (pl.getPriceListDetails().indexOf(pld) + 1));
					}

					if(pld.getRatePerUnit()<=0) {
						errors.addDataError(ErrorCode.UNIT_PRICE_GT_ZERO.getCode(), null + " at serial no." + (pl.getPriceListDetails().indexOf(pld) + 1));
					}
					
					for (PriceListDetails plds : pl.getPriceListDetails()) {

						if (pld != plds && pld.getMaterial().getCode().toString()
								.equals(plds.getMaterial().getCode().toString())) {
							throw new CustomException("Material", "Duplicate material(s) " + ""
									+ " found, please remove them and try creating pricelist");
						}
					}
				}

				// VALIDATION TO CHECK WHETHER QUANTITY PRESENT OR NOT FOR ONE
				// TIME TENDER TYPE ITEMS. HERE CHECK RATE TYPE IS "ONE TIME
				// TENDER" THEN ITERATE.

				if (pl.getRateType().name().equals(PriceList.RateTypeEnum.ONE_TIME_TENDER.name())) {

					for (PriceListDetails priceListDetail : pl.getPriceListDetails()) {

						if (priceListDetail.getQuantity() == null) {
							errors.addDataError(ErrorCode.QUANTITY_GT_ZERO_TENDER.getCode(), "quantity", "");
						} else if (priceListDetail.getQuantity() != null
								&& priceListDetail.getQuantity().doubleValue() <= 0) {
							errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "quantity",
									"" + (pl.getPriceListDetails().indexOf(priceListDetail) + 1));
						}

					}
					
				}

                //Supplier reference validation
                if(null != pl.getSupplier() && !isEmpty(pl.getSupplier().getCode()))
                if(!isValidSupplier(tenantId, pl.getSupplier().getCode()))
                	errors.addDataError(ErrorCode.INVALID_REF_VALUE.getCode(), "Supplier", pl.getSupplier().getCode());
				
				// Negative epoch time is for years below 1970
				if (Long.valueOf(pl.getAgreementDate()) < 0) {
					throw new CustomException("agreementDate", "Enter a valid Agreement Date");
				}

				if (Long.valueOf(pl.getRateContractDate()) < 0) {
					throw new CustomException("rateContractDate", "Enter a valid Rate Contract Date");
				}
				if (Long.valueOf(pl.getAgreementStartDate()) < 0) {
					throw new CustomException("agreementStartDate", "Enter a valid Agreement Start Date");
				}
				if (Long.valueOf(pl.getAgreementEndDate()) < 0) {
					throw new CustomException("agreementEndDate", "Enter a valid Agreement End Date");
				}
				if (Long.valueOf(pl.getAgreementDate()) > currentMilllis) {
					errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "agreementDate", convertEpochtoDate(pl.getAgreementDate()));
				}

				if (Long.valueOf(pl.getRateContractDate()) > currentMilllis) {
					errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "rateContractDate", convertEpochtoDate(pl.getRateContractDate()));
				}

				if (Long.valueOf(pl.getAgreementStartDate()) > currentMilllis) {
					errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "agreementStartDate",
							convertEpochtoDate(pl.getAgreementStartDate()));
				}
				
				if (Long.valueOf(pl.getAgreementEndDate()) < Long.valueOf(pl.getAgreementStartDate())) {
					errors.addDataError(ErrorCode.DATE1_GE_DATE2.getCode(), "agreementEndDate", "agreementStartDate",
							convertEpochtoDate(pl.getAgreementEndDate()), convertEpochtoDate(pl.getAgreementStartDate()));

				}
				//No clear input in hld.
				/*if (Long.valueOf(pl.getAgreementStartDate()) < Long.valueOf(pl.getAgreementDate())) {
					errors.addDataError(ErrorCode.DATE1_GE_DATE2.getCode(), "agreementStartDate", "agreementDate",
							convertEpochtoDate(pl.getAgreementStartDate()), convertEpochtoDate(pl.getAgreementDate()));

				}*/

			}
//TODO: CHECK IN CASE OF UPDATE..
			
            if (priceListJdbcRepository.isDuplicateContract(priceLists, method)) {
                throw new CustomException("inv.0011", "A ratecontract already exists in the system for the given material in the specified time duration. Please select alternate duration for the contract.");
            }

        } catch (IllegalArgumentException e) {

        }
        // THROW errors here. if found in above case.
        if (errors.getValidationErrors().size() > 0)
			throw errors;

    }
    
	private boolean isValidSupplier(String tenantId, String supplierCode) {
		 SupplierGetRequest supplierGetRequest = SupplierGetRequest.builder()
				 									.code(Collections.singletonList(supplierCode))
				 									.tenantId(tenantId)
				 									.active(true)
				 									.build();
        Pagination<Supplier> suppliers = supplierJdbcRepository.search(supplierGetRequest);
        if(suppliers.getPagedData().size() > 0)
       	 return true;
        return false;
	}
    
    private String convertEpochtoDate(Long date)
	 {
		 Date epoch = new Date(date);
		 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		 String s2 = format.format(epoch);
		 return s2;
	 }

	public PriceListResponse getTenderUsedQty(String material, String priceListId, String uom, String tenantId){
		PriceListResponse plr = new PriceListResponse();
		Uom uomFetch = (Uom) mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code", uom, Uom.class);
		plr.setPriceLists(Arrays.asList(PriceList.builder().priceListDetails(Arrays.asList(PriceListDetails.builder().tenderUsedQuantity(purchaseOrderJdbcRepository.getTenderUsedQty(material, priceListId).divide(uomFetch.getConversionFactor())).build())).build()));
		return plr;
	}
    
}
