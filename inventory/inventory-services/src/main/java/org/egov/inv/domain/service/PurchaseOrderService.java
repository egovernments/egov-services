package org.egov.inv.domain.service;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.model.PurchaseOrder.PurchaseTypeEnum;
import org.egov.inv.model.PurchaseOrder.StatusEnum;
import org.egov.inv.persistence.repository.MaterialReceiptJdbcRepository;
import org.egov.inv.persistence.repository.PriceListJdbcRepository;
import org.egov.inv.persistence.repository.PurchaseOrderJdbcRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PurchaseOrderService extends DomainService {

    @Autowired
    private PurchaseOrderJdbcRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @Autowired
    private PriceListJdbcRepository priceListjdbcRepository;

    @Autowired
    private IndentService indentService;

    @Autowired
    private PriceListService priceListService;

    @Autowired
    private UomService uomService;

    @Value("${inv.purchaseorders.save.topic}")
    private String saveTopic;

    @Value("${inv.pricelists.podetail.required}")
    private Boolean priceListConfig;

    @Value("${inv.purchaseorders.save.key}")
    private String saveKey;

    @Value("${inv.purchaseorders.update.topic}")
    private String updateTopic;

    @Value("${inv.purchaseorders.update.key}")
    private String updateKey;

    @Value("${inv.purchaseorders.nonindent.save.topic}")
    private String saveNonIndentTopic;

    @Value("${inv.purchaseorders.nonindent.save.key}")
    private String saveNonIndentKey;

    @Value("${inv.purchaseorders.nonindent.update.topic}")
    private String updateNonIndentTopic;

    @Value("${inv.purchaseorders.nonindent.update.key}")
    private String updateNonIndentKey;

    @Value("${usetotal.indent.quantity}")
    private boolean isTotalIndentQuantityUsedInPo;

    @Autowired
    private MaterialReceiptJdbcRepository materialJdbcRepository;

    @Autowired
    private MdmsRepository mdmsRepository;

    private String INDENT_MULTIPLE = "Multiple";

    @Transactional
    public PurchaseOrderResponse create(PurchaseOrderRequest purchaseOrderRequest, String tenantId) {

        try {
            List<PurchaseOrder> purchaseOrders = purchaseOrderRequest.getPurchaseOrders();
            InvalidDataException errors = new InvalidDataException();
            validate(purchaseOrders, Constants.ACTION_CREATE, tenantId);
            List<String> sequenceNos = purchaseOrderRepository.getSequence(PurchaseOrder.class.getSimpleName(), purchaseOrders.size());
            int i = 0;
            for (PurchaseOrder purchaseOrder : purchaseOrders) {

                if (purchaseOrder.getAdvanceAmount() != null) {
                    if (purchaseOrder.getAdvanceAmount().compareTo(purchaseOrder.getTotalAmount()) > 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), purchaseOrder.getAdvanceAmount() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }
                }

                if (purchaseOrder.getAdvanceAmount() != null && purchaseOrder.getAdvancePercentage() != null) {

                    if (purchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 1) {
                        errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), purchaseOrder.getAdvancePercentage() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }

                    if (purchaseOrder.getAdvanceAmount().compareTo(purchaseOrder.getTotalAmount()) > 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), purchaseOrder.getAdvanceAmount() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }

                    if ((purchaseOrder.getTotalAmount().multiply(purchaseOrder.getAdvancePercentage())).divide(new BigDecimal(100), RoundingMode.FLOOR).compareTo(purchaseOrder.getAdvanceAmount().divide(new BigDecimal(1), RoundingMode.FLOOR)) < 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), purchaseOrder.getAdvancePercentage() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }

                }

                purchaseOrder.setStatus(StatusEnum.APPROVED);
                String purchaseOrderNumber = appendString(purchaseOrder);
                purchaseOrder.setId(sequenceNos.get(i));

                if (purchaseOrders.size() > 0 && purchaseOrders.get(0).getPurchaseType() != null) {
                    purchaseOrder.setPurchaseType(purchaseOrders.get(0).getPurchaseType());
                } else
                    purchaseOrder.setPurchaseType(PurchaseTypeEnum.INDENT);

                //TODO: move to id-gen with format <ULB short code>/<Store Code>/<fin. Year>/<serial No.>
                purchaseOrder.setPurchaseOrderNumber(purchaseOrderNumber);
                i++;
                int j = 0;
                purchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
                List<String> detailSequenceNos = purchaseOrderRepository.getSequence(PurchaseOrderDetail.class.getSimpleName(), purchaseOrder.getPurchaseOrderDetails().size());

                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                    purchaseOrderDetail.setId(detailSequenceNos.get(j));
                    purchaseOrderDetail.setTenantId(purchaseOrder.getTenantId());

                    if (purchaseOrderDetail.getPurchaseIndentDetails() != null
                            && purchaseOrderDetail.getPurchaseIndentDetails().size() > 0) {
                        int k = 0;
                        List<String> poIndentDetailSequenceNos = purchaseOrderRepository.getSequence(
                                PurchaseIndentDetail.class.getSimpleName(),
                                purchaseOrderDetail.getPurchaseIndentDetails().size());

                        // Order quantity must be less than (tenderQuantity - usedQuantity) in case of tender
                        if(null != purchaseOrder.getRateType() && purchaseOrder.getRateType().name().equals("One Time Tender"))
                        if(null != purchaseOrderDetail.getOrderQuantity() && purchaseOrderDetail.getOrderQuantity().compareTo(purchaseOrderDetail.getTenderQuantity().subtract(purchaseOrderDetail.getUsedQuantity())) > 0) {
                        	errors.addDataError(ErrorCode.QTY_LE_SCND_ROW.getCode(), " at serial no." + purchaseOrder.getPurchaseOrderDetails().indexOf(purchaseOrderDetail));
                        }
                        
                        for (PurchaseIndentDetail purchaseIndentDetail : purchaseOrderDetail
                                .getPurchaseIndentDetails()) {
                            purchaseIndentDetail.setId(poIndentDetailSequenceNos.get(k));
                            purchaseIndentDetail.setTenantId(purchaseOrder.getTenantId());
                            k++;
                        }

                    }

                    Object uom = mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code", purchaseOrderDetail.getUom().getCode(), Uom.class);
                    purchaseOrderDetail.setUom((Uom) uom);
                    // purchaseOrderDetail.setUom(uomService.getUom(purchaseOrderDetail.getTenantId(), purchaseOrderDetail.getUom().getCode(),new RequestInfo()));
                    if (purchaseOrderDetail.getOrderQuantity() != null) {
                        purchaseOrderDetail.setOrderQuantity(purchaseOrderDetail.getOrderQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getIndentQuantity() != null) {
                        purchaseOrderDetail.setIndentQuantity(purchaseOrderDetail.getIndentQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getTenderQuantity() != null) {
                        purchaseOrderDetail.setTenderQuantity(purchaseOrderDetail.getTenderQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getUsedQuantity() != null) {
                        purchaseOrderDetail.setUsedQuantity(purchaseOrderDetail.getUsedQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getUnitPrice() != null) {
                        purchaseOrderDetail.setUnitPrice(purchaseOrderDetail.getUnitPrice().divide(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    j++;
                }

            }

            // TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
            // PUSH DATA TO KAFKA.

            if (purchaseOrders.size() > 0 && purchaseOrders.get(0).getPurchaseType() != null) {
                if (purchaseOrders.get(0).getPurchaseType().toString()
                        .equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
                    kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
                else
                    kafkaQue.send(saveNonIndentTopic, saveNonIndentKey, purchaseOrderRequest);
            } else { //TODO: REMOVE BELOW, IF PURCHASE TYPE IS PROPER. oTHER WISE BY DEFAULT PASSING TO INDENT PURCHASE.
                kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
            }
            PurchaseOrderResponse response = new PurchaseOrderResponse();
            response.setPurchaseOrders(purchaseOrderRequest.getPurchaseOrders());
            response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }

    }

    @Transactional
    public PurchaseOrderResponse update(PurchaseOrderRequest purchaseOrderRequest, String tenantId) {

        try {
            List<PurchaseOrder> purchaseOrder = purchaseOrderRequest.getPurchaseOrders();
            validate(purchaseOrder, Constants.ACTION_UPDATE, tenantId);

            for (PurchaseOrder eachPurchaseOrder : purchaseOrder) {

                InvalidDataException errors = new InvalidDataException();
                if (eachPurchaseOrder.getAdvanceAmount() != null) {
                    if (eachPurchaseOrder.getAdvanceAmount().compareTo(eachPurchaseOrder.getTotalAmount()) > 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), eachPurchaseOrder.getAdvanceAmount() + " at serial no." + (purchaseOrder.indexOf(eachPurchaseOrder) + 1));
                    }
                }

                BigDecimal totalAmount = new BigDecimal(0);
                eachPurchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_UPDATE));

                for (PurchaseOrderDetail eachPurchaseOrderDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
                    eachPurchaseOrderDetail.setUom(uomService.getUom(eachPurchaseOrderDetail.getTenantId(), eachPurchaseOrderDetail.getUom().getCode(), new RequestInfo()));
                    if (eachPurchaseOrderDetail.getOrderQuantity() != null) {
                        eachPurchaseOrderDetail.setOrderQuantity(eachPurchaseOrderDetail.getOrderQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (eachPurchaseOrderDetail.getIndentQuantity() != null) {
                        eachPurchaseOrderDetail.setIndentQuantity(eachPurchaseOrderDetail.getIndentQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (eachPurchaseOrderDetail.getTenderQuantity() != null) {
                        eachPurchaseOrderDetail.setTenderQuantity(eachPurchaseOrderDetail.getTenderQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (eachPurchaseOrderDetail.getUsedQuantity() != null) {
                        eachPurchaseOrderDetail.setUsedQuantity(eachPurchaseOrderDetail.getUsedQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (eachPurchaseOrderDetail.getUnitPrice() != null) {
                        eachPurchaseOrderDetail.setUnitPrice(eachPurchaseOrderDetail.getUnitPrice().divide(eachPurchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    totalAmount = totalAmount.add(eachPurchaseOrderDetail.getOrderQuantity().multiply(eachPurchaseOrderDetail.getUnitPrice()).add(totalAmount));
                }
                eachPurchaseOrder.setTotalAmount(totalAmount);
            }

            // TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
            // PUSH DATA TO KAFKA.

            if (purchaseOrder.size() > 0 && purchaseOrder.get(0).getPurchaseType() != null) {
                if (purchaseOrder.get(0).getPurchaseType().toString()
                        .equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
                    kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest);
                else
                    kafkaQue.send(updateNonIndentTopic, updateNonIndentKey, purchaseOrderRequest);
            } else
                kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest); //TODO REMOVE THIS IF PUCHASE TYPE PRESENT.

            PurchaseOrderResponse response = new PurchaseOrderResponse();
            response.setPurchaseOrders(purchaseOrderRequest.getPurchaseOrders());
            response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }

    }


    public PurchaseOrderResponse search(PurchaseOrderSearch is) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        Pagination<PurchaseOrder> search = purchaseOrderRepository.search(is);

        if (search.getPagedData().size() > 0) {
            for (PurchaseOrder purchaseOrder : search.getPagedData()) {
                PurchaseOrderDetailSearch purchaseOrderDetailSearch = new PurchaseOrderDetailSearch();
                purchaseOrderDetailSearch.setPurchaseOrder(purchaseOrder.getPurchaseOrderNumber());
                purchaseOrderDetailSearch.setTenantId(purchaseOrder.getTenantId());
                Pagination<PurchaseOrderDetail> detailPagination = purchaseOrderDetailService.search(purchaseOrderDetailSearch);
                purchaseOrder.setPurchaseOrderDetails(detailPagination.getPagedData().size() > 0 ? detailPagination.getPagedData() : Collections.EMPTY_LIST);
            }
        }

        response.setPurchaseOrders(search.getPagedData());
        response.setPage(getPage(search));
        return response;

    }

    private void validate(List<PurchaseOrder> pos, String method, String tenantId) {
        InvalidDataException errors = new InvalidDataException();

        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (pos == null) {
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrders", null);
                    }
                }
                break;

                case Constants.ACTION_UPDATE: {
                    if (pos == null) {
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrders", null);
                    }
                }
                break;

            }

            Long currentMilllis = System.currentTimeMillis();

            for (PurchaseOrder eachPurchaseOrder : pos) {
                BigDecimal totalAmount = new BigDecimal(0);
                int index = pos.indexOf(eachPurchaseOrder) + 1;
                if (eachPurchaseOrder.getPurchaseOrderDate() > currentMilllis) {
                    errors.addDataError(ErrorCode.PO_DATE_LE_TODAY.getCode(), eachPurchaseOrder.getPurchaseOrderDate().toString() + " at serial no." + index);
                }
                if (null != eachPurchaseOrder.getExpectedDeliveryDate()) {
                    if (eachPurchaseOrder.getExpectedDeliveryDate() < eachPurchaseOrder.getPurchaseOrderDate()) {
                        errors.addDataError(ErrorCode.EXP_DATE_GE_PODATE.getCode(), eachPurchaseOrder.getExpectedDeliveryDate().toString() + " at serial no." + index);
                    }
                }

                if (eachPurchaseOrder.getAdvanceAmount() != null && eachPurchaseOrder.getAdvancePercentage() != null) {

                    if (eachPurchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 1) {
                        errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), eachPurchaseOrder.getAdvancePercentage() + " at serial no." + (pos.indexOf(eachPurchaseOrder) + 1));
                    }

                } else if (eachPurchaseOrder.getAdvancePercentage() != null) {
                    if (eachPurchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 1) {
                        errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), eachPurchaseOrder.getAdvancePercentage() + " at serial no." + (pos.indexOf(eachPurchaseOrder) + 1));
                    }
                }

                String indentNumbers = "";
                for (PurchaseOrderDetail purchaseOrderDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
                    indentNumbers += purchaseOrderDetail.getIndentNumber() + ",";
                }
                indentNumbers.replaceAll(",$", "");

                IndentSearch is = IndentSearch.builder().ids(new ArrayList<String>(Arrays.asList(indentNumbers.split(",")))).tenantId(tenantId).build();
                IndentResponse isr = indentService.search(is, new RequestInfo());

                for (Indent in : isr.getIndents()) {
                    if (in.getIndentDate().compareTo(eachPurchaseOrder.getPurchaseOrderDate()) > 0) {
                        errors.addDataError(ErrorCode.DATE1_LE_DATE2.getCode(), eachPurchaseOrder.getPurchaseOrderDate().toString() + " at serial no." + pos.indexOf(eachPurchaseOrder));
                    }
                }

                if (null != eachPurchaseOrder.getPurchaseOrderDetails()) {
                    for (PurchaseOrderDetail poDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
                        int detailIndex = eachPurchaseOrder.getPurchaseOrderDetails().indexOf(poDetail) + 1;
                        if (null != poDetail.getMaterial() && StringUtils.isEmpty(poDetail.getMaterial().getCode())) {
                            errors.addDataError(ErrorCode.MAT_DETAIL.getCode(), " at serial no." + detailIndex);

                        }

                        if (priceListConfig && null == poDetail.getPriceList().getId()) {
                            errors.addDataError(ErrorCode.RATE_CONTRACT.getCode(), " at serial no." + detailIndex);

                        }

                        if (null != poDetail.getOrderQuantity() && null != poDetail.getIndentQuantity()) {
                            int res = poDetail.getOrderQuantity().compareTo(poDetail.getIndentQuantity());
                            if (res == 1) {
                                errors.addDataError(ErrorCode.ORDQTY_LE_INDQTY.getCode(), eachPurchaseOrder.getExpectedDeliveryDate().toString() + " at serial no." + detailIndex);
                            }
                        }
                        totalAmount = totalAmount.add(poDetail.getOrderQuantity().multiply(poDetail.getUnitPrice()).add(totalAmount));
                    }
                    eachPurchaseOrder.setTotalAmount(totalAmount);
                } else
                    errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrdersDetail", null);
            }


        } catch (IllegalArgumentException e) {

        }
        if (errors.getValidationErrors().size() > 0)
            throw errors;

    }


    // GET INDENT OBJECTS BY PASSING ID'S
    // Iterate through each indent
    // supplier and rate type is required. -- mandatory fields.?????
    // for each indent check available quantity to procure.COMBINE
    // MATERIALS WHICH ARE NOT ISSUED. Build indent detail list.
    // for each material, group together and build indent detail and po
    // link . Save indent number at po detail level. ?
    // for each material, check rate contract and amount detail by
    // supplier. If not found, skip that material.
    // Get rate contract details for each line
    // Tender quantity and used quantity should be required.
    // Show unit rate based on rate contract.
    // Get available quantity for each material- ?
    // Send indent detail list for each row of po line.
    // Build Po reponse object and send back.

    public PurchaseOrderResponse preparePoFromIndents(PurchaseOrderRequest purchaseOrderRequest, String tenantId) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));

        List<PurchaseOrder> purchaseOrders = purchaseOrderRequest.getPurchaseOrders();
        validate(purchaseOrders, Constants.ACTION_SEARCH_INDENT_FOR_PO, tenantId);
        List<PurchaseOrder> finalPurchaseOrders = new ArrayList<PurchaseOrder>();

        for (PurchaseOrder purchaseOrder : purchaseOrders) {
        	InvalidDataException errors = new InvalidDataException();
            // TODO: validation of supplier, rate type and indents.

            IndentSearch indentSearch = new IndentSearch();
            indentSearch.setIds(purchaseOrder.getIndentNumbers());
            indentSearch.setTenantId(tenantId);
            IndentResponse indentResponse = indentService.search(indentSearch, purchaseOrderRequest.getRequestInfo());

            Map<String, PurchaseOrderDetail> purchaseOrderLines = new HashMap<String, PurchaseOrderDetail>();

            // configure to check whether full quantity of indent to be consider or quantity pending ( out of issue) used to create po.

            // convert all items into purchase uom by referring each indent.

            for (Indent indent : indentResponse.getIndents()) {

                for (IndentDetail indentDetail : indent.getIndentDetails()) {
                    PurchaseOrderDetail purchaseOrderDetail = null;
                    BigDecimal pendingQty = BigDecimal.ZERO;

                    // Consider full indent quantity to be used in
                    // poorderquantity
                    if (!isTotalIndentQuantityUsedInPo) {
                        if (indentDetail != null && indentDetail.getIndentQuantity()
                                .compareTo(indentDetail.getPoOrderedQuantity() != null
                                        ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO) > 0) {
                            pendingQty = indentDetail.getIndentQuantity()
                                    .subtract(indentDetail.getPoOrderedQuantity() != null
                                            ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO);

                        }
                    } else {

                        if (indentDetail != null && (indentDetail.getIndentQuantity()
                                .subtract(indentDetail.getTotalProcessedQuantity() != null
                                        ? indentDetail.getTotalProcessedQuantity() : BigDecimal.ZERO)
                                .compareTo(indentDetail.getPoOrderedQuantity() != null
                                        ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO) > 0)) {
                            pendingQty = indentDetail.getIndentQuantity()
                                    .subtract(indentDetail.getTotalProcessedQuantity() != null
                                            ? indentDetail.getTotalProcessedQuantity() : BigDecimal.ZERO)
                                    .subtract(indentDetail.getPoOrderedQuantity() != null
                                            ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO);

                        }

                    }

                    PriceListSearchRequest priceListSearchRequest = new PriceListSearchRequest();
                    priceListSearchRequest.setTenantId(tenantId);
                    priceListSearchRequest.setSupplierName(purchaseOrder.getSupplier().getCode());
                    priceListSearchRequest.setRateType(purchaseOrder.getRateType().toString());
                    priceListSearchRequest.setRateContractDate(System.currentTimeMillis());

                    priceListSearchRequest.setActive(true);

                    // write api to get price list by passing material,supplier, rate type, active one and with current date.

                    PriceListResponse priceListResponse = priceListService.searchPriceList(priceListSearchRequest,
                            purchaseOrderRequest.getRequestInfo());

                    // get used quantity for each tender type rate types and used ones.

                    // save indent number, tender used quantity, total indent  qty?

                    if (purchaseOrderLines.get(indentDetail.getMaterial().getCode()) == null) {
                        purchaseOrderDetail = new PurchaseOrderDetail();
                        purchaseOrderDetail.setMaterial(indentDetail.getMaterial());
                        purchaseOrderDetail.setUom(indentDetail.getMaterial().getPurchaseUom());
                        purchaseOrderDetail.setIndentQuantity(pendingQty);
                        if (null != purchaseOrder.getRateType() && purchaseOrder.getRateType().name().equals("One Time Tender")) {
                            purchaseOrderDetail.setTenderQuantity(new BigDecimal(priceListjdbcRepository.getTenderQty(purchaseOrder.getSupplier().getCode(), indentDetail.getMaterial().getCode(), purchaseOrder.getRateType().name())));
                        }
                        purchaseOrderDetail.setUsedQuantity(new BigDecimal(purchaseOrderRepository.getUsedQty(purchaseOrder.getSupplier().getCode(), indentDetail.getMaterial().getCode(), purchaseOrder.getRateType().name())));
                        purchaseOrderDetail.setIndentNumber(indent.getIndentNumber());
                        purchaseOrderDetail.setTenantId(tenantId);
                        buildPurchaseOrderIndentDetail(purchaseOrderRequest, purchaseOrder, indentDetail,
                                purchaseOrderDetail, pendingQty, tenantId);

                    } else {
                        purchaseOrderDetail = purchaseOrderLines.get(indentDetail.getMaterial().getCode());
                        purchaseOrderDetail.setIndentQuantity(purchaseOrderDetail.getIndentQuantity().add(pendingQty));
                        purchaseOrderDetail.setIndentNumber(INDENT_MULTIPLE);
                        buildPurchaseOrderIndentDetail(purchaseOrderRequest, purchaseOrder, indentDetail,
                                purchaseOrderDetail, pendingQty, tenantId);

                    }

                    if (priceListResponse != null && priceListResponse.getPriceLists() != null && priceListResponse.getPriceLists().size() > 0)
                        purchaseOrderDetail.setPriceList(priceListResponse.getPriceLists().get(0));//TODO: SHOULD SUPPORT MULTIPLE PRICE LIST.

                    purchaseOrder.addPurchaseOrderDetailsItem(purchaseOrderDetail);

                }

            }

            finalPurchaseOrders.add(purchaseOrder);
        }

        response.setPurchaseOrders(finalPurchaseOrders);

        return response;
    }

    public Boolean checkAllItemsSuppliedForPo(PurchaseOrderSearch purchaseOrderSearch) {

        Boolean flag = true;

        PurchaseOrderResponse search = search(purchaseOrderSearch);

        if (search.getPurchaseOrders().size() > 0) {
            List<PurchaseOrder> purchaseOrders = search.getPurchaseOrders();

            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                    if (purchaseOrderDetail.getOrderQuantity().compareTo(purchaseOrderDetail.getReceivedQuantity()) > 0) {
                        return false;
                    }
                }
            }
        } else
            throw new CustomException("po", "Purchase order not found");

        return flag;
    }


    public PurchaseOrderResponse getPOPendingToDeliver(PurchaseOrderSearch purchaseOrderSearch) {

        PurchaseOrderResponse search = search(purchaseOrderSearch);

        if (search.getPurchaseOrders().size() > 0) {
            List<PurchaseOrder> purchaseOrders = search.getPurchaseOrders();

            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                    if (purchaseOrderDetail.getOrderQuantity().compareTo(purchaseOrderDetail.getReceivedQuantity()) == 0) {
                        purchaseOrder.getPurchaseOrderDetails().remove(purchaseOrderDetail);
                    }
                }
            }
        } else
            throw new CustomException("po", "Purchase order not found");

        return search;
    }


    private void buildPurchaseOrderIndentDetail(PurchaseOrderRequest purchaseOrderRequest, PurchaseOrder purchaseOrder,
                                                IndentDetail indentDetail, PurchaseOrderDetail purchaseOrderDetail, BigDecimal pendingQty, String tenantId) {
        PurchaseIndentDetail poIndentDetail = new PurchaseIndentDetail();
        poIndentDetail.setTenantId(tenantId);
        poIndentDetail.setAuditDetails(
                getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
        poIndentDetail.setQuantity(pendingQty);
        poIndentDetail.setIndentDetail(indentDetail);
        purchaseOrderDetail.addPurchaseIndentDetailsItem(poIndentDetail);
    }

    private String appendString(PurchaseOrder poOrder) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String code = "PO/";
        int id = Integer.valueOf(materialJdbcRepository.getSequence(poOrder));
        String idgen = String.format("%05d", id);
        String purchaseOrderNumber = code + idgen + "/" + year;
        return purchaseOrderNumber;
    }

}