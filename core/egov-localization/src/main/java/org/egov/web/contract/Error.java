package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Error {
    private String code;
    private String field;
    private String message;
}

