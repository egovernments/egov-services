package org.egov.collection.web.contract;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Employee {

    private Long id;

    private String code;

    private String name;

    private String userName;

    private List<Assignment> assignments = new ArrayList<>();
}
