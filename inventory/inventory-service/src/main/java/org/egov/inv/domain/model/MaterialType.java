package org.egov.inv.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialType {

    private String id ;

    private String name ;

    private String code ;

    private String parent ;

}
