package org.egov.eis.web.contract;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@Setter
public class Attribute {

    private Boolean variable;

    private String code;

    private String datatype;

    private Boolean required;

    private String datatypeDescription;

    private List<Value> values;

}