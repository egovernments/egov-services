package io.swagger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MaterialSearchRequest {

    private String tenantId;

    private List<String> ids;

    private String code;

    private String name;

    private String store;

    private String description;

    private String oldCode;

    private String materialType;

    private String inventoryType;

    private String status;

    private String materialClass;

    private String materialControlType;

    private String model;

    private String manufacturePartNo;

    private Integer pageSize;

    private Integer offSet;

    private String sortBy;

}
