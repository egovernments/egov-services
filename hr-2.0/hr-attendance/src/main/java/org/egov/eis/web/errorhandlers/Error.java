package org.egov.eis.web.errorhandlers;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Error {

    private Integer code = null;
    private String message = null;
    private String description = null;
    private List<Object> fields = new ArrayList<Object>();
}
