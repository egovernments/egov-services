package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.Indent;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.IndentResponse;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.model.PriceListResponse;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.model.PurchaseIndentDetail;
import org.egov.inv.model.PurchaseOrder;
import org.egov.inv.model.PurchaseOrder.PurchaseTypeEnum;
import org.egov.inv.model.PurchaseOrderDetail;
import org.egov.inv.model.PurchaseOrderDetailSearch;
import org.egov.inv.model.PurchaseOrderRequest;
import org.egov.inv.model.PurchaseOrderResponse;
import org.egov.inv.model.PurchaseOrderSearch;
import org.egov.inv.persistence.repository.PurchaseOrderJdbcRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PurchaseOrderService extends DomainService {

    @Autowired
    private PurchaseOrderJdbcRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @Autowired
    private IndentService indentService;

    @Autowired
    private PriceListService priceListService;

    @Value("${inv.purchaseorders.save.topic}")
    private String saveTopic;

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

    private String INDENT_MULTIPLE = "Multiple";

    @Transactional
    public PurchaseOrderResponse create(PurchaseOrderRequest purchaseOrderRequest) {

        try {
            List<PurchaseOrder> purchaseOrders = purchaseOrderRequest.getPurchaseOrders();
            validate(purchaseOrders, Constants.ACTION_CREATE);
            List<String> sequenceNos = purchaseOrderRepository.getSequence(PurchaseOrder.class.getSimpleName(), purchaseOrders.size());
            int i = 0;
            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                purchaseOrder.setId(sequenceNos.get(i));

                if (purchaseOrders.size() > 0 && purchaseOrders.get(0).getPurchaseType() != null) {
                    purchaseOrder.setPurchaseType(purchaseOrders.get(0).getPurchaseType());
                } else
                    purchaseOrder.setPurchaseType(PurchaseTypeEnum.INDENT);

                //TODO: move to id-gen with format <ULB short code>/<Store Code>/<fin. Year>/<serial No.>
                purchaseOrder.setPurchaseOrderNumber(sequenceNos.get(i));
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

                        for (PurchaseIndentDetail purchaseIndentDetail : purchaseOrderDetail
                                .getPurchaseIndentDetails()) {
                            purchaseIndentDetail.setId(poIndentDetailSequenceNos.get(k));
                            purchaseIndentDetail.setTenantId(purchaseOrder.getTenantId());
                            k++;
                        }

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
    public PurchaseOrderResponse update(PurchaseOrderRequest purchaseOrderRequest) {

        try {
            List<PurchaseOrder> purchaseOrder = purchaseOrderRequest.getPurchaseOrders();
            validate(purchaseOrder, Constants.ACTION_UPDATE);

            for (PurchaseOrder eachPurchaseOrder : purchaseOrder)
                eachPurchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_UPDATE));

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

    private void validate(List<PurchaseOrder> pos, String method) {
		InvalidDataException errors = new InvalidDataException();

        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (pos == null) {
                    	errors.addDataError(ErrorCode.NOT_NULL.getCode(),"purchaseOrders", null);
                    }
                }
                break;

                case Constants.ACTION_UPDATE: {
                    if (pos == null) {
                       errors.addDataError(ErrorCode.NOT_NULL.getCode(),"purchaseOrders",  null);
                    }
                }
                break;

            }
            
            Long currentMilllis = System.currentTimeMillis();
                        
            for(PurchaseOrder eachPurchaseOrder : pos){
            	if(eachPurchaseOrder.getPurchaseOrderDate() > currentMilllis){
            		errors.addDataError(ErrorCode.PO_DATE_LE_TODAY.getCode(), eachPurchaseOrder.getPurchaseOrderDate().toString());
                }
            	if(null != eachPurchaseOrder.getExpectedDeliveryDate()){
            	if(eachPurchaseOrder.getExpectedDeliveryDate()  < eachPurchaseOrder.getPurchaseOrderDate()){
            		errors.addDataError(ErrorCode.EXP_DATE_GE_PODATE.getCode(), eachPurchaseOrder.getExpectedDeliveryDate().toString());
                }
            	}
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
        validate(purchaseOrders, Constants.ACTION_SEARCH_INDENT_FOR_PO);
        List<PurchaseOrder> finalPurchaseOrders = new ArrayList<PurchaseOrder>();

        for (PurchaseOrder purchaseOrder : purchaseOrders) {

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

}