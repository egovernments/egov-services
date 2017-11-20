package org.egov.inv.persistence.entity;

import lombok.*;
import org.egov.inv.model.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MaterialReceiptDetailEntity {

    private String id;

    private String tenantid;

    private String material;

    private String mrnnumber;

    private String batchno;

    private String uomno;

    private String ordernumber;

    private String podetailid;

    private BigDecimal receivedqty;

    private BigDecimal acceptedqty;

    private BigDecimal unitrate;

    private String asset;

    private String voucherheader;

    private String rejectionremark;

    private Boolean isscrapitem;

    private String remarks;

    private String createdby;

    private Long createdtime;

    private String lastmodifiedby;

    private Long lastmodifiedtime;

    public MaterialReceiptDetail toDomain() {
        MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail();
        return materialReceiptDetail
                .id(id)
                .tenantId(tenantid)
                .material(buildMaterial())
                .uom(buildUom())
                .orderNumber(null != ordernumber ? new BigDecimal(ordernumber) : null)
                .purchaseOrderDetail(buildPurchaseOrderDetail())
                .receivedQty(receivedqty)
                .acceptedQty(acceptedqty)
                .unitRate(unitrate)
                .asset(buildAsset())
                .voucherHeader(voucherheader)
                .rejectionRemark(rejectionremark)
                .isScrapItem(isscrapitem)
                .remarks(remarks)
                .batchNo(batchno);
    }

    private Material buildMaterial() {
        Material material = new Material();
        return material.code(this.material);
    }

    private Uom buildUom() {
        Uom uom = new Uom();
        return uom.code(uomno);
    }

    private PurchaseOrderDetail buildPurchaseOrderDetail() {
        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
        return purchaseOrderDetail.id(podetailid);
    }

    private Asset buildAsset() {
        Asset asset = new Asset();
        return asset.code(this.asset);
    }
}
