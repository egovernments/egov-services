package org.egov.inv.domain.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Material {

    private String id;

    private String code;

    @NotNull
    @Size(min = 5, max = 50)
    @Pattern(regexp="^[A-Za-z0-9]+$")
    private String name;

    @NotNull
    @Size(max = 1024)
    @Pattern(regexp="^[A-Za-z0-9]+$")
    private String description;

    @Size(max = 50)
    @Pattern(regexp="^[A-Za-z0-9]+$")
    private String oldCode;

    @NotNull
    private MaterialType materialType;

    @NotNull
    private Uom baseUom;

    private InventoryType inventoryType;

    private Status status;

    @NotNull
    private Uom purchaseUom;

    private ChartOfAccount expenseAccount;

    @NotNull
    private Long minQuality;

    @NotNull
    private Long maxQuality;

    @NotNull
    private Uom stockingUom;

    @NotNull
    private MaterialClass materialClass;

    @NotNull
    private Long reorderLevel;

    @NotNull
    private Long reorderQuantity;

    @NotNull
    private MaterialControlType materialControlType;

    private String model;

    private String manufacturePartNo;

    private String technicalSpecs;

    private String termsOfDelivery;

    private boolean overrideMaterialControlType;

    private AuditDetails auditDetails;

}
