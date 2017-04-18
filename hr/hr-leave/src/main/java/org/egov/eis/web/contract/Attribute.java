package org.egov.eis.web.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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