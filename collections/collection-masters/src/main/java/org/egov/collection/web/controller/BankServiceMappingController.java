package org.egov.collection.web.controller;

import org.egov.collection.web.contract.BankAccountServiceMappingReq;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bankAccountServiceMapping/_create")
public class BankServiceMappingController {


    public String create(@ModelAttribute BankAccountServiceMappingReq bankAccountServiceMappingReq,@RequestBody RequestInfo requestInfo) {
        return null;
    }
}
