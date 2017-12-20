package org.egov.inv.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreGetRequest {

    private List<String> id;

    private List<String> code;

    private String name;

    private String searchPurpose;

    private String description;

    private String department;

    private String contactNo1;

    private String officelocation;

    private String billingAddress;

    private String deliveryAddress;

    private String contactNo2;

    private String email;

    private Boolean isCentralStore;

    private String storeInCharge;

    private Boolean active;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    @NotNull
    private String tenantId;
}
