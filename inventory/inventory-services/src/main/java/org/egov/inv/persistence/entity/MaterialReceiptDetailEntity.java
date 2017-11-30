package org.egov.inv.persistence.entity;

import lombok.*;
import org.egov.inv.model.*;

import java.math.BigDecimal;

import static org.springframework.util.StringUtils.isEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MaterialReceiptDetailEntity {

    private String id;

    private String tenantId;

    private String material;

    private String mrnNumber;

    private String uomNo;

    private String orderNumber;

    private String poDetailId;

    private BigDecimal receivedQty;

    private BigDecimal acceptedQty;

    private BigDecimal unitRate;

    private String asset;

    private String voucherHeader;

    private String rejectionRemark;

    private Boolean isScrapItem;

    private String remarks;

    private String createdBy;

    private Long createdTime;

    private String lastModifiedBy;

    private Long lastModifiedTime;

    public MaterialReceiptDetail toDomain() {
        MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail();
        return materialReceiptDetail
                .id(id)
                .tenantId(tenantId)
                .material(buildMaterial())
                .uom(buildUom())
                .orderNumber(null != orderNumber ? new BigDecimal(orderNumber) : null)
                .mrnNumber(!isEmpty(mrnNumber) ? mrnNumber : null)
                .purchaseOrderDetail(buildPurchaseOrderDetail())
                .receivedQty(receivedQty)
                .acceptedQty(acceptedQty)
                .unitRate(unitRate)
                .asset(!isEmpty(asset) ? buildAsset() : null)
                .voucherHeader(voucherHeader)
                .rejectionRemark(rejectionRemark)
                .isScrapItem(isScrapItem)
                .remarks(remarks);
    }

    private Material buildMaterial() {
        Material material = new Material();
        return material.code(this.material);
    }

    private Uom buildUom() {
        Uom uom = new Uom();
        return uom.code(uomNo);
    }

    private PurchaseOrderDetail buildPurchaseOrderDetail() {
        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
        return purchaseOrderDetail.id(poDetailId);
    }

    private Asset buildAsset() {
        Asset asset = new Asset();
        return asset.code(this.asset);
    }
}
