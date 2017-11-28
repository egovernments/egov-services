package org.egov.inv.domain.service;

import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.model.*;
import org.egov.inv.persistence.repository.MaterialReceiptDetailJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class MaterialReceiptDetailService extends DomainService {

    @Autowired
    private MaterialReceiptDetailJdbcRepository materialReceiptDetailJdbcRepository;

    @Autowired
    private MaterialReceiptDetailAddInfoService materialReceiptDetailAddInfoService;

    public Pagination<MaterialReceiptDetail> search(MaterialReceiptDetailSearch materialReceiptDetailSearch) {
        Pagination<MaterialReceiptDetail> materialReceiptDetailPagination = materialReceiptDetailJdbcRepository.search(materialReceiptDetailSearch);

        List<MaterialReceiptDetail> receiptDetails = materialReceiptDetailPagination.getPagedData();

        if (receiptDetails.size() > 0) {
            for (MaterialReceiptDetail materialReceiptDetail : receiptDetails) {
                Uom uom = getUom(materialReceiptDetail.getTenantId(), materialReceiptDetail.getUom().getCode(), new RequestInfo());

                if (null != materialReceiptDetail.getReceivedQty() && null != uom.getConversionFactor()) {
                    Double receivedQuantity = getSearchConvertedQuantity(materialReceiptDetail.getReceivedQty().doubleValue(), uom.getConversionFactor().doubleValue());
                    materialReceiptDetail.setReceivedQty(BigDecimal.valueOf(receivedQuantity));
                }

                if (null != materialReceiptDetail.getAcceptedQty() && null != uom.getConversionFactor()) {
                    Double acceptedQuantity = getSearchConvertedQuantity(materialReceiptDetail.getAcceptedQty().doubleValue(), uom.getConversionFactor().doubleValue());
                    materialReceiptDetail.setAcceptedQty(BigDecimal.valueOf(acceptedQuantity));
                }
                MaterialReceiptAddInfoSearch materialReceiptAddInfoSearch = MaterialReceiptAddInfoSearch.builder()
                        .receiptDetailId(Arrays.asList(materialReceiptDetail.getId()))
                        .tenantId(materialReceiptDetailSearch.getTenantId())
                        .build();
                Pagination<MaterialReceiptDetailAddnlinfo> detailAddnlInfoPagination = materialReceiptDetailAddInfoService.search(materialReceiptAddInfoSearch);
                materialReceiptDetail.setReceiptDetailsAddnInfo(detailAddnlInfoPagination.getPagedData());
            }
        }

        return materialReceiptDetailPagination;
    }
}
