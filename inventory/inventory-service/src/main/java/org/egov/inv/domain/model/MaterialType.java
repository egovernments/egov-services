package org.egov.inv.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialType {

    private String id ;

    private String name ;

    private String code ;

    private String parent ;

}
