package org.egov.inv.domain.service;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.persistence.entity.PriceListEntity;
import org.egov.inv.persistence.repository.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

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
    private MaterialJdbcRepository materialJdbcRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    SupplierJdbcRepository supplierJdbcRepository;

    public static final String SEQ_PRICELIST = "seq_priceList";
    public static final String SEQ_PRICELISTDETAILS = "seq_pricelistdetails";

    public PriceListResponse save(PriceListRequest priceListRequest, String tenantId) {

        try {

            List<String> priceListIdList = priceListJdbcRepository.getSequence(PriceList.class.getSimpleName(), priceListRequest.getPriceLists().size());
            validate(priceListRequest.getPriceLists(), Constants.ACTION_CREATE);
            priceListRequest.getPriceLists().forEach(priceList -> {
                priceList.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
                priceList.getPriceListDetails().forEach(priceListDetail -> {
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
                PriceList priceList1 = priceListRequest.getPriceLists().get(i);
                if (!priceListJdbcRepository.uniqueCheck("rateContractNumber", new PriceListEntity().toEntity(priceList1))) {
                    throw new CustomException("inv.0011", "RateContract Number already exists " + priceList1.getRateContractNumber());
                }
                priceListRequest.getPriceLists().get(i)
                        .setId(priceListIdList.get(i).toString());

                if (priceListRequest.getPriceLists().get(i).getTenantId() == null) {
                    priceListRequest.getPriceLists().get(i).setTenantId(tenantId);
                    for (PriceListDetails priceListDetail : priceListRequest.getPriceLists().get(i).getPriceListDetails()) {
                        if (priceListDetail.getTenantId() == null) {
                            priceListDetail.setTenantId(tenantId);
                        }
                    }
                }

                List<String> priceListDetailsIdList = priceListJdbcRepository.getSequence(PriceListDetails.class.getSimpleName(), priceListRequest.getPriceLists().get(i).getPriceListDetails().size());
                for (int j = 0; j <= priceListDetailsIdList.size() - 1; j++) {
                    priceListRequest.getPriceLists().get(i)
                            .getPriceListDetails().get(j)
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
            validate(priceListRequest.getPriceLists(), Constants.ACTION_UPDATE);
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

            });

            kafkaQue.send(updateTopic, updateKey, priceListRequest);
            priceListDetailsJdbcRepository.markDeleted(ids, tenantId, "pricelistdetails", "pricelist", priceListRequest.getPriceLists().get(0).getId());
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

    private void validate(List<PriceList> priceLists, String method) {
        InvalidDataException errors = new InvalidDataException();
        try {

            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (priceLists == null) {
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "pricelists", "null");
                    }
                }
                break;

                case Constants.ACTION_UPDATE: {
                    if (priceLists == null) {
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "pricelists", "null");
                    }
                }
                break;

            }


            Long currentMilllis = System.currentTimeMillis();

            for (PriceList pl : priceLists) {

                for (PriceListDetails pld : pl.getPriceListDetails()) {
                    for (PriceListDetails plds : pl.getPriceListDetails()) {
                        if (pld != plds && pld.getMaterial().getCode().toString().equals(plds.getMaterial().getCode().toString())) {
                            throw new CustomException("Material", "Duplicate material(s) " + "" + " found, please remove them and try creating pricelist");
                        }
                    }
                }

                if (!Arrays.stream(PriceList.RateTypeEnum.values()).anyMatch((t) -> t.equals(PriceList.RateTypeEnum.fromValue(pl.getRateType().toString())))) {
                    throw new CustomException("rateType", "Please enter a valid RateType");
                }

                if (Long.valueOf(pl.getAgreementDate()) > currentMilllis) {
					String agreementDate = convertEpochtoDate(pl.getAgreementDate());
                    errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "agreementDate",agreementDate);
                }

                if (Long.valueOf(pl.getRateContractDate()) > currentMilllis) {
					String rateContractDate = convertEpochtoDate(pl.getRateContractDate());
                    errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "rateContractDate", rateContractDate);
                }

                if (Long.valueOf(pl.getAgreementStartDate()) > currentMilllis) {
					String agreementStartDate = convertEpochtoDate(pl.getAgreementStartDate());
                    errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "agreementStartDate",agreementStartDate);
                }

                if (Long.valueOf(pl.getAgreementEndDate()) < Long.valueOf(pl.getAgreementStartDate())) {
                    if (Long.valueOf(pl.getAgreementEndDate()) < 0) {
                        throw new CustomException("agreementEndDate", "Enter a valid Agreement End Date");
                    }
					String agreementStartDate = convertEpochtoDate(pl.getAgreementStartDate());
					String agreementEndDate = convertEpochtoDate(pl.getAgreementEndDate());
                    errors.addDataError(ErrorCode.DATE1_GE_DATE2.getCode(), "agreementStartDate", "agreementEndDate", agreementStartDate,agreementEndDate );
                }

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

                if (null != pl.getSupplier() && !isEmpty(pl.getSupplier().getCode())) {
                    SupplierGetRequest supplierGetRequest = new SupplierGetRequest();
                    supplierGetRequest.setCode(Collections.singletonList(pl.getSupplier().getCode()));
                    supplierGetRequest.setTenantId(pl.getTenantId());
                    Pagination<Supplier> supplierPagination = supplierJdbcRepository.search(supplierGetRequest);
                    if (!(supplierPagination.getPagedData().size() > 0)) {
                        throw new CustomException("supplier", "Supplier " + pl.getSupplier().getCode() + " doesn't exists");
                    }
                }

                for (PriceListDetails priceListDetail : pl.getPriceListDetails()) {
                    if (priceListDetail.getQuantity() != null)
                        if (priceListDetail.getQuantity().doubleValue() < 0) {
                            errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "quantity", priceListDetail.getQuantity().toString());
                        }
                    if (pl.getRateType().name().equals(PriceList.RateTypeEnum.ONE_TIME_TENDER.name())) {
                        if (priceListDetail.getQuantity() == null) {
                            errors.addDataError(ErrorCode.QUANTITY_GT_ZERO_TENDER.getCode(), "quantity", priceListDetail.getQuantity().toString());
                        }
                    }
                }
            }

            if (priceListJdbcRepository.isDuplicateContract(priceLists, method)) {
                throw new CustomException("inv.0011", "A ratecontract already exists in the system for the given material in the specified time duration. Please select alternate duration for the contract.");
            }

        } catch (IllegalArgumentException e) {

        }
    }
    private String convertEpochtoDate(Long date)
	 {
		 Date epoch = new Date(date);
		 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		 String s2 = format.format(epoch);
		 return s2;
	 }

}
