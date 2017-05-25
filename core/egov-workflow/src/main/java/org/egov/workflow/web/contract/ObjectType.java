package org.egov.workflow.web.contract;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectType {

    private Long id;

    private String type;

    private String description;

    private String tenantId;

}