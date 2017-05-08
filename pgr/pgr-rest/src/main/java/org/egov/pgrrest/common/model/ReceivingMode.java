package org.egov.pgrrest.common.model;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReceivingMode {

    private Long id;
    private String name;
    private String code;
    private boolean visible;
    private String tenantId;


}
